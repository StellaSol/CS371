public class MyMemoryAllocation extends MemoryAllocation {
	
	//Attributes
	int offset = 1; //Starting address is 1 
	int sumSize; //Sum of available size in free list
	int maxSizeValue; //Returns the Maximum size of a bock in free list	
	String algorithm; 
	int mem_size;
	
	//Linked Lists
	MyLinkedList free_list = new MyLinkedList(); //List for available blocks
	MyLinkedList used_list = new MyLinkedList(); //List for allocated blocks
	
	
	//Constructor
	public MyMemoryAllocation(int mem_size, String algorithm) {
		super(mem_size,algorithm);
		this.mem_size = mem_size - 1;
		this.algorithm = algorithm;
	}
	
	//prints out blocks in an ascending order of their offsets. 
	public void print() {
		//free_list.insertionSort(free_list.head);
		System.out.println("Used/Unavabilable Blocks");
		int count = used_list.getCount();
		free_list.printlist(used_list.head, count);
		
		System.out.println();
	
		System.out.println("Free Blocks/Available");
		int count2 = free_list.getCount();
		free_list.printlist(free_list.head, count2);


	}
	
	
	 //allocates memory with specified size. If the memory is available the function returns pointer (offset) of the beginning of allocated memory. Otherwise it returns 0.
	public int alloc(int size) {
		if(size <= mem_size) {
			if (algorithm.equals("BF")) {
		        offset = used_list.insertNode(offset,size); 
		        sumSize = free_list.findSum(free_list.head,mem_size);
		       if(size <= sumSize) {
		    	   	offset=used_list.BFAlloc(used_list, free_list, size);
		    	   	free_list.insertionSortSize(free_list.head);
		       }
                //System.out.print("Offset Value" + offset);
			}	 
		else if (algorithm.equals("FF")){
			  offset = used_list.insertNode(offset,size); 
		        sumSize = free_list.findSum(free_list.head,mem_size);
		       if(size <= sumSize) {
		    	   	offset=used_list.FFAlloc(used_list, free_list, size);
		       }
		        mem_size = mem_size - size;   
		
			}
	        mem_size = mem_size - size;                	

		}
		
		//else if (algorithm.equals("NF")) {
		//	return 0;
		//}else {
		//	return 0;
		//}
		
		else {
			return 0;
		}
		offset = used_list.getPrevOffset(used_list.head);
		return offset;
	 }



	//The memory is referenced by its pointer (offset). The function must detect if it is a valid address, that is, the function must detect if the memory was previously allocated.
	 //if offset is vaid, delete node. 
	//Frees in the USED LIST
	 public void free(int address) {
		 used_list.freeNodeAndInsert(free_list,address);


	 }
	 
	 //Sum of all sizes in memory nodes
	 @Override
	public int size() {
		sumSize = free_list.findSum(free_list.head,mem_size);
		return sumSize;
	}
	
	 
	 //returns the biggest size of memory block. Maybe sort it and then get the last node?
	@Override
	public int max_size() {
		/*Iterator itr = free_list.Iterator();
		
		while(itr.hasNext()) {
			sumSize = sumSize + itr.next();
		}
		
		return sumSize;
		
		}*/
		
		maxSizeValue = free_list.findMaxSize(free_list.head,mem_size);
		return maxSizeValue;
	}
}






