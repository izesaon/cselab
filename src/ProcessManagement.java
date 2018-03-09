
///**
// * Created by jit_biswas on 2/1/2018.
// */


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ProcessManagement {

	//set the working directory
	private static File currentDirectory=new File("/home/cindy/workspace/cseProgAssign/src");
    //set the instructions file
    private static File instructionSet = new File("graph-file1.txt");
    private static int totalNumberOfNodesExecuted=0;
    private static boolean allAreExecuted=false;

    public static void main(String[] args) throws InterruptedException {

    	//checks for input text file by user on the command line
    	if(args.length!=0)
    	{
    		instructionSet=new File(args[0]);
    		//parse the instruction file and construct a data structure, stored inside ProcessGraph class
    		ParseFile.generateGraph(new File(instructionSet.getAbsolutePath()));
    	}
    	else if (args.length==0){
    		 //parse the instruction file and construct a data structure, stored inside ProcessGraph class
        	ParseFile.generateGraph(new File(currentDirectory.getAbsolutePath() + "/"+instructionSet));
    		
    	}
      
    	// Print the graph information
        ProcessGraph.printGraph();
     
        
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
        	//while not the nodes have finished executing, we keep running the loop to run the appropriate nodes when they are ready to run
        	while(allAreExecuted==false)
        	{
        		for (ProcessGraphNode node : ProcessGraph.nodes)
            	{
        			//for nodes that have not been executed, we need to execute them
            		if(!node.isExecuted())
            		{
            			//keep track of the number of parent nodes that have been executed to facilitate the checking below
            			int numberOfParentNodesExecuted=0;
            			for(ProcessGraphNode parent: node.getParents())
                		{
                			if(parent.isExecuted())
                			{
                				numberOfParentNodesExecuted++;
                			}
                		}	
            			//if the number of parent nodes executed is same as the number of parents for the child, it means the child can begin to execute and we set node to be runnable
            			if(numberOfParentNodesExecuted==node.getParents().size() && !node.isRunnable())
            			{
            				node.setRunnable();
            				ExecuteProcess(node);
            				node.setExecuted();
            				totalNumberOfNodesExecuted++;
            			}
            			//to run all runnable nodes
            			else if(node.isRunnable()){
            				ExecuteProcess(node);
            				node.setExecuted();
            				totalNumberOfNodesExecuted++;
            			}
            		}  
        		//we can break out of while loop when all the nodes have been executed
        		if(totalNumberOfNodesExecuted==ProcessGraph.nodes.size())
        		{
        			allAreExecuted=true;
        		}
        		
        	}
          }
        }
        	
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
        
        System.out.println("All process finished successfully");
    }
   
    public static void ExecuteProcess(ProcessGraphNode node) 
    {
    	//check if we are running the code on Windows or in Linux as in this case when the testing of code was done
    	boolean isWindows=System.getProperty("os.name").toLowerCase().startsWith("windows");
    	
    	//create a new process for every node
    	ProcessBuilder pbBuilder=new ProcessBuilder();
    	pbBuilder.directory(currentDirectory);
    	
    	File input=node.getInputFile();
    	File output=node.getOutputFile();
    	
    	if(!input.toString().equals("stdin"))
    	{	//if input file for process is not "stdin", we redirect the input file to the appropriate file as given in the text file
    		pbBuilder.redirectInput(input);
    	}
    	if(!output.toString().equals("stdout"))
    	{
    		//if output file for process is not "stdout", we redirect the output file to the appropriate file as given in the text file
    		pbBuilder.redirectOutput(output);
    	}
    	if(isWindows)
        {
            pbBuilder.command("cmd.exe","/c","dir");
        }
        else{
            //this is to execute the appropriate commands such as "grep" as written in the text file
            String[] command = {"sh","-c",node.getCommand().toString()};
            pbBuilder.command(command);
        }
    		
    	try{
    		Process process = pbBuilder.start();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    }

}
