/**
 * 
 */
package ganglia.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

/**
 * @author david
 *
 */
public class DataRetriever {

	private String user = null;
	private String keyFile = null;
	private String passphrase = null;
	private String preferredAccesFrontale = null;
	private String gangliaFrontale = null;
	private String connexionCommand = null;
	private static String defaultCommand = "telnet ganglia 8649";
	private Document document = null;
	private Element racine = null;
	
	public DataRetriever(HashMap<String, String> properties){
		user = properties.get("user");
		keyFile = properties.get("keyFile");
		passphrase = properties.get("passphrase");
		preferredAccesFrontale = properties.get("accesFrontale");
		gangliaFrontale = properties.get("destination");
		connexionCommand = properties.get("connexionCommand");
	}
	
	public void retrieveData(){
		try {
            /* Create a connection instance */
            Connection conn = new Connection(preferredAccesFrontale);

            /* Now connect */
            conn.connect();
            
            /* Authenticate.
             */
            File fichier = new File(this.keyFile);
            boolean isAuthenticated = conn.authenticateWithPublicKey(this.user,
                    fichier, this.passphrase);

            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");

            /* Create a session */

            Session sess = conn.openSession();
            
            sess.requestPTY("toto",
                    80, 80,
                    16, 16,
                    null);

            sess.startShell();
            
            sess = conn.openSession();
           String xmlFile = "";
//           if(gangliaFrontale.contains("sophia")) sess.execCommand("ssh " + gangliaFrontale + " \"" + sophiaCommand +"\"");
//           else{
        	   sess.execCommand(connexionCommand + " " + gangliaFrontale + " \"" + defaultCommand +"\"");
//           }
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			//The three first lines should be removed
			int numberOfLines = 0;
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				numberOfLines++;
				//System.out.println(line);
				if(numberOfLines >3) xmlFile+=line;
			}
			br.close();
			sess.close();
			InputStream stderr = new StreamGobbler(sess.getStderr());
			BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
			while (true) {
				String line = brErr.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}
			brErr.close();
			conn.close();
			
			SAXBuilder builder = new SAXBuilder();
			
			document = builder.build(new StringBufferInputStream(xmlFile));
			racine = document.getRootElement();

        }
        catch (Exception e) {
        	e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection failed");
            return;
        }
	}
	
	public Element getRootElement(){
		return racine;
	}
	
}
