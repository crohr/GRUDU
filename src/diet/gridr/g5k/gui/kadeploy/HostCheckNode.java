/****************************************************************************/
/* Host check node                                                          */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: HostCheckNode.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: HostCheckNode.java,v $
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

/**
 * Host check node
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class HostCheckNode extends CheckNode {
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     *
     * @param cluster cluster of the host
     */
    public HostCheckNode(int cluster) {
        super(cluster);
    }

    /**
     * Second constructor
     *
     * @param userObject text of the node
     * @param cluster cluster of the host
     */
    public HostCheckNode(Object userObject, int cluster) {
        super(userObject, cluster, false, false);
    }

    /**
     * Third constructor
     *
     * @param userObject text of the node
     * @param cluster cluster of the host
     * @param allowsChildren boolean value telling that the node can have children
     * @param isSelected boolean value telling if the node is selected
     */
    public HostCheckNode(Object userObject, int cluster, boolean allowsChildren,
            boolean isSelected) {
        super(userObject, cluster, allowsChildren, isSelected);
    }
}