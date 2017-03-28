package hu.oe.nik.szfmv17t.visualisation;

import hu.oe.nik.szfmv17t.environment.domain.Turn;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.interfaces.IWorldVisualisation;
import hu.oe.nik.szfmv17t.environment.utils.Config;
import hu.oe.nik.szfmv17t.visualisation.interfaces.IWorldVisualization;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mariusz on 2017.03.15..
 */
public class Drawer implements IWorldVisualization {
    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    private IWorldVisualization world;

    @Override
    public List<IWorldObject> getWorld() {
        return world.getWorld();
    }

    private static Drawer instance = null;
    private static ArrayList<BufferedImage> worldImages;

    private Drawer(IWorldVisualisation world) {
    }

    public static Drawer getDrawer(IWorldVisualisation world) throws IOException {
        if (instance == null) {
            worldImages = new ArrayList<BufferedImage>();
            instance = new Drawer(world);
            for (IWorldObject object : world.getWorld()) {
                BufferedImage bimg = ImageIO.read(new File(ClassLoader.getSystemResource(object.getImageName()).getFile()));
                worldImages.add(bimg);
            }
        }
        return instance;
    }

    public FrameComposer getComposer(IWorldVisualisation world) {
        return FrameComposer.getComposer(world);
    }

    //private static double t=0.1;
    //double direction =0.1;
    int t = 0;

    public void DrawFrametoPanel(JPanel worldObjectsPanel, IWorldVisualisation world, JPanel mainPanel) {
        BorderLayout layout = (BorderLayout) mainPanel.getLayout();
        mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        FrameComposer fc = getComposer(world);
        fc.setCameraSize(worldObjectsPanel.getWidth(), worldObjectsPanel.getHeight());
        List<IWorldObject> toDraw = fc.composeFrame();
        worldObjectsPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                int t2 = 0;
                BufferedImage image;
                Graphics2D g2d = (Graphics2D) g.create();
                for (IWorldObject object : toDraw) {
                    // draw objects
                    image = worldImages.get(t2++);
                    g2d.drawImage(image, getObjectTransformation(calculateDrawCornerX(object), calculateDrawCornerY(object), object), null);
                }
            }
        };
        mainPanel.add(worldObjectsPanel, BorderLayout.CENTER);
    }
    private AffineTransform getCornerRotateTransform(IWorldObject object)
    {
        if (calculateRotateBaseY(object)!=Double.MIN_VALUE)
            return AffineTransform.getRotateInstance(-object.getAxisAngle(),calculateRotateBaseX(object),calculateRotateBaseY(object));
        return AffineTransform.getRotateInstance(-object.getAxisAngle());
    }
    private double calculateRotateBaseY(IWorldObject object)
    {
        if (Turn.class.isInstance(object))
        {
            if (object.getImageName()=="road_2lane_tjunctionright.png" || object.getImageName()=="road_2lane_tjunctionleft.png")
                return 0;
            else
               return (object.getHeight()) / Config.SCALE;
        }
        return Double.MIN_VALUE;
    }
    private double calculateRotateBaseX(IWorldObject object)
    {
        if (Turn.class.isInstance(object))
        {
            if (object.getImageName()=="road_2lane_tjunctionright.png" || object.getImageName()=="road_2lane_tjunctionleft.png")
                return object.getWidth()/Config.SCALE;
            else
               return Config.roadWidth/Config.SCALE;
        }
        return Double.MIN_VALUE;
    }
    private double calculateDrawCornerY(IWorldObject object)
    {
        double drawCornerY=0;
        if (Turn.class.isInstance(object))
        {
            double baseY=(object.getCenterY()-(object.getHeight()/2));
            if (object.getImageName()=="road_2lane_tjunctionright.png" || object.getImageName()=="road_2lane_tjunctionleft.png")
                drawCornerY=baseY;
            else
                drawCornerY = (baseY - object.getHeight());
        }
        else
            drawCornerY = ((int)(object.getCenterY()-object.getHeight()/2)) ;
        return drawCornerY/Config.SCALE;
    }
    private double calculateDrawCornerX(IWorldObject object)
    {
        double drawCornerX=0;
        if (Turn.class.isInstance(object))
        {
            double baseX=(object.getCenterX()-(object.getWidth()/2));
            if (object.getImageName()=="road_2lane_tjunctionright.png" || object.getImageName()=="road_2lane_tjunctionleft.png")
                drawCornerX = (baseX - object.getWidth());
            else
                drawCornerX = (baseX - 350) ;
        }
        else
            drawCornerX = ((int) (object.getCenterX() - object.getWidth() / 2)) ;
        return drawCornerX/Config.SCALE;
    }
    private AffineTransform getObjectTransformation(double drawCornerX,double drawCornerY, IWorldObject object)
    {
        AffineTransform transform=AffineTransform.getTranslateInstance(drawCornerX,drawCornerY);
        transform.concatenate(getCornerRotateTransform(object));
        transform.scale(Config.SCALENUM, Config.SCALENUM);
        return transform;
    }
    private void PutDebugInformationOnImage (Image image, IWorldObject object) {
        Graphics2D g = (Graphics2D) image.getGraphics();

        String loc = String.format ("x: %.0f, y:%.0f", object.getCenterX(), object.getCenterY(), object.getAxisAngle());
        String rot = String.format ("%.3f (rad)", object.getAxisAngle());

        g.setColor(Color.red);
        g.drawRect(0, 0, image.getWidth(null) - 1, image.getHeight(null) - 1);

        g.setColor(Color.black);
        g.setFont(new Font("sans", Font.PLAIN, 15));
        g.drawString(loc, 3, 20);
        g.drawString(rot, 3, 35);
    }
}
