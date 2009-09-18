/**
 * 
 */
package ganglia.views.charts;

import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.LegendItemCollection;


/**
 * @author david
 *
 */
public class NetworkChart extends JPanel {

	private double[] valuesForTheGraph = null;
	
    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public NetworkChart(double[] values) {
    	valuesForTheGraph = values;
        CategoryDataset dataset1 = createDataset1();
        CategoryDataset dataset2 = createDataset2();
        JFreeChart chart = createChart(dataset1, dataset2);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        add(chartPanel);
    }

    /**
     * Creates a sample dataset.
     *
     * @return  The dataset.
     */
    private CategoryDataset createDataset1() {

        // row keys...
        String series1 = "Bytes in/out";
        String series2 = "tac";

        // column keys...
        String category1 = "IN";
        String category2 = "OUT";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(valuesForTheGraph[0], series1, category1);
        dataset.addValue(valuesForTheGraph[1], series1, category2);
        dataset.addValue(null, series2, category1);
        dataset.addValue(null, series2, category2);

        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return  The dataset.
     */
    private CategoryDataset createDataset2() {

        // row keys...
        String series1 = "tac";
        String series2 = "Packets in/out";

        // column keys...
        String category1 = "IN";
        String category2 = "OUT";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(null, series1, category1);
        dataset.addValue(null, series1, category2);

        dataset.addValue(valuesForTheGraph[2], series2, category1);
        dataset.addValue(valuesForTheGraph[3], series2, category2);

        return dataset;

    }

    /**
     * Creates a chart.
     * 
     * @param dataset1  the first dataset.
     * @param dataset2  the second dataset.
     * 
     * @return A chart.
     */
    private JFreeChart createChart(CategoryDataset dataset1, 
                                          CategoryDataset dataset2) {

        CategoryAxis domainAxis = new CategoryAxis("Type");
        NumberAxis rangeAxis = new NumberAxis("Bytes/sec");
        BarRenderer renderer1 = new BarRenderer();
        CategoryPlot plot 
            = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {
            
            /**
             * Override the getLegendItems() method to handle special case.
             *
             * @return the legend items.
             */
            public LegendItemCollection getLegendItems() {

                LegendItemCollection result = new LegendItemCollection();

                CategoryDataset data = getDataset();
                if (data != null) {
                    CategoryItemRenderer r = getRenderer();
                    if (r != null) {
                        LegendItem item = r.getLegendItem(0, 0);
                        result.add(item);
                    }
                }

                // the JDK 1.2.2 compiler complained about the name of this
                // variable 
                CategoryDataset dset2 = getDataset(1);
                if (dset2 != null) {
                    CategoryItemRenderer renderer2 = getRenderer(1);
                    if (renderer2 != null) {
                        LegendItem item = renderer2.getLegendItem(1, 1);
                        result.add(item);
                    }
                }

                return result;

            }
            
        };
        
        JFreeChart chart = new JFreeChart("Network Data Transfert", plot);
        chart.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        ValueAxis axis2 = new NumberAxis("Packets/sec");
        plot.setRangeAxis(1, axis2);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        BarRenderer renderer2 = new BarRenderer();
        plot.setRenderer(1, renderer2);
        
        return chart;
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset1(), createDataset2());
        return new ChartPanel(chart);
    }
}
