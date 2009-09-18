/****************************************************************************/
/* This class corresponds to a node with a text and an icon                 */
/*                                                                          */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: IconNode.java,v 1.5 2007/07/12 14:40:43 dloureir Exp $
 * $Log: IconNode.java,v $
 * Revision 1.5  2007/07/12 14:40:43  dloureir
 * A light typo correction
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * This class corresponds to a node with an icon and a label
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class IconNode extends DefaultMutableTreeNode {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = 1048108979163578633L;
    /**
     * icon of the node
     */
	protected Icon myIcon;
    /**
     * label of the node
     */
	protected String iconName;

    /**
     * Default constructor of the IconNode
     *
     */
	public IconNode() {
		this(null);
	}

    /**
     * Second constructor with only it name
     *
     * @param userObj
     */
	public IconNode(Object userObj) {
		this(userObj, true, null);
	}

    /**
     * Third constructor with its label and a icon
     * @param userObj
     * @param allowChild
     * @param icon
     */
	public IconNode(Object userObj, boolean allowChild, Icon icon) {
		super(userObj, allowChild);
		this.iconName = (String)userObj;
		this.myIcon = icon;
	}

    /**
     * Method setting the icon
     *
     * @param icon icon to set
     */
	public void setIcon(Icon icon) {
		this.myIcon = icon;
	}

    /**
     * Method returning the icon
     *
     * @return the icon of the IconNode
     */
	public Icon getIcon() {
		return this.myIcon;
	}

    /**
     * Method returning the icon name
     *
     * @return the icon name
     */
	public String getIconName() {
		if (iconName != null) {
			return iconName;
		} else {
			String str = userObject.toString();
		    int index = str.lastIndexOf(".");
		    if (index != -1) {
		    	return str.substring(++index);
		    } else {
		    	return null;
		    }
		}
	}
}