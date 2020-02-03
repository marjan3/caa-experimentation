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


# Available as JAR
 - The application is now available as a JAR file. Within the `dist/` directory of this repository the executable JAR file can be found.

Check console and generated graph for results or use the application to run own simulations or traversals

