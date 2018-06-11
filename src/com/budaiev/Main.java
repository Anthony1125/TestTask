package com.budaiev;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;

public class Main {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Invalid command line arguments. Enter path to log file.");
    }
    File file = new File(args[0]);
    if (!file.exists() || !file.isFile() || !file.canRead()) {
      System.out.println("Can not read file");
    }
    HashMap<String, MethodTime> hashMap;
    try {
      hashMap = new LogReader(args[0]).read();
    } catch (IOException | UncheckedIOException e) {
      System.out.println("Could not read data from file");
      return;
    }
    HashMap<String, MethodSummaryInfo> methodSummaryMap = processData(hashMap);
    print(methodSummaryMap);
  }

  private static HashMap<String, MethodSummaryInfo> processData(
      HashMap<String, MethodTime> hashMap) {
    HashMap<String, MethodSummaryInfo> methodSummaryMap = new HashMap<>();
    hashMap.forEach((s, methodTime) -> {
      String[] strs = s.split("-");
      String name = strs[0];
      String id = strs[1];
      if (methodSummaryMap.containsKey(name)) {
        methodSummaryMap.get(name).addMethodCall(methodTime, id);
      } else {
        MethodSummaryInfo methodSummaryInfo = new MethodSummaryInfo();
        methodSummaryInfo.addMethodCall(methodTime, id);
        methodSummaryMap.put(name, methodSummaryInfo);
      }
    });
    return methodSummaryMap;
  }

  private static void print(HashMap<String, MethodSummaryInfo> methodSummaryMap) {
    methodSummaryMap
        .forEach((s, methodSummaryInfo) -> System.out.printf("%30s %s%n", s, methodSummaryInfo));
  }
}
