package au.radsoft.clock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.RemoteViews;

public class ClockWidget extends AppWidgetProvider
{    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            Log.i(getClass().getName(), "ACTION_APPWIDGET_UPDATE");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            
            PendingIntent pendingIntent = createPendingIntent(context);
            if (pendingIntent != null)
                views.setOnClickPendingIntent(R.id.Widget, pendingIntent);
                                    
            AppWidgetManager.getInstance(context)
                .updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
        }
    }
    
    PendingIntent createPendingIntent(Context context)
    {
        PackageManager packageManager = context.getPackageManager();

        String clockImpls[][] = {
            { "com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage" }, // Samsung Galaxy Clock
            { "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" }, // HTC Alarm Clock
            { "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"}, // Moto Blur Alarm Clock
            { "com.android.alarmclock", "com.android.alarmclock.AlarmClock"}, // Standard Alarm Clock
            { "com.google.android.deskclock", "com.google.android.deskclock.DeskClock" }, // Froyo Nexus Alarm Clock
            { "com.android.deskclock", "com.android.deskclock.AlarmClock" },
        };

        PendingIntent pendingIntent = null;
        for(int i = 0; i < clockImpls.length && pendingIntent == null; ++i)
        {
            String packageName = clockImpls[i][0];
            String className = clockImpls[i][1];
            
            try
            {
                ComponentName cn = new ComponentName(packageName, className);
                ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                
                Log.i(getClass().getName(), "Found " + packageName + "/" + className);
                Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                    .setComponent(cn);
                pendingIntent = PendingIntent.getActivity(context, 0, alarmClockIntent, 0);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                Log.i(getClass().getName(), packageName + "/" + className + " does not exists");
            }
        }
        
        return pendingIntent;
    }
}
