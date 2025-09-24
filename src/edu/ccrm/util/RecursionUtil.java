package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class RecursionUtil {
    public static long calculateDirectorySize(Path path) {
        try (Stream<Path> walk = Files.walk(path)) {
            return walk
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            System.err.println("Failed to get size of " + p + ": " + e.getMessage());
                            return 0L;
                        }
                    })
                    .sum();
        } catch (IOException e) {
            System.err.println("Could not walk directory: " + path + ": " + e.getMessage());
            return 0L;
        }
    }
}