package com.mtanevski.master.caa.lib;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
@Ignore("this is a base test, meant to be extended")
public class CaaImplementationBaseTest {

    private static final String GRAPHS_DIR = "src/test/resources/graphs/";
    private static final String GRAPH_ML_EXTENSION = ".graphml";

    public final String graphFilename;
    public final String graphLocation;

    public CaaImplementationBaseTest(String graphFilename, String graphLocation) {
        this.graphFilename = graphFilename;
        this.graphLocation = graphLocation;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<String[]> graphsToTest() {
        try {
            Path graphDirectory = Paths.get(GRAPHS_DIR);
            Predicate<Path> onlyGraphMlFiles = path -> path.getFileName().toString().endsWith(GRAPH_ML_EXTENSION);
            Function<Path, String[]> toString = path -> new String[]{path.getFileName().toString(), path.toAbsolutePath().toString()};
            return Files.list(graphDirectory)
                    .filter(onlyGraphMlFiles)
                    .map(toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
