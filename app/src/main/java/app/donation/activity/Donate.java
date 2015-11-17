package app.donation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import app.donation.model.Donation;
import app.donation.main.DonationApp;
import app.donation.R;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Donate extends AppCompatActivity implements Callback<Donation>
{
  private Button       donateButton;
  private RadioGroup   paymentMethod;
  private ProgressBar  progressBar;
  private NumberPicker amountPicker;
  private int          totalDonated;
  private int          target;

  private TextView     amountText;
  private TextView     amountTotal;

  private DonationApp app;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_donate);

    app = (DonationApp) getApplication();

    donateButton  = (Button) findViewById(R.id.donateButton);
    paymentMethod = (RadioGroup)   findViewById(R.id.paymentMethod);
    progressBar   = (ProgressBar)  findViewById(R.id.progressBar);
    amountPicker  = (NumberPicker) findViewById(R.id.amountPicker);
    amountTotal   = (TextView)     findViewById(R.id.amountTotal);
    amountText    = (EditText)     findViewById(R.id.amountText);

    amountPicker.setMinValue(0);
    amountPicker.setMaxValue(1000);
    progressBar.setMax(10000);

    totalDonated = 0;
    target = 10000;

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_donate, menu);
    return true;
  }


  public void donateButtonPressed (View view)
  {
    String method = paymentMethod.getCheckedRadioButtonId() == R.id.PayPal ? "PayPal" : "Direct";
    int donatedAmount =  amountPicker.getValue();
    if (donatedAmount == 0)
    {
      String text = amountText.getText().toString();
      if (!text.equals(""))
        donatedAmount = Integer.parseInt(text);
    }
    if (donatedAmount > 0)
    {
      Donation donation = new Donation(donatedAmount, method);
      Call<Donation> call = (Call<Donation>) app.donationService.createDonation(app.currentUser.id, donation);
      call.enqueue(this);
    }
    amountText.setText("");
    amountPicker.setValue(0);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.menuReport : startActivity (new Intent(this, Report.class));
        break;
      case R.id.menuLogout : startActivity (new Intent(this, Welcome.class));
        break;
    }
    return true;
  }

  @Override
  public void onResponse(Response<Donation> response, Retrofit retrofit)
  {
    Toast toast = Toast.makeText(this, "Donation Accepteed", Toast.LENGTH_SHORT);
    toast.show();
    app.newDonation(response.body());
    progressBar.setProgress(app.totalDonated);
    String totalDonatedStr = "$" + app.totalDonated;
    amountTotal.setText(totalDonatedStr);
    amountText.setText("");
    amountPicker.setValue(0);
  }

  @Override
  public void onFailure(Throwable t)
  {
    Toast toast = Toast.makeText(this, "Error making donation", Toast.LENGTH_LONG);
    toast.show();
  }
}
