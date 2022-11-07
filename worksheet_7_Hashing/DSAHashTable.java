
/*****************************************************
 * Author : Vishmi Kalansooriya
 * Date : 23rd september 2021
 * Purpose : Implementing a hash table .
 *************************************************************/
import java.util.*;
import java.io.*;

public class DSAHashTable implements Serializable {
    private DSAHashEntry[] hashArray;
    private int count; // To keep track of the number of elemnts inserted.

    // cunstructor
    public DSAHashTable(int tableSize) {
        int actualSize = 0;
        actualSize = findNextPrime(tableSize); // Finding the nearest prime number to the entered number.
        // System.out.println(actualSize);
        hashArray = new DSAHashEntry[actualSize]; // defining the size of the array according to the user's choice.
        for (int i = 0; i < hashArray.length; i++) {
            hashArray[i] = new DSAHashEntry();
        }
        count = 0;

        // for (int i = 0; i < hashArray.length - 1; i++) {
        // hashArray[i] = null; // The initial state of the array is null.
        // }

    }

    /**********************************************************************
     * Name : hash Import : inkey (String) Export : hashIdx % hashArray.length (int)
     * Purpose : To map the keys into integer indexes in the array.
     *********************************************************************/

    private int hash(String inkey) {
        // using the java' s hashcode implementatin method. but using the prime number
        // 33 (bernstein function )
        // instead of 31 since it was found to be more effective by practise.
        int hashIdx = 0;
        for (int i = 0; i < inkey.length(); i++) {
            hashIdx = (31 * hashIdx) + inkey.charAt(i); // multiplying the hash index by the prime number 33 and adding
            // the ASCII value of characters inside the string
            // one by one to create a unique index

        }

        return Math.abs(hashIdx % hashArray.length); // To get the absolute value .
        // dividing the above created index by the array length and getting its
        // modulus to fit the size of the array.
        // To stop creating an array with an unwanted size ( a larger size than the
        // actual size needed)

    }

    /**********************************************************************
     * Name : put Import : inKey (String) , inValue (Object) Export : None Purpose :
     * To add a new element.
     *****************************************************************************************/
    public void put(String inKey, Object inValue) {
        int hashIdx = hash(inKey); // Hash the key using the hash function first.
        // System.out.println(" The hash index " + hashIdx);
        while (hashArray[hashIdx] != null && hashArray[hashIdx].getKey() != inKey) {
            hashArray[hashIdx] = new DSAHashEntry(inKey, inValue);
            hashArray[hashIdx].setState(1);
            count++;
        }

        if (getLoadFactor(count, hashArray.length) >= 0.6) // web searched.
        // if the load factor is greater than 0.6 then the table needs resizing.
        {
            int newlength = hashArray.length * 2;
            reSize(newlength); // to make a new hash table which is atleast 2 times bigger.
        }

    }

    public void put(DSAHashEntry entry) {
        put(entry.getKey(), entry.getValue());
    }

    /**********************************************************************
     * Name : hasKey Import : inKey (String) Export : true/false : Purpose : To see
     * whether the key contains in the aray.
     * 
     *****************************************************************************************/
    public boolean hasKey(String inKey) throws IllegalAccessException {
        int hashIdx = find(inKey); // getting the hashcode by the find method.
        boolean found = false;
        while (hashArray[hashIdx] != null) {
            if ((hashArray[hashIdx].getState() != 0) && (hashArray[hashIdx].getKey() == inKey)) {
                found = true;
            } else if ((hashArray[hashIdx].getState() == 0) && (hashArray[hashIdx].getKey() == inKey)) {
                found = false;
            } else {
                System.out.println(" Key cannot be found ");
            }

            hashIdx = (hashIdx + 1) % hashArray.length; // lineraly procing to calculate the hash code.

        }

        return found;

    }

