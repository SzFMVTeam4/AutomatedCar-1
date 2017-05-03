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

    private JLabel lbImage;

    private Label labelName;
    private Label labelValue;

    public static void setCameraSensorController(CameraSensorController cameraSensorController) {
        SignPanel.cameraSensorController = cameraSensorController;
    }

    public static CameraSensorController getCameraSensorController() {
        return cameraSensorController;
    }

    public SignPanel() {
        this.iWorldObject = getCameraSensorController().getClosestSign();

        this.labelName = new Label("Image");
        this.add(labelName);
        this.labelValue = new Label(String.valueOf(0));
        this.add(labelValue);

        if (iWorldObject != null) {
            try {
                this.bufferedImage = ImageIO.read(new File(ClassLoader.getSystemResource(iWorldObject.getImageName()).getFile()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.lbImage = new JLabel(new ImageIcon(bufferedImage));
            this.add(lbImage);
            lbImage.setIcon((Icon) bufferedImage);
            lbImage.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());

        }

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(bufferedImage, 0, 0, this);
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (lbImage != null) {
            lbImage.setIcon((Icon) bufferedImage);
        }
       /* if (labelValue != null) {
            String result = iWorldObject.getImageName();
            if (result.length() > 0) {
                labelValue.setText(String.valueOf(result));
            }
        }*/

    }

}
