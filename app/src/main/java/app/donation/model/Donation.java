package app.donation.model;

public class Donation
{
  public Long   id;
  public int    amount;
  public String method;

  public Donation (int amount, String method)
  {
    this.amount = amount;
    this.method = method;
  }
}