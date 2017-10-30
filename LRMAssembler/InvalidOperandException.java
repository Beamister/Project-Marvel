import java.lang.Exception;

public class InvalidOperandException extends Exception
{
  public InvalidOperandException(String message)
  {
    super(message);
  }
}
