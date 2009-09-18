/****************************************************************************/
/* Listener for the selection of a node                                     */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: NodeSelectionListener.java,v 1.6 2007/09/28 16:03:02 aamar Exp $
 * $Log: NodeSelectionListener.java,v $
 * Revision 1.6  2007/09/28 16:03:02  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
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
import javax.swing.tree.*;

import diet.logging.LoggingManager;

import java.awt.event.*;
import java.util.logging.Level;

/**
 * Listener for the selection of a node
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class NodeSelectionListener extends MouseAdapter {
    /**
     * the tree
     */
    private JTree myTree;
    /**
     * parent panel
     */
    private MainPanel myParent;
    /**
     * Default constructor
     *
     * @param parent parent panel
     * @param tree tree
     */
    NodeSelectionListener(MainPanel parent, JTree tree) {
        this.myParent = parent;
        this.myTree = tree;
    }

    /**
     * Method used to handle the event when the mouse is clicked
     *
     * @param e a MouseEvent to handle
     *
     */
    public void mouseClicked(MouseEvent e) {
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "mouseClicked", "+ button   " + e.getButton());
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "mouseClicked","- modifier " + e.getModifiers());
        int x = e.getX();
        int y = e.getY();
        int row = this.myTree.getRowForLocation(x, y);
        TreePath  path = this.myTree.getPathForRow(row);
        if (path != null) {
            if (path.getLastPathComponent() instanceof CheckNode) {
                CheckNode node = (CheckNode)path.getLastPathComponent();
                boolean isSelected = ! (node.isSelected());
                node.setSelected(isSelected);
                if (node.getSelectionMode() == SelectionMode.RECURSIVE_SELECTION) {
                    if ( isSelected) {
                        this.myTree.expandPath(path);
                    } else {
                        
                    }
                }
                ((DefaultTreeModel) this.myTree.getModel()).nodeChanged(node);
                // I need revalidate if node is root.  but why?
                if (row == 0) {
                    this.myTree.revalidate();
                    this.myTree.repaint();
                }
            }
        }
    }

    /**
     * Method used to handle the event when the mouse is pressed
     *
     * @param event a MouseEvent to handle
     *
     */
    public void mousePressed( MouseEvent event ) {
        handlePopup( event );
    }

    /**
     * Method used to handle the event when the mouse is released
     *
     * @param event a MouseEvent to handle
     *
     */
    public void mouseReleased( MouseEvent event ) {
        handlePopup( event );
    }

    /**
     * Method used to handle the event when the mouse is used
     *
     * @param event a MouseEvent to handle
     *
     */
    private void handlePopup( MouseEvent event ) {
        if ( event.isPopupTrigger() ) {
            myParent.setSelectedNode(null);
            TreePath path = myTree.getPathForLocation( event.getX(), event.getY() );
            if ( path != null ) {
                myTree.getSelectionModel().setSelectionPath( path );
                TreeNode node = (TreeNode) path.getLastPathComponent();
                if (node instanceof ClusterCheckNode) {
                    myParent.setSelectedNode((ClusterCheckNode) node);
                    myParent.showPopup(event.getComponent(), event.getX(), event.getY() );
                } // end if instanceof ClusterCheckNode
            } // end if path != null
        } // end if isPopupTrigger
    } // end void handlePopup
}