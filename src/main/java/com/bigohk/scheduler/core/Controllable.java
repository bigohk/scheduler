package com.bigohk.scheduler.core;

import com.bigohk.scheduler.api.Function;
import com.bigohk.scheduler.api.FunctionCompletionCallback;
import com.bigohk.scheduler.api.FunctionExceptionCallback;
import com.bigohk.scheduler.api.StateChangeCallback;

public interface Controllable extends Runnable {
  enum TaskState {
    NO_STATE, PAUSING, PAUSED, RUNNABLE, RUNNING;
  }

  SchedulingId getSchedulingId();

  void pause();

  void resume();

  TaskState getTaskState();

  void attachStateChangeCallback(TaskState state, StateChangeCallback callback);

  void attachFunction(Function function);

  void attachFunctionCompletionCallback(FunctionCompletionCallback callback, boolean useSeparateThread);

  void attachFunctionExceptionCallback(FunctionExceptionCallback callback, boolean useSeparateThread);

  long getLastFunctionLatency();
}
