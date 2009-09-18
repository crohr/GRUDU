/**
 * 
 */
package ganglia.views.charts.history;

import ganglia.models.history.HistoryMetric;
import ganglia.models.history.MetricNames;

import java.util.Date;
import java.util.Iterator;

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
public class CPUHistoryChart extends JPanel implements ganglia.misc.Observer{

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
    private TimeSeries seriesIdle;
    private TimeSeries seriesSystem;
    private TimeSeries seriesUser;
	
    private String titleOfTheGraph = "History of the idle/system/user cpu occupation";
    private String nameOfTheDataToPlotIdle = "Idle";
    private String nameOfTheDataToPlotSystem = "System";
    private String nameOfTheDataToPlotUser = "User";
    private String axisXName = "Time";
    private String axisYName = "CPU usage in %";
    double fixedAutoRange;
    private String name = null;
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public CPUHistoryChart(String hostName,double range) {
    	this.name = hostName+"|cpu";

    	fixedAutoRange = range;
    	seriesIdle = new TimeSeries(nameOfTheDataToPlotIdle, Millisecond.class);
    	seriesSystem = new TimeSeries(nameOfTheDataToPlotSystem, Millisecond.class);
    	seriesUser= new TimeSeries(nameOfTheDataToPlotUser, Millisecond.class);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(seriesIdle);
        dataset.addSeries(seriesSystem);
        dataset.addSeries(seriesUser);
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
    public void addOrUpdateData(String metric,long date, double value) {
    	if(metric.equalsIgnoreCase(MetricNames.CPU_IDLE)) seriesIdle.addOrUpdate(new Millisecond(new Date(date)), value);
    	else if(metric.equalsIgnoreCase(MetricNames.CPU_SYSTEM)) seriesSystem.addOrUpdate(new Millisecond(new Date(date)), value);
    	else seriesUser.addOrUpdate(new Millisecond(new Date(date)), value);
    }
}
