/****************************************************************************/
/* Deployment progression utility class                                     */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DeploymentProgression.java,v 1.5 2007/07/12 15:26:34 dloureir Exp $
 * $Log: DeploymentProgression.java,v $
 * Revision 1.5  2007/07/12 15:26:34  dloureir
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

import java.util.Vector;
import java.util.logging.Level;

import diet.gridr.g5k.util.KadeployDeployment;
import diet.logging.LoggingManager;

/**
 * Deployment progression utility class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class DeploymentProgression extends KadeployDeployment {

    /**
     * progression value
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
     * @param name name of the deployment
     * @param hosts hosts for the deployment
     * @param cluster cluster where the deployment is happening
     * @param p value of the progression
     * @param halt boolean value telling that deployment is halted
     */
    public DeploymentProgression(String name, Vector<String> hosts, int cluster, int p, boolean halt) {
        super(name, cluster);
        for (int ix=0; ix<hosts.size(); ix++)
            addHost(hosts.get(ix));
        this.progression = p;
        this.halt = halt;
        this.done = false;
        LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "DeploymentProgression", "DeploymentProgression created");
    }

    /**
     * Second constructor
     *
     * @param name name of the deployment
     * @param hosts hosts for the deployment
     * @param cluster cluster where the deployment is happening
     */
    public DeploymentProgression(String name, Vector<String> hosts, int cluster) {
        this(name, hosts, cluster, 0, false);
    }

    /**
     * Construct a deployment with no host to deploy
     *
     * @param name the deployment name
     * @param cluster the cluster index
     */
    public DeploymentProgression(String name, int cluster) {
        this(name, new Vector<String>(), cluster);
    }

    /**
     * Method returning the progression of the deployment
     *
     * @return the progression of the deployment
     */
    public int getProgression() {
        return this.progression;
    }

    /**
     * Method setting the progression of the deployment
     *
     * @param p progression to set
     */
    public void setProgression(int p) {
        this.progression = p;
    }

    /**
     * Method telling if the deployment is halted
     *
     * @return value telling if the deployment is halted
     */
    public boolean isHalt() {
        return this.halt;
    }

    /**
     * Method setting the halt state
     *
     * @param halt halt state
     */
    public void setHalt(boolean halt) {
        this.halt = halt;
    }

    /**
     * Method telling if the deployment is done
     *
     * @return a boolean value telling if the deployment is done
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Method setting the deployment as done
     *
     */
    public void setAsDone() {
        this.done = true;
    }

    /**
     * Method telling that deploy is to be done
     *
     * @return boolean value telling that the deployment is to be done or not
     */
    public boolean isToDeploy() {
        return (this.myHosts.size() != 0);
    }
}
