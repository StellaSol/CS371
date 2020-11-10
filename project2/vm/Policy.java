import java.util.*;

public class Policy {
    //ATTRIBUTES
    private Queue <Integer> q = new LinkedList<>();
    private int maxNumFrames;
    public boolean evict = false;
    public int tempPFN;
    public boolean justGotRemoved = false;
    

    //CONSTRUCTOR
    public Policy(int maxNumFrames){
        this.maxNumFrames = maxNumFrames;
    }

    //CONSULTING METHOD TO RETRIEVE AN ARBITRARY PFN -- FIFO
    public VirtMemory.PolicyConsultant consult() {
        //PROBLEM: A new vpn  needs to get a new PFN... Test 5 keeps stopping at 64 meaning new VPN = Diff Block = new PFN
        if(q.size() < maxNumFrames){ // if the size of the queue has not reach the maxNumFrames 
            tempPFN = q.size();
            q.add(q.size()); //add the size (0...1....2...) to the queue and size will keep incrementing the more you add to it
        }
        
        else{//Evict is always true if it doesnt reach max size
            tempPFN = q.remove(); //removes the head 
            evict = true;  //sets evict to true
            justGotRemoved = true;
            q.add(tempPFN); // adds PFN back
        }
        return new VirtMemory.PolicyConsultant(tempPFN, evict,justGotRemoved);
    }

    public int getPFN(){
        return tempPFN;
    }

    public void setJustGotRemoved(boolean justGotRemoved){
        this.justGotRemoved = justGotRemoved;
    }
}
