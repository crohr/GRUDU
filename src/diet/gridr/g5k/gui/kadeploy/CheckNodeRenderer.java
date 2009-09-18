/****************************************************************************/
/* Class representing the renderer of the associated Check node             */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: CheckNodeRenderer.java,v 1.6 2007/07/12 15:26:35 dloureir Exp $
 * $Log: CheckNodeRenderer.java,v $
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
import javax.swing.tree.*;
import javax.swing.plaf.*;

import diet.logging.LoggingManager;

import java.awt.*;
import java.util.logging.Level;

/**
 * Class representing the renderer of the associated Check node
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class CheckNodeRenderer extends JPanel implements TreeCellRenderer {
    /**
     * Default renderer for the tree of the KaDeploy frame
     */
    private static DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer ();
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 880066206148077267L;
    /**
     * JCheckBox for the renderer
     */
    protected JCheckBox check;

    /**
     * label of the tree
     */
    protected TreeLabel label;

    /**
     * Default constructor
     *
     */
    public CheckNodeRenderer() {
        setLayout(null);
        add(check = new JCheckBox());
        add(label = new TreeLabel());
        check.setBackground(UIManager.getColor("Tree.textBackground"));
        label.setForeground(UIManager.getColor("Tree.textForeground"));
        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "CheckNodeRenderer", "CheckNodeRenderer created");
    }

    /**
     * Method returning a component for the rendering of the check node
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean isSelected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected,
                expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        if (! (value instanceof CheckNode) )
            return defaultTreeCellRenderer.getTreeCellRendererComponent(tree, value,
                    isSelected, expanded, leaf, row,
                    hasFocus);
        check.setSelected(((CheckNode) value).isSelected());
        label.setFont(tree.getFont());
        label.setText(stringValue);
        label.setSelected(isSelected);
        label.setFocus(hasFocus);
        if (leaf) {
            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
        } else if (expanded) {
            label.setIcon(UIManager.getIcon("Tree.openIcon"));
        } else {
            label.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }
        return this;
    }

    /**
     * Method returning the preferred site of the renderer of the checknode
     *
     * @return the dimension of the check node renderer
     *
     */
    public Dimension getPreferredSize() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();
        return new Dimension(d_check.width + d_label.width,
                (d_check.height < d_label.height ? d_label.height
                        : d_check.height));
    }

    /**
     * Method placing the components of the CheckNode Renderer
     *
     */
    public void doLayout() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();
        int y_check = 0;
        int y_label = 0;
        if (d_check.height < d_label.height) {
            y_check = (d_label.height - d_check.height) / 2;
        }
        else {
            y_label = (d_check.height - d_label.height) / 2;
        }
        check.setLocation(0, y_check);
        check.setBounds(0, y_check, d_check.width, d_check.height);
        label.setLocation(d_check.width, y_label);
        label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
    }

    /**
     * Method setting the background color of the check node renderer
     *
     * @param color the color of the renderer to set
     *
     */
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
            color = null;
        super.setBackground(color);
    }
}