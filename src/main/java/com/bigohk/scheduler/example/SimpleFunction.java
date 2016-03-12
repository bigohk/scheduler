package com.bigohk.scheduler.example;

import java.util.Date;

import javax.swing.JTextArea;

import com.bigohk.scheduler.api.Function;
import com.bigohk.scheduler.api.FunctionException;
import com.bigohk.scheduler.api.Schedulable;

public class SimpleFunction implements Function {
  private Date timestamp = new Date();

  private JTextArea   textArea;
  private Schedulable schedulable;

  public SimpleFunction(JTextArea textArea) {
    this.textArea = textArea;
  }

  @Override
  public void attachSchedulable(Schedulable schedulable) {
    this.schedulable = schedulable;
  }

  @Override
  public void perform() throws FunctionException {
    timestamp.setTime(System.currentTimeMillis());
    textArea.append("Task ID:" + schedulable.getId() + " scheduled at " + timestamp + "\n");
  }
}

