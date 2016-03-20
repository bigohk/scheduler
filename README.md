# Scheduler
A simple Java scheduler based on human readable schedule configuration. 

### Cron is a bit abstract
The cron schedule format has stood the test of time but it is not straight forward i.e. when I look at a cron schedule, I go *ummm ?... oh ok, fine*

With Java, we have Quartz Scheduler as well. Quartz uses cron format to configure the schedule. On top of the terse nature of cron format, the Quartz variant can turn out to be ambiguous.

### begin asap repeat every hour
That's what the schedules look like with this project. (well, that was just an example, there's more vocabulary)

There is an Antlr4 grammar which declares the language, then some code which translates the schedule description into scheduling parameters and schedules threads into ScheduledThreadPoolExecutor.

####A few more examples...
* begin 2016-04-01T00:00:00 repeat hourly
* begin 2016-04-01T00:00:00 repeat every 2 days
* begin 2016-04-01T00:00:00 *(note:no repetition)*

