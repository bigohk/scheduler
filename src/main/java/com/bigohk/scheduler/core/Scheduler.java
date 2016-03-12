package com.bigohk.scheduler.core;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bigohk.scheduler.api.Schedulable;

public class Scheduler {
  private ScheduledThreadPoolExecutor threadPool;
  private final HashMap<SchedulingId, Schedulable>     taskNameMap   = new HashMap<SchedulingId, Schedulable>();
  private final HashMap<SchedulingId, ScheduledFuture<?>> taskFutureMap = new HashMap<>();

  public Scheduler(int numThreads) {
    threadPool = new ScheduledThreadPoolExecutor(numThreads);
  }

  public void scheduleTask(Schedulable task) {
    final Schedule schedule = task.getSchedule();
    final Runnable payload = task.getTask();

    ScheduledFuture<?> future = null;
    if (schedule == Schedule.ONE_TIME_ASAP) {
      threadPool.execute(payload);
    } else {
      future = threadPool.scheduleAtFixedRate(payload, schedule.getFixedDelay(), schedule.getFrequency(), schedule.getTimeUnit());
    }

    taskNameMap.put(task.getId(), task);
    taskFutureMap.put(task.getId(), future);
  }

  public void cancelTask(SchedulingId id) {
    final Schedulable schedulable = taskNameMap.remove(id);
    if (schedulable != null) {
      taskFutureMap.remove(id).cancel(true);
      taskNameMap.remove(id);
      threadPool.remove(schedulable.getTask());
    }
  }

  public void pauseTask(SchedulingId id) {
    final Schedulable schedulable = taskNameMap.get(id);
    if (schedulable != null) {
      schedulable.getTask().pause();
    }
  }

  public void resumeTask(SchedulingId id) {
    final Schedulable schedulable = taskNameMap.get(id);
    if (schedulable != null) {
      schedulable.getTask().resume();
    }
  }

  public void modifySchedule(SchedulingId id, long frequency, TimeUnit timeUnit) {
    final Schedulable schedulable = taskNameMap.get(id);
    if (schedulable != null) {
      final Controllable payload = schedulable.getTask();
      taskFutureMap.remove(id).cancel(true);
      threadPool.remove(payload);

      final Schedule schedule = schedulable.getSchedule();
      schedule.setFrequency(frequency);
      schedule.setTimeUnit(timeUnit);

      ScheduledFuture<?> future = null;
      if (schedule == Schedule.ONE_TIME_ASAP) {
        threadPool.execute(payload);
      } else {
        future = threadPool.scheduleAtFixedRate(payload, 0L, schedule.getFrequency(), schedule.getTimeUnit());
      }

      taskNameMap.put(id, schedulable);
      taskFutureMap.put(id, future);
    }

  }

  public Controllable.TaskState queryTaskState(SchedulingId id) {
    final Schedulable schedulable = taskNameMap.get(id);
    if (schedulable != null) {
      return schedulable.getTask().getTaskState();
    } else {
      return Controllable.TaskState.NO_STATE;
    }
  }

  public void start() {
    //Placeholder
  }

  public void stop(boolean force) {
    if (force) {
      threadPool.shutdownNow();
    } else {
      threadPool.shutdown();
    }
  }

}

