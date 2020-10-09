import java.util.Iterator;
public class MyLinkedList implements Iterable{ 
  //in addition to other regular list member functions such as insert and delete: (split and consolidate blocks must be implemented at the level of linked list)
   private Node head;
   private Node sorted;
   int offsetVal;
   int offset;
   int sum;
   int maxSize;
   int currentOffset;
   int nextOffset;


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
   

   

   
 //NODE INSERTIONS
 public int insertNode(int offset,int size) {
		Node node = head;

		if (head == null) {
			offsetVal = 1;
			head = new Node(offsetVal,size);
		}		
		else {
			while(node.getNext() != null){
				node = node.getNext();
			}
			node.setNext(new Node(offsetVal,size));			
		}
		offsetVal += size;
		
		return 10;//????
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
 
 public int getPrevOffset(Node head) {    
	    Node curr = head;
	    Node prev = head;
	    while(curr !=null ){
	        prev = curr;
	        curr = curr.next;
	    }
	    
	    return prev.getOffset();
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

 
  
 //ALLGORITHMS START HERE
  public int BFAlloc(MyLinkedList usedList, MyLinkedList freeList, int size) {
	  Node headref = freeList.head;

	  while(headref != null) {
		  if(headref.getSize() == size) {	  
			 offsetVal = headref.getOffset();
			 usedList.insertFreeNode(headref.getOffset(),headref.getSize()); //inserts it back to the list
			 usedList.deleteSecLast();
			 freeList.freeNode(usedList,headref.getOffset());
			  break;
		  }
		  else if (headref.getSize() != 0 && headref.getSize()-size != -1){
			  offsetVal = 0;
			  offsetVal = headref.getOffset();
			  usedList.insertFreeNode(headref.getOffset(),headref.getSize()-size); //inserts it back to the list
			  
			  headref.setSize(headref.getSize()-size); 
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
  public void splitMayDelete() {  
	  
  }
  
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
  
	  	
	
  
  
  /*
  public void MergeTogether(MyLinkedList list, Node curr, Node currNext) {	  
	  if(curr.getOffset() < currNext.getOffset()) {
		  curr.setSize(curr.getSize()+currNext.getSize());
		  list.freeNode(list,currNext.getOffset());
	  }
	  else {
		  currNext.setSize(curr.getSize()+currNext.getSize());
		  list.freeNode(list,curr.getOffset());
	  }  	
  }*/
  
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





  /*
public Iterator <Node> Iterator() {
	return new LinkedListIterator();
}
class LinkedListIterator <Node>implements Iterator {  		
     	 MyLinkedList.Node current = head;
     	 int offset;
		@Override
  		public boolean hasNext() {
			return current!= null;
		}
  		
		@Override
		public Object next() {
            if(hasNext()){
                int offset = current.getOffset();
                current = current.next;
            }
            return offset;
        }		
	}
*/



/*Prints the List
@Override
public String toString() {
	return toString(head);
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

