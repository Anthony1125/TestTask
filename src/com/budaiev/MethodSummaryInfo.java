package com.budaiev;

public class MethodSummaryInfo {

  private String maxTimeId;
  private long minTime;
  private long maxTime;
  private long sumTime;
  private int count;

  public MethodSummaryInfo() {
    minTime = Long.MAX_VALUE;
  }

  public void addMethodCall(MethodTime methodTime, String id) {
    if (!methodTime.hasFullData()) {
      return;
    }
    long executionTime = methodTime.getExecutionTime();
    sumTime += executionTime;
    if (executionTime < minTime) {
      minTime = executionTime;
    }
    if (executionTime > maxTime) {
      maxTime = executionTime;
      maxTimeId = id;
    }
    count++;
  }

  private long getAverageTime() {
    return sumTime / count;
  }

  @Override
  public String toString() {
    return count > 0 ? String.format("min %10s, max %10s, avg %10s, max id %10s, count %10s",
        minTime, maxTime, getAverageTime(), maxTimeId, count)
        : "Not enough info about method calls";
  }
}
