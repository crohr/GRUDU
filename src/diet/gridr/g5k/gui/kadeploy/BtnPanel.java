/****************************************************************************/
/* Class for the button panel of the KaDeploy frame                         */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: BtnPanel.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: BtnPanel.java,v $
 * Revision 1.5  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.3  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.2  2007/02/22 15:14:18  aamar
 * Correct header.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui.kadeploy;

import java.awt.event.*;
import java.awt.FlowLayout;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JPanel;

import diet.logging.LoggingManager;

/**
 * Class for the button panel of the KaDeploy frame
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class BtnPanel extends JPanel {
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
    private static final long serialVersionUID = 1L;
    /**
     * ok button
     */
    private JButton okBtn;
    /**
     * cancel button
     */
    private JButton cancelBtn;
    /**
     * deploy command
     */
    public final static String DEPLOY_CMD = "Deploy";
    /**
     * Ok text
     */
    public final static String OK = "Ok";
    /**
     * cancel button
     */
    public final static String CANCEL = "Cancel";

    /**
     * default constructor
     *
     * @param listener
     */
    public BtnPanel(ActionListener listener) {
        this(listener, OK);
    }

    /**
     * Second constructor initializing the panel
     *
     * @param listener
     * @param okCmd
     */
    public BtnPanel(ActionListener listener, String okCmd) {
        setLayout(new FlowLayout());
        add(this.okBtn = new JButton(okCmd));
        add(this.cancelBtn = new JButton(CANCEL));
        this.okBtn.setActionCommand(okCmd);
        this.cancelBtn.setActionCommand(CANCEL);
        this.okBtn.addActionListener(listener);
        this.cancelBtn.addActionListener(listener);
        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "BtnPanel","BtnPanel created");
    }
}