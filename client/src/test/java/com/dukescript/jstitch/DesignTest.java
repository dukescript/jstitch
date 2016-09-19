/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.jstitch;

import com.dukescript.jstitch.DSTModel.Stitch;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author antonepple
 */
public class DesignTest {

    String[] testFiles = new String[]{
        //        "Embroidermodder.DST","TurtleCLASSIC12.DST",
        "GOLDFISH.dst", "KING.dst", "Bone.DST", "Motiv3.dst", "REPTILES-FM.DST", "SYM0040.DST",
        "Star.DST", "lantern.DST"};

    public DesignTest() {
    }

    @Test
    public void testCompareOriginalandWrittenHeader() throws IOException {
        for (String testFile : testFiles) {
            InputStream resourceAsStream = DesignTest.class.getResourceAsStream(testFile);
            DSTModel readDesign = DSTModel.readDesign(resourceAsStream);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            readDesign.write(output);
            byte[] data = output.toByteArray();
            InputStream original = DesignTest.class.getResourceAsStream(testFile);
            ByteArrayInputStream copy = new ByteArrayInputStream(data);
            byte[] data1 = new byte[512];
            byte[] data2 = new byte[512];

            original.read(data1, 0, 512);
            copy.read(data2, 0, 512);
//            for (int i = 0; i < 512; i++) {
//                System.out.println(""+i+":"+data1[i]+" "+data2[i]);
//            }
            for (int i = 0; i < 512; i++) {
                Assert.assertEquals(data2[i], data1[i], "mismatch in design " + testFile + " at pos " + i);
            }

        }
    }

    @Test
    public void testCompareOriginalandWrittenBytes() throws IOException {
        for (String testFile : testFiles) {
            System.out.println("testing " + testFile);
            InputStream resourceAsStream = DesignTest.class.getResourceAsStream(testFile);
            DSTModel readDesign = DSTModel.readDesign(resourceAsStream);
            Stitch[] stitches = readDesign.getStitches();
//            for (int i = stitches.length-10; i <= stitches.length-1; i++) {
//                Stitch stitch = stitches[i];
//                System.out.println(""+stitch);
//            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            readDesign.write(output);
            FileOutputStream fileOutputStream = new FileOutputStream(testFile);
            readDesign.write(fileOutputStream);
            fileOutputStream.close();
            byte[] data = output.toByteArray();
            InputStream original = DesignTest.class.getResourceAsStream(testFile);
            ByteArrayInputStream copy = new ByteArrayInputStream(data);
            byte[] data1 = DSTModel.readFully(original);
            byte[] data2 = DSTModel.readFully(copy);
//            System.out.println("Array should be "+(512+(readDesign.getNumStitches()*3))+" long");
            System.out.format("original is %d bytes long\n", data1.length);
            System.out.format("copy is %d bytes long\n", data2.length);
            for (int i = 0; i < data1.length; i++) {
//               if (data1[i]!=data2[i]) System.out.format("data1[%d]=%s, data2=%s\n",i,String.format("%8s", Integer.toBinaryString(data1[i] & 0xFF)).replace(' ', '0'),String.format("%8s", Integer.toBinaryString(data2[i] & 0xFF)).replace(' ', '0'));
                Assert.assertEquals(String.format("%8s", Integer.toBinaryString(data1[i] & 0xFF)).replace(' ', '0'), String.format("%8s", Integer.toBinaryString(data2[i] & 0xFF)).replace(' ', '0'), "mismatch in design " + testFile + " at pos " + i);
            }

        }
    }

    @Test
    public void testCompareValidVsInvalid() throws IOException {

        InputStream resourceAsStream = DesignTest.class.getResourceAsStream("test-2.DST");
        byte[] data1 = DSTModel.readFully(resourceAsStream);

        InputStream invalid = DesignTest.class.getResourceAsStream("test.DST");
        byte[] data2 = DSTModel.readFully(invalid);
//            System.out.println("Array should be "+(512+(readDesign.getNumStitches()*3))+" long");
        System.out.format("valid is %d bytes long\n", data1.length);
        System.out.format("invalid is %d bytes long\n", data2.length);
        int i = 0;
        for (; i < data2.length; i++) {
            if (data1[i] != data2[i]) {
//                System.out.format("data1[%d]=%s, data2=%s\n", i, String.format("%8s", Integer.toBinaryString(data1[i] & 0xFF)).replace(' ', '0'), String.format("%8s", Integer.toBinaryString(data2[i] & 0xFF)).replace(' ', '0'));
            System.out.format("data1[%d]=%d, data2=%d\n", i, data1[i], data2[i]);
            }
//            Assert.assertEquals(String.format("%8s", Integer.toBinaryString(data1[i] & 0xFF)).replace(' ', '0'), String.format("%8s", Integer.toBinaryString(data2[i] & 0xFF)).replace(' ', '0'), "mismatch in design at pos " + i);
        }
        for (; i < data1.length; i++) {
            System.out.format("data1[%d]=%s\t%d\n", i, String.format("%8s", Integer.toBinaryString(data1[i] & 0xFF)).replace(' ', '0'), data1[i]);
        }
    }

