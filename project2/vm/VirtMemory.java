
/**
 * Notes:
 * currBlock = vpn
 * Dirty Bit:
 * -TRUE: It's been written to the physical memory 
 * -FALSE: It's been stored to to the DISK so it is not longer dirty / Eviction is happening on a dirty page 
 * For write and read: MUST load the page before doing any rights or read
 * write(): If it is within the same block(same vpn), same PFN but ofc the PhyAddr will be different due to the offset
 * StartAddr = PFN * 64 
 * Eviction: 
 * -boolean isEvicted will be set to false
 * -WILL be true when it is removed in the policy
 * -if isEvicted = TRUE, then go to that physical addr and STORE it to the disk  
 * write_back(): Iterate through the list and store all dirtied pages into the disk
 */
import Storage.PhyMemory;
import java.util.*;

public class VirtMemory extends Memory{
    //ATTRIBUTES
    MyPageTable PageTable = new MyPageTable();
    PhyMemory phyRam = new PhyMemory();
    Policy policy = new Policy(phyRam.num_frames());
    PolicyConsultant getPFN;
    
    int virtMemSize; 
    int phyMemAddr;
    int startAddr;
    int numWriteBacks = 0;

    int vpn;
    int pfn; 
    int offset;
    int sameBlockPFN;
    int currBlock;
    byte value;

   //CONSTRUCTORS
    public VirtMemory(){
        virtMemSize = 1024 * 64; //64KB 
        super.ram = phyRam;
    }

    public VirtMemory(int size){
        this.virtMemSize = size;
    }

    //RETRIEVING THE PFN THROUGH THE POLICY CONSULTANT 
    public static class PolicyConsultant{
        int evictedPFN;
        boolean isEvicted;
        boolean justGotRemoved;

        public PolicyConsultant(int evictedPFN, boolean isEvicted,boolean justGotRemoved) {
            this.evictedPFN = evictedPFN;
            this.isEvicted = isEvicted;
            this.justGotRemoved = justGotRemoved;
        }
    }

