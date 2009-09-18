/****************************************************************************/
/* Class that represents the selection panel                                */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: SelectionPanel.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: SelectionPanel.java,v $
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

import javax.swing.*;

import diet.logging.LoggingManager;

import java.awt.*;
import java.util.logging.Level;

/**
 * Class that represents the selection panel
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class SelectionPanel extends JPanel {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3544461806228328538L;
    /**
     * panel for the selection mode
     */
    private SelectionModePanel mySelectionModePanel;
    /**
     * panel for the kernels
     */
    private KernelPanel   myKernelPanel;

    /**
     * Default panel
     *
     * @param parent parent panel
     * @param sp selection mode panel
     * @param kp kernel selection panel
     * @param btnPanel button panel
     */
    public SelectionPanel(MainPanel parent, SelectionModePanel sp, KernelPanel kp,
            BtnPanel btnPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        GridBagConstraints c = new GridBagConstraints();
        this.mySelectionModePanel = sp;
        this.myKernelPanel = kp;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        add(this.mySelectionModePanel);
        c.gridy = 1;
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(this.myKernelPanel);
        add (pane);
        c.gridy = 2;
        add(btnPanel);
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "SelecionPanel", "SelectionPanel");
    }
}