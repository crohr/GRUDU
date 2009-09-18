/****************************************************************************/
/* This class corresponds to the summary chart for he Grid 5000 view        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kSummaryChart.java,v 1.9 2007/11/23 14:38:27 dloureir Exp $
 * $Log: G5kSummaryChart.java,v $
 * Revision 1.9  2007/11/23 14:38:27  dloureir
 * Changing the display of the data for the layered bar chart
 *
 * Revision 1.8  2007/10/24 12:51:05  dloureir
 * Removing some calls to the G5KCluster class by the corresponding calls to the G5KSite class
 *
 * Revision 1.7  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.6  2007/07/12 14:14:42  dloureir
 * Some code correction and the clean up of an unused attribute.
 *
 * Revision 1.5  2007/06/25 08:18:47  dloureir
 * Adding for the G5KView's graphs and the Cluster's graphs the number of nodes of the user.
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
import org.jfree.util.SortOrder;

import diet.gridr.g5k.util.Config;
import diet.gridr.g5k.util.ExtendedStackedBarRenderer;
import diet.gridr.g5k.util.G5kCfg;
import diet.gridr.g5k.util.G5kCluster;
import diet.gridr.g5k.util.G5kSite;
import diet.logging.LoggingManager;

/**
 * This class corresponds to the summary chart for he Grid 5000 view
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 */
public class G5kSummaryChart extends JPanel {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class representing the Stack Bar chart
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class StackBarChart extends JPanel{

        /**
         * serialVersionUID defines the version for the serialisation of
         * this object
         */
        private static final long serialVersionUID = 1L;

        /**
         * Default constructor of the Stack Bar chart
         *
         */
        public StackBarChart(){
            super();
            CategoryDataset dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(chart);
            add(chartPanel);
            setVisible(true);
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "StackBarChart", "StackBarChart initialized");
        }

        /**
         * Creates a dataset.
         *
         * @return a dataset.
         */
        private CategoryDataset createDataset() {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(int i = 0 ;i < data.length ; i ++){
                if(Config.isSiteEnable(i)){
                    // String aCluster = G5kCluster.getNameForIndex(i);
                    String aSite = G5kSite.getSiteForIndex(i);
                    int index = aSite.indexOf("--");
                    if(index != -1) aSite = aSite.substring(index+2, aSite.length());
                    dataset.setValue(((double)data[i][0]/(double)data[i][6])*100,"Free",aSite);
                    dataset.setValue(((double)(data[i][1]-data[i][5])/(double)data[i][6])*100,"Job",aSite);
                    dataset.setValue(((double)data[i][2]/(double)data[i][6])*100,"Suspected",aSite);
                    dataset.setValue(((double)data[i][3]/(double)data[i][6])*100,"Dead",aSite);
                    dataset.setValue(((double)data[i][4]/(double)data[i][6])*100,"Absent",aSite);
                    if(data[i][5] != 0){
                    	String userID = G5kCfg.get(G5kCfg.USERNAME);
                    	dataset.setValue(((double)data[i][5]/(double)data[i][6])*100,"Nodes of " + userID,aSite);
                    }
                }
            }
            return dataset;
        }


