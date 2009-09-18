/****************************************************************************/
/* Method showing the activity of Grid5000 in term of nodes status and the   */
/* reservations  of the user                                                */
/*                                                                          */
/*  Author(s):                                                              */
/*    - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                       */
/*    - David LOUREIRO (David.Loureiro@ens-lyon.fr)                         */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ViewSummaryDlg.java,v 1.25 2007/11/07 11:35:01 dloureir Exp $
 * $Log: ViewSummaryDlg.java,v $
 * Revision 1.25  2007/11/07 11:35:01  dloureir
 * Some printlns are passed to the LoggingManager
 *
 * Revision 1.24  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.23  2007/10/05 14:54:34  aamar
 * Correcting bugs of saving job reservations.
 *
 * Revision 1.22  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.21  2007/07/12 15:17:02  dloureir
 * Some typo corrections and some javadoc
 *
 * Revision 1.20  2007/06/25 08:18:47  dloureir
 * Adding for the G5KView's graphs and the Cluster's graphs the number of nodes of the user.
 *
 * Revision 1.19  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.18  2007/03/07 15:00:14  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.17  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.3  2006/12/18 13:07:18  dloureir
 * Adding serial version UIDs
 *
 * Revision 1.2  2006/12/10 13:48:59  aamar
 * Some display modifications before the demo
 *
 * Revision 1.1.1.1  2006/12/08 18:11:12  aamar
 * Import DIET DashBoard without svn files and unnecessary files.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.logging.Level;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

import diet.grid.api.GridJob;

