

/**@author   Himanshu Gupta (himanshu.gupta@ufl.edu)
 * @version  1.01
 *
 *A node class for Fibonacci Heap
 *
 *
 */

public class Node {
	
	/**
	 * The key of data 
	 * It is unique for data.
	 * 
	 */
	public String key;
	
	/**
	 * The value of key
	 */
	
	public int  value;
	
	/**FHeap maintains a double 
	 * circular linked list.
	 * These nodes field are
	 * for maintaining this doubly circular link list
	 * Nodes maintained are left, right
	 * 
	 */
	public Node left;
	
	/**The right node
	 * 
	 * 
	 */
	public Node right;
	
	/**The parent of the node
	 * 
	 */
	public Node parent;
	
	/**It is any one child of 
	 * the given node
	 */
	public Node child;
	
	/**The number of children
	 * of the node
	 * 
	 */
	public int degree;
	
	/**The childcut field for node
	 * This field is useful for trigerring {@code cascadingCut}.
	 * It is set to {@code true} after node has lost a child
	 * after {@code increaseKey} operation.
	 */
	public boolean childcut;
	
	
	/**A constructor for creating a new node
	 * 
	 * 
	 * @param key  the key of data
	 * @param value the value of data
	 */
	public Node(String key, int value) {
		this.key = key;
		this.value = value;
		this.left = this;
		this.right = this;
	}

}
