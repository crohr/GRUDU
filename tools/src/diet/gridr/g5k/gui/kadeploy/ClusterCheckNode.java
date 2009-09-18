/****************************************************************************/
/* Deployment check node class                                              */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ClusterCheckNode.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: ClusterCheckNode.java,v $
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

/**
 * Deployment check node class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class ClusterCheckNode extends CheckNode {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     *
     * @param cluster the cluster of this check node
     */
    public ClusterCheckNode(int cluster) {
        super(cluster);
    }

    /**
     * Second constructor
     *
     * @param userObject text of the check node
     * @param cluster cluster of the check node
     */
    public ClusterCheckNode(Object userObject, int cluster) {
        super(userObject, cluster, true, false);
    }

    /**
     * Third constructor
     *
     * @param userObject text of the check node
     * @param cluster cluster of the check node
     * @param allowsChildren boolean value telling if the cluster is allowed to have children
     * @param isSelected selected value telling that node is selected
     */
    public ClusterCheckNode(Object userObject, int cluster, boolean allowsChildren,
            boolean isSelected) {
        super(userObject, cluster, allowsChildren, isSelected);
    }

    /**
     * Method returning a vector containing the non selected nodes
     *
     * @return the non selected nodes
     */
    public Vector<HostCheckNode> getNonSelectedNodes() {
        Vector<HostCheckNode> v = new Vector<HostCheckNode>();
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            HostCheckNode node = (HostCheckNode) e.nextElement();
            if (node != null)
                v.add(node);
        }
        return v;
    }
}