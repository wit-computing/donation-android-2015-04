package app.donation.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;

import app.android.helpers.LogHelpers;
import app.donation.R;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{

  private SharedPreferences prefs;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setupActionBar();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.status, menu);
    return true;
  }

  private void setupActionBar()
  {
    ActionBar actionBar = getActionBar();
    if (actionBar != null)
    {
      // Show the Up button in the action bar.
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public void onStart()
  {
    super.onStart();
    prefs = PreferenceManager.getDefaultSharedPreferences(this);
    prefs.registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onStop()
  {
    super.onStop();
    prefs.unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
  {
    LogHelpers.info(this, "change to preference whose key is " + key);
  }
}