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
public class HistoryItem implements HistoryXML {

	private HashMap<String, Host> hosts = null;
	
	private long date;
	
	public HistoryItem(Element anXMLElement){
		date = Long.parseLong(anXMLElement.getAttributeValue(DATE));
		List<Element> hostsElement = anXMLElement.getChildren(HostXML.HOST_ELEMENT);
		Iterator<Element> iter = hostsElement.iterator();
		hosts = new HashMap<String, Host>();
		while(iter.hasNext()){
			Host anhost = new Host(iter.next());
			hosts.put(anhost.getName(), anhost);
		}
	}

	/**
	 * @return the hosts
	 */
	public HashMap<String, Host> getHosts() {
		return hosts;
	}

	/**
	 * @param hosts the hosts to set
	 */
	public void setHosts(HashMap<String, Host> hosts) {
		this.hosts = hosts;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}
	
}
