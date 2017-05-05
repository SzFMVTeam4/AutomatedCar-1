package hu.oe.nik.szfmv17t.visualisation;

import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.utils.Config;
import hu.oe.nik.szfmv17t.environment.utils.Triangle;

import java.awt.*;

public class DebugGraphics {
    public static final int radius = 10;

    private Camera camera;
    private IWorldObject car;
    private Graphics2D g;

    public DebugGraphics(Camera camera, IWorldObject car, Graphics2D g) {
        this.camera = camera;
        this.car = car;
        this.g = g;
    }

    public void setColor (Color color) {
        g.setColor(color);
    }

    public void drawPoint (int x, int y, Color color) {
        g.setColor(color);
        drawPoint(x, y);
    }

    public void drawPoint (Point.Double p) {
        drawPoint(p.x, p.y);
    }

    public void drawPoint (double x, double y) {
        Point p = getCanvasPoint(x, y);
        g.drawOval(p.x - radius / 2, p.y - radius / 2, radius, radius);
    }

    public void drawLine (Point.Double p1, Point.Double p2) {
        drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public void drawLine (double x1, double y1, double x2, double y2) {
        Point p1 = getCanvasPoint(x1, y1);
        Point p2 = getCanvasPoint(x2, y2);

        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public void drawCircle (double x, double y, int radius) {
        Point p = getCanvasPoint(x, y);
        g.drawOval (p.x - radius / 2, p.y - radius / 2, radius, radius);
    }

    private Point getCanvasPoint (double x, double y) {
        double x1 = camera.getX() * Config.SCALE - car.getCenterX() + x;
        double y1 = camera.getY() * Config.SCALE - car.getCenterY() + y;

        int x2 = (int) (x1 / Config.SCALE);
        int y2 = (int) (y1 / Config.SCALE);

        return new Point(x2, y2);
    }

    Point.Double rotate_point(double cx, double cy, double angle, int x, int y) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        // translate point back to origin:
        x -= cx;
        y -= cy;

        // rotate point
        double xnew = x * c - y * s;
        double ynew = x * s + y * c;

        // translate point back:
        Point.Double p = new Point.Double ();
        p.x = xnew + cx;
        p.y = ynew + cy;
        return p;
    }
}
