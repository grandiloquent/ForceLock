package psycho.euphoria.forcelock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("B5aOx2", String.format("onReceive, %s", "-------------------------"));
        LockScreenManager lockScreenManager = new LockScreenManager(context);
        lockScreenManager.lockScreen();
    }
}
