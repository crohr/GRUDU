/**
 * 
 */
package ganglia.views.charts.history;

import ganglia.models.history.HistoryMetric;
import ganglia.models.history.MetricNames;

import java.awt.BorderLayout;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * @author david
 *
 */
public class ProcessesHistoryChart extends JPanel implements ganglia.misc.Observer{

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
    private TimeSeries seriesRunning;
	
    private String titleOfTheGraph = "History of the processes";
    private String nameOfTheDataToPlotTotal = "All processes";
    private String nameOfTheDataToPlotRunning = "Running processes";
    private String axisXName = "Time";
    private String axisYName = "Process number";
    double fixedAutoRange;
    private String name = null;;
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public ProcessesHistoryChart(String hostName,double range) {
    	name = hostName+"|" + "proc";
    	fixedAutoRange = range;
    	this.seriesTotal = new TimeSeries(nameOfTheDataToPlotTotal, Millisecond.class);
    	this.seriesRunning = new TimeSeries(nameOfTheDataToPlotRunning, Millisecond.class);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(seriesTotal);
        dataset.addSeries(seriesRunning);
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
    	if(metric.equalsIgnoreCase(MetricNames.PROC_TOTAL)) seriesTotal.addOrUpdate(new Millisecond(new Date(date)), value);
    	else seriesRunning.addOrUpdate(new Millisecond(new Date(date)), value);
    }
}
