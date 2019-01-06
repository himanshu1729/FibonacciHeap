

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

/**
 * @author Himanshu Gupta (himanshu.gupta@ufl.edu)
 *<p>
 *This class takes input a text files, constructs 
 *and updates fibonacci heap. Also, it returns top
 *k words present in heap at any time. The input file
 *contains 3 type of queries:
 *A) "$facebook 12"     Insert/Update data (here "facebook") with given key (here 12)
 *B)  3                 A number indicates you need to output top k(here 3) elements
 *C)  Stop				File ends here. Stop the program
 * </p>
 *
 */

public class Keywordcounter {
	public static void main(String[] args) throws Exception{
        
		// Initialize the Fibonacci Heap
		FibonacciHeap pq = new FibonacciHeap();
		
		// Initialize for reading input file
	    FileReader fr  = new FileReader(args[0]);
	    BufferedReader br = new BufferedReader(fr);
	    
     	// Initialize for writing to output file
		File out = new File("output_file.txt");
		//File out = new File("C:\\Users\\himan\\eclipse-workspace\\FibHeap\\src\\result_s1.txt");
		FileWriter fos = new FileWriter(out);
		BufferedWriter writer = new BufferedWriter(fos);
		
		//double b = System.currentTimeMillis();
		
		String line = "";
		
		//Do while stop is not reached in file
		while(true) {
			line = br.readLine();
			if(line.equalsIgnoreCase("stop"))
				break;
			else if(line.contains("$")) {
				String arr[]  = line.split(" ");
				int value = Integer.parseInt(arr[1].trim());
				String key = arr[0].substring(1).trim();
				if(pq.containsKey(key)) {
					pq.increaseKey(key, value);
				} else {
					pq.insert(key, value);
				}
			}else {
				int k = Integer.parseInt(line.trim());
				List<String> ll = pq.getTopK(k);
				for(int i=0;i <k-1;i++) {
						writer.append(ll.get(i)+",");
					//	System.out.print(ll.get(i)+ ",");
				}
				//System.out.println(ll.get(k-1));
				//System.out.println("\n");
				writer.append(ll.get(k-1));
				writer.append("\n");
		   }
		
		}
		//b = System.currentTimeMillis() - b;
		//System.out.println("Time taken " +b);
		br.close();
		writer.close();
		fr.close();
		fos.close();
	}
}
