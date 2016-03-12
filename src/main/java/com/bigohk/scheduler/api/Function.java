package com.bigohk.scheduler.api;

public interface Function {
  void attachSchedulable(Schedulable schedulable); //This is only a hack for the time being
  void perform() throws FunctionException;
}
