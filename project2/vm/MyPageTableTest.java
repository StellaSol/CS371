/**
 * References used: https://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit
 *                  https://stackoverflow.com/questions/42786266/junit-catch-exceptions-in-tests
 *                  https://stackabuse.com/how-to-make-custom-exceptions-in-java/
 * 
 * Notes: 
 * Junit test must test the following cases:
    1)For all methods, what if the PageTable is empty? This is a corner case.
        -put
        -get
        -containsVPN
        -remove
    2)For contains, get and remove, what if the PageTable is not empty, and it doesn't have the key you try to get/remove? This is a negative case.
        - Have error message
    3)For contains, get and remove, what if the PageTable is not empty, and it DOES have the key you try to get/remove? This is a positive case.
    4)For put, what if the PageTable is not empty, and it DOES have the key you try to put?  This is a negative case. 
    5)For put, what if the PageTable is not empty, and it does not have the key you try to put?  This is a positive case.
    
    -In each negative case, your junit test must check if the method exited safely and it is up to you if you'd like to use an additional exception/error message.  
        -you can make it throw an exception, and try to catch it and within catch, assert true means the negative case fails as you expected. failing as expected is a "safe out"
    -In each positive case, your junit test must check if the method functioned as expected: did the newly added item show up? did the deleted item disappear? did the search find the right stuff in the table? etc.  
    -Each corner case needs to be handled case-by-case
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class MyPageTableTest {
    static final int TEST_SIZE = 128;// Filling Half of Page Table

    @Test
    public void test1_contains_negativeCase() throws KeyAlreadyExistException { //Page Table is NOT empty, and DOES NOT have the key. Expected result: false, which was already handled in the method containsVPN()
        boolean contains = true;
        MyPageTable myPageTable = new MyPageTable();
        assert(myPageTable!=null);
        for(int i = 0; i < TEST_SIZE/2 ; i++){ //Filling only the  HALF OF TEST_SIZE  so it has keys 0-63
            myPageTable.put(i,i);
        }
        for(int j = 0; j < TEST_SIZE; j++){ //Contains will be true ONLY for keys 0-63, once it hits 64, it should be FALSE
            contains = myPageTable.containsVPN(j);
            if(contains != true){ //keys 64-127 are NOT in the page table, so contains SHOULD BE false
                contains = false;
            }
        }
        assertEquals(false, contains);
    }

    @Test
    public void test2_get_negativeCase() throws NoKeyException,KeyAlreadyExistException  {//Page Table is NOT empty, and DOES NOT have the key, Result is exception
        boolean exception_happened = false;
        int testGet;
        MyPageTable myPageTable = new MyPageTable();
        assert(myPageTable!=null);

        for(int i = 0; i < TEST_SIZE/2 ; i++){ //Filling only HALF OF TEST_SIZE so that Page Table is NOT empty
            myPageTable.put(i,i);
        }

        try {
            for(int j = 0; j<TEST_SIZE; j++){ //Only keys 0-63 exist, once it hits 64, exception should happen
                testGet = myPageTable.get_ptable_pfn(j);
            }
        } catch (NoKeyException e) {
            exception_happened  = true;
        }
        assertEquals(true,exception_happened);
    }

    @Test
    public void test3_remove_negativeCase() throws NoKeyException,KeyAlreadyExistException { //Page Table is NOT empty, and DOES NOT have the key, Result is exception
        boolean exception_happened = false; 
        MyPageTable myPageTable = new MyPageTable();
        assert(myPageTable!=null);

        for(int i = 0; i < TEST_SIZE/2 ; i++){ //Filling only HALF OF TEST_SIZE so that Page Table is NOT empty
            myPageTable.put(i,i);
        }
        try {
            for(int j = 0; j<TEST_SIZE; j++){ //Only keys 0-63 exist, once it hits 64, exception happens
                myPageTable.removePTE(j);
            }
        } catch (NoKeyException e) {
            exception_happened  = true;
        }
        assertEquals(true,exception_happened);
    }

    @Test
    public void test4_contains_get_remove_positiveCase() throws NoKeyException, KeyAlreadyExistException { //Page Table is NOT empty, so it has the key, check if contains, get, remove works
        boolean result1 = true;
        boolean result2 = true;
        boolean result3 = true;

        boolean testContains;
        int testGet;

        MyPageTable myPageTable = new MyPageTable();
        assert(myPageTable!=null);

        for(int i = 0; i < TEST_SIZE ; i++){ //Filling Page Table 
            myPageTable.put(i,i);
        }

        //test Contains 
        for(int j = 0; j < TEST_SIZE;j++){ //Check if contains work
            testContains = myPageTable.containsVPN(j);
            if(testContains != true){
                result1 = false;
            }
        }
    
        //test Get
        for(int k = 0; k < TEST_SIZE; k++){ //Check if gets method obtains the right value
            testGet = myPageTable.get_ptable_pfn(k);
            if(testGet != k ){
                result2 = false;
            }
        }

        //test Remove
        for(int l = 0; l < TEST_SIZE; l++){
            myPageTable.removePTE(l);
        }

        for(int m = 0; m < TEST_SIZE; m++){
            testContains = myPageTable.containsVPN(m); //Check if the remove method actually removes PTE
            if(testContains != false){ //if it is true, then PTE is still there and the remove method did not do its job correctly
                result1 = false;
            }
        }
        assertEquals(true, result1);
        assertEquals(true, result2);
        assertEquals(true, result3);
    }

    //@Test(expected = KeyAlreadyExistException.class)  
    @Test
    public void test5_put_negativeCase() throws KeyAlreadyExistException{ //Page Table is NOT empty, but it does have the key so it exists already
        boolean exception_happened = false; 
        MyPageTable myPageTable = new MyPageTable();
        assert(myPageTable!=null);

        for(int i = 0; i < TEST_SIZE ; i++){ //Page Table is not empty so it already has values
            myPageTable.put(i,i);
        }
        try{
            for(int j = 0; j < TEST_SIZE; j++){ //Putting values that already exist, should get an exception
                myPageTable.put(j,j);
            } 
        } catch(KeyAlreadyExistException e){ // We want that exception to happen since we are putting existing values
            exception_happened = true;
        }
        assertEquals(true,exception_happened);
    }

    @Test
    public void test6_put_positiveCase() throws KeyAlreadyExistException { // Page Table NOT empty, but does not have the key, expected result = make sure keys are put succcessfully
        boolean testContains;
        boolean result1 = true;

        MyPageTable myPageTable = new MyPageTable();
        assert(myPageTable!=null);

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
            testContains = myPageTable.containsVPN(k); //we want to make sure we successfully put it in the Page Table
            if(testContains != true ){ //if it is NOT true, then it is NOT in the page table
                result1 = false;
            }
        }
        assertEquals(true,result1);
    }
}