import java.lang.Exception;

public class InvalidMnemonicException extends Exception
{
  public InvalidMnemonicException(String message)
  {
    super(message);
  }
}
