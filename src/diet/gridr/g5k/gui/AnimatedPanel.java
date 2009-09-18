/****************************************************************************/
/* This class corresponds to the animated panel used to wait in the main    */
/* frame                                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: AnimatedPanel.java,v 1.9 2007/11/19 15:16:19 dloureir Exp $
 * $Log: AnimatedPanel.java,v $
 * Revision 1.9  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.8  2007/11/07 10:35:23  dloureir
 * Changing the rendering parameters for the animations
 *
 * Revision 1.7  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import diet.logging.LoggingManager;

/**
 * This class corresponds to the animated panel used to wait in the main
 * frame
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class AnimatedPanel extends JPanel {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = 1L;
    /**
     * Gradient
     */
	private float gradient;
    /**
     * Message to print
     */
    private String message;
    /**
     * Thread in which the animator is running
     */
    private Thread animator;
    /**
     * Convolved image
     */
    private BufferedImage convolvedImage;
    /**
     * Original image used
     */
    private BufferedImage originalImage;
    /**
     * Font for the message to print
     */
    private Font font;
    /**
     * JProgressBar showing that something is happenning in background
     */
    private JProgressBar jProgressBar = null;
    /**
     * Panel containing the JProgressBar
     */
    private JPanel jProgressBarPanel = null;

    /**
     * Default constructor of the AnimatorPanel
     *
     * @param message message to print
     * @param icon the image to print
     */
    public AnimatedPanel(String message, ImageIcon icon) {
        this.message = message;
        this.font = getFont().deriveFont(14.0f);

        Image image = icon.getImage();
        originalImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB);
        convolvedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB);
        Graphics g = originalImage.createGraphics();
        g.drawImage(image, 0, 0, this);
        g.dispose();

        setBrightness(0.7f);
        setOpaque(false);
        setEnabled(false);
        setBackground(Color.LIGHT_GRAY);

        setLayout(new BorderLayout());
        jProgressBarPanel = new JPanel();
        jProgressBarPanel.setLayout(new BorderLayout());
        jProgressBarPanel.add(Box.createHorizontalStrut(10),BorderLayout.EAST);
        jProgressBarPanel.add(Box.createHorizontalStrut(10),BorderLayout.WEST);
        jProgressBarPanel.add(Box.createVerticalStrut(10),BorderLayout.NORTH);
        jProgressBarPanel.add(Box.createVerticalStrut(10),BorderLayout.SOUTH);
        jProgressBar = new JProgressBar();
        jProgressBar.setIndeterminate(true);
        jProgressBar.setString("");
        Dimension dim = new Dimension(icon.getIconWidth(), 10);
        jProgressBar.setSize(dim);
        jProgressBar.setPreferredSize(dim);
        jProgressBar.setMaximumSize(dim);
        jProgressBar.setMinimumSize(dim);
        jProgressBarPanel.add(jProgressBar,BorderLayout.CENTER);
        this.add(jProgressBarPanel,BorderLayout.SOUTH);
    }
    /**
     * Main method of the AnimatorPanel taht change the image
     *
     * @param g Graphics from that panel
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (convolvedImage != null) {
            int width = getWidth();
            int height = getHeight();

            synchronized (convolvedImage) {
                Graphics2D g2 = (Graphics2D) g;
                
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
                g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);

                FontRenderContext context = g2.getFontRenderContext();
                TextLayout layout = new TextLayout(message, font, context);
                Rectangle2D bounds = layout.getBounds();

                int x = (width - convolvedImage.getWidth(null)) / 2;
                int y = (int) (height - (convolvedImage.getHeight(null) + bounds.getHeight() + layout.getAscent())) / 2;

                g2.drawImage(convolvedImage, x, y, this);
                g2.setColor(new Color(0, 0, 0, (int) (gradient * 255)));
                layout.draw(g2, (float) (width - bounds.getWidth()) / 2,
                    (float) (y + convolvedImage.getHeight(null) + bounds.getHeight() + layout.getAscent()));
            }
        }
    }

    /**
     * Method changing the brigthness of the panel
     *
     * @param multiple multiple value for the brigthness change
     */
    private void setBrightness(float multiple) {
        float[] brightKernel = { multiple };
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImageOp bright = new ConvolveOp(new Kernel(1, 1, brightKernel),
            ConvolveOp.EDGE_NO_OP, hints);
        bright.filter(originalImage, convolvedImage);
        repaint();
    }

    /**
     * Method changing the gradient factor
     *
     * @param gradient gradient factor to set
     */
    private void setGradientFactor(float gradient) {
        this.gradient = gradient;
    }

    /**
     * Method starting the Thread associated to the Panel
     *
     */
    public void start() {
    	setVisible(true);
        this.animator = new Thread(new HighlightCycler(), "Highlighter");
        this.animator.start();
    }

    /**
     * Method stoping the thread associated to the Panel
     *
     */
    public void stop() {
        if (this.animator != null)
            this.animator.interrupt();
        this.animator = null;
        this.setVisible(false);
    }

    /**
     * Class defining a Runnable object that change the properties of
     * the panel
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     */
    class HighlightCycler implements Runnable {

        /**
         * Value defining the sense in which the panel change
         */
        private int way = 1;
        /**
         * Lower value for the cycle
         */
        private final int LOWER_BOUND = 10;
        /**
         * Upper value for the cycle
         */
        private final int UPPER_BOUND = 35;
        /**
         * Initial value for the cycle
         */
        private int value = LOWER_BOUND;

        /**
         * Main method of the runnable class
         */
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000 / (UPPER_BOUND - LOWER_BOUND));
                } catch (InterruptedException e) {
                    return;
                }

                value += this.way;
                if (value > UPPER_BOUND) {
                    value = UPPER_BOUND;
                    this.way = -1;
                } else if (value < LOWER_BOUND) {
                    value = LOWER_BOUND;
                    this.way = 1;
                }

                synchronized (convolvedImage) {
                    setBrightness((float)value/LOWER_BOUND*0.5f);
                    setGradientFactor((float) value / UPPER_BOUND);
                }
            }
        }
    }

    /**
     * Method returning the message
     *
     * @return message
     */
	public String getMessage() {
		return message;
	}

	/**
     * Method setting the message to print
     *
     * @param message message to set
	 */
	public synchronized void setMessage(String message) {
		this.message = message;
        try{
            this.paintComponent(this.getGraphics());
        }
        catch(Exception e){
            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setMessage", e);
        }
	}
}
