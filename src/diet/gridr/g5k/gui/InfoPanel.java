/****************************************************************************/
/* This class corresponds to the info panel used to display information     */
/* about a grid, a cluster and a job                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: InfoPanel.java,v 1.19 2007/11/29 21:42:22 dloureir Exp $
 * $Log: InfoPanel.java,v $
 * Revision 1.19  2007/11/29 21:42:22  dloureir
 * Bug correction when the Ganglia plugin is not installed
 *
 * Revision 1.18  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.17  2007/10/30 14:11:39  dloureir
 * Integration of the Ganglia plugin
 *
 * Revision 1.16  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.15  2007/10/08 08:15:29  dloureir
 * Adding a jbuttonbar for the jobpanel
 *
 * Revision 1.14  2007/10/05 16:12:55  dloureir
 * Changing the way hosts are displayed in the OarJobPanel
 *
 * Revision 1.13  2007/10/05 15:19:40  dloureir
 * Addition of the button bar from the l2fprod commons for further addition of plugins (such as the ganglia plugin)
 *
 * Revision 1.12  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.11  2007/07/12 14:14:42  dloureir
 * Some code correction and the clean up of an unused attribute.
 *
 * Revision 1.10  2007/03/26 20:36:25  dloureir
 * Adding the ssh connection to the constructor for the gantt chart
 *
 * Revision 1.9  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;

import com.trilead.ssh2.Connection;

import java.awt.*;
import java.util.Map;
import java.util.logging.Level;

import diet.logging.LoggingManager;

import diet.grid.api.GridJob;

/**
 * This class corresponds to the info panel used to display information
 * about a grid, a cluster and a job
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class InfoPanel extends JPanel {
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = 1889774818638150933L;
    /**
     * Panel displaying the properties of a job
     */
	private JPanel myJobPanel;
    /**
     * CardLayout that allows the displaying of several different panels in the same container
     */
	private CardLayout carder;
    /**
     * Cluster Info Panel
     */
	//private ClusterInfoPanel clusterInfoPanel;
	private SiteInformationPanel siteInformationPanel = null;
    /**
     * CardPanel where the card Layout is applied
     */
	private JPanel cardPanel = null;
    /**
     * Legend panel for the (grid5000, cluster or job state)
     */
	private LegendPanel legendPanel = null;
    /**
     * Default constructor of the InfoPanel
     *
     */
	public InfoPanel() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		this.add(getCardPanel());
		this.add(Box.createVerticalGlue());
		this.add(getLegendPanel());
		this.setVisible(true);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL , this.getClass().getName(), "InfoPanel", "Info panel initialized");
	}

    /**
     * Method returning the cardPanel
     *
     * @return the cardPanel
     */
	public JPanel getCardPanel(){
		if(cardPanel == null){
			cardPanel = new JPanel();
			carder = new CardLayout();
			cardPanel.setLayout(carder);
		}
		return cardPanel;
	}

    /**
     * Method returning the LegendPanel
     *
     * @return the legendPanel
     */
	public JPanel getLegendPanel(){
		if(legendPanel == null){
			legendPanel = new LegendPanel();
		}
		return legendPanel;
	}

    /**
     * Method showing the job given in parameter
     *
     * @param job the job to display
     */
	protected void showJob(GridJob job, Connection aConnection, String parentSite) {
		cardPanel.removeAll();
		try{
			Class aClass = Class.forName("ganglia.controller.GangliaAnalyzer");
		myJobPanel = new JobInformationPanelWithGanglia(job, aConnection,parentSite);
		}
		catch(Exception e){
			myJobPanel = new JobInformationPanel(job, aConnection,parentSite);
		}
		cardPanel.add("job", myJobPanel);
		carder.show(cardPanel,"job");
		this.validate();
		this.setVisible(true);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL , this.getClass().getName(), "showJob", "Job " +job.getParameterValue(GridJob.KEY_GRID_JOB_ID) + " is shown");
	}

    /**
     * Method used to display a cluster
     *
     * @param data data to display
     * @param jobsMap map of jobs
     * @param clusterName name of the cluster to display
     */
	protected void showCluster(int[] data, Map<String, GridJob> jobsMap, String clusterName,Connection connection) {
			cardPanel.removeAll();
			//clusterInfoPanel = new ClusterInfoPanel(data,jobsMap, clusterName, connection);
			siteInformationPanel = new SiteInformationPanel(data,jobsMap, clusterName, connection);
			cardPanel.add("site",siteInformationPanel);
			carder.show(cardPanel, "site");
			this.validate();
			this.setVisible(true);
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL , this.getClass().getName(), "showCluster", "Cluster " + clusterName + " is shown");
	}

    /**
     * Method displaying the Grid5000 view
     *
     * @param someComponents to display that composed to view
     * @param data data to display
     */
	protected void setG5KView(Component[] someComponents ,int[][] data){
		cardPanel.removeAll();
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel,BoxLayout.Y_AXIS));
		int maxWidth = 0;
		for(int i = 0 ; i < someComponents.length ; i++){
			if(someComponents[i].getPreferredSize().width > maxWidth) maxWidth = someComponents[i].getPreferredSize().width;
		}
		myPanel.add(new G5kSummaryChart(data));
		for(int i = 0 ; i < someComponents.length ; i++){
			myPanel.add(someComponents[i]);
		}
		cardPanel.add("g5k",myPanel);
		carder.show(cardPanel, "g5k");
		this.validate();
		this.setVisible(true);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL , this.getClass().getName(), "setG5KView", "Grid 5000 view is shown");
	}

    /**
     * Method cleaning the view
     *
     */
	public void showCleaned(){
		cardPanel.removeAll();
	}

}
