package app.donation.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.List;

import app.android.helpers.LogHelpers;
import app.donation.activity.Report;
import app.donation.main.DonationApp;
import app.donation.model.Donation;
import retrofit.Call;
import retrofit.Response;

public class RefreshService extends IntentService
{
  private String tag = "Donation";
  DonationApp app;
  public RefreshService()
  {
    super("RefreshService");
  }

  @Override
  protected void onHandleIntent(Intent intent)
  {
    app = (DonationApp) getApplication();
    Intent localIntent = new Intent(Report.BROADCAST_ACTION);
    // Broadcasts the Intent to receivers in this app.
    //LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    Call<List<Donation>> call = (Call<List<Donation>>) app.donationService.getAllDonations();
    try
    {
      Response<List<Donation>> response = call.execute();
      app.donations = response.body();
      LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
    catch (IOException e)
    {

    }
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    LogHelpers.info(tag, "RefreshService instance destroyed");
  }

}