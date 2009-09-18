/****************************************************************************/
/* This class corresponds to a node describing an OAR Job                   */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: JobTreeNode.java,v 1.8 2007/10/08 14:53:55 dloureir Exp $
 * $Log: JobTreeNode.java,v $
 * Revision 1.8  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.7  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.6  2007/07/12 14:42:16  dloureir
 * Some typo corrections
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;
import diet.grid.api.GridJob;;

/**
 * This class corresponds to a node describing an OAR Job
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class JobTreeNode extends IconNode {

    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = 1974665854321157794L;
    /**
     * The job
     */
	GridJob myJob;

    /**
     * Default constructor of a node describing an OAR Job
     *
     * @param userObj text for the node
     * @param allowChild parameter describing if the node should have children
     * @param icon icon for the node
     * @param job an OAR Job
     */
	public JobTreeNode(Object userObj, boolean allowChild, Icon icon,
						GridJob job) {
		super(userObj, allowChild, icon);
		this.myJob = job;
	}

    /**
     * Method returning an array of the hosts of a job
     *
     * @return an array of the hosts of a job
     */
	public String[] getHosts() {
		return this.myJob.getHostsAsArray();
	}

    /**
     * Method update the job associated to that node
     *
     * @param job the new job associated to that node
     */
	public void update(GridJob job) {
		this.myJob = job;
	}

    /**
     * Method returning the job associated to this node
     *
     * @return the job associated to that node
     */
	public GridJob getJob() {
		return this.myJob;
	}
}
