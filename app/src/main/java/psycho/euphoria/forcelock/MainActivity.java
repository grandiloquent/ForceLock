package psycho.euphoria.forcelock;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (checkSelfPermission(permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission.POST_NOTIFICATIONS}, 0);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int lastInput = preferences.getInt("last", 10);
        EditText editText = findViewById(R.id.myEditText);
        editText.setText(Integer.toString(lastInput));
        findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LockScreenManager lockScreenManager = new LockScreenManager(MainActivity.this);
                if (!lockScreenManager.isAdminActive()) {
                    lockScreenManager.requestAdminPermission();
                    Toast.makeText(MainActivity.this, "请求权限", Toast.LENGTH_SHORT).show();
                } else {
                    String value = editText.getText().toString();
                    Pattern pattern = Pattern.compile("^\\d+$");
                    Matcher matcher = pattern.matcher(value);
                    if (!matcher.find()) {
                        value = "10";
                    }
                    int time = Integer.parseInt(value);
                    preferences.edit().putInt("last", time).commit();
                    Intent serviceIntent = new Intent(MainActivity.this, MyForegroundService.class);
                    startForegroundService(serviceIntent);

                }

            }
        });
    }
}