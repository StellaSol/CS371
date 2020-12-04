
/**
 * References used: https://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit
 *                  https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
 *                  https://stefanbirkner.github.io/system-rules/
 *                  https://stackoverflow.com/questions/42786266/junit-catch-exceptions-in-tests
 *                  https://stackabuse.com/how-to-make-custom-exceptions-in-java/
 * Your Junit test must test the following cases:
    1)For all methods, what if the PageTable is empty? This is a corner case.
        -put
        -get
        -containsVPN
        -remove
    2)For contains, get and remove, what if the PageTable is not empty, and it doesn't have the key you try to get/remove? This is a negative case.
        - Have error message
    3)For contains, get and remove, what if the PageTable is not empty, and it DOES have the key you try to get/remove? This is a positive case.
    4)For put, what if the PageTable is not empty, and it DOES have the key you try to put?  This is a positive case. IF IT DOES have the key, no duplicates? 
    5)For put, what if the PageTable is not empty, and it does not have the key you try to put?  This is a negative case.
    
    **** 

    In each negative case, your junit test must check if the method exited safely and it is up to you if you'd like to use an additional exception/error message.  
    If it is negative, you can make it throw an exception, and try to catch it and within catch, assert true means the negative case fails as you expected. failing as expected is a "safe out"
    In each positive case, your junit test must check if the method functioned as expected: did the newly added item show up? did the deleted item disappear? did the search find the right stuff in the table? etc.  
    Each corner case needs to be handled case-by-case
 */

// import java.io.ByteArrayOutputStream;
// import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MyPageTableTest {
    // private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    // private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    // private final PrintStream originalOut = System.out;
    // private final PrintStream originalErr = System.err;

    static final int TEST_SIZE = 128;// Filling Half of Page Table

    // put 512 and use pfn 1 so the key would be 512
    @Test
    public void test1_pageTableEmpty(){
        MyPageTable myPageTable = new MyPageTable();
    }


    @Test
    public void test2_contains_negativeCase() throws KeyAlreadyExistException {
        /**
         * To Do:
         * - make sure each method exits safely
         * - use additional error message
        */ 
        MyPageTable myPageTable = new MyPageTable();
        myPageTable.put(512,1);
        boolean testContains = myPageTable.containsVPN(256);
        assertEquals(false, testContains);
    }

    @Test
    public void test3_get_negativeCase() throws KeyAlreadyExistException {
        /**
         * To Do:
         * - make sure each method exits safely
         * - use additional error message
        */ 
        MyPageTable myPageTable = new MyPageTable();
        myPageTable.put(512,1);
        int testGet = myPageTable.get_ptable_pfn(256);
        assertEquals(-1, testGet);
    }

    // @Before
    // public void setUpStreams() {
    //     System.setOut(new PrintStream(outContent));
    //     System.setErr(new PrintStream(errContent));
    // }
    @Test
    public void test4_remove_negativeCase() throws NoKeyException,KeyAlreadyExistException { //Page Table is NOT empty, and DOES NOT have the key
        /**
         * To Do:
         * - make sure each method exits safely
         * - use additional error message
        */ 
        try {
            MyPageTable myPageTable = new MyPageTable();
            myPageTable.put(512,1);
            myPageTable.removePTE(256);
        } catch (NoKeyException e) {
           assertEquals("Key does NOT exist", e.getMessage());
        }
        //assertEquals("Error - No such item in the PageTable.", errContent.toString());
    }

    // @After
    // public void restoreStreams() {
    //     System.setOut(originalOut);
    //     System.setErr(originalErr);
    // }

    @Test
    public void test5_contains_get_remove_positiveCase() throws NoKeyException, KeyAlreadyExistException { //Page Table is NOT empty, so it has the key, check if contains, get, remove works
        boolean result1 = true;
        boolean result2 = true;
        boolean result3 = true;

        boolean testContains;
        int testGet;

        MyPageTable myPageTable = new MyPageTable();
        for(int i = 0; i < TEST_SIZE ; i++){ //Filling half of Page Table since we 
            myPageTable.put(i,i);
        }

        for(int j = 0; j < TEST_SIZE;j++){ //Check if contains work
            testContains = myPageTable.containsVPN(j);
            if(testContains != true){
                result1 = false;
            }
        }

        for(int k = 0; k < TEST_SIZE; k++){ //Check if gets method obtains the right value
            testGet = myPageTable.get_ptable_pfn(k);
            if(testGet != k ){
                result2 = false;
            }
        }

        for(int l = 0; l < TEST_SIZE; l++){
            myPageTable.removePTE(l);
        }

        for(int m = 0; m < TEST_SIZE; m++){
            testContains = myPageTable.containsVPN(m); //Check if the remove method actually removes PTE
            if(testContains != false){ //if it is true, then PTE is still their, remove method invalid
                result1 = false;
            }
        }
        assertEquals(true, result1);
        assertEquals(true, result2);
        assertEquals(true, result3);
    }

    @Test
    public void test6_put_negativeCase() throws KeyAlreadyExistException{ //Page Table is NOT empty, but it does have the key
        MyPageTable myPageTable = new MyPageTable();
        for(int i = 0; i < TEST_SIZE ; i++){ //Page Table is not empty so it already has values
            myPageTable.put(i,i);
        }

        try{
            for(int j = 0; j < TEST_SIZE; j++){ //Putting values that already exist, should get an exception
                myPageTable.put(j,j);
            } 
        }catch(KeyAlreadyExistException e){
            assertEquals("Key Already Exists", e.getMessage());
        }
    }

    @Test
    public void test7_put_positiveCase() throws KeyAlreadyExistException { // Page Table NOT empty, but does not have the key
        boolean testContains;
        boolean result1 = true;

        MyPageTable myPageTable = new MyPageTable();
        for(int i = 0; i < TEST_SIZE/2 ; i++){ //Page Table is not empty so it already has values
            myPageTable.put(i,i);
        }

        for(int j = 0; j < TEST_SIZE; j++){
            testContains = myPageTable.containsVPN(j);
            if(testContains != true){ //If the PageTable does not contain the key that you try to put it, put it in the Page table
                myPageTable.put(j,j);
            }
        }

        for(int k = 0; k < TEST_SIZE; k++){ //Check if Page Table contains the keys that were added including the existing ones  
            testContains = myPageTable.containsVPN(k);
            if(testContains != true ){
                result1 = false;
            }
        }
        assertEquals(true,result1);
    }
}
