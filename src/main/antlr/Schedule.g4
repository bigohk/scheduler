grammar Schedule;

@header {
   package com.bigohk.scheduler.antlr;
}

begin:  'begin' DATETIME repeat;

repeat: 'repeat' (each|every);

each:   (EACH_MONTH | EACH_DAY | EACH_HOUR | EACH_MINUTE | EACH_SECOND | EACH_MILLIS);

every:  'every' DIGITS (DAY | HOUR | MINUTE | SECOND | MILLIS);

DATETIME:   ('asap'|XYEAR'-'XMONTH'-'XDAY'T'XHOUR':'XMINUTE':'XSECOND);
XYEAR:   [0-9][0-9][0-9][0-9];
XMONTH:  [0-9][0-9];
XDAY:    [0-9][0-9];
XHOUR:   [0-9][0-9];
XMINUTE: [0-9][0-9];
XSECOND: [0-9][0-9];
WS :    [ \t\r\n]+ -> skip ;

EACH_MONTH: 'each_month' | 'monthly' | JAN | FEB | MAR | APR | MAY | JUN | JUL | AUG | SEP | OCT | NOV | DEC;
JAN: 'jan'  | 'january'     ;
FEB: 'feb'  | 'february'    ;
MAR: 'mar'  | 'march'       ;
APR: 'apr'  | 'april'       ;
MAY: 'may'                  ;
JUN: 'jun'  | 'june'        ;
JUL: 'jul'  | 'july'        ;
AUG: 'aug'  | 'august'      ;
SEP: 'sep'  | 'september'   ;
OCT: 'oct'  | 'october'     ;
NOV: 'nov'  | 'november'    ;
DEC: 'dec'  | 'december'    ;

EACH_DAY:   'each_day' | 'daily' | SUN | MON | TUE | WED | THU | FRI | SAT;
SUN: 'sun'  | 'sunday'      ;
MON: 'mon'  | 'monday'      ;
TUE: 'tue'  | 'tuesday'     ;
WED: 'wed'  | 'wednesday'   ;
THU: 'thu'  | 'thursday'    ;
FRI: 'fri'  | 'friday'      ;
SAT: 'sat'  | 'saturday'    ;

EACH_HOUR:  'each_hour' | 'hourly';

EACH_MINUTE:'each_minute';

EACH_SECOND:'each_second';

EACH_MILLIS:'each_millis';

DIGITS: [1-9]+;

DAY: 'days' | 'day';

HOUR: 'hours' | 'hour';

MINUTE: 'minutes' | 'minute';

SECOND: 'seconds' | 'second';

MILLIS: 'millis' | 'millisecond' | 'milliseconds';