    /**********************************************************************
     * Name : get Import : inKey (String) , Export : value (Object) Purpose : To
     * access an elemnt in the array.
     *****************************************************************************************/
    public Object get(String inKey) throws IllegalAccessException {
        int hashIdx = hash(inKey); // hash the String first using the hash function so that we get to know the hash
                                   // Index where it is stored in the array.

        int origIdx = hashIdx; // in case the hash table is 100% full
        boolean found = false;
        boolean giveup = false;
        Object retValue = null;

        while ((!found) && (!giveup)) {
            if (hashArray[hashIdx].getState() == 0) // check if the entry is never used and stop.
            {
                giveup = true;

            } else if (hashArray[hashIdx].getKey() == inKey) // check if the hash index contains the inserted key
            {
                found = true; // return the data inside the hash Index.
                System.out.println(" key is in the table");
                retValue = hashArray[hashIdx].getValue();

            } else {
                hashIdx = (hashIdx + stepHash(hashIdx)) % hashArray.length; // Now using step hash also to linear probe.
                if (hashIdx == origIdx) {
                    giveup = true;
                }
            }

        }
        if (!found) {
            System.out.println(" Sorry ! the key is not in the table.");
        }

        return retValue;

    }

    /**********************************************************************
     * Name : find Import : inKey (String) , Export : value (int) Purpose : To
     * search for the key within the array.
     *****************************************************************************************/
    private int find(String inKey) throws IllegalAccessException {
        int hashIdx = 0;
        int value = 0;

        if ((inKey == null) || (inKey.length() < 1)) {
            throw new IllegalAccessException(" Sorry not valid key entered "); // checking for illegal arguments in when
                                                                               // inserting the key.

        }

        hashIdx = hash(inKey); // if the arguments are correct hash the inserted key.

        while (hashArray[hashIdx] != null) {
            if (hashArray[hashIdx].getValue() == inKey) // check whether the key exists.
            {
                value = hashIdx; // assign the hash index to an int variable to return
                hashIdx++; // Increase the index

                hashIdx = hashIdx % hashArray.length; // linerly probe.
            }
        }

        return value;

    }

    /**********************************************************************
     * Name : remove Import : inKey (String) , Export : value Purpose : To remove
     * elements from the array.
     *****************************************************************************************/

    public Object remove(String inKey) throws IllegalAccessException {
        int hashIdx = hash(inKey);
        // System.out.println("hash Idx at find" + hashIdx);
        Object value = 0;
        value = hashArray[hashIdx].getValue();

        // if the state is 0 the key does not exist in the table.
        if (hashArray[hashIdx].getState() == 0) {
            System.out.println(" Sorry the elemnt you are looking for is not in the table");

        }

        while ((hashArray[hashIdx].getKey() == inKey) && (hashArray[hashIdx] != null)) {
            hashIdx = (hashIdx + stepHash(hashIdx)) % hashArray.length; // Linerly probe
            hashArray[hashIdx] = new DSAHashEntry(); // removing the ellement // freeing the space
            hashArray[hashIdx].setState(-1); // setting the state to -1 since it was formaly used
            count--; // reducing the number of elements in the array

        }
        if (getLoadFactor(count, hashArray.length) <= 0.4) // web searched // shrinking the array if the load factor is
                                                           // less than 0.4
        {
            reSize(hashArray.length / 2); // halving the array
        }
        System.out.println(" Removed the value " + value + " at " + hashIdx);

        return value;

    }

    /**********************************************************************
     * Name : findNextPrime Import : startVal (int) , Export : primeVal ( int)
     * Purpose : To find the next prime number according to the user input.(for the
     * table size.)
     *****************************************************************************************/

    private int findNextPrime(int startVal) {
        boolean isPrime = false;
        int i = 0;
        int primeVal = 0;
        int rootVal = 0;
        if (startVal % 2 == 0)

        {
            primeVal = startVal - 1; // Because even numbers can never be prime numbers.
        } else {
            primeVal = startVal;
        }

        isPrime = false;
        do {
            primeVal = primeVal + 2;
            i = 3;
            isPrime = true;
            rootVal = (int) Math.sqrt((double) primeVal); // Limiting the search upto the squareroot of the
                                                          // entered value.
            do {
                if (primeVal % i == 0) {
                    isPrime = false;
                } else {
                    i = i + 2; // skipping the even numbers in the search process.
                }
            } while ((i <= rootVal) && (isPrime));

        } while (!isPrime); // There is always a prime number to be found

        return primeVal;
    }

    /**********************************************************************
     * Name : stepHash Import : inCalHashIdx (int) , Export : newHashIdx (int) the
     * new hashIdx Purpose : to implement the double hashing method.
     *****************************************************************************************/

