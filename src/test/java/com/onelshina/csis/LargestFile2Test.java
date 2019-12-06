package com.onelshina.csis;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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


    @Test
    public void extreme() throws IOException {
        File directory1 = temporary.newFolder("TestDirectory");
        //Create another directory within directory1
        File directory2 = temporary.newFolder(directory1.getName(), "TestDirectory2");

        File file1 = temporary.newFile("TestDirectory/test.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file1, "rw");
        randomAccessFile.setLength(5); //Changing the size of the file
        randomAccessFile.close();

        File file2 = temporary.newFile("TestDirectory/TestDirectory2/test2.txt");
        randomAccessFile = new RandomAccessFile(file2, "rw");
        randomAccessFile.setLength(5); //Changing the size of the file
        randomAccessFile.close();

        File file3 = temporary.newFile("TestDirectory/TestDirectory2/test3.txt");
        randomAccessFile = new RandomAccessFile(file3, "rw");
        randomAccessFile.setLength(5); //Changing the size of the file
        randomAccessFile.close();

        //Both same size, file2 has longer path
        Assert.assertEquals(LargestFile2.extreme(file1, file2), file2);
        /*Both same size and path length, but first found file get passed into extreme method first
        * Therefore, we expect the extreme method to return the first file that was found
        * Which would be file2 in this case
         */
        Assert.assertEquals(LargestFile2.extreme(file2, file3), file2);

    }

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
                }
                else if (fileSize == largestFileSize) {
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
    public void findLargestFile() {
        Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
        File fileMain = LargestFile2.findLargestFile(currentRelativePath); //Using DFS
        File fileTest = LargestFile2Test.findLargestFileTest(currentRelativePath); //Using BFS
        Assert.assertEquals(fileMain, fileTest);
    }
}