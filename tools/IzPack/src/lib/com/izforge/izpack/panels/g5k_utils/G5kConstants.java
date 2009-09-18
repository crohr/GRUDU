/****************************************************************************/
/* Grid 5000 information                                                    */
/*                                                                          */
/* Author(s):                                                               */
/*  - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                         */
/*  - David LOUREIRO (David.Loureiro@ens-lyon.fr)                           */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kConstants.java,v 1.4 2007/10/30 10:25:23 dloureir Exp $
 * $Log: G5kConstants.java,v $
 * Revision 1.4  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.8  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.7  2007/02/22 14:10:02  aamar
 * Some changes.
 *
 * Revision 1.6  2007/02/09 14:15:28  dloureir
 * Adding log system
 *
 * Revision 1.5  2007/01/16 09:59:44  dloureir
 * Adding G5kSite and G5kCluster for the management of sites and clusters
 *
 * Revision 1.4  2007/01/12 13:28:58  dloureir
 * Adding all clusters and related methods
 *
 * Revision 1.3  2007/01/10 11:52:46  dloureir
 * Correcting oar hosts
 *
 * Revision 1.2  2007/01/03 15:31:54  dloureir
 * Adding grenoble to the sites
 *
 * Revision 1.1.1.1  2006/12/04 17:21:44  aamar
 * Initial sources
 *
 ****************************************************************************/

package com.izforge.izpack.panels.g5k_utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Grid 5000 information
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */

public class G5kConstants {
    /**
     * ArrayList containing the sites of Grid5000
     */
    public static final ArrayList<G5kSite> sites = initSites();
    /**
     * ArrayList containing the clusters of Grid5000
     */
    public static final ArrayList<G5kCluster> clusters = initClusters();

    /**
     * Method initializing the ArrayList of Grid5000 sites
     *
     * @return an ArrayList containing the sites of Grid 5000
     */
    private static ArrayList<G5kSite> initSites(){
    	ArrayList<G5kSite> result = new ArrayList<G5kSite>();
    	for(int i = 0 ; i < G5kSite.getSitesNumber() ; i ++){
            result.add(new G5kSite(i));
    	}
    	return result;
    }

    /**
     * Method initializing the ArrayList of Grid5000 clusters
     *
     * @return an ArrayList containing the clusters of Grid 5000
     */
    private static ArrayList<G5kCluster> initClusters(){
    	ArrayList<G5kCluster> result = new ArrayList<G5kCluster>();
    	Iterator<G5kSite> iter = sites.iterator();
    	while(iter.hasNext()){
            G5kSite temp = iter.next();
            int[] tempIndexes = G5kSite.getClustersIndexesForSite(temp.getName());
            for(int i= 0 ; i < temp.getClustersNumber(); i ++){
                G5kCluster tempCluster = new G5kCluster(tempIndexes[i]);
                result.add(tempCluster);
            }
    	}
    	return result;
    }

}
