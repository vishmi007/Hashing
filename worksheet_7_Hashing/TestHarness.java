
/*****************************************************
 * Author : Vishmi Kalansooriya
 * Date : 23rd september 2021
 * Purpose : Testing the hash table.
 *************************************************************/
import java.util.Arrays;
import java.io.*;
import java.util.*;

public class TestHarness {
    public static void main(String args[]) throws IllegalAccessException {

        testResizing();
        // testLargeDataFile();

    }

    /*****************************************************
     * Author : readFile Import : None Export : table (DSAHashTable) Purpose: To
     * read a csv file and store the elements in the hash table.
     *************************************************************/

    public static DSAHashTable readFile() throws IllegalAccessException {
        // Reading the CSV file.
        DSAHashTable table = new DSAHashTable(7000);
        FileInputStream fileStream = null;
        InputStreamReader rdr;
        BufferedReader bufRdr;
        int lineNum;
        String line;
        try {
            fileStream = new FileInputStream("RandomNames.csv"); // Reading the csv file and keys and values in the hash
                                                                 // table.

            rdr = new InputStreamReader(fileStream);
            bufRdr = new BufferedReader(rdr);
            lineNum = 0;

            // bufRdr.readLine();
            line = bufRdr.readLine();

            while (line != null) {
                lineNum++;
                String[] splitLine;
                splitLine = line.split(",");

                {

                    table.put(splitLine[1], splitLine[0]); // storing the valies in the hash table.

                }

                line = bufRdr.readLine();
            }
            fileStream.close();

        } catch (IOException errorDetails) {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException ex2) {
                }
            }
            System.out.println("Error in fileProcessing: " + errorDetails.getMessage());
            System.out.println("Sorry we could not find the file you are reffering to");
        }

        return table;
    }

    /*****************************************************
     * Name : testResizing Import : None Export : None Purpose: To test the resizing
     * methods along with the put,get and remove methods in the DSAHashtable class.
     ******************************************************************************************************************/
    public static void testResizing() throws IllegalAccessException {
        // Intilializing the size of the table as 10.
        DSAHashTable table = new DSAHashTable(10);

        // adding values to the hashtable until the loadFactor is triggered and expands.
        System.out.println(" checking the growth of the table along with the put method \n");
        table.put("apple", 1);
        table.put("orange", 2);
        table.put("grapes", 5);
        table.put("papaya", 9);
        table.put("mango", 11);
        table.put("lemon", 1);
        table.put("guava", 2);
        table.put("pine apple", 6);
        table.put("peaches", 7);
        table.put("avacado", 15); // The load factor triggers when the 11th element is inserted.
        table.put("manderine", 9);

        table.printTable();

        // Removing an element as soon as the hash table is expanded to see whether it
        // shrinks back to the orginal.
        System.out.println(" checking whether the table shrinks along with the remove method\n");
        table.remove("apple");

        System.out.println();

        // To see whether all the values are in the array after resizing.
        System.out.println(" checking whether all the data were transfered to the new array along with the get method");
        System.out.println(" The value at the key is  " + table.get("peaches"));
        System.out.println(table.get("orange"));

    }

    /*****************************************************
     * Name : testLargeDataFile Import : None Export : None Purpose: To test the
     * methods with larger data files.
     ******************************************************************************************************************/

    public static void testLargeDataFile() throws IllegalAccessException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please choose an option from the below options");
        System.out.println(" 1.Read a CSV \n 2.Write a CSV \n 3.Write a serialized file \n 4.Read a serialized file");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                DSAHashTable getTable = readFile();
                getTable.printTable();
                break;

            case 2:
                DSAHashTable table = readFile();
                table.saveFile();
                break;

            case 3:
                DSAHashTable newTable = readFile();
                newTable.save(newTable, "writeSerializedFile.xml");
                break;

            case 4:
                DSAHashTable intable = new DSAHashTable(700);
                intable.load("writeSerializedFile.xml");
                intable.get("Clinton Blumer");

        }

    }

}
