/**
 * 
 */
package ganglia.models;

import java.util.ArrayList;

/**
 * @author david
 *
 */
public class HistoryConfigModel {

	/**
	 * period for each update of the history in milliseconds
	 */
	private int period;
	/**
	 * list of hosts of the reservation
	 */
	private ArrayList listOfHosts = null;
	/**
	 * job id of the reservation to monitor
	 */
	private int jobId;
	
	public HistoryConfigModel(int aJobId, int refreshingPeriod, ArrayList aListOfHosts){
		period = refreshingPeriod;
		jobId = aJobId;
		listOfHosts = aListOfHosts;
	}
	
	public String getFirstHost(){
		return (String)listOfHosts.get(0);
	}

	/**
	 * @return the period
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * @return the listOfHosts
	 */
	public ArrayList getListOfHosts() {
		return listOfHosts;
	}

	/**
	 * @param listOfHosts the listOfHosts to set
	 */
	public void setListOfHosts(ArrayList listOfHosts) {
		this.listOfHosts = listOfHosts;
	}

	/**
	 * @return the jobId
	 */
	public int getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}	
	
}
