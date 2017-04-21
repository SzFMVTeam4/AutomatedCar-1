package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import hu.oe.nik.szfmv17t.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv17t.environment.domain.Road;
import hu.oe.nik.szfmv17t.environment.domain.Sign;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.utils.Resizer;
import hu.oe.nik.szfmv17t.environment.utils.SensorType;
import hu.oe.nik.szfmv17t.environment.utils.Triangle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by winifred on 2017.04.10..
 */
public class CameraSensor {

    private Resizer resizer;

    private double viewAngle;
    private double viewDistanceInMeter;
    private double viewDistanceInCoordinates;

    //latoter 3 pontja
    private Point centerPoint;
    private Point leftPoint;
    private Point rightPoint;

    //latomezo bal es jobb sarkanak center ponttol valo tavolsaga
    private double addingOffsetDistanceInMeter;
    private double addingOffsetDistanceInCoordinates;

    //latoter kirajzolasahoz
    public static int[] fieldViewCoordsX;
    public static int[] fieldViewCoordsY;
    public static double carDirectionAngle;

    public CameraSensor(AutomatedCar carObject) {
        resizer = Resizer.getResizer();
        this.viewAngle = 60;
        this.viewDistanceInMeter = 110;

        viewDistanceInCoordinates = resizer.meterToCoordinate(viewDistanceInMeter);
        addingOffsetDistanceInMeter = calculateOffsetDistance();
        addingOffsetDistanceInCoordinates = resizer.meterToCoordinate(addingOffsetDistanceInMeter);
    }

    public Triangle getSensorFieldView(AutomatedCar car) {
        centerPoint = calculateCenterPoint(car);
        leftPoint = calculateLeftCornerPoint(car, centerPoint);
        rightPoint = calculateRightCornerPoint(car, centerPoint);

        fieldViewCoordsX = collectCoordinatesForDrawing();
        fieldViewCoordsY = collectCoordinatesY();
        carDirectionAngle = car.getDirectionAngle();
        return new Triangle(leftPoint, rightPoint, centerPoint, SensorType.Camera);
    }

    Point calculateLeftCornerPoint(AutomatedCar car, Point center) {
        double leftUpperCornerBaseX = center.getX() - addingOffsetDistanceInCoordinates;
        double leftUpperCornerBaseY = center.getY() - viewDistanceInCoordinates;
        double[] coordinates = {leftUpperCornerBaseX, leftUpperCornerBaseY};
        double angleOfRotationInDeg = (360 - Math.toDegrees(car.getDirectionAngle()));

        AffineTransform.getRotateInstance(Math.toRadians(angleOfRotationInDeg), center.getX(), center.getY()).transform(coordinates, 0, coordinates, 0, 1);
        double leftUpperCornerX = coordinates[0];
        double leftUpperCornerY = coordinates[1];

        return new Point((int) leftUpperCornerX, (int) leftUpperCornerY);
    }

    Point calculateRightCornerPoint(AutomatedCar car, Point center) {
        double rightUpperCornerBaseX = center.getX() + addingOffsetDistanceInCoordinates;
        double rightUpperCornerBaseY = center.getY() - viewDistanceInCoordinates;

        double[] coordinates = {rightUpperCornerBaseX, rightUpperCornerBaseY};
        double angleOfRotationInDeg = (360 - Math.toDegrees(car.getDirectionAngle()));

        AffineTransform.getRotateInstance(Math.toRadians(angleOfRotationInDeg), center.getX(), center.getY()).transform(coordinates, 0, coordinates, 0, 1);
        double rightUpperCornerX = coordinates[0];
        double rightUpperCornerY = coordinates[1];
        return new Point((int) rightUpperCornerX, (int) rightUpperCornerY);
    }

    Point calculateCenterPoint(AutomatedCar car) {
        return new Point((int) car.getCenterX(), (int) car.getCenterY());
    }

    double calculateOffsetDistance() {
        double viewAngleInRadian = Math.toRadians(viewAngle / 2);
        double baseOfTriangle = Math.tan(viewAngleInRadian) * viewDistanceInMeter;
        return baseOfTriangle;
    }

    double carDistanceFromObjectInMeter(double distanceInCoordinate) {
        double distanceInMeter = resizer.coordinateToMeter(distanceInCoordinate);
        return distanceInMeter;
    }

    double carDistanceFromObjectInCoordinate(AutomatedCar carObject, IWorldObject worldObject) {
        double carUpMiddleX = carObject.getCenterX();
        double carUpMiddleY = carObject.getCenterY() - (carObject.getHeight() / 2);

        double woLowerMiddleX = worldObject.getCenterX();
        double woLowerMiddleY = worldObject.getCenterY() + (worldObject.getHeight() / 2);

        double distanceInCoordinate = Math.abs(Math.sqrt(Math.pow((woLowerMiddleX - carUpMiddleX), 2) + Math.pow((woLowerMiddleY - carUpMiddleY), 2)));
        return distanceInCoordinate;
    }

    List<IWorldObject> getRelevantWorldObjects(List<IWorldObject> worldObjects) {
        List<IWorldObject> result = new ArrayList<IWorldObject>();

        for (IWorldObject element : worldObjects) {
            if (Road.class.isInstance(element) || Sign.class.isInstance(element)) {
                result.add(element);
            }
        }
        return result;
    }

    private int[] collectCoordinatesForDrawing() {
        //kirajzolashoz, Drawerben
        int[] xCoordinatesForDrawing = new int[3];
        xCoordinatesForDrawing[0] = (int) centerPoint.getX();
        xCoordinatesForDrawing[1] = (int) leftPoint.getX();
        xCoordinatesForDrawing[2] = (int) rightPoint.getX();
        return xCoordinatesForDrawing;
    }

    private int[] collectCoordinatesY() {
        //kirajzolashoz, Drawerben
        int[] yCoordinatesForDrawing = new int[3];
        yCoordinatesForDrawing[0] = (int) centerPoint.getY();
        yCoordinatesForDrawing[1] = (int) leftPoint.getY();
        yCoordinatesForDrawing[2] = (int) rightPoint.getY();
        return yCoordinatesForDrawing;
    }
}
