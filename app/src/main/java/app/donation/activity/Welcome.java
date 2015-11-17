package app.donation.activity;

import app.donation.R;
import app.donation.main.DonationApp;
import app.donation.model.Donor;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class Welcome extends AppCompatActivity implements Callback<List<Donor>>
{
  private DonationApp app;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    app = (DonationApp) getApplication();
  }

  @Override
  public void onResume()
  {
    super.onResume();
    app.currentUser = null;
    Call<List<Donor>> call = (Call<List<Donor>>) app.donationService.getAllDonors();
    call.enqueue(this);
  }

  @Override
  public void onResponse(Response<List<Donor>> response, Retrofit retrofit)
  {
    serviceAvailableMessage();
    app.donors = response.body();
    app.donationServiceAvailable = true;
  }

  @Override
  public void onFailure(Throwable t)
  {
    app.donationServiceAvailable = false;
    serviceUnavailableMessage();
  }

  public void loginPressed (View view)
  {
    if (app.donationServiceAvailable)
    {
      startActivity (new Intent(this, Login.class));
    }
    else
    {
      serviceUnavailableMessage();
    }
  }

  public void signupPressed (View view)
  {
    if (app.donationServiceAvailable)
    {
      startActivity (new Intent(this, Signup.class));
    }
    else
    {
      serviceUnavailableMessage();
    }
  }

  void serviceUnavailableMessage()
  {
    Toast toast = Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG);
    toast.show();
  }

  void serviceAvailableMessage()
  {
    Toast toast = Toast.makeText(this, "Donation Contacted Successfully", Toast.LENGTH_LONG);
    toast.show();
  }
}