        /**
         * Creates a chart.
         *
         * @param dataset the dataset for the chart.
         *
         * @return a sample chart.
         */
        private JFreeChart createChart(CategoryDataset dataset) {

            JFreeChart chart = ChartFactory.createStackedBarChart(
                    "Nodes repartition summary for Grid 5000",  // chart title
                    "Clusters",                  // domain axis label
                    "Nodes percentage",                     // range axis label
                    dataset,                     // data
                    PlotOrientation.VERTICAL,    // the plot orientation
                    true,                        // legend
                    false,                       // tooltips
                    false                        // urls
            );
            CategoryPlot plot = chart.getCategoryPlot();
            CategoryItemRenderer renderer = new ExtendedStackedBarRenderer();
            renderer.setItemLabelsVisible(true);
            renderer.setItemLabelGenerator(
                    new StandardCategoryItemLabelGenerator());
            renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
            plot.setRenderer(renderer);
            renderer.setSeriesPaint(0, Color.GREEN);
            renderer.setSeriesPaint(1, Color.BLUE);
            renderer.setSeriesPaint(2, Color.PINK);
            renderer.setSeriesPaint(3,Color.RED);
            renderer.setSeriesPaint(4,Color.YELLOW);
            renderer.setSeriesPaint(5, Color.CYAN);
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setLowerMargin(0.15);
            rangeAxis.setUpperMargin(0.15);
            return chart;

        }
    }

    /**
     * data for the different charts
     */
    int[][] data = null;
    /**
     * Array containing the column names
     */
    String[] columnNames = {"Free", "Job", "Suspected", "Absent", "Dead", "Total", "Total usable"};
    /**
     * CardLayotu for the display of the different charts
     */
    private CardLayout carder;
    /**
     * Panel were the cardLayout is applied
     */
    private JPanel cardPanel;
    /**
     * Panel for the card selection panel
     */
    private JPanel chartSelectionPanel = null;
    /**
     * Label for the chart selection
     */
    private JLabel chartSelectionLabel = null;
    /**
     * ComboBox for the selection of a chart
     */
    private JComboBox chartSelectionComboBox = null;

    /**
     * Default constructor
     *
     * @param data data to display
     * @param dim dimension of the Panel
     */
    public G5kSummaryChart(int[][] data) {
        super();
        this.data = data;
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(getChartSelectionPanel());
        add(getCardPanel());
        setVisible(true);
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kSummaryChart", "G5k summary Chart initiliazed");
    }

    /**
     * Method returning the chart selection panel
     *
     * @return panel for the selection of a chart to display
     */
    private JPanel getChartSelectionPanel(){
        if(chartSelectionPanel == null){
            chartSelectionPanel = new JPanel();
            chartSelectionPanel.setLayout(new BoxLayout(chartSelectionPanel,BoxLayout.X_AXIS));
            chartSelectionLabel = new JLabel("Available charts :");
            chartSelectionPanel.add(chartSelectionLabel);
            chartSelectionPanel.add(Box.createHorizontalStrut(10));
            chartSelectionPanel.add(getChartSelectionComboBox());
            chartSelectionPanel.add(Box.createHorizontalGlue());
        }
        return chartSelectionPanel;
    }

    /**
     * CardPanel displaying the data for the Grid5000 summary view
     *
     * @return card panel
     */
    private JPanel getCardPanel(){
        if(cardPanel == null){
            cardPanel = new JPanel();
            carder = new CardLayout();
            cardPanel.setLayout(carder);
            cardPanel.add("Stack Bar Chart",new StackBarChart());
            cardPanel.add("Bar Chart 3D",new BarChart3D());
            cardPanel.add("Layered Bar Chart",new LayeredBarChart());
            cardPanel.add("Pie Chart",new PieChart());
            cardPanel.add("Pie Chart 3D",new PieChart3D());
            carder.show(cardPanel, "Stack Bar Chart");
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getCardPanel", "Charts added");
        }
        return cardPanel;
    }

    /**
     * JComboBox for the selection of a chart
     *
     * @return ComboBox for the selection of a chart
     */
    private JComboBox getChartSelectionComboBox(){
        if(chartSelectionComboBox == null){
            Vector<String> vector = new Vector<String>();
            vector.add("Stack Bar Chart");
            vector.add("Bar Chart 3D");
            vector.add("Layered Bar Chart");
            vector.add("Pie Chart");
            vector.add("Pie Chart 3D");
            chartSelectionComboBox = new JComboBox(vector);
            chartSelectionComboBox.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    carder.show(cardPanel,(String)chartSelectionComboBox.getSelectedItem());
                }
            });
        }
        return chartSelectionComboBox;
    }

    /**
     * Class representing the 3D Bar chart
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class BarChart3D extends JPanel{

        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
        private static final long serialVersionUID = 1L;

        /**
         * Default constructor
         *
         */
        public BarChart3D() {
            super();
            CategoryDataset dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(chart);
            add(chartPanel);
            setVisible(true);
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "BarChart3D", "BarChart3D initialized");
        }

        /**
         * Creates a dataset.
         *
         * @return a dataset.
         */
        private CategoryDataset createDataset() {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(int i = 0 ;i < data.length ; i ++){
                if(Config.isSiteEnable(i)){
                    //String aCluster = G5kCluster.getNameForIndex(i);
                    //int index = aCluster.indexOf("--");
                    //if(index != -1) aCluster = aCluster.substring(index+2, aCluster.length());
                	String aCluster = G5kSite.getSiteForIndex(i);
                    dataset.addValue((double)data[i][0],"Free",aCluster);
                    dataset.addValue((double)(data[i][1]-data[i][5]),"Job",aCluster);
                    dataset.addValue((double)data[i][2],"Suspected",aCluster);
                    dataset.addValue((double)data[i][3],"Dead",aCluster);
                    dataset.addValue((double)data[i][4],"Absent",aCluster);
                    if(data[i][5] != 0){
                    	String userID = G5kCfg.get(G5kCfg.USERNAME);
                    	dataset.setValue((double)data[i][5],"Nodes of "+userID, aCluster);
                    }
                }
            }
            return dataset;
        }

        /**
         * Creates a chart.
         *
         * @param dataset  the dataset.
         *
         * @return The chart.
         */
        private JFreeChart createChart(CategoryDataset dataset) {

            JFreeChart chart = ChartFactory.createBarChart3D(
                    "Nodes repartition summary for Grid 5000",  // chart title
                    "Clusters",                  // domain axis label
                    "Nodes number",                     // range axis label
                    dataset,                  // data
                    PlotOrientation.VERTICAL, // orientation
                    true,                     // include legend
                    true,                     // tooltips
                    false                     // urls
            );

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinesVisible(true);
            CategoryAxis axis = plot.getDomainAxis();
            axis.setCategoryLabelPositions(
                    CategoryLabelPositions.createUpRotationLabelPositions(
                            Math.PI / 8.0));
            BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
            renderer.setItemLabelsVisible(true);
            renderer.setSeriesPaint(0, Color.GREEN);
            renderer.setSeriesPaint(1, Color.BLUE);
            renderer.setSeriesPaint(2, Color.PINK);
            renderer.setSeriesPaint(3,Color.RED);
            renderer.setSeriesPaint(4,Color.YELLOW);
            renderer.setSeriesPaint(5, Color.CYAN);
            renderer.setDrawBarOutline(false);
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            return chart;

        }
    }

    /**
     * Class representing the 3D Bar chart
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class LayeredBarChart extends JPanel{

        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
        private static final long serialVersionUID = 1L;

        /**
         * Default constructor
         *
         */
        public LayeredBarChart(){
            super();
            CategoryDataset dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(chart);
            add(chartPanel);
            setVisible(true);
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "LayeredBarChart", "LayeredBarChart initialized");
        }

        /**
         * Returns a dataset.
         *
         * @return The dataset.
         */
        private CategoryDataset createDataset() {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(int i = 0 ;i < data.length ; i ++){
                if(Config.isSiteEnable(i)){
//                    String aCluster = G5kCluster.getNameForIndex(i);
//                    int index = aCluster.indexOf("--");
//                    if(index != -1) aCluster = aCluster.substring(index+2, aCluster.length());
                	String aCluster = G5kSite.getSiteForIndex(i);
                    dataset.setValue((double)data[i][0],"Free",aCluster);
                    dataset.setValue((double)(data[i][1]-data[i][5]),"Job",aCluster);
                    dataset.setValue((double)data[i][2],"Suspected",aCluster);
                    dataset.setValue((double)data[i][3],"Dead",aCluster);
                    dataset.setValue((double)data[i][4],"Absent",aCluster);
                    if(data[i][5] != 0){
                    	String userID = G5kCfg.get(G5kCfg.USERNAME);
                    	dataset.setValue(((double)data[i][5]/(double)data[i][6])*100,"Nodes of "+userID,aCluster);
                    }
                }
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

            // create the chart...
            JFreeChart chart = ChartFactory.createBarChart(
                    "Nodes repartition summary for Grid 5000",  // chart title
                    "Clusters",                  // domain axis label
                    "Nodes number",
                    dataset,                     // data
                    PlotOrientation.VERTICAL,    // orientation
                    true,                        // include legend
                    true,                        // tooltips?
                    false                        // URLs?
            );

            chart.setBackgroundPaint(Color.white);
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.lightGray);
            plot.setDomainGridlinePaint(Color.white);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.white);
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            LayeredBarRenderer renderer = new LayeredBarRenderer();
            renderer.setDrawBarOutline(false);
            plot.setRenderer(renderer);
            renderer.setSeriesPaint(0, Color.GREEN);
            renderer.setSeriesPaint(1, Color.BLUE);
            renderer.setSeriesPaint(2, Color.PINK);
            renderer.setSeriesPaint(3,Color.RED);
            renderer.setSeriesPaint(4,Color.YELLOW);
            renderer.setSeriesPaint(5, Color.CYAN);
            plot.setRowRenderingOrder(SortOrder.DESCENDING);

            return chart;

        }
    }

    /**
     * Class representing the Pie chart
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class PieChart extends JPanel{
        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
        private static final long serialVersionUID = 1L;
        //		create a dataset...

        /**
         * Default constructor of a Pie chart
         */
        public PieChart(){
            DefaultPieDataset dataset = new DefaultPieDataset();
            int[] values = new int[6];
            for(int i = 0 ; i < data.length ; i++){
                if(Config.isSiteEnable(i)){
                    values[0] += data[i][0];
                    values[1] += data[i][1];
                    values[2] += data[i][2];
                    values[3] += data[i][3];
                    values[4] += data[i][4];
                    values[5] += data[i][5];
                }
            }
            dataset.setValue("Free", values[0]);
            dataset.setValue("Job", values[1]-values[5]);
            dataset.setValue("Suspected", values[2]);
            dataset.setValue("Dead", values[3]);
            dataset.setValue("Absent", values[4]);
            if(values[5] != 0){
            	String userID = G5kCfg.get(G5kCfg.USERNAME);
            	dataset.setValue("Nodes of " + userID, values[5]);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Nodes repartition summary for Grid 5000",  // chart title,
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
            String userID = G5kCfg.get(G5kCfg.USERNAME);
            plot.setSectionPaint("Nodes of "+userID,Color.CYAN);
            plot.setIgnoreNullValues(true);
            plot.setIgnoreZeroValues(true);
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0} : {1} ({2})"));
            plot.setLabelBackgroundPaint(new Color(220, 220, 220));
            ChartPanel myPieChart = new ChartPanel(chart);
            myPieChart.setDisplayToolTips(false);
            myPieChart.setVisible(true);
            add(myPieChart);
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "PieChart", "PieChart initialized");
        }
    }

    /**
     * Class representing the 3D Pie chart
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class PieChart3D extends JPanel{

        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
        private static final long serialVersionUID = 1L;

        /**
         * Default constructor
         *
         */
        public PieChart3D() {
            super();
            PieDataset dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            ChartPanel myPieChart3D = new ChartPanel(chart);
            myPieChart3D.setDisplayToolTips(false);
            myPieChart3D.setVisible(true);
            add(myPieChart3D);
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "PieChart3D", "PieChart3D initialized");
        }

        /**
         * Creates a dataset
         *
         * @return A dataset.
         */
        private PieDataset createDataset() {

            DefaultPieDataset dataset = new DefaultPieDataset();
            int[] values = new int[6];
            for(int i = 0 ; i < data.length ; i++){
                if(Config.isSiteEnable(i)){
                    values[0] += data[i][0];
                    values[1]+= data[i][1];
                    values[2]+= data[i][2];
                    values[3]+= data[i][3];
                    values[4]+= data[i][4];
                    values[5]+= data[i][5];
                }
            }
            dataset.setValue("Free", values[0]);
            dataset.setValue("Job", values[1]-values[5]);
            dataset.setValue("Suspected", values[2]);
            dataset.setValue("Dead", values[3]);
            dataset.setValue("Absent", values[4]);
            if(values[5] != 0){
            	String userID = G5kCfg.get(G5kCfg.USERNAME);
            	dataset.setValue("Nodes of " + userID, values[5]);
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
                    "Nodes repartition summary for Grid 5000",  // chart title
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
            plot.setSectionPaint("Nodes of "+userID, Color.CYAN);
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
}