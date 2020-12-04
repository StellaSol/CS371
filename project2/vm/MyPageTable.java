/*
REFERENCES USED: 
Array of Linked Lists: https://www.cs.cmu.edu/~ab/15-123S09/lectures/Lecture%2011%20%20-%20%20Array%20of%20Linked%20Lists.pdf
Java Standard Linked List: https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html
Hash Table:  https://www.sanfoundry.com/c-program-implement-hash-tables-chaining-with-singly-linked-lists/
          : https://stackoverflow.com/questions/33903852/java-how-to-iterate-through-an-array-of-type-linked-lists-and-append-those-to-a
*/

import java.util.*;

public class MyPageTable{
    //PAGE TABLE ATTRIBUTES
    private static int INITIAL_SIZE = 256;
    private LinkedList<PTE>[] PageTable = new LinkedList [INITIAL_SIZE];
    private LinkedList<PTE> DirtyBitsList = new LinkedList<PTE>(); //List to keep the PTE's that have been written to physical memory
    private int indexOfPageTable;
    private PTE head;

    //PAGE TABLE CONSTRUCTOR
    public MyPageTable(){
        //Initializes the Page Table according to size
        for (int i = 0; i < INITIAL_SIZE; i++) {
            PageTable[i] = new LinkedList<PTE>();
        }
    }

    //PRIVATE PTE NODE CLASS 
     static class PTE{
        //PTE ATTRIBUTES
        int vpn;
        int pfn; 
        boolean dirty;

        //PTE CONSTRUCTOR
        public PTE(int vpn, int pfn, boolean dirty){
            this.vpn = vpn;
            this.pfn = pfn;
            this.dirty = dirty;
        }
        
        //Getters of VPN, PFN, Dirty
        public int getVPN(){
            return vpn;
        }
        
        public int getPFN(){
            return pfn;
        }

        public void setDirtyBit(boolean dirty){
            this.dirty=dirty;
        }

        public boolean getDirtyBit(){
            return dirty;
        }
    }
  

    //PAGE TABLE METHOD STARTS HERE

    //Hash function to get the key
    public int hash(int hashcode){
        indexOfPageTable = hashcode % INITIAL_SIZE;
        return indexOfPageTable;
    }
    
    //Inserting an entry into Page Table in accordance to its key
    public void put(int hashcode, int pfn) throws KeyAlreadyExistException{
        int key = hash(hashcode); // Retrieve the index
        PTE headPTE; 
        int getCounter = 0;
        ListIterator<PTE> it = null;
        it = PageTable[key].listIterator();   
        //now you have the key to the bucket, iterate through the list to match the vpn and obtain pfn
        while(it.hasNext()){
            headPTE = PageTable[key].get(getCounter);
            if(headPTE.vpn == hashcode){ 
                throw new KeyAlreadyExistException("Key Already Exists");
            }
            else{
                getCounter++;
                it.next();
            }
        }
        head = new PTE(hashcode, pfn, true); //Make an entry with the hashCode/vpn, pfn, dirty bit
        //add entry to both the Page Table and Dirty List
        PageTable[key].add(head); 
        DirtyBitsList.add(head);
    }

    //Iterating through the Page Table to check if vpn is there. Will return its corresponding pfn
    public int get_ptable_pfn(int hashCode) throws NoKeyException{ //this is the get method because it returns the pfn
        PTE headPTE; 
        int getCounter = 0;
        int key = hash(hashCode); //get the key to traverse through the Page Table
        boolean contains = false;
        ListIterator<PTE> it = null;
        it = PageTable[key].listIterator();   

        //now you have the key to the bucket, iterate through the list to match the vpn and obtain pfn
        while(it.hasNext()){
            headPTE = PageTable[key].get(getCounter);
            if(headPTE.vpn == hashCode){ 
                contains = true;
                return headPTE.getPFN();
            }
            else{
                getCounter++;
                it.next();
            }
        }
        if (contains!=true){
            throw new NoKeyException("Key does NOT exist");
        }
        return -1; //cant return 0 since it is possible that a pfn can be 0 
    }

    //so a boolean value can be return instead of its associated pfn
    public boolean containsVPN(int hashCode){  
        PTE headPTE; 
        int getCounter = 0;
        int key = hash(hashCode); //get the key to traverse through the Page Table
     
        ListIterator<PTE> it = null;
        it = PageTable[key].listIterator();   
        //now you have the key to the bucket, iterate through the list to match the vpn and return a boolean value
        while(it.hasNext()){
            headPTE = PageTable[key].get(getCounter);
            if(headPTE.vpn == hashCode){ 
                return true;
            }
            else{
                getCounter++;
                it.next();
            }
        }
        return false;
    }

    //Remove an entry based on the vpn
    public void removePTE(int hashCode) throws NoKeyException{
        PTE headPTE; 
        int getCounter = 0;
        int key = hash(hashCode); //get the key to traverse through the Page Table
        ListIterator<PTE> it = null;
        it = PageTable[key].listIterator();
        boolean removed = false;
        while(it.hasNext()){
            headPTE = PageTable[key].get(getCounter);
            if(headPTE.vpn == hashCode){ 
                PageTable[key].remove(getCounter);
                removed = true;
            }
            else{
                getCounter++;
                it.next();
            }
        }
        if (removed != true){
            throw new NoKeyException("Key does NOT exist");
        }
    }


    public LinkedList<PTE> returnDirtyBitsList() {
       return DirtyBitsList;
    }

    public void clearDirtyBitsList() {
        DirtyBitsList.clear();
    }

    public int size(){ //TO be used in Virt Memory
        return PageTable.length;
    }

}
