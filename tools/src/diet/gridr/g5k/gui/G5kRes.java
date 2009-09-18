/****************************************************************************/
/* G5K tool main window                                                     */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kRes.java,v 1.73 2007/12/07 08:03:14 dloureir Exp $
 * $Log: G5kRes.java,v $
 * Revision 1.73  2007/12/07 08:03:14  dloureir
 * Adding the connection used to get information from Grid'5000 in order to get the time on the grid in the TimePanel
 *
 * (part of the resolution of the bug #47)
 *
 * Revision 1.72  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.71  2007/11/07 11:38:25  dloureir
 * Removing the setResizable call
 *
 * Revision 1.70  2007/11/07 11:35:01  dloureir
 * Some printlns are passed to the LoggingManager
 *
 * Revision 1.69  2007/11/07 08:29:05  dloureir
 * Little bug correction
 *
 * Revision 1.68  2007/11/06 13:12:43  dloureir
 * Adding a currentAnimator for settings message during information retrieving
 *
 * Revision 1.67  2007/10/30 14:11:39  dloureir
 * Integration of the Ganglia plugin
 *
 * Revision 1.66  2007/10/24 12:51:05  dloureir
 * Removing some calls to the G5KCluster class by the corresponding calls to the G5KSite class
 *
 * Revision 1.65  2007/10/12 14:00:03  dloureir
 * Changing the import of some classes that have been moved for compilation purpose
 *
 * Revision 1.64  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.63  2007/10/08 08:15:30  dloureir
 * Adding a jbuttonbar for the jobpanel
 *
 * Revision 1.62  2007/10/05 15:18:38  dloureir
 * Some syntaxic corrections and the modification of the divider location
 *
 * Revision 1.61  2007/10/05 14:54:34  aamar
 * Correcting bugs of saving job reservations.
 *
 * Revision 1.60  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.59  2007/07/18 14:07:47  dloureir
 * The configuration of GRUDU has now only two buttons for the saving of the modified (or not) configuration and the correction of a little bug : the JList of theUserPanel is composed of external acces frontales whereas it was not the case before (it was site's names).
 *
 * Revision 1.58  2007/07/17 10:19:53  dloureir
 * Adding the application settings for GRUDU and the JavaHelp for GRUDU won't depend on the application context now.
 *
 * Revision 1.57  2007/07/16 14:21:09  dloureir
 * Some additions for the JFTP module of GRUDU
 *
 * Revision 1.56  2007/07/12 14:09:57  dloureir
 * Some code clean up and javadoc for undocumented methods or classes.
 *
 * Revision 1.55  2007/07/11 15:40:25  dloureir
 * Adding an application package for the application wide properties and two panels for the Tips of the Day for DDB and GRUDU
 *
 * Revision 1.54  2007/07/11 08:35:03  dloureir
 * Adding an SSHAgent for the correction of bug 36. The ssh key is nomore written on the disk. It will be ask at every application startup and stored in memory during the execution.
 *
 * Revision 1.53  2007/07/06 08:27:04  dloureir
 * Changing the JToolBars of the different exisiting modules of the DDB for a sexiest one (see diet.util.gui.DDBJToolBar)
 *
 * Revision 1.52  2007/06/25 09:52:45  dloureir
 * Adding a feature : the user can now provide a scriptto launch when the reservation comes up (the path should be absolute)
 *
 * Revision 1.51  2007/06/22 11:53:50  dloureir
 * Corrections for bugs :
 *
 *  - bug 37 : It was about the wrong number of nodes on Nancy. In fact, it was a problem of properties when reserving some nodes. Now you have a extra panel in the reservation frame that allows you to specify some properties for each clusters.
 *    * G5KRes : Adding  the propertiesHashMap in the constructor call of the OAROpRunnable
 *    * OAROpRunnable : Adding an attribute (properties), and changing the constructor and the call to the oarsub method of the OARUtil class.
 *    * ResaDlg : Adding a propertiesPanel as an internal class. There is now two tabs (one for the basic and one for the properties of the reservation)
 *    * G5KReservation : Adding a propertiesHashMap and the associated getter/setter
 *    * OARUtil : Adding the properties when call the oarsub
 *
 *  - bug 39 : Allows the OAR grid sub behaviour when reserving (It means that when you want to reserve on the grid, if one of the reservation fails, all the already realized jobs are deleted). The choice is realized at the reservation time be checking/uncheking a checkbox in the reservation panel.
 *    * G5KRes : Adding a hashmap for the conservation of the ids of jobs submitted with success in order to delete them if their is one job not successfully submitted in the same "reservation" when the oargridsub behaviour is activated
 *    * OAROPRunnable : conservation of the id job, and getter/setter for that idjob
 *    * ResaDlg : Adding a checkbox for the selection of the oargridsub behaviour, and adding this behaviour to the reservation
 *    * G5KReservation : Boolean for the oargridsub behaviour with the associated getter and setter
 *    * OARUtil :  oarsub now returns the job id (when it is submitted successfully or not). When the job is not submitted successfully, the job id is lower than zero.
 *
 *  - bug 40 : To solve the lack of reservation status after the submission, a reservation status frame has been added. It displays the statuses of the different submissions realized by this reservation.
 *    * G5KRes : Adding the call to the ReservationStatusFrame constructor that displays the status frame
 *    * ReservationStatusFrame : frame for the status of the reservation that displays the information about the different submissions.
 *
 * Revision 1.50  2007/05/14 14:22:55  dloureir
 * Adding the help for grudu and changing the link for the help of the DashBoard
 *
 * Revision 1.49  2007/03/26 20:39:21  dloureir
 * Updating the call of the ClusterInfoPanel constructor with the addition of the ssh connection
 *
 * Revision 1.48  2007/03/15 09:40:36  dloureir
 * Correcting a bug concerning the jobId of the OAR jobs and allowing an acces to the nodes with an good OAR environment
 *
 * Revision 1.47  2007/03/14 09:13:47  dloureir
 * Correcting a bug concerning the refeshing of the cluster view
 *
 * Revision 1.46  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.45  2007/03/07 15:00:14  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.44  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.43  2007/03/05 12:17:50  dloureir
 * Chaging timeout value for cluster autorefreshing to 5 minutes
 *
 * Revision 1.42  2007/03/01 12:00:04  dloureir
 * Correcting bug : problems with messages when animations are running
 *
 * Revision 1.41  2007/02/27 16:59:04  dloureir
 * removing unused imports
 *
 * Revision 1.40  2007/02/27 14:10:06  dloureir
 * Adding sol cluster of sophia and removing some print outs
 *
 * Revision 1.39  2007/02/23 13:15:29  aamar
 * Changing manipulated kadeploy environment objects from Vecto<KadeployEnv>
 * to KaenvironmentHelper.
 *
 * Revision 1.38  2007/02/22 15:45:56  aamar
 * Minor change.
 *
 * Revision 1.37  2007/02/22 15:42:20  aamar
 * Changing the KadeployDlg base class.
 *
 * Revision 1.36  2007/02/22 15:10:52  aamar
 * Some changes.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.jftp.JFtp;

import com.birosoft.liquid.LiquidLookAndFeel;
import com.jcraft.jcterm.JCTerm;
import com.trilead.ssh2.Connection;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import diet.application.ApplicationConfiguration;
import diet.application.settings.SettingsFrame;
import diet.application.totd.GRUDUTipOfTheDay;
import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;
import diet.util.gui.DDBJToolBar;
import diet.util.gui.DietGuiTool;
import diet.util.gui.GuiThread;
import diet.util.gui.WaitingDlg;
import diet.util.gui.WaitingFrame;
import diet.gridr.g5k.gui.kadeploy.KadeployDlg;

import diet.grid.api.GridJob;
import diet.grid.BatchSchedulerMgr;
import diet.grid.api.util.DefaultBatchSchedulerListener;

/**
 * G5K tool main window
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kRes extends JFrame
    implements TreeSelectionListener, ActionListener, DietGuiTool{
    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = -300024249439407577L;

    /**
     * The grid5000 sites icons
     */
    private Icon [] sitesIcons;

    /**
     * The root node of sites tree
     */
    private IconNode rootNode;

    /**
     * The grid5000 sites tree nodes
     */
    private IconNode [] siteNodes;

    /**
     * The grid5000 sites tree
     */
    private JTree sitesTree;

    /**
     * The toolbar buttons
     */
    private JButton connect_btn, allocate_btn, refresh_btn, settings_btn, view_btn, disconnect_btn,
	shell_btn, kadeploy_btn, jftp_btn, applicationSettings_btn;

    /**
     * The ssh session
     */
    private Connection myConn;

    /**
     * The toolbar
     */
    private DDBJToolBar myToolBar;

    /**
     * The jobs map
     */
    private Vector<HashMap<String, JobTreeNode>> jobsMap;

    /**
     * The info panel
     */
    InfoPanel myInfoPanel;

    /**
     * The popup menu for the deletion of a Job
     */
    JPopupMenu myPopupMenu;
    /**
     * the menu item for the deletion of a job
     */
    JMenuItem delItem;

    /**
     * The popup menu for the updating of a cluster view and the deletion of
     * every user's jobs on the cluster
     */

    JPopupMenu myClusterPopupMenu;
    /**
     * The menu item for deletion of all jobs of a cluster
     */
    JMenuItem delClustersJobs;
    /**
     * The menu item for the updating of all jobs of a cluster
     */
    JMenuItem updateClustersJobs;

    /**
     * The popup menu for the updating and the deleting of jobs on the entire grid
     */
    JPopupMenu myG5kPopupMenu;
    /**
     * menu item for the deletion of all jobs on grid5000
     */
    JMenuItem delG5kJobs;
    /**
     * menu item for the updating of the Grid5000 jobs
     */
    JMenuItem updateG5kJobs;

    /**
     * The selected node if it is a JobTreeNode;
     */
    JobTreeNode selectedNode;
    /**
     * the selected node if it is a IconNode;
     */
    IconNode selectedSiteNode;

    /**
     * Waiting dialog frame
     */
    WaitingDlg myWaitDlg;

    /**
     * boolean value defining if the waiting dialog frame should be seen or not
     */
    private boolean viewSummaryDlgVisibleState = true;
    /**
     * ViewSummary dialog frame from which we can extract data about Grid5000
     */
    private ViewSummaryDlg viewSummaryDlg;
    /**
     * Animator panel for the update action
     */
    private AnimatedPanel updatingAnimator = null;
    /**
     * Animator panel for the view action
     */
    private AnimatedPanel viewingAnimator = null;
    /**
     * Animator panel for the reserve action
     */
    private AnimatedPanel reservingAnimator = null;
    /**
     * Animator panel for the refresh action
     */
    private AnimatedPanel refreshingAnimator = null;
    /**
     * Animator panel for the delete action
     */
    private AnimatedPanel deletingAnimator = null;
    /**
     * Animator panel for the information summarization
     */
    private AnimatedPanel informationSummaryAnimator = null;
    /**
     * Current Animator
     */
    private AnimatedPanel currentAnimator = null;
    /**
     * JPanel for the cardLayout where information will be displayed
     */
    private JPanel cardPanel = null;
    /**
     * CardLayout for the display for different informations
     */
    private CardLayout carder = null;
    /**
     * HashMpa containing the shell properties
     */
    private HashMap<String, String> shellProperties = new HashMap<String, String>();
    /**
     * menuItem for the opening of a shell on a cluster
     */
    private JMenuItem openAShellOnCluster = null;
    /**
     * menuItem for the opening of a shell on a job
     */
    private JMenuItem openAShellOnJob = null;
    /**
     * boolean status specifying that a popup is triggered
     */
    private boolean isPopupTriggered = false;
    /**
     * array containing the last cluster Summary date for each cluster
     */
    private long[] lastSiteSummaryDate;
    /**
     * array containing the last cluster jobs date for each cluster
     */
    private long[] lastSiteJobsDate;
    /**
     * delay between two updates for the clusters information
     */
    private static final long delayForUpdateClusterInformation = 300000;
    /**
     * map containing the jobs of the clusters
     */
    private Vector<Map<String,GridJob>> clusterJobsMap = new Vector<Map<String,GridJob>>(G5kSite.getSitesNumber());
    /**
     * Vector containing arrays of nodes status
     */
    private Vector<int[]> clusterNodesStatusArray = new Vector<int[]>(G5kSite.getSitesNumber());
    /**
     * Help button but only for GRUDU
     */
    private JButton helpButton = null;
    
    /**
     * default constructor of the G5kRes
     *
     */
    public G5kRes() {
		super("Grid5000 resources management tool");

		if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.GRUDU_CONTEXT)){
			LoggingManager.getInstance();
		}

		// Read the configuration file
		G5kCfg.initCfg();
		Config.init();
		LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kRes", "Configuration values initialized");
		initializeJobsMaps();
		initializeClusterInformationsData();

		// Init the g5k tree
		initTree();
		this.myPopupMenu = new JPopupMenu();
		this.delItem = new JMenuItem("delete");
		this.delItem.addActionListener(new DeletingAction());
		this.openAShellOnJob = new JMenuItem("open a shell");
		this.openAShellOnJob.addActionListener(new OpeningShellActionOnJob());
		this.myPopupMenu.add(this.delItem);
		this.myPopupMenu.add(this.openAShellOnJob);

		this.myClusterPopupMenu = new JPopupMenu();
		this.delClustersJobs = new JMenuItem("delete all your jobs");
		this.delClustersJobs.addActionListener(new DeletingClusterAction());
		this.updateClustersJobs = new JMenuItem("update the cluster view");
		this.updateClustersJobs.addActionListener(new RefreshClusterAction());
		this.openAShellOnCluster = new JMenuItem("open a shell");
		this.openAShellOnCluster.addActionListener(new OpeningShellActionOnCluster());
		this.myClusterPopupMenu.add(updateClustersJobs);
		this.myClusterPopupMenu.add(delClustersJobs);
		this.myClusterPopupMenu.add(openAShellOnCluster);

		this.myG5kPopupMenu = new JPopupMenu();
		this.updateG5kJobs = new JMenuItem("update the grid view");
		updateG5kJobs.addActionListener(new UpdateAction());
		this.delG5kJobs = new JMenuItem("delete all your jobs");
		this.delG5kJobs.addActionListener(new DeleteG5kAction());
		this.myG5kPopupMenu.add(updateG5kJobs);
		this.myG5kPopupMenu.add(delG5kJobs);
		this.sitesTree.addMouseListener(new TreeMouseAdapter());
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kRes", "Tree Beahviour initialized (PopupMenus and actions)");
		// init the information panel
		this.myInfoPanel = new InfoPanel();


		// add the components
		JScrollPane pane1 = new JScrollPane(this.sitesTree);
		JScrollPane pane2 = new JScrollPane(this.myInfoPanel);

		updatingAnimator = new AnimatedPanel("Updating the view, please wait ...",new ImageIcon(getClass().getResource("/resources/gridr/g5k/g5kImageWait.png")));
		viewingAnimator = new AnimatedPanel("Preparing the view, please wait ...",new ImageIcon(getClass().getResource("/resources/gridr/g5k/g5kImageWait.png")));
		reservingAnimator = new AnimatedPanel("Reserving resources, please wait ...",new ImageIcon(getClass().getResource("/resources/gridr/g5k/g5kImageWait.png")));
        deletingAnimator = new AnimatedPanel("Deleting jobs, please wait ...", new ImageIcon(getClass().getResource("/resources/gridr/g5k/g5kImageWait.png")));
        refreshingAnimator = new AnimatedPanel("Refreshing the view, please wait ...",new ImageIcon(getClass().getResource("/resources/gridr/g5k/g5kImageWait.png")));
        informationSummaryAnimator = new AnimatedPanel("Summarizing Information, please wait ...", new ImageIcon(getClass().getResource("/resources/gridr/g5k/g5kImageWait.png")));

		cardPanel = new JPanel();
		carder = new CardLayout();
		cardPanel.setLayout(carder);
		cardPanel.add("Job", pane2);
		cardPanel.add("Update", updatingAnimator);
		cardPanel.add("View",viewingAnimator);
		cardPanel.add("Reserve",reservingAnimator);
        cardPanel.add("Refresh",refreshingAnimator);
        cardPanel.add("Delete",deletingAnimator);
        cardPanel.add("Information", informationSummaryAnimator);
		carder.show(cardPanel, "Job");
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				pane1,
				cardPanel);
		splitPane.setDividerLocation(0.3);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kRes", "SplitPane and animator initialized");
		this.myToolBar = initToolBar();
		getContentPane().add(this.myToolBar, BorderLayout.NORTH);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kRes", "Resources Tool GUI initialized");
		//pack();
		if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.GRUDU_CONTEXT)){

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			addWindowListener(new WindowAdapter(){

				/* (non-Javadoc)
				 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
				 */
				@Override
				public void windowClosing(WindowEvent e) {
					LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "windowClosing", "Resources Tool is closing");
					LoggingManager.close();
				}
			});
		}else{
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addWindowListener(new WindowAdapter(){

				/* (non-Javadoc)
				 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
				 */
				@Override
				public void windowClosing(WindowEvent e) {
					LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "windowClosing", "Resources Tool is closing");
				}
			});
		}
	}

    /**
     * Method initializing the G5k Tree
     *
     */
	public void initTree() {
		this.sitesIcons = new ImageIcon[G5kSite.getSitesNumber()];
		for (int ix=0; ix<this.sitesIcons.length; ix++) {
			URL url = getClass().getResource("/resources/gridr/g5k/"+ G5kConstants.sites.get(ix).getName()+".png");
			if (url != null) {
				this.sitesIcons[ix] = new ImageIcon(url);
			}
			else
				this.sitesIcons[ix] = null;
		}

		this.rootNode = new IconNode("G5K", true, null);

		this.siteNodes = new IconNode[G5kSite.getSitesNumber()];
		for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
			this.siteNodes[ix] = new IconNode(G5kConstants.sites.get(ix).getName(),
					true,
					this.sitesIcons[ix]);
			rootNode.add(this.siteNodes[ix]);
		}
		this.sitesTree = new JTree(this.rootNode);
		this.sitesTree.addTreeSelectionListener(this);
		this.sitesTree.setCellRenderer(new IconNodeRenderer());
