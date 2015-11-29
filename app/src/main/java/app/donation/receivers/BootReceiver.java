package app.donation.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import app.donation.services.RefreshService;
import app.utils.NumUtil;


/**
 * If permission set and BootReceiver registered in manifest file ...
 * and application manually launched, then...
 * then BootReceiver.onReceive method will be invoked by system when device started.
 * In this method we set the interval at which the alarm should trigger.
 * This will be either a default or a value input by user in preference settings.
 */
public class BootReceiver extends BroadcastReceiver
{
  private static final long DEFAULT_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
  public static int REQUESTCODE = -1;
  private String tag = "Donation";

  @Override
  public void onReceive(Context context, Intent intent)
  {
    Log.i(tag, "In BootReceiver.onReceive");

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    // This key introduced in res/xml/settings.xml
    String key = "refresh_interval";
    long interval = DEFAULT_INTERVAL;
    // We check for a valid user inputted interval and use it if it exists
    // Otherwise use DEFAULT_INTERVAL
    String value = prefs.getString(key, Long.toString(DEFAULT_INTERVAL));
    if (NumUtil.isPositiveNumber(value))
    {
      interval = Long.parseLong(value);
    }
    // Convert interval from minutes to milliseconds
    interval *= 60000;//here we convert minutes to milliseconds since input at settings menu is specified in minutes
    // Set an arbitrary minimum interval value of 60 seconds to avoid overloading service.
    interval = interval < 60000 ? 60000 : interval;

    // Prepare an PendingIntent with a view to triggering RefreshService
    PendingIntent operation = PendingIntent.getService(
        context,
        REQUESTCODE,
        new Intent(context, RefreshService.class),
        PendingIntent.FLAG_UPDATE_CURRENT
    );

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmManager.cancel(operation);//cancel any existing alarms with matching intent
    alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, operation);

    long appliedInterval = interval/60000;
    Log.i(tag, "Boot receiver alarm repeats every: " + appliedInterval + " minutes.");
  }
}