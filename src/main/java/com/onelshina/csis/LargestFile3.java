package com.onelshina.csis;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Finds the largest file using DFS.
 */

public class LargestFile3 {
    /**
     * If no start location is given, the we start the search in the current dir
     *
     * @param args {@link String}[] start location for the largest file search.
     */
    public static void main(final String[] args) {
        final Path path = Paths.get(args.length < 1 ? "." : args[0]);
        final File ex = findExtremeFile(path);
        if (ex != null) {
            System.out.printf("Starting at : %s, the largest file was found here:\n%s\n its size is: %d bytes\n",
                    path.toAbsolutePath().toString(),
                    ex.getAbsolutePath(),
                    ex.length());
        } else {
            System.out.printf("No Files were found in: %s", path.toAbsolutePath());
        }
    }


    /**
     * Identifies the more extreem of two given files.
     * Modifying this method allows to search for other extreems, like smallest, oldest, etc.
     *
     * @param f1 {@link File} 1st file
     * @param f2 {@link File} 2nd file
     * @return {@link File} the more extreme of the two given files.
     */
    static File extreme(final File f1, final File f2) {
        if (f1 == null) return f2;
        else if (f2 == null) return f1;
        else {
            long fileOneSize = f1.length();
            long fileTwoSize = f2.length();
            if (fileOneSize > fileTwoSize) return f1;
            else if (fileOneSize < fileTwoSize) return f2;
            else {
                int fileOnePathLength = f1.getAbsolutePath().length();
                int fileTwoPathLength = f2.getAbsolutePath().length();
                if (fileOnePathLength > fileTwoPathLength) return f1;
                else if (fileTwoPathLength > fileOnePathLength) return f2;
                else return f1; //f1 is found last.
            }
        }
    }

    /**
     * DFS for the most extreme file, starting the search at a given directory path.
     *
     * @param p {@link Path} path to a directory
     * @return {@link File} most extreme file in the given path
     */
    static File findExtremeFile(final Path p) {
        File x = null;
        final File[] fa = p.toFile().listFiles();
        if (fa != null && fa.length > 0) {
            for (File file : fa) {
                if (file != null) {
                    if (file.isFile()) {
                        x = extreme(file, x);
                    } else if (file.isDirectory()) {
                        x = extreme(findExtremeFile(file.toPath()), x);
                    }
                }
            }
        }
        return x;
    }
}
