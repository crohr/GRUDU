/****************************************************************************/
/* This class corresponds to the panel printing some information about a    */
/* cluster                                                                  */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ClusterInfoPanel.java,v 1.15 2007/11/19 15:16:19 dloureir Exp $
 * $Log: ClusterInfoPanel.java,v $
 * Revision 1.15  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.14  2007/10/30 14:11:39  dloureir
 * Integration of the Ganglia plugin
 *
 * Revision 1.13  2007/10/12 13:59:19  dloureir
 * If the site has the OAR2 batch Scheduler, the GanttChart button is not displayed
 *
 * Revision 1.12  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.11  2007/10/05 15:12:59  dloureir
 * changing the border sizes.
 *
 * Revision 1.10  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.9  2007/07/12 12:49:40  dloureir
 * Some javadoc for the code and some clean up of commented code
 *
 * Revision 1.8  2007/06/25 08:18:47  dloureir
 * Adding for the G5KView's graphs and the Cluster's graphs the number of nodes of the user.
 *
 * Revision 1.7  2007/03/26 20:37:48  dloureir
 * Adding a button for the display of a Gantt chart of the jobs of a cluster
 *
 * Revision 1.6  2007/03/07 15:00:14  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.Rotation;

import com.trilead.ssh2.Connection;

import diet.gridr.g5k.util.Config;
import diet.gridr.g5k.util.G5kCfg;
import diet.gridr.g5k.util.G5kSite;
import diet.logging.LoggingManager;

import diet.grid.api.GridJob;
import diet.grid.api.util.HistoryUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

