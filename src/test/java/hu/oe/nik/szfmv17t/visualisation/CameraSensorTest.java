package hu.oe.nik.szfmv17t.visualisation;

import hu.oe.nik.szfmv17t.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.utils.Resizer;
import hu.oe.nik.szfmv17t.environment.utils.SensorType;
import hu.oe.nik.szfmv17t.environment.utils.Triangle;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by winifred on 2017.04.13..
 */
public class CameraSensorTest {

    private Resizer resizer;
    private AutomatedCar car;
    //57.29(=1 rad) fokkal elforgatva, 0 rad iranybol
    private AutomatedCar car2;
    private List<IWorldObject> worldObjects;
    private CameraSensor cameraSensor;

    private double viewAngle;
    private double viewDistanceInMeter;
    private double addingOffsetDistanceInMeter;
    private double addingOffsetDistanceInCoordinates;
    private double viewDistanceInCoordinates;

    private Point center;
    private Point leftPoint;
    private Point rightPoint;

    @Before
    public void setUp() {
        resizer = Resizer.getResizer();
        car = new AutomatedCar(100, 100, 108, 240, 0d, 0, "car_1_white.png", 200d, 0d, 0d);
        car2 = new AutomatedCar(100, 100, 108, 240, 0d, 0, "car_1_white.png", 200d, 0d, 1d);
        worldObjects = new ArrayList<IWorldObject>();
        cameraSensor = new CameraSensor(car);

        viewAngle = 60;
        viewDistanceInMeter = 110;
        addingOffsetDistanceInMeter = cameraSensor.calculateOffsetDistance();

        addingOffsetDistanceInCoordinates = resizer.meterToCoordinate(addingOffsetDistanceInMeter);
        viewDistanceInCoordinates = resizer.meterToCoordinate(viewDistanceInMeter);

        center = new Point((int) car.getCenterX(), (int) car.getCenterY());
    }

    @Test
    public void calculateOffsetDistanceTest() {
        double result = cameraSensor.calculateOffsetDistance();
        assertTrue(addingOffsetDistanceInMeter == result);
    }

    @Test
    public void calculateCenterPointTest() {
        Point resultPoint = cameraSensor.calculateCenterPoint(car);
        assertEquals(center, resultPoint);
    }

    @Test
    public void calculateLeftCornerPointTest() {
        double leftUpperXBase = center.getX() - addingOffsetDistanceInCoordinates;
        double leftUpperYBase = center.getY() - viewDistanceInCoordinates;

        double leftUpperCornerX = (((leftUpperXBase - center.getX()) * Math.cos(car.getDirectionAngle())) - ((leftUpperYBase - center.getY()) * Math.sin(car.getDirectionAngle()))) + center.getX();
        double leftUpperCornerY = (((leftUpperXBase - center.getX()) * Math.sin(car.getDirectionAngle())) + (leftUpperYBase - center.getY()) * Math.cos(car.getDirectionAngle())) + center.getY();
        leftPoint = new Point((int) leftUpperCornerX, (int) leftUpperCornerY);

        Point resultPoint = cameraSensor.calculateLeftCornerPoint(car, center);
        assertEquals(leftPoint, resultPoint);
    }

    @Test
    public void calculateRightCornerPointTest() {
        double rightUpperXBase = center.getX() + addingOffsetDistanceInCoordinates;
        double rightUpperYBase = center.getY() - viewDistanceInCoordinates;

        double rightUpperCornerX = (((rightUpperXBase - center.getX()) * Math.cos(car.getDirectionAngle())) - ((rightUpperYBase - center.getY()) * Math.sin(car.getDirectionAngle()))) + center.getX();
        double rightUpperCornerY = (((rightUpperXBase - center.getX()) * Math.sin(car.getDirectionAngle())) + (rightUpperYBase - center.getY()) * Math.cos(car.getDirectionAngle())) + center.getY();
        rightPoint = new Point((int) rightUpperCornerX, (int) rightUpperCornerY);

        Point resultPoint = cameraSensor.calculateRightCornerPoint(car, center);
        assertEquals(rightPoint, resultPoint);
    }

    @Test
    public void calculateLeftCornerPointTest2() {
        //1 radiannal valo elforgatas bal pontja
        Point car2Center = cameraSensor.calculateCenterPoint(car2);

        double leftUpperXBase = car2Center.getX() - addingOffsetDistanceInCoordinates;
        double leftUpperYBase = car2Center.getY() - viewDistanceInCoordinates;

        double leftUpperCornerX = (((leftUpperXBase - car2Center.getX()) * Math.cos(car2.getDirectionAngle())) - ((leftUpperYBase - car2Center.getY()) * Math.sin(car2.getDirectionAngle()))) + car2Center.getX();
        double leftUpperCornerY = (((leftUpperXBase - car2Center.getX()) * Math.sin(car2.getDirectionAngle())) + (leftUpperYBase - car2Center.getY()) * Math.cos(car2.getDirectionAngle())) + car2Center.getY();

        leftPoint = new Point((int) leftUpperCornerX, (int) leftUpperCornerY);

        Point resultPoint = cameraSensor.calculateLeftCornerPoint(car2, car2Center);
        assertEquals(leftPoint, resultPoint);
    }

    @Test
    public void calculateRightCornerPointTest2() {
        //1 radiannal valo elforgatas jobb pontja
        Point car2Center = cameraSensor.calculateCenterPoint(car2);

        double rightUpperXBase = car2Center.getX() + addingOffsetDistanceInCoordinates;
        double rightUpperYBase = car2Center.getY() - viewDistanceInCoordinates;

        double rightUpperCornerX = (((rightUpperXBase - car2Center.getX()) * Math.cos(car2.getDirectionAngle())) - ((rightUpperYBase - car2Center.getY()) * Math.sin(car2.getDirectionAngle()))) + car2Center.getX();
        double rightUpperCornerY = (((rightUpperXBase - car2Center.getX()) * Math.sin(car2.getDirectionAngle())) + (rightUpperYBase - car2Center.getY()) * Math.cos(car2.getDirectionAngle())) + car2Center.getY();

        rightPoint = new Point((int) rightUpperCornerX, (int) rightUpperCornerY);
        Point result = cameraSensor.calculateRightCornerPoint(car, car2Center);

    }


    public void calculateLeftCornerPointTest3() {
        /*1 radiannal valo elforgatas bal pontja
        ez a bal sarokpont ranezesre talan megegyezik, az auto iranyaval, hogyha
        a Main-ben az axis angle=1d-re van allitva
        ha futtatod debugolva, mert fail test
        */
        Point car2Center = cameraSensor.calculateCenterPoint(car2);

        double leftUpperXBase = car2Center.getX() - addingOffsetDistanceInCoordinates;
        double leftUpperYBase = car2Center.getY() - viewDistanceInCoordinates;

        double rotationAngleInRad = Math.toRadians(360) - car2.getDirectionAngle();

        double leftUpperCornerX = (((leftUpperXBase - car2Center.getX()) * Math.cos(rotationAngleInRad)) - ((leftUpperYBase - car2Center.getY()) * Math.sin(rotationAngleInRad))) + car2Center.getX();
        double leftUpperCornerY = (((leftUpperXBase - car2Center.getX()) * Math.sin(rotationAngleInRad)) + (leftUpperYBase - car2Center.getY()) * Math.cos(rotationAngleInRad)) + car2Center.getY();

        leftPoint = new Point((int) leftUpperCornerX, (int) leftUpperCornerY);

        Point resultPoint = cameraSensor.calculateLeftCornerPoint(car2, car2Center);
        assertEquals(leftPoint, resultPoint);
    }
}
