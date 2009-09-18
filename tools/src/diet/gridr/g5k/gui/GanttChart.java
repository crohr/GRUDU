/****************************************************************************/
/* A Gantt Chart for the occupation of the cluster by the running and       */
/* waiting jobs                                                             */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: GanttChart.java,v 1.8 2007/11/19 15:16:19 dloureir Exp $
 * $Log: GanttChart.java,v $
 * Revision 1.8  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.7  2007/10/12 13:57:11  dloureir
 * Changing the location of the HistoryUtil class
 *
 * Revision 1.6  2007/10/09 13:41:23  dloureir
 * Moving the parsing of the oar history from the GanttChart class to the oar1 scheduler and modifications of the history command for all shceduler in the batchScheduler class
 *
 * Revision 1.5  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.4  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.3  2007/07/12 14:40:17  dloureir
 * Some typo corrections and the clean up of some unused code
 *
 * Revision 1.2  2007/04/02 09:53:10  dloureir
 * Adding documentation to the Gantt chart
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.experimental.chart.renderer.LookupPaintScale;
import org.jfree.experimental.chart.renderer.xy.XYBlockRenderer;
import org.jfree.ui.RectangleAnchor;

import com.trilead.ssh2.Connection;

import diet.gridr.g5k.util.Config;
import diet.gridr.g5k.util.G5kSite;
import diet.logging.LoggingManager;
import diet.util.gui.WaitingFrame;

import diet.grid.api.GridJob;
import diet.grid.api.oar1.Oar1Job;
import diet.grid.api.util.HistoryUtil;

