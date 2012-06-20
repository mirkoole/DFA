import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;

public class DFA implements IDFA {

  private static int n;
  private static int[][] delta;

  public static void main(String [] args) {

    if(args.length != 3) {
      System.out.println("Usage: java DFA [n] [String] [Filename]");
      System.exit(1);
    }

    DFA.n = Integer.parseInt(args[0]);

    String input = args[1];
    String filename = args[2];

    if(1 >= DFA.n) {
      System.out.println("Error: n is not > 1");
      System.exit(2);
    }

    DFA myDFA = new DFA(input, filename);

  }

  public DFA(String input, String filename) {

    String output = "Der generierte DFA hat folgende Transitionen:\n";

    output = output.concat( getDelta() );

    if( accept(input) ){

      output = output.concat("\nDas Wort " + input + " wurde akzeptiert.\n");

    } else {

      output = output.concat("\nDas Wort " + input + " wurde abgelehnt.\n");

    }

    toFile(output, filename);
  }

  public String getDelta() {

    String output = "";
    int[][] delta = new int[DFA.n][2];

    // run trough z0...zn and 0...1
    for(int z = 0; z < delta.length; z++){
      for(int b = 0; b < 2; b++){

      // re-init on every loop
      String bin = "";
      int dec = 0;

      // decimal to binary: number of z (state/Zustand)
      bin = Integer.toBinaryString(z);
      //System.out.println(bin);
      
      // concat 0 or 1 to the binary number
      bin = bin.concat(Integer.toBinaryString(b));
      //System.out.println(bin);
      
      // binary to decimal
      dec = Integer.parseInt(bin, 2);
      //System.out.println(dec);
      
      // calculate decimal number modulo n
      dec %= DFA.n;
      //System.out.println(dec);
      
      // save result
      delta[z][b] = dec;
      
      // add the string line to the output
      output = output.concat("z" + z + " -" + b + "> z" + dec + "\n");
      
      }
    }
    
    // save table
    DFA.delta = delta;
    
    return output;  
  }
  
  public boolean accept(String s) {

    char[] input = s.toCharArray();
    int z = 0;
    int[][] delta = DFA.delta;

    for(int i = 0; i < input.length; i++){
      
      if( input[i] == '0' ){
        
        z = delta[z][0];
      
      } else if( input[i] == '1' ){
      
        z = delta[z][1];
      
      } else {
      
        System.out.println("Error: Input is not a binary number!");
        System.exit(3);
      
      }
      
    }
    
    if(0 == z){
      return true;
    }
    
    return false;
  
  }
  
  // use filewriter to write given string to given file
  public void toFile(String str, String filename) {

    Writer fw = null;
    try {
      fw = new FileWriter( filename );
      fw.write(str);
    } catch ( IOException e ){
      System.out.println("Error: Could not write to file!");
      e.printStackTrace();
    } finally {
      if( null != fw ){
        try {
          fw.close();
        } catch ( IOException e ){
          System.out.println("Error: Could not close file stream!");
          e.printStackTrace();
        }
      }
    }
  }
}