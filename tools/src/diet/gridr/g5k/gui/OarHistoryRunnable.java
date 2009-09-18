/****************************************************************************/
/* Oar History utility class                                                */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarHistoryRunnable.java,v 1.6 2007/11/19 15:16:19 dloureir Exp $
 * $Log: OarHistoryRunnable.java,v $
 * Revision 1.6  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.5  2007/10/09 13:41:23  dloureir
 * Moving the parsing of the oar history from the GanttChart class to the oar1 scheduler and modifications of the history command for all shceduler in the batchScheduler class
 *
 * Revision 1.4  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.3  2007/07/12 14:46:26  dloureir
 * Adding the javadoc
 *
 * Revision 1.2  2007/05/22 07:35:52  aamar
 * Correct header.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.util.ArrayList;
import java.util.Date;

import com.trilead.ssh2.Connection;
import diet.grid.BatchSchedulerMgr;
import diet.grid.api.GridJob;
import diet.grid.api.util.DefaultBatchSchedulerListener;

/**
 * Oar History utility class
 * 
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class OarHistoryRunnable implements Runnable {

	/**
	 * SSH connection
	 */
    Connection connection = null;
    /**
     * Name of the oar frontale
     */
    String oar_host = null;
    /**
     * lines returned by the oar command corresponding to the history
     */
    ArrayList<GridJob> jobsList = null;
    /**
     * Start date of the history
     */
    Date startDate = null;
    /**
     * end date of the history
     */
    Date endDate = null;
    /**
     * Batch Scheduler name
     */
    String batchScheduler = null;

    /**
     * Default constructor
     * 
     * @param conn SSH connection
     * @param oar_host oar frontale name
     * @param historyLines lines of the history
     * @param startDate start date of the history
     * @param endDate end date of the history
     */
    public OarHistoryRunnable(Connection conn,
    		ArrayList<GridJob> historyLines,
    		String aBatchScheduler,
            String oar_host,
            Date startDate, Date endDate){
    	batchScheduler = aBatchScheduler;
        connection = conn;
        this.oar_host = oar_host;
        this.jobsList = historyLines;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** 
     * Method running the history retrieving
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        // TODO : replace OAR2 by the real batch scheduler id
        // historyLines = OarUtil.oarhistory(connection, oar_host, startDate, endDate);
        this.jobsList = BatchSchedulerMgr.history(
        		batchScheduler,
                connection, 
                jobsList,
                this.oar_host, 
                this.startDate, 
                this.endDate, 
                new DefaultBatchSchedulerListener());
    }

    /**
     * method returning the ArrayList containing the history lines
     * 
     * @return an ArrayList containing the lines of history
     */
    public ArrayList<GridJob> getJobsList(){
        return jobsList;
    }

}
