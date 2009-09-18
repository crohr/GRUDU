/****************************************************************************/
/* This class corresponds to the rendered of the IconNode                   */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: IconNodeRenderer.java,v 1.12 2007/10/08 14:53:55 dloureir Exp $
 * $Log: IconNodeRenderer.java,v $
 * Revision 1.12  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.11  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.10  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.*;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.tree.*;

import diet.grid.api.GridJob;
import diet.gridr.g5k.util.Config;
import diet.gridr.g5k.util.G5kCfg;
import diet.gridr.g5k.util.G5kSite;

/**
 * This class corresponds to the rendered of the IconNode
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
class IconNodeRenderer extends JPanel implements TreeCellRenderer {
    /**
     * JLabel for the icon
     */
	JLabel labelIcon = null;
    /**
     * JLabel for the text
     */
	JLabel labelText = null;

    /**
     * Default constructor for the IconNodeRenderer
     *
     */
	public IconNodeRenderer(){
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		labelIcon = new JLabel();
		labelText = new JLabel();
		this.setBackground(Color.white);
		this.add(labelIcon,null);
		this.add(labelText,null);
	}

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = -7640733662746518192L;

    /**
     * Method returning the iconNode rendered
     */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if (value instanceof IconNode) {
			Icon icon = ((IconNode) value).getIcon();
			labelText.setText(((IconNode) value).getIconName());
			labelIcon.setIcon(icon);
			labelText.setForeground(Color.BLACK);
			this.setBackground(Color.white);
			int index = G5kSite.getIndexForSite(((IconNode) value).getIconName());
			if(index != -1){
				if(!Config.isSiteEnable(index)) this.setBackground(Color.LIGHT_GRAY);
			}
			Enumeration<DefaultMutableTreeNode> enumer = ((DefaultMutableTreeNode) value).children();
			boolean thereIsARunningReservation = false;
			boolean thereIsAWaitingReservation = false;
			while(enumer.hasMoreElements()){
				DefaultMutableTreeNode temp = enumer.nextElement();
				if(temp instanceof JobTreeNode){
					if(G5kCfg.get(G5kCfg.USERNAME).equalsIgnoreCase(((JobTreeNode) temp).myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER)) && ((JobTreeNode) temp).myJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE).toLowerCase().startsWith("r") ){
						thereIsARunningReservation = true;
					}
					else if(G5kCfg.get(G5kCfg.USERNAME).equalsIgnoreCase(((JobTreeNode) temp).myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER)) && ((JobTreeNode) temp).myJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE).toLowerCase().startsWith("w")){
						thereIsAWaitingReservation = true;
					}
				}
			}
			if(thereIsAWaitingReservation){
				labelText.setForeground(Color.RED);
				this.setBackground(Color.YELLOW);
			}
			if(thereIsARunningReservation){
				labelText.setForeground(Color.RED);
				this.setBackground(Color.GREEN);
			}
		}
		if (value instanceof JobTreeNode) {
			Icon icon = ((JobTreeNode) value).getIcon();
			labelIcon.setIcon(icon);
			labelText.setText(((JobTreeNode) value).getIconName());
			if(Config.getUser().equalsIgnoreCase(((JobTreeNode) value).myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER)) && ((JobTreeNode) value).myJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE).toLowerCase().startsWith("r")){
				labelText.setForeground(Color.RED);
				this.setBackground(Color.GREEN);
			}
			else if(G5kCfg.get(G5kCfg.USERNAME).equalsIgnoreCase(((JobTreeNode) value).myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER)) && ((JobTreeNode) value).myJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE).toLowerCase().startsWith("w") ){
				labelText.setForeground(Color.RED);
				this.setBackground(Color.YELLOW);
			}
		}

		return this;
	}
}
