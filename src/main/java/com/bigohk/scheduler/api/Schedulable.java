package com.bigohk.scheduler.api;

import com.bigohk.scheduler.core.Controllable;
import com.bigohk.scheduler.core.Schedule;
import com.bigohk.scheduler.core.SchedulingId;

public interface Schedulable {

  SchedulingId getId();

  Schedule getSchedule();

  Controllable getTask();
}
