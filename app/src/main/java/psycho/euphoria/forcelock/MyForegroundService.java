package psycho.euphoria.forcelock;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE;

public class MyForegroundService extends Service {

    public static final String ACTION_LOCK = "psycho.euphoria.forcelock.LOCK";
    private static final String CHANNEL_ID = "MyForegroundServiceChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getAction() : null;
        if (action != null && action.equals(ACTION_LOCK)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int time = preferences.getInt("last", 10);
            Log.e("B5aOx2", String.format("onStartCommand, %s", time));
            TaskSchedulerWM.scheduleTask(this, 0, time);
            AlarmScheduler.scheduleAlarm(this, 0, time * 2);
            Toast.makeText(this, time + " 分钟后锁屏", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY; // Or other appropriate return value
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MyForegroundService.class); // Replace MainActivity.class
        notificationIntent.setAction(ACTION_LOCK);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new Builder(this, CHANNEL_ID)
                .setContentTitle("强制锁屏")
                .setContentText("运行中...")
                .setSmallIcon(android.R.drawable.ic_notification_overlay) // Replace with your icon
                .setContentIntent(pendingIntent)
                .build();
        if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            startForeground(1, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(1, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }
}