package com.onelshina.csis;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Finds the largest file using DFS (Depth First Search).
 */

public class LargestFile2 {

    /**
     * If no start location is given, the we start the search in the current directory
     *
     * @param args {@link String}[] start location for the largest file search.
     */
    public static void main(final String[] args) {
        final Path path = Paths.get(args.length < 1 ? "." : args[0]);
        final File file = findLargestFile(path);
        if (file != null) {
            System.out.printf("Starting at : %s, the largest file was found here:\n%s\n its size is: %d bytes\n",
                    path.toAbsolutePath().toString(),
                    file.getAbsolutePath(),
                    file.length());
        }
        else {
            System.out.printf("No Files were found in: %s", path.toAbsolutePath());
        }
    }

    /**
     * Identifies the more extreem of two given files.
     * Modifying this method allows to search for other extreems, like smallest, oldest, etc.
     *
     * @param file1 {@link File} 1st file (file found first)
     * @param file2 {@link File} 2nd file (file found last)
     * @return {@link File} the more extreme of the two given files.
     */

    protected static File extreme(final File file1, final File file2) {
        int fileOnePathLength = file1.getAbsolutePath().length();
        int fileTwoPathLength = file2.getAbsolutePath().length();
        File largestFile;

        /* * *file1 is found first, file2 is found last* * *
         *
         * if file2's (found last) path length larger or equal to file1's path length
         * return file2
         *
         * if file1's (found first) path length larger than file'2 path length
         * return file1
         */
        if (fileTwoPathLength >= fileOnePathLength) {
            largestFile = file2;
        } else {
            largestFile = file1; //Found last and equal path length, or longer path length than file1
        }
        return largestFile;
    }

    /**
     * This method calls {@link #largestFile(File, File, long)}
     * Passing important initial arguments into this method which contains
     * DFS for the most extreme file, starting the search at a given directory path.
     *
     * @param path {@link Path} path to a directory
     * @return {@link File} most extreme file in the given path
     */

    protected static File findLargestFile(final Path path) {
        return largestFile(path.toFile(), null, 0);
    }


    private static File largestFile(final File inputDirectory, final File largestFileFound, final long largestFileFoundSize) {
        File largestFile = largestFileFound;
        long largestFileSize = largestFileFoundSize;
        File[] files = inputDirectory.listFiles();
        if (files != null) {
            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    currentFile = largestFile(currentFile, largestFile, largestFileSize); //Recursive calls
                }
                if (currentFile != null && currentFile.isFile()) {
                    long currentFileLength = currentFile.length();
                    if (currentFileLength > largestFileSize || largestFile == null) {
                        largestFile = currentFile;
                        largestFileSize = currentFileLength;
                    } else if (currentFileLength == largestFileSize) {
                        /* Both files passed into extreme method are not null, and have the same size
                        *  largestFile: found first
                        *  currentFile: found last
                         */
                        largestFile = extreme(largestFile, currentFile);
                    }
                }
            }
        }
        return largestFile;
    }
}
