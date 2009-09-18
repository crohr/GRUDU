/**
 * 
 */
package ganglia.views.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.ValueDataset;

/**
 * @author david
 *
 */
public class MeterChart extends JPanel {

    private org.jfree.data.general.DefaultValueDataset dataset;
    
    private double valueTotal = 0.0;
    private double valueUsed = 0.0;
    private String valueUnits = null;
    private String title = null;
    
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public MeterChart(double valueTotal, double valueFree, String units, String title) {
    	this.valueTotal = valueTotal;
    	this.valueUnits = units;
    	this.valueUsed = valueTotal - valueFree;
    	this.title = title;
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new Dimension(500, 270));
        add(chartPanel);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(ValueDataset dataset) {
        MeterPlot plot = new MeterPlot(dataset);
        plot.setRange(new Range(0, valueTotal));
        plot.addInterval(new org.jfree.chart.plot.MeterInterval("Normal", new Range(0.0, 0.5*valueTotal), 
                Color.lightGray, new BasicStroke(2.0f), Color.green));
        plot.addInterval(new MeterInterval("Warning", new Range(0.5*valueTotal, 0.8*valueTotal), 
                Color.lightGray, new BasicStroke(2.0f), Color.orange));
        plot.addInterval(new MeterInterval("Critical", new Range(0.8*valueTotal,valueTotal), 
                Color.lightGray, new BasicStroke(2.0f), Color.red));
        plot.setNeedlePaint(Color.darkGray);
        plot.setDialBackgroundPaint(Color.white);
        plot.setDialOutlinePaint(Color.white);
        plot.setDialShape(DialShape.CHORD);
        plot.setMeterAngle(260);
        plot.setUnits(valueUnits);
        plot.setTickLabelsVisible(true);
        plot.setTickLabelFont(new Font("Dialog", Font.BOLD, 10));
        plot.setTickLabelPaint(Color.darkGray);
        plot.setTickSize(valueTotal/100);
        plot.setTickPaint(Color.lightGray);
        
        plot.setValuePaint(Color.black);
        plot.setValueFont(new Font("Dialog", Font.BOLD, 14));
        
        JFreeChart chart = new JFreeChart(title, 
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        return chart;
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public JPanel createDemoPanel() {
        dataset = new org.jfree.data.general.DefaultValueDataset(valueUsed);
        JFreeChart chart = createChart(dataset);
        JPanel panel = new org.jfree.chart.ChartPanel(chart);
        return panel;
    }
	
}
