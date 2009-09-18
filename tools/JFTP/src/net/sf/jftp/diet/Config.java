/****************************************************************************/
/* Grid5000 configuration class                                             */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: Config.java,v 1.2 2007/11/06 12:15:47 dloureir Exp $
 * $Log: Config.java,v $
 * Revision 1.2  2007/11/06 12:15:47  dloureir
 * New versions of configuration files
 *
 * Revision 1.15  2007/09/28 16:03:19  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.14  2007/07/12 15:27:59  dloureir
 * Some code clean up and typo correction
 *
 * Revision 1.13  2007/06/26 15:01:18  dloureir
 * Removing defualt value for the login
 *
 * Revision 1.12  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.11  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.10  2007/02/27 14:10:07  dloureir
 * Adding sol cluster of sophia and removing some print outs
 *
 * Revision 1.9  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.8  2007/02/22 14:05:57  aamar
 * Changing the configuration parameters (adding kadeploy params, merging
 * cluster of one site, etc.)
 *
 ****************************************************************************/
package net.sf.jftp.diet;

import java.io.*;
import java.util.logging.Level;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.xpath.*;

/**
 * Grid5000 configuration class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */

public class Config {
    /**
     * The user identifier (login).
     */
    private static String user = null;

    /**
     * Grid5000 enabled sites.
     */
    private static boolean [] enabledSites = new boolean[G5kSite.getSitesNumber()];

    /**
     * Clusters deployment partition.
     */
    private static String [] kadeployPartitions = new String[G5kCluster.getClustersNumber()];

    /**
     * Clusters batch schedulers.
     */
    private static String [] batchSchedulers = new String[G5kSite.getSitesNumber()];

    /**
     * Available batch schedulers
     */
    private static String [] availableBatchSchedulers = {"OAR1", "OAR2", "LoadLeveler", "Not found"};

    /**
     * Grid5000 configuration filename.
     */
    private static String G5K_CFG = "g5k_cfg.xml";

    /**
     * New line
     */
    public static String newline = System.getProperty("line.separator");

    /**
     * Initialize the configuration by reading the configuration file.
     */
    public static void init() {
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
            enabledSites[ix] = false;
            batchSchedulers[ix] = "";
        }
        for (int ix=0; ix<G5kCluster.getClustersNumber(); ix++) {
            kadeployPartitions[ix] = "";
        }
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
     * Read the configuration file.
     */
    protected static void readCfgFile() {
        String cfgFileName = System.getProperty("user.home") + "/.diet/" + G5K_CFG;
        File cfgFile = new File(cfgFileName);


        if (!cfgFile.exists()) {
            try {
                cfgFile.createNewFile();
                save(cfgFile);
            }
            catch (IOException e) {
                return;
            }
        }
        getProperties(cfgFile);
   }

    /**
     * Read the XML properties of configuration file.
     *
     * @param cfgFile the configuration file
     */
    protected static void getProperties(File cfgFile) {
        Document doc = getDoc(cfgFile);
        try {
            XPathFactory xpfactory = XPathFactory.newInstance();
            XPath xpath = xpfactory.newXPath();

            // get g5k clusters
            int count = ((Number) xpath.evaluate("count(/g5k/site)",
                    doc, XPathConstants.NUMBER)).intValue();
            for (int ix=0; ix < count; ix++) {
                Node node = (Node) xpath.evaluate("/g5k/site[" + (ix+1) +"]", doc, XPathConstants.NODE);
                String siteId   = xpath.evaluate("@id", node);
                String enabled     = xpath.evaluate("@enable", node);
                String batchs      = xpath.evaluate("@batch_schedulers", node);
                int siteIndex = G5kSite.getIndexForSite(siteId);

                if (siteIndex != -1) {
                    enabledSites[siteIndex] = Boolean.valueOf(enabled).booleanValue();
                    batchSchedulers[siteIndex] = batchs;
                    int clusterCount = ((Number) xpath.evaluate("count(./cluster)",
                            node, XPathConstants.NUMBER)).intValue();
                    for (int cx=0; cx<clusterCount; cx++) {
                        Node clusterNode = (Node) xpath.evaluate("./cluster[" + (cx+1) +"]", 
                                node, 
                                XPathConstants.NODE);
                        String clusterName = xpath.evaluate("@name", clusterNode);
                        String kadeployPartition = xpath.evaluate("@xda", 
                                clusterNode);
                        int clusterIdx = G5kCluster.getIndexForCluster(clusterName);
                        if (clusterIdx != -1) {
                            kadeployPartitions[clusterIdx] = kadeployPartition;
                        }

                    }
                }
            }
        }
        catch (javax.xml.xpath.XPathExpressionException e) {
        }
    }

    /**
     * Returns if the cluster identified by its index is enabled
     *
     * @param index the cluster index
     * @return the state of cluster (enabled or not).
     */
    public static boolean isSiteEnable(int index) {
        return enabledSites[index];
    }

    /**
     * Set the user login.
     *
     * @param u    the user login
     */
    public static void setUser(String u) {
        user = u;
    }

    /**
     * Returns the user login
     *
     * @return Return the user login.
     */
    public static String getUser() {
        return user;
    }

