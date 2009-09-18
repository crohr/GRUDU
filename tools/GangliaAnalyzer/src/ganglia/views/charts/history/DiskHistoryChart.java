/**
 * 
 */
package ganglia.views.charts.history;

import ganglia.models.history.HistoryMetric;
import ganglia.models.history.MetricNames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * @author david
 *
 */
public class DiskHistoryChart extends JPanel implements ganglia.misc.Observer{

    /* (non-Javadoc)
	 * @see ganglia.misc.Observer#getObserverName()
	 */
	public String getObserverName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see ganglia.misc.Observer#update(ganglia.models.history.HistoryMetric)
	 */
	public void update(HistoryMetric newValues) {
		String metricName = newValues.getName();
		Iterator<Long> iter = newValues.getValues().keySet().iterator();
		while(iter.hasNext()){
			long date = iter.next();
			double value = newValues.getValue(date);
			addOrUpdateData(metricName,date, value);
		}
	}

	/** The time series data. */
    private TimeSeries seriesTotal;
    private TimeSeries seriesUsed;
    private TimeSeries seriesWarning;
    private TimeSeries seriesCritical;
    
    private String titleOfTheGraph = "Disk usage";
    private String nameOfTheDataToPlotUsed = "Used";
    private String nameOfTheDataToPlotTotal = "Total";
    private String nameOfTheDataToPlotWarning = "Warning";
    private String nameOfTheDataToPlotCritical = "Critical";
    private String axisXName = "Time";
    private String axisYName = "Size used in GB";
    double fixedAutoRange;
    private String name = null;
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public DiskHistoryChart(String hostName, double range) {
    	this.name = hostName+"|disk";
    	fixedAutoRange = range;
    	this.seriesUsed = new TimeSeries(nameOfTheDataToPlotUsed, Millisecond.class);
    	this.seriesTotal = new TimeSeries(nameOfTheDataToPlotUsed, Millisecond.class);
    	this.seriesWarning = new TimeSeries(nameOfTheDataToPlotWarning, Millisecond.class);
    	this.seriesCritical = new TimeSeries(nameOfTheDataToPlotCritical, Millisecond.class);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(seriesUsed);
        dataset.addSeries(seriesWarning);
        dataset.addSeries(seriesCritical);
        dataset.addSeries(seriesTotal);
        ChartPanel chartPanel = new ChartPanel(createChart(dataset));
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        add(chartPanel);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A sample chart.
     */
    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart result = ChartFactory.createTimeSeriesChart(
            titleOfTheGraph, 
            axisXName, 
            axisYName,
            dataset, 
            true, 
            true, 
            false
        );
        XYPlot plot = result.getXYPlot();
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.green);
        renderer.setSeriesPaint(1, Color.yellow);
        renderer.setSeriesPaint(2, Color.ORANGE);
        renderer.setSeriesPaint(3, Color.red);
       
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(fixedAutoRange);
        return result;
    }
        
    /**
     * Handles a click on the button by adding new (random) data.
     *
     * @param e  the action event.
     */
    public void addOrUpdateData(String metric, long date, double value) {
    	if(metric.equalsIgnoreCase(MetricNames.DISK_USED)) seriesUsed.addOrUpdate(new Millisecond(new Date(date)), value);
    	else{
    		seriesTotal.addOrUpdate(new Millisecond(new Date(date)), value);
    		seriesWarning.addOrUpdate(new Millisecond(new Date(date)), 0.5*value);
    		seriesCritical.addOrUpdate(new Millisecond(new Date(date)), 0.8*value);
    	}
    }
}
