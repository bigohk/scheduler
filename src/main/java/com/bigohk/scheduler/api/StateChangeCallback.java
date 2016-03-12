package com.bigohk.scheduler.api;

import com.bigohk.scheduler.core.Controllable;

public interface StateChangeCallback {
  void notifyStateChange(Controllable.TaskState currentState);
}
