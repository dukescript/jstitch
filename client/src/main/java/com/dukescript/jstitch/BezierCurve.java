/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.jstitch;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonepple
 */
public class BezierCurve {
    
    private final Vector start;
    private final Vector end;
    private final Vector control1;
    private final Vector control2;
    private final double dist;
    private final List<Vector> points;
    private final double length;
    
    public BezierCurve(Vector start, Vector control1, Vector control2, Vector end, double dist) {
        this.start = start;
        this.end = end;
        this.control1 = control1;
        this.control2 = control2;
        this.dist = dist;
        this.points = new ArrayList<>();
        double max = 100;
        double seg = 1 / max;
        Vector last = start;
        double approxLength = 0;
        for (int i = 0; i <= max; i++) {
            double t = i * seg;
            Vector calc = calculateBezierPoint(t);
            points.add(calc);
            final double distance = last.dist(calc);
            approxLength += distance;
            last = calc;
        }
        this.length = approxLength;
        int segments = (int) (length / dist) ;
        last = start;
        double t = 0.5f;
        double k = 0.5f;
        double tmin = 0;
        this.points.clear();
//        System.out.println("length " + length);
//        System.out.println("segments " + segments);
        double accDist = 0;
        for (int i = 0; i < segments; i++) {
//            System.out.println("######################################");
//            System.out.println("segment " + i);
//            System.out.println("######################################");
//            System.out.println("Start with t= " + t + " k = " + k);
            Vector test = calculateBezierPoint(t);
            double dist1 = last.dist(test);
//            System.out.println("dist = " + dist1);
            while (Math.abs(dist1 - dist) > .2 && k != 0) {
                k = k / 2;
                if (dist1 > dist) {
                    while ((t - k) < tmin) {
                        k = k / 2;
//                        System.out.println("########");
//                        System.out.println("(t - k)" + (t - k) + " tMin " + tmin + " k " + k);
                    }
                    t = t - k;
                } else {
                    while ((t + k) >= 1) {
                        k = k / 2;
//                        System.out.println("########");
//                        System.out.println("(t + k)" + (t + k) + " k " + k);
                    }
                    t = t + k;
                }
//                System.out.println("t: " + t + "\t\t k = " + k + " d=" + dist1);
                test = calculateBezierPoint(t);
                dist1 = last.dist(test);
                
            }
            accDist += dist1;
            this.points.add(test);
            last = test;
            tmin = t;
            t = tmin + ((1 - tmin) / 2);
            k = (1 - tmin) / 2;
        }
        this.points.add(end);
    }
    
    public List<Vector> calculateSatinStitch(double l) {
        ArrayList<Vector> result = new ArrayList<Vector>();
        Vector curr = start;
        int toggle = 1;
        for (Vector point : points) {
            double lambda = l / curr.dist(point);
            double x1 = 0.5 *(curr.getX()+point.getX());
            double y1 = 0.5 *(curr.getY()+point.getY());
            
            
            double x2 = point.getX();
            double y2 = point.getY();
            double x3 = x1 + (toggle *lambda * -(y2 - y1) );
            double y3 = y1 + (toggle *lambda * (x2 - x1) );
//            result.add(curr);
            result.add(new Vector(x3, y3));
            curr = point;       
            toggle *=-1;
        }
        return result;
    }
    
    public List<Vector> getPoints() {
        return points;
    }
    
    public Vector getStart() {
        return start;
    }
    
    public Vector getEnd() {
        return end;
    }
    
    public Vector getControl1() {
        return control1;
    }
    
    public Vector getControl2() {
        return control2;
    }
    
    private final Vector calculateBezierPoint(double t) {
        
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;
        double x = (uuu * start.getX()) + (3 * uu * t * control1.getX()) + (3 * u * tt * control2.getX()) + (ttt * end.getX());
        double y = (uuu * start.getY()) + (3 * uu * t * control1.getY()) + (3 * u * tt * control2.getY()) + (ttt * end.getY());
        return new Vector(x, y);
    }
    
}
