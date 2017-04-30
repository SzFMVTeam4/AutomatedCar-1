package hu.oe.nik.szfmv17t.visualisation;

import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.utils.Config;

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

    public void drawPoint (int x, int y) {
        Point p = getCanvasPoint(x, y);
        g.drawOval(p.x - radius / 2, p.y - radius / 2, radius, radius);
    }

    public void drawPoint (double x, double y) {
        drawPoint((int)x, (int)y);
    }

    public void drawLine (int x1, int y1, int x2, int y2) {
        Point p1 = getCanvasPoint(x1, y1);
        Point p2 = getCanvasPoint(x2, y2);

        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    private Point getCanvasPoint (int x, int y) {
        double x1 = camera.getX() * Config.SCALE - car.getCenterX() + x;
        double y1 = camera.getY() * Config.SCALE - car.getCenterY() + y;

        int x2 = (int) (x1 / Config.SCALE);
        int y2 = (int) (y1 / Config.SCALE);

        return new Point(x2, y2);
    }
}
