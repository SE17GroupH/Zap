package teamh.zapapp;


import android.app.Activity;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by sid on 3/7/17.
 */

public final class CurrentActivityUtil {
    public static Class getCurrentActivityClass() {
        final Activity[] currentActivity = {null};
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity[0] = resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0].getClass();
    }
}
