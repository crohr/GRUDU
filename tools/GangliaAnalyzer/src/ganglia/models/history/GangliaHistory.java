/**
 * 
 */
package ganglia.models.history;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * @author david
 *
 */
public class GangliaHistory implements HistoryXML {

	private HashMap<Long, HistoryItem> historyItemsMap = null;
	
	public GangliaHistory(Element anXMLElement){
		List<Element> historyItemsList = anXMLElement.getChildren(HISTORY_ITEM);
		Iterator<Element> iter = historyItemsList.iterator();
		historyItemsMap = new HashMap<Long, HistoryItem>();
		while(iter.hasNext()){
			HistoryItem item = new HistoryItem(iter.next());
			historyItemsMap.put(item.getDate(), item);
		}
	}

	/**
	 * @return the historyItemsMap
	 */
	public HashMap<Long, HistoryItem> getHistoryItemsMap() {
		return historyItemsMap;
	}

	/**
	 * @param historyItemsMap the historyItemsMap to set
	 */
	public void setHistoryItemsMap(HashMap<Long, HistoryItem> historyItemsMap) {
		this.historyItemsMap = historyItemsMap;
	}
	
}
