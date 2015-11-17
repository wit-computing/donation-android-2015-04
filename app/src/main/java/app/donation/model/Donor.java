package app.donation.model;

public class Donor
{
  public Long   id;
  public String firstName;
  public String lastName;
  public String email;
  public String password;

  public Donor(String firstName, String lastName, String email, String password)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }
}