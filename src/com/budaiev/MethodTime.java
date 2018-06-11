package com.budaiev;

import java.sql.Timestamp;

public class MethodTime {

  private Timestamp entryTime;
  private Timestamp exitTime;

  public void setEntryTime(Timestamp entryTime) {
    this.entryTime = entryTime;
  }

  public void setExitTime(Timestamp exitTime) {
    this.exitTime = exitTime;
  }

  public boolean hasFullData() {
    return entryTime != null && exitTime != null;
  }

  public long getExecutionTime() {
    return exitTime.getTime() - entryTime.getTime();
  }
}
