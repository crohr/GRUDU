/****************************************************************************/
/* Panel for the definition of the selection mode                           */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: SelectionModePanel.java,v 1.5 2007/04/05 07:58:18 dloureir Exp $
 * $Log: SelectionModePanel.java,v $
 * Revision 1.5  2007/04/05 07:58:18  dloureir
 * Correcting a bug concerning the selection of all or none nodes
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
import javax.swing.border.*;

import diet.logging.LoggingManager;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;

/**
 * Panel for the definition of the selection mode
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class SelectionModePanel extends JPanel implements ActionListener {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 1687427344436718679L;
    /**
     * parent panel
     */
    private MainPanel myParent;
    /**
     * button used to select all nodes
     */
    private JButton selectAllBtn;
    /**
     * button used to deselect all nodes
     */
    private JButton deselectAllBtn;
    /**
     * selection mode
     */
    private int mode;

    /**
     * Default constructor
     *
     * @param parent parent panel
     */
    public SelectionModePanel(MainPanel parent) {
        this.myParent = parent;
        setLayout(new FlowLayout());
        setBorder(new TitledBorder("Selection Mode"));
        ButtonGroup group = new ButtonGroup();
        add(this.selectAllBtn = new JButton("Select all hosts"));
        add(this.deselectAllBtn = new JButton("Select None"));
        group.add(this.selectAllBtn);
        group.add(this.deselectAllBtn);
        this.selectAllBtn.addActionListener(this);
        this.deselectAllBtn.addActionListener(this);
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "SelectionModePanel", "SelectionModePanel created" );
    }

    /**
     * Method managing the event for the selection mode panel
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(selectAllBtn))this.myParent.selectAllNodes(true);
        else if(e.getSource().equals(deselectAllBtn))this.myParent.selectAllNodes(false);
    }

    /**
     * Method returning the selection mode
     *
     * @return selection mode
     */
    public int getSelectionMode() {
        return this.mode;
    }
}