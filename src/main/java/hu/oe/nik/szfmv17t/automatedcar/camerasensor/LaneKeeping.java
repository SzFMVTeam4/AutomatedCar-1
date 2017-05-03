package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import hu.oe.nik.szfmv17t.automatedcar.SystemComponent;
import hu.oe.nik.szfmv17t.automatedcar.bus.Signal;
import hu.oe.nik.szfmv17t.environment.domain.Road;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorld;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.utils.Resizer;
import hu.oe.nik.szfmv17t.environment.utils.Vector2d;
import hu.oe.nik.szfmv17t.visualisation.DebugGraphics;
import hu.oe.nik.szfmv17t.visualisation.IDebugDrawer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class LaneKeeping extends SystemComponent implements IDebugDrawer {
    CameraSensorController csc;

    public LaneKeeping (CameraSensorController csc) {
        this.csc = csc;
    }

    @Override
    public void drawDebugInfo(DebugGraphics dd) {
        dd.setColor(Color.red);
        double angle = Math.toRadians(-30);
        double speed = Resizer.getResizer().meterToCoordinate(5);

        Path path = getCarPath(2759, 3318, 0, speed, -angle);
        path.drawDebugInfo(dd);

        dd.setColor (Color.magenta);
        Path road = getPathFromCamera();
        road.drawDebugInfo(dd);

        dd.setColor (Color.green);
        dd.drawPoint(2600, 3300);
        dd.drawPoint(2800, 3500);
        Line l = new Line(2600, 3300, 2800, 3500);
        dd.drawPoint(2600, 3500);
        dd.drawPoint(2800, 3300);
        Line l2 = new Line (2600, 3500, 2800, 3300);
        Point.Double i = l.getIntersectionPoint(l2);
        dd.drawPoint( i.x, i.y);
        System.err.println ("Inters: " + i);
    }

    private Path getCarPath (double x, double y, double carAngleInRadian, double speedInCoordinates, double wheelAngleInRadian) {
        Path path = new Path();

        wheelAngleInRadian = -wheelAngleInRadian;
        double a = Math.toRadians(90) + carAngleInRadian;

        path.addPoint(x, y);

        for (int i = 0; i < 10; i++) {
            a += wheelAngleInRadian;
            Point.Double direction = new Point2D.Double(speedInCoordinates * Math.cos (a), -speedInCoordinates * Math.sin (a));

            double nextx = x + direction.x;
            double nexty = y + direction.y;

            x = nextx;
            y = nexty;

            path.addPoint(x, y);
        }

        return path;
    }


    private Path getPathFromCamera () {
        Path path = new Path ();
        HashMap<IWorldObject, Double> data = csc.getDataOfCameraSensor();
        List<Map.Entry<IWorldObject, Double>> sorted = getSortedRoads(data);

        for (Map.Entry<IWorldObject, Double> e : sorted) {
            IWorldObject object = e.getKey();
            sorted = getSortedRoads(data);

            path.addPoint (object.getCenterX(), object.getCenterY());
        }

        return path;
    }

    private List<Map.Entry<IWorldObject, Double>> getSortedRoads (Map<IWorldObject, Double> data) {
        List<Map.Entry<IWorldObject, Double>> sorted = new LinkedList<>();

        for (Map.Entry<IWorldObject, Double> e : data.entrySet()) {
            IWorldObject object = e.getKey();

            if (object instanceof Road) {
                sorted.add (e);
            }
        }

        sorted.sort((o1, o2) -> (int) (o1.getValue() - o2.getValue()));

        return sorted;
    }

    @Override
    public void loop () {

    }

    @Override
    public void receiveSignal (Signal s) {
        System.out.format("Signal %d %d\n", s.getId(), s.getData());
    }
}
