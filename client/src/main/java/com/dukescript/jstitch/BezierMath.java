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
public class BezierMath {

    static Vector calculateBezierPoint(double t,
            Vector p0, Vector p1, Vector p2, Vector p3) {
        
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;
        double x = (uuu *p0.getX())+(3 * uu * t * p1.getX())+(3 * u * tt * p2.getX())+(ttt * p3.getX());
        double y = (uuu *p0.getY())+(3 * uu * t * p1.getY())+(3 * u * tt * p2.getY())+(ttt * p3.getY());
        return new Vector(x, y);
    }
    
    
    static double calculateSlope(Vector v1, Vector v2){
        return (v1.getY()-v2.getY())/(v1.getX()-v2.getX());
    }
    
    static double calculateYIntercept(Vector v, float slope){
        return v.getY() - (slope * v.getX());        
    }
    
   
}