    //METHODS
    @Override
    public void write(int addr, byte value){
        
        vpn = addr / 64; //HashCode
        offset = addr % 64;
        int numPhyFrames = ram.num_frames(); // retrieve the max number of physical frames to check for out of bounds
        
        //OUT OF BOUNDS CHECKER
        if(vpn >= numPhyFrames * 64){
            System.err.print("Out of Bounds");
        }

        else{
            //Checking to see if it is in the page table
            pfn = PageTable.get_ptable_pfn(vpn);
                // If it is NOT in the Page Table then create an entry and write it to physical memory 
                 if(pfn == -1 ){
                    System.err.print("Page Fault");
                    getPFN = policy.consult(); //retrieving a PFN from the PolicyConsultant
                    int tempStartAddr = getPFN.evictedPFN * 64;
                    phyRam.load(vpn,tempStartAddr); // Load the page before any writes

                    //if eviction is true, then write to PA. 
                    if(getPFN.isEvicted = true){ 
                        startAddr = getPFN.evictedPFN * 64;  //getting the StartAddr to LOAD
                        pfn = getPFN.evictedPFN;
                        try {
                            PageTable.put(vpn, pfn);
                        } catch (KeyAlreadyExistException e) {
                            e.printStackTrace();
                        } // Adding PTE to the Page Table along with the vpn, pfn
                        phyMemAddr = getPFN.evictedPFN * 64 + offset;//PA = PFN * 64 + offset
                        phyRam.write(phyMemAddr,value);//write it to Physical Memory based on Physical Address. 
                    }  
     
                    //if eviction is false, then store it to disk
                    if(getPFN.isEvicted = false){
                        phyRam.load(vpn,tempStartAddr);
                        phyRam.store(vpn,tempStartAddr); //store this dirty page to the disk
                        try {
                            PageTable.removePTE(vpn);
                        } catch (NoKeyException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //else it is in the page table contains the vpn, then they are in the same block and so they have the same pfn
                else{                       
                        sameBlockPFN = PageTable.get_ptable_pfn(vpn); //obtain that pfn from vpn that is already in the page table
                        if (PageTable.containsVPN(vpn)){ //meaning that it is true, there is a vpn within that block 
                            phyMemAddr = sameBlockPFN * 64 + offset;
                            phyRam.write(phyMemAddr,value);
                        }
                }

                //Checks if the WriteBacks are 32
                numWriteBacks++;

                if(numWriteBacks == 32){
                    write_back();
                    numWriteBacks = 0; //reset the counter here because during eviction, you store it right away to storage
                } 
            }  
        }
            
    

    @Override
    //Walks thriugh the page table and writes all the firtied pages to disk for persistence
    public void write_back(){
        LinkedList<MyPageTable.PTE> DirtyBitsList = PageTable.returnDirtyBitsList(); //Creating  list to include all the DirtyBits
        
        //Makes Sure the list is not empty
        if(DirtyBitsList.isEmpty() != true){
        
            //Go through the DirtyBits List 
            for (int i = 0; i < DirtyBitsList.size(); i++) {
                vpn = DirtyBitsList.get(i).getVPN();
                pfn = DirtyBitsList.get(i).getPFN();  
                    if(vpn != currBlock){ //if not in the same block then continuously store because each vpn is a different block
                        phyRam.store(currBlock, startAddr);
                        startAddr = pfn * 64;
                        currBlock = vpn;
                    }
                }
                if(vpn == currBlock){ //if it is in the same block then just store the whole block
                    phyRam.store(currBlock, startAddr);
                }

                //Clears everything in the dirty bit list so that 32 new writes can be stored
                DirtyBitsList.clear();
                PageTable.clearDirtyBitsList(); 
            }
    }

    //if the read method throws a page fault 
    @Override
    public byte read(int addr){
        vpn = addr / 64; // This is the hashCode()
        offset = addr % 64;
        
        //Out of Bounds Exception 
        if(addr > virtMemSize){
            System.err.print("Out of Bounds");
        }
        else{
            pfn = PageTable.get_ptable_pfn(vpn);
            //If it is not in the Page Table
            if(pfn == -1){ 
                    System.err.println("Page Fault"); //ADD THE PAGE TO THE PAGE TABLE
                    // Load the page first 
                    startAddr = policy.getPFN() * 64;
                    phyRam.load(vpn,startAddr); 

                    //Decided whether to evict or add 
                    getPFN = policy.consult();      

                    //if eviction is true, then store the whole page into physical memory 
                    if((getPFN.isEvicted = true) && (getPFN.justGotRemoved = false)){ 
                        //we need to find the vpn that already uses the pfn so we can phyRam.store() to the disk the dirty page
                        sameBlockPFN = PageTable.get_ptable_pfn(vpn);
                        int tempStartAddr = getPFN.evictedPFN * 64;
                        startAddr = policy.getPFN() * 64;
                        phyRam.load(vpn,startAddr); 
                        phyRam.store(vpn,tempStartAddr); //store this dirty page to the disk
                        try {
                            PageTable.removePTE(vpn);
                        } catch (NoKeyException e) {
                            e.printStackTrace();
                        } // remove pte that had the same vpn
                    }  
                    //if eviction is false, then just load it into physical memory, add it to page table, read from physical memory        
                    else{
                        startAddr = policy.getPFN() * 64;
                        phyRam.load(vpn,startAddr); 
                        try {
                            PageTable.put(vpn, getPFN.evictedPFN);
                        } catch (KeyAlreadyExistException e) {
                            e.printStackTrace();
                        }
                        phyMemAddr = PageTable.get_ptable_pfn(vpn) * 64 + offset;
                        value = phyRam.read(phyMemAddr);
                    }
                }

            //else it is in the Page Table - Cache Hit
            else{   
                sameBlockPFN = PageTable.get_ptable_pfn(vpn);
                //if it is in the same block then just read from physical memory
                if (PageTable.containsVPN(vpn)){ 
                    phyMemAddr = sameBlockPFN * 64 + offset;
                    value = phyRam.read(phyMemAddr);
                }
            }
        }
        return value;
    }
}
