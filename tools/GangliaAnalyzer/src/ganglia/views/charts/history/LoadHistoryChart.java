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
public class LoadHistoryChart extends JPanel implements ganglia.misc.Observer{

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
    private TimeSeries seriesOne;
    private TimeSeries seriesFive;
    private TimeSeries seriesFifteen;
    
    private String titleOfTheGraph = "Load";
    private String nameOfTheDataToPlotOne = "One";
    private String nameOfTheDataToPlotFive = "Five";
    private String nameOfTheDataToPlotFifteen = "Fifteen";
    private String axisXName = "Time";
    private String axisYName = "Load";
    double fixedAutoRange;
    private String name = null;
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public LoadHistoryChart(String hostName, double range) {
    	this.name = hostName+"|load";
    	fixedAutoRange = range;
    	this.seriesOne = new TimeSeries(nameOfTheDataToPlotOne, Millisecond.class);
    	this.seriesFive = new TimeSeries(nameOfTheDataToPlotFive, Millisecond.class);
    	this.seriesFifteen = new TimeSeries(nameOfTheDataToPlotFifteen, Millisecond.class);
    	TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(seriesOne);
        dataset.addSeries(seriesFive);
        dataset.addSeries(seriesFifteen);
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
    	if(metric.equalsIgnoreCase(MetricNames.LOAD_ONE)) seriesOne.addOrUpdate(new Millisecond(new Date(date)), value);
    	else if(metric.equalsIgnoreCase(MetricNames.LOAD_FIVE))seriesFive.addOrUpdate(new Millisecond(new Date(date)), value);
    	else{
    		seriesFifteen.addOrUpdate(new Millisecond(new Date(date)), value);
    	}
    }
}
