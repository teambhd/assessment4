package logicClasses;

import java.lang.Math;


public class Point {

    /**
	 * @uml.property  name="x"
	 */
    protected double x;
    /**
	 * @uml.property  name="y"
	 */
    protected double y;
    /**
	 * @uml.property  name="pointRef"
	 */
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
        this.x = x;
        this.y = y;
        this.pointRef = pointRef;
    }


    // MUTATORS AND ACCESSORS

    /**
	 * @return
	 * @uml.property  name="x"
	 */
    public double getX() {
        return x;
    }

    /**
	 * @return
	 * @uml.property  name="y"
	 */
    public double getY() {
        return y;
    }

    /**
	 * @param x
	 * @uml.property  name="x"
	 */
    public void setX(double x) {
        this.x = x;
    }

    /**
	 * @param y
	 * @uml.property  name="y"
	 */
    public void setY(double y) {
        this.y = y;
    }

    /**
	 * @return
	 * @uml.property  name="pointRef"
	 */
    public String getPointRef() {
        return pointRef;
    }

    public boolean equals(Point point) {
        return point.getX() == x && point.getY() == y;
    }

}
