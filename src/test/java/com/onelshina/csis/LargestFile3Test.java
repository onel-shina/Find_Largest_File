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

public class LargestFile3Test {
    private static File findExtremeFileTest(final Path path) {
        File x = null;
        File dir = path.toFile();
        final ArrayList<File> fileAndDirectories = new ArrayList<File>();
        fileAndDirectories.add(dir);

        while (!fileAndDirectories.isEmpty()) {
            final File f = fileAndDirectories.remove(0);
            if (f.isFile()) {
                x = LargestFile3.extreme(f, x);

            } else if (f.isDirectory()) {
                final File[] fa = f.listFiles();
                if (fa != null) {
                    fileAndDirectories.addAll(Arrays.asList(fa));
                }
            }
        }
        return x;
    }

    @Test
    public void main() throws IOException {
        File directory1 = temporary.newFolder("TestDirectory");

        //Testing out output in case of empty directory
        setUpStreams();
        assertNull(LargestFile.findLargestFile(directory1.toPath()));
        assertEquals(String.format("No Files were found in: %s", directory1.getAbsolutePath()), outputStream.toString());
        restoreStreams();
    }

    @Test
    public void extreme() throws IOException {
        Assert.assertEquals(LargestFile3.extreme(null, null), null);

        File file1 = temporary.newFile("test1.txt");
        Assert.assertEquals(LargestFile3.extreme(null, file1), file1);

        File file2 = temporary.newFile("test2test2.txt");

        RandomAccessFile randomAccessFile = new RandomAccessFile(file1, "rw");
        randomAccessFile.setLength(5); //Changing the size of the file1
        randomAccessFile = new RandomAccessFile(file2, "rw");
        randomAccessFile.setLength(10); //Changing the size of the file2
        randomAccessFile.close();

        //file2 size is larger
        Assert.assertEquals(LargestFile3.extreme(file2, file1), file2);

        randomAccessFile = new RandomAccessFile(file1, "rw");
        randomAccessFile.setLength(10); //Changing the size of the file1
        randomAccessFile.close();

        //file1 size is equal to file2 size, file2 has longer path length
        Assert.assertEquals(LargestFile3.extreme(file2, file1), file2);

        File file3 = temporary.newFile("test3test3.txt");
        randomAccessFile = new RandomAccessFile(file3, "rw");
        randomAccessFile.setLength(10); //Changing the size of the file3
        randomAccessFile.close();

        /*
        file3 & file2 have the same size
        file3 & file2 have the same path length
        file3 passed first into extreme method and it's found last by DFS
         */
        Assert.assertEquals(LargestFile3.extreme(file3, file2), file3);

    }

    @Test
    public void findExtremeFile() {
        Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
        File fileMain = LargestFile3.findExtremeFile(currentRelativePath); //Using DFS
        File fileTest = LargestFile3Test.findExtremeFileTest(currentRelativePath); //Using BFS
        Assert.assertEquals(fileMain, fileTest); //Largest file found by DFS should be equal to largest file found by BFS
    }

    /* To create temporary directories and files for testing
     * that will be deleted if test fails or if it runs
     * successfully
     */
    @Rule
    public TemporaryFolder temporary = new TemporaryFolder();

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