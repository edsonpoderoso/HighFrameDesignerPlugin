/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edsonmoreira.editor;

import java.awt.Point;
import java.awt.Rectangle;
import org.edsonmoreira.hfd.Dimension;
import org.edsonmoreira.hfd.Position;

/**
 *
 * @author edson
 */
public class Util {

    public static Position pointToPosition(Point p) {
        Position position = new Position();
        position.setX((long) p.getX());
        position.setY((long) p.getY());
        return position;
    }

    public static Point positionToPoint(Position p) {
        if (p.getX() <= Integer.MAX_VALUE
                && p.getX() >= Integer.MIN_VALUE
                && p.getY() <= Integer.MAX_VALUE
                && p.getY() >= Integer.MIN_VALUE
                ){
        Point point = new Point((int)p.getX(), (int)p.getY());
        return point;
        }else{
            throw new IllegalArgumentException("The position given for conversion is out of a Point range");
        }
    }
    
    public static Dimension rectangleToDimension(Rectangle r){
        Dimension d = new Dimension();
        d.setHeight((long) r.getHeight());
        d.setWidth((long) r.getWidth());
        return d;
    }
    
    public static Rectangle dimensionToRectangle(Dimension d){
        return new Rectangle((int)d.getWidth(),(int)d.getHeight());
    }
}
