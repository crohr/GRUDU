/****************************************************************************/
/* This class is used to launch the oarstat command on a cluster            */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarClusterStatRunnable.java,v 1.10 2007/11/19 15:16:19 dloureir Exp $
 * $Log: OarClusterStatRunnable.java,v $
 * Revision 1.10  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.9  2007/10/24 12:51:05  dloureir
 * Removing some calls to the G5KCluster class by the corresponding calls to the G5KSite class
 *
 * Revision 1.8  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.7  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.6  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import diet.gridr.g5k.util.Config;
import diet.gridr.g5k.util.G5kCluster;
import diet.gridr.g5k.util.G5kSite;
import diet.logging.LoggingManager;

import diet.grid.api.GridJob;
import diet.grid.BatchSchedulerMgr;
import diet.grid.api.util.DefaultBatchSchedulerListener;

/**
 * This class is used to launch the oarstat command on a cluster
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class OarClusterStatRunnable implements Runnable {
    /**
     * parent frame
     */
	G5kRes myParent;
    /**
     * the ssh connection
     */
	Connection myConn;
    /**
     * the cluster on which we are doing the oarstat
     */
	int aCluster;

    /**
     * Default constructor
     *
     * @param parent parent frame
     * @param conn ssh connection
     * @param aCluster the cluster on which we are doing the oarstat
     */
	public OarClusterStatRunnable(G5kRes parent,
			Connection conn, int aCluster) {
		this.myParent = parent;
		this.myConn   = conn;
		this.aCluster = aCluster;
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "OarClusterStatRunnable", "Oar Cluster Stat Runnable Initialized ");
	}

    /**
     * The method is used to launch the oarstat and then get then data for the cluster
     *
     */
	public void run() {
//		try {
			ArrayList<String> lines = new ArrayList<String>();

			//Session sess = this.myConn.openSession();
			if (Config.isSiteEnable(aCluster)) {
			    /*
				lines = OarUtil.oarstat(this.myConn,
						G5kCluster.getOarFrontalForIndex(aCluster));
				*/
				GridJob[] tab = BatchSchedulerMgr.getJobs(
				        Config.getBatchScheduler(aCluster), 
				        this.myConn, 
				        //G5kCluster.getOarFrontalForIndex(aCluster),
				        G5kSite.getInternalFrontalForIndex(aCluster),
				        new DefaultBatchSchedulerListener());
				
				// Map<String, GridJob> jobs = OarUtil.getJobs(lines);
				Map<String, GridJob> jobs = new HashMap<String, GridJob>();
				for (int ix=0; ix<tab.length; ix++)
				    jobs.put(tab[ix].getParameterValue(GridJob.KEY_GRID_JOB_ID), tab[ix]);
				
				//if (aCluster == (G5kCluster.getClustersNumber() - 1))
				if(aCluster == (G5kSite.getSitesNumber() - 1))
					this.myParent.setJobs(jobs, aCluster, true);
				else
					this.myParent.setJobs(jobs, aCluster, false);
			} // end if enabled
			//sess.close();
		//}
//		catch (IOException e) {
//			LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);
//		}
		this.myParent.setJobs(null, -1, true);
	}
}