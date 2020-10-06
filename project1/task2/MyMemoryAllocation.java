import java.util.*;

public class MyMemoryAllocation extends MemoryAllocation {
	//Attributes
	String algorithm; 
	int mem_size;
	int offset = 1; //Starting address is 1 
	int offsetTemp; //holds offset of the current block 
	int sumSize; //Sum of available size in free list
	int maxSizeValue; //Returns the Maximum size of a bock in free list
	
	//Linked Lists
	MyLinkedList free_list = new MyLinkedList(); //List for available blocks
	MyLinkedList used_list = new MyLinkedList(); //List for allocated blocks
	
	
	//MyMemoryAllocation Constructor 
	public MyMemoryAllocation(int mem_size, String algorithm) {
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
                offsetTemp = offset;
		        offset = used_list.insertNode(offset,size); 
                offset =  used_list.BFAlloc(used_list, free_list, size);
                mem_size = mem_size - size;                	
                //System.out.print("Offset Value" + offset);
                }
			    
			} 
		//else if (algorithm.equals("FF")){
		//	return 0;
		
		//}else if (algorithm.equals("NF")) {
		//	return 0;
		//}else {
		//	return 0;
		//}
		
		else {
			return 0;
		}
		return offsetTemp;
	 }



	//The memory is referenced by its pointer (offset). The function must detect if it is a valid address, that is, the function must detect if the memory was previously allocated.
	 //if offset is vaid, delete node. 
	//Frees in the USED LIST
	 public void free(int address) {
		 used_list.freeNodeAndInsert(free_list,address);		 
	     free_list.insertionSortSize(free_list.head);
	     //free_list.insertionSort(free_list.head);
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
		maxSizeValue = free_list.findMaxSize(free_list.head,mem_size);
		return maxSizeValue;
	}

}


class MyLinkedList{ 
  //in addition to other regular list member functions such as insert and delete: (split and consolidate blocks must be implemented at the level of linked list)
   Node head;
   Node sorted;
   int offsetVal;
   int sum;
   int maxSize;


   private class Node {
		  int offset; //keeps adding
		  int size;   //keeps subtracting
		  Node next;
		  
		  //Constructor 
		  public Node(int offset, int size) {
			  this.offset = offset;
			  this.size = size;
		  }
		  
		  //Node Methods
		  public void setOffset(int offsetVal) {
			  this.offset = offsetVal;
		  }
		  
		  public int getOffset() {
			  return offset;
		  }
		  
		  public int getSize() {
			  return size;
		  }
		  
		  public void setSize(int sizeVal) {
			  this.size = sizeVal;
		  }
		  
		  public Node getNext() {
			  return next;
		  }
		  
		  public void setNext(Node nextVal) {
			  next = nextVal;
		  }
		  
		  //OTHER METHODS-DO LATER
		 //public String toString() {		}
		 //public boolean is_adjacent(Node other);
	}
   
   
 //NODE INSERTIONS
 public int insertNode(int offset,int size) {
		if (head == null) {
			offsetVal = 1;
			head = new Node(offsetVal,size);
		}		
		else {
			Node node = head;
			while(node.getNext() != null){
				node = node.getNext();
			}
			node.setNext(new Node(offset,size));
		}
		return offsetVal += size;
 }
 

 
 //INSERTING A NODE WITH EXACT OFFSET AND SIZE
 public void insertFreeNode(int offset,int size) {
	 if (head == null) {
			head = new Node(offset,size);
		}
	 
	 else {
			Node node = head;
			while(node.getNext() != null){
				node = node.getNext();
			}
			node.setNext(new Node(offset,size));
		}
	 
 }
 
 /*
 public int mergeTogether(Node headref, int offset, int size) {
	 	Node current = headref;
		current.setSize(current.getSize()-size);
		int tempSize = 0;
		while(current.getNext()!=null) {
			current = current.getNext();
		}
		current.setNext(new Node(offset,tempSize));
		return offsetVal;
 }
 */
 
 /*public int newMergedNode(int offset,int size) {
			Node node = head;
			offsetVal = offsetVal + node.getOffset();
			while(node.getNext() != null){
				node = node.getNext();
			}
			node.setNext(new Node(offset,size));
		
	  	
		return offsetVal += size;
}*/
  
//INSERTION SORT FOR OFFSETS
//REFERENCE: https://www.geeksforgeeks.org/insertion-sort-for-singly-linked-list/
  public void insertionSort(Node headref) {
	  sorted = null;
	  Node curr = headref;
	  while (curr != null) {
		  Node currNext = curr.next;
	      sortedInsert(curr);
	      curr = currNext;
	  }
	     head = sorted;
  }
  
  
  public void sortedInsert(Node newNode) {
	  if(sorted == null || sorted.offset >= newNode.offset) {
		  newNode.next = sorted;
		  sorted = newNode;
	  }
	  else {
		  Node current = sorted;
		  while(current.next != null && current.next.offset < newNode.offset){
			  current = current.next;
		  }
		  newNode.next = current.next;
		  current.next = newNode;
	  }
	  
 }
  
 
//INSERTION SORT FOR SIZE
//REFERENCE: https://www.geeksforgeeks.org/insertion-sort-for-singly-linked-list/
  public void insertionSortSize(Node headref) {
	  sorted = null;
	  Node curr = headref;
	  while (curr != null) {
		  Node currNext = curr.next;
	      sortedInsertSize(curr);
	      curr = currNext;
	  }
	     head = sorted;
  }
  
  
  public void sortedInsertSize(Node newNode) {
	  if(sorted == null || sorted.size >= newNode.size) {
		  newNode.next = sorted;
		  sorted = newNode;
	  }
	  else {
		  Node current = sorted;
		  while(current.next != null && current.next.size <= newNode.size ){
			  current = current.next;
		  }
		  newNode.next = current.next;
		  current.next = newNode;
	  }
 }
  
