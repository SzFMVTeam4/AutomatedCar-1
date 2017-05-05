package hu.oe.nik.szfmv17t.visualisation;

import hu.oe.nik.szfmv17t.automatedcar.camerasensor.CameraSensorController;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by winifred on 2017.05.03..
 */
public class SignPanel extends JPanel {

    private static CameraSensorController cameraSensorController;
    private IWorldObject iWorldObject;
    private BufferedImage bufferedImage;

    public static void setCameraSensorController(CameraSensorController cameraSensorController) {
        SignPanel.cameraSensorController = cameraSensorController;
    }

    public static CameraSensorController getCameraSensorController() {
        return cameraSensorController;
    }

    public SignPanel() {
        this.setPreferredSize(new Dimension(40, 50));
        setBackground(Color.blue);
        this.iWorldObject = cameraSensorController.getClosestSign();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        iWorldObject = cameraSensorController.getClosestSign();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (iWorldObject != null) {
            try {
                this.bufferedImage = ImageIO.read(new File(ClassLoader.getSystemResource(iWorldObject.getImageName()).getFile()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //int x = (getWidth() - bufferedImage.getWidth()) / 2;
            // int y = (getHeight() - bufferedImage.getHeight()) / 2;
            int width = (int) (bufferedImage.getWidth() / 2);
            int height = (int) (bufferedImage.getHeight() / 2);

            g.drawImage(bufferedImage, 0, 0, width, height, this);
        }
    }

}
