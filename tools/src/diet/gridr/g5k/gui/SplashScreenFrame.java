/****************************************************************************/
/* This class represents the Splash Screen frame                            */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: SplashScreenFrame.java,v 1.4 2007/07/12 15:12:09 dloureir Exp $
 * $Log: SplashScreenFrame.java,v $
 * Revision 1.4  2007/07/12 15:12:09  dloureir
 * Some typo corrections.
 *
 * Revision 1.3  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.2  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JWindow;

import diet.logging.LoggingManager;

/**
 * This class represents the Splash Screen frame
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class SplashScreenFrame extends JWindow {
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
    private static final long serialVersionUID = 6782427845950075444L;
    /**
     * image for the splash screen
     */
    private BufferedImage splash = null;

    /**
     * Default constructor
     *
     * @param image image to show
     */
    public SplashScreenFrame(BufferedImage image) {
        createShadowPicture(image);
    }

    /**
     * Method painting the image into the panel
     */
    public void paint(Graphics g) {
        if (splash != null) {
            g.drawImage(splash, 0, 0, null);
        }
    }

    /**
     * Method creating the shadow of the image
     *
     * @param image image on which we have to create a shadow
     */
    private void createShadowPicture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int extra = 14;

        setSize(new Dimension(width + extra, height + extra));
        setLocationRelativeTo(null);
        Rectangle windowRect = getBounds();

        splash = new BufferedImage(width + extra, height + extra, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) splash.getGraphics();

        try {
            Robot robot = new Robot(getGraphicsConfiguration().getDevice());
            BufferedImage capture = robot.createScreenCapture(new Rectangle(windowRect.x, windowRect.y, windowRect.width + extra, windowRect.height + extra));
            g2.drawImage(capture, null, 0, 0);
        } catch (AWTException e) { }

        BufferedImage shadow = new BufferedImage(width + extra, height + extra, BufferedImage.TYPE_INT_ARGB);
        Graphics g = shadow.getGraphics();
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
        g.fillRoundRect(3, 3, width, height, 12, 12);

        g2.drawImage(shadow, getBlurOp(10), 0, 0);
        g2.drawImage(image, 0, 0, this);
    }

    /**
     * Method creating the shadow
     *
     * @param size site of the shadow
     *
     * @return operation of convolution
     */
    private ConvolveOp getBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1 / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data));
    }

    /**
     * Method displaying the splash screen on the screen
     * @param anImage
     */
    public static void splashImageOnScreen(String anImage){
        try {
            BufferedImage image = ImageIO.read(SplashScreenFrame.class.getResourceAsStream(anImage));
            SplashScreenFrame window = new SplashScreenFrame(image);
            window.setVisible(true);
            Thread.sleep(5000);
            window.setVisible(false);
        } catch (Exception e) {
            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, SplashScreenFrame.class.getName(), "splashImageOnScreen", e);
        }
    }
}