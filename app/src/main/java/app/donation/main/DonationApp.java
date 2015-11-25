package app.donation.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.donation.model.Donation;
import app.donation.model.Donor;
import app.donation.services.RefreshService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class DonationApp extends Application
{
  //public String               service_url  = "http://10.0.2.2:9000";   // Standard Emulator IP Address
  //public String               service_url  = "http://10.0.3.2:9000"; // Genymotion IP address
  public String                 service_url  = "http://donation-service-2015v2.herokuapp.com/";
  //public String          service_url  = "http://mytweet-service-2015.herokuapp.com/";

  public DonationServiceProxy donationService;
  public boolean              donationServiceAvailable = false;

  public Donor                currentUser;
  public List <Donor>         donors       = new ArrayList<Donor>();
  public List <Donation>      donations    = new ArrayList<Donation>();

  public final int            target       = 10000;
  public int                  totalDonated = 0;

  private Timer timer;

  public boolean newDonation(Donation donation)
  {
    boolean targetAchieved = totalDonated > target;
    if (!targetAchieved)
    {
      donations.add(donation);
      totalDonated += donation.amount;
    }
    else
    {
      Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
      toast.show();
    }
    return targetAchieved;
  }

  public void newUser(Donor user)
  {
    donors.add(user);
  }

  public boolean validUser (String email, String password)
  {
    for (Donor user : donors)
    {
      if (user.email.equals(email) && user.password.equals(password))
      {
        currentUser = user;
        return true;
      }
    }
    return false;
  }

  @Override
  public void onCreate()
  {
    super.onCreate();
    Gson gson = new GsonBuilder().create();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(service_url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    donationService = retrofit.create(DonationServiceProxy.class);

    timer = new Timer();

    //this.refreshDonationList();

    Log.v("Donation", "Donation App Started");
  }



  public void refreshDonationList()
  {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    String refreshInterval = prefs.getString("refresh_interval", "5000"); // parm 2 is default val in milli secs
    int refreshFrequency = Integer.parseInt(refreshInterval) * 60 * 1000;
    int initialDelay = 1000;
    timer.schedule(new TimerTask()
    {
      @Override
      public void run()
      {
        startService(new Intent(getBaseContext(), RefreshService.class));
      }
    }, initialDelay, refreshFrequency); // Example 2*60*1000 == 2 x 60 secs x 1000 : 2 minutes -> milliseconds
  }
}