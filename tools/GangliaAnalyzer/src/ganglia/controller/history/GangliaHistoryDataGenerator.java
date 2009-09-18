/**
 * 
 */
package ganglia.controller.history;

import ganglia.models.history.GangliaHistory;
import ganglia.models.history.HistoryItem;
import ganglia.models.history.HistoryMetric;
import ganglia.models.history.Host;
import ganglia.models.history.Metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author david
 *
 */
public class GangliaHistoryDataGenerator {

	public static HashMap<String, HashMap<String, HistoryMetric>> getHistoryData(GangliaHistory history, ArrayList<String> hosts){
		
		HashMap<String, HashMap<String,  HistoryMetric>> historyData = new HashMap<String, HashMap<String,  HistoryMetric>>();
		for(int i = 0 ; i < hosts.size() ; i++){
			historyData.put(hosts.get(i), new HashMap<String,  HistoryMetric>());
		}
		HashMap<Long, HistoryItem> historyItemHashMap = history.getHistoryItemsMap();
		Iterator<Long> iter = historyItemHashMap.keySet().iterator();
		// on boucle sur toutes le périodes d'histoire
		while(iter.hasNext()){
			
			Long date = iter.next();
			HistoryItem item = historyItemHashMap.get(date);
			HashMap<String, Host> hostsMap = item.getHosts();
			Iterator<String> iterHost = hostsMap.keySet().iterator();
			// on boucle sur tous les hotes
			while(iterHost.hasNext()){
				String hostName = iterHost.next();
				Host anHost = hostsMap.get(hostName);
				HashMap<String, Metric> hostMetrics = anHost.getMetrics();
				Iterator<String> iterMetrics = hostMetrics.keySet().iterator();
				// on boucle sur toutes les metriques
				
				while(iterMetrics.hasNext()){
					Metric aMetric = hostMetrics.get(iterMetrics.next());
					HashMap<String, HistoryMetric> hMetricMap = historyData.get(hostName);
					if(hMetricMap.get(aMetric.getName()) == null){
						HistoryMetric aHMetric = new HistoryMetric(aMetric.getName(),aMetric.getUnits());
						aHMetric.putValue(date, Double.parseDouble(aMetric.getValue()));
						hMetricMap.put(aMetric.getName(),aHMetric );
					}
					else{
						hMetricMap.get(aMetric.getName()).putValue(date, Double.parseDouble(aMetric.getValue()));
					}
					
				}
			}
		}
		
		return historyData;
	}
	
}