/**
 * This class corresponds to the panel printing some information about a
 * cluster
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ClusterInfoPanel extends JPanel {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = 1L;
    /**
     * Panel containing the the selection chart combobox
     */
	private JPanel jPanel = null;
    /**
     * Panel containing the cluster nodes summary Table
     */
	private JPanel jPanel1 = null;
    /**
     * Data to display
     */
	private int[] data = null;
    /**
     * Name of the cluster
     */
	private String siteName = null;
    /**
     * HashMap containing all jobs
     */
	private Map<String, GridJob> jobsMap = null;
    /**
     * Panel containing the Cluster Jobs Summary table
     */
	private JPanel jPanel2 = null;
    /**
     * Table representing the information about the nodes of the cluster
     */
	private JTable nodesTable = null;
    /**
     * Table representing the information about the jobs of the cluster
     */
	private JTable jobsTable = null;
    /**
     * Model for the table of the cluster jobs summary
     */
	private ClusterJobsSummaryModel jobsModel = null;
    /**
     * Model for the table of the cluster nodes summary
     */
	private ClusterNodesSummaryModel nodesModel = null;
    /**
     * Chart panel for the pie chart
     */
	private ChartPanel myPieChart = null;
    /**
     * Chart panel for the spider chart
     */
	private ChartPanel mySpiderChart = null;
    /**
     * chart panel for the 3D pie chart
     */
	private ChartPanel myPieChart3D = null;
    /**
     * CardLayout for printing of the different charts
     */
	private CardLayout carder = null;
    /**
     * Panel were the different charts can be displayed
     */
	private JPanel cardPanel = null;
    /**
     * Panel for the selection of the chart
     */
	private JPanel selectionChartPanel = null;
    /**
     * JComboBox for the selection of the cart to display
     */
	private JComboBox selectionComboBox = null;

    private Connection connection = null;

	/**
     * Default constructor of the ClusterInfoPanel
     *
     * @param data data to display
     * @param jobsMap map of the jobs of this cluster
     * @param clusterName name of the cluster
	 */
	public ClusterInfoPanel(int[] data, Map<String, GridJob> jobsMap, String siteName,Connection connection) {
		super();
        this.connection = connection;
		this.data = data;
		this.siteName = siteName;
		this.jobsMap = jobsMap;
		initialize();
		LoggingManager.log(Level.FINE, 
		        LoggingManager.RESOURCESTOOL, 
		        this.getClass().getName(), 
		        "ClusterInfoPanel", 
		        "ClusterInfoPanel for cluster " + siteName + " created");
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getJPanel1(), BorderLayout.CENTER);
		this.add(getJPanel2(), BorderLayout.SOUTH);
		this.add(getJPanel(), BorderLayout.NORTH);
		validate();
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ClusterInfoPanel", "ClusterInfoPanel for cluster "+siteName+" initialized");
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
			jPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jPanel.add(getSelectionChartPanel());
			jPanel.add(Box.createVerticalStrut(10));
			jPanel.add(getCardPanel());
			jPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		}
		return jPanel;
	}

    /**
     * Method returning a Panel that can be displayed the different chart panel
     *
     * @return cardPanel
     */
	private JPanel getCardPanel(){
		if(cardPanel == null){
			cardPanel = new JPanel();
			carder = new CardLayout();
			cardPanel.setLayout(carder);
			cardPanel.add("Pie Chart", new ClusterPieChart());
			cardPanel.add("Spider Chart",new SpiderWebChart());
			cardPanel.add("Pie Chart 3D",new PieChart3D());
			carder.show(cardPanel, "Pie Chart");
			LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getCardPanel", "Charts added");
		}
		return cardPanel;
	}

    /**
     * Method returning the selectionChartPanel
     *
     * @return the selection chart panel
     */
	private JPanel getSelectionChartPanel(){
		if(selectionChartPanel == null){
			selectionChartPanel = new JPanel();
			selectionChartPanel.setLayout(new BoxLayout(selectionChartPanel,BoxLayout.X_AXIS));
			JLabel selectionChartLabel = new JLabel("Available charts: ");
			selectionChartPanel.add(selectionChartLabel);
			selectionChartPanel.add(Box.createHorizontalStrut(10));
			selectionChartPanel.add(getSelectionComboBox());
			selectionChartPanel.add(Box.createHorizontalGlue());
            
			if(Config.getBatchScheduler(G5kSite.getIndexForSite(siteName)).equalsIgnoreCase("OAR1")){
				JButton button = new JButton("Jobs Gantt Chart");
				selectionChartPanel.add(button);
				button.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Iterator<String> iter = jobsMap.keySet().iterator();
						ArrayList<GridJob> jobsList = new ArrayList<GridJob>();
						while(iter.hasNext()){
							jobsList.add(jobsMap.get(iter.next()));
						}
						new GanttChart("Jobs Gantt Chart",siteName,jobsList,connection);
					}
				});
			}
		}
		return selectionChartPanel;
	}

    /**
     * Method returning the JComboBox for the selection of the chart
     *
     * @return a JComboBox allowing the user to selected the chat he wants
     */
	private JComboBox getSelectionComboBox(){
		if(selectionComboBox == null){
			ArrayList<String> listOfCharts = new ArrayList<String>();
			listOfCharts.add("Pie Chart");
			listOfCharts.add("Spider Chart");
			listOfCharts.add("Pie Chart 3D");
			selectionComboBox = new JComboBox(listOfCharts.toArray(new String[0]));
			selectionComboBox.addActionListener(new ActionListener(){
				/* (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					String selectionChartType = (String)selectionComboBox.getSelectedItem();
					LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Selected Chart is : " + selectionChartType);
					carder.show(cardPanel, selectionChartType);
				}
			});
		}
		return selectionComboBox;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(jPanel1,BoxLayout.X_AXIS));
			jPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			JPanel intermediaryPanel = new JPanel();
			intermediaryPanel.setLayout(new BoxLayout(intermediaryPanel,BoxLayout.Y_AXIS));
			nodesTable = new JTable();
			nodesModel = new ClusterNodesSummaryModel();
			nodesTable.setModel(nodesModel);
			JLabel nodesTableTitle = new JLabel("Nodes status");
			nodesTableTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			nodesTableTitle.setFont(new Font("Dialog",Font.BOLD,14));
			intermediaryPanel.add(Box.createVerticalStrut(5));
			intermediaryPanel.add(nodesTableTitle);
			intermediaryPanel.add(Box.createVerticalStrut(10));
			intermediaryPanel.add(nodesTable.getTableHeader());
			intermediaryPanel.add(nodesTable);
			intermediaryPanel.add(Box.createVerticalStrut(10));
			intermediaryPanel.add(new JSeparator(JSeparator.HORIZONTAL));
			jPanel1.add(Box.createHorizontalGlue());
			jPanel1.add(intermediaryPanel);
			jPanel1.add(Box.createHorizontalGlue());
			LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getJPanel1","Cluster nodes summary table added");
		}
		return jPanel1;
	}

    /**
     * Class defining the JPanel for the Pie Chart
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
	public class ClusterPieChart extends JPanel{

        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
		private static final long serialVersionUID = 1L;

        /**
         * Default constructor of the Cluster Pie chart
         *
         */
		public ClusterPieChart(){
			DefaultPieDataset dataset = new DefaultPieDataset();
			dataset.setValue("Free", data[0]);
			dataset.setValue("Job", (data[1]-getNumberOfNodesForTheCurrentUser()));
			dataset.setValue("Suspected", data[2]);
			dataset.setValue("Dead", data[3]);
			dataset.setValue("Absent", data[4]);
			if(getNumberOfNodesForTheCurrentUser() != 0){
				String userID = G5kCfg.get(G5kCfg.USERNAME);
				dataset.setValue("Nodes of " + userID, getNumberOfNodesForTheCurrentUser());
			}



//			 create a chart...
			JFreeChart chart = ChartFactory.createPieChart(
			"Status of "+siteName,
			dataset,
			true, // legend?
			true, // tooltips?
			false // URLs?
			);
			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setSectionPaint("Free", Color.GREEN);
			plot.setSectionPaint("Job", Color.BLUE);
			plot.setSectionPaint("Suspected", Color.PINK);
			plot.setSectionPaint("Dead",Color.RED);
			plot.setSectionPaint("Absent",Color.YELLOW);
			if(data[5] != 0){
				String userID = G5kCfg.get(G5kCfg.USERNAME);
				plot.setSectionPaint("Nodes of " + userID, Color.CYAN);
			}
			plot.setIgnoreNullValues(true);
			plot.setIgnoreZeroValues(true);
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0} : {1} ({2})"));
			plot.setLabelBackgroundPaint(new Color(220, 220, 220));
			myPieChart = new ChartPanel(chart);
			myPieChart.setDisplayToolTips(false);
			myPieChart.setVisible(true);
			add(myPieChart);
			Dimension dim2 = jobsTable.getPreferredSize();
			Dimension dim = myPieChart.getPreferredSize();
			double scale = (double)dim2.width/(double)dim.width;
			dim.width = dim2.width;
			dim.height = (int)(scale*dim.height);
			myPieChart.setSize(dim);
			myPieChart.setPreferredSize(dim);
			myPieChart.setMaximumSize(dim);
			myPieChart.setMinimumSize(dim);
			myPieChart.setVisible(true);
			LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ClusterPieChart", "Cluster pie chart created");
		}
	}

    /**
     * Class representing the Model of the ClusterNodesSummary
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
	public class ClusterNodesSummaryModel extends AbstractTableModel{

	    /**
	     * serialVersionUID defines the version for the serialisation of
	     * this object
	     */
	    private static final long serialVersionUID = 1L;
	    /**
	     * The data to display
	     */
	    private Vector<String> myData =new Vector<String>();
	    /**
	     * The array containing the column names
	     */
	    private String [] columnNames = {"Free", "Jobs", "Suspected","Dead","Absent", "Total", "Total usable"};
	    /**
	     * Default constructor of the ClusterNodesSummaryModel
	     *
	     */
	    public ClusterNodesSummaryModel() {
	        this.myData = new Vector<String>();
	        for(int i = 0 ; i < data.length ; i ++){
	            myData.add(Integer.toString(data[i]));
	        }
	        myData.add(Integer.toString(data[0]+data[1]+data[4]));
	        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"ClusterNodesSummaryModel" , "ClusterNodesSummaryModel initialization");
	    }

	    /**
	     * Method returning the name of a column from its index
	     *
	     * @param columnIndex index of the column
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
	     * Method telling if a cell is editable
	     *
	     * @param rowIndex index of the row
	     * @param columnIndex index of the column
	     *
	     * @return boolean value telling if the cell is editable
	     *
	     */
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
	        return false;
	    }

	    /**
	     * Method returning the number of columns
         *
         * @return the number of columns of the table
	     */
	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    /**
	     * Method returning the number of rows
	     *
	     * return 1 (because there is only one row)
	     */
	    public int getRowCount() {
	        return 1;
	    }

	    /**
	     * Method returning the value at the cell (rowIndex, columnIndex)
         *
         * @param rowIndex index of the row
         * @param columnIndex index of the column
         *
         * @return object placed at the the position (rowIndex,columnIndex)
         *
	     */
	    public Object getValueAt(int rowIndex, int columnIndex) {
	        return (myData.get(columnIndex));
	    }
	    /**
	     * Method returning the class of the elements of the column
	     *
	     * @param columnIndex index of the column
	     *
	     * @return type of the element of the column
	     */
	    public Class getColumnClass(int columnIndex){
	        return String.class;
	    }
	}

    /**
     * Class representing the model of the Cluster JobsSummary Table
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
	public class ClusterJobsSummaryModel extends AbstractTableModel{
	    /**
		 * 
		 */
		private static final long serialVersionUID = -6262897193948732440L;
		/**
	     * Data to be displayed
	     */
	    private Vector<Vector<String>> myData;
	    /**
	     * Array containing the title of the columns
	     */
	    private String[] columnsNames = {"Id", "User", "Queue", "NbNodes", "Type", "Properties", "Walltime", "SubTime", "StTime", "SchStart"};
	    /**
	     * Default constructor of the Model of the ClusterJobsSummary
	     *
	     */
	    public ClusterJobsSummaryModel(){
	        myData = new Vector<Vector<String>>();
	        Iterator<String> iter = jobsMap.keySet().iterator();
	        while(iter.hasNext()){
	            String temp = iter.next();
	            Vector<String> tempVector =new Vector<String>();
	            GridJob job = jobsMap.get(temp);
	            // String jobId = job.jobId.substring(0, job.jobId.indexOf("."));
	            String jobId = job.getParameterValue(GridJob.KEY_GRID_JOB_ID);
	            tempVector.add(jobId);
	            //tempVector.add(job.jobOwner);
	            tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_OWNER));
	            //tempVector.add(job.jobQueue);
	            tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_QUEUE));
	            //tempVector.add(job.jobNbNodes);
	            tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT));
	            //tempVector.add(job.jobType);
	            tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_TYPE));
	            //tempVector.add(job.jobProperties); // TODO:
	            tempVector.add("");
	            // tempVector.add(job.jobWallTime);
	            long time =0;
	            Date date = null;
	            try{
	            	time = Long.parseLong(job.getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME))*1000;
	            	tempVector.add(HistoryUtil.getWallTimeFromDate(time));
	            } catch(Exception e){
	            	tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME));
	            }
	            
	            try{
	            	time = Long.parseLong(job.getParameterValue(GridJob.KEY_GRID_JOB_SUBTIME))*1000;
	            	date = new Date(time);
	            	tempVector.add(HistoryUtil.getOARDateFromDate(date));
	            }catch(Exception e){
	            	tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_SUBTIME));
	            }
	            
	            try{
	            	time = Long.parseLong(job.getParameterValue(GridJob.KEY_GRID_JOB_STARTTIME))*1000;
	            	date = new Date(time);
	            	tempVector.add(HistoryUtil.getOARDateFromDate(date));
	            }catch(Exception e){
	            	tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_STARTTIME));
	            }
	            
	            try{
	            	time = Long.parseLong(job.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME))*1000;
	            	date = new Date(time);
	            	tempVector.add(HistoryUtil.getOARDateFromDate(date));
	            }
	            catch(Exception e){
	            	tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME));
	            }
	            myData.add(tempVector);
	        }
	        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"ClusterJobsSummaryModel" , "ClusterJobsSummaryModel initialization");
	    }
	    /**
	     * Method returning the class of the elements of the column
	     *
	     * @param columnIndex index of the column
	     *
	     * @return type of the element of the column
	     */
	    public Class getColumnClass(int columnIndex){
	        return String.class;
	    }
	    /**
	     * Method returning the number of columns
	     *
	     * @return the number of columns of the table
	     */
	    public int getColumnCount() {
	        // TODO Auto-generated method stub
	        return columnsNames.length;
	    }

        /**
         * Method returning the number of rows
         *
         * return the number of rows
         */
	    public int getRowCount() {
	        // TODO Auto-generated method stub
	        return myData.size();
	    }

        /**
         * Method returning the value at the cell (rowIndex, columnIndex)
         *
         * @param rowIndex index of the row
         * @param columnIndex index of the column
         *
         * @return object placed at the the position (rowIndex,columnIndex)
         *
         */
	    public Object getValueAt(int rowIndex, int columnIndex) {
	        return myData.get(rowIndex).get(columnIndex);
	    }

        /**
         * Method returning the name of the column
         *
         * @param columnIndex of the column
         *
         * @return name of the column corresponding to the selected index
         */
	    public String getColumnName(int columnIndex) {
	        String colName="";
	        if (columnIndex <= getColumnCount())
	            colName = columnsNames[columnIndex];
	        return colName;
	    }

        /**
         * Method telling if a cell is editable
         *
         * @param rowIndex index of the row
         * @param columnIndex index of the column
         *
         * @return boolean value telling if the cell is editable
         *
         */
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
	        return false;
	    }
	}

    /**
     * Class corresponding to the renderer of the ClusterJobsSummary Cell
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class ClusterJobsSummaryCellRenderer implements TableCellRenderer{

        /**
         * Method returning a component rendered
         *
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setText(value.toString());
            boolean bool = ((String)table.getValueAt(row, 1)).equalsIgnoreCase(Config.getUser());
            if(bool){
                label.setOpaque(true);
                label.setForeground(Color.RED);
                GridJob job = jobsMap.get((String)table.getValueAt(row, 0));
                if ( (job != null) &&
                        (job.getParameterValue(GridJob.KEY_GRID_JOB_STATE) != null) && 
                        (job.getParameterValue(GridJob.KEY_GRID_JOB_STATE).toLowerCase().startsWith("r")) 
                        ) {
                    label.setBackground(Color.GREEN);
                }
                else{
                    label.setBackground(Color.YELLOW);
                }
            }
            return label;
        }
    }

	/**
	 * This method initializes jPanel2
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(jPanel2,BoxLayout.Y_AXIS));
			jPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jobsTable = new JTable();
			jobsModel = new ClusterJobsSummaryModel();
			jobsTable.setModel(jobsModel);
            ClusterJobsSummaryCellRenderer renderer = new ClusterJobsSummaryCellRenderer();
            jobsTable.setDefaultRenderer(String.class, renderer);
			//jobsTable.createDefaultColumnsFromModel();
			JLabel jobsTableTitle = new JLabel("Jobs status");
			jobsTableTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			jobsTableTitle.setFont(new Font("Dialog",Font.BOLD,14));
			jPanel2.add(Box.createVerticalStrut(5));
			jPanel2.add(jobsTableTitle);
			jPanel2.add(Box.createVerticalStrut(10));
			jPanel2.add(jobsTable.getTableHeader());
			jPanel2.add(jobsTable);
			LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getJPanel2","Cluster jobs summary table added");
		}
		return jPanel2;
	}


	/**
	 * Class representing a Spider Chart
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
	 */
	public class SpiderWebChart extends JPanel{

	    /**
		 * 
		 */
		private static final long serialVersionUID = 2567242386899115838L;

		/**
	     * Creates a instance of a SpiderWebChart
	     *
	     */
	    public SpiderWebChart() {
	        createPanel();
	        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "SpiderWebChart", "Spider web chart created");
		}

	    /**
	     * Returns the dataset.
	     *
	     * @return The dataset.
	     */
	    private CategoryDataset createDataset() {

	        // create the dataset...
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        String series1 = "Nodes number";
	        NumberFormat format = NumberFormat.getPercentInstance();
			dataset.addValue(data[0],series1,"Free : "+data[0] + " (" +format.format((double)data[0]/(double)data[5]) + ")");
			dataset.addValue(data[1],series1,"Job : "+ (data[1]-getNumberOfNodesForTheCurrentUser()) + " (" + format.format((double)(data[1]-getNumberOfNodesForTheCurrentUser())/(double)data[5]) + ")");
			dataset.addValue(data[2],series1,"Suspected : "+data[2] + " (" + format.format((double)data[2]/(double)data[5]) + ")");
			dataset.addValue(data[3],series1,"Dead : "+data[3] + " (" +format.format((double)data[3]/(double)data[5]) + ")");
			dataset.addValue(data[4],series1,"Absent : "+data[4] + " (" + format.format((double)data[4]/(double)data[5]) + ")");
			if(getNumberOfNodesForTheCurrentUser() != 0){
				String userID = G5kCfg.get(G5kCfg.USERNAME);
				dataset.addValue(data[5],series1,"Nodes of "+userID+" : "+getNumberOfNodesForTheCurrentUser() + " (" + format.format((double)getNumberOfNodesForTheCurrentUser()/(double)data[5]) + ")");
			}
	        return dataset;

	    }

	    /**
	     * Creates a chart.
	     *
	     * @param dataset the dataset.
	     *
	     * @return The chart.
	     */
	    private JFreeChart createChart(CategoryDataset dataset) {
	        SpiderWebPlot plot = new SpiderWebPlot(dataset);
	        plot.setStartAngle(3);
	        plot.setInteriorGap(0.40);
	        plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
	        JFreeChart chart = new JFreeChart("Status of "+siteName,
	                TextTitle.DEFAULT_FONT, plot, false);
	        LegendTitle legend = new LegendTitle(plot);
	        legend.setPosition(RectangleEdge.BOTTOM);
	        chart.addSubtitle(legend);
	        return chart;

	    }

	    /**
	     * Creates a panel
	     *
	     */
	    public void createPanel() {
	        JFreeChart chart = createChart(createDataset());
	        mySpiderChart = new ChartPanel(chart);
	        mySpiderChart.setDisplayToolTips(false);
			mySpiderChart.setVisible(true);
	        add(mySpiderChart);
			Dimension dim2 = jobsTable.getPreferredSize();
			Dimension dim = mySpiderChart.getPreferredSize();
			double scale = (double)dim2.width/(double)dim.width;
			dim.width = dim2.width;
			dim.height = (int)(scale*dim.height);
			mySpiderChart.setSize(dim);
			mySpiderChart.setPreferredSize(dim);
			mySpiderChart.setMaximumSize(dim);
			mySpiderChart.setMinimumSize(dim);
			mySpiderChart.setVisible(true);
	    }
	}

	/**
	 * Class representing a 3D Pie Chart
     *
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
	 */
	public class PieChart3D extends JPanel{

	    /**
		 * 
		 */
		private static final long serialVersionUID = 355086143464465818L;

		/**
	     * Creates a instance of the 3D Pie Chart
	     *
	     */
	    public PieChart3D() {
	    	super();
	        PieDataset dataset = createDataset();
	        JFreeChart chart = createChart(dataset);
	        myPieChart3D = new ChartPanel(chart);
	        myPieChart3D.setDisplayToolTips(false);
			myPieChart3D.setVisible(true);
			Dimension dim2 = jobsTable.getPreferredSize();
			Dimension dim = myPieChart3D.getPreferredSize();
			double scale = (double)dim2.width/(double)dim.width;
			dim.width = dim2.width;
			dim.height = (int)(scale*dim.height);
			myPieChart3D.setSize(dim);
			myPieChart3D.setPreferredSize(dim);
			myPieChart3D.setMaximumSize(dim);
			myPieChart3D.setMinimumSize(dim);
			myPieChart3D.setVisible(true);
	        add(myPieChart3D);
	        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "PieChart3D", "3D pie chart created");
		}

	    /**
	     * Creates a dataset
	     *
	     * @return A dataset.
	     */
	    private PieDataset createDataset() {

	        DefaultPieDataset dataset = new DefaultPieDataset();
			dataset.setValue("Free", data[0]);
			dataset.setValue("Job", data[1]-getNumberOfNodesForTheCurrentUser());
			dataset.setValue("Suspected", data[2]);
			dataset.setValue("Dead", data[3]);
			dataset.setValue("Absent", data[4]);
			if(getNumberOfNodesForTheCurrentUser() != 0){
				String userID = G5kCfg.get(G5kCfg.USERNAME);
				dataset.setValue("Nodes of : " + userID, getNumberOfNodesForTheCurrentUser());
			}
	        return dataset;

	    }

	    /**
	     * Creates a chart.
	     *
	     * @param dataset the dataset.
	     *
	     * @return A chart.
	     */
	    private JFreeChart createChart(PieDataset dataset) {

	        JFreeChart chart = ChartFactory.createPieChart3D(
	            "Status on : " + siteName,  // chart title
	            dataset,                // data
	            true,                   // include legend
	            true,
	            false
	        );

	        PiePlot3D plot = (PiePlot3D) chart.getPlot();
	        plot.setDirection(Rotation.CLOCKWISE);
	        plot.setForegroundAlpha(0.5f);
	        plot.setNoDataMessage("No data to display");
			plot.setSectionPaint("Free", Color.GREEN);
			plot.setSectionPaint("Job", Color.BLUE);
			plot.setSectionPaint("Suspected", Color.PINK);
			plot.setSectionPaint("Dead",Color.RED);
			plot.setSectionPaint("Absent",Color.YELLOW);
			String userID = G5kCfg.get(G5kCfg.USERNAME);
			plot.setSectionPaint("Nodes of : " + userID,Color.CYAN);
			plot.setIgnoreNullValues(true);
			plot.setIgnoreZeroValues(true);
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0} : {1} ({2})"));
			plot.setLabelBackgroundPaint(new Color(220, 220, 220));
	        return chart;

	    }

	    /**
	     * Creates a panel
	     *
	     * @return A panel.
	     */
	    public JPanel createDemoPanel() {
	        JFreeChart chart = createChart(createDataset());
	        return new ChartPanel(chart);
	    }
	}
	
	/**
	 * Method returning the number of nodes of the user. It is used to display
	 * the user occupation of the cluster.
	 * 
	 * @return the number of user nodes
	 */
	private int getNumberOfNodesForTheCurrentUser(){
		int numberOfNodes = 0;
		Iterator<String> iter = jobsMap.keySet().iterator();
		while(iter.hasNext()){
			String idJob = iter.next();
			GridJob job = jobsMap.get(idJob);
			String jobOwner = job.getParameterValue(GridJob.KEY_GRID_JOB_OWNER);
			if(jobOwner.equalsIgnoreCase(G5kCfg.get(G5kCfg.USERNAME))){
				if (job.getParameterValue(GridJob.KEY_GRID_JOB_STATE).toLowerCase().startsWith("r")) 
				    numberOfNodes += Integer.parseInt(job.getParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT));
			}
		}
		return numberOfNodes;
	}
}