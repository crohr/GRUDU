package ganglia.controller;
import ganglia.models.HistoryConfigModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author david
 *
 */
public class DataRetriever {

	private static String defaultCommand = "telnet ganglia 8649";
	private Document document = null;
	private Element racine = null;
	
	public void retrieveData(){
		try {
			
//            /* Create a connection instance */
//            Connection conn = new Connection("frontend");
//
//            /* Now connect */
//            conn.connect();
//            
//            /* Authenticate.
//             */
//            System.out.println(user + " " + keyFile + " " + passphrase + " " + preferredAccesFrontale );
//            File fichier = new File(this.keyFile);
//            boolean isAuthenticated = conn.authenticateWithPublicKey(this.user,
//                    fichier, this.passphrase);
//
//            if (isAuthenticated == false)
//                throw new IOException("Authentication failed.");
//
//            /* Create a session */
//
//            Session sess = conn.openSession();
//            
//            sess.requestPTY("toto",
//                    80, 80,
//                    16, 16,
//                    null);
//
//            sess.startShell();
//            
//            sess = conn.openSession();
//           String xmlFile = "";
//
//        	sess.execCommand("telnet " + gangliaFrontale+ " 8649 > /tmp/" + jobId + "_" + user);
//			InputStream stdout = new StreamGobbler(sess.getStdout());
//			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
//			//The three first lines should be removed
//			int numberOfLines = 0;
//			while (true) {
//				String line = br.readLine();
//				if (line == null)
//					break;
//				numberOfLines++;
//				if(numberOfLines >3){
//					xmlFile+=line;
//					System.out.println(line);
//				}
//			}
//			br.close();
//			sess.close();
//			conn.close();
			String xmlFile = "";
			String[] commands = {"ssh", "frontend","\"telnet", "ganglia","8649\""};
			String myCommand = "telnet ganglia 8649";
			Process myProcess = Runtime.getRuntime().exec(myCommand);
//			int i =myProcess.waitFor();
//			System.out.println(i);
			InputStream inStream = myProcess.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
			int numberOfLines = 0;
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				numberOfLines++;
				if(numberOfLines >3){
					xmlFile+=line;
				}
			}
			br.close();
			inStream.close();
			InputStream errStream = myProcess.getErrorStream();
			BufferedReader brErr = new BufferedReader(new InputStreamReader(errStream));
			while (true) {
				String line = brErr.readLine();
				if (line == null)
					break;
			}
			brErr.close();
			errStream.close();
			SAXBuilder builder = new SAXBuilder();
			
			document = builder.build(new StringBufferInputStream(xmlFile));
			racine = document.getRootElement();

        }
        catch (Exception e) {
        	System.err.println(e.getMessage());
            return;
        }
	}
	
	public Element getRootElement(){
		return racine;
	}
	
}