//		this.sitesTree.setMinimumSize(new Dimension(200,600));
//		this.sitesTree.setSize(new Dimension(200,600));
//		this.sitesTree.setPreferredSize(new Dimension(200,600));
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "initTree", "Cluster Tree initialized");
	}

    /**
     * Method initializing the JToolBar
     *
     * @return the initialized JToolBar
     */
	public DDBJToolBar initToolBar() {
		DDBJToolBar toolBar = new DDBJToolBar(DDBJToolBar.HORIZONTAL);
		URL url = getClass().getResource("/resources/gridr/g5k/connect.png");
		if (url != null)
			this.connect_btn = new JButton(new ImageIcon(url));
		else
			this.connect_btn = new JButton("connect");
		this.connect_btn.setToolTipText("Connection to Grid 5000");
		connect_btn.setFocusable(false);
		url = getClass().getResource("/resources/gridr/g5k/allocate.png");
		if (url != null)
			this.allocate_btn = new JButton(new ImageIcon(url));
		else
			this.allocate_btn = new JButton("allocate");
		this.allocate_btn.setToolTipText("Reservation of ressources on Grid 5000");
		allocate_btn.setFocusable(false);
		url = getClass().getResource("/resources/gridr/g5k/refresh.png");
		if (url != null)
			this.refresh_btn = new JButton(new ImageIcon(url));
		else
			this.refresh_btn = new JButton("refresh");
		this.refresh_btn.setToolTipText("Refresh the reservations in the cluster's tree");
		refresh_btn.setFocusable(false);
		url = getClass().getResource("/resources/gridr/g5k/settings.png");
		if (url != null)
			this.settings_btn = new JButton(new ImageIcon(url));
		else
			this.settings_btn = new JButton("settings");
		this.settings_btn.setToolTipText("Settings : used clusters, environnements ...");
		settings_btn.setFocusable(false);
		url = getClass().getResource("/resources/gridr/g5k/view_res.png");
		if (url != null)
			this.view_btn = new JButton(new ImageIcon(url));
		else
			this.view_btn = new JButton("View my ressources");
		this.view_btn.setToolTipText("Print the available and reserved ressources");
		view_btn.setFocusable(false);
		url = getClass().getResource("/resources/gridr/g5k/connect_no.png");
		if (url != null)
			this.disconnect_btn = new JButton(new ImageIcon(url));
		else
			this.disconnect_btn = new JButton("disconnect");
		this.disconnect_btn.setToolTipText("Disconnect from Grid 5000");
		disconnect_btn.setFocusable(false);
		url = getClass().getResource("/resources/gridr/g5k/Terminal.png");
		if(url != null){
			this.shell_btn = new JButton(new ImageIcon(url));
		}
		else{
			this.shell_btn = new JButton("Open a shell");
		}
		this.shell_btn.setToolTipText("Open a shell on the connection frontal");
		shell_btn.setFocusable(false);
		url = getClass().getResource("/resources/gridr/g5k/kadeploy.png");
		if (url != null)
			this.kadeploy_btn = new JButton(new ImageIcon(url));
		else
			this.kadeploy_btn = new JButton("Kadeploy");
		this.kadeploy_btn.setToolTipText("Deploy kernel images using Kadeploy");
		kadeploy_btn.setFocusable(false);
		
		boolean classLoadedWithSuccess = false;
		
		try {
			Class.forName("net.sf.jftp.JFtp");
			classLoadedWithSuccess = true;
		} catch (ClassNotFoundException e) {
		}
		if(classLoadedWithSuccess){
			url = getClass().getResource("/resources/gridr/g5k/jftp.png");
			if (url != null) jftp_btn = new JButton(new ImageIcon(url));
			else jftp_btn = new JButton("JFTP");
			jftp_btn.setToolTipText("File transfert manager for Grid'5000");
			jftp_btn.setEnabled(false);
			jftp_btn.setFocusable(false);
		}
		
        url = getClass().getResource("/resources/gridr/g5k/help.png");
        if (url != null)
            this.helpButton = new JButton(new ImageIcon(url));
        else
            this.helpButton = new JButton("Help");
        this.helpButton.setToolTipText("Help for GRUDU");
        helpButton.addActionListener(new HelpAction());
		
        if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.GRUDU_CONTEXT)){

            url = getClass().getResource("/resources/gridr/g5k/applicationSettings.png");
            if (url != null) applicationSettings_btn = new JButton(new ImageIcon(url));
            else applicationSettings_btn = new JButton("Application Settings");
            applicationSettings_btn.setToolTipText("Application wide settings of GRUDU");
            applicationSettings_btn.addActionListener(this);
        }
        
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "initToolBar", "ToolBar buttons created");

		this.shell_btn.addActionListener(new OpeningShellAction());
		// Add the buttons listeners;
		this.connect_btn.addActionListener(new UpdateAction());
		this.allocate_btn.addActionListener(new ReservationAction());
		this.refresh_btn.addActionListener(new UpdateAction());
		this.settings_btn.addActionListener(this);
		this.view_btn.addActionListener(new ViewAction());
		this.kadeploy_btn.addActionListener(new KadeployAction());
		this.disconnect_btn.addActionListener(this);
		if(jftp_btn!= null) this.jftp_btn.addActionListener(new JFTPAction());
		// listeners for repainting of the toolbar
		this.shell_btn.addMouseMotionListener(toolBar);
		this.connect_btn.addMouseMotionListener(toolBar);
		this.allocate_btn.addMouseMotionListener(toolBar);
		this.refresh_btn.addMouseMotionListener(toolBar);
		this.settings_btn.addMouseMotionListener(toolBar);
		this.view_btn.addMouseMotionListener(toolBar);
		this.kadeploy_btn.addMouseMotionListener(toolBar);
		this.disconnect_btn.addMouseMotionListener(toolBar);
		if(jftp_btn!= null) jftp_btn.addMouseMotionListener(toolBar);
		if(applicationSettings_btn!= null) applicationSettings_btn.addMouseMotionListener(toolBar);
		// disable the allocate and refresh buttons before connect
		this.allocate_btn.setEnabled(false);
		this.refresh_btn.setEnabled(false);
		this.view_btn.setEnabled(false);
		this.shell_btn.setEnabled(false);
		this.kadeploy_btn.setEnabled(false);
		if(jftp_btn!= null) this.jftp_btn.setEnabled(false);
		if(applicationSettings_btn!= null) applicationSettings_btn.setEnabled(true);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "initToolBar", "ToolBar buttons behaviour initialized");

		// Add the button to the toolbar
		toolBar.add(this.connect_btn);
		toolBar.add(this.allocate_btn);
		toolBar.add(this.refresh_btn);
		toolBar.add(this.settings_btn);
		toolBar.add(this.view_btn);
		toolBar.add(this.shell_btn);
		toolBar.add(this.kadeploy_btn);
		if(jftp_btn!= null) toolBar.add(jftp_btn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(helpButton);
        
        if(applicationSettings_btn!= null) toolBar.add(applicationSettings_btn);
		

		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "initToolBar", "ToolBar buttons added");
		return toolBar;
	}

    /**
     * Method initializing the jobsMap
     *
     */
	private void initializeJobsMaps(){
		this.jobsMap = new Vector<HashMap<String, JobTreeNode>>();
		for (int ix=0; ix<G5kSite.getSitesNumber(); ix++)
			this.jobsMap.add(ix, new HashMap<String, JobTreeNode>());
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL , this.getClass().getName(), "initializeJobsMaps", "JobsMaps initialized");
	}

    /**
     * Method that manage the events happening to this frame
     */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.disconnect_btn) {
			this.myToolBar.remove(disconnect_btn);
			this.myToolBar.add(connect_btn,0);
			this.myConn.close();
			this.connect_btn.setEnabled(true);
			this.allocate_btn.setEnabled(false);
			this.refresh_btn.setEnabled(false);
			this.view_btn.setEnabled(false);
			this.shell_btn.setEnabled(false);
			if(jftp_btn!= null) this.jftp_btn.setEnabled(false);
			this.kadeploy_btn.setEnabled(false);

			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "State of the buttons modified");

			for (int ix=0; ix<this.siteNodes.length; ix++)
				this.siteNodes[ix].removeAllChildren();
			this.jobsMap.clear();
			this.sitesTree.updateUI();
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Sites tree cleared");
			this.myInfoPanel.showCleaned();
			URL url = getClass().getResource("/resources/gridr/g5k/connect.png");
			this.connect_btn.setIcon(new ImageIcon(url));
			this.myToolBar.updateUI();
			this.myConn = null;
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Toolbar reinitialized");
			LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL,this.getClass().getName(), "actionPerformed", "Connection closed");
		} // end if disconnect

		if (event.getSource() == this.settings_btn) {
			// Open the setting dialog
			G5kCfg.initCfg();
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Grid5000 configuration loaded");
			G5kCfgDlg dlg = new G5kCfgDlg();
			dlg.exec();
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Grid5000 configuration frame opened");
		}
		if(event.getSource() == this.applicationSettings_btn){
    		SwingUtilities.invokeLater(new Runnable(){
    			public void run(){
    				LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching Application settings frame");
    				GuiThread thread = new GuiThread(new SettingsFrame());
    				thread.start();
    			}
    		});
		}
		myToolBar.repaint();
	}

    /**
     * Method getting my jobs that are running
     *
     * @return the jobs map of my jobs that are running
     */
	private Map<Integer, Vector<GridJob>> getMyJobs() {
		Map<Integer, Vector<GridJob>> v = new HashMap<Integer, Vector<GridJob>>();
		for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
			Vector<GridJob> siteV = new Vector<GridJob>();
			v.put(new Integer(ix), siteV);
			Map<String, JobTreeNode> siteMap = this.jobsMap.get(ix);
			for (Iterator p = siteMap.values().iterator();
			    p.hasNext();
			) {
				    GridJob j = ((JobTreeNode)p.next()).getJob();
				if (j.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equals(Config.getUser()) &&
						j.getParameterValue(GridJob.KEY_GRID_JOB_STATE).substring(0, 1).equals("R")) {
					siteV.add(j);
				}
			}
		}
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getMyJobs", "retrieving the jobs of the user");
		return v;
	}

    /**
     * Method returning the map of jobs
     *
     * @return the jobs map
     */
	public Map<Integer, Vector<GridJob>> getMyJobsMap(){
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getMyJobsMap", "retrieving the jobs of the user");

		return getMyJobs();
	}

    /**
     * Method returning the index from a node
     *
     * @param node a node of which we want to know the site
     * @return the site
     */
	private int getSiteByNode(IconNode node) {
		for (int ix=0; ix<this.sitesIcons.length; ix++) {
			if (this.siteNodes[ix] == node){
				LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getMyJobs", "Getting site : " + ix);
				return ix;
			}
		}
		LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getMyJobs", "Unknown node : no site for this node");
		return -1;
	}

    /**
     * Method implementing the TreeSelectionListener interface
     *
     * @param e a TreeSelectionEvent
     */
	public void valueChanged(TreeSelectionEvent e) {
		try{Thread.sleep(100);}catch(Exception ex){
		    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "valueChanged", ex);
        }
		if(!isPopupTriggered){

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
			sitesTree.getLastSelectedPathComponent();

			if (node == null) return;
			if(node.equals(rootNode)){
				if(myConn != null){
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							Thread performer = new Thread(new Runnable() {
								public void run() {
									viewSummaryDlgVisibleState = false;
									LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "valueChanged", "Click on the G5k node");
									performViewing();
									myInfoPanel.setG5KView(viewSummaryDlg.getContentPanel(),viewSummaryDlg.getDataForG5k());
									viewSummaryDlgVisibleState = true;

								}
							}, "Viewer");
							performer.start();
						}
					});
				}

			}
			if (node instanceof JobTreeNode) {
				LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "valueChanged", "Click on the "+node.toString()+" job");
				JobTreeNode treeNode = (JobTreeNode)node;
				String parentSite = node.getParent().toString();
				this.myInfoPanel.showJob(treeNode.getJob(), myConn,parentSite);
			}
			else {
				if(myConn != null){
					int index = isSite(node);
					if (index != -1 ){
						if(Config.isSiteEnable(index)){
							LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "valueChanged", "Click on the "+node.toString()+" cluster");
							summarizeInformationForCluster(index);
						}
					}
				}
			}
		}
	}

    /**
     * Method telling if the node is node site
     *
     * @param node node that we are tested
     * @return if this is a node site or note
     */
	private int isSite(DefaultMutableTreeNode node) {
		for (int ix=0; ix<this.siteNodes.length; ix++) {
			if (node == this.siteNodes[ix])
				return ix;
		}
		return -1;
	}

	/**
     * Class implementing the event management raise by the mouse action
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
	 */
	class TreeMouseAdapter extends MouseAdapter {
        /**
         * Method realizing the action when the mouse is pressed
         */
		public void mousePressed( MouseEvent event ) {
			handlePopup( event );
		}
        /**
         * Method realizing the action when the mouse is released
         */
		public void mouseReleased( MouseEvent event ) {
			handlePopup( event );
		}

        /**
         * Common behaviour for the mouse pressed event, and the mouse released event
         *
         * @param event
         */
		private void handlePopup( MouseEvent event ) {
			if ( event.isPopupTrigger() ) {
				LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "handlePopup", "Handling popup considering the node");
				isPopupTriggered = true;
				TreePath path = sitesTree.getPathForLocation( event.getX(), event.getY() );
				if ( path != null ) {
					sitesTree.getSelectionModel().setSelectionPath( path );
					TreeNode node = (TreeNode) path.getLastPathComponent();
					if (node instanceof JobTreeNode) {
						selectedNode = (JobTreeNode) node;
						if(selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equalsIgnoreCase(Config.getUser())){
								myPopupMenu.show( event.getComponent(), event.getX(), event.getY() );
								LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "handlePopup", "Popup on job " + 
								        selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID));
						}
					}
					else if(node.equals(rootNode)){
						myG5kPopupMenu.show(event.getComponent(), event.getX(), event.getY() );
						LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "handlePopup", "Popup on G5k node");
					}
					else{
						selectedSiteNode = (IconNode) node;
						if(!selectedSiteNode.isLeaf()){
							if(!selectedSiteNode.iconName.equalsIgnoreCase(rootNode.iconName)){
								myClusterPopupMenu.show(event.getComponent(),event.getX(),event.getY());
								LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "valueChanged", "Popup on "+selectedSiteNode.iconName);
							}
						}
					}
				}
				isPopupTriggered = false;
			}
		}

	}

    /**
     * Method setting the jobs
     *
     * @param jobs jobs map
     * @param site site
     * @param dispose boolean value
     */
	public void setJobs(Map<String, GridJob> jobs, int site, boolean dispose) {
		if (site == -1)
			return;
		if(jobsMap.isEmpty()){
			LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setJobs","jobsMap is empty");
			initializeJobsMaps();
		}
		Map<String, JobTreeNode> siteMap = this.jobsMap.get(site);
		for (Iterator p = jobs.values().iterator();
		p.hasNext();
		) {
			GridJob job = (GridJob)p.next();

			if (siteMap.get(job.getParameterValue(GridJob.KEY_GRID_JOB_ID)) == null) {
				JobTreeNode node = new JobTreeNode(job.getParameterValue(GridJob.KEY_GRID_JOB_ID), true, this.sitesIcons[site], job);
				siteMap.put(job.getParameterValue(GridJob.KEY_GRID_JOB_ID), node);
				this.siteNodes[site].add(node);
			}
			else {
				JobTreeNode node = (JobTreeNode)(siteMap.get(job.getParameterValue(GridJob.KEY_GRID_JOB_ID)));
				node.update(job);
			}
		} // end for (in jobs
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setJobs","jobs from site retrieved");
		Vector<String> toRemove = new Vector<String>();
		for (Iterator p = siteMap.values().iterator();
		p.hasNext();
		) {
			JobTreeNode node = (JobTreeNode)(p.next());
			if (jobs.get(node.getJob().getParameterValue(GridJob.KEY_GRID_JOB_ID)) == null) {
				LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setJobs", "Removing job " + node);
				node.removeFromParent();
				toRemove.add(node.getJob().getParameterValue(GridJob.KEY_GRID_JOB_ID));
			}
		}
		for (int ix=0; ix<toRemove.size(); ix++) {
			siteMap.remove(toRemove.get(ix));
		}
		LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setJobs", "jobsMap updated");
	}

	/**
     * Method setting the summary data to the viewSummaryDlg
     *
     * @param data data for Grid5000
     * @param userData data for the user of Grid5000
     * @param reservationMap map of the reserved jobs
	 */
	public void setSummaryData(int [][] data, String [][] userData, HashMap<String,String> reservationMap) {
		viewingAnimator.stop();
		viewSummaryDlg = new ViewSummaryDlg(this, data, userData,reservationMap);
		viewSummaryDlg.pack();
		viewSummaryDlg.setLocationRelativeTo(this);
		viewSummaryDlg.setVisible(viewSummaryDlgVisibleState);
	}

    /**
     * Main method for GRUDU
     *
     * @param args arguments passed to GRUDU (they won't be considered because
     * none are wanted)
     */
	public static void main(String [] args) {
		try {
        	Locale.setDefault(Locale.ENGLISH);
			ApplicationConfiguration.setApplicationContext(ApplicationConfiguration.GRUDU_CONTEXT);
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            LiquidLookAndFeel.setToolbarButtonsFocusable(false);
            SplashScreenFrame.splashImageOnScreen("/resources/gridr/g5k/GRUDU.png");
			G5kRes g5kRes = new G5kRes();
			Dimension dim = new Dimension(1000, 650);
			g5kRes.setSize (dim);
			g5kRes.setPreferredSize(dim);
			g5kRes.setMinimumSize(dim);
			g5kRes.setVisible(true);
            new GRUDUTipOfTheDay();
		}
		catch (Exception e) {
			LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, G5kRes.class.getName(), "main", e);
		}
	}

    /**
     * Method launching the frame and the resource tool
     */
	public void go() {
		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "go", "Resources tool launched");
		Dimension dim = new Dimension(1000, 650);
		setSize (dim);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible (true);

	}

    /**
     * Method saving the reservation in a file given in parameter
     *
     * @param aFile the file where the reservation is saved
     */
	public void saveReservation(File aFile){
		Vector<GridJob> userJobs = new Vector<GridJob>();
		Vector<Integer> jobLoc = new Vector<Integer>();
		HashMap<String, String> reservationMap = new HashMap<String, String>();
		for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
			Map<String, JobTreeNode> siteMap = this.jobsMap.get(ix);
			for (Iterator p = siteMap.values().iterator();
			p.hasNext();
			) {
				GridJob job = ((JobTreeNode)p.next()).getJob();
				if (job.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equals(Config.getUser())) {
					// LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveReservation", "Getting job : " + job.jobName);
				    LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveReservation", "Getting job : " + job.getParameterValue(GridJob.KEY_GRID_JOB_ID));
					userJobs.add(job);
					jobLoc.add(new Integer(ix));
					//reservationMap.put(G5kConstants.clusters.get(ix).getName(), job.getHostsAsString("+"));
					reservationMap.put(G5kSite.getSiteForIndex(ix), job.getParameterValue(GridJob.KEY_GRID_JOB_HOSTS).replace(GridJob.HOSTS_SEPARATOR, "+"));
				}
			}
		} // end for

		String [][] userData = new String[userJobs.size()][6];
		for (int ix=0; ix<userJobs.size(); ix++) {
			// userData[ix][0] = G5kConstants.clusters.get(jobLoc.get(ix).intValue()).getName();
		    userData[ix][0] = G5kSite.getSiteForIndex(jobLoc.get(ix).intValue());
			userData[ix][1] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_ID);
			userData[ix][2] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT);
			userData[ix][3] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_STATE);
			userData[ix][4] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME);
			userData[ix][5] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME);
		}
		viewSummaryDlgVisibleState = false;
		this.myWaitDlg = new WaitingDlg(this,
		"Saving the reservation, please wait");
		OarSummaryRunnable doRun = new OarSummaryRunnable(this, this.myConn, userData,reservationMap);
		Thread th = new Thread(doRun);
		th.start();
		this.myWaitDlg.setLocationRelativeTo(this);
		this.myWaitDlg.setVisible(true);
		try{
		    th.join();
		}
		catch(Exception e){
		    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveReservation", e);
        };
		viewSummaryDlg.saveDirectoryOfReservation(aFile);
		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveReservation", "Reservation saved to File : " + aFile.getName());
		viewSummaryDlgVisibleState = true;
	}

    /**
     * Class implementing the update action
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class UpdateAction implements ActionListener {
        /**
         * Method implementing the main behaviour of the Update Action
         */
        public void actionPerformed(ActionEvent e) {
        	if(e.getSource().equals(connect_btn)){
        		SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				Thread performer = new Thread(new Runnable() {
        					public void run() {
        						performUpdating();
        						if(myConn != null){
        							myToolBar.remove(connect_btn);
        							myToolBar.add(disconnect_btn,0 );
        						}
        					}
        				}, "Updater");
        				performer.start();
        			}
        		});
        	}
        	else{
        		SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				Thread performer = new Thread(new Runnable() {
        					public void run() {
        						performRefreshing();
        					}
        				}, "Refresher");
        				performer.start();
        				
        			}
        		});
        	}
        }
    }

    /**
     * Class implementing the Kadeploy action
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class KadeployAction implements ActionListener {
        /**
         * Method implementing the main behaviour of the Kadeploy Action
         */
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Thread performer = new Thread(new Runnable() {
                        public void run() {
                            performKadeploy();
                        }
                    }, "Kadeploy");
                    performer.start();
                }
            });
            myToolBar.repaint();
        }
    }

    /**
     * Method performing the action for Kadeploy
     *
     */
    protected void performKadeploy() {
        Map<OarJobKey, GridJob>          kadeployJobs = getKadeployResources();
        Map<Integer,   KaenvironmentHelper> kadeployEnvs = getKadeployEnvs();
        KadeployDlg frame = new KadeployDlg(this.myConn, kadeployJobs, kadeployEnvs);
        frame.setVisible(true);
    }

    /**
     * Methos returning the KadeployResources
     *
     * @return Map of Kadeploy resources
     */
    private Map<OarJobKey, GridJob> getKadeployResources() {
        Map<OarJobKey, GridJob> map = new HashMap<OarJobKey, GridJob>();
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
            if (Config. isSiteEnable(ix)) {
                Map<String, JobTreeNode> siteMap = this.jobsMap.get(ix);
                for (Iterator p = siteMap.values().iterator();p.hasNext();) {
                    GridJob job = ((JobTreeNode)p.next()).getJob();
                    if (job.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equals(Config.getUser()) &&
                            job.getParameterValue(GridJob.KEY_GRID_JOB_QUEUE).equals(G5kCfg.KADEPLOY_QUEUE_NAME)
                    ) {
                        // LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getKadeployResources", "My job : " + job.jobName);
                        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getKadeployResources", "My job : " + job.getParameterValue(GridJob.KEY_GRID_JOB_ID));
                        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getKadeployResources","  - Job Queue :" + job.getParameterValue(GridJob.KEY_GRID_JOB_QUEUE));
                        // map.put(new OarJobKey(ix, job.jobName), job);
                        map.put(new OarJobKey(ix, job.getParameterValue(GridJob.KEY_GRID_JOB_ID)), job);
                    }
                }
            } // end if isClusterEnabled
        } // end for

        return map;
    }

    /**
     * Method returning the KadeployEnvironmentHelper
     *
     * @return Map of KadeployEnvironmentHelper
     */
    public Map<Integer, KaenvironmentHelper> getKadeployEnvs() {
        Map<Integer, KaenvironmentHelper> map = new HashMap<Integer, KaenvironmentHelper>();
        WaitingFrame frame = new WaitingFrame("Retrieving available KaDeploy environments ...","Retrieving available KaDeploy environments ...",G5kSite.getSitesNumber(),true);
        frame.launch("Retrieving available KaDeploy environments ...");
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
            if (Config. isSiteEnable(ix)) {
                String siteHost = G5kSite.getInternalFrontalForIndex(ix);
                if (siteHost != null) {
                    map.put (new Integer(ix),KadeployUtil.getKadeployEnv(this.myConn, siteHost));
                    frame.setStatusText("Retrieving available KaDeploy environments for : "+ siteHost + " ...");
                    frame.incrementProgressBar();
                }
            } // end if isClusterEnabled
        } // end for
        frame.dispose();
        return map;
    }

    /**
     * Method initializing the Last Cluster Times Array
     *
     */
    private void initializeLastSiteTimesArray(){
		lastSiteJobsDate = new long[G5kSite.getSitesNumber()];
		lastSiteSummaryDate = new long[G5kSite.getSitesNumber()];
		long thisTime = System.currentTimeMillis();
		for(int i = 0 ; i < G5kSite.getSitesNumber(); i ++){
			lastSiteJobsDate[i] = thisTime;
			lastSiteSummaryDate[i] = 0;
		}
    }

    /**
     * Method performing the update action
     *
     */
    private void performUpdating() {
        myConn = ConnectDlg.exec(this);
        
        this.repaint();
        if (this.myConn != null) {
            initializeShellProperties();
            shellProperties = ConnectDlg.properties;
            carder.show(cardPanel, "Update");
            this.setEnabled(false);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                	currentAnimator = updatingAnimator;
                    updatingAnimator.start();
                }
            });
            this.repaint();
            this.remove(connect_btn);
            this.add(disconnect_btn,0);
            this.connect_btn.setEnabled(false);
            this.allocate_btn.setEnabled(true);
            this.refresh_btn.setEnabled(true);
            this.view_btn.setEnabled(true);
            this.shell_btn.setEnabled(true);
            this.kadeploy_btn.setEnabled(true);
            if(jftp_btn!= null) this.jftp_btn.setEnabled(true);
            URL url = getClass().getResource("/resources/gridr/g5k/connect_established.png");
            this.connect_btn.setIcon(new ImageIcon(url));
            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performUpdating","ToolBar buttons in connected mode");
            OarStatRunnable doRun = new OarStatRunnable(this, this.myConn);
            initializeLastSiteTimesArray();
            final Thread th = new Thread(doRun);
            th.start();
            this.repaint();
            try{
                th.join();
            }catch(Exception e){
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performUpdating", e);
            }
            long thisTime = System.currentTimeMillis();
            for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
                lastSiteJobsDate[i] = thisTime;
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    G5kRes.this.sitesTree.updateUI();
                }
            });
            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performUpdating", "Sites tree UI updated");
            currentAnimator.stop();
            updatingAnimator.stop();
            resetAnimatorMessages();
            this.setEnabled(true);
            carder.show(cardPanel, "Job");
            LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performUpdating", "Update done");
        }
    }

    /**
     * Method performing the refreshing action
     *
     */
    private void performRefreshing() {
    	carder.show(cardPanel, "Refresh");
    	this.setEnabled(false);
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			currentAnimator = refreshingAnimator;
    			refreshingAnimator.start();
    			
    		}
    	});
    	OarStatRunnable doRun = new OarStatRunnable(this, this.myConn);
    	Thread th = new Thread(doRun);
    	th.start();
    	try{th.join();}catch(Exception e){
    	    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performRefreshing", e);
        }
    	long thisTime = System.currentTimeMillis();
    	for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
    		lastSiteJobsDate[i] = thisTime;
    	}
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				G5kRes.this.sitesTree.updateUI();
    		}
    	});
    	LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performUpdating", "Sites tree UI updated");
    	currentAnimator.stop();
		refreshingAnimator.stop();
		resetAnimatorMessages();
    	this.setEnabled(true);
    	carder.show(cardPanel, "Job");
    	LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performUpdating", "Refresh done");
    }

    /**
     * Class implementing the View action
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class ViewAction implements ActionListener {
        /**
         * Method implementing the main behaviour of the View Action
         */
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
		            Thread performer = new Thread(new Runnable() {
		                public void run() {
		                    performViewing();
		                }
		            }, "Viewer");
		            performer.start();
                }
            });
        }
    }

    /**
     * Method performing the View action
     *
     */
    private void performViewing(){
        carder.show(cardPanel, "View");

        this.setEnabled(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	currentAnimator = viewingAnimator;
                viewingAnimator.start();
            }
        });
        Vector<GridJob> userJobs = new Vector<GridJob>();
        Vector<Integer> jobLoc = new Vector<Integer>();
        HashMap<String, String> reservationMap = new HashMap<String, String>();
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {

            Map<String, JobTreeNode> siteMap = this.jobsMap.get(ix);
            for (Iterator p = siteMap.values().iterator();p.hasNext();) {
                GridJob job = ((JobTreeNode)p.next()).getJob();
                if (job.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equals(Config.getUser())) {
                    // LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performViewing", "Getting job : " + job.jobName);
                    LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performViewing", "Getting job : " + job.getParameterValue(GridJob.KEY_GRID_JOB_ID));
                    userJobs.add(job);
                    jobLoc.add(new Integer(ix));
                    reservationMap.put(G5kSite.getSiteForIndex(ix) + "!" + job.getParameterValue(GridJob.KEY_GRID_JOB_ID), 
                            job.getParameterValue(GridJob.KEY_GRID_JOB_HOSTS).replace(GridJob.HOSTS_SEPARATOR, "+"));
                }
            }
        } // end for

        String [][] userData = new String[userJobs.size()][6];
        for (int ix=0; ix<userJobs.size(); ix++) {
            userData[ix][0] = G5kSite.getSiteForIndex(jobLoc.get(ix).intValue());
            userData[ix][1] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_ID);
            userData[ix][2] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT);
            userData[ix][3] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_STATE);
            userData[ix][4] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME);
            userData[ix][5] = userJobs.get(ix).getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME);
        }

        OarSummaryRunnable doRun = new OarSummaryRunnable(this, this.myConn, userData,reservationMap);
        Thread th = new Thread(doRun);
        th.start();
        try{
            th.join();
            currentAnimator.stop();
            viewingAnimator.stop();
            resetAnimatorMessages();
        }catch(Exception e){
            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performViewing", e);
        }

        LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performViewing","View initialized");

        this.setEnabled(true);
        carder.show(cardPanel, "Job");
    }

    /**
     * Class implementing the Reserve action
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class ReservationAction implements ActionListener {
        /**
         * Method implementing the main behaviour of the View Action
         */
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
		            Thread performer = new Thread(new Runnable() {
		                public void run() {
		                    performReservation();
		                }
		            }, "Reserver");
		            performer.start();
                }
            });
        }
    }

    /**
     * Method performing the reservation
     *
     */
    private void performReservation(){
    	ResaDlg dlg = new ResaDlg(this,myConn);
    	if (dlg.exec() == JOptionPane.OK_OPTION) {
    		carder.show(cardPanel, "Reserve");
    		this.setEnabled(false);
    		G5kReservation resa = dlg.getResa();
    		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, 
    		        this.getClass().getName(), "perfomReservation","Reservation done");
    		// start the reservation
    		if (resa == null){
            	reservingAnimator.stop();
            	resetAnimatorMessages();
            	this.setEnabled(true);
            	carder.show(cardPanel, "Job");
            	return;
    		}
    		SwingUtilities.invokeLater(new Runnable() {
    			public void run() {
    				currentAnimator = reservingAnimator;
    				reservingAnimator.start();
    			}
    		});
    		HashMap<Integer, String> idJobAndFrontalMap = new HashMap<Integer, String>();
    		boolean aSubmissionFailed = false;
    		for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
    			if (resa.getRequestedNodeCount(ix) > 0) {
    				reservingAnimator.setMessage("Reserving " + resa.getRequestedNodeCount(ix) + 
    				        " resources in " + G5kSite.getSiteForIndex(ix) +
    				        ", please wait");
    				OarOpRunnable doRun = new OarOpRunnable(this,
    				        ix,
    						this.myConn,
    						resa.getUser(),
    						// G5kConstants.sites.get(G5kCluster.getSiteIndexForCluster(ix)).getExternalFrontal(),
    						G5kConstants.sites.get(ix).getExternalFrontal(),
    						// G5kCluster.getOarFrontalForIndex(ix),
    						G5kSite.getInternalFrontalForIndex(ix),
    						resa.getStartTime(),
    						resa.getWallTime(),
    						resa.getRequestedNodeCount(ix),
                            resa.getSelectedQueue(),
                            resa.getPropertiesHashMap().get(ix), 
                            resa.getScriptToLaunch(),
                            resa.getChosenCluster(ix));

    				Thread th = new Thread(doRun);
    		    	th.start();
    		    	try {
    		    	    th.join();
    		    	}
    		    	catch(Exception e){
    		    	    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, 
    		    	            this.getClass().getName(), "performReserving", e);
                    }
    		    	int idJob = doRun.getIdJob();
    		    	if(resa.isOargridsubBehaviour()){
    		    		if(idJob < 0){
    		    			aSubmissionFailed = true;
    		    			break;
    		    		}
    		    	}
    		    	idJobAndFrontalMap.put(idJob, G5kSite.getSiteForIndex(ix));
    		    	long thisTime = System.currentTimeMillis();
    		    	for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
    		    		lastSiteJobsDate[i] = thisTime;
    		    	}
    		    	LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, 
    		    	        this.getClass().getName(),"performReservation","Reservation performed for site : " 
    		    	        + G5kSite.getSiteForIndex(ix));
    			}
    		}
    		if(aSubmissionFailed){
    			Iterator<Integer> iter = idJobAndFrontalMap.keySet().iterator();
    			while(iter.hasNext()){
    				final Connection connection = this.myConn;
    				final int idJob = iter.next();
    				final String oar_frontal = idJobAndFrontalMap.get(idJob);
    				Thread th = new Thread(new Runnable(){
    					public void run(){
    						// OarUtil.oardel(connection, oar_frontal, Integer.toString(idJob));
    						BatchSchedulerMgr.delJob(
    						        //Config.getBatchScheduler(G5kCluster.getClusterByOar(oar_frontal)),
    								Config.getBatchScheduler(G5kSite.getIndexForSite(oar_frontal)),
    						        connection, oar_frontal, Integer.toString(idJob), 
    						        new DefaultBatchSchedulerListener());
    					}
    				});
    				th.start();
    				try{th.join();}catch(Exception e){
    					LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performReserving", e);
    				}
    			}
    		}
    		currentAnimator.stop();
    		reservingAnimator.stop();
    		resetAnimatorMessages();
    		this.setEnabled(true);
        	carder.show(cardPanel, "Job");
        	
        	new ReservationStatusFrame(resa,idJobAndFrontalMap);
        	
    	}
    }

    /**
     * Class implementing the Delete action for all the grid
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class DeleteG5kAction implements ActionListener {
        /**
         * Method implementing the main behaviour of the Reserve Action
         */
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
		            Thread performer = new Thread(new Runnable() {
		                public void run() {
		                    performG5kDeletion();
		                }
		            }, "Deleter");
		            performer.start();
                }
            });
        }
    }

    /**
     * Method performing the dleetion of all Grid5000 jobs of the user
     *
     */
    private void performG5kDeletion(){
    	int resultValue = JOptionPane.showConfirmDialog(null, "Do you really want to delete all your jobs on Grid5000 ?", "Deletion confirmation", JOptionPane.YES_NO_OPTION);
    	if(resultValue == JOptionPane.YES_OPTION){
            carder.show(cardPanel, "Delete");
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                	currentAnimator = deletingAnimator;
                    deletingAnimator.start();

                }
            });
            this.setEnabled(false);
    		LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "erformG5kDeletion", "Deletion of all jobs on G5k launched");
    		for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
    			if(Config.isSiteEnable(i)){
    				int siteInd = i;
    				TreeNode aNode = rootNode.getChildAt(i);
    				Enumeration<JobTreeNode> allJobsOfThisCluster = aNode.children();
    				while(allJobsOfThisCluster.hasMoreElements()){
    					JobTreeNode myNode = allJobsOfThisCluster.nextElement();
    					if(myNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equalsIgnoreCase(Config.getUser())){
                            deletingAnimator.setMessage("Deleting job "+myNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID) + " on cluster " + G5kSite.getSiteForIndex(siteInd));
    						LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, 
    						        this.getClass().getName(), 
    						        "performG5kDeletion", 
    						        "Executing oardel of job "+myNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID) + " on cluster " + G5kSite.getSiteForIndex(siteInd));
    						/*
    						ArrayList<String> lines = OarUtil.oardel(this.myConn,
    								G5kCluster.getOarFrontalForIndex(siteInd),
    								myNode.myJob.getId());
    						String [] result = lines.toArray(new String[lines.size()]);
    						*/
    						String [] result = BatchSchedulerMgr.delJob(
    						        Config.getBatchScheduler(siteInd), 
    						        this.myConn, 
    						        //G5kCluster.getOarFrontalForIndex(siteInd),
    						        G5kSite.getInternalFrontalForIndex(siteInd),
    						        myNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID), 
    						        new DefaultBatchSchedulerListener());
    						
    						String lineForLogger = "";
    						for (int ix=0; ix<result.length; ix++) {
    							lineForLogger += result[ix];
    						}
    						LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performG5kDeletion", lineForLogger);
    					}
    				}
    			}
    			LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "erformG5kDeletion", "Deletion of all jobs on G5k done");
    		}
            this.setEnabled(true);
            currentAnimator.stop();
            deletingAnimator.stop();
            resetAnimatorMessages();
            carder.show(cardPanel, "Job");
    	}
    }

    /**
     * Class implementing the Delete action
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class DeletingAction implements ActionListener {
        /**
         * Method implementing the main beahviour of the Delete Action
         */
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
		            Thread performer = new Thread(new Runnable() {
		                public void run() {
		                    performDeletion();
		                }
		            }, "Deleter");
		            performer.start();
                }
            });
        }
    }

    /**
     * Method performing the delete action
     */
    private void performDeletion(){
    	if(selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equalsIgnoreCase(Config.getUser())){
            carder.show(cardPanel, "Delete");
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                	currentAnimator = deletingAnimator;
                    deletingAnimator.start();

                }
            });
            this.setEnabled(false);
    		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performDeletion", "Deleting job " + selectedNode);

            IconNode parentNode = (IconNode)selectedNode.getParent();
    		int siteInd = getSiteByNode(parentNode);
            deletingAnimator.setMessage("Deleting job "+selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID) + " on cluster " + G5kSite.getSiteForIndex(siteInd));

    		if (siteInd == -1)
    			return;
    		/*
    		ArrayList<String> lines = OarUtil.oardel(this.myConn,
    				G5kCluster.getOarFrontalForIndex(siteInd),
    				selectedNode.myJob.getId());
    		String [] result = lines.toArray(new String[lines.size()]);
    		*/
    		String [] result = BatchSchedulerMgr.delJob(
    		        Config.getBatchScheduler(siteInd), 
    		        this.myConn, 
    		        //G5kCluster.getOarFrontalForIndex(siteInd),
    		        G5kSite.getInternalFrontalForIndex(siteInd),
    		        selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID), 
    		        new DefaultBatchSchedulerListener());
    		String lineForLogger = "";
			for (int ix=0; ix<result.length; ix++) {
				lineForLogger += result[ix];
			}
			LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performDeletion", lineForLogger);
			LoggingManager.log(Level.INFO,LoggingManager.RESOURCESTOOL,this.getClass().getName(), "performDeletion", "Deletion done");
            this.setEnabled(true);
            currentAnimator.stop();
            deletingAnimator.stop();
            resetAnimatorMessages();
            carder.show(cardPanel, "Job");
    		performClusterRefreshing(siteInd);
    	}
    }

    /**
     * Class implementing the Delete action on a cluster
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class DeletingClusterAction implements ActionListener {
        /**
         * Method implementing the main beahviour of the Delete Action on a cluster
         */
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
		            Thread performer = new Thread(new Runnable() {
		                public void run() {
		                    performClusterDeletion();
		                }
		            }, "Deleter");
		            performer.start();
                }
            });
        }
    }

    /**
     * Method performing the deletion of all jobs of the current user
     * on a cluster
     *
     */
    private void performClusterDeletion(){
        carder.show(cardPanel, "Delete");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	currentAnimator = deletingAnimator;
                deletingAnimator.start();

            }
        });
		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performClusterDeletion", "Try to delete all of your jobs on the cluster " + selectedSiteNode.iconName);
    	int siteInd = getSiteByNode(selectedSiteNode);
		if (siteInd == -1)
			return;
		Enumeration enumer = selectedSiteNode.children();
		while(enumer.hasMoreElements()){
			JobTreeNode aNode = (JobTreeNode)enumer.nextElement();
			if(aNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER).equalsIgnoreCase(Config.getUser())){
                deletingAnimator.setMessage("Deleting job "+aNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID) + " on cluster " + G5kSite.getSiteForIndex(siteInd));
				LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performClusterDeletion", "Deleting job " + selectedNode);
				/**
				 * PREVIOUSLY
				 */
				/*
				ArrayList<String> lines = OarUtil.oardel(this.myConn,
						G5kCluster.getOarFrontalForIndex(siteInd),
						aNode.myJob.jobName);
				String [] result = lines.toArray(new String[lines.size()]);
				*/
				String [] result = BatchSchedulerMgr.delJob(
				        Config.getBatchScheduler(siteInd), 
				        this.myConn, 
				        //G5kCluster.getOarFrontalForIndex(siteInd),
				        G5kSite.getInternalFrontalForIndex(siteInd),
				        aNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID),
				        new DefaultBatchSchedulerListener());
				String lineForLogger = "";
				for (int ix=0; ix<result.length; ix++) {
					lineForLogger += result[ix];
				}
				LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performClusterDeletion", lineForLogger);
				LoggingManager.log(Level.INFO,LoggingManager.RESOURCESTOOL,this.getClass().getName(), "performClusterDeletion", "Cluster Deletion done");
			}
		}
        this.setEnabled(true);
        currentAnimator.stop();
        deletingAnimator.stop();
        resetAnimatorMessages();
        carder.show(cardPanel, "Job");
		performClusterRefreshing(siteInd);
    }

    /**
     * Class implementing the Refresh action on a cluster
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class RefreshClusterAction implements ActionListener {
        /**
         * Method implementing the main beahviour of the Refresh Action on a cluster
         */
        public void actionPerformed(ActionEvent e) {
        	final String name = selectedSiteNode.iconName;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
		            Thread performer = new Thread(new Runnable() {
		                public void run() {
		                	int siteIndex = G5kSite.getIndexForSite(name);
		                    performClusterRefreshing(siteIndex);
		                }
		            }, "UpdateClusterView");
		            performer.start();
                }
            });
            
        }
    }

    /**
     * Method performing the cluster refreshing
     *
     * @param siteIndex index of the site to refresh
     */
    private void performClusterRefreshing(int siteIndex) {
    	carder.show(cardPanel, "Refresh");
    	this.setEnabled(false);
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			currentAnimator = refreshingAnimator;
    			refreshingAnimator.start();
    		}
    	});
    	OarClusterStatRunnable doRun = new OarClusterStatRunnable(this, this.myConn,siteIndex);
    	Thread th = new Thread(doRun);
    	th.start();
    	try{th.join();}catch(Exception e){
            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"performClusterRefreshing", e);
        }
    	lastSiteJobsDate[siteIndex] = System.currentTimeMillis();
        lastSiteSummaryDate[siteIndex] = 0;
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				G5kRes.this.sitesTree.updateUI();
    		}
    	});
    	currentAnimator.stop();
    	refreshingAnimator.stop();
    	resetAnimatorMessages();
    	this.setEnabled(true);
    	LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performClusterRefreshing", "ClusterRefresh done");
    	if( Math.abs(lastSiteJobsDate[siteIndex] - lastSiteSummaryDate[siteIndex]) > delayForUpdateClusterInformation )
    	    this.performClusterInformationSummarization(siteIndex);
    	carder.show(cardPanel, "Job");
    }

    /**
     * Method performing the summarization of the information for a cluster
     *
     * @param index index of the site
     */
    private void performClusterInformationSummarization(int index){
    	if(myConn != null){
    		carder.show(cardPanel, "Information");
    		// informationSummaryAnimator.setMessage("Gathering information on " + G5kCluster.getNameForIndex(index));
    		informationSummaryAnimator.setMessage("Gathering information on " + G5kSite.getSiteForIndex(index));
    		G5kRes.this.setEnabled(false);
    		currentAnimator = informationSummaryAnimator;
            informationSummaryAnimator.start();
    		long thisTime = System.currentTimeMillis();
    		if(( thisTime -lastSiteSummaryDate[index] ) > delayForUpdateClusterInformation){
    			String site = G5kSite.getSiteForIndex(index);
    			String batchScheduler = Config.getBatchScheduler(index);
    			String internalFrontale = G5kSite.getInternalFrontalForIndex(index);
    			
    			GridJob[] jobs = BatchSchedulerMgr.getJobs(
    					batchScheduler, 
    			        this.myConn, 
    			        internalFrontale, 
    			        new DefaultBatchSchedulerListener());
    			Map<String, GridJob> jMap = new HashMap<String, GridJob>();
    			for (int ix=0; ix<jobs.length; ix++)
    			    jMap.put(jobs[ix].getParameterValue(GridJob.KEY_GRID_JOB_ID), jobs[ix]);
    			this.clusterJobsMap.setElementAt(jMap, index);
    			
    			// clusterNodesStatusArray.setElementAt(OarUtil.getNodesStatus(myConn, G5kCluster.getOarFrontalForIndex(index)),index);
    			clusterNodesStatusArray.setElementAt(
    			        BatchSchedulerMgr.getNodesStatus(Config.getBatchScheduler(index), 
    			                this.myConn, 
    			                G5kSite.getInternalFrontalForIndex(index), 
    			                new DefaultBatchSchedulerListener()),
    			        index);
    		}
    		lastSiteSummaryDate[index] = thisTime;
    		currentAnimator.stop();
    		informationSummaryAnimator.stop();
    		resetAnimatorMessages();
    		G5kRes.this.setEnabled(true);

    		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "performClusterInformationSummarization", "Information summarization done");
    		if( Math.abs(lastSiteJobsDate[index] - lastSiteSummaryDate[index]) > delayForUpdateClusterInformation ) this.performClusterRefreshing(index);
    		// this.myInfoPanel.showCluster(clusterNodesStatusArray.get(index),clusterJobsMap.get(index),G5kCluster.getNameForIndex(index),this.myConn);
    		this.myInfoPanel.showCluster(clusterNodesStatusArray.get(index),
    		        clusterJobsMap.get(index),
    		        G5kSite.getSiteForIndex(index),
    		        this.myConn);
            carder.show(cardPanel, "Job");
    	}
    }

    /**
     * Method performing the summarization of information for a cluster
     * from an index
     *
     * @param index index of the cluster
     */
    public void summarizeInformationForCluster(int index){
    	final int clusterIndex = index;

    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			Thread performer = new Thread(new Runnable() {
    				public void run() {
    					performClusterInformationSummarization(clusterIndex);
    				}
    			}, "InformationSummarization");
    			performer.start();
    		}
    	});

    }

    /**
     * Class implementing the opening action of a shell on the preferred acces point
     * (e.g. the connection frontal)
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class OpeningShellAction implements ActionListener{

        /**
         * Method managing the action
         */
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	    		public void run() {
                    initializeShellProperties();
	    			//shellProperties.put("destination", "");
	    			LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"actionPerformed","Shell is opening on frontale");
	    			JCTerm.newTerminal(shellProperties);
	    		}
			});
			myToolBar.repaint();
		}
    }

    /**
     * Class implementing the opening action of a shell on a cluster
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class OpeningShellActionOnCluster implements ActionListener{
        /**
         * Method managing the action
         */
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	    		public void run() {
                    initializeShellProperties();
	    			final String name = selectedSiteNode.iconName;
	    			shellProperties.put("destination", G5kSite.getInternalFrontalForSite(name));
	    			LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"actionPerformed","Shell is opening on cluster " + name);
	    			JCTerm.newTerminal(shellProperties);
	    		}
			});

		}
    }

    /**
     * Class implementing the opening action of a shell
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class OpeningShellActionOnJob implements ActionListener{
        /**
         * Method managing the action
         */
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	    		public void run() {
	    			String jobOwner = selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER);
	    			String configOwner = Config.getUser();
	    			String jobState = selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE);
	    			if(jobOwner.equalsIgnoreCase(configOwner) && jobState.toLowerCase().startsWith("r")){
                        initializeShellProperties();
	    				shellProperties.put("destination", G5kSite.getInternalFrontalForSite(selectedNode.getParent().toString()));
                        shellProperties.put("command", selectedNode.myJob.getJobConnectionCommand()+ " " + selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID));
	    				LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"actionPerformed","Shell is opening on job " + selectedNode.myJob.getParameterValue(GridJob.KEY_GRID_JOB_ID));
		    			JCTerm.newTerminal(shellProperties);

	    			}
	    		}
			});
		}
    }

    /**
     * Class implementing the action of the opening of the JavaHelp for GRUDU
     * 
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class HelpAction implements ActionListener{
       /**
        * Method managing the action
        */
        public void actionPerformed(ActionEvent e) {
//           Find the HelpSet file and create the HelpSet object:
            HelpSet hs;
            try {
                URL hsURL = new File(System.getProperty("user.dir") +"/Help/GRUDU/GRUDU_Help.hs").toURL();
                hs = new HelpSet(null, hsURL);
            } catch (Exception ee) {
//              Say what the exception really is
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed","HelpSet"+ ee);
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed","HelpSet "+ System.getProperty("user.dir") +"/Help/GRUDU/GRUDU_Help.hs" +" not found");
                return;
            }
//           Create a HelpBroker object:
            HelpBroker hb = hs.createHelpBroker();
            hb.setSize(new Dimension(800,600));
            ActionEvent cshAction = new ActionEvent(new JLabel(),
                    ActionEvent.ACTION_FIRST, null);
            new CSH.DisplayHelpFromSource( hb ).actionPerformed(cshAction);
            myToolBar.repaint();

        }

    }

    /**
     * Method initializing the shell properties
     *
     */
    public void initializeShellProperties(){
    	shellProperties = new HashMap<String, String>();
		shellProperties.put("username", Config.getUser());
		shellProperties.put("host", G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
		shellProperties.put("publickey", G5kCfg.get(G5kCfg.SSHKEYFILE));
		shellProperties.put("passphrase", G5kCfg.getSSHKey());
		// the destination should be setted to "" if we are just connecting to the access frontend
		shellProperties.put("destination", "");
		LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "initializeShellProperties", "Shell properties initialized");
    }

    /**
     * Method initializing the data information of a cluster
     *
     */
    private void initializeClusterInformationsData(){
    	for(int i = 0 ; i < G5kSite.getSitesNumber() ; i ++){
    		clusterJobsMap.add(new HashMap<String, GridJob>());
    		clusterNodesStatusArray.add(new int[0]);
    	}
    	LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL , this.getClass().getName(),"initializeClusterInformationsData" , "Data information of the clusters initialized");
    }
    
    /**
     * Class implementing the action of the opening of the JFTP
     * the Java File Transfert Protocol for GRUDU and Grid'5000
     * 
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class JFTPAction implements ActionListener{

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			String[] args = new String[1];
			args[0] = G5kCfg.getSSHKey();
			JFtp.main(args);
		}
    	
    }
    
    public void setMessageToCurrentAnimator(String message){
    	if(currentAnimator != null) currentAnimator.setMessage(message);
    }
    
    public void resetAnimatorMessages(){
    	currentAnimator = null;
		updatingAnimator.setMessage("Updating the view, please wait ...");
		viewingAnimator.setMessage("Preparing the view, please wait ...");
		reservingAnimator.setMessage("Reserving resources, please wait ...");
        deletingAnimator.setMessage("Deleting jobs, please wait ...");
        refreshingAnimator.setMessage("Refreshing the view, please wait ...");
        informationSummaryAnimator.setMessage("Summarizing Information, please wait ...");

    }
}
