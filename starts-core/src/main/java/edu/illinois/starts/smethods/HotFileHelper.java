package edu.illinois.starts.smethods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.nio.file.Paths;
import static java.util.Collections.reverseOrder;

import edu.illinois.starts.util.Macros;

public class HotFileHelper {

    public static List<String> hotFiles;

    public static List<String> getHotFiles(int type) {
        List<String> hotFiles = new ArrayList<>();
        if (type == Macros.DEP_HOTFILE) {

        } else if (type == Macros.CHANGE_FRE_HOTFILE) {

        } else if (type == Macros.SIZE_HOTFILE) {
            HashMap<String, Long> fileToSize = new HashMap<>();
            // check all the classes
            try {
                Files.walk(Paths.get("."))
                        .sequential()
                        .filter(x -> !x.toFile().isDirectory())
                        .filter(x -> x.toFile().getAbsolutePath().endsWith(".class"))
                        .forEach(p -> {
                            File classFile = p.toFile();
                            if (classFile.isFile()) {
                                fileToSize.put(classFile.getAbsolutePath(), classFile.length());
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
            // sort the files by size
            HashMap<String, Long> sortedFileToSize = fileToSize.entrySet().stream()
                    .sorted(reverseOrder(Entry.comparingByValue()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));
            // get the top 30% of the files
            int topSize = (int) (sortedFileToSize.size() * 0.35);
            for (int i = 0; i < topSize; i++) {
                hotFiles.add(sortedFileToSize.keySet().toArray()[i].toString());
            }
        }
        return hotFiles;
    }

    public static void main(String[] args) {
        hotFiles = getHotFiles(Macros.SIZE_HOTFILE);
        hotFiles.forEach(System.out::println);
    }
}