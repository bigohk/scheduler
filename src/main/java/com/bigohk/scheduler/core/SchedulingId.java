package com.bigohk.scheduler.core;

public class SchedulingId {
  private String id;

  public SchedulingId(String id) {
    this.id = id;
  }

  public String value() {
    return id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object otherObject) {
    if (!(otherObject instanceof SchedulingId)) return false;
    SchedulingId other = (SchedulingId) otherObject;
    return id.equals(other.id);
  }

  @Override
  public String toString() {
    return id;
  }
}

