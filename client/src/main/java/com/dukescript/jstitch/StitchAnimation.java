/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.jstitch;

import com.dukescript.api.canvas.GraphicsContext2D;
import com.dukescript.jstitch.DSTModel.Stitch;

/**
 *
 * @author antonepple
 */
public class StitchAnimation {

    private int i = 0, x = 0, y = 0;
    private final GraphicsContext2D g2d;
    private final Stitch[] stitches;

    public StitchAnimation(Stitch[] stitches, GraphicsContext2D g2d) {
        this.stitches = stitches;
        this.g2d = g2d;
    }

    public boolean stitch(GraphicsContext2D g2d) {
        if (i >= stitches.length) {
            return false;
        }
        Stitch stitch = stitches[i];
        x += stitch.getIncX();
        y -= stitch.getIncY();
        System.out.format("%d (%d,%d)\n",i,x,y);
        if (stitch.isColorChange() || stitch.isJump()) {
            g2d.beginPath();
            g2d.moveTo(x, y);
            g2d.fillCircle(x, y, 1.5f);
        } else {
            g2d.lineTo(x, y);
            g2d.stroke();
            g2d.fillCircle(x, y, 1.5f);
        }
        i++;
        return true;
    }

}
