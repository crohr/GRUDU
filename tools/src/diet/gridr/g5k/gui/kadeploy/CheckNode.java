/****************************************************************************/
/* Node to check for the  tree of the kaDeploy frame                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: CheckNode.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: CheckNode.java,v $
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

import java.util.*;
import java.util.logging.Level;

import javax.swing.tree.*;

import diet.logging.LoggingManager;

/**
 * Node to check for the  tree of the kaDeploy frame
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class CheckNode extends DefaultMutableTreeNode {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = -6791209333583975631L;
    /**
     * Integer value corresponding to selection mode
     */
    protected int selectionMode;
    /**
     * boolean value telling if the node is selected
     */
    protected boolean isSelected;

    /**
     * a value corresponding to a cluster
     */
    protected int myCluster;

    /**
     * Default constructor
     *
     * @param cluster the cluster of this node
     */
    public CheckNode(int cluster) {
        this(null, cluster);
    }

    /**
     * Second constructor with the text for this node
     *
     * @param userObject the text of the node
     * @param cluster the cluster for this node
     */
    public CheckNode(Object userObject, int cluster) {
        this(userObject, cluster, true, false);
    }

    /**
     * Third constructor
     *
     * @param userObject text of the node
     * @param cluster cluster of the node
     * @param allowsChildren boolean value defining that the node can have childrens
     * @param isSelected boolean value defining that the node is selected
     */
    public CheckNode(Object userObject, int cluster, boolean allowsChildren,
            boolean isSelected) {
        super(userObject, allowsChildren);
        this.myCluster = cluster;
        this.isSelected = isSelected;
        setSelectionMode(SelectionMode.RECURSIVE_SELECTION);
        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "CheckNode", "CheckNode created");
    }

    /**
     * Method returning the cluster index of the node
     *
     * @return cluster index of the node
     */
    public int getCluster() {
        return this.myCluster;
    }

    /**
     * Method setting the selection mode of the node
     *
     * @param mode selection mode
     */
    public void setSelectionMode(int mode) {
        selectionMode = mode;
    }

    /**
     * Method returning the selection mode of the node
     *
     * @return selection mode of the node
     */
    public int getSelectionMode() {
        return selectionMode;
    }

    /**
     * Method setting the selection state of the node
     *
     * @param isSelected boolean value telling that the node is selected
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

        if ((selectionMode == SelectionMode.RECURSIVE_SELECTION)
                && (children != null)) {
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                CheckNode node = (CheckNode) e.nextElement();
                node.setSelected(isSelected);
            }
        }
    }

    /**
     * Method telling that the node is selected of not
     *
     * @return boolean value telling that node is selected or not
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Method returning the selected nodes
     *
     * @return selected nodes
     */
    public CheckNode[] getSelectedNode() {
        Vector<CheckNode> selectedChild = new Vector<CheckNode>();
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            CheckNode node = (CheckNode) e.nextElement();
            if (node.isSelected())
                selectedChild.add(node);
        }
        CheckNode[] tab = new CheckNode[selectedChild.size()];
        selectedChild.toArray(tab);
        return tab;
    }
}