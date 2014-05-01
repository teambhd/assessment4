package logicClasses;

import java.lang.Math;


public class Point {

    protected double x;
    protected double y;
    protected String pointRef;

    // CONSTRUCTORS

    // Point Constructor taking two doubles for X then Y coordinates.
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.pointRef = "-";
    }

    // Point Constructor that also takes pointRef string, more commonly used.
    public Point(double x, double y, String pointRef) {
        this.x = xcoord;
        this.y = ycoord;
        this.pointRef = pointRef;
    }


    // MUTATORS AND ACCESSORS

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getPointRef() {
        return pointRef;
    }

    public boolean equals(Point point) {
        return point.getX() == x && point.getY() == y;
    }

}
