## cselab
## 2018 Programming Assignment 1 for Computer System Engineering
Author: Tan Yi Long, Cindy Ong Wen Ling
ID: 1001566, 1001792
Date: 4th March 2018

### Purpose of Program:
Program enables users to construct a directed acyclic graph (DAG) of user programs from the input text file.

### How to compile Programe:
1. Download the code on github and save it into a folder of your choice.
2. On the command line, navigate to your folder e.g. cd /workspace/cseProgAssign/src
3. Under "ProcessManagement.java" file, change the input file to any file you are interested in running the program on e.g. "graph-textfile1.txt"
4a (For IDE e.g. Eclipse) Click "run" and the program should run.
4b1 Enter "javac ProcessManagement.java" on the command line  
4b2 Enter "java ProcessManagement"
5. The command line should output the output text files when the program is completed.

### What exactly the program does:
1) The program parses in a file to generate a ProcessGraph

2) User views information of the ProcessGraph that is printed out

3) Each line in the input file represents a node. The line is of format:
<program name with arguments :list of children ID's : input file : output file>

4) Program recursively check each node to see if they have been executed. While all the
nodes are still not executed, we check if the nodes are runnable. Nodes may not be executed yet because they have process or data dependencies with other nodes, especially the parent nodes.

5) If the nodes are runnable, program executes them. 
Repeat step 4 until all nodes are executed.
