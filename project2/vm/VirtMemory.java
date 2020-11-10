/* 
NOTES TO SELF:
- blockNUM = VPN 
- Dirty Bit:
    - TRUE: It's been written to the physical memory 
    -FALSE: It's been stored to to the DISK so it is not longer dirty / Eviction is happening on a dirty page 
-For write and read: MUST load the page before doing any rights or read
-write(): If it is within the same block(same vpn), same PFN but ofc the PhyAddr will be different due to the offset
- StartAddr = PFN * 64 
- Eviction: 
    -boolean isEvicted will be set to false
    -WILL be true when it is removed in the policy
    -if isEvicted = TRUE, then go to that physical addr and STORE it to the disk  
-write_back(): Iterate through the list and store all dirtied pages into the disk
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
            pfn = PageTable.contains(vpn);
            // If it is NOT in the Page Table then create an entry and write it to physical memory 
            if(pfn == -1 ){
                    System.err.print("Page Fault");
                    getPFN = policy.consult(); //retrieving a PFN
                    // if((getPFN.isEvicted = true) && (getPFN.justGotRemoved = false)){ //if eviction is true, then store the whole page into physical memory 
                    //     //we need to find the vpn that already uses the pfn so we can phyRam.store() to the disk the dirty page
                    //     int tempStartAddr = getPFN.evictedPFN * 64;
                    //     phyRam.store(vpn,tempStartAddr); //store this dirty page to the disk
                    //     PageTable.removePTE(vpn);
                    //     //remove pte that had the same vpn
                    // }  
                    
                    if(getPFN.isEvicted = true){ //if eviction is true, then store the whole page into physical memory 
                        startAddr = getPFN.evictedPFN * 64;  //getting the StartAddr to LOAD
                        phyRam.load(vpn,startAddr); //Loading the page first before any writes
            
                        //if they are not in the same block, then consult with policy to get a new pfn
                        pfn = getPFN.evictedPFN;
                        PageTable.put(vpn, pfn); //Adding PTE to the Page Table along with the vpn, pfn
                        phyMemAddr = getPFN.evictedPFN * 64 + offset;//write it to Physical Memory based on Physical Address. PA = PFN * 64 + offset
                        phyRam.write(phyMemAddr,value);//Write it to physical memory
                    }  
                    if(getPFN.isEvicted = false){
                        int tempStartAddr = getPFN.evictedPFN * 64;
                        phyRam.store(vpn,tempStartAddr); //store this dirty page to the disk
                        PageTable.removePTE(vpn);
                    }


                    numWriteBacks++;//Keeping track on how many writes have been built up since last write back  
                }

                else{ //if it the page table contains the vpn, then they are in the same block and so they have the same pfn
                        sameBlockPFN = PageTable.contains(vpn);
                        if (PageTable.containsVPN(vpn)){ //meaning that it is true, there is a vpn within that block 
                            phyMemAddr = sameBlockPFN * 64 + offset;
                            phyRam.write(phyMemAddr,value);
                            numWriteBacks++;
                        }
                }
                
                //Checks if the WriteBacks are 32
                if(numWriteBacks == 32){
                    write_back();
                    numWriteBacks = 0; //reset the counter here because during eviction, you store it right away to storage
                } 
            }    
    }

    @Override
    //Walks thriugh the page table and writes all the firtied pages to disk for persistence
    public void write_back(){
        int index = 0;
        LinkedList<MyPageTable.PTE> DirtyBitsList = PageTable.returnDirtyBitsList(); //Creating  list to include all the DirtyBits
        
        if(DirtyBitsList.isEmpty() != true){
            //Iterator for the List
            ListIterator<MyPageTable.PTE> it = null;
            it = DirtyBitsList.listIterator();

            //Start Iterating in the list
            pfn = DirtyBitsList.get(index).getVPN();
            currBlock = DirtyBitsList.get(index).getVPN(); //gets the first vpn in the list          
            
            while(it.hasNext()){
                vpn = DirtyBitsList.get(index).getVPN();
                pfn = DirtyBitsList.get(index).getPFN();
                
                if(vpn != currBlock){ //if not in the same block then just contain 
                    phyRam.store(currBlock, startAddr);
                    startAddr = pfn * 64;
                    currBlock = vpn;
                }
                    index++;
                    it.next();
                }
                //if they are in the same block store
                phyRam.store(currBlock,startAddr);
                PageTable.clearDirtyBitsList(); //Clears everything in the dirty bit list so that 32 new writes can be stored
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
            pfn = PageTable.contains(vpn);
            //If it is not in the Page Table
            if(pfn == -1){ 
                    System.err.println("Page Fault"); //ADD THE PAGE TO THE PAGE TABLE
                    // Load the page first 
                    startAddr = policy.getPFN() * 64;
                    phyRam.load(vpn,startAddr); 

                    //Decided whether to evict or add 
                    getPFN = policy.consult();      

                    if((getPFN.isEvicted = true) && (getPFN.justGotRemoved = false)){ //if eviction is true, then store the whole page into physical memory 
                        //we need to find the vpn that already uses the pfn so we can phyRam.store() to the disk the dirty page
                        int tempStartAddr = getPFN.evictedPFN * 64;
                        phyRam.store(vpn,tempStartAddr); //store this dirty page to the disk
                        PageTable.removePTE(vpn);
                        //remove pte that had the same vpn
                    }            
                    PageTable.put(vpn,getPFN.evictedPFN);
                    phyMemAddr = PageTable.contains(vpn) * 64 + offset;
                    value =  phyRam.read(phyMemAddr);
                }
            //If it is in the Page Table - Cache Hit
            else{ 
                phyMemAddr = pfn * 64 + offset;
                value = phyRam.read(phyMemAddr);
            }
        }
        return value;
    }
}

