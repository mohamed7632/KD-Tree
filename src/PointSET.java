import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> arr;


    // construct an empty set of points
    public PointSET() {
        arr = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return arr.isEmpty();
    }

    // number of points in the set
    public int size() {
        return arr.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!contains(p))
            arr.add(p);

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return (arr.contains(p));
    }

    // draw all points to standard draw
    public void draw() {

        for (Point2D p : arr) {
            p.draw();
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<>();
        for (Point2D p : arr) {
            if (rect.contains(p))
                points.add(p);

        }
        return points;

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (size() == 0)
            return null;
        double minDistance = Integer.MAX_VALUE;
        Point2D minPoint = new Point2D(0, 0);
        for (Point2D x : arr) {
            if (minDistance > p.distanceSquaredTo(x)) {
                minDistance = p.distanceSquaredTo(x);
                minPoint = x;
            }

        }
        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        //UncommentedEmptyMethodBody

    }
}
