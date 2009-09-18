/**
 * 
 */
package ganglia.views.charts;

import java.text.DecimalFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * @author david
 *
 */
public class CPUChart extends JPanel{

	private double[] valuesForChart = null;
	
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public CPUChart(double[] values) {
    	valuesForChart = values;
        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        add(chartPanel);
    }
    
    /**
     * Creates a sample dataset for the demo.
     * 
     * @return A sample dataset.
     */
    private PieDataset createDataset() {
        
        DefaultPieDataset result = new DefaultPieDataset();
        DecimalFormat format = new DecimalFormat("###");
        result.setValue("Idle ( " + format.format(valuesForChart[0]) + " %)", new Double(valuesForChart[0]));
        result.setValue("System ( " + format.format(valuesForChart[1]) + " %)", new Double(valuesForChart[1]));
        result.setValue("User ( " + format.format(valuesForChart[2]) + " %)", new Double(valuesForChart[2]));
        return result;
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(
            "CPU usage",  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        org.jfree.chart.plot.PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage("No data to display");
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
