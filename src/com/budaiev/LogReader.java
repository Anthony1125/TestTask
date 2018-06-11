package com.budaiev;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LogReader {

  private String uri;
  private Pattern pattern = Pattern.compile("(.*?)\\s(ALL|TRACE|DEBUG|INFO|WARN|ERROR|FATAL|OFF)\\s"
      + "\\[(.*?)]\\s(entry|exit)\\swith\\s\\((\\w*):(\\d*)\\)");

  public LogReader(String uri) {
    this.uri = uri;
  }

  public HashMap<String, MethodTime> read() throws IOException, UncheckedIOException {
    HashMap<String, MethodTime> hashMap = new HashMap<>();
    try (Stream<String> stringStream = Files.lines(Paths.get(uri), StandardCharsets.UTF_8)) {
      stringStream.forEach(s -> parseLogString(hashMap, s));
    }
    return hashMap;
  }

  private void parseLogString(HashMap<String, MethodTime> hashMap, String s) {
    Matcher matcher = pattern.matcher(s);
    if (!matcher.find()) {
      return;
    }
    Timestamp time;
    try {
      time = Timestamp.valueOf(matcher.group(1)
          .replace('T', ' ')
          .replace(',', '.'));
    } catch (IllegalArgumentException e) {
      return;
    }
    String key = matcher.group(3) + ":" + matcher.group(5) + "-" + matcher.group(6);
    if (hashMap.containsKey(key)) {
      hashMap.get(key).setExitTime(time);
    } else {
      MethodTime methodTime = new MethodTime();
      methodTime.setEntryTime(time);
      hashMap.put(key, methodTime);
    }
  }
}
