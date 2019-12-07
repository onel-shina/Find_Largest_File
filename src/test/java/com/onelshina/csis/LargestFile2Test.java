package com.onelshina.csis;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class LargestFile2Test {


    /* To create temporary directories and files for testing
     * that will be deleted if test fails or if it runs
     * successfully
     */
    @Rule
    public TemporaryFolder temporary = new TemporaryFolder();

    /**
     * BFS (Breadth First Search) to find all the files within the
     * given directory and its subdirectories
     *
     * @param path {@link Path} the file representing the directory we want to search
     * @return {@link File} largest file
     * and it's subdirectories
     */

    @SuppressWarnings({"SameParameterValue", "unchecked"})
    private static File findLargestFileTest(final Path path) {
        File largestFile = null;
        long largestFileSize = 0;
        File dir = path.toFile();
        final ArrayList<File> fileAndDirectories = new ArrayList<File>();
        fileAndDirectories.add(dir);

        while (!fileAndDirectories.isEmpty()) {
            final File f = fileAndDirectories.remove(0);
            if (f.isFile()) {
                long fileSize = f.length();
                if (fileSize > largestFileSize) {
                    largestFile = f;
                    largestFileSize = fileSize;
                } else if (fileSize == largestFileSize) {
                    largestFile = LargestFile2.extreme(largestFile, f);
                }
            } else if (f.isDirectory()) {
                final File[] fa = f.listFiles();
                if (fa != null) {
                    fileAndDirectories.addAll(Arrays.asList(fa));
                }
            }
        }
        return largestFile;
    }

    @Test
    public void extreme() throws IOException {
        File directory1 = temporary.newFolder("TestDirectory");
        //Create another directory within directory1
        File directory2 = temporary.newFolder(directory1.getName(), "TestDirectory2");

        File file1 = temporary.newFile("TestDirectory/test.txt");
        File file2 = temporary.newFile("TestDirectory/TestDirectory2/test2.txt");
        File file3 = temporary.newFile("TestDirectory/TestDirectory2/test3.txt");

        //Both same size, file2 has longer path
        Assert.assertEquals(LargestFile2.extreme(file1, file2), file2);
        /*Both same size and path length, but first found file get passed into extreme method first
         * Therefore, we expect the extreme method to return the last file that was found
         * Which would be file3 in this case
         */
        Assert.assertEquals(LargestFile2.extreme(file2, file3), file3);

    }

    @Test
    public void findLargestFile() throws IOException {
        Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
        File fileMain = LargestFile2.findLargestFile(currentRelativePath); //Using DFS
        File fileTest = LargestFile2Test.findLargestFileTest(currentRelativePath); //Using BFS
        Assert.assertEquals(fileMain, fileTest); //Largest file found by DFS should be equal to largest file found by BFS

        /* Below are more tests to verify that findLargestFile returns the largest file in a
            directory and its subdirectories
         */

        File directory1 = temporary.newFolder("TestDirectory");
        //Create another directory within directory1
        File directory2 = temporary.newFolder(directory1.getName(), "TestDirectory2");
        Path directoryOnePath = directory1.toPath();


        /*
         * Testing the output of System.out if the directory doesn't contain any files.
         *
         * Empty Directory
         */

        assertNull(LargestFile2.findLargestFile(directoryOnePath));

        File file1 = temporary.newFile("TestDirectory/test.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file1, "rw");
        randomAccessFile.setLength(5); //Changing the size of the file
        randomAccessFile.close();

        /* Files in directory:
        file1 - size: 5 -
         */
        assertEquals(LargestFile2.findLargestFile(directoryOnePath), file1); //Directory with only one File

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
        file2 - size: 5 - (longer path length)
        file3 - size: 4 -
         */
        assertEquals(LargestFile2.findLargestFile(directoryOnePath), file2);

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
        assertEquals(LargestFile2.findLargestFile(directoryOnePath), file4);

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
        assertEquals(LargestFile2.findLargestFile(directoryOnePath), file5);
    }

}
