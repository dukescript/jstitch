/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.jstitch;

/**
 *
 * @author antonepple
 */
public class Vector {

    private final double x;
    private final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double dist(Vector v2) {
        double px = v2.getX() - this.getX();
        double py = v2.getY() - this.getY();
        return (double)Math.sqrt(px * px + py * py);
    }

}
