/*Author: Drona Banerjee
 *Developed for Advanced Data Structures course work 
 *UFID: 46627749
 *UF Email: dr.banerjee@ufl.edu 
 */




/* The program accepts an input file as command line argument with tags and their frequencies and 
 * generates output based on a query for n most popular tags. This has been implemented using a 
 * Max Fibonacci heap. When we encounter a query, we perform RemoveMax operation n times and write/print
 * the result. After that we reconstruct the heap with the nodes removed and repeat the process
 * until we encounter "stop".
 * 
 * Run using following command: 
 * 
 * java hashtagcounter <input_file_name> [output_file_name]
 * 
 * The output filename is optional. If an output filename is provided, a file is created with the name
 * provided and the output is written to it. If the output filename is not provided, the output is
 * printed in the console.
 * 
 */


import java.io.*;
import java.util.*;


public class hashtagcounter 
{
    public static void main(String[] args) throws IOException 
    {
		
		
		FibonacciHeap<String> maxFibHeap = new FibonacciHeap<String>();
    	
    	/*create hash table : The key for the hashtable is the hashtag,and the value 
    	is the pointer to the corresponding node in the Fibonacci heap.
    	*/
        Hashtable tags = new Hashtable();
        
        
        String outfilename = "";
		
        //if no filename is provided, we simply return and prompt to provide commandline argument with filenames
        if (args.length == 0) 
        {
			System.out.println("Please enter a command line argument: java hashtagcounter <input_file_name> [output_file_name]");
			return;
		}

        
        String hTag = "";
        int hTagFrequency = 0;
        String[] substringArray = null;
        
        BufferedReader inputFile = new BufferedReader(new FileReader(args[0]));
        String inputLine = "";
        
        int queryInteger = 0;
        
        //if output file name is provided, write output in file with provided name
        if(args.length == 2)
        {
        	outfilename = args[1];
        	File outputFile = new File(outfilename);
            FileOutputStream foutStream = new FileOutputStream(outputFile);
            OutputStreamWriter writer = new OutputStreamWriter(foutStream, "UTF-8");


            while(true)
            {
            	
                inputLine = inputFile.readLine(); //reads a single line from the input file
                
                //if we encounter "stop" or "STOP" we break from the loop
                if (inputLine.equals("STOP")||inputLine.equals("stop"))
                    break;
                
                
                //If a query is encountered
                if (!inputLine.contains("#"))
                {
                	
                	 queryInteger = Integer.parseInt(inputLine); // stores the query integer
                     
                     /*
                      * We create an array to store the nodes that are removed so that we can 
                      * reconstruct the heap after generating the output
                      * 
                      */
                     
                     FibonacciHeapNode<String>[] removedNodes = new FibonacciHeapNode[queryInteger];

                     /* writes the tags to output file by printing the max node and 
                      * performs a remove max operation so that we have the next most popular 
                      * hashtag for the next iteration 
                      */
                     for (int j = 0; j < queryInteger; j++)
                     {
                         String maxNodeData = maxFibHeap.getMax().getData();
                         Double maxKey = maxFibHeap.getMax().getKey();
                         FibonacciHeapNode<String> rNode = new FibonacciHeapNode<String>(maxNodeData,maxKey);
                         removedNodes[j] = rNode;
                         writer.append(maxFibHeap.getMax().getData()); 
                         
                         //appends a comma after writing the hashtag, except for the last one
                         if (j<queryInteger-1)
                             writer.append(",");
                      
                         tags.remove(maxFibHeap.getMax().getData());
                         
                         //performs RemoveMax operation to get the next most popular hashtag for next iteration
                         maxFibHeap.removeMax();
                     }
   
                     //appends a new line
                     writer.append("\n");
                     
                     

                     //adding back the tags which were removed in the Remove Max operation
                     for (int j = 0; j < queryInteger; j++)
                     {
                         tags.put(removedNodes[j].data, removedNodes[j]);
                         maxFibHeap.insertNode(removedNodes[j], removedNodes[j].getKey()); //inserts the removed hashtag back in the fibonacci heap
                     }

                }
                else 
                {
                	
                	 /*If line starts with # we check if the hashtag is already in hashtable.
                     * 
                     * If hashtag is already in hash table, we do an increase key operation to increase 
                     * the frequency of the hashtag in fibonacci heap
                     * 
                     * If hashtag is not in hash table, we insert the new hashtag in the hash table and
                     * in the fibonacci heap. 
                     * 
                     */
                	
                	if(inputLine.startsWith("#"))
                	{
                        substringArray = inputLine.split(" "); // splits the input line read from input file based on space to separate the hashtag and it's frequency.
                        hTag = substringArray[0].substring(1); // stores hashtag
                        hTagFrequency = Integer.parseInt(substringArray[1]); // stores hashtag frequency
                        
                        //If hash table already contains the hashtag, perform increase key operation for the node in the fibonacci heap
                        if (tags.containsKey(hTag))
                        {
                            FibonacciHeapNode<String> heapNode = (FibonacciHeapNode<String>) tags.get(hTag);

                            maxFibHeap.increaseKey(heapNode, hTagFrequency); // performs increase key operation

                        }
                        //If hash table does not have the hashtag, insert in the heap and hash table
                        else 
                        {
                            FibonacciHeapNode<String> node = new FibonacciHeapNode<String>(hTag,hTagFrequency);
                            tags.put(hTag, node);
                            maxFibHeap.insertNode(node, hTagFrequency); // performs insert operation
                        } 
                	}
                	else
                	{
                		System.out.println("Error: Invalid Input encountered! Input does not start with #");
                	}
                    
                }
            }

            writer.close();
            foutStream.close();

            inputFile.close(); //To close the input file
            
        }
        //If no output file name is provided just print the result in console
        else
        {
        	
            while(true)
            {
            	
                inputLine = inputFile.readLine(); //reads a single line from the input file
                
                //if we encounter "stop" or "STOP" we break from the loop
                if (inputLine.equals("STOP")||inputLine.equals("stop"))
                    break;
                
                
                //If a query is encountered
                if (!inputLine.contains("#"))
                {
                	
                	 queryInteger = Integer.parseInt(inputLine); // stores the query integer
                     
                     /*
                      * We create an array to store the nodes that are removed so that we can 
                      * reconstruct the heap after generating the output
                      * 
                      */
                     
                     FibonacciHeapNode<String>[] removedNodes = new FibonacciHeapNode[queryInteger];

                     /* prints the tags to the console by printing the max node and 
                      * performs a remove max operation so that we have the next most popular 
                      * hashtag for the next iteration 
                      */
                     for (int j = 0; j < queryInteger; j++)
                     {
                         String maxNodeData = maxFibHeap.getMax().getData();
                         Double maxKey = maxFibHeap.getMax().getKey();
                         FibonacciHeapNode<String> rNode = new FibonacciHeapNode<String>(maxNodeData,maxKey);
                         removedNodes[j] = rNode;
                         System.out.print(maxFibHeap.getMax().getData()); 
                         
                         //prints a comma after printing the hashtag, except for the last one
                         if (j<queryInteger-1)
                        	 System.out.print(",");
                      
                         tags.remove(maxFibHeap.getMax().getData());
                         
                         //performs RemoveMax operation to get the next most popular hashtag for next iteration
                         maxFibHeap.removeMax();
                     }
   
                     //prints a new line
                     System.out.print("\n");
                     
                     

                     //adding back the tags which were removed in the Remove Max operation
                     for (int j = 0; j < queryInteger; j++)
                     {
                         tags.put(removedNodes[j].data, removedNodes[j]);
                         maxFibHeap.insertNode(removedNodes[j], removedNodes[j].getKey()); //inserts the removed hashtag back in the fibonacci heap
                     }

                }
                else 
                {
                	
                	 /*If line starts with # we check if the hashtag is already in hashtable.
                     * 
                     * If hashtag is already in hash table, we do an increase key operation to increase 
                     * the frequency of the hashtag in fibonacci heap
                     * 
                     * If hashtag is not in hash table, we insert the new hashtag in the hash table and
                     * in the fibonacci heap. 
                     * 
                     */
                	
                	if(inputLine.startsWith("#"))
                	{
                        substringArray = inputLine.split(" "); // splits the input line read from input file based on space to separate the hashtag and it's frequency.
                        hTag = substringArray[0].substring(1); // stores hashtag
                        hTagFrequency = Integer.parseInt(substringArray[1]); // stores hashtag frequency
                        
                        //If hash table already contains the hashtag, perform increase key operation for the node in the fibonacci heap
                        if (tags.containsKey(hTag))
                        {
                            FibonacciHeapNode<String> heapNode = (FibonacciHeapNode<String>) tags.get(hTag);

                            maxFibHeap.increaseKey(heapNode, hTagFrequency); // performs increase key operation

                        }
                        //If hash table does not have the hashtag, insert in the heap and hash table
                        else 
                        {
                            FibonacciHeapNode<String> node = new FibonacciHeapNode<String>(hTag,hTagFrequency);
                            tags.put(hTag, node);
                            maxFibHeap.insertNode(node, hTagFrequency); // performs insert operation
                        } 
                	}
                	else
                	{
                		System.out.println("Error: Invalid Input encountered! Input does not start with #");
                	}
                    
                }
            }


            inputFile.close(); //To close the input file
        
        }
        
        /*
        //writing output to console if output file name is not provided
        if(args.length == 1)
        {
        	  Scanner scanner = new Scanner(of);
        	    while (scanner.hasNextLine()) {
        	      String l = scanner.nextLine();
        	      System.out.println(l);
        	   }
        }
        */

    }
}



