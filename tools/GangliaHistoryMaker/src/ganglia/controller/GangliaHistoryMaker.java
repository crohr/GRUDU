package ganglia.controller;
import ganglia.models.HistoryConfigModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 
 */

/**
 * @author david
 *
 */
public class GangliaHistoryMaker {

	/**
	 * Main method of the class and the jar file.
	 * Three arguments minimum should be supplied:
	 *  - the id of the job to monitor
	 *  - the period of refreshing
	 *  - at least one node to monitor
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 4){
			help();
			System.exit(0);
		}
		// let's go
		int jobId = Integer.parseInt(args[0]);
		int refreshingPeriod = Integer.parseInt(args[1]);
		String user = args[2];
		long endOftheReservation = Long.parseLong(args[3]);
		ArrayList listOfHosts = new ArrayList();
		for(int i = 4 ; i < args.length; i ++){
			listOfHosts.add(args[i]);
		}
		HistoryConfigModel model = new HistoryConfigModel(jobId,refreshingPeriod,listOfHosts);
		File fichier = new File("/tmp/"+jobId + "_" + user);
		try{
			fichier.createNewFile();
			Document document = null;
			Element rootElementOfOutput = new Element("ganglia_history");
			document = new Document(rootElementOfOutput);
			System.out.println(System.currentTimeMillis());
			while(System.currentTimeMillis() < endOftheReservation){
				System.out.println(" ---------------------- ");
				System.out.println(System.currentTimeMillis() + " : Retrieving data ...");
				DataRetriever dataRetriever = new DataRetriever();
				dataRetriever.retrieveData();
				Element rootElement = dataRetriever.getRootElement();
				System.out.println(System.currentTimeMillis() + " : Extracting Reserved Nodes data ...");
				ArrayList listOfHostObjects = ReservationNodesExtractor.getReservationNodes(rootElement, model);
				System.out.println(System.currentTimeMillis() + " : Generating history ...");
				rootElementOfOutput.addContent(HistoryGenerator.generateHistory(listOfHostObjects));
				System.out.println(System.currentTimeMillis() + " : Outputing the history ...");
				XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
				sortie.output(document, new FileOutputStream(fichier));
				System.out.println(System.currentTimeMillis() + " : Waiting For Next Step ...");
				Thread.sleep(refreshingPeriod);
			}
			System.out.println(" ---------------------- ");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void help(){
		System.out.println("Application : Ganglia History Maker");
		System.out.println("");
		System.out.println("Purpose     : Generation of a file containing the history of the main");
		System.out.println("              metrics for the nodes of the reservation whose id is ");
		System.out.println("              specified in first argument.");
		System.out.println("");
		System.out.println("Usage       : java -jar GangliaHistoryMaker \n" +
				"\t\t\t<jobId> \n" +
				"\t\t\t<refreshingPeriod (in ms)> \n" +
				"\t\t\t<userName> \n"+
				"\t\t\t<end of the reservation (time in milliseconds)> \n"+
				"\t\t\t<list of nodes to monitor>");
		System.out.println("");
		GregorianCalendar calendar = new java.util.GregorianCalendar(); 
		calendar.add(calendar.MINUTE, 10);
		System.out.println(calendar.getTimeInMillis());
	}

}
