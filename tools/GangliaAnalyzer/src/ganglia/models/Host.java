/**
 * 
 */
package ganglia.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * @author david
 *
 */
public class Host implements HostXML, MetricXML{

	private String name = null;
	private String ip = null;
	private String reported = null;
	private HashMap<String, Metric> metrics = null;
	
	public Host(Element xmlElement){
		name = xmlElement.getAttributeValue(HOST_ATTRIBUTE_NAME);
		ip = xmlElement.getAttributeValue(HOST_ATTRIBUTE_IP);
		reported = xmlElement.getAttributeValue(HOST_ATTRIBUTE_REPORTED);
		List<Element> aListOfMetrics = xmlElement.getChildren(METRIC_ELEMENT);
		Iterator<Element> iter = aListOfMetrics.iterator();
		metrics = new HashMap<String, Metric>();
		while(iter.hasNext()){
			Element aMetricXML = iter.next();
			Metric aMetric = new Metric(aMetricXML);
			metrics.put(aMetric.getName(), aMetric);
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the reported
	 */
	public String getReported() {
		return reported;
	}

	/**
	 * @param reported the reported to set
	 */
	public void setReported(String reported) {
		this.reported = reported;
	}

	/**
	 * @return the metrics
	 */
	public HashMap<String, Metric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(HashMap<String, Metric> metrics) {
		this.metrics = metrics;
	}
	
}
