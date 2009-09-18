/****************************************************************************/
/* Kaenvironments command class helper                                      */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KaenvironmentHelper.java,v 1.3 2007/07/12 15:42:11 dloureir Exp $
 * $Log: KaenvironmentHelper.java,v $
 * Revision 1.3  2007/07/12 15:42:11  dloureir
 * Some typo corrections.
 *
 * Revision 1.2  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.1  2007/02/23 13:09:38  aamar
 * Initial revision.
 *
 ****************************************************************************/
package diet.gridr.g5k.util;

import java.util.*;

/**
 * Kaenvironments command class helper
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class KaenvironmentHelper {
    /**
     * Index of the cluster
     */
    private int myCluster;
    /**
     * Vector containing the KadeployEnv of the cluster
     */
    private Vector<KadeployEnv> myEnvs;
    /**
     * Vector containing the users corresponding to the environments of the cluster
     */
    private Vector<String>      myUsers;

    /**
     * Default constructor of the KaenvironmentHelper
     *
     * @param clusterIdx index of a cluster
     * @param envs environments of the cluster
     * @param users users of the cluster
     */
    public KaenvironmentHelper(int clusterIdx, Vector<KadeployEnv> envs, Vector<String> users) {
        this.myCluster = clusterIdx;
        this.myEnvs    = envs;
        this.myUsers   = users;
    } // end constructor

    /**
     * Method returning the vector of environments of the cluster
     *
     * @return a vector of environments for that cluster
     */
    public Vector<KadeployEnv> getEnvs() {
        return this.myEnvs;
    } // end getEnvs

    /**
     * Method returning the vector of the users corresponding to the environments of the cluster
     *
     * @return a vector of users
     */
    public Vector<String> getUsers() {
        return this.myUsers;
    }
}