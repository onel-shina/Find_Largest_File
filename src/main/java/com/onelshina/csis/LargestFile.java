package com.onelshina.csis;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Finds the largest file using DFS (Depth First Search).
 */
public class LargestFile {

    /**
     * If no start location is given, the we start the search in the current directory
     *
     * @param args {@link String}[] start location for the largest file search.
     */
    public static void main(final String[] args) {
        final Path path = Paths.get(args.length < 1 ? "." : args[0]);
        final File file = findLargestFile(path);
        if (file != null) {
            System.out.printf("Starting at : %s, the largest file was found here:\n%s\n its size is: %d\n",
                    path.toAbsolutePath().toString(),
                    file.getAbsolutePath(),
                    file.length());
        }
    }
    /**
     * Calls {@link #findFilesList(File)} method that returns an ArrayList with files.
     *
     * Iterates over all the the files and returns the largest file.
     *
     * If two files exist with same size (length), it compares their path length and
     * returns the file with the longest path.
     *
     * If both files that are equal in size have the same path length, it returns the file
     * that was found first
     *
     * @param directory {@link Path} The directory we want to search.
     * @return {@link File} the largest File based on the criteria mentioned above.
     */

    protected static File findLargestFile(final Path directory) {
        File largestFile; //Method will return this variable

        long fileSize = 0;
        File firstFile = null;
        File secondFile = null;

        ArrayList<File> filesList = findFilesList(directory.toFile()); //Getting an ArrayList of all files

        File[] filesArray = filesList.toArray(new File[filesList.size()]); //Converting from ArrayList improved running time
        if (filesArray.length == 0) { //If there are no files in the given directory
            System.out.printf("No Files were found in: %s", directory);
        }

        /*
        * Algorithm to find the largest file and assign it to firstFile
        * If there is another file in the array with same size as firstFile
        * but it's not equal to the firstFile, assign it to the secondFile
         */

        for (File file : filesArray) {
            long fileLength = file.length();
            if (fileLength > fileSize) {
                firstFile = file;
                fileSize = fileLength;
            }
            if (fileLength == fileSize && !file.equals(firstFile)) {
                secondFile = file;
            }
        }

        if (secondFile == null) { //If there was not two files with same maximum size
            largestFile = firstFile;

        } else { //If there was two files with the same maximum size
            int firstFilePathLength = firstFile.toPath().getNameCount(); //Path length of the first file
            int secondFilePathLength = secondFile.toPath().getNameCount(); //Path length of the second file

            if (firstFilePathLength > secondFilePathLength) {
                largestFile = firstFile;
            } else if (firstFilePathLength < secondFilePathLength) {
                largestFile = secondFile;
            } else { //If both files have the same path length
                largestFile = firstFile; //Since firstFile is found before secondFile
            }
        }
        return largestFile;
    }

    /**
     * This method is called by {@link #findLargestFile(Path)}
     *
     * This method uses DFS to search the directories and add all files within
     * the given and its subdirectories to an ArrayList.
     *
     *
     * @param directory {@link File} path to a directory, passed by {@link #findLargestFile(Path)}
     * @return {@link ArrayList<File>} an arrayList containing all files within the directory and its
     * subdirectories.
     */

    protected static ArrayList<File> findFilesList(final File directory) {
        ArrayList<File> files = new ArrayList<File>();
        File[] filesAndDirectories = directory.listFiles();
        if (filesAndDirectories != null) {
            for (File file : filesAndDirectories) {
                if (file.isFile()) {
                    files.add(file);
                }
                if (file.isDirectory()) {
                    files.addAll(findFilesList(file));
                }
            }
        }
        return files;
    }
}

