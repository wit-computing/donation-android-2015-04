package app.donation.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.donation.model.Donation;
import app.donation.model.Donor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class DonationApp extends Application
{
  public String               service_url  = "http://10.0.2.2:9000";   // Standard Emulator IP Address
  //public String               service_url  = "http://10.0.3.2:9000"; // Genymotion IP address

  public DonationServiceProxy donationService;
  public boolean              donationServiceAvailable = false;

  public Donor                currentUser;
  public List <Donor>         donors       = new ArrayList<Donor>();
  public List <Donation>      donations    = new ArrayList<Donation>();

  public final int            target       = 10000;
  public int                  totalDonated = 0;


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
    Log.v("Donation", "Donation App Started");
  }
}