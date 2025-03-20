package psycho.euphoria.forcelock;

import android.content.Context;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TaskSchedulerWM {

    public static void scheduleTask(Context context, int hour, int minute) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        calendar.add(Calendar.MINUTE, minute);
//        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
//        if (delay <= 0) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//            delay = calendar.getTimeInMillis() - System.currentTimeMillis();
//        }
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(TaskWorker.class)
                .setInitialDelay(minute * 60 * 1000, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

    public static class TaskWorker extends Worker {
        private Context mContext;

        public TaskWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            mContext = context;
        }

        @Override
        public Result doWork() {
            // Your task code here
            Log.e("Debug", "WorkManager task executed at: " + Calendar.getInstance().getTime());
            LockScreenManager lockScreenManager = new LockScreenManager(mContext);
            lockScreenManager.lockScreen();
            return Result.success();
        }
    }
}