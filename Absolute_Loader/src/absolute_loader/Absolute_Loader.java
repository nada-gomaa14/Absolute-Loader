
package absolute_loader;

import java.io.*;
import java.util.*;

public class Absolute_Loader {
    static List<String> lines = new ArrayList<>();      //Array list of HTE records
    static String memory[][] = new String[21][16];     //Array representing memeory
    
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("inLoaders.txt");      //Open file
        Scanner s = new Scanner(file);      //Open scanner
        
        //Count number of lines in the file (except blank)
        while (s.hasNextLine()) {
            String currentLine = s.nextLine().trim();
            if (!currentLine.isBlank()) {
                lines.add(currentLine);
            }
        }
        s.close();      //Close scanner
        
        int prog1 = 0, prog2 = 0, prog3 = 0;        //Default values
        
        //Extract starting index of each program
        for (int i=0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("H")) {     //Find H record
                if (line.contains("PROG1")) {       //Get PROG1 start
                    prog1 = i;
                } else if (line.contains("PROG2")) {        //Get PROG2 start
                    prog2 = i;
                } else if (line.contains("PROG3")) {        //Get PROG3 start
                    prog3 = i;
                }
            }
        }
        
        //Load programs to memory
        absolute(prog2);
        absolute(prog3);
        absolute(prog1);
                
        printMemory();
    }
    
    //Method to run absolute loader
    public static void absolute(int prog){
        String line, start, record;
        int Trecord = prog + 3;
        for(int i=Trecord; i < lines.size(); i++){       //Start at first T record
            line = lines.get(i);
            if(line.startsWith("E"))        //Stop at E record
                break;
            
            if(line.startsWith("T")){       //Make sure it's a T record
                start = line.substring(2, 8).trim();        //Extract starting addess
                record = line.substring(12).trim();     //Extract T record
                
                load(start, record);    //Load record to memory
            }
        }
    }
    
    //Method to load T record to memory
    public static void load(String start, String record){
        int startAddress = Integer.parseInt(start, 16);     //Convert starting address to decimal
        int row = startAddress / 16;        //Get row of starting address
        int column = startAddress % 16;     //Get column of starting address
        
        for(int i=0; i<record.length()-1; i+=2){
            String data = record.substring(i, i + 2);       //Extract every 2 hexadecimal digits
            memory[row][column] = data;     //Add data to memory cell
            column++;       //Move to next column
            //If end of row, reset column and increment row
            if(column >= 16){
                column = 0;
                row++;
            }
        }
    }
    
    //Method to print memory
    public static void printMemory(){
        System.out.println("Addr\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\tA\tB\tC\tD\tE\tF");
        
        String row = "0000";
        for(int i=0; i<memory.length; i++){
            for(int j=0; j<16; j++){
                if(j==0){
                    System.out.print(row + "\t");
                    row = String.format("%04X", Integer.parseInt(row, 16) + 16);
                }    
                if(memory[i][j] == null)
                    memory[i][j]= "--";
                System.out.print(memory[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
