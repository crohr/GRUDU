/****************************************************************************/
/* Label for a tree                                                         */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: TreeLabel.java,v 1.6 2007/07/12 15:26:35 dloureir Exp $
 * $Log: TreeLabel.java,v $
 * Revision 1.6  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.5  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
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
import javax.swing.plaf.*;
import java.awt.*;

/**
 * Label for a tree
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class TreeLabel extends JLabel {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 1513724274844973469L;
    /**
     * Boolean value telling if the label is selected
     */
    boolean isSelected;
    /**
     * Boolean value telling if the label has the focus
     */
    boolean hasFocus;

    /**
     * Default constructor
     *
     */
    public TreeLabel() {
    }

    /**
     * Method setting the background
     *
     * @param color the color to set
     */
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
            color = null;
        super.setBackground(color);
    }

    /**
     * Method painting the component when needed
     *
     * @param g graphics to paint
     *
     */
    public void paint(Graphics g) {
        String str;
        if ((str = getText()) != null) {
            if (0 < str.length()) {
                if (isSelected) {
                    g.setColor(UIManager
                            .getColor("Tree.selectionBackground"));
                } else {
                    g.setColor(UIManager.getColor("Tree.textBackground"));
                }
                Dimension d = getPreferredSize();
                int imageOffset = 0;
                Icon currentI = getIcon();
                if (currentI != null) {
                    imageOffset = currentI.getIconWidth()
                    + Math.max(0, getIconTextGap() - 1);
                }
                g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
                        d.height);
                if (hasFocus) {
                    g.setColor(UIManager
                            .getColor("Tree.selectionBorderColor"));
                    g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
                            d.height - 1);
                }
            }
        }
        super.paint(g);
    }

    /**
     * Method returning the preferred size of the label
     *
     * @return the preferred dimension of the label
     *
     */
    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();
        if (retDimension != null) {
            retDimension = new Dimension(retDimension.width + 3,
                    retDimension.height);
        }
        return retDimension;
    }

    /**
     * Method setting the selected state of the label
     *
     * @param isSelected selected state to set
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Method setting the focus state of the label
     *
     * @param hasFocus focus state of the label
     */
    public void setFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}
