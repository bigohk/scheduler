package com.bigohk.scheduler.core;

import java.util.concurrent.TimeUnit;

public class Schedule {
  public static final Schedule ONE_TIME_ASAP = new Schedule(0L, 0, TimeUnit.NANOSECONDS);
  public static final Schedule NEVER         = new Schedule(0L, -1, TimeUnit.NANOSECONDS);

  private long     fixedDelay;
  private long     frequency;
  private TimeUnit timeUnit;
  private final String toStr;

  public Schedule(long fixedDelay, long frequency, TimeUnit timeUnit) {
    this.fixedDelay = fixedDelay;
    this.frequency = frequency;
    this.timeUnit = timeUnit;
    StringBuilder toStrBuilder = new StringBuilder();
    toStrBuilder.append("Schedule : fixedDelay=").append(fixedDelay).append(", frequency=").append(frequency).append(", timeUnit=").append(timeUnit.toString());
    toStr = toStrBuilder.toString();
  }

  public long getFixedDelay() {
    return fixedDelay;
  }

  public void setFixedDelay(long fixedDelay) {
    this.fixedDelay = fixedDelay;
  }

  public void setFrequency(long frequency) {
    this.frequency = frequency;
  }

  public long getFrequency() {
    return frequency;
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

  @Override
  public int hashCode() {
    //Not particularly thoughtful but will work for now
    return (int)((7*fixedDelay + 13*frequency + 31*timeUnit.ordinal()) >> 32) ;
  }

  @Override
  public boolean equals(Object otherObj) {
    if (!(otherObj instanceof Schedule))
      return false;

    Schedule other = (Schedule) otherObj;
    return fixedDelay == other.fixedDelay && frequency == other.frequency && timeUnit == other.timeUnit;
  }

  @Override
  public String toString() {
    return toStr;
  }
}

