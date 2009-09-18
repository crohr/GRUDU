/**
 * 
 */
package ganglia.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdom.Element;

import ganglia.models.Cluster;
import ganglia.models.ClusterXML;
import ganglia.views.ClusterView;
import ganglia.views.WaitingFrame;

/**
 * @author david
 *
 */
public class GlobalDataController {

	JPanel dataPanel = null;
	private HashMap<String, String> properties = null;
	private boolean gangliaInfoGenerated = false;
	
	public GlobalDataController( HashMap<String, String> properties,JPanel aPanel){
		dataPanel = aPanel;
		this.properties = properties;
	}
	
	public void start(){
		if(!gangliaInfoGenerated){
			final WaitingFrame waiting = new WaitingFrame("Waiting frame", "Ganglia Information",4,true);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Thread thread = new Thread(new Runnable(){
						public void run(){
							waiting.setStatusText("Retrieving data from the ganglia machine on " + properties.get("destination") + " ...");
							waiting.launch("Retrieving data from the ganglia machine on " + properties.get("destination") + " ...");
							waiting.setSize(550,180);
					        waiting.incrementProgressBar();
							DataRetriever dataRetriever = new DataRetriever(properties);
							dataRetriever.retrieveData();
							waiting.setStatusText("Generating the Ganglia data ...");
							waiting.incrementProgressBar();
							Element racine = dataRetriever.getRootElement();
							Element clusterElement = racine.getChild(ClusterXML.CLUSTER_ELEMENT);
							Cluster cluster = new Cluster(clusterElement);
							waiting.setStatusText("Generating the Ganglia charts ...");
							waiting.incrementProgressBar();
							dataPanel = new ClusterView(cluster);
					        waiting.dispose();
							gangliaInfoGenerated = true;
						}
					});
					thread.start();
				}
			}
			);
		}
	}

	/**
	 * @return the dataPanel
	 */
	public JPanel getDataPanel() {
		return dataPanel;
	}

	/**
	 * @param dataPanel the dataPanel to set
	 */
	public void setDataPanel(JPanel dataPanel) {
		this.dataPanel = dataPanel;
	}
	
	
	
}
