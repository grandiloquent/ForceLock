package psycho.euphoria.forcelock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
                    TaskSchedulerWM.scheduleTask(MainActivity.this, 0, time);
                    AlarmScheduler.scheduleAlarm(MainActivity.this, 0, time * 2);
                    Toast.makeText(MainActivity.this, time + " 分钟后锁屏", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
}