package com.onelshina.csis;

import org.junit.Test;

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
     * @return {@link ArrayList<File>}
     */

    @SuppressWarnings({"SameParameterValue", "unchecked"})
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


    /*
    * Testing findLargestFile method of FileDraft class to ensure
    * that it returns the largest file in the ArrayList
    * Also test the behavior when two files exist with same size
    * And when two files have the same size and path length, then
    * The method should return the file we expect to be found first
     */
    @Test
    public void findLargestFile() throws IOException {
        File directory1 = new File("TestFile");
        File directory2 = new File("TestFile/TestFile2");
        Path directoryOnePath = directory1.toPath();
        directory1.mkdir();
        directory2.mkdir();
        /*
        Empty Directory
         */

        setUpStreams();
        assertNull(LargestFile.findLargestFile(directoryOnePath));
        assertEquals(String.format("No Files were found in: %s", directoryOnePath.toAbsolutePath()), outputStream.toString());
        restoreStreams();

        File file1 = new File("TestFile/test.txt");
        file1.createNewFile();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file1, "rw");
        randomAccessFile.setLength(5); //Changing the size of the file
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 - relative path length: 2
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file1); //Directory with only one File


        File file2 = new File("TestFile/TestFile2/test2.txt");
        file2.createNewFile();
        randomAccessFile = new RandomAccessFile(file2, "rw");
        randomAccessFile.setLength(5);
        randomAccessFile.close();

        File file3 = new File("TestFile/test3.txt");
        file3.createNewFile();
        randomAccessFile = new RandomAccessFile(file3, "rw");
        randomAccessFile.setLength(4);
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 - relative path length: 2
        file2 - size: 5 - relative path length: 3 (Should be returned, because it has the largest size, and a longer path than file1)
        file3 - size: 4 - relative path length: 2
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file2);

        File file4 = new File("TestFile/TestFile2/test3.txt");
        file4.createNewFile();
        randomAccessFile = new RandomAccessFile(file4, "rw");
        randomAccessFile.setLength(10);
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 - relative path length: 2
        file2 - size: 5 - relative path length: 3
        file3 - size: 4 - relative path length: 2
        file4 - size: 10 - relative path length: 3 (Should be returned, because it has the largest size)
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file4);

        File file5 = new File("TestFile/TestFile2/test4.txt");
        file5.createNewFile();
        randomAccessFile = new RandomAccessFile(file5, "rw");
        randomAccessFile.setLength(10);
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 - relative path length: 2
        file2 - size: 5 - relative path length: 3
        file3 - size: 4 - relative path length: 2
        file4 - size: 10 - relative path length: 3 (Should be returned, has the same size and path length as file5, but it should be found first)
        file5 - size: 10 - relative path length: 3
         */
        assertEquals(LargestFile.findLargestFile(directoryOnePath), file4);


        //Deleting all the directories and files we created for testing//
        File[] filesToDelete = {file1, file2, file3, file4, file5, directory2, directory1};
        for (File file : filesToDelete) {
            file.delete();
        }

    }

    /*
     * To test the output of System.out
     * */
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = System.out;
    private void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }
    private void restoreStreams() { System.setOut(printStream);
    }
}