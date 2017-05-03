package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import java.awt.*;
import java.awt.geom.Point2D;

public class Line {
    double slope;
    double yintercept;

    public Line (Point.Double p1, Point.Double p2) {
        this(p1.x, p1.y, p2.x, p2.y);
    }

    public  Line (double x1, double y1, double x2, double y2) {
        if (x1 != x2) {
            slope = (y1 - y2) / (x1 - x2);
            yintercept = y1 - slope * x1;

        } else {
            slope = Double.POSITIVE_INFINITY;
            yintercept = x1;
        }
    }

    public Point.Double getIntersectionPoint (Line other) {
        if (this.slope == other.slope)
            return  null;

        if (Double.isInfinite(slope)) {
            double x = yintercept;
            double y = other.slope * x + other.yintercept;

            return new Point2D.Double(x, y);

        } else {
            double x = (other.yintercept - this.yintercept) / (this.slope - other.slope);
            double y = slope * x + yintercept;

            return new Point2D.Double(x, y);
        }
    }
}
