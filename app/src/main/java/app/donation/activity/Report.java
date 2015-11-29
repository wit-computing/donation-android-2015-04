package app.donation.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import app.donation.main.DonationApp;
import app.donation.R;
import app.donation.model.Donation;

import android.widget.Toast;

import app.donation.services.RefreshService;
import app.donation.settings.SettingsActivity;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Report extends Activity implements Callback<List<Donation>>
{
  public static final String BROADCAST_ACTION = "app.donation.activity.Report";
  private ListView        listView;
  private DonationApp     app;
  private DonationAdapter adapter;
  private IntentFilter intentFilter;
  //Call<List<Donation>> call;
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_report);

    app = (DonationApp) getApplication();

    listView = (ListView) findViewById(R.id.reportList);
    adapter = new DonationAdapter (this, app.donations);
    listView.setAdapter(adapter);

    intentFilter = new IntentFilter(BROADCAST_ACTION);
    registerBroadcastReceiver(intentFilter);

    //call = (Call<List<Donation>>) app.donationService.getAllDonations();

//    Call<List<Donation>> call = (Call<List<Donation>>) app.donationService.getAllDonations();
//    call.enqueue(this);
    //refreshDonationList();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_report, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.menuDonate : startActivity (new Intent(this, Donate.class));
        break;

      case R.id.menuLogout : startActivity (new Intent(this, Welcome.class));
        break;

      case R.id.action_settings:
        startActivity(new Intent(this, SettingsActivity.class));
        return true;

      case R.id.action_refresh:
        //refreshDonationList();
        startService(new Intent(this, RefreshService.class));
        //app.refreshDonationList();
        return true;
    }
    return true;
  }

  @Override
  public void onResponse(Response<List<Donation>> response, Retrofit retrofit)
  {
    adapter.donations = response.body();
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onFailure(Throwable t)
  {
    Toast toast = Toast.makeText(this, "Error retrieving donations", Toast.LENGTH_LONG);
    toast.show();
  }

  private void refreshDonationList()
  {
    Call<List<Donation>> call = (Call<List<Donation>>) app.donationService.getAllDonations();
    call.enqueue(this);
//      Call<List<Donation>> listCall = call.clone();
//      listCall.enqueue(this);
  }

  private void registerBroadcastReceiver(IntentFilter intentFilter)
  {
    ResponseReceiver responseReceiver = new ResponseReceiver();
    // Registers the ResponseReceiver and its intent filters
    LocalBroadcastManager.getInstance(this).registerReceiver(responseReceiver, intentFilter);
  }

  //Broadcast receiver for receiving status updates from the IntentService
  private class ResponseReceiver extends BroadcastReceiver
  {
    //private void ResponseReceiver() {}
    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent)
    {
      //refreshDonationList();
      adapter.donations = app.donations;
      adapter.notifyDataSetChanged();
    }
  }

}

class DonationAdapter extends ArrayAdapter<Donation>
{
  private Context context;
  public List<Donation> donations;

  public DonationAdapter(Context context, List<Donation> donations)
  {
    super(context, R.layout.row_layout, donations);
    this.context   = context;
    this.donations = donations;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View     view       = inflater.inflate(R.layout.row_layout, parent, false);
    Donation donation   = donations.get(position);
    TextView amountView = (TextView) view.findViewById(R.id.row_amount);
    TextView methodView = (TextView) view.findViewById(R.id.row_method);

    amountView.setText("" + donation.amount);
    methodView.setText(donation.method);

    return view;
  }

  @Override
  public int getCount()
  {
    return donations.size();
  }
}