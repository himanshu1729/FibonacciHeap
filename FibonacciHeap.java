

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @author   Himanshu Gupta (himanshu.gupta@ufl.edu)
 * @version  1.01
 * 
 * <p>An implementation of a priority queue with Fibonacci Heaps as backend
 * Data Structure</p>
 * 
 * <p>Fibonacci heaps provide O(1) amortized costs for insert
 * and decrease key and O(log n) for remove max.
 * </p>
 *
 * <p>The <em>head</em> of the queue points to the maximum value element in Heap.
 * The nodes are ordered by values.The keys are String and values are integers.
 * This implementation doesn't allow to have duplicate and null keys.Ties in 
 * values(if any) are broken arbitrarily.
 * </p>
 * 
 */


public class FibonacciHeap {
	
	/**
	 * The Node that contains the maximum value 
	 * 
	 */
	private Node head;   
	
	/**
	 * Number of nodes in the Heap
	 * 
	 */
	private int count;
	
	/**
	 * A hashmap that maintains the pointer to 
	 * location of node with a given key.
	 * 
	 */
	private Map<String, Node> nodeMap;

	/**
	 * Constructs a new Fibonacci Heap
	 * with zero nodes .
	 * 
	 * 
	 */
	
	public FibonacciHeap() {
		this.head = null;
		this.count = 0;
		this.nodeMap = new HashMap<>();
	}
	
	
	/**
	 *Deletes the whole Heap
	 * 
	 *
	 */
	public void clear() {
		this.head = null;
		this.count = 0;
	}
	
	
	/**
	 * If  the key is present in Heap or not
	 * 
	 * @param key the key of the node to be searched
	 * 
	 * @return {@code true}/{@code false} if key is present or not
	 */
	
	public boolean containsKey(String key) {
		return nodeMap.containsKey(key);
	}
	
	
	/**
	 * Returns a list of the top k keys(ordered by decreasing values) 
	 * present in Fibonacci Heap. This function first removes the 
	 * top k nodes from Heap, stores their key in a List
	 * and then reinserts the nodes back into heap.
	 * 
	 * @param  k The number of top values to be retrieved
	 * 
	 * @return  result A list containing the top k keys. 
	 */
	
	public List<String> getTopK(int k) {
		//List of Removed Nodes
		List<Node> removed = new ArrayList<>();
		
		List<String> result = new ArrayList<>();
		
		for(int i=0; i< k; i++) {
			Node n = removeMax();
			removed.add(n);
			result.add(n.key);
		}
		
		for(Node n: removed) {
			//System.out.print( "("+n.key + ","+ n.value+")");
			insert(n.key, n.value);
		}
		
		return result;
	}
	
	
	/**Inserts a given Key and value in the FHeap.
	 * It first form a Node from {@code key} and {@code value}.
	 * Then puts the new node in {@code nodeMap} and
	 * increment {@code count} by 1. Then it inserts node in 
	 * toplevel circular list at right of {@code head}.
	 * 
	 * Amortized Complexity O(1)
	 * 
	 * @param  key  the key of the data to be inserted
	 * @param  value the value of the data
	 * 
	 * @return  void 
	 */
	
	public void insert(String key, int value) {
		Node n = new Node(key, value);
		count += 1;
		nodeMap.put(key, n);
		if(head == null) {
			head = n;
		} else {
			n.left = head;
			n.right = head.right;
			head.right = n;
			n.right.left = n;
			
			if(n.value > head.value) {
				head = n;
			}
		}
	}
	
	
	/**Removes the maxElement from FHeap.
	 * It first removes all the children of maxelement ie {@code head}
	 * and insert the children in top level list.
	 * Then the head is removed and a {@code degreewiseCombine()} function is 
	 * triggered
	 * 
	 * @param none
	 * 
	 * @return  h the max node
	 */
	
	public Node removeMax() {
		Node h = head; // Save a copy of head
		
		if( h != null) {
			Node ch = h.child;
			int numchildren = h.degree;
			for(;numchildren > 0; numchildren--) { // Move all childs to top one by one
				Node t = ch.right;
				
				//Remove ch from sibling list
				ch.left.right = ch.right;
				ch.right.left = ch.left;
				
				//Add ch to topcircular list
				ch.left = head;
				ch.right = head.right;
				head.right = ch;
				ch.right.left = ch;

				ch.parent = null;
				ch =  t;
			}
			
			//Now remove head from circular list
			head.left.right = head.right;
			head.right.left = head.left;
			
			// Delete head from Nodemap
			nodeMap.remove(head.key);
			
			
			if(head == h.right) { //Case 1:  when only head node is there, then resulting Fheap
				head = null;	// is empty			
			}else {             
				                    //Case 2: There are more than 1 nodes. Here set head to point
				head = h.right;	    //to arbitrary element. We will restore the invariant head pointer = maximum 
								    // element in degreewiseCombine()
				degreewiseCombine();
			}
			count --;
		}
		
		return h;
	}
	
