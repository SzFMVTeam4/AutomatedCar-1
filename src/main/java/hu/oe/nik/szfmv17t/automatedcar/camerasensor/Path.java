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

    /*
    public Point.Double getIntersectionPoint (Line line) {
        Point.Double
    }
    */

    public void drawDebugInfo (DebugGraphics dd) {
        if (points.size() > 0) {
            Point.Double first = points.get(0);
            dd.drawPoint(first.x, first.y);

            for (int i = 1; i < points.size(); i++) {
                Point.Double prev = points.get(i - 1);
                Point.Double p = points.get(i);

                dd.drawPoint(p.x, p.y);
                dd.drawLine(prev.x, prev.y, p.x, p.y);
            }
        }
    }
}
