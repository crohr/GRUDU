/**
 * 
 */
package ganglia.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ganglia.models.HistoryConfigModel;
import ganglia.models.Host;

import org.jdom.Element;

/**
 * @author david
 *
 */
public class ReservationNodesExtractor {

	
	public static ArrayList getReservationNodes(Element anElement, HistoryConfigModel theModel){
		ArrayList reservationNodesElements = new ArrayList();
		Element cluster = anElement.getChild("CLUSTER");
		List hosts = cluster.getChildren("HOST");
		Iterator iter = hosts.iterator();
		ArrayList listOfHostsTomonitor = theModel.getListOfHosts();
		while(iter.hasNext()){
			Element anHost = (Element)(iter.next());
			String nameOfHost = anHost.getAttributeValue("NAME");
			if(listOfHostsTomonitor.contains(nameOfHost)){
				reservationNodesElements.add(new Host(anHost));
			}
		}
		return reservationNodesElements;
	}
}
