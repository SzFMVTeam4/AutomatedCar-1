package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.visualisation.DebugGraphics;
import hu.oe.nik.szfmv17t.visualisation.IDebugDrawer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by winifred on 2017.04.03..
 */
public class LaneKeeping implements IDebugDrawer {
    CameraSensorController csc;

    public LaneKeeping (CameraSensorController csc) {
        this.csc = csc;
    }

    @Override
    public void drawDebugInfo(DebugGraphics dd) {
        dd.setColor(Color.red);
        dd.drawPoint(0, 0);
        dd.drawPoint(470, 766);

        dd.setColor(Color.green);
        dd.drawLine(0, 0, 470, 766);

        dd.setColor(Color.magenta);
        HashMap<IWorldObject, Double> data = csc.getDataOfCameraSensor();
        for (Map.Entry<IWorldObject, Double> e : data.entrySet()) {
            IWorldObject object = e.getKey();
            dd.drawPoint(object.getCenterX(), object.getCenterY());
        }
    }
}
