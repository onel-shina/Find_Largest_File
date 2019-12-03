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
            System.out.printf("Starting at : %s, the largest file was found here:\n%s\n its size is: %d bytes\n",
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
        ArrayList<File> filesList = findFilesList(directory.toFile()); //Getting an ArrayList of all files

        File[] filesArray = filesList.toArray(new File[filesList.size()]); //Converting from ArrayList improved running time
        if (filesArray.length == 0) { //If there are no files in the given directory
            System.out.printf("No Files were found in: %s", directory.toAbsolutePath());
        }

        /*
        * For loop to find the largest file and assign it to largestFile
        *
        * If there is another file in the array with same size as firstFile
        * but it's path length is longer than the path length of the previous largestFile
        * reassign the value of largestFile to the the file with the longer path length
        *
        * If another file existed in the array with the same size and path length as the
        * first found largestFile, then the value of the largestFile will remain unchanged
        * and the file found first will be returned.
         */
        File largestFile = null; //Method will return this variable
        long fileSize = 0; //Size of largestFile

        for (File file : filesArray) {
            long fileLength = file.length();
            if (fileLength > fileSize) {
                largestFile = file;
                fileSize = fileLength;
            }
            /*If another file exists with same size as largestFile, but with a longer path
            *   assign the value of largestFile to the file with longer path
             */
            if (fileLength == fileSize && file.toPath().getNameCount() > largestFile.toPath().getNameCount()) {
                largestFile = file;
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

