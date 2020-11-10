import Storage.PhyMemory;
import java.util.*;

public class Policy {
    //ATTRIBUTES
    private Queue<Integer> q = new LinkedList<>();
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
        if(q.size() != maxNumFrames){
            int tempSize = q.size(); 
            q.add(tempSize);
        }
        else{
            tempPFN = q.remove();
            evict = true;
            justGotRemoved = true;
            q.add(tempPFN);
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
