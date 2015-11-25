package app.donation.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import app.donation.R;


public class SettingsActivity extends Activity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null)
    {
      SettingsFragment fragment = new SettingsFragment();
      getFragmentManager().beginTransaction().add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
          .commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.status, menu);
    return true;
  }
}