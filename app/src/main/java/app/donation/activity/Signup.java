package app.donation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.donation.R;
import app.donation.main.DonationApp;
import app.donation.model.Donor;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Signup extends Activity implements Callback<Donor>
{
  private DonationApp app;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    app = (DonationApp) getApplication();
  }

  public void registerPressed (View view)
  {
    TextView firstName = (TextView)  findViewById(R.id.firstName);
    TextView lastName  = (TextView)  findViewById(R.id.lastName);
    TextView email     = (TextView)  findViewById(R.id.Email);
    TextView password  = (TextView)  findViewById(R.id.Password);

    Donor donor = new Donor(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), password.getText().toString());

    DonationApp app = (DonationApp) getApplication();
    Call<Donor> call = (Call<Donor>) app.donationService.createDonor(donor);
    call.enqueue(this);
  }

  @Override
  public void onResponse(Response<Donor> response, Retrofit retrofit)
  {
    app.donors.add(response.body());
    startActivity(new Intent(this, Welcome.class));
  }

  @Override
  public void onFailure(Throwable t)
  {
    app.donationServiceAvailable = false;
    Toast toast = Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG);
    toast.show();
    startActivity (new Intent(this, Welcome.class));
  }
}