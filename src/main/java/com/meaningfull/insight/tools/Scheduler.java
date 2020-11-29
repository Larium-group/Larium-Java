package com.meaningfull.insight.tools;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shahaf Pariente on 10/2/2020
 */
public interface Scheduler {
    default void setScheduleInitialize(Runnable func, long delay) {
        Timer timer = new Timer(true);
        long period = 1000L*60*10;  // twice a day
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                func.run();
            }
        },delay, period);
    }
}
