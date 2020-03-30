# Visual implementation of CAA algorithm with example usage
Java implementation of the Crossbar Adaptive Array (WIP) with usage

# Requirements

 - Java 1.8
 - Maven
 
# Nice to have
 - Intellij

# Install
 - `git clone https://github.com/Tanevski3/caa-algorithm-visualization.git`
 - `cd ./caa-algorithm-visualization`
 - `mvn clean install`
 
# Run
 - Right Click and Run `ConsoleEntry.java` to check the results
 or
 - Right Click and Run `JavaFxEntry.java` to check the results
 
# Deploy
 - `mvn jfx:jar`
 - `mv jfx/app/caa-experimentation-${version}-jfx.jar jfx/app/caa-experimentation-${version}.jar`
 - `zip jfx/app/. caa-experimentation-${version}`
 - `rm -rf dist/*`
 - `mv caa-experimentation-${version}.zip dits/`

# Usage Guide

[screenshot1]: i1.png
[screenshot2]: i2.png
 
Once File -> Open Graph is clicked a file selection window will appear, where the user can browse through its filesystem to select a graph for loading into the software.
 
In the bottom right corner just above the Open & Cancel buttons there is a list of all supported formats for loading graphs. At the time of writing this paper, GraphML format is the only format that is supported. Once a graph is loaded into the software the working space should be filled with rendering of the graph.  

## Changing preferences
The outlook of the graph displayed in the main area can be modified through File -> Preferences. Besides the color of the nodes & edges within Preferences edge width & vertices labels could also be modified. Additionally, some properties specific to the experiment can be modified; that is the CAA tab.
  
## Experimenting
Experimenting is divided in two menu items:
1.	Agent Traversal
2.	Find Happy State
 
Experiments can be executed with different properties. Clicking on one of the menu items will show a popup for selecting properties of the algorithm used for traversal. Properties are as follows:
•	Agent traversing algorithms: original & advanced
•	Experiment animation 
•	Selecting initial values
 
Once Find Happy State experiment is run, results will be generated in a separate tab. For agent traversal there are no results.
The results are related to the graph that is being experimented on.

## Experiment Results
The experiment results are divided in two parts.
1.	Overall vertices traversals tab contains the following data
    a.	Vertices occurrences per generation is showing how many traversals have occurred for each vertex
    b.	“Happy” vs “Sad” generation is showing how each generation ended
    c.	Happy to shortest path factor is the percentage of edges that match from the happy path to the shortest path 
2.	Vertices traversals per generation tab goes more intro details about a single generation. It shows the increments, traversed edges, increments weight & traversal weight. The results are paged depending on the number of generations.

   

## Viewing additional statistics & shortest path
Additionally, the software provides a visualization of the shortest path & vertices distribution. That can be done through the View menu.
 
  

# Available as JAR
 - The application is now available as a JAR file. Within the `dist/` directory of this repository the executable JAR file can be found.

Check console and generated graph for results or use the application to run own simulations or traversals

