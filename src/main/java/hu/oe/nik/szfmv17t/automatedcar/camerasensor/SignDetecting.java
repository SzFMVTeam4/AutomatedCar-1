package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import hu.oe.nik.szfmv17t.environment.domain.Sign;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by winifred on 2017.04.26..
 */
public class SignDetecting {


    private HashMap<IWorldObject, Double> cameraSensorStoredData;

    public SignDetecting(HashMap<IWorldObject, Double> cameraSensorStoredData) {
        this.cameraSensorStoredData = cameraSensorStoredData;
    }

    //Levalogatja a latoterbol a tablakat
    HashMap<IWorldObject, Double> searchSigns(HashMap<IWorldObject, Double> cameraSensorStoredData) {
        HashMap<IWorldObject, Double> result = new HashMap<IWorldObject, Double>();

        for (Map.Entry<IWorldObject, Double> entry : cameraSensorStoredData.entrySet()) {
            if (Sign.class.isInstance(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    //A legkozelebbi objektumot adja vissza
    IWorldObject findClosestSign(HashMap<IWorldObject, Double> signObjects) {
        IWorldObject result = null;
        Double closestDistance = 0.0;
        boolean first = true;

        for (Map.Entry<IWorldObject, Double> actual : signObjects.entrySet()) {
            if (first) {
                closestDistance = actual.getValue();
                result = actual.getKey();
                first = false;
            }

            if (actual.getValue() < closestDistance) {
                closestDistance = actual.getValue();
                result = actual.getKey();
            }
        }
        if (result != null) {
            System.out.println("Closest Object: " + result.getImageName() + " X:" + result.getCenterX() + " Y:" + result.getCenterY());
        }
        return result;
    }

    //A legkozelebbi tabla erteket adja vissza
    double getValueOfSign(IWorldObject worldObject) {
        double result = 0.0;
        if (worldObject != null) {
            try {
                if (worldObject.getImageName() == "roadsign_priority_stop.png" || worldObject.getImageName() == "roadsign_parking_right.png") {
                    return result;
                }
                //ha az uj elemek bekerulnek a parserbe, akkor lehet ezek osztalya nem Sign lesz, igy torolni kell
                if (worldObject.getImageName() == "bollard.png" || worldObject.getImageName() == "boundary.png" || worldObject.getImageName() == "garage.png") {
                    return result;
                } else {
                    String valueOfSign = readNameOfSign(worldObject.getImageName());
                    result = Double.parseDouble(valueOfSign);

                    System.out.println("Value of sign:" + result);
                }
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    private String readNameOfSign(String imageName) {
        String[] pieces = imageName.split("[_.]");
        String result = pieces[2];
        return result;
    }

}
