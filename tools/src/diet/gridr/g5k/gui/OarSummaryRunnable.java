/****************************************************************************/
/* This class runs the oarnodes command to get informations on nodes status */
/* for all clusters of Grid5000                                             */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarSummaryRunnable.java,v 1.16 2007/11/23 14:39:07 dloureir Exp $
 * $Log: OarSummaryRunnable.java,v $
 * Revision 1.16  2007/11/23 14:39:07  dloureir
 * Correction of the recuperation of the nodes info
 *
 * Revision 1.15  2007/11/20 17:19:56  dloureir
 * Correction of two bugs :
 * - The variables are well retrieved when doing an oar nodes command for the whole grid
 * - The correction of the timezone in the reservation dialog frame led to a bug when reserving some nodes (there were no data anymore but integer), and it is fixed
 *
 * Revision 1.14  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.13  2007/11/07 11:35:01  dloureir
 * Some printlns are passed to the LoggingManager
 *
 * Revision 1.12  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.11  2007/07/12 14:49:39  dloureir
 * Some typo corrections
 *
 * Revision 1.10  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import com.trilead.ssh2.Connection;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

import diet.grid.BatchSchedulerMgr;
import diet.grid.api.util.DefaultBatchSchedulerListener;

/**
 * This class runs the oarnodes command to get informations on nodes status
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class OarSummaryRunnable implements Runnable {
    /**
     * SSH Connection
     */
	private Connection myConn;
    /**
     * parent object
     */
	private G5kRes myParent;
    /**
     * user data
     */
	private String [][] userData;
    /**
     * Reservation map
     */
	private HashMap<String,String> reservationMap = null;
	
	private Vector<int[]> data = null;

    /**
     * Default constructor
     *
     * @param parent parent object
     * @param conn ssh connection
     * @param _userData user data
     * @param reservationMap reservation Map
     */
	public OarSummaryRunnable(G5kRes parent, Connection conn, String [][] _userData, HashMap<String, String> reservationMap) {
		this.myParent = parent;
		this.myConn = conn;
		this.userData = _userData;
		this.reservationMap = reservationMap;
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "OarSummaryRunnable", "OarSummaryRunnable initialized");
	}

    /**
     * Methods used to run the oarnodes command on all clusters
     */
	public void run() {
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		data = new Vector<int[]>();
		for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
			data.add(new int[6]);
		}
		//int [][] data = new int[G5kSite.getSitesNumber()][6];
		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "oar nodes status started");
		for (int ix=0 ; ix < G5kSite.getSitesNumber(); ix++) {
			final int anIndex = ix;
			Thread th = new Thread(new Runnable(){
				public void run(){
					boolean isStatDone = false;
					while(!isStatDone){
						try{
							int[] dataArray = null;
							if (Config.isSiteEnable(anIndex)) {
								// data[ix] = OarUtil.getNodesStatus(this.myConn, G5kCluster.getOarFrontalForIndex(ix));
								myParent.setMessageToCurrentAnimator("oar nodes status on "+ G5kSite.getSiteForIndex(anIndex));
								LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "oar nodes status on "+ G5kSite.getSiteForIndex(anIndex) + "... " + anIndex);
								dataArray = BatchSchedulerMgr.getNodesStatus(
										Config.getBatchScheduler(anIndex), 
										myConn, G5kSite.getInternalFrontalForIndex(anIndex), 
										new DefaultBatchSchedulerListener());
								
							}
							else {
								dataArray = new int[6];
								for (int jx=0; jx < 6; jx++)dataArray[jx] = -1;
							} // end if enabled
							if(dataArray != null){
								if(allDataAreGood(dataArray)) isStatDone = true;
							}
							OarSummaryRunnable.this.data.set(anIndex, dataArray);
						}catch(Exception e){
							try{Thread.sleep(1000);}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
					LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(),"run","oar nodes status on : " + G5kSite.getSiteForIndex(anIndex) + " ... done");
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
		int[][] dataFinal = new int[G5kSite.getSitesNumber()][6];

		for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
			System.out.println("Oar nodes data treatment for site : " + G5kSite.getSiteForIndex(i) +" ... ");
			int[] dataArray = data.get(i);
			dataFinal[i] = dataArray;
		}
		LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "oar nodes status ended");
		myParent.setSummaryData(dataFinal, this.userData, this.reservationMap);
	}
	
	private boolean allDataAreGood(int[] dataArray){
		for(int i = 0 ; i < dataArray.length ; i++){
			if(dataArray[i] != 0) return true;
		}
		return false;
	}
}