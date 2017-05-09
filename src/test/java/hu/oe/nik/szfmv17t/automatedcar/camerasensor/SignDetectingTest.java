package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import hu.oe.nik.szfmv17t.environment.domain.Road;
import hu.oe.nik.szfmv17t.environment.domain.Sign;
import hu.oe.nik.szfmv17t.environment.domain.Turn;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by winifred on 2017.05.03..
 */
public class SignDetectingTest {

    private SignDetecting signDetecting;
    private IWorldObject road1;
    private IWorldObject road2;
    private IWorldObject turn1;
    private IWorldObject sign1;
    private IWorldObject sign2;
    private IWorldObject sign3;
    private HashMap<IWorldObject, Double> cameraSensorStoredData;


    @Before
    public void setUp() {
        cameraSensorStoredData = new HashMap<IWorldObject, Double>();
        init();
        signDetecting = new SignDetecting(cameraSensorStoredData);
    }


    private void init() {
        road1 = new Road(0, 0, 1, 1, 0, 0, "road_2lane_straight.png", 0, 1, 1, 1);
        road2 = new Road(10, 10, 1, 1, 0, 0, "road_2lane_straight.png", 0, 1, 1, 1);
        turn1 = new Turn(0, 2, 1, 1, 0d, 0, "road_2lane_45left.png", 0, 1, 1, 1);
        sign1 = new Sign(0, 1, 1, 1, 0d, 0, "roadsign_speed_40.png", 0, 0, 0d);
        sign2 = new Sign(0, 3, 1, 1, 0d, 0, "roadsign_speed_50.png", 0, 0, 0d);
        sign3 = new Sign(0, 1, 1, 1, 0d, 0, "roadsign_parking_right.png", 0, 0, 0d);

        cameraSensorStoredData.put(road1, 0.0);
        cameraSensorStoredData.put(road2, 10.0);
        cameraSensorStoredData.put(turn1, 2.0);
        cameraSensorStoredData.put(sign1, 1.0);
        cameraSensorStoredData.put(sign2, 3.0);
        cameraSensorStoredData.put(sign3, 1.0);
    }

    @Test
    public void searchSignTest() {
        HashMap<IWorldObject, Double> expected = new HashMap<IWorldObject, Double>();
        expected.put(sign1, 1.0);
        expected.put(sign2, 3.0);
        expected.put(sign3, 1.0);
        HashMap<IWorldObject, Double> result = signDetecting.searchSigns(cameraSensorStoredData);
        assertEquals(expected, result);
    }

    @Test
    public void closestSignTest() {
        HashMap<IWorldObject, Double> signObjects = new HashMap<IWorldObject, Double>();
        signObjects.put(sign1, 1.0);
        signObjects.put(sign2, 3.0);

        IWorldObject expected = sign1;
        IWorldObject result = signDetecting.findClosestSign(signObjects);
        assertEquals(expected, result);
    }

    @Test
    public void getValueOfSignTest() {
        double expected = 40.0;
        double result = signDetecting.getValueOfSign(sign1);
        assertEquals(expected, result, 0);
    }

    @Test
    public void getValueOfSignTest2() {
        double expected = 0.0;
        double result = signDetecting.getValueOfSign(sign3);
        assertEquals(expected, result, 0);
    }
}
