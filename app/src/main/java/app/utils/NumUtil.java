package app.utils;

/**
 * Created by usplitu on 28/11/2015.
 */
public class NumUtil
{

  /**
   * Verifies that a string parses to a positive number
   * @param str The string to be verified
   * @return If string comprises digits 0 to 9 only, returns true, else false.
   */
  public static boolean isPositiveNumber(String str)
  {
    // check for empty string
    if (str.compareTo("") == 0)
      return false;

    // if any non-digit char found return false
    for (char c : str.toCharArray())
    {
      if (!Character.isDigit(c))
        return false;
    }
    return true;
  }

}
