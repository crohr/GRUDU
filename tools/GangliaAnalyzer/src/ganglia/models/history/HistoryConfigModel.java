/**
 * 
 */
package ganglia.models.history;

import java.util.ArrayList;

/**
 * @author david
 *
 */
public class HistoryConfigModel {

	/**
	 * period for each update of the history in milliseconds
	 */
	private long period;
	/**
	 * list of hosts to monitor
	 */
	private ArrayList<String> listOfHosts = null;
	/**
	 * Id of the reservation for the file where the history is stored
	 */
	private int jobId;
	/**
	 * name of the user
	 */
	private String userName = null;
	/**
	 * End of the reservation
	 */
	private long endOfTheReservation;
	/**
	 * pid of the distant java job that creates the history
	 */
	private int pidOfDistantJavaJob;
	/**
	 * range For the graph (should be expressed in milliseconds)
	 */
	private double rangeForTheGraph;
	/**
	 * Tells if the history is running
	 */
	private boolean historyRunning = false;
	/**
	 * Path to the java home
	 */
	private String javaPath = null;
	
	/**
	 * Connexion mode should be one of ONLINE or OFFLINE 
	 */
	private String connexionMode = null;
	
	public static final String ONLINE = "ONLINE";
	public static final String OFFLINE = "OFFLINE";
	
	/**
	 * Whether the user want to get the Ganglia history from a file (so offline)
	 */
	private String gangliaHistoryFile = null;
	
	
	/**
	 * @return the connexionMode
	 */
	public String getConnexionMode() {
		return connexionMode;
	}


	/**
	 * @param connexionMode the connexionMode to set
	 */
	public void setConnexionMode(String connexionMode) {
		this.connexionMode = connexionMode;
	}


	/**
	 * @return the gangliaHistoryFile
	 */
	public String getGangliaHistoryFile() {
		return gangliaHistoryFile;
	}


	/**
	 * @param gangliaHistoryFile the gangliaHistoryFile to set
	 */
	public void setGangliaHistoryFile(String gangliaHistoryFile) {
		this.gangliaHistoryFile = gangliaHistoryFile;
	}


	public HistoryConfigModel(){
	}


	/**
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}


	/**
	 * @param period the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}


	/**
	 * @return the listOfHosts
	 */
	public ArrayList<String> getListOfHosts() {
		return listOfHosts;
	}


	/**
	 * @param listOfHosts the listOfHosts to set
	 */
	public void setListOfHosts(ArrayList<String> listOfHosts) {
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


	/**
	 * @return the pidOfDistantJavaJob
	 */
	public int getPidOfDistantJavaJob() {
		return pidOfDistantJavaJob;
	}


	/**
	 * @param pidOfDistantJavaJob the pidOfDistantJavaJob to set
	 */
	public void setPidOfDistantJavaJob(int pidOfDistantJavaJob) {
		this.pidOfDistantJavaJob = pidOfDistantJavaJob;
	}


	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the endOfTheReservation
	 */
	public long getEndOfTheReservation() {
		return endOfTheReservation;
	}


	/**
	 * @param endOfTheReservation the endOfTheReservation to set
	 */
	public void setEndOfTheReservation(long endOfTheReservation) {
		this.endOfTheReservation = endOfTheReservation;
	}


	/**
	 * @return the rangeForTheGraph
	 */
	public double getRangeForTheGraph() {
		return rangeForTheGraph;
	}


	/**
	 * @param rangeForTheGraph the rangeForTheGraph to set
	 */
	public void setRangeForTheGraph(double rangeForTheGraph) {
		this.rangeForTheGraph = rangeForTheGraph;
	}


	/**
	 * @return the historyRunning
	 */
	public boolean isHistoryRunning() {
		return historyRunning;
	}


	/**
	 * @param historyRunning the historyRunning to set
	 */
	public void setHistoryRunning(boolean historyRunning) {
		this.historyRunning = historyRunning;
	}
	
	public String getFirstHost(){
		return listOfHosts.get(0);
	}


	/**
	 * @return the javaPath
	 */
	public String getJavaPath() {
		return javaPath;
	}


	/**
	 * @param javaPath the javaPath to set
	 */
	public void setJavaPath(String javaPath) {
		if(!isHistoryRunning()) this.javaPath = javaPath;
	}
}
