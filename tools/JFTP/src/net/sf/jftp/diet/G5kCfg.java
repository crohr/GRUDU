/****************************************************************************/
/* This class corresponds to the Grid5000 configuration                     */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kCfg.java,v 1.2 2007/11/06 12:15:47 dloureir Exp $
 * $Log: G5kCfg.java,v $
 * Revision 1.2  2007/11/06 12:15:47  dloureir
 * New versions of configuration files
 *
 * Revision 1.20  2007/11/05 17:26:05  dloureir
 * Bug correction : when saving the variables and adding/removing/importing variables some variables their were some mistakes in the xml files and in the GUI.
 * Bug correction : the output of the mapping tool is now correct : good variables are generated and the output is well formatted
 *
 * Revision 1.19  2007/10/23 15:18:35  dloureir
 * Adding the path for the JAVA Home when deploying remotely a DIET hierarchy
 *
 * Revision 1.18  2007/10/12 13:52:28  dloureir
 * Adding a method for the creation of a configuration file when it is accidently removed
 *
 * Revision 1.17  2007/07/13 13:36:27  dloureir
 * The SSH pass phrase is no more written on standard output...
 *
 * Revision 1.16  2007/07/12 15:32:58  dloureir
 * Some code clean up and typo corrections.
 *
 * Revision 1.15  2007/07/11 08:35:03  dloureir
 * Adding an SSHAgent for the correction of bug 36. The ssh key is nomore written on the disk. It will be ask at every application startup and stored in memory during the execution.
 *
 * Revision 1.14  2007/06/26 15:06:13  dloureir
 * The ways the environment variables are stored has been modified to be compliant with GoDIET (the last version). You can now supply all the variables you want for each sites.
 *
 * Revision 1.13  2007/05/02 15:22:38  aamar
 * Add Java_Home property
 *
 * Revision 1.12  2007/05/02 12:50:58  dloureir
 * Adding the support of the batch schedulers for the SeD in the Wizzard (through an updated XMLGoDIETgenerator version) and the log option for GoDIET
 *
 * Revision 1.11  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.10  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package net.sf.jftp.diet;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.xpath.*;

