package logicClasses;

import java.lang.Math;


public class Point {

    protected double x;
    protected double y;
    protected String pointRef;

    // STATIC METHODS

    public static double distanceBetween(Point a, Point b) {
        return Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
    }

    // CONSTRUCTORS

    //Point Constructor taking two doubles for X then Y coordinates.
    public Point(double xcoord, double ycoord) {
        x = xcoord;
        y = ycoord;
        pointRef = "-";
    }

    //Point Constructor that also takes pointRef string, more commonly used.
    public Point(double xcoord, double ycoord, String name) {
        x = xcoord;
        y = ycoord;
        pointRef = name;
    }


    // MUTATORS AND ACCESSORS

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public String getPointRef() {
        return this.pointRef;
    }

    public void setPointRef(String pointRef) {
        this.pointRef = pointRef;
    }

    public boolean equals(Point point) {
        return ((point.getX() == x) && (point.getY() == y));
    }

}