/**
 * This class represents the Gantt chart drawing the occupation of
 * the cluster by jobs running or waiting
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class GanttChart extends JFrame {

    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Number of nodes for the chart (set to a dummy value : 50)
     */
    private int numberOfNodes = 50;
    /**
     * Duration for the visualization
     */
    private int visualizationDuration = ONE_DAY;
    /**
     * Selection value for a visualization duration of six hours
     */
    private final static int SIX_HOURS = 0;
    /**
     * Selection value for a visualization duration of twelve hours
     */
    private final static int TWELVE_HOURS = 1;
    /**
     * Selection value for a visualization duration of one day
     */
    private final static int ONE_DAY = 2;
    /**
     * Selection value for a visualization duration of two days
     */
    private final static int TWO_DAYS = 3;
    /**
     * Selection value for a visualization duration of one week
     */
    private final static int ONE_WEEK = 4;
    /**
     * Selection value for a visualization duration of two weeks
     */
    private final static int TWO_WEEKS = 5;
    /**
     * Selection value for a visualization duration of one month
     */
    private final static int ONE_MONTH = 6;
    /**
     * array containing the string corresponding to the durations for the gantt chart
     */
    private static String[] durationsStringArray = { "six hours",
            "twelve hours",
            "one day",
            "two days",
            "one week",
            "two weeks",
            "one month",
    };
    /**
     * array containing the formatters corresponding to the durations for the gantt chart
     */
    private static String[] durationsFormatterArray = { "HH:mm",
        "HH:mm",
        "HH:mm",
        "HH:mm",
        "HH:mm - dd",
        "dd-MMM",
        "dd-MMM"
    };
    /**
     * List of jobs
     */
    private ArrayList<ArrayList<GridJob>> jobsList = null;
    /**
     * selection panel
     */
    private JPanel selectionPanel = null;
    /**
     * selection label
     */
    private JLabel selectionLabel = null;
    /**
     * comboBox containing the duration strings
     */
    private JComboBox selectionComboBox = null;
    /**
     * The panel containing the jobs table
     */
    private JPanel tablePanel = null;
    /**
     * The jobs table containing the information about the jobs
     */
    private JTable jobsTable = null;
    /**
     * Model for the jobs of the cluster
     */
    private GanttChart.ClusterJobsSummaryModel jobsModel;
    /**
     * CardLayout for the gantt chart
     */
    private CardLayout ganttChartLayout = null;
    /**
     * Name of the cluster
     */
    private String siteName = null;
    /**
     * Different panel for the charts of the different durations
     */
    private JPanel[] chartPanels = null;
    /**
     * Panel for the cardLayout
     */
    private JPanel cardPanel = null;
    /**
     * Connection for the retrieving of information
     */
    private Connection connection =  null;

    /**
     * Method realizing a copy of the jobsList
     *
     * @param aJobList a JobList to copy
     * @return a copy of the jobsList
     */
    private static ArrayList<GridJob> makeACopyOf(ArrayList<GridJob> aJobList){
        ArrayList<GridJob> aCopyOfTheList = new ArrayList<GridJob>();
        for(int i = 0 ; i < aJobList.size() ; i++) aCopyOfTheList.add(aJobList.get(i).copy());
        return aCopyOfTheList;
    }

    /**
     * Default constructor of the GanttChart
     *
     * @param title title of the GanttChart
     * @param aSiteName name of the cluster
     * @param aJobsList list of Jobs for the cluster
     * @param connection ssh connection
     */
    public GanttChart(String title,String aSiteName,ArrayList<GridJob> aJobsList,Connection connection) {
        super(title);
        this.connection= connection;
        jobsList = new ArrayList<ArrayList<GridJob>>();
        for(int i = 0 ; i < HistoryUtil.durationsTimesArray.length; i++){
            jobsList.add(makeACopyOf(aJobsList));
        }
        // numberOfNodes = G5kCluster.getCapacityForIndex(G5kCluster.getIndexForCluster(aClusterName));
        numberOfNodes = G5kSite.getResCount(G5kSite.getIndexForSite(aSiteName));
        siteName = aSiteName;
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(getSelectionPanel(),BorderLayout.NORTH);
        getContentPane().add(getTablePanel(),BorderLayout.SOUTH);
        getContentPane().add(getCardPanel(),BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "GanttChart", "GanttChart constructed");
    }

    /**
     * Method returning the TablePanel
     *
     * @return the table panel
     */
    private JPanel getTablePanel(){
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        jobsTable = new JTable();
        jobsModel = new ClusterJobsSummaryModel();
        jobsTable.setModel(jobsModel);
        ClusterJobsSummaryCellRenderer renderer = new ClusterJobsSummaryCellRenderer();
        jobsTable.setDefaultRenderer(String.class, renderer);
        JLabel jobsTableTitle = new JLabel("Jobs status");
        jobsTableTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        jobsTableTitle.setFont(new Font("Dialog",Font.BOLD,14));
        tablePanel.add(Box.createVerticalStrut(5));
        tablePanel.add(jobsTableTitle);
        tablePanel.add(Box.createVerticalStrut(10));
        tablePanel.add(jobsTable.getTableHeader());
        tablePanel.add(jobsTable);
        LoggingManager.log(Level.FINE,LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getTablePanel", "TablePanel constructed");
        return tablePanel;
    }

    /**
     * Method returning the card panel
     *
     * @return the card panel
     */
    private JPanel getCardPanel(){
        cardPanel = new JPanel();
        ganttChartLayout = new CardLayout();
        cardPanel.setLayout(ganttChartLayout);
        final WaitingFrame waiting = new WaitingFrame("Waiting frame", "Creating the gantt charts for this cluster ...", durationsStringArray.length + 1,true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Thread thread = new Thread(new Runnable(){
                    public void run(){
                        waiting.setStatusText("Operation : ");
                        waiting.launch("running oar stat history command ...");
                        OarHistoryRunnable historyRunnable = null;
                        chartPanels = new JPanel[durationsStringArray.length];
                        for(int i = 0 ; i < durationsStringArray.length ; i ++){
                            waiting.setStatusText("retrieving OAR history for " + durationsStringArray[i]);
                            waiting.incrementProgressBar();
                            Date startDate = GregorianCalendar.getInstance().getTime();
                            Date endDate = new Date(startDate.getTime()+HistoryUtil.durationsTimesArray[i]);
                            historyRunnable = new OarHistoryRunnable(connection,
                            		jobsList.get(i),
                            		Config.getBatchScheduler(G5kSite.getIndexForSite(siteName)),
                                    G5kSite.getInternalFrontalForSite(siteName),
                                    startDate,
                                    endDate);
                            Thread th = new Thread(historyRunnable);
                            th.start();
                            try{
                                th.join();
                            }
                            catch(Exception e){
                                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);
                            }
                            jobsList.set(i, historyRunnable.getJobsList());
//                            Iterator<String> iter = historyLines.iterator();
//                            /*
//                             * The history is separated into three parts:
//                             *
//                             *  - the first is the free nodes written as follows :
//                             *  'node1' 'weightOfNode1' 'node2' 'weightOfNode2' ...
//                             *   eg :
//                             *  'sagittaire-12.lyon.grid5000.fr' '2' 'sagittaire-13.lyon.grid5000.fr' '2'
//                             *
//                             *  - the second is a list of jobs :
//                             *  'job id' 'type' 'user' 'state' 'command' 'properties' 'start time' 'queue' 'submission time' 'end time' 'node1' 'weightOfNode1' 'node2' 'weightOfNode2' ...
//                             *   e.g. :
//                             *   '20081' 'PASSIVE' 'ssoudan' 'Running' '/bin/sleep 72000' '(p.deploy = "YES") AND p.deploy = "YES" ' '2007-03-23 10:18:48' 'deploy' '2007-03-23 10:19:03' '2007-03-24 06:18:48' 'sagittaire-19.lyon.grid5000.fr' '2' 'sagittaire-24.lyon.grid5000.fr' '2'
//                             *
//                             *  - the last one is a list of dead jobs :
//                             *  the last part as the same pattern as the jobs
//                             *  '0' 'Dead' 'oar' 'null' 'null' 'null' '0000-00-00 00:00:00' 'default' '2007-03-22 19:30:02' '2007-03-24 14:00:00' 'sagittaire-60.lyon.grid5000.fr' '2'
//                             *
//                             */
//                            // for all jobs
//                            while(iter.hasNext()){
//                                String historyLine = iter.next();
//                                historyLine = historyLine.replace("''","' '");
//                                StringTokenizer tokenizer = new StringTokenizer(historyLine,"'");
//                                ArrayList<String> information = new ArrayList<String>();
//                                while(tokenizer.hasMoreTokens()){
//                                    information.add(tokenizer.nextToken());
//                                }
//                                int jobNumber=-1;
//                                try{
//                                    // if no exception is thrown then it means that we are no! in the first
//                                    // part of the history
//                                    jobNumber = Integer.parseInt(information.get(0));
//                                }
//                                catch(Exception e){
//                                    LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);
//                                }
//                                if(jobNumber == 0){
//                                    // this is a dead of absent node
//                                    GridJob aJob = new Oar1Job();
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_HOSTS, information.get(20));
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_ID, "0");
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_OWNER, "");
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_QUEUE, "");
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_STATE, "");
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT, "1");
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME,
//                                            GanttChart.getOARDateFromDate(Calendar.getInstance().getTime()));//information.get(12);
//                                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_WALLTIME,
//                                            getWallTimeFromDate(HistoryUtil.durationsTimesArray[i]));//ntheWallTime;
//                                    jobsList.get(i).add(aJob);
//                                }
//                                else if (jobNumber != -1){
//                                    // this is not a dead or absent or suspected job but a classical job
//                                    GridJob aJob = null;
//                                    //searching the job
//                                    for(int j = 0 ;j < jobsList.get(i).size() ; j++){
//                                        aJob = jobsList.get(i).get(j);
//                                        String firstParam = aJob.getParameterValue(GridJob.KEY_GRID_JOB_ID);
//                                        if(jobNumber == Integer.parseInt(firstParam)){
//                                            j=jobsList.get(i).size();
//                                        }
//                                    }
//                                    // if the job is waiting
//                                    if(aJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE).substring(0, 1).equalsIgnoreCase("w")){
//                                        ArrayList<String> theNodes = new ArrayList<String>();
//                                        for(int index = 20 ; index < information.size() ; index +=4){
//                                            theNodes.add(information.get(index));
//                                        }
//
//                                        if(theNodes.size() >= 1){
////                                            String nodesOfTheJob = theNodes.get(0);
////                                            for(int anIndex = 1 ; anIndex < theNodes.size(); anIndex ++){
////                                                nodesOfTheJob += "+" + theNodes.get(anIndex);
////                                            }
//                                            // TODO: check this operation (don't understand how use it)
//                                            aJob.setHostsFromArray(theNodes.toArray(new String[0]));
//                                        }
//                                    }
//                                }
//                            }

                            waiting.setStatusText("creating chart for " + durationsStringArray[i]);
                            visualizationDuration = i;
                            chartPanels[i] = new ChartPanel(createChart(createDataset()));
                            chartPanels[i].setPreferredSize(new java.awt.Dimension(800, 600));
                            chartPanels[i].setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
                            cardPanel.add(durationsStringArray[i],chartPanels[i]);
                        }
                        visualizationDuration = ONE_DAY;
                        ganttChartLayout.show(cardPanel,durationsStringArray[visualizationDuration]);
                        waiting.dispose();
                        GanttChart.this.pack();
                        GanttChart.this.setVisible(true);
                        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "GanttChart initialized");
                    }
                }
                );
                thread.start();
            }
        });
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getCardPanel", "CardPanel initialized");
        return cardPanel;
    }

    /**
     * Method printing to files the jobs
     *
     */
    private void printToFile(){
        try{
            for(int i = 0 ; i < durationsStringArray.length ; i++){
                ArrayList<GridJob>  aJobList = jobsList.get(i);
                PrintWriter ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(System.getProperty("user.home") + durationsStringArray[i] + ".txt")));
                for(int j = 0 ;j < aJobList.size() ; j++){
                    GridJob aJob = aJobList.get(j);
                    ecrivain.println("---");
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_ID));
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT));
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_HOSTS));
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_OWNER));
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_QUEUE));
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME));
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE));
                    ecrivain.println(aJob.getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME));
                }
                ecrivain.close();
            }
        }
        catch(Exception e){
            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "printFile", e);
        }
    }

    /**
     * Method returning the nodes from the OAR history
     *
     * @param nodesList list of nodes
     * @return an array of nodes
     */
    private String[] getNodesFromOARHistory(ArrayList<String> nodesList){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0 ; i < nodesList.size()/2 ; i ++){
            list.add(nodesList.get(2*i));
        }
        String[] result = new String[list.size()];
        for(int i = 0 ; i < list.size() ; i ++){
            result[i] = list.get(i);
        }
        return result;
    }

    /**
     * Method returning the selection panel for the Gantt chart
     *
     * @return the selection Panel for the Gantt chart
     */
    private JPanel getSelectionPanel(){
        if(selectionPanel == null){
            selectionPanel = new JPanel();
            selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            selectionPanel.setLayout(new BoxLayout(selectionPanel,BoxLayout.X_AXIS));
            selectionLabel = new JLabel("Select the duration : ");
            selectionComboBox = new JComboBox(durationsStringArray);
            selectionComboBox.setSelectedIndex(visualizationDuration);
            selectionComboBox.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){
                   visualizationDuration = selectionComboBox.getSelectedIndex();
                   ganttChartLayout.show(cardPanel, (String)selectionComboBox.getSelectedItem());
               }
            });
            selectionPanel.add(selectionLabel);
            selectionPanel.add(selectionComboBox);
            selectionPanel.add(Box.createHorizontalGlue());
            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getSelectionPanel", "Selection Panel initialized");
        }

        return selectionPanel;
    }

    /**
     * Method creating the chart
     *
     * @param dataset dataset containing the data for the chart
     * @return a chart
     */
    private JFreeChart createChart(XYZDataset dataset) {
        DateAxis xAxis = new DateAxis("Date");
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        xAxis.setDateFormatOverride(new SimpleDateFormat(durationsFormatterArray[this.visualizationDuration]));
        xAxis.setRange(Calendar.getInstance().getTime(), new Date(System.currentTimeMillis() + HistoryUtil.durationsTimesArray[visualizationDuration]-HistoryUtil.blockWidthsArray[visualizationDuration]));
        NumberAxis yAxis = new NumberAxis("Nodes");
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setInverted(true);
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        yAxis.setRange(1, numberOfNodes);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBlockRenderer renderer = new XYBlockRenderer();
        LookupPaintScale paintScale = new LookupPaintScale();
        for(int i = 0 ; i < jobsList.get(visualizationDuration).size() ; i ++){
            // String jobId = jobsList.get(visualizationDuration).get(i).getId().substring(0,jobsList.get(visualizationDuration).get(i).getId().indexOf("."));
            String jobId = jobsList.get(visualizationDuration).get(i).getParameterValue(GridJob.KEY_GRID_JOB_ID);
            int seed = Integer.parseInt(jobId);
            Random rng = new Random(seed);
            Color tempColor = Color.red;
            int red = tempColor.getRed();
            int green = tempColor.getGreen();
            int blue = tempColor.getBlue();
            int redRNG = rng.nextInt(255);
            int greenRNG = rng.nextInt(255);
            int blueRNG = rng.nextInt(255);
            if(red == redRNG && green == greenRNG && blue == blueRNG){
                tempColor=  new Color(rng.nextInt(255),rng.nextInt(255),rng.nextInt(255));
            }
            else{
                tempColor = new Color(redRNG,greenRNG,blueRNG);
            }
            if(seed == 0) tempColor = Color.red;
            paintScale.add(new Double(i),tempColor);
        }
        renderer.setBlockWidth(HistoryUtil.blockWidthsArray[visualizationDuration]);
        renderer.setBlockAnchor(RectangleAnchor.TOP_LEFT);
        renderer.setPaintScale(paintScale);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);
        JFreeChart chart = new JFreeChart("Gantt Chart activity for cluster " + siteName, plot);
        chart.removeLegend();
        chart.setBackgroundPaint(Color.white);
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "createChart", "Chart created");
        return chart;
    }

    /**
     * Method creating the dataset
     *
     * @return the dataset
     */
    private XYZDataset createDataset() {

        long numberOfBlocks = HistoryUtil.durationsTimesArray[visualizationDuration]/HistoryUtil.blockWidthsArray[visualizationDuration];
        double[] xvalues = new double[(int)(numberOfNodes*numberOfBlocks)];
        double[] yvalues = new double[(int)(numberOfNodes*numberOfBlocks)];
        double[] zvalues = new double[(int)(numberOfNodes*numberOfBlocks)];
        double[][] data = new double[][] {xvalues, yvalues, zvalues};
        Iterator<GridJob> iterJobs = jobsList.get(visualizationDuration).iterator();
        long now = Calendar.getInstance().getTimeInMillis();
        while(iterJobs.hasNext()){
            GridJob thisJob = iterJobs.next();
            // get the maximum date between now and the start date of the job
            long startDate = (Long.parseLong(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME)) -now > 0 )?(Long.parseLong(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME))):now;
            		//getDateFromOARDate(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME)).getTime()-now > 0 )?getDateFromOARDate(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME)).getTime():now;
            long timeFromNow = startDate - now;
            long endDateInMillis = Long.parseLong(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_STARTTIME));
            	//getDateFromOARDate(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_STARTTIME)).getTime();