    /**
     * Enable/disable a cluster.
     *
     * @param site the site index
     * @param enable  the cluster enable status
     */
    public static void setEnable(int site, boolean enable) {
        if (site < 0 || site >= G5kSite.getSitesNumber()) {
            String message = "The site " + site + " cannot be ";
            if (enable)
                message+="enabled";
            else
                message+="disabled";
            return;
        }
        enabledSites[site] = enable;
        String temp = enable?"enabled":"disable";
    }

    /**
     * Get cluster kaDeploy partition.
     *
     * @param cluster the cluster index
     * @return  the partition name
     */
    public static String getXDA(int cluster) {
        if (cluster < 0 || cluster >= G5kCluster.getClustersNumber()) {
            System.err.print("The cluster " + cluster + " was not found ");
            return null;
        }
        return kadeployPartitions[cluster];
    }

    /**
     * Set cluster kaDeploy partition.
     *
     * @param cluster the cluster index
     * @param xda     the partition name
     */
    public static void setXDA(int cluster, String xda) {
        if (cluster < 0 || cluster >= G5kCluster.getClustersNumber()) {
        }
        kadeployPartitions[cluster] = new String(xda);
    }


    /**
     * Get cluster batch schedulers.
     *
     * @param cluster the cluster index
     * @return  the batch schedulers names
     */
    public static String getBatchScheduler(int site) {
        final String defaultBatch = "None";
        if (site < 0 || site >= G5kSite.getSitesNumber() ||
                batchSchedulers[site] == null || batchSchedulers[site].equals("") ) {
            return defaultBatch;
        }
        return batchSchedulers[site];
    }

    /**
     * Set cluster batch schedulers.
     *
     * @param site the site index
     * @param batchs  the site batch schedulers names
     */
    public static void setBatchScheduler(int site, String batchs) {
        if (site < 0 || site >= G5kSite.getSitesNumber() ) {
        }
        batchSchedulers[site] = batchs;
    } // end setBatchScheduler

    /**
     * Get the available Batch schedulers
     *
     * @return the string array representing the available batch schedulers
     */
    public static String[] getAvailableBatchSchedulers() {
        return availableBatchSchedulers;
    }

    /**
     * Get the scheduler index if found in the available batch schedulers.
     *
     * @param batchName batch scheduler name
     * @return the batch scheduler index or (availableBatchSchedulers.length-1)
     */
    public static int getBatchSchedulerIndex(String batchName) {
        for (int ix=0; ix<availableBatchSchedulers.length; ix++) {
            if (availableBatchSchedulers[ix].equals(batchName))
                return ix;
        } // end for
        return availableBatchSchedulers.length-1;
    }

    /**
     * Get the scheduler name if found in the available batch schedulers.
     *
     * @param index batch scheduler index
     * @return the batch scheduler name or ("Not Found")
     */
    public static String getBatchSchedulerName(int index) {
        if (index < 0 || index >= availableBatchSchedulers.length)
            return availableBatchSchedulers[availableBatchSchedulers.length -1];
        return availableBatchSchedulers[index];
    }

    /**
     * Returns an XML document of a file
     *
     * @param file the file to read
     * @return An XML document of the specified file
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
    } // end getDoc

    /**
     * Save the configuration file.
     *
     * @param cfgFile the configuration file
     */
    protected static void save(File cfgFile) {
        try {
            FileWriter writer = new FileWriter(cfgFile);
            writer.write("<g5k>" + newline);
            for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
                writer.write("\t<site " + newline +
                        "\t\t id=\"" + G5kConstants.sites.get(ix).getName() + "\" " + newline +
                        "\t\t enable=\"" +  enabledSites[ix] +"\" " + newline +
                        "\t\t batch_schedulers=\"" + batchSchedulers[ix] + "\" " + newline +
                        "\t >" + newline);
                int [] clusterIdx = G5kSite.getClustersIndexesForIndex(ix);
                for (int cx=0; cx < clusterIdx.length; cx++) {
                    writer.write("\t\t<cluster " +
                            "name=\"" + G5kCluster.getNameForIndex(clusterIdx[cx]) + "\" " +
                            " xda=\"" + kadeployPartitions[clusterIdx[cx]] +"\" /> " + newline);
                }
                writer.write("\t</site>" + newline);
            }
            writer.write("</g5k>" + newline);
            writer.close();
        }
        catch (IOException e) {
            return ;
        }
    }


    /**
     * Save the configuration file.
     */
    public static void save() {
        String cfgFileName = System.getProperty("user.home") + "/.diet/" + G5K_CFG;
        File cfgFile = new File(cfgFileName);

        if (!cfgFile.exists()) {
            try {
                cfgFile.createNewFile();
            }
            catch (IOException e) {
                return;
            }
        } // end if not exist
        save(cfgFile);
    }
    
    /**
     * Get an array of kadeploy partitions
     */
    public static String [] getXDAs() {
        String [] partitions = new String[kadeployPartitions.length];
        for (int ix = 0; ix < kadeployPartitions.length; ix++)
            partitions[ix] = new String(kadeployPartitions[ix]);
        return partitions;
    }
}