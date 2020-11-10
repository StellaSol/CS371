public class MyVirtMemTest {
    public static void main(String[] args) {

        Memory m = new VirtMemory();
        m.startup();
        for (int i = 0; i < 32; i++) {
            m.write(i * 64, Byte.parseByte("-1"));
        }

        int writeCount = m.getPhyMemory().writeCountDisk();
        int readCount = m.getPhyMemory().readCountDisk();
        System.out.println();
        System.out.println("Write Count:" + writeCount);
        System.out.println("Read Count:" + readCount);
        //there is a problem with 63 loads??? 

     
        // m.shutdown();
        // // if you cleared up dirty bits, then shutdown
        // // should add no more writes.
        
        //  writeCount = m.getPhyMemory().writeCountDisk();
        //  readCount = m.getPhyMemory().readCountDisk();
        // System.out.println();
        // System.out.println("Write Count:" + writeCount);
        // System.out.println("Read Count:" + readCount);
     
    }
}


