/****************************************************************************/
/* Splash screen class                                                      */
/*                                                                          */
/* Author(s)                                                                */
/* - David Loureiro (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: SplashScreenFrame.java,v 1.4 2007/02/24 19:23:17 aamar Exp $
 * $Log: SplashScreenFrame.java,v $
 * Revision 1.4  2007/02/24 19:23:17  aamar
 * Correct the header.
 *
 * Revision 1.3  2007/02/24 19:20:26  aamar
 * Remove some warnings.
 *
 ****************************************************************************/
package diet;

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

import javax.imageio.ImageIO;
import javax.swing.JWindow;

public class SplashScreenFrame extends JWindow {
    /**
     * Serial version ID 
     */
	private static final long serialVersionUID = 1L;

    private BufferedImage splash = null;

	    public SplashScreenFrame(BufferedImage image) {
	        createShadowPicture(image);
	    }

	    public void paint(Graphics g) {
	        if (splash != null) {
	            g.drawImage(splash, 0, 0, null);
	        }
	    }

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

	    private ConvolveOp getBlurOp(int size) {
	        float[] data = new float[size * size];
	        float value = 1 / (float) (size * size);
	        for (int i = 0; i < data.length; i++) {
	            data[i] = value;
	        }
	        return new ConvolveOp(new Kernel(size, size, data));
	    }

	public static void splashImageOnScreen(String anImage){
		try {
            BufferedImage image = ImageIO.read(SplashScreenFrame.class.getResourceAsStream(anImage));
            SplashScreenFrame window = new SplashScreenFrame(image);
            window.setVisible(true);
            Thread.sleep(5000);
            window.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
