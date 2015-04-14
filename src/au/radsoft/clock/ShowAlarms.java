package au.radsoft.clock;

import android.app.Activity;
import android.content.Intent;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ShowAlarms extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        
        Intent newIntent = createIntent();
        if (newIntent != null)
        {
            newIntent.setFlags(intent.getFlags());
            startActivity(newIntent);
        }
        else
            Toast.makeText(this, R.string.cant_find_show_alarms, Toast.LENGTH_LONG).show();
		finish();
    }
    
    Intent createIntent()
    {
        PackageManager packageManager = getPackageManager();

        String clockImpls[] = {
            "com.sec.android.app.clockpackage/.TabletClockPackage", // Samsung Galaxy Clock
            "com.sec.android.app.clockpackage/.ClockPackage", // Samsung Galaxy Clock
            "com.htc.android.worldclock/.WorldClockTabControl", // HTC Alarm Clock
            "com.motorola.blur.alarmclock/.AlarmClock", // Moto Blur Alarm Clock
            "com.android.alarmclock/.AlarmClock", // Standard Alarm Clock
            "com.google.android.deskclock/.DeskClock", // Froyo Nexus Alarm Clock
            "com.android.deskclock/.AlarmClock",
        };

        Intent alarmClockIntent = null;
        for(int i = 0; i < clockImpls.length && alarmClockIntent == null; ++i)
        {
            try
            {
                ComponentName cn = ComponentName.unflattenFromString(clockImpls[i]);
                ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                
                Log.i(getClass().getName(), "Found " + clockImpls[i]);
                alarmClockIntent = new Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                    .setComponent(cn);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                Log.i(getClass().getName(), clockImpls[i] + " does not exists");
            }
        }
        
        return alarmClockIntent;
    }
}