	/**Pairwise combine the top level trees whose degree are same.
	 * This is combining is done, until there is each tree has a
	 * unique degree. This function is invoked after {@code removeMax()}
	 * function.
	 * 
	 * @param None
	 * 
	 * @return void
	 */
	
	private void degreewiseCombine() {
		
		 //Initialize degree table
		Map<Integer, Node> degreeTable = new HashMap<>();

        
   
        // Find the number of root nodes.
        int numRoots = 0;
        Node h = head;

        if (h != null) {
            numRoots++;
            h = h.right;

            while (h != head) {
                numRoots++;
                h = h.right;
            }
        }

        
        while (numRoots > 0) {
            int deg = h.degree;
            Node r = h.right;

            while(true) {
                
            	Node y = degreeTable.getOrDefault(deg, null);
                if (y == null) {
                    break;
                }

               
                if (h.value < y.value) {
                    Node temp = y;
                    y = h;
                    h = temp;
                }

                // Link the nodes
                link(y, h);

              
                degreeTable.remove(deg);
                deg++;
            }
            
            degreeTable.put(deg, h);
          
            // Move forward through list.
            h = r;
            numRoots--;
        }

        // Set max to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        head = null;

      
       
        for(Node n : degreeTable.values()) {
        	if(head == null) {
        		head = n;
        	} else {
        	   
        		//Remove from Root List and Reinsert	
        		n.left.right = n.right;
        		n.right.left = n.left;
        	
        		n.left= head;
        		n.right = head.right;
        		head.right =n;
        		n.right.left = n;
        		
        		
        		if(n.value > head.value) {
        			head = n;
        		}
        	}
        	
        	
        }
    }


    /**
     * Make node y a child of node x.
     *
     *
     * @param y node to become child
     * @param x node to become parent
     * 
     * @return void
     */
    private void link(Node y, Node x)
    {
        // remove y from root list of heap
        y.left.right = y.right;
        y.right.left = y.left;

        // make y a child of x
        y.parent = x;

        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }

 
        x.degree++;
        y.childcut = false;
    }
		
	/**Increase the value of given key by given amount
	 * CascadingCut may be triggered after that(if childCut of parent is {@code true} already)
	 * 
	 * 
	 * @param key  the key of data 
	 * @param value the value by which to increase the key
	 * 
	 * @return void
	 */
    
    public void increaseKey(String key, int value) {
    	
    	Node n =  nodeMap.get(key);
    	n.value += value;
    	Node parent = n.parent;
    	if(parent != null && n.value > parent.value) {
    		cut(n,parent);
    		cascadingcut(parent);
    	}
        if (n.value > head.value) {
            head = n;
        }
    }
    
    /**Cut the child from parent and insert it to top level list 
     * This function is used by {@code increaseKey()} function. 
     * 
     * 
     * 
     * @param n   Node to be cut
     * @param parent parent of the node
     * 
     * @return void
     */
    
	private void cut(Node n, Node parent) {
        // remove x from childlist of y and decrement degree
        n.left.right = n.right;
        n.right.left = n.left;
        parent.degree--;
        n.parent = null;
        n.childcut = false;

        // reset y.child if necessary
        if (parent.child == n) {
            parent.child = n.right;
        }

        if (parent.degree == 0) {
            parent.child = null;
        }

        // add x to root list of heap
        n.left = head;
        n.right = head.right;
        head.right = n;
        n.right.left = n;


		
	}
	
	/**Performs cascadingCut after {@code increaseKey} operation 
	 * In cascasding cut, after the node has lost a second 
	 * child ie {@code childCut = true}, the node
	 * and it's ancestors on path to root
	 * are recursively moved to top level root list until root node
	 * or node with {@code childCut = false} is encountered 
	 * 
	 * 
	 * @param n Node on which cascasing cut is performed
	 * 
	 * @return void
	 */
	private void cascadingcut(Node n) {
		Node parent =  n.parent;
        if (parent != null) {
            // if y is unmarked, set it marked
            if (n.childcut) {
                cut(n, parent);
                // cut its parent as well
                cascadingcut(parent);
            } else {
                // it's marked, cut it from parent
            	n.childcut = true;
            }
        }
		
	}
}
