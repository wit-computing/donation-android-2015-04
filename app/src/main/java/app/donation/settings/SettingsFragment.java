package app.donation.settings;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import app.android.helpers.LogHelpers;
import app.donation.R;

import static app.android.helpers.LogHelpers.info;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
  private SharedPreferences prefs;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);
    setHasOptionsMenu(true);
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);


  }

  @Override
  public void onStart()
  {
    super.onStart();
    prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
	LogHelpers.info(getActivity(), "change to preference whose key is " + key);
  }
}
