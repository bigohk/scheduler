package com.bigohk.scheduler.util;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.antlr.v4.runtime.misc.NotNull;
import org.joda.time.DateTime;

import com.bigohk.scheduler.antlr.ScheduleBaseListener;
import com.bigohk.scheduler.antlr.ScheduleParser;
import com.bigohk.scheduler.core.Schedule;

public class Antlr4ScheduleParseEventsListener extends ScheduleBaseListener {
  public static final String MONTH  = "MONTH";
  public static final String DAY    = "DAY";
  public static final String HOUR   = "HOUR";
  public static final String MINUTE = "MINUTE";
  public static final String SECOND = "SECOND";
  public static final String MILLIS = "MILLIS";
  public static final String ASAP   = "asap";

  private int frequencyDigits = 1;

  private boolean  isBeginTimeSane;
  private boolean  isAsap;
  private DateTime referenceDateTime;
  private DateTime beginDateTime;
  private String   eachOrEverySelector;


  private final HashMap<String, TimeUnit> eachOrEveryTokenVals = new HashMap<String, TimeUnit>(12);

  public Antlr4ScheduleParseEventsListener(DateTime referenceDateTime) {
    this.referenceDateTime = referenceDateTime;
    eachOrEveryTokenVals.put(MONTH, null);
    eachOrEveryTokenVals.put(DAY, TimeUnit.DAYS);
    eachOrEveryTokenVals.put(HOUR, TimeUnit.HOURS);
    eachOrEveryTokenVals.put(MINUTE, TimeUnit.MINUTES);
    eachOrEveryTokenVals.put(SECOND, TimeUnit.SECONDS);
    eachOrEveryTokenVals.put(MILLIS, TimeUnit.MILLISECONDS);
  }

  @Override
  public void enterBegin(@NotNull ScheduleParser.BeginContext ctx) {
    final String text = ctx.DATETIME().getSymbol().getText();

    if (text.equals(ASAP)) {
      beginDateTime = DateTime.now();
      isBeginTimeSane = true;
      isAsap = true;
      return;

    } else {
      beginDateTime = DateTime.parse(text);

    }

    if (beginDateTime.isAfter(referenceDateTime.toInstant())) {
      isBeginTimeSane = true;
    }
  }

  @Override
  public void enterRepeat(@NotNull ScheduleParser.RepeatContext ctx) {
    //nothing to be done here, 'repeat' is only syntactic sugar
  }

  @Override
  public void enterEvery(@NotNull ScheduleParser.EveryContext ctx) {
    frequencyDigits = Integer.parseInt(ctx.DIGITS().getText());
    if (isBeginTimeSane) {
      if (ctx.DAY() != null) {
        eachOrEverySelector = DAY;

      } else if (ctx.HOUR() != null) {
        eachOrEverySelector = HOUR;

      } else if (ctx.MINUTE() != null) {
        eachOrEverySelector = MINUTE;

      } else if (ctx.SECOND() != null) {
        eachOrEverySelector = SECOND;

      } else if (ctx.MILLIS() != null) {
        eachOrEverySelector = MILLIS;

      }
    }
  }

  @Override
  public void enterEach(@NotNull ScheduleParser.EachContext ctx) {
    if (isBeginTimeSane) {
      if (ctx.EACH_MONTH() != null) {
        eachOrEverySelector = MONTH;

      } else if (ctx.EACH_DAY() != null) {
        eachOrEverySelector = DAY;

      } else if (ctx.EACH_HOUR() != null) {
        eachOrEverySelector = HOUR;

      } else if (ctx.EACH_MINUTE() != null) {
        eachOrEverySelector = MINUTE;

      } else if (ctx.EACH_SECOND() != null) {
        eachOrEverySelector = SECOND;

      } else if (ctx.EACH_MILLIS() != null) {
        eachOrEverySelector = MILLIS;

      }
    }
  }

  public Schedule generateSchedule() {
    Schedule schedule = Schedule.NEVER;
    if (isBeginTimeSane) {
      long fixedDelay = 0L;
      if (!isAsap) {
        fixedDelay = beginDateTime.getMillis() - referenceDateTime.getMillis();
      }
      TimeUnit timeUnit = eachOrEveryTokenVals.get(eachOrEverySelector);
      fixedDelay = alignDelayToTimeUnits(fixedDelay, timeUnit);
      if (timeUnit != null) {
        schedule = new Schedule(fixedDelay, frequencyDigits, timeUnit);
      }
      isBeginTimeSane = false;
      isAsap = false;
      frequencyDigits = 1;
    }
    return schedule;
  }

  private long alignDelayToTimeUnits(long fixedDelay, TimeUnit timeUnit) {
    if (timeUnit == TimeUnit.MILLISECONDS) {
      return fixedDelay;

    } else if (timeUnit == TimeUnit.SECONDS) {
      return fixedDelay / 1000;

    } else if (timeUnit == TimeUnit.MINUTES) {
      return (fixedDelay / 1000) / 60;

    } else if (timeUnit == TimeUnit.HOURS) {
      return (fixedDelay / 1000) / 3600;

    } else if (timeUnit == TimeUnit.DAYS) {
      return (fixedDelay / 1000) / 86400;

    }
    return 0;
  }
}

