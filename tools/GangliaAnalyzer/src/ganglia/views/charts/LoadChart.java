/**
 * 
 */
package ganglia.views.charts;

import java.awt.Color;
import java.awt.GradientPaint;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.ui.TextAnchor;

/**
 * @author david
 *
 */
public class LoadChart extends JPanel{

	
	private double[] valuesForChart = null;
	
    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public LoadChart(double[] values) {
    	valuesForChart = values;
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        add(chartPanel);

    }

    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    private CategoryDataset createDataset() {
        
        // row keys...
        String series1 = "Load";

        // column keys...
        String category1 = "One minute";
        String category2 = "Five minutes";
        String category3 = "Fifteen minutes";
        
        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(valuesForChart[0], series1, category1);
        dataset.addValue(valuesForChart[1], series1, category2);
        dataset.addValue(valuesForChart[2], series1, category3);
        
        return dataset;
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(CategoryDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "Load of the machine",         // chart title
            "Load",               // domain axis label
            "Percentage",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        //chart.setBackgroundPaint(new Color(0xBBBBDD));

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        
        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setMaximumBarWidth(0.10);
        
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.green
        );
//        GradientPaint gp1 = new GradientPaint(
//            0.0f, 0.0f, Color.green, 
//            0.0f, 0.0f, Color.lightGray
//        );
        renderer.setSeriesPaint(0, gp0);
        
        renderer.setItemLabelGenerator(
        		new StandardCategoryItemLabelGenerator()
        );
        renderer.setItemLabelsVisible(true);
        renderer.setItemLabelPaint(Color.white);
        ItemLabelPosition p = new ItemLabelPosition(
        		ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 0.0
        );
        renderer.setPositiveItemLabelPosition(p);
        
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
}