//Implementation of Max Fibonacci Heap
class FibonacciHeap<T>
{


	private int nodeCount; // stores number of nodes
	
	private FibonacciHeapNode<T> maxNode; // Max node
	
	public FibonacciHeap()
	{
	
	}
	
	//returns node with max key
	public FibonacciHeapNode<T> getMax()
	{
		return maxNode;
	}
	
	
	//Remove Max operation: The max element is removed from the heap and degree-wise meld is performed
	public FibonacciHeapNode<T> removeMax()
	{
	    FibonacciHeapNode<T> max = maxNode;
	
	    if (max != null) 
	    {
	        int childCount = max.degree;
	        FibonacciHeapNode<T> c = max.child;
	        FibonacciHeapNode<T> tRight;
	
	        // removes every child from child list, adds them to the top level of the heap and sets parent of the children to null
	        while (childCount > 0) 
	        {
	            tRight = c.right;
	
	            //removal from child list
	            c.left.right = c.right;
	            c.right.left = c.left;
	
	            // adding to top level
	            c.left = maxNode;
	            c.right = maxNode.right;
	            maxNode.right = c;
	            c.right.left = c;
	
	            c.parent = null; // sets parent to null
	            c = tRight;
	            childCount--;
	        }
	
	        // remove from top level
	        max.left.right = max.right;
	        max.right.left = max.left;
	
	        if (max == max.right)
	        {
	            maxNode = null;
	        } 
	        else 
	        {
	            maxNode = max.right;
	            degreeWiseMeld();
	        }
	
	        nodeCount--; // reduce node count
	    }
	
	    return max;
	}
	
		
	//Insert operation: inserts a new node in the heap
	public void insertNode(FibonacciHeapNode<T> node, double nodeKey)
	{
	    node.key = nodeKey;

	    if (maxNode != null) 
	    {
	        node.left = maxNode;
	        node.right = maxNode.right;
	        maxNode.right = node;
	        node.right.left = node;
	
	        if (nodeKey > maxNode.key)
	            maxNode = node;
	
	    } 
	    else 
	    {
	        maxNode = node;
	    }
	
	    nodeCount++;
	}
	