/**
 * Method showing the activity of Grid5000 in term of nodes status and the
 * reservations  of the user
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ViewSummaryDlg extends JFrame {
    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = 1466053264149756508L;
    /**
     * parent frame
     */
	private JFrame myParent;
    /**
     * G5k View table model
     */
	private G5kViewTableModel myModel;
    /**
     * Table for Grid5000
     */
	private JTable myTable;
    /**
     * Table for th user reservations
     */
	private JTable myUserTable;
    /**
     * map of reservations
     */
	private HashMap<String, String> reservationMap;
    /**
     * Panel for the saving buttons
     */
	private JPanel savePanel = null;
    /**
     * Button for the saving of all reservations
     */
	private JButton saveAllReservationsButton = null;
    /**
     * Button for the saving of the selected reservations
     */
	private JButton saveSelectedReservationsButton = null;
    /**
     * Data for the Grid5000 table
     */
	private int[][] dataForG5k = null;
    /**
     * Separator for the different jobs in the job file
     */
	public static String jobsSeparator = "**--**";
	
	/**
	 * Method generating the data for the G5K View with the user data
	 * 
	 * @param data the data of the clusters
	 * @param userData the data of the user
	 */
	private void generateDataForG5K(int[][] data, String[][] userData){
		dataForG5k = new int[data.length][data[0].length+1];
		for(int i = 0 ; i < data.length ; i ++){
			for(int j = 0 ; j < data[0].length-1 ; j ++){
				dataForG5k[i][j] = data[i][j];
			}
			dataForG5k[i][data[0].length] = data[i][data[0].length-1];
		}
		for(int i = 0; i < userData.length ; i++){
		    
			int index = G5kSite.getIndexForSite(userData[i][0]);
			dataForG5k[index][data[0].length-1] = Integer.parseInt(userData[i][2]);
		}
	}
	
	/**
     * Default constructor
     *
     * @param parent parent frame
     * @param data data for Grid5000
     * @param userData data for the user on Grid5000
     * @param reservationMap map of the reservations
	 */
	public ViewSummaryDlg(JFrame parent, int[][] data, String [][] userData, HashMap<String, String> reservationMap) {
		super("Grid5000 & User summary");
		this.myParent = parent;
		generateDataForG5K(data,userData);
		this.reservationMap = reservationMap;
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.myModel = new G5kViewTableModel(data);
		this.myTable=new JTable();
		this.myTable.setModel(this.myModel);
		this.myTable.createDefaultColumnsFromModel();
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		tablePanel.add(myTable.getTableHeader());
		tablePanel.add(myTable);
		panel.add(tablePanel, BorderLayout.NORTH);

		UserViewTableModel userModel = new UserViewTableModel(userData);
		this.myUserTable = new JTable();
		this.myUserTable.setModel(userModel);
		UserViewTableCellRenderer renderer = new UserViewTableCellRenderer();
        this.myUserTable.setDefaultRenderer(String.class, renderer);
		this.myUserTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		if(myUserTable.getRowCount() != 0){
			JPanel userTablePanel = new JPanel();
			userTablePanel.setLayout(new BoxLayout(userTablePanel,BoxLayout.Y_AXIS));
			userTablePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			userTablePanel.add(myUserTable.getTableHeader());
			userTablePanel.add(myUserTable);
			panel.add(userTablePanel, BorderLayout.CENTER);

			panel.add(getSavePanel(),BorderLayout.SOUTH);
		}

		getContentPane().add(panel, BorderLayout.CENTER);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ViewSummaryDlg", "Summary Dialog Frame initialized");
	}

    /**
     * Method returning the components of the frame
     *
     * @return the components of the frame
     */
	public Component[] getContentPanel(){
		return getContentPane().getComponents();
	}

    /**
     * Method returning the save panel
     *
     * @return save panel
     */
	private JPanel getSavePanel(){
		if(savePanel == null){
			savePanel = new JPanel();
			savePanel.setLayout(new BoxLayout(savePanel,BoxLayout.X_AXIS));
			savePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
			savePanel.add(Box.createHorizontalGlue());
			savePanel.add(getSaveAllReservationsButton(),null);
			savePanel.add(Box.createHorizontalStrut(10));
			savePanel.add(getSaveSelectedReservationsButton(),null);
			savePanel.add(Box.createHorizontalGlue());
		}
		return savePanel;
	}

    /**
     * Method returning the button for the saving of the selected reservations
     *
     * @return the saveSelectedreservationButton
     */
	private JButton getSaveSelectedReservationsButton(){
		if(saveSelectedReservationsButton == null){
			saveSelectedReservationsButton = new JButton("Save selected reservations");
			saveSelectedReservationsButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					int returnVal = fileChooser.showSaveDialog(null);

					if(returnVal == JFileChooser.APPROVE_OPTION) {
						saveSelectedDirectoryOfReservation(fileChooser.getSelectedFile());
					}
				}
			});
		}
		return saveSelectedReservationsButton;
	}

    /**
     * Method returning the button for the saving of the all reservations
     *
     * @return the button used to save all reservations
     */
	private JButton getSaveAllReservationsButton(){
		if(saveAllReservationsButton == null){
			saveAllReservationsButton = new JButton("Save all reservations");
			saveAllReservationsButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					int returnVal = fileChooser.showSaveDialog(null);

					if(returnVal == JFileChooser.APPROVE_OPTION) {
						saveDirectoryOfReservation(fileChooser.getSelectedFile());
					}
				}
			});
		}
		return saveAllReservationsButton;
	}

    /**
     * Method that save all the reservations in the directory given in parameter
     *
     * @param theDirectory the directory were the reservations will be stored
     */
	public void saveDirectoryOfReservation(File theDirectory){
		// creating directory
		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveDirectoryOfReservation", "Saving the reservation directory : " + theDirectory.getAbsolutePath());
		if(!theDirectory.exists()){
			theDirectory.mkdirs();
		}
		// getting the number of jobs for each cluster
		Iterator<String> iter = reservationMap.keySet().iterator();
		// int[] clusterJobs = new int[G5kCluster.getClustersNumber()];
		int[] siteJobs = new int[G5kSite.getSitesNumber()];
		while(iter.hasNext()){
			String site = iter.next();
			String siteName = new StringTokenizer(site,"!").nextToken();
			// int index = G5kCluster.getIndexForCluster(siteName);
			int index = G5kSite.getIndexForSite(siteName);
			if(index != -1) siteJobs[index]++;
		}
		try{
			// openning the files for the selected cluster that have a reservation
			File[] aFileForNodesArray = new File[G5kSite.getSitesNumber()];
			BufferedWriter[] bufferArray = new BufferedWriter[G5kSite.getSitesNumber()];
			for(int i = 0 ; i < siteJobs.length ; i++){
				if(siteJobs[i] != 0){
					aFileForNodesArray[i] = new File(theDirectory + System.getProperty("file.separator") 
					        + G5kSite.getSiteForIndex(i) + ".machines");
					aFileForNodesArray[i].createNewFile();
					bufferArray[i] = new BufferedWriter(new FileWriter(aFileForNodesArray[i]));
				}
			}
			// Writing the nodes
			iter = reservationMap.keySet().iterator();
			while(iter.hasNext()){
				String site = iter.next();
				String siteName = new StringTokenizer(site,"!").nextToken();
				int siteIndex = G5kSite.getIndexForSite(siteName);
                
				LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), 
				        "saveDirectoryOfReservation", "Writing nodes of site: " + siteName);
				try{
					StringTokenizer nodesCutter = new StringTokenizer(reservationMap.get(site),"+");
					String node;
					while(nodesCutter.hasMoreTokens()){
						node = nodesCutter.nextToken();
						LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), 
						        "saveDirectoryOfReservation"," - " + node);
						bufferArray[siteIndex].write(node);
						bufferArray[siteIndex].newLine();
						bufferArray[siteIndex].flush();
					}
				}
				catch(Exception e){
					LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveDirectoryOfReservation", e);
				}
			}
			// closing the buffers of the clusters enabled and where there is a reservation
			for(int i = 0 ; i < siteJobs.length ; i++){
				if(siteJobs[i] != 0) bufferArray[i].close();
			}

			// opening the files for the selected cluster that have a reservation
			File[] aFileForJobsArray = new File[G5kSite.getSitesNumber()];
			bufferArray = new BufferedWriter[G5kSite.getSitesNumber()];
			for(int i = 0 ; i < siteJobs.length ; i++){
				if(siteJobs[i] != 0){
					aFileForJobsArray[i] = new File(theDirectory + System.getProperty("file.separator") + 
					        G5kSite.getSiteForIndex(i) + ".jobs");
					aFileForJobsArray[i].createNewFile();
					bufferArray[i] = new BufferedWriter(new FileWriter(aFileForJobsArray[i]));
				}
			}
			// Writing the jobs
			iter = reservationMap.keySet().iterator();
			while(iter.hasNext()){
				String site = iter.next();
				String siteName = new StringTokenizer(site,"!").nextToken();
				int siteIndex = G5kSite.getIndexForSite(siteName);
				LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveDirectoryOfReservation","Writing jobs of cluster : " + siteName);
				Map<Integer, Vector<GridJob>> myMap = ((G5kRes)myParent).getMyJobsMap();
				Vector<GridJob> vectorTemp = myMap.get(siteIndex);
				Iterator<GridJob> iter1 = vectorTemp.iterator();
				while(iter1.hasNext()){
					GridJob job = iter1.next();
					if(reservationMap.get(site).contains(job.getHostsAsString("+"))){
						LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, 
						        this.getClass().getName(), "saveDirectoryOfReservation"," - " + job.getParameterValue(GridJob.KEY_GRID_JOB_ID));
						bufferArray[siteIndex].write(jobsSeparator);
						bufferArray[siteIndex].newLine();
						bufferArray[siteIndex].write(job.toString());
						bufferArray[siteIndex].newLine();
						bufferArray[siteIndex].flush();
					}
				}
			}
			// closing the buffers of the clusters enabled and where there is a reservation
			for(int i = 0 ; i < siteJobs.length ; i++){
				if(siteJobs[i] != 0) bufferArray[i].close();
			}
		}
		catch(Exception e){
			LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveDirectoryOfReservation", e);
		}
	}

    /**
     * Method for the saving of the the selected reservations in the
     * directory given in argument
     *
     * @param theDirectory the directory were the reservations will be stored
     */
	public void saveSelectedDirectoryOfReservation(File theDirectory){
		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveSelectedDirectoryOfReservation", "Saving the reservation directory : " + theDirectory.getAbsolutePath());
		if(!theDirectory.exists()){
			theDirectory.mkdirs();
		}
		int[] selectedReservations = myUserTable.getSelectedRows();
		String[] selectedJobs = new String[selectedReservations.length];
		String[] sitesOfSelectedReservations = new String[selectedReservations.length];
		int index = 0;
		for(int i : selectedReservations){
			sitesOfSelectedReservations[index] = (String)myUserTable.getValueAt(i, 0);
			selectedJobs[index] = (String)myUserTable.getValueAt(i, 1);
			index++;
		}
		try{
			// opening the files for the selected cluster that have a reservation
			File[] aFileForNodesArray = new File[G5kSite.getSitesNumber()];
			File[] aFileForJobsArray = new File[G5kSite.getSitesNumber()];
			BufferedWriter[] bufferNodesArray = new BufferedWriter[G5kSite.getSitesNumber()];
			BufferedWriter[] bufferJobsArray = new BufferedWriter[G5kSite.getSitesNumber()];
			for(int i = 0 ; i < sitesOfSelectedReservations.length ; i++){
				if(sitesOfSelectedReservations[i] != null){
					int clusterIndex = G5kSite.getIndexForSite(sitesOfSelectedReservations[i]);
					aFileForNodesArray[clusterIndex] = new File(theDirectory + System.getProperty("file.separator") + sitesOfSelectedReservations[i] + ".machines");
					aFileForJobsArray[clusterIndex] = new File(theDirectory + System.getProperty("file.separator") + sitesOfSelectedReservations[i] + ".jobs");
					aFileForNodesArray[clusterIndex].createNewFile();
					aFileForJobsArray[clusterIndex].createNewFile();
					bufferNodesArray[clusterIndex] = new BufferedWriter(new FileWriter(aFileForNodesArray[clusterIndex]));
					bufferJobsArray[clusterIndex] = new BufferedWriter(new FileWriter(aFileForJobsArray[clusterIndex]));
				}
			}

			Iterator<String> iter = reservationMap.keySet().iterator();
			while(iter.hasNext()){

				String cluster = iter.next();
				StringTokenizer tokenizer = new StringTokenizer(cluster,"!");
				String siteName = tokenizer.nextToken();
				String currentJob = tokenizer.nextToken();
				int siteIndex = G5kSite.getIndexForSite(siteName);
				for(String jobs : selectedJobs){
					if(jobs.equalsIgnoreCase(currentJob)){
						LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveSelectedDirectoryOfReservation","Writting nodes of cluster : " + siteName);
						StringTokenizer nodesCutter = new StringTokenizer(reservationMap.get(cluster),"+");
						String node;
						while(nodesCutter.hasMoreTokens()){
							node = nodesCutter.nextToken();
							LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveSelectedDirectoryOfReservation"," - " + node);
							bufferNodesArray[siteIndex].write(node);
							bufferNodesArray[siteIndex].newLine();
							bufferNodesArray[siteIndex].flush();
						}


						LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveSelectedDirectoryOfReservation","Writting jobs of cluster : " + siteName);

						Map<Integer, Vector<GridJob>> myMap = ((G5kRes)myParent).getMyJobsMap();
						Vector<GridJob> vectorTemp = myMap.get(siteIndex);
						Iterator<GridJob> iter1 = vectorTemp.iterator();
						while(iter1.hasNext()){
							GridJob job = iter1.next();
							String nodesOfTheReservation = reservationMap.get(cluster);
							String jobhosts = job.getHostsAsString("+");
							if(nodesOfTheReservation.contains(jobhosts)){
								LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveSelectedDirectoryOfReservation"," - "+ job.getParameterValue(GridJob.KEY_GRID_JOB_ID));
								bufferJobsArray[siteIndex].write(jobsSeparator);
								bufferJobsArray[siteIndex].newLine();
								bufferJobsArray[siteIndex].write(job.toString());
								bufferJobsArray[siteIndex].newLine();
								bufferJobsArray[siteIndex].flush();
							}
						}
					}
				}
			}
			// closing the buffers of the clusters enabled and where there is a reservation
			for(int i = 0 ; i < sitesOfSelectedReservations.length ; i++){
				if(bufferNodesArray[i] != null){
					bufferNodesArray[i].close();
					bufferJobsArray[i].close();
				}
			}
		}
		catch(Exception e){
			LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "saveSelectedDirectoryOfReservation", e);
		}
	}

    /**
     * Class representing the table of the Grid5000 view
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
	public class G5kViewTableModel extends AbstractTableModel {
        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
		private static final long serialVersionUID = -5341872579624754691L;
        /**
         * Data to display
         */
		private Vector<Vector<String>> myData;
        /**
         * array corresponding to the column names
         */
		private String [] columnNames = {"Cluster", "Free", "Job", "Suspected",
				"Dead", "Absent", "Total", "Total usable" };
        /**
         * Default constructor
         *
         * @param data data to display
         */
		public G5kViewTableModel(int [][] data) {
			myData = new Vector<Vector<String>>();
			int[] totals = new int[columnNames.length-1];
			for(int i = 0 ; i < data.length ; i ++){
				if(Config.isSiteEnable(i)){
					Vector<String> temp = new Vector<String>();
					String aSite = G5kSite.getSiteForIndex(i);
					int index = aSite.indexOf("--");
					if(index != -1) aSite = aSite.substring(index+2, aSite.length());
					temp.add(aSite);
					for(int j = 0 ; j < data[i].length ; j++){
						totals[j] += data[i][j];
						temp.add(Integer.toString(data[i][j]));
					}
					temp.add(Integer.toString(data[i][0] + data[i][1] + data[i][4]));
					totals[totals.length-1] +=(data[i][0] + data[i][1] + data[i][4]);
					myData.add(temp);
				}
			}
			// Adding totals
			Vector<String> temp = new Vector<String>();
			temp.add("Total");
			for(int i = 0 ; i< totals.length; i ++){
				temp.add(Integer.toString(totals[i]));
			}
			myData.add(temp);
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kViewTableModel", "G5kViewTableModel initialized");
		}

        /**
         * Method returning the number of columns
         *
         * @return the column count
         */
		public int getColumnCount() {
			return columnNames.length;
		}

        /**
         * Method returning the number of rows of the table
         *
         * @return the row count
         */
		public int getRowCount() {
			return myData.size();
		}

        /**
         * Method returning the name of a column from its index
         *
         * @param columnIndex the index of the column of which we want the name
         *
         * @return the name of the column
         */
		public String getColumnName(int columnIndex) {
			String colName="";
			if (columnIndex <= getColumnCount())
				colName = columnNames[columnIndex];
			return colName;
		}

        /**
         * Method telling if the cell in the position (rowIndex, columnIndex)
         * is editable or not
         *
         * @param rowIndex
         * @param columnIndex
         *
         * @return the boolean value telling if the cell is editable
         */
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

        /**
         * Method returning the value at the position (rowIndex,columnIndex)
         *
         * @param rowIndex index of the value we want to get
         * @param columnIndex index of the value we want to get
         *
         * @return object of the position (rowIndex,columnIndex)
         */
		public Object getValueAt(int rowIndex, int columnIndex) {
			return (myData.get(rowIndex)).get(columnIndex);
		}

	}

    /**
     * Class representing the view of the user reservation
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
	public class UserViewTableModel extends AbstractTableModel {
        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
		private static final long serialVersionUID = -77862344227269245L;
        /**
         * data to display
         */
		private Vector<Vector<String>> myData;
        /**
         * array containing the names of the columns
         */
		private String [] columnNames = {"Site", "Job ID", "Nodes", "State", "Wall Time", "Schedule start"};
        /**
         * Default constructor
         *
         * @param data data to display
         */
		public UserViewTableModel(String [][] data) {
			this.myData = new Vector<Vector<String>>();
			for(int i = 0 ; i < data.length ; i ++){
				Vector<String> temp = new Vector<String>();
				for(int j = 0 ; j < data[i].length ; j ++){
					temp.add(data[i][j]);
				}
				this.myData.add(temp);
			}
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "UserViewTableModel", "UserViewTableModel initialized");
		}

        /**
         * Method returning the number of columns
         *
         * @return the number of columns
         */
		public int getColumnCount() {
			return columnNames.length;
		}

        /**
         * Method returning the number of rows
         *
         * @return the number of rows
         */
		public int getRowCount() {
			return myData.size();
		}

        /**
         * Method returning the names of the columns from its
         * index
         *
         * @param columnIndex the index of the column
         *
         * @return the name of the column
         *
         */
		public String getColumnName(int columnIndex) {
			String colName="";
			if (columnIndex <= getColumnCount())
				colName = columnNames[columnIndex];
			return colName;
		}

        /**
         * Method telling if the cell in the position (rowIndex, columnIndex)
         * is editable or not
         *
         * @param rowIndex
         * @param columnIndex
         *
         * @return the boolean value telling if the cell is editable
         */
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

        /**
         * Method returning the value at the position (rowIndex,columnIndex)
         *
         * @param rowIndex index of the value we want to get
         * @param columnIndex index of the value we want to get
         *
         * @return object of the position (rowIndex,columnIndex)
         */
		public Object getValueAt(int rowIndex, int columnIndex) {
			return (myData.get(rowIndex)).get(columnIndex);
		}
	}

    /**
     * Class changing the rendering of the cells of the user table
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class UserViewTableCellRenderer implements TableCellRenderer{

        /**
         * Method returning a component representing a cell of the table after being rendered
         *
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setText(value.toString());
            label.setOpaque(true);
            label.setForeground(Color.RED);
            if(((String)table.getValueAt(row, 3)).toLowerCase().startsWith("r")){
                label.setBackground(Color.GREEN);
            }
            else{
                label.setBackground(Color.yellow);
            }
            if(isSelected){
                label.setBackground(label.getBackground().darker());
            }
            return label;
        }

    }

	/**
     * Method returning the parent frame
     *
	 * @return the myParent the parent frame
	 */
	public JFrame getMyParent() {
		return myParent;
	}

	/**
     * Method setting the parent frame
     *
	 * @param myParent the myParent to set
	 */
	public void setMyParent(JFrame myParent) {
		this.myParent = myParent;
	}

    /**
     * Method returning the data for the G5k view
     *
     * @return the data to display
     */
	public int[][] getDataForG5k(){
		return dataForG5k;
	}
}