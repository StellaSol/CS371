import java.util.Iterator;
public class MyLinkedList implements Iterable{ 
  //in addition to other regular list member functions such as insert and delete: (split and consolidate blocks must be implemented at the level of linked list)
   Node head;
   Node sorted;
   int offsetVal;
   int offset;
   int sum;
   int maxSize;
   int currentOffset;
   int nextOffset;


   private class Node {
		  int offset; //keeps adding
		  int size;   //keeps subtracting
		  int startRange;//Temp to keep offset 
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
		 
		  public void setStartRange(int startVal) {
			  startRange = startVal;
		  }
		  public int getStartRange() {
			  return startRange;
		  }
		  
		  //OTHER METHODS-DO LATER
		 public String toString() {	
			        Node current = head;
			        while (current != null)
			        {
			            current = current.next;
			        }	        
			        return Integer.toString(current.getOffset());
		 }
		 //public boolean is_adjacent(Node other);
		  
	}
   
   

   //ITERATOR
 

   public Iterator<Node> iterator() { 
       return new CustomIterator(); 
   } 
   
   class CustomIterator implements Iterator<Node>{ 
	   Node current = head;
	    
	      
	    // moves the cursor/iterator to next element 
	    public Node next() { 
	    	while(current!= null) {
	    		int offset = current.getOffset();
	    		current=current.next;
	    	}
	    	
	    	return current;
	    }


		@Override
		public boolean hasNext() {
			return current != null;
		} 
   } 
   

   
//CONTAINS
   public boolean contains(int addr)
   {
     Node current = head;

     while (current != null)
     {
       if (current.offset == addr)
       {
         return true;
       }

       current = current.next;
     }

     return false;
   }
   
 //NODE INSERTIONS
 public void insertNode(int offset,int size) {
		Node node = head;

		if (head == null) {
			offsetVal = 1;
			head = new Node(offsetVal,size);
			head.setStartRange(offsetVal);

		}		
		else {
			while(node.getNext() != null){
				node = node.getNext();
			}
			node.setNext(new Node(offsetVal,size));	
			head.setStartRange(offsetVal);

		}
		offsetVal += size;
 }
 

 //INSERTING A NODE WITH EXACT OFFSET AND SIZE
 public void insertFreeNode(int offset,int size) {
	 if (head == null) {
			head = new Node(offset,size);
			head.setStartRange(offset);
		}
	 
	 else {
			Node node = head;
			while(node.getNext() != null){
				node = node.getNext();
			}
			node.setNext(new Node(offset,size));
			head.next.setStartRange(offset);

		}
 }
 
 public int getPrevOffset(Node head) {    
	    Node curr = head;
	    Node prev = head;
	    while(curr !=null ){
	        prev = curr;
	        curr = curr.next;
	    }
	    
	    return prev.getOffset();
	}
 
 
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

 
  
 //ALLGORITHMS START HERE
 /* public void BFAlloc(MyLinkedList usedList, MyLinkedList freeList, int size) {
	  Node headref = freeList.head;
	  Node headref2 = usedList.head;
	  boolean contains;
	  while(headref != null) {
		  /*if(headref.getSize() == size) { //IF the size is a perfect fit, then move the whole node back into the used list and delete it from the free list
			 usedList.insertFreeNode(headref.getOffset(),headref.getSize()); //inserts it back to the list
			 usedList.deleteSecLast();
			 freeList.freeNode(usedList,headref.getOffset());
			 break;
		  }
		  
		   if (size <= headref.getSize()){
			  if(headref.getOffset() < headref.getRange()) {			  
				  contains = usedList.contains(headref.getStartRange());
						  if(contains) {
							  while (headref2!= null) {
								  if(headref2.offset == headref.getStartRange()) {
									  headref2.setSize(headref2.getSize()+size); 
									  headref.setSize(headref.getSize()-size); 
									  headref.setOffset(headref.offset+size);	
									  usedList.deleteFromEnd();
								  }
								  headref2 = headref2.next;
							  }
							  
						  } 
						  else {
							  usedList.insertFreeNode(headref.getStartRange(),size);	
							  headref.setSize(headref.getSize()-size); 
							  headref.setOffset(headref.offset+size);
							  usedList.deleteSecLast();
						  }
			  }    
		  }

		  headref = headref.next;
	  } 
  }*/
  
