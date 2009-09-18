/****************************************************************************/
/* This class run the oarstat command for all cluster of Grid5000           */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarStatRunnable.java,v 1.15 2007/11/19 15:16:19 dloureir Exp $
 * $Log: OarStatRunnable.java,v $
 * Revision 1.15  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.14  2007/11/06 13:12:43  dloureir
 * Adding a currentAnimator for settings message during information retrieving
 *
 * Revision 1.13  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.12  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.11  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.10  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.util.*;
import java.util.logging.Level;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;


import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

import diet.grid.api.GridJob;
import diet.grid.BatchSchedulerMgr;
import diet.grid.api.util.DefaultBatchSchedulerListener;

/**
 * This class run the oarstat command for all cluster of Grid5000
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class OarStatRunnable implements Runnable {
    /**
     * parent frame
     */
	G5kRes myParent;
    /**
     * SSH connection
     */
	Connection myConn;
    /**
     * Default constructor
     *
     * @param parent parent frame
     * @param conn SSH connection
     */
	public OarStatRunnable(G5kRes parent,
			Connection conn) {
		this.myParent = parent;
		this.myConn   = conn;
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "OarStatRunnable", "OarStatRunnable initialized");
	}

    /**
     * Method that runs the oarstat for all clusters
     */
	public void run() {
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "OarStat launched");
			ArrayList<Thread> threadList = new ArrayList<Thread>();
			//ArrayList<String> lines = new ArrayList<String>();
			for (int ix=0 ; ix < G5kSite.getSitesNumber(); ix++) {
				final int anIndex = ix;
				Thread th = new Thread(new Runnable(){
					public void run(){
						boolean isStatDone = false;
						while(!isStatDone){
							try{
								if (Config.isSiteEnable(anIndex)) {
									//Session sess = myConn.openSession();
									LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"run","Getting statistics for site : " + G5kSite.getSiteForIndex(anIndex) + " ...");
									myParent.setMessageToCurrentAnimator("Getting statistics for site : " + G5kSite.getSiteForIndex(anIndex) + " ... ");
									/*
					lines = OarUtil.oarstat(this.myConn,
							G5kCluster.getOarFrontalForIndex(ix));

					Map<String, GridJob> jobs = OarUtil.getJobs(lines);
									 */
									GridJob [] tab = BatchSchedulerMgr.getJobs(
											Config.getBatchScheduler(anIndex), 
											myConn, 
											G5kSite.getInternalFrontalForIndex(anIndex), 
											new DefaultBatchSchedulerListener());
									Map<String, GridJob> jobs = new HashMap<String, GridJob>();
									for (int jx=0; jx<tab.length; jx++)
										jobs.put(tab[jx].getParameterValue(GridJob.KEY_GRID_JOB_ID), tab[jx]);
									if (anIndex == (G5kSite.getSitesNumber() - 1))
										myParent.setJobs(jobs, anIndex, true);
									else
										myParent.setJobs(jobs, anIndex, false);
									
									//sess.close();
								} // end if enabled
								isStatDone = true;
							}catch(Exception e){
								try{Thread.sleep(1000);}catch(Exception ex){}
							}
						}
						LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"run","Getting statistics for site : " + G5kSite.getSiteForIndex(anIndex) + " ... done");
					}
				},G5kSite.getSiteForIndex(anIndex) + "StatThread");
				threadList.add(th);
			} // end for
			Iterator<Thread> iter = threadList.iterator();
			while(iter.hasNext()){
				Thread th = iter.next();
				th.start();
			}
			iter = threadList.iterator();
			while(iter.hasNext()){
				Thread th = iter.next();
				try{th.join();}catch(Exception e){
					LoggingManager.log(Level.WARNING,LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);	
				}
			}

			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "OarStat ended");
			//this.myParent.setJobs(null, -1, true);
	}
}