	//Performs the cut operation to cut/remove child from parent 
	protected void cutNode(FibonacciHeapNode<T> c, FibonacciHeapNode<T> p)
	{
	    //remove c from list of children
	    c.left.right = c.right;
	    c.right.left = c.left;
	    p.degree--; //decrease degree of parent
		
		    
	    if (p.child == c)
	        p.child = c.right;
		
		if (p.degree == 0)
			p.child = null;
		    
		
		//adds child to the top-level
	    c.left = maxNode;
	    c.right = maxNode.right;
	    maxNode.right = c;
	    c.right.left = c;
		
		   
		c.parent = null; //set c's parent as null as it no longer has a parent
		c.cCut = false;
	}
		
		
	//Performs a cascading cut. We keep cutting if the node's parent has Child Cut value set as true
	protected void cascadingCut(FibonacciHeapNode<T> n)
	{
	    FibonacciHeapNode<T> p = n.parent;
		
	    // Check if there is a parent
	    if (p != null)
	    {
	        //we set child cut value, if it is not set
	        if (!n.cCut) 
	        {
	            n.cCut = true;
	        } 
	        else 
	        {
		         //cut it from parent
		         cutNode(n, p);
		
		         cascadingCut(p); // cascades by cutting parent
		     }
		 }
	}
	
	
	
	
	
	// Performs meld operation by doing a degree-wise merge
	protected void degreeWiseMeld()
	{
		
		//array size calculated based on formula from Cormen
	    int degreeTableSize = ((int) Math.floor(Math.log(nodeCount) * (1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0)))) + 1;
	
	    List<FibonacciHeapNode<T>> dTable = new ArrayList<FibonacciHeapNode<T>>(degreeTableSize);
	
	    //Initialize degree table
	    for (int j = 0; j < degreeTableSize; j++) 
	    {
	        dTable.add(null);
	    }
	
	    int rootCount = 0;
	    FibonacciHeapNode<T> max = maxNode;
	
