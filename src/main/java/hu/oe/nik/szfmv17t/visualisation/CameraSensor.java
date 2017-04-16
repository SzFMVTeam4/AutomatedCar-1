package hu.oe.nik.szfmv17t.visualisation;

import hu.oe.nik.szfmv17t.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv17t.environment.domain.Road;
import hu.oe.nik.szfmv17t.environment.domain.Sign;
import hu.oe.nik.szfmv17t.environment.domain.World;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.utils.Resizer;
import hu.oe.nik.szfmv17t.environment.utils.SensorType;
import hu.oe.nik.szfmv17t.environment.utils.Triangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by winifred on 2017.04.10..
 */
public class CameraSensor {

    private Resizer resizer;
    private World world;

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

    //3 pont altal meghatarozott latoter
    Triangle fieldView;
    private List<IWorldObject> worldObjects;
    //relevans objektumok
    private List<IWorldObject> relevantWorldObjects;

    public CameraSensor(AutomatedCar carObject) {
        resizer = Resizer.getResizer();

        this.viewAngle = 60;
        this.viewDistanceInMeter = 110;

        viewDistanceInCoordinates = resizer.meterToCoordinate(viewDistanceInMeter);
        addingOffsetDistanceInMeter = calculateOffsetDistance();
        addingOffsetDistanceInCoordinates = resizer.meterToCoordinate(addingOffsetDistanceInMeter);

        fieldView = getSensorFieldView(carObject);
        // worldObjects = world.checkSensorArea(fieldView);
//        relevantWorldObjects = getRelevantWorldObjects(worldObjects);
    }

    public Triangle getSensorFieldView(AutomatedCar car) {
        centerPoint = calculateCenterPoint(car);
        leftPoint = calculateLeftCornerPoint(car, centerPoint);
        rightPoint = calculateRightCornerPoint(car, centerPoint);
        return new Triangle(leftPoint, rightPoint, centerPoint, SensorType.Camera);
    }

    Point calculateCenterPoint(AutomatedCar car) {
        return new Point((int) car.getCenterX(), (int) car.getCenterY());
    }

    Point calculateLeftCornerPoint(AutomatedCar car, Point center) {
        double leftUpperXBase = center.getX() - addingOffsetDistanceInCoordinates;
        double leftUpperYBase = center.getY() - viewDistanceInCoordinates;

        double leftUpperCornerX = (((leftUpperXBase - center.getX()) * Math.cos(car.getDirectionAngle())) - ((leftUpperYBase - center.getY()) * Math.sin(car.getDirectionAngle()))) + center.getX();
        double leftUpperCornerY = (((leftUpperXBase - center.getX()) * Math.sin(car.getDirectionAngle())) + (leftUpperYBase - center.getY()) * Math.cos(car.getDirectionAngle())) + center.getY();

        return new Point((int) leftUpperCornerX, (int) leftUpperCornerY);
    }

    Point calculateRightCornerPoint(AutomatedCar car, Point center) {
        double rightUpperXBase = center.getX() + addingOffsetDistanceInCoordinates;
        double rightUpperYBase = center.getY() - viewDistanceInCoordinates;

        double rightUpperCornerX = (((rightUpperXBase - center.getX()) * Math.cos(car.getDirectionAngle())) - ((rightUpperYBase - center.getY()) * Math.sin(car.getDirectionAngle()))) + center.getX();
        double rightUpperCornerY = (((rightUpperXBase - center.getX()) * Math.sin(car.getDirectionAngle())) + (rightUpperYBase - center.getY()) * Math.cos(car.getDirectionAngle())) + center.getY();

        return new Point((int) rightUpperCornerX, (int) rightUpperCornerY);
    }

    double calculateOffsetDistance() {
        double viewAngleInRadian = Math.toRadians(viewAngle / 2);
        double baseOfTriangle = Math.tan(viewAngleInRadian) * viewDistanceInMeter;
        return baseOfTriangle;
    }

    double carDistanceFromObject(AutomatedCar carObject, IWorldObject worldObject) {
        double carUpMiddleX = carObject.getCenterX();
        double carUpMiddleY = carObject.getCenterY() - (carObject.getHeight() / 2);

        double woLowerMiddleX = worldObject.getCenterX();
        double woLowerMiddleY = worldObject.getCenterY() + (worldObject.getHeight() / 2);
//abszolutertek kell vagy sem??
        double distance = Math.abs(Math.sqrt(Math.pow((woLowerMiddleX - carUpMiddleX), 2) + Math.pow((woLowerMiddleY - carUpMiddleY), 2)));

        return distance;
    }

    List<IWorldObject> getRelevantWorldObjects(List<IWorldObject> worldObjects) {
        worldObjects = world.checkSensorArea(fieldView);
        List<IWorldObject> result = new ArrayList<IWorldObject>();

        for (IWorldObject element : worldObjects) {
            if (Road.class.isInstance(element) || Sign.class.isInstance(element)) {
                result.add(element);
            }
        }
        return result;
    }
}