  public int FFAlloc(MyLinkedList usedList, MyLinkedList freeList, int size) {
	  Node headref = freeList.head;

	  while(headref != null) {
		  if(headref.getSize()!= 0 && head.getSize() <= size) {	  
			 if(head.getSize() == size) {
				 offsetVal = headref.getOffset();
				 usedList.insertFreeNode(headref.getOffset(),headref.getSize()); //inserts it back to the list
				 usedList.deleteSecLast();
				 freeList.freeNode(usedList,headref.getOffset());
			  break;
			 } else {
				 
			 }
		  }
		  else {
			  offsetVal = 0;
		  }
		  headref = headref.next;
	  } 
	  return offsetVal;
  }
  
  public int NFAlloc(MyLinkedList usedList, MyLinkedList freeList, int size) {
	  Node headref = freeList.head;

	  while(headref != null) {
		  if(headref.getSize()!= 0 && head.getSize() <= size) {	  
			 if(head.getSize() == size) {
				 offsetVal = headref.getOffset();
				 usedList.insertFreeNode(headref.getOffset(),headref.getSize()); //inserts it back to the list
				 usedList.deleteSecLast();
				 freeList.freeNode(usedList,headref.getOffset());
				 
			  break;
			 } else {
				 
			 }
		  }
		  else {
			  offsetVal = 0;
		  }
		  headref = headref.next;
	  } 
	  return offsetVal;
  }
  
  /* if (blockSize[j] >= processSize[i]) 
                { 
                    // allocate block j to p[i] process 
                    allocation[i] = j; 
      
                    // Reduce available memory in this block. 
                    blockSize[j] -= processSize[i]; 
      
                    break; 
                } */
  //DELETING NODE METHODS
  
  public Node removeLastNode(Node head) 
  { 
      if (head == null) 
          return null; 

      if (head.next == null) { 
          return null; 
      } 

      // Find the second last node 
      Node second_last = head; 
      while (second_last.next.next != null) 
          second_last = second_last.next; 

      // Change next of second last 
      second_last.next = null; 

      return head; 
  } 
  
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
    	
    	list.insertFreeNode(prev.next.offset,prev.next.size); //frees node from used/free_list, then inserts it back to used/free_list
    	
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
  