import java.io.*;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * This class corresponds to the Grid500 configuration
 * 
 * TODO : the management of the configuration file should be done with something
 * else than Xpath, perhaps jdom. It will be simpler for he creation of the file
 * for example...
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kCfg {

    /**
     * Index for the Username
     */
    public static final int USERNAME             = 0;
    /**
     * Index for the SSH key file
     */
    public static final int SSHKEYFILE           = 1;
    /**
     * Index for the GoDIET scratch directory
     */
    public static final int GODIETSCRATCH        = 2;
    /**
     * Index for the GoDIET debug level
     */
    public static final int GODIETDEBUG          = 3;
    /**
     * Index for the GoDIET unique dirs parameter
     */
    public static final int GODIETUSEUNIQUEDIRS  = 4;
    /**
     * Index for the GoDIET save stdOut parameter
     */
    public static final int GODIETSAVESTDOUT     = 5;
    /**
     * Index for the GoDIET save stdErr
     */
    public static final int GODIETSAVESTDERR     = 6;
    /**
     * Index for the preferred access point
     */
    public static final int PREFERREDACCESPOINT  = 7;
	/**
     * Index for the logging in GoDIET
     */
    public static final int LOGGINGINGODIET      = 8;
    /**
     * Index specifying if the tip of the day has to be shown on startup
     */
    public static final int SHOWTOTDONSTARTUP = 9;
    /**
     * 
     */
    public static final int JAVAHOME = 10;
    /**
     * Number of parameters
     */
    private static final int GLOBAL_PARAM_COUNT  = 11;

    /**
     * Array containing the elements corresponding to each
     * parameters
     */
    private static String [] globalParam = new String[GLOBAL_PARAM_COUNT];

    /**
     * String corresponding to the KaDeploy queue name
     */
    public static final String KADEPLOY_QUEUE_NAME = "deploy";

    /**
     * Method returning the string corresponding to the parameter i
     *
     * @param i parameter of the string we want to return
     *
     * @return string corresponding to the i parameter
     */
    public static String get(int i) {
        if ( (i<0) ||
                (i>=GLOBAL_PARAM_COUNT) ||
                (globalParam[i] == null) )
            return "";
        return globalParam[i];
    }

    /**
     * Method setting the value corresponding to the parameter given in argument
     *
     * @param paramIndex value index
     * @param param value
     */
    public static void set(int paramIndex, String param) {
        if (paramIndex<0 || paramIndex>=GLOBAL_PARAM_COUNT)
            return;
        globalParam[paramIndex] = param;
    }

    /**
     * Array containing the configuration of every sites
     */
    private static SiteCfg [] sitesCfg = new SiteCfg[G5kSite.getSitesNumber()];

    /**
     * Method returning the SiteCfg of a Site
     *
     * @param i index of a site
     * @return the SiteCfg corresponding the the index given in argument
     */
    public static SiteCfg getSite(int i) {
        if (i<0 || i>= G5kSite.getSitesNumber()) {
            return null;
        }
        //
        if(sitesCfg[i] == null){
            sitesCfg[i] = new SiteCfg();
        }
        //
        return sitesCfg[i];
    }

    /**
     * Method setting the SiteCfg corresponding to a certain index
     *
     * @param siteIndex index of a site
     * @param cfg SiteCfg of this site
     */
    public static void setSite(int siteIndex, SiteCfg cfg) {
        if (siteIndex<0 || siteIndex>=G5kSite.getSitesNumber())
            return;
        sitesCfg[siteIndex] = cfg;
    }

    /**
     * Method initializing the configuration
     *
     */
    public static void initCfg() {
        String homeDir = System.getProperty("user.home");
        File cfgDir = new File(homeDir + "/.diet");
        if (cfgDir.exists() && (!cfgDir.isDirectory())) {
            System.exit(1);
        }

        try {
            if (!cfgDir.exists()) {
                cfgDir.mkdir();
            }
        }
        catch (SecurityException e) {
            System.exit(1);
        }

        readCfgFile();
    } // end initCfg

    /**
     * Method realizing the reading of the configuration file
     *
     */
    public static void readCfgFile() {
        String cfgFileName = System.getProperty("user.home") + "/.diet/g5k.xml";
        File cfgFile = new File(cfgFileName);

        if (!cfgFile.exists()) {
            generateNewConfigFile();
            return;
        }

        getProperties(cfgFile);
    }
    
    private static void generateNewConfigFile(){
    	set(GODIETDEBUG, "0");
    	set(GODIETSAVESTDERR, "yes");
    	set(GODIETSAVESTDOUT,"yes");
    	set(GODIETSCRATCH,"");
    	set(GODIETUSEUNIQUEDIRS,"no");
    	set(LOGGINGINGODIET,"yes");
    	set(PREFERREDACCESPOINT,"");
    	set(SHOWTOTDONSTARTUP,"yes");
    	set(USERNAME,"");
    	set(SSHKEYFILE,"");
    	set(JAVAHOME,"");
    	for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
    		SiteCfg cfg = new SiteCfg();
    		cfg.set(cfg.SCRATCH_RUNTIME,"");
    		setSite(i, cfg);
    	}
    	save();
    }

    /**
     * Method loading the configuration properties from a configuration file
     *
     * @param cfgFile configuration file
     */
    public static void getProperties(File cfgFile) {
        Document doc = getDoc(cfgFile);

        try {
            XPathFactory xpfactory = XPathFactory.newInstance();
            XPath xpath = xpfactory.newXPath();

            String userName                 = xpath.evaluate("/g5k/username/@id", doc);
            String sshKey                   = xpath.evaluate("/g5k/sshkey/@file", doc);
            String goScratch                = xpath.evaluate("/g5k/goDietScratch/@dir", doc);
            String goDebug                  = xpath.evaluate("/g5k/goDiet/@debug", doc);
            String goUseUniqueDirs          = xpath.evaluate("/g5k/goDiet/@useUniqueDirs", doc);
            String goSaveStdOut             = xpath.evaluate("/g5k/goDiet/@saveStdOut", doc);
            String goSaveStdErr             = xpath.evaluate("/g5k/goDiet/@saveStdErr", doc);
            String preferredAccesPoint      = xpath.evaluate("/g5k/preferredAccesPoint/@host", doc);
            String loggingInGoDIET          = xpath.evaluate("/g5k/goDiet/@loggingInGoDIET", doc);
            String showTipOfTheDayOnStartup = xpath.evaluate("/g5k/applicationProperties/@showTOTD", doc);
            String javaHomePath 			= xpath.evaluate("/g5k/javaHome/@path", doc );
            set(G5kCfg.USERNAME,            userName);
            set(G5kCfg.SSHKEYFILE,          sshKey);
            set(G5kCfg.GODIETSCRATCH,       goScratch);
            set(G5kCfg.GODIETDEBUG,         goDebug);
            set(G5kCfg.GODIETUSEUNIQUEDIRS, goUseUniqueDirs);
            set(G5kCfg.GODIETSAVESTDOUT,    goSaveStdOut);
            set(G5kCfg.GODIETSAVESTDERR,    goSaveStdErr);
            set(G5kCfg.PREFERREDACCESPOINT, preferredAccesPoint);
            set(G5kCfg.LOGGINGINGODIET,     loggingInGoDIET);
            set(G5kCfg.SHOWTOTDONSTARTUP,   showTipOfTheDayOnStartup);
            set(G5kCfg.JAVAHOME,            javaHomePath);

            // get g5k sites
            int count = ((Number) xpath.evaluate("count(/g5k/site)",
                    doc, XPathConstants.NUMBER)).intValue();
            for (int ix=0; ix < count; ix++) {
            	Node node = (Node) xpath.evaluate("/g5k/site[" + (ix+1) +"]", doc, XPathConstants.NODE);
            	String siteId         = xpath.evaluate("@id", node);
            	String runtimeScratch = xpath.evaluate(SiteCfg.SCRATCH_RUNTIME +"/@dir", node);

            	SiteCfg siteCfg = new SiteCfg();
            	siteCfg.set(SiteCfg.SCRATCH_RUNTIME,runtimeScratch);
            
            	
            	int count_variables = ((Number) xpath.evaluate("count (variable)",node, XPathConstants.NUMBER)).intValue();
            	for (int ix1=0; ix1 < count_variables; ix1++) {
            		Node nodeVariable = (Node) xpath.evaluate("variable["+ (ix1+1) + "]",node,XPathConstants.NODE);
            		String variableKey   = xpath.evaluate("@key",nodeVariable);
            		String variableValue = xpath.evaluate("@value", nodeVariable); 
            		siteCfg.set(variableKey, variableValue);
            	}
            	// TODO passer a un tableau de taille le nombre de sites
            	int siteIndex = G5kSite.getIndexForSite(siteId);
            	setSite(siteIndex, siteCfg);
                
            }
        }
        catch (javax.xml.xpath.XPathExpressionException e) {
        }
    }

    /**
     * Method returning the document of the file given in argument
     *
     * @param file file from which we want to get the document
     *
     * @return document of the file given in argument
     */
    private static Document getDoc(File file) {
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource in =
                new InputSource(new FileInputStream(file));
            Document doc = builder.parse(in);
            return doc;
        }
        catch (ParserConfigurationException e) {
            return null;
        }
        catch (SAXException e) {
            return null;
        }
        catch (IOException e) {
            return null;
        }
        // Unreachable
    } // end getDoc

    /**
     * Method transforming the Object to XML
     *
     * @return aString representing the XML serialization of this Object
     */
    public static String toXML() {
    	String xml =  "<?xml version=\"1.0\" standalone=\"yes\"?>" + newline;
        xml += "<g5k>" + newline;
        // usename
        xml += nSpaces(2) +
        "<preferredAccesPoint host=\"" + get(G5kCfg.PREFERREDACCESPOINT) + "\" />" +
        newline;
        xml += nSpaces(2) +
        "<username id=\"" + get(G5kCfg.USERNAME) + "\" />" +
        newline;
        // sshkey
        xml += nSpaces(2) +
        "<sshkey file=\"" + get(G5kCfg.SSHKEYFILE) + "\" />" +
        newline;
        // javaHome
        xml += nSpaces(2) +
        "<javaHome path=\"" + get(G5kCfg.JAVAHOME) + "\" />" +
        newline;
        // godiet scratch
        xml += nSpaces(2) +
        "<goDietScratch dir=\"" +
        get(G5kCfg.GODIETSCRATCH) + "\" />" +
        newline;
        // godiet  debug level, useUniqueDirs, saveStdOut, saveStdErr and loggingInGoDIET
        xml += nSpaces(2) +
        "<goDiet debug=\"" +
        get(G5kCfg.GODIETDEBUG) + "\" " +
        "useUniqueDirs=\"" +
        get(G5kCfg.GODIETUSEUNIQUEDIRS) + "\" " +
        "saveStdOut=\"" +
        get(G5kCfg.GODIETSAVESTDOUT) + "\" " +
        "saveStdErr=\"" +
        get(G5kCfg.GODIETSAVESTDERR) + "\" " +
        "loggingInGoDIET=\"" +
        get(G5kCfg.LOGGINGINGODIET)  + "\" " +
        " />" +
        newline;
        xml += nSpaces(2) +
        "<applicationProperties showTOTD=\"" + get(G5kCfg.SHOWTOTDONSTARTUP) + "\" />" + newline;
        xml += newline + "<!-- G5K Sites -->" + newline;
        // g5k sites
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
            SiteCfg siteCfg = getSite(ix);
            //	  TODO passer a un tableau de taille le nombre de sites
            String siteXml = nSpaces(2) + "<site id=\"" + G5kConstants.sites.get(ix).getName() + "\">" + newline;
            if (siteCfg != null) {
                siteXml += nSpaces(4) + "<"+SiteCfg.SCRATCH_RUNTIME+" dir=\"" + siteCfg.get(SiteCfg.SCRATCH_RUNTIME) + "\" />" + newline;
                Iterator<String> iter = siteCfg.getKeySet().iterator();
                while(iter.hasNext()){
                	String key = iter.next();
                	String value = siteCfg.get(key);
                	if(!key.equalsIgnoreCase(SiteCfg.SCRATCH_RUNTIME)) siteXml += nSpaces(4) + "<variable key=\"" + key + "\" value=\"" + value +"\""  +"/>" + newline;
                }
            }
            else {
                siteXml += nSpaces(4) + "<"+SiteCfg.SCRATCH_RUNTIME+" dir=\"" + "\" />" + newline;
                siteXml += nSpaces(4) + "<variable key=\"PATH\" value=\"\" />" + newline;
                siteXml += nSpaces(4) + "<variable key=\"LD_LIBRARY_PATH\" value=\"\"/>" + newline;
            }

            siteXml += nSpaces(2) + "</site>" + newline;
            xml += siteXml;
        }


        xml += "</g5k>" + newline;
        return xml;
    }

    /**
     * Method saving the G5k configuration to a file
     *
     * @return boolean value telling if the configuration as been
     * successfully saved
     */
    public static boolean save() {
        // Create the XML file
        String homeDir = System.getProperty("user.home");
        File cfgFile = new File(homeDir   + "/.diet/g5k.xml");
        try {
            FileWriter writer = new FileWriter(cfgFile);
            writer.write(toXML());
            writer.close();
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Method returning n spaces
     *
     * @param n number of spaces w want
     *
     * @return a String containing n spaces
     */
    public static String nSpaces(int n) {
        String spaces = "";
        for (int i=0; i<n; i++)
            spaces += " ";
        return spaces;
    }

    /**
     * Static String corresponding to a line separator for the system were the application has been launched
     */
    public static String newline = System.getProperty("line.separator");
    
    /**
     * Method setting the SSH key passphrase
     * 
     * @param sshKey the SSH key passphrase
     */
    public static void setSSHKey(String sshKey){
    	SSHAgent sshAgent =  SSHAgent.getInstance();
    	sshAgent.setSshKey(sshKey);
    }
    
    /**
     * method returning the SSH passphrase
     * 
     * @return
     */
    public static String getSSHKey(){
    	SSHAgent sshAgent =  SSHAgent.getInstance();
    	return SSHAgent.getInstance().getSshKey();
    }
}
