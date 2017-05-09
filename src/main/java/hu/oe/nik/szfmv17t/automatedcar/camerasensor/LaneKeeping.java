package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import hu.oe.nik.szfmv17t.automatedcar.SystemComponent;
import hu.oe.nik.szfmv17t.automatedcar.bus.Signal;
import hu.oe.nik.szfmv17t.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv17t.environment.domain.Road;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.utils.Resizer;
import hu.oe.nik.szfmv17t.visualisation.DebugGraphics;
import hu.oe.nik.szfmv17t.visualisation.IDebugDrawer;

import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

import static hu.oe.nik.szfmv17t.automatedcar.powertrainsystem.PowertrainSystem.LANE_KEEPING_ID;
import static hu.oe.nik.szfmv17t.automatedcar.powertrainsystem.PowertrainSystem.LANE_KEEPING_SWITCH;
import static hu.oe.nik.szfmv17t.automatedcar.powertrainsystem.PowertrainSystem.SMI_SteeringWheel;

public class LaneKeeping extends SystemComponent implements IDebugDrawer {
    private static final double RAD90 = Math.toRadians(90);
    private CameraSensorController csc;

    private boolean enabled = true;

    private double carx = 0;
    private double cary = 0;
    private double speed = 0;
    private double carAngle = 0;
    private double wheelAngle = 0;

    private Path carPath = new Path ();
    private Path road = new Path ();

    private double pathDifference;

    public LaneKeeping (CameraSensorController csc) {
        this.csc = csc;
    }

    @Override
    public void drawDebugInfo(DebugGraphics dd) {
        dd.setColor(Color.red);
        carPath.drawDebugInfo(dd);

        dd.setColor (Color.magenta);
        road.drawDebugInfo(dd);

        dd.setColor(Color.DARK_GRAY);
    }

    private double calculatePathDifference () {
        double x = carx, y = cary;

        Point.Double direction = getCarDirection(speed, wheelAngle + carAngle + RAD90);
        double diff = 0;

        for (int i = 1; i <= 3; i++) {
            x = x + direction.x / 2;
            y = y + direction.y / 2;
            Point.Double p = new Point.Double(x, y);
            Line line = new Line(x, y, direction);

            Point.Double ppath = carPath.getIntersectionPoint(line);
            Point.Double proad = road.getIntersectionPoint(line);

            if (ppath != null && proad != null) {
                double curretnDiff = getDistance(ppath, proad);
                double dp = getDistance(p, ppath);
                double dr = getDistance(p, proad);

                if (dr < dp)
                    curretnDiff = -curretnDiff;

                diff += 1 / i * curretnDiff;
            }
        }

        return diff;
    }

    private double getDistance (Point.Double p1, Point.Double p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    private Path getCarPath (double x, double y, double carAngleInRadian, double speedInCoordinates, double wheelAngleInRadian) {
        Path path = new Path();

        wheelAngleInRadian = -wheelAngleInRadian;
        double a = Math.toRadians(90) + carAngleInRadian;
        //double a = 0;

        path.addPoint(x, y);

        for (int i = 0; i < 50; i++) {
            a += wheelAngleInRadian;

            Point.Double direction = getCarDirection(speedInCoordinates, a);

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
        path.addPoint (carx, cary);

        HashMap<IWorldObject, Double> data = csc.getDataOfCameraSensor();
        List<Map.Entry<IWorldObject, Double>> sorted = getSortedRoads(data);

        for (Map.Entry<IWorldObject, Double> e : sorted) {
            IWorldObject object = e.getKey();

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

    private Point.Double getCarDirection (double speed, double angle) {
        return new Point2D.Double(speed * Math.cos(angle), -speed * Math.sin(angle));
    }

    private double getDifference (Path path, Path road) {
        Point.Double direction = getCarDirection(speed, wheelAngle + carAngle);

        return 0;
    }

    @Override
    public void loop () {
        if (enabled) {
            carPath = getCarPath(carx, cary, carAngle, speed, wheelAngle);
            road = getPathFromCamera();

            pathDifference = calculatePathDifference();
            double correction = getCorrectionAngel (pathDifference);
            System.out.println(correction + " " + pathDifference);

            VirtualFunctionBus.sendSignal(new Signal(LANE_KEEPING_ID, correction));
        }
    }

    private double getCorrectionAngel (double pathDifference) {
        if (Math.abs(pathDifference) < 1)
            return 0;

        return Math.signum(pathDifference) * 5;
    }

    @Override
    public void receiveSignal (Signal s) {
        switch (s.getId()) {
            case SMI_SteeringWheel:
                wheelAngle = Math.toRadians(s.getData().doubleValue());
                break;
            case CameraSensorController.C_CARX:
                carx = s.getData().doubleValue();
                break;
            case CameraSensorController.C_CARY:
                cary = s.getData().doubleValue();
                break;
            case CameraSensorController.C_CARSPEED:
                speed = Resizer.getResizer().meterToCoordinate(s.getData().doubleValue());
                break;
            case CameraSensorController.C_CARANGLE:
                carAngle = s.getData().doubleValue();
                break;
            case LANE_KEEPING_SWITCH:
                enabled = s.getData().intValue() != 0;
                break;
        }
    }
}
