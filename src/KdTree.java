import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;


public class KdTree {
    private Node root;


    private static class Node {
        private final Point2D point;
        private final RectHV rect;
        private Node left;
        private Node right;
        private int size;
        private final int level;


        public Node(Point2D point, RectHV rect, int size, int level) {
            this.point = point;
            this.rect = rect;
            this.level = level;
            this.size = size;

        }


    }

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.size;
    }


    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point2D p is not illegal!");
        if (root == null)
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0), 1, 0);
        else
            insert(root, p);
    }


    // add the point to the set (if it is not already in the set)
    private void insert(Node x, Point2D p) {
        if (contains(x, p))
            return;
        if (x.level % 2 == 0) {
            int comp = Double.compare(p.x(), x.point.x());
            if (comp == -1) {

                if (x.left != null)
                    insert(x.left, p);
                else {
                    RectHV parent = x.rect;
                    double newXmin = parent.xmin();
                    double newYmin = parent.ymin();
                    double newXmax = x.point.x();
                    double newYmax = parent.ymax();

                    x.left = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax), 1, x.level + 1);
                }

            } else {
                if (x.right != null)
                    insert(x.right, p);
                else {
                    RectHV parent = x.rect;
                    double newXmin = x.point.x();
                    double newYmin = parent.ymin();
                    double newXmax = parent.xmax();
                    double newYmax = parent.ymax();
                    x.right = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax), 1, x.level + 1);
                }
            }
        } else {

            int comp = Double.compare(p.y(), x.point.y());
            if (comp == -1) {
                if (x.left != null)
                    insert(x.left, p);
                else {
                    RectHV parent = x.rect;
                    double newXmin = parent.xmin();
                    double newYmin = parent.ymin();
                    double newXmax = parent.xmax();
                    double newYmax = x.point.y();
                    x.left = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax), 1, x.level + 1);
                }

            } else {
                if (x.right != null)
                    insert(x.right, p);
                else {
                    RectHV parent = x.rect;
                    double newXmin = parent.xmin();
                    double newYmin = x.point.y();
                    double newXmax = parent.xmax();
                    double newYmax = parent.ymax();
                    x.right = new Node(p, new RectHV(newXmin, newYmin, newXmax, newYmax), 1, x.level + 1);
                }
            }

        }
        x.size = 1 + size(x.left) + size(x.right);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point2D p is not illegal!");
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        
        if (x == null) return false;
        if (x.point.compareTo(p) == 0)
            return true;

        if (x.level % 2 == 0) {

            int comp = Double.compare(p.x(), x.point.x());
            if (comp == -1) {
                if (x.left != null)
                    return contains(x.left, p);
            } else {
                if (x.right != null)
                    return contains(x.right, p);

            }


        } else {
            int comp = Double.compare(p.y(), x.point.y());
            if (comp == -1) {
                if (x.left != null)
                    return contains(x.left, p);


            } else {
                if (x.right != null)
                    return contains(x.right, p);

            }

        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        draw(root);
    }

    private void draw(Node x) {
        if (x == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point.draw();
        if (x.level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            Point2D start = new Point2D(x.point.x(), x.rect.ymin());
            Point2D end = new Point2D(x.point.x(), x.rect.ymax());
            start.drawTo(end);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            Point2D start = new Point2D(x.rect.xmin(), x.point.y());
            Point2D end = new Point2D(x.rect.xmax(), x.point.y());
            start.drawTo(end);
        }
        draw(x.left);
        draw(x.right);
    }

    private Iterable<Point2D> range(Node x, RectHV rect, ArrayList<Point2D> points) {
        if (rect == null)
            throw new IllegalArgumentException();
        if (rect.intersects(x.rect)) {
            if (rect.contains(x.point))
                points.add(x.point);
            if (x.left != null)
                range(x.left, rect, points);
            if (x.right != null)
                range(x.right, rect, points);
        }
        return points;

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect.xmin() > rect.xmax() || rect.ymin() > rect.ymax())
            throw new IllegalArgumentException();
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<>();
        if (root != null)
            return range(root, rect, points);
        else
            return new ArrayList<Point2D>();


    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (root != null)
            return nearest(p, root, root.point);
        else
            return null;

    }

    private Point2D nearest(Point2D p, Node x, Point2D minPoint) {
        if (p == null)
            throw new IllegalArgumentException();
        if (x.point.equals(p))
            return x.point;
        double minDistance = minPoint.distanceSquaredTo(p);
        // check if minPoint which (intiated with root) is closer than any point in its rectangle
        if (Double.compare(x.rect.distanceSquaredTo(p), minDistance) >= 0)
            return minPoint;
        else {
            double distance = x.point.distanceSquaredTo(p);
            if (Double.compare(distance, minDistance) == -1) {
                minPoint = x.point;
                minDistance = distance;
            }
            if (x.left != null)
                minPoint = nearest(p, x.left, minPoint);
            if (x.right != null)
                minPoint = nearest(p, x.right, minPoint);
        }
        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        //UncommentedEmptyMethodBody


    }


}