  /* Given a key, deletes the first occurrence of key in linked list */
  void deleteNode(int addr) 
  { 
      // Store head node 
      Node temp = head, prev = null; 

      // If head node itself holds the key to be deleted 
      if (temp != null && temp.offset == addr) 
      { 
          head = temp.next; // Changed head 
          return; 
      } 

      // Search for the key to be deleted, keep track of the 
      // previous node as we need to change temp.next 
      while (temp != null && temp.offset != addr) 
      { 
          prev = temp; 
          temp = temp.next; 
      }     

      // If key was not present in linked list 
      if (temp == null) return; 

      // Unlink the node from linked list 
      prev.next = temp.next; 
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
  public void splitMayDelete(MyLinkedList usedList, MyLinkedList freeList,int size) {  
	  Node fl = freeList.head;
	  Node ul = usedList.head;
	  int sizeTemp;
	  boolean contains;
	  boolean contains2;
	  while(fl != null) {
		   if (size<=fl.getSize()) {
				  		  contains = usedList.contains(fl.getStartRange());
						  if(contains) {
							  while (ul!= null) {
								  if(ul.offset == fl.getStartRange()) {
									  sizeTemp = ul.getSize()+size;
									  usedList.deleteNode(fl.getStartRange());
									  usedList.removeLastNode(usedList.head);
									  usedList.insertFreeNode(fl.getStartRange(),sizeTemp);	
									  fl.setSize(fl.getSize()-size);
									  fl.setOffset(fl.offset+size);
									  if(fl.getSize() == 0) {
										  freeList.deleteNode(fl.getOffset());
									  }
									  break;
								  }
								  ul = ul.next;
							  }		  
						  } 
						  else if(!contains) {
							  usedList.insertFreeNode(fl.getStartRange(),size);	
							  fl.setSize(fl.getSize()-size); 
							  fl.setOffset(fl.offset+size);
							  if(fl.getSize() == 0) {
								  freeList.deleteNode(fl.getOffset());
							  }
						      usedList.deleteSecLast();
						      break;
						  }
			      
			   	
			  /* else if(fl.getSize()-size == 0) {
				   contains2 = usedList.contains(fl.getStartRange());
				   if(contains2) {
						  while (ul!= null) {
							  if(ul.offset == fl.getStartRange()) {
								  ul.setSize(ul.getSize()+size);
								  usedList.removeLastNode(usedList.head);
								  fl.setSize(fl.getSize()-size);
								  fl.setOffset(fl.offset+size);
								  System.out.print("it workd");
								  break;
							  }
							  ul = ul.next;
						  }
						  
					  } 
					  else{
						  usedList.insertFreeNode(fl.getStartRange(),size);	
						  fl.setSize(fl.getSize()-size); 
						  fl.setOffset(fl.offset+size);
					      usedList.deleteSecLast();
					      break;
					  }
			   }*/
			}
			  fl = fl.next;
		   }

	  }
	  
	  
  
/*else if(fl.getSize() == size)  {
	  System.out.print("Yuggg");
  //contains2 = usedList.contains(fl.getStartRange());
 // if(contains2) {
	  	/*while (ul!= null) {
	  		if(ul.offset == fl.getStartRange()) {
	  			ul.setSize(ul.getSize()+size);
	  			usedList.removeLastNode(usedList.head);
	  			fl.setSize(fl.getSize()-size);
	  			fl.setOffset(fl.offset+size);
	  			freeList.deleteNode(fl.getOffset());
	  		}
		  ul = ul.next;
	  }*/
	  
	  
	  
	  /*while(headref != null) { //Traversing the freelist to check if split may delete	  
		  if (size <= headref.getSize()){ // if the size is less than or equal to the block size
			  if(headref.getOffset() != headref.getRange()) {	// if blocksize offset is less than the end range		  
				  		 contains = usedList.contains(headref.getStartRange()); 
						  if(contains) { // if usedList already contains the allocated node with the start range
							  while (headref2!= null) { //traverse the used list
								  if(headref2.offset == headref.getStartRange()) {//if the offset is equal to the start range
									  headref2.setSize(headref2.getSize()+size);  //change the size + size	 
									  headref.setSize(headref.getSize()-size);  //change the freeList block size by substracting it
									  headref.setOffset(headref.offset+size);	//change freeList block offset by adding it
									 // usedList.deleteSecLast();
								  }
							  else {
								  usedList.insertFreeNode(headref.getStartRange(),size);	//if list does not contain already then insert a node at the end of usedlist with the start range and size
								  headref.setSize(headref.getSize()-size); //change the freeList block size by substracting it
								  headref.setOffset(headref.offset+size); //change freeList block offset by adding it 
								  //usedList.deleteSecLast(); 
							  	}
								  headref2 = headref2.next;							  
							  }
				
						  }
			
			  }
		  }
		  
		  headref = headref.next;

	}*/
 
		  
	  
  
  
//If adjacent with eachother, merge the nodes 
  public void insertMayCompact(Node head) {   	  
	  			Node current = head;
	  			if(current == null) {
	  				return;
	  			}
	  			while(current != null && current.next!= null){
	  				 currentOffset = current.offset + current.size;
	  				 nextOffset = current.next.offset;
	  				if(currentOffset == nextOffset) {
	  					current.setSize(current.size+current.next.size);
	  					deleteNode(current.next.offset);
	  					break;
	  				}
	  				current = current.next;
	  			}
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

