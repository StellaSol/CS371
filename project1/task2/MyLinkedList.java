import java.util.Iterator;
public class MyLinkedList implements Iterable{ 
  //in addition to other regular list member functions such as insert and delete: (split and consolidate blocks must be implemented at the level of linked list)
   private Node head;
   private Node sorted;
   private int offsetVal;
   private int offset;
   private int sum;
   private int maxSize;
   private int currentOffset;
   private int nextOffset;


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
			head.setStartRange(offset);

		}		
		else {
			while(node.getNext() != null){
				node = node.getNext();
			}
			node.setNext(new Node(offsetVal,size));	
			node.setStartRange(offset);

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
			node.next.setStartRange(offset);

		}
 }
 
 
 public int getPrevOffset(MyLinkedList list) {    
	    Node curr = list.head;
	    Node prev = list.head;
	    while(curr !=null ){
	        prev = curr;
	        curr = curr.next;
	    }
	    
	    return prev.getOffset();
	}
 
 
//INSERTION SORT FOR OFFSETS
//REFERENCE: https://www.geeksforgeeks.org/insertion-sort-for-singly-linked-list/
  public void insertionSort(MyLinkedList list) {
	  sorted = null;
	  Node curr = list.head;
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
  public void insertionSortSize(MyLinkedList list ) {
	  sorted = null;
	  Node curr = list.head;
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
      Node temp = head, prev = null; 

      if (temp != null && temp.offset == addr)  { // If head node holds the addr
          head = temp.next; 
          return; 
      } 
      while (temp != null && temp.offset != addr) {//Searches for node with correct address 
          prev = temp; 
          temp = temp.next; 
      }     

      if (temp == null) return;  // If the addr was not present throughout the list

      prev.next = temp.next; // Unlink the node from linked list 
  } 
	
  
  
  //FOR SIZE() METHOD
  public int findSum(MyLinkedList list,int memSize) {
	  sum = 0;
	  Node current = list.head;
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
  public int findMaxSize(MyLinkedList list,int memSize) {
	  
	  Node current = list.head;
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
//if node has enough size, then allocate. Else delete the node
  public void splitMayDelete(MyLinkedList usedList, MyLinkedList freeList,int size) {  
	  Node fl = freeList.head;
	  Node ul = usedList.head;
	  int offset;
	  int sizeTemp;
	  boolean contains;
	  boolean contains2;
	  
	  while(fl != null) {
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
			
		   			  	else {
		   			  	offset = fl.getStartRange();
					 	usedList.insertFreeNode(offset,size);	
						fl.setSize(fl.getSize()-size); 
						fl.setOffset(fl.offset+size);
						if(fl.getSize() == 0) {
							 freeList.deleteNode(fl.getOffset());
						}
						      usedList.deleteSecLast();
						      break;
					  }
   			  	
		  	
			  fl = fl.next;
		  }
	  }
	  
  
//If adjacent with eachother, merge the nodes 
  public void insertMayCompact(MyLinkedList list) {   	  
	  			Node current = list.head;
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
  public void printlist(MyLinkedList list, int countVal) { 
	  Node head = list.head;
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
  
  public void getNext(MyLinkedList list) {
	  Node head = list.head;
	  head.getNext();
  }
  
 

}


