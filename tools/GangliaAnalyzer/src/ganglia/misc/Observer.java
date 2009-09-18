/**
 * 
 */
package ganglia.misc;

import ganglia.models.history.HistoryMetric;

import java.util.HashMap;

/**
 * @author david
 *
 */
public interface Observer {
	
	public void update(HistoryMetric newValues);
	
	public String getObserverName();
	
}
