package util;

import logicClasses.Point;
import logicClasses.Flight;

public class Calculations {

    // STATIC METHODS

    public static double distanceBetween(Point a, Point b) {
        return Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
    }

    public static double distanceBetween(Flight a, Flight b) {
        return Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
    }

}