//                    getDateFromWallTime(thisJob.getParam(GridJob.GJOB_WALLTIME));
            long wt = Long.parseLong(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME));
//            try {
//                     wt = Long.parseLong(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME));
//            }
//            catch (Exception e) {
//                
//            }
            endDateInMillis += wt;
            long chartEnd = now+HistoryUtil.durationsTimesArray[visualizationDuration];
            long endDate = (endDateInMillis - chartEnd > 0)?chartEnd:endDateInMillis;
            long durationOfTheJob = endDate - startDate;
            long numberOfBlocksOfTheJob = durationOfTheJob/HistoryUtil.blockWidthsArray[visualizationDuration];
            int startBlock = (int)(timeFromNow/HistoryUtil.blockWidthsArray[visualizationDuration]);
            boolean printed = true;
            // if (thisJob.jobHosts.equalsIgnoreCase("")){
            if (thisJob.getParameterValue(GridJob.KEY_GRID_JOB_HOSTS) == null) {
            }
            else{
                Iterator<Integer> iterNodes = getNodesList(thisJob).iterator();
                while(iterNodes.hasNext()){
                    int aNode = iterNodes.next();
                    for(int aBlock = 0 ; aBlock < numberOfBlocksOfTheJob ; aBlock ++){
                        // number Of time blocks
                        try{
                            int index = (int)((aNode-1)*(numberOfBlocks) + aBlock+startBlock);
                            xvalues[index] = now + (startBlock+aBlock)*HistoryUtil.blockWidthsArray[visualizationDuration];
                            yvalues[index] = aNode;
                            if(thisJob.getParameterValue(GridJob.KEY_GRID_JOB_ID).startsWith("0.oar")){
                                zvalues[index] = jobsList.get(visualizationDuration).indexOf(thisJob);
                                if(printed){
                                    printed = false;
                                }
                            }
                            zvalues[index] = jobsList.get(visualizationDuration).indexOf(thisJob);
                        }
                        catch(Exception e){
                            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "createDataset", e);
                        }
                    }
                }
            }
        }
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        dataset.addSeries("Series 1", data);
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "createDataset", "Dataset created");
        return dataset;
    }

    /**
     * Nodes list for an OarJobDesc
     *
     * @param aJob a Job
     * @return the list of the nodes of this job
     */
    public ArrayList<Integer> getNodesList(GridJob aJob){
        /*
        StringTokenizer tokenizer = new StringTokenizer(aJob.jobHosts,"+");
        */
        ArrayList<Integer> nodesList = new ArrayList<Integer>();
        if(Integer.parseInt(aJob.getParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT)) != 0){
            //String[] nodes = new String[Integer.parseInt(aJob.jobNbNodes)];
            String[] nodes = aJob.getHostsAsArray();
            StringTokenizer tokenizer1;
            Pattern aPattern = Pattern.compile("\\d+");
            int i = 0;
            // while(tokenizer.hasMoreTokens()){
            for (int ix=0; ix<nodes.length; ix++) {
                // nodes[i] = tokenizer.nextToken();
                tokenizer1 = new StringTokenizer(nodes[i],".");
                String token = tokenizer1.nextToken();
                Matcher matcher = aPattern.matcher(token);
                if(matcher.find()){
                    nodesList.add(Integer.parseInt(matcher.group()));
                }
                i++;
            }
        }
        return nodesList;
    }

    /**
     * Class representing the model of the Cluster JobsSummary Table
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class ClusterJobsSummaryModel extends AbstractTableModel{
        /**
         * Serial version ID
         */
        private static final long serialVersionUID = 1L;
        /**
         * Data to be displayed
         */
        private Vector<Vector<String>> myData;
        /**
         * Array containing the title of the columns
         */
        private String[] columnsNames = {"Color","Id", "User", "Queue", "NbNodes", "Walltime", "SchStart"};
        /**
         * Default constructor of the Model of the ClusterJobsSummary
         *
         */
        public ClusterJobsSummaryModel(){
            myData = new Vector<Vector<String>>();
            Iterator<GridJob> iter = jobsList.get(visualizationDuration).iterator();
            while(iter.hasNext()){
                Vector<String> tempVector =new Vector<String>();
                GridJob job = iter.next();
                // String jobId = job.getId().substring(0, job.getId().indexOf("."));
                String jobId = job.getParameterValue(GridJob.KEY_GRID_JOB_ID);
                if(jobId.equalsIgnoreCase("0")){

                }
                else{
                    tempVector.add(jobId);
                    tempVector.add(jobId);
                    tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_OWNER));
                    tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_QUEUE));
                    tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT));
                    tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME));
                    tempVector.add(job.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME));
                    myData.add(tempVector);
                }
            }
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
            // TODO Auto-generated method stub
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
            if(column == 0){
                Random rng = new Random(Integer.parseInt((String)value));
                label.setOpaque(true);
                Color tempColor = Color.red;
                int red = tempColor.getRed();
                int green = tempColor.getGreen();
                int blue = tempColor.getBlue();
                int redRNG = rng.nextInt(255);
                int greenRNG = rng.nextInt(255);
                int blueRNG = rng.nextInt(255);

                if(red == redRNG && green == greenRNG && blue == blueRNG){
                    tempColor = new Color(rng.nextInt(255),rng.nextInt(255),rng.nextInt(255));
                }
                else{
                    tempColor = new Color(redRNG,greenRNG,blueRNG);
                }
                label.setBackground(tempColor);
            }else{
                label.setText(value.toString());
                label.setHorizontalTextPosition(SwingConstants.CENTER);
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            return label;
        }
    }
}