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
public class Cluster implements ClusterXML, HostXML{

	private String name = null;
	private String localTime = null;
	private String owner = null;
	private HashMap hosts = null;
	
	public Cluster(Element xmlElement){
		name = xmlElement.getAttributeValue(CLUSTER_ATTRIBUTE_NAME);
		localTime = xmlElement.getAttributeValue(CLUSTER_ATTRIBUTE_LOCALTIME);
		owner = xmlElement.getAttributeValue(CLUSTER_ATTRIBUTE_OWNER);
		List aListOfHosts = xmlElement.getChildren(HOST_ELEMENT);
		Iterator iter = aListOfHosts.iterator();
		hosts = new HashMap();
		while(iter.hasNext()){
			Element aHostXML = (Element)iter.next();
			Host aHost = new Host(aHostXML);
			hosts.put(aHost.getName(), aHost);
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
	 * @return the localTime
	 */
	public String getLocalTime() {
		return localTime;
	}

	/**
	 * @param localTime the localTime to set
	 */
	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the hosts
	 */
	public HashMap getHosts() {
		return hosts;
	}

	/**
	 * @param hosts the hosts to set
	 */
	public void setHosts(HashMap hosts) {
		this.hosts = hosts;
	}
	
}
