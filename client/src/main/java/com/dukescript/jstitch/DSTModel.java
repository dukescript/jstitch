/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.jstitch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author antonepple
 */
public class DSTModel {

    private int numStitches = 0;
    private int colors = 0;
    private int xMin = 0;
    private int yMin = 0;
    private int xMax = 0;
    private int yMax = 0;
    private double maxDist;
    private double width;
    private double height;
    private String motiv = "";
    private int ax = 0;
    private int ay = 0;
    private int mx = 0;
    private int my = 0;
    private String pd;
    private String axSign = "+";
    private String aySign = "+";
    private String mxSign = "+";
    private String mySign = "+";
    private Stitch[] stitches = new Stitch[0];

    public static DSTModel readDesign(InputStream in) throws IOException {
        DSTModel design = new DSTModel();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        bufferedInputStream.mark(Integer.MAX_VALUE);
        design.readHeader(bufferedInputStream);
        bufferedInputStream.reset();
        byte[] data = readFully(bufferedInputStream);
        bufferedInputStream.close();
        design.readStitches(data);
        return design;
    }

    public static byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    private void readHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int bytes = 0;
        for (int i = 0; i < 12; ++i) {
            final String readLine = br.readLine();
            bytes += (2 * readLine.length()) + 2;
            String[] tokens = readLine.split(":");

            switch (tokens[0]) {
                case "LA":
                    motiv = tokens[1];
                    break;
                case "ST":
                    numStitches = Integer.parseInt(tokens[1].trim());
                    break;
                case "CO":
                    colors = Integer.parseInt(tokens[1].trim());
                    break;
                case "+X":
                    xMax = Integer.parseInt(tokens[1].trim());
                    break;
                case "+Y":
                    yMax = Integer.parseInt(tokens[1].trim());
                    break;
                case "-Y":
                    yMin = Integer.parseInt(tokens[1].trim());
                    break;
                case "-X":
                    xMin = Integer.parseInt(tokens[1].trim());
                    break;
                case "AX":
                    axSign = tokens[1].substring(0, 1);
                    ax = Integer.parseInt(tokens[1].substring(1).trim());
                    break;
                case "AY":
                    aySign = tokens[1].substring(0, 1);
                    ay = Integer.parseInt(tokens[1].substring(1).trim());
                    break;
                case "MX":
                    mxSign = tokens[1].substring(0, 1);
                    mx = Integer.parseInt(tokens[1].substring(1).trim());
                    break;
                case "MY":
                    mySign = tokens[1].substring(0, 1);
                    my = Integer.parseInt(tokens[1].substring(1).trim());
                    break;
                case "PD":
                    pd = tokens[1];
                    break;
                default:
                    break;
            }
        }
        width = xMax + xMin;
        height = yMax + yMin;
        maxDist = Math.max(width, height);
        System.out.println("" + toString());
//        double scale = Math.min((1000d / maxDist), 1.0) - .2;
    }

    @Override
    public String toString() {
        return "Design{" + "numStitches=" + numStitches + ", colors=" + colors + ", xMin=" + xMin + ", yMin=" + yMin + ", xMax=" + xMax + ", yMax=" + yMax + ", maxDist=" + maxDist + ", width=" + width + ", height=" + height + ", motiv=" + motiv + ", ax=" + ax + ", ay=" + ay + ", mx=" + mx + ", my=" + my + ", pd=" + pd + ", axSign=" + axSign + ", aySign=" + aySign + ", mxSign=" + mxSign + ", mySign=" + mySign + '}';
    }

    private void readStitches(byte[] data) {

        boolean start = true;
        int x = 0, y = 0;
        int i = 512;
        int maX = Integer.MIN_VALUE;
        int maY = Integer.MIN_VALUE;
        int miX = Integer.MAX_VALUE;
        int miY = Integer.MAX_VALUE;
        stitches = new Stitch[numStitches];
        for (; i < 512 + (numStitches * 3); i = i + 3) {
            int incX = 0, incY = 0;
            boolean jump = false, colorchange = false;
            byte b1 = data[i];
            byte b2 = data[i + 1];
            byte b3 = data[i + 2];
            if (isSet(b1, 0)) {
                incX += 1;
            }
            if (isSet(b1, 1)) {
                incX -= 1;
            }
            if (isSet(b1, 2)) {
                incX += 9;
            }
            if (isSet(b1, 3)) {
                incX -= 9;
            }
            if (isSet(b1, 4)) {
                incY -= 9;
            }
            if (isSet(b1, 5)) {
                incY += 9;
            }
            if (isSet(b1, 6)) {
                incY -= 1;
            }
            if (isSet(b1, 7)) {
                incY += 1;
            }
            if (isSet(b2, 0)) {
                incX += 3;
            }
            if (isSet(b2, 1)) {
                incX -= 3;
            }
            if (isSet(b2, 2)) {
                incX += 27;
            }
            if (isSet(b2, 3)) {
                incX -= 27;
            }
            if (isSet(b2, 4)) {
                incY -= 27;
            }
            if (isSet(b2, 5)) {
                incY += 27;
            }
            if (isSet(b2, 6)) {
                incY -= 3;
            }
            if (isSet(b2, 7)) {
                incY += 3;
            }
            if (isSet(b3, 0)) {
                // always true
            } else {
                System.out.println("error b3, bit 0 is set");
            }
            if (isSet(b3, 1)) {
                // always true
            } else {
                System.out.println("error b3, bit 1 is set");
            }
            if (isSet(b3, 2)) {
                incX += 81;
            }
            if (isSet(b3, 3)) {
                incX -= 81;
            }
            if (isSet(b3, 4)) {
                incY -= 81;
            }
            if (isSet(b3, 5)) {
                incY += 81;
            }
            if (isSet(b3, 7)) {
                if (isSet(b3, 6)) {
                    colorchange = true;
                } else {
                    jump = true;
                }
            }
            if (start) {
                x += incX;
                y -= incY;
                start = false;
            } else {
                x += incX;
                y -= incY;
            }
            if (jump || colorchange) {
                start = true;
            }
            if (jump) {

            }
            if (x > maX) {
                maX = x;
            }
            if (x < miX) {
                miX = x;
            }
            if (y > maY) {
                maY = y;
            }
            if (y < miY) {
                miY = y;
            }
            stitches[((i - 512) / 3)] = new Stitch(jump, colorchange, incX, incY);
        }
        System.out.format("maxX %d maxY %d minX %d minY %d\n", maX, maY, miX, miY);
//        System.out.println("last " + data[data.length - 1]);
    }

    private static Boolean isSet(byte b, int bit) {
        return (b & (1 << bit)) != 0;
    }

    public void write(OutputStream out) throws IOException {
        byte[] data = new byte[512 + (numStitches * 3) + 1];
        ByteBuffer target = ByteBuffer.wrap(data);
        fillHeader(target);
        writeStitches(target);
        out.write(data);
        out.close();
    }

    private void fillHeader(ByteBuffer target) {

        String la = "LA:" +fillSpaces(16,motiv)+ motiv + "\r";
        target.put(la.getBytes());

        final int allowed = 7;
        String fill = fillSpaces(allowed, numStitches);
        String st = "ST:" + fill + numStitches + "\r";
        target.put(st.getBytes());
        fill = fillSpaces(3, colors);
        String co = "CO:" + fill + colors + "\r";
        target.put(co.getBytes());
        fill = fillSpaces(5, xMax);
        String plusX = "+X:" + fill + xMax + "\r";
        target.put(plusX.getBytes());
        fill = fillSpaces(5, xMin);
        String minusX = "-X:" + fill + xMin + "\r";
        target.put(minusX.getBytes());
        fill = fillSpaces(5, yMax);
        String plusY = "+Y:" + fill + yMax + "\r";
        target.put(plusY.getBytes());
        fill = fillSpaces(5, yMin);
        String minusY = "-Y:" + fill + yMin + "\r";
        target.put(minusY.getBytes());
        fill = fillSpaces(5, ax);
        String axP = "AX:" + axSign + fill + ax + "\r";
        target.put(axP.getBytes());
        fill = fillSpaces(5, ay);
        String axM = "AY:" + aySign + fill + ay + "\r";
        target.put(axM.getBytes());
        fill = fillSpaces(5, mx);
        String mxP = "MX:" + mxSign + fill + mx + "\r";
        target.put(mxP.getBytes());
        fill = fillSpaces(5, my);
        String mxM = "MY:" + mySign + fill + my + "\r";
        target.put(mxM.getBytes());
        String p = "PD:" + pd + "\r";
        target.put(p.getBytes());

        target.put(new Integer(26).byteValue());
        for (int i = target.position(); i < 512; i++) {
            target.put(new Integer(32).byteValue());
        }
    }

    private String fillSpaces(final int allowed, final int value) {
        return fillSpaces(allowed, ""+value);
    }
    
    private String fillSpaces(final int allowed, final String value) {
        int positions = value.length();
        String fill = "";
        for (int i = 0; i < (allowed - positions); i++) {
            fill += " ";
        }
        return fill;
    }
    

    public int getNumStitches() {
        return numStitches;
    }

    public void setNumStitches(int numStitches) {
        this.numStitches = numStitches;
    }

    public Stitch[] getStitches() {
        return stitches;
    }

    public void setStitches(Stitch[] stitches) {
        this.stitches = stitches;
        this.setNumStitches(stitches.length);
    }

    public int getColors() {
        return colors;
    }

    public void setColors(int colors) {
        this.colors = colors;
    }

    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    public double getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(double maxDist) {
        this.maxDist = maxDist;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getMotiv() {
        return motiv;
    }

    public void setMotiv(String motiv) {
        this.motiv = motiv;
    }

    public int getAx() {
        return ax;
    }

    public void setAx(int ax) {
        this.ax = ax;
    }

    public int getAy() {
        return ay;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

    public int getMx() {
        return mx;
    }

    public void setMx(int mx) {
        this.mx = mx;
    }

    public int getMy() {
        return my;
    }

    public void setMy(int my) {
        this.my = my;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getAxSign() {
        return axSign;
    }

    public void setAxSign(String axSign) {
        this.axSign = axSign;
    }

    public String getAySign() {
        return aySign;
    }

    public void setAySign(String aySign) {
        this.aySign = aySign;
    }

    public String getMxSign() {
        return mxSign;
    }

    public void setMxSign(String mxSign) {
        this.mxSign = mxSign;
    }

    public String getMySign() {
        return mySign;
    }

    public void setMySign(String mySign) {
        this.mySign = mySign;
    }

    public void correctMaxMin() {
        int maX = Integer.MIN_VALUE;
        int maY = Integer.MIN_VALUE;
        int miX = Integer.MAX_VALUE;
        int miY = Integer.MAX_VALUE;
        int x = 0, y = 0;
        numStitches = stitches.length;
        for (int i = 0; i < numStitches; i++) {

            Stitch current = stitches[i];

            int incX = current.getIncX();
            int incY = current.getIncY();
            x += incX;
            y += incY;
            System.out.format("x: %d y:%d\n", x, y);
            if (x > maX) {
                maX = x;
            }
            if (x < miX) {
                miX = x;
            }
            if (y > maY) {
                maY = y;
            }
            if (y < miY) {
                miY = y;
            }
        }
        this.xMax = maX;
        this.xMin = Math.abs(miX);
        this.yMax = maY;
        this.yMin = Math.abs(miY);
    }

    private void writeStitches(ByteBuffer target) {

        for (int i = 0; i < numStitches; i++) {

            Stitch current = stitches[i];

            int incX = current.getIncX();
            int incY = current.getIncY();

            int s = i * 3;
            byte b1 = 0x00;
            byte b2 = 0x00;
            byte b3 = 3;
            if (incX > 121 || incX < -121) {
                System.out.format("x not in valid range [-121,121] , x = %d\n", incX);
            }
            if (incY > 121 || incY < -121) {
                System.out.format("y not in valid range [-121,121] , y = %d\n", incY);
            }
            if (incX >= +41) {
                b3 |= (1 << 2);
                incX -= 81;
            }
            if (incX <= -41) {
                b3 |= (1 << 3);
                incX += 81;
            }
            if (incX >= +14) {
                b2 |= (1 << 2);
                incX -= 27;
            }
            if (incX <= -14) {
                b2 |= (1 << 3);
                incX += 27;
            }
            if (incX >= +5) {
                b1 |= (1 << 2);
                incX -= 9;
            }
            if (incX <= -5) {
                b1 |= (1 << 3);
                incX += 9;
            }
            if (incX >= +2) {
                b2 |= (1 << 0);
                incX -= 3;
            }
            if (incX <= -2) {
                b2 |= (1 << 1);
                incX += 3;
            }
            if (incX >= +1) {
                b1 |= (1 << 0);
                incX -= 1;
            }
            if (incX <= -1) {
                b1 |= (1 << 1);
                incX += 1;
            }
            if (incX != 0) {
                System.out.format("x should be zero yet x = %d\n", incX);
            }
            if (incY >= +41) {
                b3 |= (1 << 5);
                incY -= 81;
            }
            if (incY <= -41) {
                b3 |= (1 << 4);
                incY += 81;
            }
            if (incY >= +14) {
                b2 |= (1 << 5);
                incY -= 27;
            }
            if (incY <= -14) {
                b2 |= (1 << 4);
                incY += 27;
            }
            if (incY >= +5) {
                b1 |= (1 << 5);
                incY -= 9;
            }
            if (incY <= -5) {
                b1 |= (1 << 4);
                incY += 9;
            }
            if (incY >= +2) {
                b2 |= (1 << 7);
                incY -= 3;
            }
            if (incY <= -2) {
                b2 |= (1 << 6);
                incY += 3;
            }
            if (incY >= +1) {
                b1 |= (1 << 7);
                incY -= 1;
            }
            if (incY <= -1) {
                b1 |= (1 << 6);
                incY += 1;
            }
            if (incY != 0) {
                System.out.format("y should be zero yet y = %d\n", incY);
            }
            if (current.isColorChange()) {
                b3 |= (1 << 6);
                b3 |= (1 << 7);
            }
            if (current.isJump()) {
                b3 |= (1 << 7);
            }
            target.put(b1);
            target.put(b2);
            if (i == numStitches - 1) {
                target.put((byte) -13);
            } else {
                target.put(b3);
            }
        }
        target.put((byte) 26);
    }

    public static class Stitch {

        private boolean jump;
        private boolean colorChange;
        private int incX;
        private int incY;

        public Stitch(boolean jump, boolean colorChange, int incX, int incY) {
            this.jump = jump;
            this.colorChange = colorChange;
            this.incX = incX;
            this.incY = incY;
        }

        public boolean isJump() {
            return jump;
        }

        public void setJump(boolean jump) {
            this.jump = jump;
        }

        public boolean isColorChange() {
            return colorChange;
        }

        public void setColorChange(boolean colorChange) {
            this.colorChange = colorChange;
        }

        public int getIncX() {
            return incX;
        }

        public void setIncX(int incX) {
            this.incX = incX;
        }

        public int getIncY() {
            return incY;
        }

        public void setIncY(int incY) {
            this.incY = incY;
        }

        @Override
        public String toString() {
            return "Stitch[ jump:" + jump + ",colorChange:" + colorChange + ", incX:" + incX + ", incY:" + incY + "]";
        }

    }

}