	    //calculating number of nodes in the top level
	    if (max != null) 
	    {
	        rootCount++;
	        max = max.right;
	
	        while (max != maxNode) 
	        {
	            rootCount++;
	            max = max.right;
	        }
	    }
	
	    
	    while (rootCount > 0)
	    {
	        
	        int deg = max.degree;
	        FibonacciHeapNode<T> next = max.right;
	
	        while (true) 
	        {
	            FibonacciHeapNode<T> a = dTable.get(deg);
	            if (a == null)
	                break;
	
	            // checks for the greater node to make the smaller node child of the bigger node
	            if (a.key > max.key) 
	            {
	                FibonacciHeapNode<T> t = a;
	                a = max;
	                max = t;
	            }
	
	            //Now we make the smaller node child of the bigger node
	            
	            
	            // remove from top level
	    	    a.left.right = a.right;
	    	    a.right.left = a.left;
	    	
	    	    // set the bigger node as parent of the smaller node
	    	    a.parent = max;
	    	
	    	    if (max.child != null) 
	    	    {
	    	        a.left = max.child;
	    	        a.right = max.child.right;
	    	        max.child.right = a;
	    	        a.right.left = a;
	    	        
	    	    } 
	    	    else 
	    	    {
	    	    	max.child = a;
	    	        a.right = a;
	    	        a.left = a;
	    	    }
	    	
	    	    
	    	    // increment the degree of the bigger node as it now has the smaller node as it's new child
	    	    max.degree++;
	    	
	    	    a.cCut = false;
	            
	            
	            //The smaller node has now been attached as the child of the bigger node
	            
	            
	
	            // move to next
	            dTable.set(deg, null);
	            deg++; //increase degree
	        }
	
	        // saving the node for merge if node with same degree is found
	        dTable.set(deg, max);
	
	        max = next;
	        rootCount--; 
	    }
	
	    
	    
	    
	    maxNode = null; // setting max node to null
	
	    
	    //reconstruct the top level list
	    for (int i = 0; i < degreeTableSize; i++) 
	    {
	        FibonacciHeapNode<T> a = dTable.get(i);
	        if (a == null)
	            continue;
	
	        // add to top level list
	        if (maxNode != null) 
	        {
	            // remove from top level list
	            a.left.right = a.right;
	            a.right.left = a.left;
	
	            // re-add to top level 
	            a.left = maxNode;
	            a.right = maxNode.right;
	            maxNode.right = a;
	            a.right.left = a;
	
	            
	            //checks if new node is greater than existing max node
	            if (maxNode.key < a.key)
	                maxNode = a; //updates max node if new node is greater than existing max node
	            
	        } 
	        else 
	        {
	            maxNode = a;
	        }
	    }
	}
	
	//increases the value of the key of a particular node in the fibonacci heap
	public void increaseKey(FibonacciHeapNode<T> n, double increaseVal)
	{
	    n.key += increaseVal; //increase the value of key by the increase value
	
	    FibonacciHeapNode<T> p = n.parent;
	
	    //we perform cascading cut if the key value of the child key becomes greater than that of the parent after the increase in key value
	    if(p != null)
	    {
	    	if ((n.key > p.key)) 
		    {
		        cutNode(n, p);
		        cascadingCut(p);
		    }
	    }
	
	    //update max node if the node value becomes greater than that of the existing max node after the increase in key value
	    if (maxNode.key < n.key)
	        maxNode = n;
	    
	}

}


/**
Implementation of the Fibonacci heap node. 
 
Node structure contains:

Degree, Child, Data

Left and Right Sibling - Used for circular doubly linked list of siblings.

Parent - Pointer to parent node.

ChildCut Boolean - True if node has lost a child since it became a child of its current parent.
Set to false by remove max operation, which is the only operation that makes one node a child of another.

*/

class FibonacciHeapNode<T>
{
	
	FibonacciHeapNode<T> parent; // to store the parent of current node
	FibonacciHeapNode<T> left; // to store left sibling
	FibonacciHeapNode<T> right; // to store the right sibling
	
	int degree; //to maintain the degree of the node which is equal to it's number of children
	
	T data; //To store the node data
	double key; //to maintain key value of the node
 
	FibonacciHeapNode<T> child; // To store the first child node

    //Child Cut Value: We mark this as true if node has lost a child since it became a child of its current parent
    boolean cCut;

   
   //Default constructor
   public FibonacciHeapNode(T data, double key)
   {
       right = this;
       left = this;
       this.data = data;
       this.key = key;
   }
   
   //To get the data of the node
   public final T getData()
   {
       return data;
   }
   

   //To get the key of the node
   public final double getKey()
   {
       return key;
   }

}
