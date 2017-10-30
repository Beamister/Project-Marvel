//Converts the first file given as an argument to a LRM machine code and outputs
//the result to the file given as the second argument, each word (4 bytes) is
//shown on a new line

//WIP - TO DO:
//Process line
//Directives
//
//

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class LRMAssembler
{
  private static int lineCount = 0;
  private static int lastMemoryAddress = 0;
  private static ArrayList<String> lines = new ArrayList<String>();
  private static String outputProgram;
  private static BufferedReader input = null;
  private static PrintWriter output = null;
  private static String[] directives =
    {"ORIGIN", "EQUALS", "DEFW", "DEFS", "DEFI", "DEFC"};
  private static String[][] tokens;
  private static HashMap<String, String> instructions = LanguageData.generateInstructions();
  private static HashMap<String, Integer>  registers = LanguageData.generateRegisters();
  private static HashSet<String> mnemonicList = new HashSet<String>(instructions.keySet());
  private static HashSet<String> registerNames = new HashSet<String>(registers.keySet());
  private static HashMap<String, Integer> labels = new HashMap<String, Integer>();

  public static void main(String[] args)
  {
    try{
      if(args.length < 2)
      {
        throw new Exception("Please provide the correct arguments");
      }
      readInputFile(args[0]);
      assemble();
      outputToFile(args[1]);
    }
    catch(AssemblyException exception)
    {
      System.out.println("Failed to assemble\n" + exception.getMessage());
    }
    catch(FileNotFoundException exception)
    {
      System.out.println("Please input a file that actually exists");
    }
    catch(IOException exception)
    {
      System.out.println("Something went wrong with the input and output\n" + exception.getMessage());
    }
    catch(Exception exception)
    {
      System.out.println("Something unexpected happened\n" + exception.getMessage());
    }
    finally
    {
      try
      {
        if(input != null)
          input.close();
      }
      catch(IOException exception)
      {
        System.err.println("Could not close input " + exception);
      }
      if(output != null)
      {
        output.close();
        if(output.checkError())
          System.out.println("Something went wrong with the output");
      }
    }
  }

  private static void readInputFile(String filename) throws IOException, FileNotFoundException
  {
    input = new BufferedReader(new FileReader(filename));
    String currentLine;
    while((currentLine = input.readLine()) != null)
    {
      if(!currentLine.equals(""))
      {
        lines.add(currentLine);
        lineCount++;
      }
    }
  }

  private static void assemble() throws AssemblyException
  {
    firstPass();
    lastMemoryAddress = lineCount - 1;
  }

  //first pass generates tokens and sets up labels, removes labels declarations
  //and replaces their usage with their integer values
  private static void firstPass() throws AssemblyException
  {
    tokens = new String[lineCount][];
    for(int index = 0; index < lineCount; index++)
    {
      tokens[index] = (lines.get(index).split(";")[0]).trim().split("\\s+"); //Removes comments and seperates tokens by whitespace
      if(!mnemonicList.contains(tokens[index][0]) && !Arrays.asList(directives).contains(tokens[index][0])) //If first token is a label
      {
        String label = tokens[index][0];
        if(Arrays.asList(labels).contains(label)) //Check if label already declared
        {
          throw new AssemblyException("Label declared again at: " + index);
        }
        else if(!Character.isAlphabetic(label.charAt(0))) //Check valid label
        {
          throw new AssemblyException("Invalid label at: " + index);
        }
        else if (tokens[index].length > 1 && tokens[index][1].equals("EQUALS")) //Check if label is for address or number
        {
          if(tokens[index].length > 2)
          {
            try
            {
              int number = parseLiteral(tokens[index][2]);
              labels.put(label, number);//
            }
            catch(InvalidLiteralException exception)
            {
              throw new AssemblyException("Invalid literal at: " + index + "\n" + exception.getMessage());
            }
          }
          else
          {
            throw new AssemblyException("EQUALS must be followd by a literal at: " + index);
          }
        }
        else
        {
          labels.put(label, index);
        }
        String[] unlabelledLine = new String[tokens[index].length - 1];
        for(int newArrayIndex = 0; newArrayIndex < unlabelledLine.length; newArrayIndex++)
        {
          unlabelledLine[newArrayIndex] = tokens[index][newArrayIndex + 1];
        }
        tokens[index] = unlabelledLine;
      }//Closes if has label
    }//Closes for each line process loop
  }

  //Converts a line into its machine code equivalent
  private static String processLine(String[] line, int lineNumber) throws AssemblyException
  {
    try
    {
      if(line.length == 0)
      {
        return "0000_0000_0000_0000_0000_0000_0000_0000";
      }
      else if(Arrays.asList(mnemonicList).contains(line[0]))
      {
        String instructionDetails = instructionDetails.get(line[0]);
        String instructionEncoding = Integer.toBinaryString(instructionDetails.substring(5));
        int argumentCount = Character.getNumericValue(instructionDetails.charAt(0));
        for(int argumentIndex = 0; argumentIndex < argumentCount; argumentIndex++)
        {
          char argumentType = instructionDetails.charAt(argumentIndex + 1);
          String currentArgument = null;
          if(argumentIndex < line.length())
          {
            currentArgument = line[argumentIndex + 1];
          }
          if(argumentType == 'R')
          {
            instructionEncoding += encodeRegister(currentArgument);
          }
          else if(argumentType == 'O')
          {
            if(currentArgument.charAt(0) == '#')
            {
              int literalValue = parseLiteral(currentArgument);
              instructionEncoding += "1" + Integer.toBinaryString(literalValue);
            }
            else
            {
              instructionEncoding += "0" + encodeRegister(currentArgument);
            }
          }
          else if(argumentType == 'F')
          {
            if(currentArgument.equals(null))
            {
              instructionEncoding += "1111"
            }
            else
            {
              try
              {
                int byteReference = parseLiteral(currentArgument);
              }
              catch(InvalidLiteralException exception)
              {
                throw new InvalidFlagException("Invalid byte flag operand at: " + lineNumber
                + "\n" + exception.getMessage());
              }
              if(byteReference < 0 || byteReference > 15)
              {
                throw new InvalidFlagException("Byte flags out of bounds at: " + lineNumber);
              }
              instructionEncoding += fixBinaryStringSize(Integer.toBinaryString(byteReference), 4);
            }
          }
        }
      }
      else if(Arrays.asList(directives).contains(line[0]))
      {
        if(line[0].equals("ORIGIN"))
        {

        }
        else if(line[0].equals("EQUALS"))
        {

        }
        else if(line[0].equals("DEFW"))
        {

        }
        else if(line[0].equals("DEFS"))
        {

        }
        else if(line[0].equals("DEFI"))
        {

        }
        else if(line[0].equals("DEFC"))
        {

        }
      }
      else
      {
        throw new InvalidMnemonicException("The mnemonic is invalid at: " + lineNumber);
      }
    }
    catch(InvalidMnemonicException | InvalidInstructionException | InvalidOperandException | InvalidLiteralException exception)
    {
      throw new AssemblyException(exception.getMessage());
    }
    catch(Exception exception)
    {
      throw new AssemblyException("Something unexpected happened");
    }
  }

  //Sets up the image and returns its address
  private int setupImage(String filepath)
  {
    return 0;
  }

  private static String encodeRegister(String argument) throws InvalidOperandException
  {
    Integer registerNumber = registers.get(currentArgument);
    if(registerNumber == null)
    {
      throw new InvalidOperandException("Invalid operand at line: " + lineNumber);
    }
    String registerEncoding = Integer.toBinaryString(registerNumber);
    while(registerEncoding.length() < 4)
    {
      registerEncoding = "0" + registerEncoding;
    }
    return registerEncoding;
  }

  private static String fixBinaryStringSize(String binaryString, int desiredLength)
  {
    while(binaryString.length() < desiredLength)
    {
      binaryString = "0" + binaryString;
    }
    return binaryString;
  }

  //Converts literals to their numerical values
  private static int parseLiteral(String literal) throws InvalidLiteralException
  {
    int number;
    if(!(literal.charAt(0) == '#'))
    {
      throw new InvalidLiteralException("# expected at start of literal");
    }
    try
    {
      if(literal.charAt(1) == 'b')
      {
        number = Integer.parseInt(literal.substring(2), 2);
      }
      else if(literal.charAt(1) == 'o')
      {
        number = Integer.parseInt(literal.substring(2), 8);
      }
      else if(literal.charAt(1) =='d')
      {
        number = Integer.parseInt(literal.substring(2));
      }
      else if(literal.charAt(1) =='h')
      {
        number = Integer.parseInt(literal.substring(2), 16);
      }
      else if(literal.charAt(1) == 'c')
      {
        number = (int) literal.charAt(2);
      }
      else//Defaults to converting literal to decimal
      {
        number = Integer.parseInt(literal.substring(1));
      }
      if(number > 32767)//Max value of literals
      {
        throw new InvalidLiteralException();
      }
      return number;
    }
    catch(Exception exception)
    {
      throw new InvalidLiteralException("Invalid literal value");
    }
  }

  private static void outputToFile(String filename) throws IOException
  {
    output = new PrintWriter(new FileWriter(filename));
  }
}
