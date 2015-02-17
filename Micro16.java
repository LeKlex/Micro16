
package micro16;


import micro16.M16Lexer;
import java.util.*;


/**
 *
 * @author Alexander Poschenreithner <alexander.poschenreithner@gmail.com>
 */
public class Micro16 {


    /**
     * oputput
     */
    protected static String[] out;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String line = new String();

        M16Lexer l = new M16Lexer();

        while (sc.hasNextLine()) {
            line = sc.nextLine(); //Read line
             l.syntax(line);
        }

    }


    protected static String lexer (String line) {

        //[rR][01234567]\<[rR][01234567]\+[rR][01234567]


        String patternMove = "[rR]";



        return new String();
    }




    /*protected static void readFromFile() {

        //reading file line by line in Java using BufferedReader
        FileInputStream fis = null;
        BufferedReader reader = null;

        try {
        fis = new FileInputStream("C:/sample.txt");
        reader = new BufferedReader(new InputStreamReader(fis));

        System.out.println("Reading File line by line using BufferedReader");

        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();

        }

        } catch (FileNotFoundException ex) {
            ;
        } catch (IOException ex) {
            ;

        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
                ;
            }
        }


    }*/

}
