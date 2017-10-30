
import java.util.HashMap;
public class LanguageData
{
  public static HashMap<String, Integer> generateRegisters()
  {
    HashMap<String, Integer> registers = new HashMap<String, Integer>();
    registers.put("R0", 0);
    registers.put("R1", 1);
    registers.put("R2", 2);
    registers.put("R3", 3);
    registers.put("R4", 4);
    registers.put("R5", 5);
    registers.put("R6", 6);
    registers.put("R7", 7);
    registers.put("R8", 8);
    registers.put("R9", 9);
    registers.put("R10", 10);
    registers.put("R11", 11);
    registers.put("R12", 12);
    registers.put("SP", 3);
    registers.put("R13", 13);
    registers.put("LK", 13);
    registers.put("R14", 14);
    registers.put("PC", 14);
    registers.put("R15", 15);
    registers.put("PO", 16);
    return registers;
  }

  public static HashMap<String, String> generateCommands()
  {
    HashMap commands = new HashMap<String, String>();
    commands.put("NOP", "0----00");
    //Data instructions
    commands.put("LDR", "2ROF-01");
    commands.put("STR", "2ROF-02");
    commands.put("LDM", "3RRO-03");
    commands.put("STM", "3RRO-04");
    commands.put("TFB", "3RRO-05");
    commands.put("MOV", "2RO--06");
    //Control instructions
    commands.put("BRL", "1O---07");
    commands.put("MSG", "1O---08");
    commands.put("OSC", "3O---09");
    commands.put("TST", "2RO--10");
    commands.put("CMP", "2RO--11");
    commands.put("CMN", "2RO--12");
    //Arithmetic instructions
    commands.put("ADD", "3RRO-13");
    commands.put("SUB", "3RRO-14");
    commands.put("RSB", "3RRO-15");
    commands.put("MUL", "3RRO-16");
    commands.put("MLA", "4RRRR17");
    //Logic instructions
    commands.put("AND", "3RRO-18");
    commands.put("ORR", "3RRO-19");
    commands.put("EOR", "3RRO-20");
    commands.put("NOT", "2RO--21");
    commands.put("BIC", "3RRO-22");
    commands.put("LSR", "3RRO-23");
    commands.put("LSL", "3RRO-24");
    commands.put("ASR", "3RRO-25");
    commands.put("ROR", "3RRO-26");
    return commands;
  }

  public static generateConditionCodes()
  {
    HashMap<String, String> conditionCodes = new HashMap<String, String>();
    conditionCodes.put("", "000");
    conditionCodes.put("AL", "000");
    conditionCodes.put("EQ", "001");
    conditionCodes.put("NE", "010");
    conditionCodes.put("GT", "011");
    conditionCodes.put("LT", "100");
    conditionCodes.put("GE", "101");
    conditionCodes.put("LE", "110");
    conditionCodes.put("CS", "111");
  }
}
