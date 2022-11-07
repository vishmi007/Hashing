import java.io.Serializable;

/*****************************************************
 * Author : Vishmi Kalansooriya Date : 23rd september 2021 Purpose :
 * Implementing the HashEntry class .
 *************************************************************/
public class DSAHashEntry implements Serializable {

    private String key;
    private Object value;
    private int state; // to see if the block was never used/ was used or currently using.

    // 0 will be used to denote that the index was never used, 1 will be used to
    // denote that there is a key currently using the index
    // -1 will be used to denote that the index was used earlier.

    // Default cunstructor.
    DSAHashEntry() {
        key = " ";
        value = null;
        state = 0; // at the start the initial state of every index would be 0 because it has not
                   // been used before.
    }

    // Parameter cunstructor
    DSAHashEntry(String inkey, Object inValue) {
        key = inkey;
        value = inValue;
        state = 1;

    }

    // getters
    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public int getState() {
        return state;
    }

    // setters
    public void setKey(String inKey) {
        key = inKey;
    }

    public void setValue(Object inValue) {
        value = inValue;
    }

    public void setState(int inState) {
        int state = inState;

    }

}
