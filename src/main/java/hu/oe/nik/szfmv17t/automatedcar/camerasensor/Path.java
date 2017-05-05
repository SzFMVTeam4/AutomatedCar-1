package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import hu.oe.nik.szfmv17t.visualisation.DebugGraphics;
import hu.oe.nik.szfmv17t.visualisation.IDebugDrawer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Path implements IDebugDrawer {
    private ArrayList<Point.Double> points;

    public Path () {
        points = new ArrayList<>();
    }

    public void addPoint (double x, double y) {
        points.add(new Point2D.Double(x, y));
    }

    public Point.Double getIntersectionPoint (Line line) {
        if (points.size() < 2)
            return  null;

        Point.Double prev = points.get (0);

        for (int i = 1; i < points.size(); i++) {
            Point.Double p = points.get(i);
            Line l = new Line(prev, p);

            Point.Double intersection = l.getIntersectionPoint(line);

            if (checkPointInSegment(intersection, p, prev)) {
                return intersection;
            }

            prev = p;
        }

        return null;
    }

    private boolean checkPointInSegment (Point.Double point, Point2D.Double segment1, Point2D.Double segment2) {
        double minx = Math.min(segment1.x, segment2.x);
        double maxx = Math.max(segment1.x, segment2.x);
        double miny = Math.min(segment1.y, segment2.y);
        double maxy = Math.max(segment1.y, segment2.y);

        return point.x >= minx && point.x <= maxx && point.y >= miny && point.y <= maxy;
    }

    public void drawDebugInfo (DebugGraphics dd) {
        if (points.size() > 0) {
            Point.Double prev = points.get(0);
            dd.drawPoint (prev);

            for (int i = 1; i < points.size(); i++) {
                Point.Double p = points.get(i);

                dd.drawPoint(p);
                dd.drawLine(prev, p);

                prev = p;
            }
        }
    }
}