  //ALGORITHMS
  /*public int BFAlloc(Node headref,int offset, int sizeVal) {
	 Node current = headref; 

	 if (current == null) {
		  offsetVal = insertNode(offset,sizeVal);
	  }
	 
	 else {  

	  while(current != null) {
		  if(sizeVal <= current.getSize()) {
			  offsetVal =  mergeTogether(current,current.getOffset(),sizeVal);
			  break;
		  }
		  else if(sizeVal > current.getSize()) {
			  offsetVal = insertNode(offset,sizeVal);
			  break;
		  }
		  else {
			  current = current.next;
		  }
	  	}
	  }
	  return offsetVal;
  }*/
 
  
 //ALLGORITHMS START HERE
  public int BFAlloc(MyLinkedList usedList, MyLinkedList freeList, int size) {
	  Node headref = freeList.head;

	  while(headref != null) {
		  if(headref.getSize() <= size) {	  
			  offsetVal = usedList.insertNode(headref.getOffset(),headref.getSize()); //inserts it back to the list
			  usedList.deleteSecLast();
			  freeList.freeNode(usedList,headref.getOffset());
			  break;
		  }
		  else {
			  offsetVal = 0;
		  }
		  headref = headref.next;
	  }
	  
	 
	  return offsetVal;
  }
  
  
  //DELETING NODE METHODS
  public void deleteSecLast() { //Prevents double allocation when allocating size from free_list
	    if (head == null) {
	        return;
	    }
	    if (head.next == null) {
	        return;
	    }
	    if (head.next.next == null) {
	        head = head.next;
	        return;
	    }
	    Node current = head;
	    Node s = current.next;
	    Node t = s.next;
	    while (t.next != null) {
	        current = current.next;
	        s = s.next;
	        t = t.next;
	    }
	    current.next = t;
	}
  
  
  public void freeNodeAndInsert(MyLinkedList list, int addr) {
      Node temp = head, prev = null, hold=null;
      
      if (head == null) {
    	  return;
      }
    
     if(head != null && head.offset == addr) {// If head node holds the addr
         list.insertFreeNode(head.offset,head.getSize()); //frees node from used/free_list, then inserts it back to used/free_list
         head = temp.next;
         return;
     }


    else {
    	while (temp != null && temp.offset != addr) { //Searches for node with correct address
    		prev = temp; 
    		temp = temp.next; 
    	}     
      
    	if (temp == null) return;  // If the addr was not present throughout the list
    	
    	list.insertFreeNode(prev.next.offset,prev.next.getSize()); //frees node from used/free_list, then inserts it back to used/free_list
    	
    	prev.next = temp.next; // Unlink the node from linked list 

     	}
  }
  
 
  public void freeNode(MyLinkedList free_list, int addr) {
      Node temp = head, prev = null, hold=null;
      if (head == null) {
    	  return;
      }
    
     if(head != null && head.offset == addr) { // If head node holds the addr
         head = temp.next;
         return;
     }
    
    else {
    	while (temp != null && temp.offset != addr) { //Searches for node with correct address
    		prev = temp; 
    		temp = temp.next; 
    	}     
      
    	if (temp == null) return; // If the addr was not present throughout the list

    	prev.next = temp.next; // Unlink the node from linked list 

     	}
  }
	
  
  
  //FOR SIZE() METHOD
  public int findSum(Node headref,int memSize) {
	  sum = 0;
	  Node current = headref;
	  if(current == null) {
		  sum=memSize;
	  }
	  else{
		  while (current != null) {
	      sum = sum + current.getSize();
	      current = current.next;
	   }
	  }
	  return sum;
  }
 
  //FOR MAX_SIZE() METHOS
  public int findMaxSize(Node headref,int memSize) {
	  
	  Node current = headref;
	  if(current == null) {
		  maxSize=memSize;
	  }
	  else{
		  maxSize = current.size;  
	  
		  while(current != null){  
			  if(maxSize < current.size) {  
				  maxSize = current.size;  
			  }  
			  current = current.next;  
		  }  
	  }
	 return maxSize;
  }
 
 
  
  //if node has enough size, then allocate. Else delete the node
  public void splitMayDelete() { 
	  
  }
  
  //??? IDK YET
  public void insertMayCompact() {
	  
  }
  
  
  //PRINTING THE LIST METHOD 
  public void printlist(Node head, int countVal) {
	 for(int i=1;i<countVal+1;i++) {
		  System.out.println("Block " + i + "-Offset:" + head.offset + " Size:" +head.size +  " -> ");
		  head = head.next;
	  }
  }
 
  
  public int getCount() {  //Counts number of nodes
      Node current = head; 
      int count = 0; 
      while (current != null) 
      { 
          count++; 
          current = current.next; 
      } 
      return count; 
  }
}



  
 
  /*Prints the List
  @Override
  public String toString() {
	return toString(head);
  }
  */

  
/*
@Override
public Iterator iterator() {
	return new LinkedListIterator();
}

class LinkedListIterator implements Iterator{  		
     	Node current = null;

		@Override
  		public boolean hasNext() {
		if(current == null && head!= null) {
			return true;
		} else if(current != null) {
			return current.getNext() != null;
		}
		return false;
		}
  		
  		@Override
  		public Object next() {
  			if(current == null && head != null )
  				current = head;
  				return head.getOffset();
  		}	
	}
}

*/
  
// Sort-by in Java: (needs a class)
// You may need it to sort your list or you can maintain a sorted list upon insertion
/*class ByOffset implements Comparator<Node> {
	@Override
	public int compare(Node lhs, Node rhs) {
    return Integer.compare(lhs.offset, rhs.offset);
  }
	
}
*/







