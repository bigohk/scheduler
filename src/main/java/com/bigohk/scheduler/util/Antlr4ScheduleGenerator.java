package com.bigohk.scheduler.util;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.joda.time.DateTime;

import com.bigohk.scheduler.antlr.ScheduleLexer;
import com.bigohk.scheduler.antlr.ScheduleParser;
import com.bigohk.scheduler.core.Schedule;

public class Antlr4ScheduleGenerator {
  public Schedule generateSchedule(String description) {
    ANTLRInputStream ais = new ANTLRInputStream(description.toCharArray(), description.length());
    ScheduleLexer lexer = new ScheduleLexer(ais);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ScheduleParser parser = new ScheduleParser(tokens);

    ParseTree tree = parser.begin();
    ParseTreeWalker treeWalker = new ParseTreeWalker();
    final Antlr4ScheduleParseEventsListener listener = new Antlr4ScheduleParseEventsListener(DateTime.now());
    treeWalker.walk(listener, tree);

    return listener.generateSchedule();
  }
}

