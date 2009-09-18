/****************************************************************************/
/* Main panel of KaDeploy window                                            */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: MainPanel.java,v 1.12 2007/10/08 14:53:58 dloureir Exp $
 * $Log: MainPanel.java,v $
 * Revision 1.12  2007/10/08 14:53:58  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.11  2007/09/28 16:03:02  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.10  2007/07/12 15:26:34  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.9  2007/04/05 07:58:18  dloureir
 * Correcting a bug concerning the selection of all or none nodes
 *
 * Revision 1.8  2007/04/02 10:01:26  dloureir
 * Removing from the list the waiting jobs
 *
 * Revision 1.7  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.5  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.4  2007/02/23 13:14:44  aamar
 * Changing the manipulated kadeploy environment objects from Kadeploy to
 * KaenvironmentHelper.
 *
 * Revision 1.3  2007/02/23 10:37:50  aamar
 * Adding cancel button processing.
 *
 * Revision 1.2  2007/02/22 15:14:18  aamar
 * Correct header.
 *
 ****************************************************************************/

package diet.gridr.g5k.gui.kadeploy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import java.util.*;
import java.util.logging.Level;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

import diet.grid.api.GridJob;

/**
 * Main panel of KaDeploy window
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class MainPanel extends JPanel implements ActionListener {
	/**
	 * parent dialog frame
	 */
	private KadeployDlg myParent;
	/**
	 * selection mode panel
	 */
	private SelectionModePanel mySelectionModePanel;
	/**
	 * panel for the kernels
	 */
	private KernelPanel myKernelPanel;
	/**
	 * panel for the buttons
	 */
	private BtnPanel myBtnPanel;
	/**
	 * tree of clusters and nodes
	 */
	private JTree myTree;
	/**
	 * Root node of the tree
	 */
	private DefaultMutableTreeNode rootNode;
	/**
	 * Nodes of the clusters
	 */
	private Vector<ClusterCheckNode> clusterNodes;
	/**
	 * The contextual menu for ClusterCheckNode
	 */
	private JPopupMenu myClusterPopup;
	/**
	 * MenuItem used to create other sub deployment
	 */
	private JMenuItem createSubDeploy;

	/**
	 * The selected cluster
	 */
	private ClusterCheckNode selectedNode;

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7763505254497247903L;

	/**
	 * Default constructor
	 *
	 * @param parent parent dialog frame
	 * @param kadeployJobs jobs reserved on the kaDeploy queue
	 * @param kadeployEnvs KaDeploy environments
	 */
	public MainPanel(KadeployDlg parent,
			Map<OarJobKey, GridJob> kadeployJobs,
			Map<Integer, KaenvironmentHelper> kadeployEnvs) {
		super(new BorderLayout());
		this.myParent = parent;
		this.rootNode = new DefaultMutableTreeNode("Sites");

		this.clusterNodes = new Vector<ClusterCheckNode>();
		Iterator<OarJobKey> p = kadeployJobs.keySet().iterator();
		int ix = 0;
		while (p.hasNext()) {
			OarJobKey key = (OarJobKey)(p.next());
			GridJob oarJob = kadeployJobs.get(key);
			if(!oarJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE).substring(0, 1).equalsIgnoreCase("w")){

				/*
				 this.clusterNodes.add(new ClusterCheckNode( G5kCluster.getNameForIndex(key.getCluster()),
						key.getCluster() ));
						*/
                this.clusterNodes.add(new ClusterCheckNode( G5kSite.getSiteForIndex(key.getCluster()),
                        key.getCluster() ));
				this.rootNode.add(clusterNodes.get(ix));

				String[] hosts = oarJob.getHostsAsArray();

				for (int jx=0; jx<hosts.length; jx++) {
					HostCheckNode node = new HostCheckNode(hosts[jx], key.getCluster());
					this.clusterNodes.get(ix).add(node);
				} // end for jx

				ix++;
			}
		} // end for ix
		this.myTree = new JTree(this.rootNode);
		this.myTree.setCellRenderer(new CheckNodeRenderer());
		this.myTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION
		);
		this.myTree.putClientProperty("JTree.lineStyle", "Angled");
		this.myTree.addMouseListener(new NodeSelectionListener(this, this.myTree));


		JScrollPane sp = new JScrollPane(this.myTree);
		add(sp, BorderLayout.CENTER);

		this.mySelectionModePanel = new SelectionModePanel(this);
		this.myKernelPanel = new KernelPanel(this, kadeployEnvs);
		this.myBtnPanel = new BtnPanel(this, BtnPanel.DEPLOY_CMD);
		SelectionPanel selectionPanel = new SelectionPanel(this, this.mySelectionModePanel, this.myKernelPanel,
				this.myBtnPanel);
		add(selectionPanel, BorderLayout.EAST);

		this.myClusterPopup = new JPopupMenu();
		this.createSubDeploy = new JMenuItem("Create a new deployment with free nodes");
		this.createSubDeploy.addActionListener(new NewDeployment());
		this.myClusterPopup.add(this.createSubDeploy);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "MainPanel", "MainPanel created");
	}

	/**
	 * Method returning the selection mode
	 *
	 * @return the selection mode
	 */
	public int getSelectionMode() {
		return this.mySelectionModePanel.getSelectionMode();
	}

	/**
	 * Method setting the selection mode
	 *
	 * @param mode the selection mode
	 */
	protected void setSelectionMode(int mode) {
		Enumeration childs = this.rootNode.children();
		while (childs.hasMoreElements()) {
			Object obj = childs.nextElement();
			if (obj instanceof CheckNode) {
				LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setSelectionMode", "setSelectionMode " + mode);
				((CheckNode)obj).setSelectionMode(mode);
			}
		}
	}

	protected void selectAllNodes(boolean yesWeSelectAllNodes){
	    Enumeration childs = this.rootNode.children();
	    while (childs.hasMoreElements()) {
	        Object obj = childs.nextElement();
	        if (obj instanceof CheckNode) {
	            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setSelectionMode", "selctAlNodes " + yesWeSelectAllNodes);
	            ((CheckNode)obj).setSelected(yesWeSelectAllNodes);
	        }
	    }
        updateUI();
        repaint();
	}


	/**
	 * Method managing the events of this panel
	 *
	 * @param event the event to manage
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals(BtnPanel.DEPLOY_CMD)) {
			Vector<KadeployDeployment> deployments = new Vector<KadeployDeployment>();
			for (int ix=0; ix<this.clusterNodes.size(); ix++) {
				KadeployDeployment adeployment = new KadeployDeployment(this.clusterNodes.get(ix).toString(),
						this.clusterNodes.get(ix).getCluster());
				CheckNode [] tab = this.clusterNodes.get(ix).getSelectedNode();
				for (int jx=0; jx<tab.length; jx++) {
					adeployment.addHost(tab[jx].toString());
				} // end for jx
				deployments.add(adeployment);
			}
			(new KadeployProgressDlg(this.myParent, deployments)).setVisible(true);
		} // end if DEPLOY_CMD
		if (event.getActionCommand().equals(BtnPanel.CANCEL)) {
			this.myParent.dispose();
		}
	}

	/**
	 * Method setting the selected node
	 *
	 * @param node the selected node
	 */
	public void setSelectedNode(ClusterCheckNode node) {
		this.selectedNode = node;
	}

	/**
	 * Method used to show a popup
	 *
	 * @param component the component in which the popup is displayed
	 * @param x x component
	 * @param y y component
	 */
	public void showPopup(Component component, int x, int y) {
		this.myClusterPopup.show(component, x, y);
	}

	/**
	 * Implementation of the Mouse adapter
	 *
	 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
	 *
	 */
	class TreeMouseAdapter extends MouseAdapter {
		/**
		 * action when the mouse is pressed
		 */
		public void mousePressed( MouseEvent event ) {
			handlePopup( event );
		}
		/**
		 * action when the mouse is Released
		 */
		public void mouseReleased( MouseEvent event ) {
			handlePopup( event );
		}
		/**
		 * Method handling the popup
		 *
		 * @param event the event
		 */
		private void handlePopup( MouseEvent event ) {
			if ( event.isPopupTrigger() ) {
				selectedNode = null;
				TreePath path = myTree.getPathForLocation( event.getX(), event.getY() );
				if ( path != null ) {
					myTree.getSelectionModel().setSelectionPath( path );
					TreeNode node = (TreeNode) path.getLastPathComponent();
					if (node instanceof ClusterCheckNode) {
						selectedNode = (ClusterCheckNode) node;
						myClusterPopup.show( event.getComponent(), event.getX(), event.getY() );
					} // end if instanceof ClusterCheckNode
				} // end if path != null
			} // end if isPopupTrigger
		} // end void handlePopup
	} // end class TreeMouseAdapter

	/**
	 * Method that does something ... but I don't know what ???
	 *
	 * @param deploymentName a deployment name
	 *
	 * @return something ...
	 */
	public static String newDeploymentName(String deploymentName) {
		String suffix = "";
		String prefix = "";
		if (deploymentName.lastIndexOf('#') != -1) {
			suffix = deploymentName.substring(deploymentName.lastIndexOf('#') +1 );
			prefix = deploymentName.substring(0, deploymentName.lastIndexOf('#'));
		}
		else {
			suffix = "01";
			prefix = deploymentName;
		}
		int v = 0;
		try {
			v = Integer.parseInt(suffix);
		}
		catch (NumberFormatException e) {
			v = 99;
		}
		return (prefix + "#" + v);
	} // end newDeploymentName

	/**
	 * Class implementing the ActionListener
	 *
	 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
	 *
	 */
	public class NewDeployment implements ActionListener{
		/**
		 * Method handling the action event
		 */
		public void actionPerformed(ActionEvent e) {
			if (selectedNode != null) {
				// Get the non selected nodes
				Vector<HostCheckNode> nodes = selectedNode.getNonSelectedNodes();

				// Create a new deployment based on the selected node name
				// The name  pattern is deployment#number
				String parentName = selectedNode.toString();
				String deploymentName = newDeploymentName(parentName);

				ClusterCheckNode newDeploymentNode = new ClusterCheckNode(deploymentName,
						selectedNode.getCluster());

				// Add the non selected nodes to the new deployment
				for (int ix=0; ix<nodes.size(); ix++) {
					selectedNode.remove(nodes.get(ix));
					newDeploymentNode.add(nodes.get(ix));
				}
				// Add the new deployment to the tree
				rootNode.add(newDeploymentNode);
				clusterNodes.add(newDeploymentNode);
			}
		}
	}
}