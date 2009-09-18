/****************************************************************************/
/* This class represents the view of the reservation on a site              */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ResaView.java,v 1.9 2007/10/24 12:51:05 dloureir Exp $
 * $Log: ResaView.java,v $
 * Revision 1.9  2007/10/24 12:51:05  dloureir
 * Removing some calls to the G5KCluster class by the corresponding calls to the G5KSite class
 *
 * Revision 1.8  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.7  2007/07/12 14:58:13  dloureir
 * Soem typo corrections
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

import diet.gridr.g5k.util.G5kCluster;
import diet.gridr.g5k.util.G5kSite;
import diet.logging.LoggingManager;

/**
 * This class represents the view of a reservation
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
/**
 * @author aamar
 *
 */
public class ResaView extends JLabel {

    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
    private static final long serialVersionUID = 5404927188663270258L;
//    /**
//     * indexes of the clusters for the site
//     */
//    private int[] clustersIndexes = null;
    /**
     * index of the site
     */
	private int siteIndex = 0;
	
    /**
     * array containing the number of reserved nodes for all sites of grid5000
     */
	private int [] reservedNodes = null;
	
	/**
	 * Chosen cluster index. If reservation is for any cluster -1 
	 */
	private int chosenCluster;
	
    /**
     * Default constructor
     *
     * @param siteIndex index of the site
     * @param reservedNodes array of the reserved nodes for all clusters of Grid'5000
     * @param parent parent object
     */
	public ResaView(int siteIndex, int[] reservedNodes, ResaDlg.ResaStep01Panel parent){
		super();
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.siteIndex = siteIndex;
		this.reservedNodes = reservedNodes;
		this.chosenCluster = -1;
		//clustersIndexes = G5kSite.getClustersIndexesForIndex(siteIndex);
		String aSite = G5kSite.getSiteForIndex(siteIndex);
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/gridr/g5k/" + aSite+".png"));
		this.setIcon(icon);
		this.setText(this.reservedNodes[siteIndex] + "");
		this.addMouseListener(new MouseAdapter(){
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();
				popup.add(new ResaPopupPanel(ResaView.this, popup));
				popup.show(ResaView.this, e.getX(), e.getY());
			}
		});
		createToolTipMessage();
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ResaView", "ResaView initialized");
	}

    /**
     * Method used to update the view
     *
     */
	public void updateView(){
		createToolTipMessage();
		this.setText(this.reservedNodes[this.siteIndex] + "");
		LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "updateview", "Site view of " + G5kSite.getSiteForIndex(siteIndex) + "updated");
	}

    /**
     * Method used to create the tooltip for the resaview
     *
     */
	public void createToolTipMessage(){
		String theName = G5kSite.getSiteForIndex(this.siteIndex);
//		int beginIndex = theName.indexOf("--");
//		if(beginIndex != -1) theName = theName.substring(0,beginIndex);
		String tooltipMessage = "";
		/*
		for(int i :clustersIndexes){
			// if(Config.isSiteEnable(i)){
				if (beginIndex != -1) 
				    tooltipMessage += "<li>" + G5kCluster.getNameForIndex(i).substring(beginIndex+2) + ": " + reservedNodes[i]+"</li>";
				else tooltipMessage+="<li>" + G5kCluster.getNameForIndex(i) + ": " + reservedNodes[i]+"</li>";
			// }
		}
		*/
		if (this.chosenCluster == -1) tooltipMessage += "<li> " + G5kSite.getSiteForIndex(this.siteIndex);
		else tooltipMessage += "<li> " + G5kCluster.getNameForIndex(this.chosenCluster);
		tooltipMessage += ": " + this.reservedNodes[this.siteIndex] + "</li>";
		this.setToolTipText("<html>"+ "<u><b>" + theName + "</b> reservation status:</u><br>"+
				"<ul>"+
				tooltipMessage+
				"</ul>"+
				"</html>");
	}

	/**
     * Method returning the reserved nodes
     *
	 * @return the reservedNodes
	 */
	public int[] getReservedNodes() {
		return reservedNodes;
	}

	/**
     * Method setting the reserved nodes for the resa view
     *
	 * @param reservedNodes the reservedNodes to set
	 */
	public void setReservedNodes(int[] reservedNodes) {
		this.reservedNodes = reservedNodes;
	}

	/**
     * Method returning the site index
     *
	 * @return the siteIndex
	 */
	public int getSiteIndex() {
		return siteIndex;
	}

	/**
     * Method setting the site index
     *
	 * @param siteIndex the siteIndex to set
	 */
	public void setSiteIndex(int siteIndex) {
		this.siteIndex = siteIndex;
	}

    /**
     * Return the chosen cluster for the site
     * 
     * @return the chosen cluster or -1 if no cluster was chosen
     */
    public int getChosenCluster() {
        return chosenCluster;
    }

    
    /**
     * Set a reservation cluster for reservation
     * 
     * @param chosenCluster set the selected cluster
     */
    public void setChosenCluster(int chosenCluster) {
        int [] clusterIndexes = G5kSite.getClustersIndexesForIndex(this.siteIndex);
        if (chosenCluster == -1)
            this.chosenCluster = chosenCluster;
        for (int clusterIdx : clusterIndexes) {
            if (chosenCluster == clusterIdx) {
                this.chosenCluster = chosenCluster;
                return;
            }
        }
    }
}