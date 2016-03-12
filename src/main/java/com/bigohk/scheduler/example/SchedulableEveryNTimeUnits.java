package com.bigohk.scheduler.example;

import com.bigohk.scheduler.api.Schedulable;
import com.bigohk.scheduler.core.Controllable;
import com.bigohk.scheduler.core.Schedule;
import com.bigohk.scheduler.core.SchedulingId;

public class SchedulableEveryNTimeUnits implements Schedulable {
  SchedulingId id;
  Controllable task;
  Schedule     schedule;

  public SchedulableEveryNTimeUnits(SchedulingId id, Controllable task, Schedule schedule) {
    this.id = id;
    this.task = task;
    this.schedule = schedule;
  }

  @Override
  public SchedulingId getId() {
    return id;
  }

  @Override
  public Schedule getSchedule() {
    return schedule;
  }

  @Override
  public Controllable getTask() {
    return task;
  }

}

