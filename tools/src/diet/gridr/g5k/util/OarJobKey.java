/****************************************************************************/
/* Oar job identifier (key) class                                           */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarJobKey.java,v 1.3 2007/05/22 08:09:51 aamar Exp $
 * $Log: OarJobKey.java,v $
 * Revision 1.3  2007/05/22 08:09:51  aamar
 * Correct header.
 *
 ****************************************************************************/

package diet.gridr.g5k.util;

/**
 * Oar job identifier (key) class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */

public class OarJobKey {
    /**
     * The cluster index.
     */
    private int cluster;

    /**
     * The job name
     */
    private String jobName;
    /**
     * The default constructor.
     *
     * @param cluster a cluster
     * @param jobName a name of a job
     */
    public OarJobKey(int cluster, String jobName) {
        this.cluster = cluster;
        this.jobName = jobName;
    }

    /**
     * Return the cluster index.
     *
     * @return The cluster index.
     */
    public int getCluster() {
        return this.cluster;
    }

    /**
     * Return the job name.
     *
     * @return The job name.
     */
    public String getJobName() {
        return this.jobName;
    }
}
