/****************************************************************************/
/* Progression of the deployment on a host                                  */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: HostProgression.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: HostProgression.java,v $
 * Revision 1.5  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.3  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.2  2007/02/22 15:14:18  aamar
 * Correct header.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui.kadeploy;

import java.util.logging.Level;

import diet.logging.LoggingManager;

/**
 * Progression of the deployment on a host
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class HostProgression {
    /**
     * name of the host
     */
    private String hostName;
    /**
     * cluster of the host
     */
    private int myCluster;
    /**
     * value of the progression
     */
    private int progression;
    /**
     * boolean value telling that the deployment is halted
     */
    private boolean halt;
    /**
     * boolean value telling that the deployment is done
     */
    private boolean done;
    /**
     * Default constructor
     *
     * @param host host name
     * @param cluster cluster
     * @param p value of the progression
     * @param halt boolean value telling that the deployment is halted or not
     */
    public HostProgression(String host, int cluster, int p, boolean halt) {
        this.hostName = host;
        this.myCluster = cluster;
        this.progression = p;
        this.halt = halt;
        this.done = false;
        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "HostProgression", "HostProgression created");
    }

    /**
     * Second constructor
     *
     * @param host host of the progression
     * @param cluster cluster of the host
     */
    public HostProgression(String host, int cluster) {
        this(host, cluster, 0, false);
    }

    /**
     * Method returning the host
     *
     * @return host for the progression
     */
    public String getHost() {
        return this.hostName;
    }

    /**
     * Method setting the host
     *
     * @param host host of the progression
     */
    public void setHost(String host) {
        this.hostName = host;
    }

    /**
     * Method returning the cluster of the deployment
     *
     * @return cluster of the host
     */
    public int getCluster() {
        return this.myCluster;
    }

    /**
     * Method returning the progression of the deployment
     *
     * @return progression of the deployment
     */
    public int getProgression() {
        return this.progression;
    }

    /**
     * Method setting the progression of the host
     *
     * @param p value of the progression
     */
    public void setProgression(int p) {
        this.progression = p;
    }

    /**
     * Method telling that the progression is halted of not
     *
     * @return halt state of the progression
     */
    public boolean isHalt() {
        return this.halt;
    }

    /**
     * Method setting the halt state of the progression
     *
     * @param halt halt state of the progression
     */
    public void setHalt(boolean halt) {
        this.halt = halt;
    }

    /**
     * Method telling that the progression is done or not
     *
     * @return boolean value telling if the progression is done or not
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Method setting the progression as done
     *
     */
    public void setAsDone() {
        this.done = true;
    }
}
