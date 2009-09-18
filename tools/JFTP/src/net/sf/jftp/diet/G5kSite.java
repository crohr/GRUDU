/****************************************************************************/
/* This class corresponds to the Grid5000 Site                              */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kSite.java,v 1.2 2007/11/06 12:15:47 dloureir Exp $
 * $Log: G5kSite.java,v $
 * Revision 1.2  2007/11/06 12:15:47  dloureir
 * New versions of configuration files
 *
 * Revision 1.11  2007/10/05 13:14:14  dloureir
 * Changing the names of the internal frontales to frontend.* (new naming convention)
 *
 * Revision 1.10  2007/09/28 16:03:19  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.9  2007/07/12 15:38:22  dloureir
 * Some typo corrections
 *
 * Revision 1.8  2007/03/16 13:32:21  dloureir
 * Adding paraquad cluster
 *
 * Revision 1.7  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package net.sf.jftp.diet;

import java.util.ArrayList;


/**
 * This class corresponds to the Grid5000 Site
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kSite {

    /**
     * Array containing the externals frontales of the sites
     */
    private static final String[] externalFrontals = {"grid5000.ens-lyon.fr",//"acces.lyon.grid5000.fr",
                                                      "acces.sophia.grid5000.fr",
                                                      "acces.rennes.grid5000.fr",
                                                      "acces.orsay.grid5000.fr",
                                                      "acces.bordeaux.grid5000.fr",
                                                      "acces.toulouse.grid5000.fr",
                                                      "acces.lille.grid5000.fr",
                                                      "acces.nancy.grid5000.fr",
                                                      "acces.grenoble.grid5000.fr"};

    /**
     * Array containing the internal frontales of the sites
     */
    private static final String[] internalFrontals = {"frontend.lyon.grid5000.fr",
                                                      "frontend.sophia.grid5000.fr",
                                                      "frontend.rennes.grid5000.fr",
                                                      "frontend.orsay.grid5000.fr",
                                                      "frontend.bordeaux.grid5000.fr",
                                                      "frontend.toulouse.grid5000.fr",
                                                      "frontend.lille.grid5000.fr",
                                                      "frontend.nancy.grid5000.fr",
                                                      "frontend.grenoble.grid5000.fr",};

    /**
     * Array containing the names of the sites
     */
    private static final String[] siteNames = {"Lyon",
                                               "Sophia",
                                               "Rennes",
                                               "Orsay",
                                               "Bordeaux",
                                               "Toulouse",
                                               "Lille",
                                               "Nancy",
                                               "Grenoble"
    };

    /**
     * Index of the Lyon site
     */
    public final static int siteLyon     = 0;
    /**
     * Index of the the Sophia site
     */
    public final static int siteSophia   = 1;
    /**
     * Index of the Rennes site
     */
    public final static int siteRennes   = 2;
    /**
     * Index of the Orsay site
     */
    public final static int siteOrsay    = 3;
    /**
     * Index of the Bordeaux site
     */
    public final static int siteBordeaux = 4;
    /**
     * Index of the toulouse site
     */
    public final static int siteToulouse = 5;
    /**
     * Index of Lille site
     */
    public final static int siteLille    = 6;
    /**
     * Index of the Nancy site
     */
    public final static int siteNancy    = 7;
    /**
     * Index of the Grenoble site
     */
    public final static int siteGrenoble = 8;

    /**
     * Array containing the clusters indexes for each site
     *
     *{
    	0,0,
    	1,1,1,
    	2,2,2,2,
    	3,3,
    	4,4,4,
    	5,
    	6,6,6,
    	7,7,
    	8,8
    	};
     *
     */
    private final static int[][] sitesClusters = {{0,1},
                                                  {2,3,4},
                                                  {5,6,7,8},
                                                  {9,10},
                                                  {11,12,13},
                                                  {14},
                                                  {15,16,17},
                                                  {18,19},
                                                  {20,21}};

    /**
     * Name of the site
     */
    private String name = null;
    /**
     * ArrayList containing the list of clusters of the site
     */
    ArrayList<G5kCluster> clusterList = null;
    /**
     * external frontale of the site
     */
    String externalFrontal = null;
    /**
     * internal frontale of the cluster
     */
    String internalFrontal = null;

    /**
     * Default constructor of the site
     *
     * @param siteName name of the site
     * @param externalFrontal external fontale of the site
     * @param internalFrontal internal frontale of the site
     */
    public G5kSite(String siteName, String externalFrontal, String internalFrontal){
        name = siteName;
        this.externalFrontal = externalFrontal;
        this.internalFrontal = internalFrontal;
        clusterList = new ArrayList<G5kCluster>();
        G5kCluster[] temp = getClustersForSite(name);
        for(int i = 0 ; i < temp.length; i ++){
            clusterList.add(temp[i]);
        }
    }

    /**
     * Second constructor of the site
     *
     * @param index index of a site
     */
    public G5kSite(int index){
        name = getSiteForIndex(index);
        externalFrontal = getExternalFrontalForIndex(index);
        internalFrontal = getInternalFrontalForIndex(index);
        clusterList = new ArrayList<G5kCluster>();
        G5kCluster[] temp = getClustersForSite(name);
        for(int i = 0 ; i < temp.length; i ++){
            clusterList.add(temp[i]);
        }
    }

    /**
     * Method returning the list of clusters of the site
     *
     * @return the clusterList the list of clusters of the site
     */
    public ArrayList<G5kCluster> getClusterList() {
        return clusterList;
    }
    /**
     * Method setting the list of clusters of that site
     *
     * @param clusterList the clusterList to set
     */
    public void setClusterList(ArrayList<G5kCluster> clusterList) {
        this.clusterList = clusterList;
    }
    /**
     * Method returning the external frontale of the site
     *
     * @return the externalFrontal the external frontale of the site
     */
    public String getExternalFrontal() {
        return externalFrontal;
    }
    /**
     * Method setting the external frontale of the site
     *
     * @param externalFrontal the externalFrontal to set
     */
    public void setExternalFrontal(String externalFrontal) {
        this.externalFrontal = externalFrontal;
    }
    /**
     * Method returning the internal frontale of the site
     *
     * @return the internalFrontal the internal frontale of the site
     */
    public String getInternalFrontal() {
        return internalFrontal;
    }
    /**
     * Method setting the internal frontale of the site
     *
     * @param internalFrontal the internalFrontal to set
     */
    public void setInternalFrontal(String internalFrontal) {
        this.internalFrontal = internalFrontal;
    }
    /**
     * Method returning the name of the site
     *
     * @return the name The name of the site
     */
    public String getName() {
        return name;
    }
    /**
     * Method setting the name of the site
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method returning an array of cluster indexes of the site from an index
     *
     * @param index index of a site
     * @return an array of cluster indexes of a site
     */
    public static int[] getClustersIndexesForIndex(int index){
        if(index < 0 || index > (sitesClusters.length-1)) return null;
        return sitesClusters[index];
    }

    /**
     * Method returning an array of cluster indexes from a site name
     *
     * @param aName a name of a site
     * @return an array containing the indexes of the clusters of the site
     */
    public static int[] getClustersIndexesForSite(String aName){
        int index = getIndexForSite(aName);
        if(index == -1) return null;
        return sitesClusters[index];
    }

    /**
     * the index of the site given in argument
     *
     * @param name the name of a site
     * @return the index of the site
     */
    public static int getIndexForSite(String name){
        for (int ix=0; ix< siteNames.length; ix++) {
            if (name.equals(siteNames[ix]))
                return ix;
        }
        return -1;
    }

    /**
     * Method returning the site name corresponding to an index
     *
     * @param index index of a site
     * @return the name of the corresponding site
     */
    public static String getSiteForIndex(int index){
        if(index < 0 || index > (siteNames.length-1)) return null;
        return siteNames[index];
    }

    /**
     * Method returning an array of G5kCluster of this site from the
     * site name
     *
     * @param name name of the site
     * @return Array of G5kCluster of the site
     */
    private G5kCluster[] getClustersForSite(String name){
        int index = getIndexForSite(name);
        if(index == -1) return null;
        int[] clusters = getClustersIndexesForIndex(index);
        G5kCluster[] result = new G5kCluster[clusters.length];
        for(int i = 0 ; i< clusters.length ; i ++){
            result[i] = new G5kCluster(clusters[i]);
        }
        return result;
    }

    /**
     * Method returning an array of G5kCluster of this site index
     *
     * @param index index of a site
     * @return array of G5kClusters of this site
     */
    private G5kCluster[] getClustersForSite(int index){
        if(index <0 || index > (siteNames.length-1)) return null;
        int[] clusters = getClustersIndexesForIndex(index);
        G5kCluster[] result = new G5kCluster[clusters.length];
        for(int i = 0 ; i< clusters.length ; i ++){
            result[i] = new G5kCluster(clusters[i]);
        }
        return result;
    }

    /**
     * Method returning the external frontale of a site from its name
     *
     * @param name name of a site
     * @return the external frontale of the site
     */
    public String getExternalFrontalForSite(String name){
        int index = getIndexForSite(name);
        if(index == -1) return null;
        return externalFrontals[index];
    }

    /**
     * Method returning the external frontale of a site from its index
     *
     * @param index index of a site
     * @return the external frontale of a site
     */
    public String getExternalFrontalForIndex(int index){
        if(index < 0 || index > (externalFrontals.length-1)) return null;
        return externalFrontals[index];
    }

    /**
     * Method returning the internal frontale of a site from its name
     *
     * @param name name of a site
     * @return internal frontale of this site
     */
    public static String getInternalFrontalForSite(String name){
        int index = getIndexForSite(name);
        if(index == -1) return null;
        return internalFrontals[index];
    }

    /**
     * Method returning the internal frontale of a site from its index
     *
     * @param index index of a site
     * @return internal frontale of the site
     */
    public static String getInternalFrontalForIndex(int index){
        if(index < 0 || index > (internalFrontals.length-1)) return null;
        return internalFrontals[index];
    }

    /**
     * Method returning the number of sites
     *
     * @return number of sites
     */
    public static int getSitesNumber(){
        return siteNames.length;
    }

    /**
     * Method returning the number of clusters of this site
     *
     * @return the number of clusters
     */
    public int getClustersNumber(){
        return sitesClusters[getIndexForSite(name)].length;
    }

    /**
     * Method returning the number of clusters of a site from its
     * name
     *
     * @param aName a name of a site
     * @return the number of clusters of this site
     */
    public static int getClustersNumberForSite(String aName){
        return sitesClusters[getIndexForSite(aName)].length;
    }

    /**
     * Method returning the number of clusters for a site from
     * its index
     *
     * @param index index of a site
     * @return the number of clusters of a site
     */
    public static int getClustersNumberForIndex(int index){
        return sitesClusters[index].length;
    }

    /**
     * Method returning an array corresponding to the external frontales
     *
     * @return the externalFrontals an array containing the external frontales
     * of the Grid5000 sites
     */
    public static String[] getExternalFrontals() {
        return externalFrontals;
    }

    /**
     * Method returning an array of internal frontales of
     * the grid5000 sites
     *
     * @return the internalFrontals an array of the internal frontales
     * of the sites
     */
    public static String[] getInternalFrontals() {
        return internalFrontals;
    }

    /**
     * Method returning an array of the names of the grid5000 sites
     *
     * @return the siteNames an array of names of the grid 5000 sites
     */
    public static String[] getSiteNames() {
        return siteNames;
    }

    /**
     * Method returning a G5kCluster of the site from its index
     *
     * @param index
     * @return the cluster object for this cluster index
     */
    public G5kCluster getCluster(int index){
        if(index < 0 || index > clusterList.size()) return null;
        return clusterList.get(index);
    }

    public static int getResCount(int siteIdx) {
        if (siteIdx >=0 && siteIdx < sitesClusters.length) {
            int resCount = 0;
            for (int clusterIdx : sitesClusters[siteIdx])
                resCount += G5kCluster.getCapacityForIndex(clusterIdx);
            return resCount;
        }
        return 0;
    }
}
