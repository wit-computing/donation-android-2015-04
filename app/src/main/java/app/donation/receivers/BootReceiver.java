package app.donation.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import app.android.helpers.LogHelpers;
import app.donation.services.RefreshService;


public class BootReceiver extends BroadcastReceiver
{
  private static final long DEFAULT_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
  public static int REQUESTCODE = -1;
  private String tag = "Donation";

  @Override
  public void onReceive(Context context, Intent intent)
  {
    // Facilitates stopping at breakpoint placed onReceive code below
    //android.os.Debug.waitForDebugger();

    Log.i(tag, "In BootReceiver.onReceive");

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    // Set the refresh interval i.e. frequency of alarm triggers
    long interval = Long.parseLong(prefs.getString("refresh_interval", Long.toString(DEFAULT_INTERVAL)));
    interval *= 60000;//here we convert minutes to milliseconds since input at settings menu is specified in minutes
    interval = interval < 60000 ? 60000 : interval; //Set 60 seconds as the minimum as counter to denial of service attack

    PendingIntent operation = PendingIntent.getService(
        context,
        REQUESTCODE,
        new Intent(context, RefreshService.class),
        PendingIntent.FLAG_UPDATE_CURRENT
    );

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmManager.cancel(operation);//cancel any existing alarms with matching intent
    alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, operation);

    LogHelpers.info(this, "setting repeat operation for: " + interval);
    LogHelpers.info(this, "onReceived");
  }
}