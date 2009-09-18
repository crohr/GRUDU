/**
 * 
 */
package ganglia.controller.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

import ganglia.misc.Observable;
import ganglia.misc.Observer;
import ganglia.models.history.GangliaHistory;
import ganglia.models.history.HistoryConfigModel;
import ganglia.models.history.HistoryMetric;
import ganglia.views.WaitingFrame;

/**
 * @author david
 *
 */
public class GangliaHistoryController implements Observable {

	private HistoryConfigModel model = null;
	private HashMap<String, Observer> observerMap = null;
	HashMap<String, String> properties = null;
	private  Connection conn = null;
	
    /**
     * Number of milliseconds for a second
     */
    public static final long SECOND = 1000;
    /**
     * Number of milliseconds for a minute
     */
    public static final long MINUTE = SECOND*60;
    /**
     * Number of milliseconds for an hour
     */
    public static final long HOUR   = 60*MINUTE;
	
	public GangliaHistoryController(int jobId, ArrayList<String> hostsToMonitor, long endOfTheReservation, HashMap<String, String> properties){
		model = new HistoryConfigModel();
		model.setJobId(jobId);
		model.setListOfHosts(hostsToMonitor);
		model.setEndOfTheReservation(endOfTheReservation);

		this.properties = properties;
		model.setUserName(properties.get("user"));
		observerMap = new HashMap<String, Observer>();
	}
	
	public boolean isAlreadyLaunched(){
		try{
			/* Create a connection instance */
			conn = new Connection(properties.get("accesFrontale"));

			/* Now connect */
			conn.connect();

			/* Authenticate.
			 */
			File fichier = new File(properties.get("keyFile"));
			boolean isAuthenticated = conn.authenticateWithPublicKey(model.getUserName(),
					fichier, properties.get("passphrase"));

			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");

			Session sess = conn.openSession();
			
			sess.requestPTY("toto",
					80, 80,
					16, 16,
					null);

			sess.startShell();

			sess = conn.openSession();
			String destination = properties.get("destination");
			String command = "ssh " + destination + " \" "+ properties.get("connexionCommand")+ " " + model.getFirstHost() + " \\\" test -e " + "/tmp/" + model.getJobId() + "_" + model.getUserName() + " && echo 0 || echo 1 \\\" \"" ;
			sess.execCommand(command);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			ArrayList<String> linesOfOutput = new ArrayList<String>();
			while (true) {
				String line = br.readLine();
				if (line == null)break;
				linesOfOutput.add(line);
				//System.out.println(line);
			}
			br.close();
			InputStream stderr = new StreamGobbler(sess.getStderr());
			BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));

			while (true) {
				String line = brErr.readLine();
				if (line == null)break;
				//System.err.println(line);
			}
			brErr.close();
			sess.close();
			
