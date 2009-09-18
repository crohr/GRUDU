/**
 * 
 */
package ganglia.controller;

import ganglia.models.Host;
import ganglia.models.Metric;
import ganglia.models.MetricNames;
import ganglia.models.MetricXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jdom.Element;

/**
 * @author david
 *
 */
public class HistoryGenerator {

	public static Element generateHistory(ArrayList listOfHosts){
		Element historyItem = new Element("history_item");
		historyItem.setAttribute("date", Long.toString(System.currentTimeMillis()));
		Iterator iter = listOfHosts.iterator();
		while(iter.hasNext()){
			Host anHost = ((Host)iter.next());
			Element host = new Element("host");
			host.setAttribute("name", anHost.getName());
			HashMap metrics = anHost.getMetrics();
			// network
			Metric bytes_in = (Metric)metrics.get(MetricNames.BYTES_IN);
			host.addContent(bytes_in.toXML());
			Metric bytes_out = (Metric)metrics.get(MetricNames.BYTES_OUT);
			host.addContent(bytes_out.toXML());
			Metric pkts_in = (Metric)metrics.get(MetricNames.PKTS_IN);
			host.addContent(pkts_in.toXML());
			Metric pkts_out = (Metric)metrics.get(MetricNames.PKTS_OUT);
			host.addContent(pkts_out.toXML());
			// cpu
			Metric cpu_idle = (Metric)metrics.get(MetricNames.CPU_IDLE);
			host.addContent(cpu_idle.toXML());
			Metric cpu_system = (Metric)metrics.get(MetricNames.CPU_SYSTEM);
			host.addContent(cpu_system.toXML());
			Metric cpu_user = (Metric)metrics.get(MetricNames.CPU_USER);
			host.addContent(cpu_user.toXML());
			// disk
			Metric disk_total = (Metric)metrics.get(MetricNames.DISK_TOTAL);
			host.addContent(disk_total.toXML());
			Metric disk_free = (Metric)metrics.get(MetricNames.DISK_FREE);
			
			Element disk_usedElement = new Element(MetricXML.METRIC_ELEMENT);
			disk_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_NAME, "disk_used");
			disk_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_UNITS, disk_free.getUnits());
			disk_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_VAL, Double.toString(Double.parseDouble(disk_total.getValue()) - Double.parseDouble(disk_free.getValue())));
			Metric disk_used = new Metric(disk_usedElement);
			host.addContent(disk_used.toXML());
			// swap 
			Metric swap_total = (Metric)metrics.get(MetricNames.SWAP_TOTAL);
			host.addContent(swap_total.toXML());
			Metric swap_free = (Metric)metrics.get(MetricNames.SWAP_FREE);
			Element swap_usedElement = new Element(MetricXML.METRIC_ELEMENT);
			swap_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_NAME, "swap_used");
			swap_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_UNITS, swap_free.getUnits());
			swap_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_VAL, Double.toString(Double.parseDouble(swap_total.getValue()) - Double.parseDouble(swap_free.getValue())));
			Metric swap_used = new Metric(swap_usedElement);
			host.addContent(swap_used.toXML());
			// memory
			Metric mem_total = (Metric)metrics.get(MetricNames.MEM_TOTAL);
			host.addContent(mem_total.toXML());
			Metric mem_free = (Metric)metrics.get(MetricNames.MEM_FREE);
			Element mem_usedElement = new Element(MetricXML.METRIC_ELEMENT);
			mem_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_NAME, "mem_used");
			mem_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_UNITS, mem_free.getUnits());
			mem_usedElement.setAttribute(MetricXML.METRIC_ATTRIBUTE_VAL, Double.toString(Double.parseDouble(mem_total.getValue()) - Double.parseDouble(mem_free.getValue())));
			Metric mem_used = new Metric(mem_usedElement);
			host.addContent(mem_used.toXML());
			// proc
			Metric proc_total = (Metric)metrics.get(MetricNames.PROC_TOTAL);
			host.addContent(proc_total.toXML());
			Metric proc_run = (Metric)metrics.get(MetricNames.PROC_RUN);
			host.addContent(proc_run.toXML());	
			// load
			Metric load_one = (Metric)metrics.get(MetricNames.LOAD_ONE);
			host.addContent(load_one.toXML());
			Metric load_five = (Metric)metrics.get(MetricNames.LOAD_FIVE);
			host.addContent(load_five.toXML());
			Metric load_fifteen = (Metric)metrics.get(MetricNames.LOAD_FIFTEEN);
			host.addContent(load_fifteen.toXML());
			
			historyItem.addContent(host);
		}
		
		return historyItem;
	}
	
}
