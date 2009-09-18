/****************************************************************************/
/* This class runs the oarsub command                                       */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarOpRunnable.java,v 1.12 2007/11/19 15:16:19 dloureir Exp $
 * $Log: OarOpRunnable.java,v $
 * Revision 1.12  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.11  2007/10/05 13:53:03  aamar
 * Adding script parameter to submission.
 *
 * Revision 1.10  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.9  2007/07/12 14:48:56  dloureir
 * Javadoc for the getter/setter for the jobID
 *
 * Revision 1.8  2007/06/25 09:52:45  dloureir
 * Adding a feature : the user can now provide a scriptto launch when the reservation comes up (the path should be absolute)
 *
 * Revision 1.7  2007/06/22 11:53:50  dloureir
 * Corrections for bugs :
 *
 *  - bug 37 : It was about the wrong number of nodes on Nancy. In fact, it was a problem of properties when reserving some nodes. Now you have a extra panel in the reservation frame that allows you to specify some properties for each clusters.
 *    * G5KRes : Adding  the propertiesHashMap in the constructor call of the OAROpRunnable
 *    * OAROpRunnable : Adding an attribute (properties), and changing the constructor and the call to the oarsub method of the OARUtil class.
 *    * ResaDlg : Adding a propertiesPanel as an internal class. There is now two tabs (one for the basic and one for the properties of the reservation)
 *    * G5KReservation : Adding a propertiesHashMap and the associated getter/setter
 *    * OARUtil : Adding the properties when call the oarsub
 *
 *  - bug 39 : Allows the OAR grid sub behaviour when reserving (It means that when you want to reserve on the grid, if one of the reservation fails, all the already realized jobs are deleted). The choice is realized at the reservation time be checking/uncheking a checkbox in the reservation panel.
 *    * G5KRes : Adding a hashmap for the conservation of the ids of jobs submitted with success in order to delete them if their is one job not successfully submitted in the same "reservation" when the oargridsub behaviour is activated
 *    * OAROPRunnable : conservation of the id job, and getter/setter for that idjob
 *    * ResaDlg : Adding a checkbox for the selection of the oargridsub behaviour, and adding this behaviour to the reservation
 *    * G5KReservation : Boolean for the oargridsub behaviour with the associated getter and setter
 *    * OARUtil :  oarsub now returns the job id (when it is submitted successfully or not). When the job is not submitted successfully, the job id is lower than zero.
 *
 *  - bug 40 : To solve the lack of reservation status after the submission, a reservation status frame has been added. It displays the statuses of the different submissions realized by this reservation.
 *    * G5KRes : Adding the call to the ReservationStatusFrame constructor that displays the status frame
 *    * ReservationStatusFrame : frame for the status of the reservation that displays the information about the different submissions.
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.util.logging.Level;

import com.trilead.ssh2.Connection;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

import diet.grid.BatchSchedulerMgr;
import diet.grid.api.util.DefaultBatchSchedulerListener;

/**
 * This class runs the oarsub command
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class OarOpRunnable implements Runnable {
    /**
     * SSH Connection
     */
    Connection conn;
    /**
     * User
     */
    String     user;
    /**
     * frontal
     */
    String     frontal;
    /**
     * oar host
     */
    String     oar_host;
    /**
     * Start time of the reservation
     */
    String     startTime;
    /**
     * Walltime of the reservation
     */
    String     wallTime;
    /**
     * nodes number
     */
    int        nodeCount;
    /**
     * parent frame
     */
    G5kRes     myParent;
    /**
     * Queue on which we are deplying
     */
    String     queue;
    /**
     * Job id
     */
    int idJob;
    
    /**
     * Properties for the reservation
     */
    String properties = null;
    
    /**
     * Script to launch at runtime when the reservation comes up
     */
    String scriptToLaunch = null;
    
    /**
     * Chosen cluster index 
     */
    private int chosenCluster;
    
    /**
     * Site index
     */
    private int siteIdx;
    
    /**
     * Default constructor
     *
     * @param parent parent frame
     * @param _conn ssh connection
     * @param _user user
     * @param _frontal frontal
     * @param _oar_host oar host
     * @param _startTime start time of the reservation
     * @param _wallTime walltime of the reservation
     * @param _nodeCount number of nodes
     * @param _queue queue on which we are deploying
     * @param _properties properties of the reservation
     * @param _scriptToLaunch script to launch when the reservation comes up
     */
    public OarOpRunnable (G5kRes parent,
            int _siteIdx,
            Connection _conn,
            String _user, String _frontal, String _oar_host,
            String _startTime, String _wallTime, int _nodeCount,
            String _queue, String _properties, String _scriptToLaunch,
            int _chosenCluster) {
        this.myParent  = parent;
        this.siteIdx = _siteIdx;
        this.conn      = _conn;
        this.user      = _user;
        this.frontal   = _frontal;
        this.oar_host  = _oar_host;
        this.startTime = _startTime;
        this.wallTime  = _wallTime;
        this.nodeCount = _nodeCount;
        this.queue     = _queue;
        this.properties = _properties;
        this.scriptToLaunch = _scriptToLaunch;
        this.chosenCluster = _chosenCluster;
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "OarOpRunnable", "OarOpRunnable initialized");

    }

    /**
     * Method used to run the oarsub command
     */
    public void run() {
        /*
        idJob = OarUtil.sub(this.conn,
                this.user,
                this.frontal,
                this.oar_host,
                this.startTime,
                this.wallTime,
                this.nodeCount,
                this.queue, 
                this.properties,
                this.scriptToLaunch);
        */
        // TODO : scriptToLaunch and idJob
        if (this.chosenCluster == -1) {
            this.idJob = BatchSchedulerMgr.sub(
                            Config.getBatchScheduler(this.siteIdx), 
                            this.conn, 
                            this.user, 
                            this.oar_host, 
                            this.nodeCount, 
                            this.startTime, 
                            this.wallTime, 
                            this.queue,
                            this.scriptToLaunch,
                            new DefaultBatchSchedulerListener());
        }
        else {
            String [] params = { "-p \\\"cluster='" + 
                    G5kCluster.getClusterNameProperties(this.chosenCluster) + "'\\\""
            };
            this.idJob = BatchSchedulerMgr.sub(
                                Config.getBatchScheduler(this.siteIdx), 
                                this.conn, 
                                this.user, 
                                this.oar_host, 
                                this.nodeCount, 
                                this.startTime, 
                                this.wallTime, 
                                this.queue,
                                params,
                                this.scriptToLaunch,
                                new DefaultBatchSchedulerListener());
        }
    }

	/**
	 * Method returning the jobID
	 * 
	 * @return the idJob
	 */
	public int getIdJob() {
		return idJob;
	}

	/**
	 * Method setting the jobID
	 * 
	 * @param idJob the idJob to set
	 */
	public void setIdJob(int idJob) {
		this.idJob = idJob;
	}
}