			Iterator<String> iter = linesOfOutput.iterator();
			while(iter.hasNext()){
				String line = iter.next();
				if(line.equalsIgnoreCase("1")){
					model.setHistoryRunning(false);
					return false;
				}
				if(line.equalsIgnoreCase("0")){
					model.setHistoryRunning(true);
					return true;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		model.setHistoryRunning(false);
		return false;
	}
	
	public boolean validateConfiguration(String aRefreshingPeriod, String aRange){
		StringTokenizer tokenizer = null;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		long refreshingPeriod = 0;
		if(!model.isHistoryRunning()){
			tokenizer = new StringTokenizer(aRefreshingPeriod,":");
			hours = Integer.parseInt(tokenizer.nextToken());
			minutes = Integer.parseInt(tokenizer.nextToken());
			seconds = Integer.parseInt(tokenizer.nextToken());
			refreshingPeriod = hours*HOUR + minutes*MINUTE + seconds*SECOND;
		}else{
			refreshingPeriod = 0;
		}
		tokenizer = new StringTokenizer(aRange,":");
        hours = Integer.parseInt(tokenizer.nextToken());
        minutes = Integer.parseInt(tokenizer.nextToken());
        seconds = Integer.parseInt(tokenizer.nextToken());
        
        double range = hours*HOUR + minutes*MINUTE + seconds*SECOND;
        
		if(refreshingPeriod > 0 && range > 0){
			model.setPeriod(refreshingPeriod);
			model.setRangeForTheGraph(range);
			return true;
		}
		return false;
	}
	
	private void startOnlineGeneration(){
		if(model.isHistoryRunning()) updateOnline();
		else{
			final WaitingFrame waiting = new WaitingFrame("Waiting frame", "Ganglia History",1,false);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Thread thread = new Thread(new Runnable(){
						public void run(){
							waiting.launch("Launching distant Ganglia daemon ...");
							try{
								/* Create a connection instance */
								conn = new Connection(properties.get("accesFrontale"));

								/* Now connect */
								conn.connect();

								/* Authenticate.
								 */
								File fichier = new File(properties.get("keyFile"));
								boolean isAuthenticated = conn.authenticateWithPublicKey(model.getUserName(),
										fichier, properties.get("passphrase"));

								if (isAuthenticated == false)
									throw new IOException("Authentication failed.");

								Session sess = conn.openSession();

								sess.requestPTY("toto",
										80, 80,
										16, 16,
										null);

								sess.startShell();

								sess = conn.openSession();
								SCPClient client = conn.createSCPClient();
								String destination = properties.get("destination");
								System.out.println("scp " + System.getProperty("user.dir") + "/lib/GangliaHistoryMaker.jar" + " " + destination + ":$HOME/.diet/bin");
								client.put(System.getProperty("user.dir") + "/lib/GangliaHistoryMaker.jar", "$HOME/.diet/bin");
								
								System.out.println(destination);
								sess.execCommand("scp $HOME/.diet/bin/GangliaHistoryMaker.jar "+destination+":$HOME/.diet/bin");
								String arguments = model.getJobId() + " " + model.getPeriod() + " " + model.getUserName() + " " + model.getEndOfTheReservation();
								Iterator<String> iter = model.getListOfHosts().iterator();
								while(iter.hasNext()){
									arguments += " " + iter.next();
								}
								sess.close();
								sess = conn.openSession();
								String command = "ssh "+ destination + " \"" + properties.get("connexionCommand")+ " " + model.getFirstHost() + " \\\" nohup "+model.getJavaPath()+"/bin/java -jar $HOME/.diet/bin/GangliaHistoryMaker.jar "+ arguments +" & \\\" \"";
								System.out.println(command);
								sess.execCommand(command);
//								InputStream stdout = new StreamGobbler(sess.getStdout());
//								BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
//								while (true) {
//								String line = br.readLine();
//								if (line == null)
//								break;
//								System.out.println(line);
//								}
//								br.close();
//								InputStream stderr = new StreamGobbler(sess.getStderr());
//								BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));

//								while (true) {
//								String line = brErr.readLine();
//								if (line == null)
//								break;
//								System.err.println(line);
//								}
//								brErr.close();
								sess.close();

								waiting.incrementProgressBar();
								waiting.dispose();
								conn.close();
							}
							catch(Exception e){
								e.printStackTrace();
								waiting.dispose();
							}
						}
					});
					thread.start();
				}
			}
			);
		}
	}
	
	private void startOfflineGeneration(){
		
	}
	
	public void startHistoryGenerator(){
		if(model.getConnexionMode().equalsIgnoreCase(HistoryConfigModel.OFFLINE)) startOfflineGeneration();
		else startOnlineGeneration();
		model.setHistoryRunning(true);
	}

	/* (non-Javadoc)
	 * @see ganglia.misc.Observable#addObserver(ganglia.misc.Observer)
	 */
	public void addObserver(Observer anObserver) {
		observerMap.put(anObserver.getObserverName(), anObserver);
		
	}

	/* (non-Javadoc)
	 * @see ganglia.misc.Observable#removeObserver(ganglia.misc.Observer)
	 */
	public void removeObserver(Observer anObserver) {
		observerMap.remove(anObserver.getObserverName());
		
	}
	
	public void updateOnline(){
		final WaitingFrame waiting = new WaitingFrame("Waiting frame", "Ganglia History Update",2,true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Thread thread = new Thread(new Runnable(){
					public void run(){
						waiting.launch("Retrieving history  ...");
						try{

							conn = new Connection(properties.get("accesFrontale"));

							/* Now connect */
							conn.connect();

							/* Authenticate.
							 */
							File fichier = new File(properties.get("keyFile"));
							boolean isAuthenticated = conn.authenticateWithPublicKey(model.getUserName(),
									fichier, properties.get("passphrase"));

							if (isAuthenticated == false)
								throw new IOException("Authentication failed.");

							Session sess = conn.openSession();

							sess.requestPTY("toto",
									80, 80,
									16, 16,
									null);

							sess.startShell();

							sess = conn.openSession();
							String scpCommand = properties.get("copyCommand");
							String destination = properties.get("destination");
							// here we do : 
							// 1 - ssh on the frontale of the job site
							// 2 - scp the file from the job main node to the frontale of the job site
							// 3 - scp the file from the frontale of the job site to the accesFrontale
							sess.execCommand("ssh " + destination + " \" " + scpCommand +" "+model.getFirstHost() +":/tmp/" + model.getJobId() + "_" + model.getUserName() + " /tmp \" ; scp " + destination + ":/tmp/" + model.getJobId() + "_" + model.getUserName() + " /tmp");
							InputStream stdout = new StreamGobbler(sess.getStdout());
							BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
							while (true) {
								String line = br.readLine();
								if (line == null)
									break;
								System.out.println(line);
							}
							br.close();
							InputStream stderr = new StreamGobbler(sess.getStderr());
							BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));

							while (true) {
								String line = brErr.readLine();
								if (line == null)
									break;
								System.err.println(line);
							}
							brErr.close();
							sess.close();
							SCPClient scpClient = conn.createSCPClient();
							scpClient.get("/tmp/"+ model.getJobId() + "_" + model.getUserName(), System.getProperty("user.home") + "/.diet/scratch/");
							waiting.setSize(550,180);
							waiting.incrementProgressBar();
							waiting.setStatusText("Generating & Updating the history data ...");
							SAXBuilder builder = new SAXBuilder();
							Document doc = null;
							Element rootElement = null;
							doc = builder.build(new File(System.getProperty("user.home") + "/.diet/scratch/"+model.getJobId() + "_" + model.getUserName()));
							rootElement = doc.getRootElement();
							GangliaHistory history = new GangliaHistory(rootElement);
							HashMap<String, HashMap<String, HistoryMetric>> historyData = GangliaHistoryDataGenerator.getHistoryData(history, model.getListOfHosts());

							Iterator<String> iter = observerMap.keySet().iterator();
							while(iter.hasNext()){
								String observerName = iter.next();
								Observer anObserver =  observerMap.get(observerName);
								StringTokenizer tokenizer = new StringTokenizer(observerName,"|");
								String host = tokenizer.nextToken();
								String metric = tokenizer.nextToken();
								Iterator<String> iterMetricsNames = historyData.get(host).keySet().iterator();
								while(iterMetricsNames.hasNext()){
									String metricName = iterMetricsNames.next();
									if(metricName.contains(metric)) anObserver.update(historyData.get(host).get(metricName));
								}
								//anObserver.update(historyData.get(host).get(metric));
							}
							waiting.incrementProgressBar();
							waiting.dispose();
							conn.close();
						}
						catch(Exception e){
							e.printStackTrace();
							waiting.dispose();
						}
					}
				});
				thread.start();
			}
		}
		);



	}
	
	public void updateOffline(){
		final WaitingFrame waiting = new WaitingFrame("Waiting frame", "Ganglia History Update",1,false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Thread thread = new Thread(new Runnable(){
					public void run(){
						waiting.launch("Generating Ganglia history data  ...");
						try{
							SAXBuilder builder = new SAXBuilder();
							Document doc = null;
							Element rootElement = null;
							doc = builder.build(new File(model.getGangliaHistoryFile()));
							rootElement = doc.getRootElement();
							GangliaHistory history = new GangliaHistory(rootElement);
							HashMap<String, HashMap<String, HistoryMetric>> historyData = GangliaHistoryDataGenerator.getHistoryData(history, model.getListOfHosts());

							Iterator<String> iter = observerMap.keySet().iterator();
							while(iter.hasNext()){
								String observerName = iter.next();
								Observer anObserver =  observerMap.get(observerName);
								StringTokenizer tokenizer = new StringTokenizer(observerName,"|");
								String host = tokenizer.nextToken();
								String metric = tokenizer.nextToken();
								Iterator<String> iterMetricsNames = historyData.get(host).keySet().iterator();
								while(iterMetricsNames.hasNext()){
									String metricName = iterMetricsNames.next();
									if(metricName.contains(metric)) anObserver.update(historyData.get(host).get(metricName));
								}
								//anObserver.update(historyData.get(host).get(metric));
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
						waiting.incrementProgressBar();
						waiting.dispose();
					}
				});
				thread.start();
			}
		}
		);

	}

	/* (non-Javadoc)
	 * @see ganglia.misc.Observable#updateObserver()
	 */
	public void updateObserver() {
		if(model.getConnexionMode().equalsIgnoreCase(HistoryConfigModel.OFFLINE)) updateOffline();
		else updateOnline();
	}

	/**
	 * @return the model
	 */
	public HistoryConfigModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(HistoryConfigModel model) {
		this.model = model;
	}
	
}
