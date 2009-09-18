/****************************************************************************/
/* This class represents the status of the reservation realized             */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ReservationStatusFrame.java,v 1.3 2007/09/28 16:02:44 aamar Exp $
 * $Log: ReservationStatusFrame.java,v $
 * Revision 1.3  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.2  2007/07/12 15:11:33  dloureir
 * Some javadoc
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import diet.gridr.g5k.util.G5kSite;
import diet.gridr.g5k.util.G5kReservation;
import diet.logging.LoggingManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

/**
 * This class represents the status of the reservation realized
 * 
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ReservationStatusFrame extends JFrame {

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 4011398467977319715L;

	/**
	 * JContentPane of the frame
	 */
	private JPanel jContentPane = null;
	/**
	 * JPanel containing the JScrollPane
	 */
	private JPanel jPanel = null;
	/**
	 * JPanel containing the quit button
	 */
	private JPanel jPanel2 = null;
	/**
	 * JScrollPane containing the data to display about the jobs
	 */
	private JScrollPane jScrollPane = null;

	/**
	 * JTable displaying the jobs information
	 */
	private JTable jTable = null;

	/**
	 * Quit JButton
	 */
	private JButton jButton = null;
	
	/**
	 * User data
	 */
	private String[][] userData = null;
	
	/**
	 * Start time label
	 */
	private JLabel startTimeLabel = null;
	/**
	 * Walltime label
	 */
	private JLabel walltimeLabel = null;
	/**
	 * OAR grid sub behaviour label
	 */
	private JLabel oargridsubBehaviourLabel = null;
	/**
	 * Queue label
	 */
	private JLabel queueLabel = null;
	/**
	 * Start time value label
	 */
	private JLabel startTimeValueLabel = null;
	/**
	 * Walltime value label
	 */
	private JLabel walltimeValueLabel = null;
	/**
	 * OAR grid sub behaviour value label
	 */
	private JLabel oargridsubBehaviourValueLabel = null;
	/**
	 * Queue value label
	 */
	private JLabel queueValueLabel = null;
	/**
	 * Panel of information about the grid reservation
	 */
	private JPanel jPanel1 = null;
	/**
	 * Status label
	 */
	private JLabel statusLabel = null;
	/**
	 * Reservation from which we are displaying information
	 */
	private G5kReservation reservation = null;

	/**
	 * Default constructor
	 * 
	 * @param aReservation the reservation to display
	 * @param hashMapOfJobId the HashMap containing the jobId of the reserved jobs
	 */
	public ReservationStatusFrame(G5kReservation aReservation, HashMap<Integer, String> hashMapOfJobId) {
		super();
		reservation = aReservation;
		generateUserData(aReservation, hashMapOfJobId);
		initialize();
	}
	
	/**
	 * Method generating the user data about the realized jobs
	 * 
	 * @param aReservationthe reservation to display
	 * @param hashMapOfJobId the HashMap containing the jobId of the reserved jobs
	 */
	private void generateUserData(G5kReservation aReservation, HashMap<Integer, String> hashMapOfJobId){
		userData = new String[hashMapOfJobId.size()][3];
		Iterator<Integer> iter = hashMapOfJobId.keySet().iterator();
		int index = 0;
		while(iter.hasNext()){
			int jobId = iter.next();
			String oarFrontale = hashMapOfJobId.get(jobId);
			int clusterIndex = G5kSite.getIndexForSite(oarFrontale);
			String clusterName = G5kSite.getSiteForIndex(clusterIndex);
			int numberOfNodes = aReservation.getRequestedNodeCount(clusterIndex);
			userData[index][0] = clusterName;
			userData[index][1] = Integer.toString(numberOfNodes);
			if(jobId>0) userData[index][2] = "OK" ;
			else userData[index][2] = "K0";	      
			index++;
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Reservation status frame");
		this.pack();
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
			jContentPane.add(getJPanel2(), BorderLayout.SOUTH);
			jContentPane.add(getJPanel1(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.weightx = 1.0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJScrollPane(), gridBagConstraints);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(getJButton(), new GridBagConstraints());
		}
		return jPanel2;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JPanel getJTable() {
		JPanel userTablePanel = null;
		if (jTable == null) {
			UserViewTableModel userModel = new UserViewTableModel(userData);
			this.jTable = new JTable();
			this.jTable.setModel(userModel);
			if(jTable.getRowCount() != 0){
				userTablePanel = new JPanel();
				userTablePanel.setLayout(new BoxLayout(userTablePanel,BoxLayout.Y_AXIS));
				userTablePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
				userTablePanel.add(jTable.getTableHeader());
				userTablePanel.add(jTable);
			}
		}
		return userTablePanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton("Quit");
			jButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					dispose();
				}
			});
		}
		return jButton;
	}
	
    /**
     * Class representing the view of the user reservation status
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
		private String [] columnNames = {"Cluster", "Nodes number", "Reservation state"};
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
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
			jPanel1.setLayout(new GridLayout(5,2));
			statusLabel = new JLabel("Reservation summary :");
			statusLabel.setFont(new Font("Courier",Font.BOLD, 20));
			jPanel1.add(statusLabel);
			jPanel1.add(Box.createHorizontalGlue());
			startTimeLabel = new JLabel("Start time : ");
			jPanel1.add(startTimeLabel);
			startTimeValueLabel = new JLabel(reservation.getStartTime());
			jPanel1.add(startTimeValueLabel);
			walltimeLabel = new JLabel("Walltime : ");
			jPanel1.add(walltimeLabel);
			walltimeValueLabel = new JLabel(reservation.getWallTime());
			jPanel1.add(walltimeValueLabel);
			oargridsubBehaviourLabel = new JLabel("OAR grid sub behaviour : ");
			jPanel1.add(oargridsubBehaviourLabel);
			oargridsubBehaviourValueLabel = new JLabel(Boolean.toString(reservation.isOargridsubBehaviour()));
			jPanel1.add(oargridsubBehaviourValueLabel);
			queueLabel = new JLabel("Queue : ");
			jPanel1.add(queueLabel);
			queueValueLabel = new JLabel(reservation.getSelectedQueue());
			jPanel1.add(queueValueLabel);
			
		}
		return jPanel1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
