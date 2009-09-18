/****************************************************************************/
/* KaDeploy deployment                                                      */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployDeployment.java,v 1.4 2007/07/12 15:42:12 dloureir Exp $
 * $Log: KadeployDeployment.java,v $
 * Revision 1.4  2007/07/12 15:42:12  dloureir
 * Some typo corrections.
 *
 * Revision 1.3  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.2  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.1  2007/02/22 14:12:51  aamar
 * Initial revision.
 *
 ****************************************************************************/

package diet.gridr.g5k.util;

import java.util.Vector;

/**
 * KaDeploy deployment
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class KadeployDeployment {
    /**
     * Grid5000 cluster index
     */
    protected int myCluster;

    /**
     * Host where deploy
     */
    protected Vector<String> myHosts;

    /**
     * The deployment name
     */
    private String myName;

    /**
     *  Create deployment for a cluster with the specified hosts
     *
     * @param name    the deployment name
     * @param cluster the deployment cluster index
     * @param hosts   the hosts to deploy
     */
    public KadeployDeployment(String name, int cluster, String [] hosts) {
        this(name, cluster);
        for (int ix=0; ix<hosts.length; ix++)
            this.myHosts.add(hosts[ix]);
    }

    /**
     *  Create an empty deployment for a cluster
     *
     * @param name    the deployment name
     * @param cluster the deployment cluster index
     */
    public KadeployDeployment(String name, int cluster) {
        this.myName = name;
        this.myCluster = cluster;
        this.myHosts   = new Vector<String>();
    }

    /**
     * Return the deployment name;
     *
     * @return the deployment name
     */
    public String getName() {
        return this.myName;
    }

    /**
     * Return the deployment cluster index
     *
     * @return the cluster index
     */
    public int getCluster() {
        return this.myCluster;
    }

    /**
     * Return the hosts to deploy
     *
     * @return the hosts to deploy
     */
    public String [] getHosts() {
        String [] hosts = new String[this.myHosts.size()];
        return this.myHosts.toArray(hosts);
    }

    /**
     * Add a host to the deployment
     *
     * @param host the host name to add
     */
    public void addHost(String host) {
        this.myHosts.add(host);
    }

    /**
     * Return the number of hosts to deploy
     *
     * @return the hosts to deploy number
     */
    public int getDeploySize() {
        return this.myHosts.size();
    }
}