    private int stepHash(int inCalHashIdx) {
        int maximumStep = 0;
        int newHashIdx = 0;
        maximumStep = findNextPrime(hashArray.length % 2); // To find the new maximum step.
        newHashIdx = maximumStep - (inCalHashIdx % maximumStep); // Making a new hash index.
        return newHashIdx; // returning the new hash index.

    }

    /**********************************************************************
     * Name : getLoadFactor Import : count (int) size (int) , Export : loadFactor
     * (double) Purpose : to calculate the load factor to se how full the array is.
     *****************************************************************************************/

    public double getLoadFactor(int count, int size) {
        // imports the number of existing elements in the array and the size of the
        // array
        double loadFactor = count / size;
        return loadFactor;

    }

    /**********************************************************************
     * Name : reSize Import : arraySize (int) , Export :None Purpose : to change the
     * size of the array.
     *****************************************************************************************/

    private void reSize(int arraySize) {
        int newSize = findNextPrime(arraySize); // finding the nearest prime number to the newSize.
        count = 0; // making the number of elemnts inside the array to 0.

        DSAHashEntry newArray[] = new DSAHashEntry[newSize]; // A new array is created using the new array size.
        DSAHashEntry copy[] = hashArray;
        System.out.println(" Resized the hash table to " + newSize); // A message for the user to know when resizing is
                                                                     // done.
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new DSAHashEntry();
        }

        hashArray = newArray;

        for (int i = 0; i < copy.length; i++)
        // To insert all elemnts inside the old array into the new array.
        {
            if (copy[i].getState() == 1) {
                String inKey = copy[i].getKey();
                Object inValue = copy[i].getValue();
                put(inKey, inValue);

            }
        }

    }

    /**********************************************************************
     * Name : printTable Import : None , Export :None Purpose : to Print the hash
     * table.
     *****************************************************************************************/

    public void printTable() {
        for (int i = 0; i < hashArray.length; i++) {
            {
                if (hashArray[i].getState() == 1) // Checking whether there is a key/object at the index.
                    System.out.println(hashArray[i].getKey() + " " + hashArray[i].getValue());
            }
        }
    }

    /**********************************************************************
     * Name : saveFile Import : None , Export :None Purpose : to write the Hash
     * Table to a csv.
     *****************************************************************************************/

    public void saveFile() {

        FileOutputStream fileStrm = null;
        PrintWriter pw;

        try {

            pw = new PrintWriter(new FileOutputStream(new File("saveToFIle.csv"), true));
            for (int i = 0; i < hashArray.length; i++) {
                if (hashArray[i].getState() != 0) // check whether there is a key/object at the index.
                {

                    String key = hashArray[i].getKey();

                    Object value = hashArray[i].getValue();

                    pw.write(key);
                    pw.write(","); // seperate the data by commas

                    pw.write((String) (value));
                    pw.write("\r\n"); // To print line by line.
                }

            }

            pw.close();
        } catch (IOException e) {
            System.out.println("Error in writing to file: " + e.getMessage());

        }
    }

    /**********************************************************************
     * Name : load Import : fileName (string) , Export :None Purpose : to read a
     * serialized file.
     *****************************************************************************************/

    public DSAHashTable load(String fileName) throws IllegalArgumentException {
        FileInputStream fileStrm;
        ObjectInputStream objStrm;
        DSAHashTable inObject = null;

        try {
            fileStrm = new FileInputStream(fileName);
            objStrm = new ObjectInputStream(fileStrm);
            inObject = (DSAHashTable) objStrm.readObject();
            objStrm.close();
        } catch (ClassNotFoundException error) {
            System.out.println(" Class DSALinkedList class not found" + error);

        } catch (Exception error) {
            throw new IllegalArgumentException("Unable to load the object from the file." + error.getMessage());
        }

        return inObject;
    }

    /**********************************************************************
     * Name : save Import : objToSave (DSAHashTable) , Export :None Purpose : write
     * a serialized file.
     *****************************************************************************************/

    public static void save(DSAHashTable objToSave, String fileName) {
        FileOutputStream fileStrm;
        ObjectOutputStream objStrm;

        try {
            fileStrm = new FileOutputStream(fileName);
            objStrm = new ObjectOutputStream(fileStrm);
            objStrm.writeObject(objToSave);

            objStrm.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

}
