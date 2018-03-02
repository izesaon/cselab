
///**
// * Created by jit_biswas on 2/1/2018.
// */



import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ProcessManagement {
//
//    //set the working directory

	private static File currentDirectory=new File("/home/cindy/workspace/cseProgAssign/src");
    //set the instructions file
    private static File instructionSet = new File("graph-file1.txt");
    public static Object lock=new Object();
    private static int totalNumberOfNodesExecuted=0;
    private static boolean allAreExecuted=false;

    public static void main(String[] args) throws InterruptedException {

        //parse the instruction file and construct a data structure, stored inside ProcessGraph class
    	ParseFile.generateGraph(new File(currentDirectory.getAbsolutePath() + "/"+instructionSet));

        ProcessGraph.printGraph();
        // Print the graph information
        // WRITE YOUR CODE
        
        //check the number of nodes that have been executed
        for(ProcessGraphNode node: ProcessGraph.nodes)
        {
        	if(node.isExecuted())
        	{
        		totalNumberOfNodesExecuted++;
        	}
        }
        
    	ProcessBuilder pb = new ProcessBuilder();
        try{
        	pb.directory(currentDirectory);
        	//while not the nodes have finished executing, we keep running the loop
        	while(allAreExecuted==false)
        	{
        		for (ProcessGraphNode node : ProcessGraph.nodes)
            	{
            		if(node.isExecuted())
            		{
            		}
            		else {
            			//keep track of the number of parent nodes that have been executed to faciliate the checking below
            			int numberOfParentNodesExecuted=0;
            			for(ProcessGraphNode parent: node.getParents())
                		{
                			if(parent.isExecuted())
                			{
                				numberOfParentNodesExecuted++;
                			}
                		}	
            			//if the number of parent nodes executed are same as the number of parents for the child, it means the child can begin to execute
            			if(numberOfParentNodesExecuted==node.getParents().size() && !node.isRunnable())
            			{
            				node.setRunnable();
            				ExecuteProcess(node);
            				node.setExecuted();
            				totalNumberOfNodesExecuted++;
            			}
            			//ensure that child node can run when previously, it couldn't
            			else if(node.isRunnable()){
            				ExecuteProcess(node);
            				node.setExecuted();
            				totalNumberOfNodesExecuted++;
            			}
            		}	
            	}   
        		//we can break out of while loop when all the nodes have been executed
        		if(totalNumberOfNodesExecuted==ProcessGraph.nodes.size())
        		{
        			allAreExecuted=true;
        		}
        		
        	}
        }
        	
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
        

        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
        // check if all the nodes are executed
        // WRITE YOUR CODE

        //mark all the runnable nodes
        // WRITE YOUR CODE

        //run the node if it is runnable
        // WRITE YOUR CODE

        System.out.println("All process finished successfully");
    }
   
    public static void ExecuteProcess(ProcessGraphNode node) 
    {
    	//check if we are running the code on Windows or in Linux as in this case
    	boolean isWindows=System.getProperty("os.name").toLowerCase().startsWith("windows");
    	
    	//create a new process for every node
    	ProcessBuilder pbBuilder=new ProcessBuilder();
    	pbBuilder.directory(currentDirectory);
    	File input=node.getInputFile();
    	File output=node.getOutputFile();
    	
    	try {
    		if(!input.toString().equals("stdin"))
    		{
    			pbBuilder.redirectInput(input);
    		}
    		if(!output.toString().equals("stdout"))
    		{
    			System.out.println(output.toString());
    			pbBuilder.redirectOutput(output);
    		}
    		if(isWindows)
            {
            	pbBuilder.command("cmd.exe","/c","dir");
            }
            else{
                String[] command = {"sh","-c",node.getCommand().toString()};
                pbBuilder.command(command);
            }
    		
            Process process = pbBuilder.start();
            process.waitFor();
            
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while(bufferedReader.readLine()!=null)
                {
                	//print out the output e.g output text file created after the process is completed
                	line=bufferedReader.readLine();
                    System.out.println(line);  
                }
                    bufferedReader.close();         	
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		System.out.println("Error present :(");
    	}
    	
    }

}
