public class MyVirtMemTest {
           // the following are more releastic workloads
           static final int TEST_SIZE = 64 * 1024;// 64K, test on max address space!

           static byte fce(int adr) {
               return (byte) ((adr * 5 + 6) % 256 - 128);
           }
       
           static byte fce2(int adr) {
               return (byte) ((adr * 7 + 5) % 256 - 128);
           }
    public static void main(String[] args) {

        Memory m = new VirtMemory();
        m.startup();
        boolean result = true;
        for (int i = 0; i < TEST_SIZE; i++)
            m.write(i, fce(i));
        for (int i = 0; i < TEST_SIZE; i++)
            if (m.read(i) != fce(i))
                result = false;
            // every 32 writes triggers a write-back to disk.
            // Memory m = new VirtMemory();
            // m.startup();
            // for (int i = 0; i < 32; i++) {
            //     m.write(i, Byte.parseByte("-1"));
            // }
            // same block, so there should be only one write
 
            m.shutdown();
            int writeCount = m.getPhyMemory().writeCountDisk();
            int readCount = m.getPhyMemory().readCountDisk();
        System.out.println();
        System.out.println("Write Count:" + writeCount);
        System.out.println("Read Count:" + readCount);

        //Expected 2048 loads and 2048 stores
     
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


