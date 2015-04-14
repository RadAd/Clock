package au.radsoft.clock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class ClockWidget extends AppWidgetProvider
{    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction()))
        {
            Log.i(getClass().getName(), "ACTION_APPWIDGET_UPDATE");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);

            //Intent intent = new Intent(android.provider.AlarmClock.ACTION_SHOW_ALARMS);
            Intent newIntent = new Intent("android.intent.action.SHOW_ALARMS")
                //.addCategory(Intent.CATEGORY_DEFAULT)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (newIntent != null)
                views.setOnClickPendingIntent(R.id.Widget, PendingIntent.getActivity(context, 0, newIntent, 0));

            AppWidgetManager.getInstance(context)
                .updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
        }
    }
}