    @Test
    public void testValidFile() throws IOException {

        InputStream resourceAsStream = DesignTest.class.getResourceAsStream("test-2.DST");
        DSTModel readDesign = DSTModel.readDesign(resourceAsStream);
        Stitch[] stitches = readDesign.getStitches();
        for (int i = 0; i < stitches.length; i++) {
            Stitch stitch = stitches[i];
            System.out.println(""+i+" "+stitch);
        }
    }

    @Test
    public void testWriteFile() throws FileNotFoundException, IOException {
        DSTModel design = new DSTModel();
        String motiv = "Motiv";

        design.setMotiv(motiv);
        final int colors = 0;
        design.setColors(colors);
        final int h = 100;
        design.setHeight(h);
        final int mD = 100;
        design.setMaxDist(mD);
        final int st = 48;
        design.setNumStitches(st);
        final int w = 100;
        design.setWidth(w);
        final int xMax = 30;
        design.setxMax(xMax);
        final int xMin = 30;
        design.setxMin(xMin);
        final int yMax = 30;
        design.setyMax(yMax);
        final int yMin = 30;
        design.setyMin(yMin);
        design.setPd("******");

        Stitch[] stitches = new Stitch[st];
        stitches[0] = new Stitch(true, false, 0, 0);
        stitches[1] = new Stitch(true, false, 0, 2);
        stitches[2] = new Stitch(true, false, -100, -100);
        stitches[3] = new Stitch(true, false, 0, 0);
        for (int i = 4; i < 14; i++) {
            stitches[i] = new Stitch(false, false, 20, 0);
        }
        for (int i = 14; i < 24; i++) {
            stitches[i] = new Stitch(false, false, 0, 20);
        }
        for (int i = 24; i < 34; i++) {
            stitches[i] = new Stitch(false, false, -20, 0);
        }
        for (int i = 34; i < 44; i++) {
            stitches[i] = new Stitch(false, false, 0, -20);
        }
        stitches[44] = new Stitch(false, false, 0, 0);
        stitches[45] = new Stitch(true, false, 0, 0);
        stitches[46] = new Stitch(true, false, 95, 102);
        stitches[47] = new Stitch(false, true, 0, 0);
        design.setStitches(stitches);
        design.correctMaxMin();
        FileOutputStream fileOutputStream = new FileOutputStream("test.DST");
        design.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Test
    public void testWriteHeader() throws IOException {
        DSTModel design = new DSTModel();
        String motiv = "Motiv";

        design.setMotiv(motiv);
        final int colors = 4;
        design.setColors(colors);
        final int h = 500;
        design.setHeight(h);
        final int mD = 500;
        design.setMaxDist(mD);
        final int st = 10;
        design.setNumStitches(st);
        final int w = 500;
        design.setWidth(w);
        final int xMax = 5123;
        design.setxMax(xMax);
        final int xMin = 9232;
        design.setxMin(xMin);
        final int yMax = 9237;
        design.setyMax(yMax);
        final int yMin = 2334;
        design.setyMin(yMin);

        Stitch[] stitches = new Stitch[st];
        for (int i = 0; i < stitches.length; i++) {
            stitches[i] = new Stitch(true, true, 1, 1);
        }
        design.setStitches(stitches);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        design.write(output);
        FileOutputStream fileOutputStream = new FileOutputStream("testdesign.dst");
        design.write(fileOutputStream);
        fileOutputStream.close();
        byte[] data = output.toByteArray();
        DSTModel readDesign = DSTModel.readDesign(new ByteArrayInputStream(data));
        Assert.assertEquals(readDesign.getMotiv().trim(), motiv);
        Assert.assertEquals(readDesign.getColors(), colors);
        Assert.assertEquals(readDesign.getNumStitches(), st);
        Assert.assertEquals(readDesign.getxMax(), xMax);
        Assert.assertEquals(readDesign.getyMax(), yMax);
        Assert.assertEquals(readDesign.getyMin(), yMin);
        Assert.assertEquals(readDesign.getxMin(), xMin);
    }

}
