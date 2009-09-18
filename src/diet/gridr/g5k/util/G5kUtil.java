/****************************************************************************/
/* G5K utility class                                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kUtil.java,v 1.6 2007/11/19 15:16:20 dloureir Exp $
 * $Log: G5kUtil.java,v $
 * Revision 1.6  2007/11/19 15:16:20  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.5  2007/07/16 14:22:24  dloureir
 * A method has been added. This method generates a JDOM Element. tis method will perhaps e used in the future for the management of modules that need information from GRUDU.
 *
 * Revision 1.4  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.3  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.2  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.1  2007/02/22 14:11:36  aamar
 * Initial revision.
 *
 ****************************************************************************/
package diet.gridr.g5k.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import diet.logging.LoggingManager;

/**
 * G5K utility class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class G5kUtil {
    /**
     * Create the .diet directory in home.
     *
     * @param conn the ssh connection
     */
    public static void initDietDir(Connection conn) {
        LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, G5kUtil.class.getName(), "initDietDir", "Init the .DIET directory");
        try{
            Session sess = conn.openSession();
            // Create the .diet/bin directory if doesn't exist
            sess.execCommand("mkdir -p $HOME/.diet/bin; mkdir -p $HOME/.scratch");
            sess.close();
        }
        catch(Exception e){
            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, G5kUtil.class.getName(), "initDietDir",e);
        }
    } // end void initDietDir
    
    public static Element generateG5kCfgTreeForModules(){
    	SAXBuilder sxb = new SAXBuilder();
    	Element rootElement = new Element("GRUDU_configuration");
    	Document document = null;
    	try{
    		// information of the g5k.xml XML file
    		File file = new File(System.getProperty("user.home") + "/.diet/g5k.xml");
    		document = sxb.build(file);
    		Element g5kElement = document.getRootElement();
    		Element keyElement = new Element("passphrase");
    		keyElement.setText(G5kCfg.getSSHKey());
    		g5kElement.addContent(keyElement);
    		rootElement.addContent(g5kElement.detach());
    		// information of the g5k_clusters.xml XMl file
    		// some other information will be added to complete the informations
    		// such as the access frontales adresses
    		file = new File(System.getProperty("user.home") + "/.diet/g5k_clusters.xml");
    		document = sxb.build(file);
    		Element g5kClustersElement = document.getRootElement();
    		g5kClustersElement.setName("g5k_clusters");
    		List<Element> elementList = g5kClustersElement.getChildren("cluster");
    		Iterator<Element> iter = elementList.iterator();
    		while(iter.hasNext()){
    			Element clusterElement = iter.next();
    			String clusterName = clusterElement.getAttributeValue("id");
    			String internal_frontale = G5kSite.getInternalFrontals()[G5kCluster.getSiteIndexForCluster(G5kCluster.getIndexForCluster(clusterName))];
    			clusterElement.setAttribute("internal_frontale", internal_frontale);
    			String external_frontale = G5kSite.getExternalFrontals()[G5kCluster.getSiteIndexForCluster(G5kCluster.getIndexForCluster(clusterName))];
    			clusterElement.setAttribute("external_frontale", external_frontale);
    		}
    		rootElement.addContent(g5kClustersElement.detach());
    	}
    	catch(Exception e){
    		
    	}
    	return rootElement;
    }
}