package com.bigohk.scheduler.example;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.bigohk.scheduler.core.DefaultControllable;
import com.bigohk.scheduler.core.Schedule;
import com.bigohk.scheduler.core.Scheduler;
import com.bigohk.scheduler.core.SchedulingId;
import com.bigohk.scheduler.util.Antlr4ScheduleGenerator;

public class SchedTest {
  public static void main(String[] args) throws IOException {
    Scheduler myScheduler = new Scheduler(5);

    final JTextArea textArea = new JTextArea();
    final JFrame frame = new JFrame("Custom Scheduler Spike");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    HashMap<String, SchedulingId> idMap = new HashMap<String, SchedulingId>();

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI(frame, textArea);
      }
    });

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String command = "NO_OP";
    String[] cmdTokens;
    while (!command.equalsIgnoreCase("quit")) {
      System.out.print("> ");
      command = br.readLine();
      cmdTokens = command.split(",", -1);
      if (command.startsWith("queryTaskState") && cmdTokens.length > 1) {
        System.out.println(myScheduler.queryTaskState(idMap.get(cmdTokens[1])));

      } else if (command.startsWith("cancelTask") && cmdTokens.length > 1) {
        myScheduler.cancelTask(idMap.get(cmdTokens[1]));

      } else if (command.startsWith("pauseTask") && cmdTokens.length > 1) {
        myScheduler.pauseTask(idMap.get(cmdTokens[1]));

      } else if (command.startsWith("resumeTask") && cmdTokens.length > 1) {
        myScheduler.resumeTask(idMap.get(cmdTokens[1]));

      } else if (command.startsWith("schedTask") && cmdTokens.length > 1) {
        scheduleNewTask(myScheduler, textArea, idMap, cmdTokens);

      } else if (command.startsWith("modifySched") && cmdTokens.length > 1) {
        final Schedule schedule = parseSchedule(cmdTokens[2]);
        myScheduler.modifySchedule(idMap.get(cmdTokens[1]), schedule.getFrequency(), schedule.getTimeUnit());

      } else if (command.startsWith("parseSched") && cmdTokens.length > 1) {
        System.out.println(parseSchedule(cmdTokens[1]));

      }
    }

    myScheduler.stop(false);

    WindowEvent wev = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
  }

  private static void scheduleNewTask(Scheduler myScheduler, JTextArea textArea, HashMap<String, SchedulingId> idMap, String[] cmdTokens) {
    SchedulingId id = new SchedulingId(cmdTokens[1]);
    idMap.put(id.value(), id);
    SimpleFunction function = new SimpleFunction(textArea);
    DefaultControllable controllable = new DefaultControllable(id, function);

    Schedule schedule = parseSchedule(cmdTokens[2]);
    if (!(schedule == Schedule.NEVER)) {
      SchedulableEveryNTimeUnits schedulable = new SchedulableEveryNTimeUnits(id, controllable, schedule);
      function.attachSchedulable(schedulable);
      myScheduler.scheduleTask(schedulable);
    }
  }

  private static Schedule parseSchedule(String cmdToken) {
    Antlr4ScheduleGenerator scheduleGenerator = new Antlr4ScheduleGenerator();
    return scheduleGenerator.generateSchedule(cmdToken);
  }

  private static void createAndShowGUI(JFrame frame, JTextArea textArea) {
    //Create and set up the window.
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    frame.getContentPane().add(scrollPane);
    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

}

