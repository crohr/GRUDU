/****************************************************************************/
/* This class coresponds to a frame for a waiting state                     */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: WaitingDlg.java,v 1.6 2007/10/04 12:11:00 dloureir Exp $
 * $Log: WaitingDlg.java,v $
 * Revision 1.6  2007/10/04 12:11:00  dloureir
 * Chaging the Waiting Dialog to a JFrame
 *
 * Revision 1.5  2007/03/05 13:18:57  dloureir
 * Adding documentation
 *
 ****************************************************************************/
package diet.util.gui;

import javax.swing.*;
import java.awt.*;

/**
 * This class coresponds to a frame for a waiting state
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class WaitingDlg extends JFrame {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = -9019050571064609092L;
    /**
     * JProgressBar showing that something is happening
     */
    private JProgressBar myProgressBar;

    /**
     * Default constructor of the class
     *
     * @param parent parent frame for the JDialog behaviour
     * @param msg message to print in the dialog frame
     */
    public WaitingDlg(JFrame parent, String msg) {
        super("Operation in progress");
        setLocationRelativeTo(parent);
        this.myProgressBar = new JProgressBar();
        this.myProgressBar.setString("");
        this.myProgressBar.setIndeterminate(true);
        JPanel panel = new JPanel();

        panel.add(new JLabel(msg));
        panel.add(this.myProgressBar);

        add(panel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pack();
    }
}
