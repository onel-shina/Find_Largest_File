package com.onelshina.csis;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


import static org.junit.Assert.*;

public class LargestFileTest {

    @Test
    public void findFilesList() {
        //Get the parent path of the current directory
        Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
        File searchDirectory = new File(String.valueOf(currentRelativePath));

        //ArrayList of all files within the directory using DFS in main class
        ArrayList<File> listDFS = LargestFile.findFilesList(searchDirectory);

        //ArrayList of all files within the directory using BFS in test class
        ArrayList<File> listBFS = LargestFileTest.findFilesListTest(searchDirectory);

        //All files in listDFS must be in listBFS, all files in listBFS must be in listDFS, and both arrayLists should have the same size
        assertTrue(listBFS.containsAll(listDFS) && listDFS.containsAll(listBFS) && listBFS.size() == listDFS.size());
    }

    /**
     * BFS (Breadth First Search) to find all the files within the
     * given directory and its subdirectories
     *
     * @param dir {@link File} the file representing the directory we want to search
     * @return {@link ArrayList<File>} containing all files within the @param dir {@link File}
     * and it's subdirectories
     */

    private static ArrayList<File> findFilesListTest(final File dir) {
        final ArrayList<File> fileAndDirectories = new ArrayList<File>();
        ArrayList<File> files = new ArrayList<File>();
        fileAndDirectories.add(dir);

        while (!fileAndDirectories.isEmpty()) {
            final File f = fileAndDirectories.remove(0);
            if (f.isFile()) {
                files.add(f);
            } else if (f.isDirectory()) {
                final File[] fa = f.listFiles();
                if (fa != null) {
                    fileAndDirectories.addAll(Arrays.asList(fa));
                }
            }
        }
        return files;

    }

    /* To create temporary directories and files for testing
    * that will be deleted if test fails or if it runs
    * successfully
    */
    @Rule
    public TemporaryFolder temporary = new TemporaryFolder();


    /*
    * Testing findLargestFile method of LargestFile class to ensure
    * that it returns the largest file in the ArrayList
    *
    * Also test the behavior when two files exist with same size,
    * then the method should return the file with longest path
    *
    * And when two files have the same size and path length, then
    * The method should return the file we expect to be found the last
     */
    @Test
    public void findLargestFile() throws IOException {
        File directory1 = temporary.newFolder("TestDirectory");
        //Create another directory within directory1
        File directory2 = temporary.newFolder(directory1.getName(), "TestDirectory2");
        Path directoryOnePath = directory1.toPath();


        /*
        * Testing the output of System.out if the directory doesn't contain any files.
        *
        * Empty Directory
         */
        setUpStreams();
        assertNull(LargestFile.findLargestFile(directoryOnePath));
        assertEquals(String.format("No Files were found in: %s", directoryOnePath.toAbsolutePath()), outputStream.toString());
        restoreStreams();

        File file1 = temporary.newFile("TestDirectory/test.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file1, "rw");
        randomAccessFile.setLength(5); //Changing the size of the file
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file1); //Directory with only one File

        File file2 = temporary.newFile("TestDirectory/TestDirectory2/test2.txt");
        randomAccessFile = new RandomAccessFile(file2, "rw");
        randomAccessFile.setLength(5);
        randomAccessFile.close();

        File file3 = temporary.newFile("TestDirectory/test3.txt");
        randomAccessFile = new RandomAccessFile(file3, "rw");
        randomAccessFile.setLength(4);
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 -
        file2 - size: 5 - (longest path)
        file3 - size: 4 -
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file2);

        File file4 = temporary.newFile("TestDirectory/TestDirectory2/test3.txt");
        randomAccessFile = new RandomAccessFile(file4, "rw");
        randomAccessFile.setLength(10);
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 -
        file2 - size: 5 -
        file3 - size: 4 -
        file4 - size: 10 - (largest size)
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file4);

        File file5 = temporary.newFile("TestDirectory/TestDirectory2/test4.txt");
        randomAccessFile = new RandomAccessFile(file5, "rw");
        randomAccessFile.setLength(10);
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 -
        file2 - size: 5 -
        file3 - size: 4 -
        file4 - size: 10 - (largest size) (equal path length to file5) (found first)
        file5 - size: 10 - (largest size) (equal path length to file4) (found last)
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file5);
    }

    //To test out the result of System.out
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = System.out;
    private void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }
    private void restoreStreams() {
        System.setOut(printStream);
    }
}