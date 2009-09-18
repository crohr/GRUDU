/****************************************************************************/
/* Batch scheduler general interface                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: BatchScheduler.java,v 1.5 2007/11/19 15:15:47 dloureir Exp $
 * $Log: BatchScheduler.java,v $
 * Revision 1.5  2007/11/19 15:15:47  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.4  2007/10/09 13:41:23  dloureir
 * Moving the parsing of the oar history from the GanttChart class to the oar1 scheduler and modifications of the history command for all shceduler in the batchScheduler class
 *
 * Revision 1.3  2007/10/05 13:52:35  aamar
 * Adding script parameter to submission.
 *
 * Revision 1.2  2007/09/28 16:00:41  aamar
 * Initial revision
 *
 * Revision 1.1  2007/09/21 13:52:35  aamar
 * Initial revision
 *
 ****************************************************************************/

package diet.grid.api;

import com.trilead.ssh2.Connection;
import java.util.ArrayList;
import java.util.Date;

import diet.grid.api.util.*;
/**
 * The <code>BatchScheduler</code> interface defines the methods needed to manipulate 
 * a Batch scheduler for the Grid.
 * @author aamar
 *
 */
public interface BatchScheduler {

    /**
     * Delete a job by its identifier.
     * 
     * @param conn Ssh connection to the gateway
     * @param batch_host The host that execute the batch scheduler
     * @param jobId the identifier of job to remove
     * @param listener batch scheduler listener that handles output messages   
     * @return the output of the command execution
     */
    public String[] delJob(Connection conn, String batch_host, String jobId, BatchSchedulerListener listener);

    /**
	 * Delete a job by its identifier.
	 * 
	 * @param conn Ssh connection to the gateway
	 * @param batch_host The host that execute the batch scheduler
	 * @param jobId the identifier of job to remove
	 * @return the output of the command execution
	 */
	public String[] delJob(Connection conn, String batch_host, String jobId);

	/**
	 * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
	 * objects.
	 * 
	 * @param conn Ssh connection to the gateway
     * @param listener batch scheduler listener that handles output messages   
	 * @return The actives jobs (waiting or running jobs)
	 */
    public GridJob[] getJobs(Connection conn, BatchSchedulerListener listener);
    
    /**
     * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
     * objects.
     * 
     * @param conn Ssh connection to the gateway
     * @return The actives jobs (waiting or running jobs)
     */

    public GridJob[] getJobs(Connection conn);

	/**
	 * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
	 * objects.
	 * 
	 * @param conn Ssh connection to the gateway
	 * @param batch_host The batch host if different from the gateway
     * @param listener batch scheduler listener that handles output messages   
	 * @return The actives jobs (waiting or running jobs)
	 */
	public GridJob[] getJobs(Connection conn, String batch_host, BatchSchedulerListener listener);

    /**
     * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
     * objects.
     * 
     * @param conn Ssh connection to the gateway
     * @param batch_host The batch host if different from the gateway
     * @return The actives jobs (waiting or running jobs)
     */
    public GridJob[] getJobs(Connection conn, String batch_host);

	/**
	 * Return the status of hardware nodes
	 * 
	 * @param conn Ssh connection to the gateway
	 * @param batch_host The batch host if different from the gateway
     * @param listener batch scheduler listener that handles output messages   
	 * @return An array of status
	 */
	public abstract int[] getNodesStatus(Connection conn, String batch_host, BatchSchedulerListener listener);

    /**
     * Return the status of hardware nodes
     * 
     * @param conn Ssh connection to the gateway
     * @param batch_host The batch host if different from the gateway
     * @return An array of status
     */
    public int[] getNodesStatus(Connection conn, String batch_host);

    /**
	 *  Return the status of the actives jobs as a string. The content is the result of the status command
	 *  execution
	 *  
	 * @param conn Ssh connection to the gateway
	 * @param batch_host The Batch host if different from the gateway
     * @param listener batch scheduler listener that handles output messages   
	 * @return The output of status command
	 */
	public  String[] stat(Connection conn, String batch_host, BatchSchedulerListener listener);

    /**
     *  Return the status of the actives jobs as a string. The content is the result of the status command
     *  execution
     *  
     * @param conn Ssh connection to the gateway
     * @param batch_host The Batch host if different from the gateway
     * @return The output of status command
     */
    public String[] stat(Connection conn, String batch_host);

	/**
	 * This method executes an allocation request.
	 * 
	 * @param conn Ssh connection to the gateway
	 * @param user the user login
	 * @param nodeCount the number of requested nodes
	 * @param startTime the start time
	 * @param wallTime the reservation wall time
	 * @param queue the reservation queue
     * @param script the script to launch
     * @param listener batch scheduler listener that handles output messages   
	 * @return the reservation pre-status
	 */
	public int sub (Connection conn,
			String user,
			int nodeCount, String startTime, String wallTime,
			String queue,
			String script,
            BatchSchedulerListener listener);

    /**
     * This method executes an allocation request.
     * 
     * @param conn Ssh connection to the gateway
     * @param user the user login
     * @param nodeCount the number of requested nodes
     * @param startTime the start time
     * @param wallTime the reservation wall time
     * @param queue the reservation queue
     * @param script the script to launch
     * @return the reservation pre-status
     */
    public int sub (Connection conn,
            String user,
            int nodeCount, String startTime, String wallTime,
            String queue,
            String script);

	/**
	 * This method executes an allocation request.
	 * 
	 * @param conn Ssh connection to the gateway
	 * @param user the user login
     * @param batch_host Host running the batch scheduler
	 * @param nodeCount the number of requested nodes
	 * @param startTime the start time
	 * @param wallTime the reservation wall time
	 * @param queue the reservation queue
	 * @param params extra parameters
     * @param script the script to launch
     * @param listener batch scheduler listener that handles output messages   
	 * @return the reservation pre-status
	 */
	public int sub (Connection conn,
	        String user,
	        String batch_host,
			int nodeCount, String startTime, String wallTime,
			String queue,
			String [] params,
			String script,
            BatchSchedulerListener listener);

    /**
     * This method executes an allocation request.
     * 
     * @param conn Ssh connection to the gateway
     * @param user the user login
     * @param batch_host Host running the batch scheduler
     * @param nodeCount the number of requested nodes
     * @param startTime the start time
     * @param wallTime the reservation wall time
     * @param queue the reservation queue
     * @param params extra parameters
     * @param script the script to launch
     * @return the reservation pre-status
     */
	public int sub (Connection conn,
            String user,
            String batch_host,
            int nodeCount, String startTime, String wallTime,
            String queue,
            String [] params,
            String script);
    
	/**
	 * This methods executes an allocation requests.
	 * 
	 * @param conn Ssh connection to the gateway
	 * @param user the user login
	 * @param batch_host the batch host if different from the gateway
	 * @param nodeCount the number of requested nodes
	 * @param startTime the start time
	 * @param wallTime the reservation wall time
	 * @param queue the reservation queue
	 * @param script the script to launch
     * @param listener batch scheduler listener that handles output messages   
	 * @return the reservation pre-status
	 */
	public int sub (Connection conn,
			String user, String batch_host,
			int nodeCount, String startTime, String wallTime,
			String queue,
			String script,
            BatchSchedulerListener listener);

    /**
     * This methods executes an allocation requests.
     * 
     * @param conn Ssh connection to the gateway
     * @param user the user login
     * @param batch_host the batch host if different from the gateway
     * @param nodeCount the number of requested nodes
     * @param startTime the start time
     * @param wallTime the reservation wall time
     * @param script the script to launch
     * @param queue the reservation queue
     * @return the reservation pre-status
     */
    public int sub (Connection conn,
            String user, String batch_host,
            int nodeCount, String startTime, String wallTime,
            String script,
            String queue);

	/**
	 * Return the Batch Scheduler unique identifier
	 */
	public abstract String getID();
    
	/**
	 * 
	 * @param connection
	 * @param batch_host
	 * @param startDate
	 * @param endDate
	 * @param listener
	 * @return
	 */
	public ArrayList<GridJob>
	history(Connection connection, ArrayList<GridJob> aJobList, String batch_host, Date startDate, Date endDate,
	        BatchSchedulerListener listener);
	
	/**
	 * return the scheduler job class
	 */
	public abstract Class getJobType();
	
    /**
     * Index representing the free state of a node
     */
    public final static int FREE_NODE      = 0;
    /**
     * Index representing the job state of a node
     */
    public final static int JOB_NODE       = 1;
    /**
     * Index representing the suspected state of a node
     */
    public final static int SUSPECTED_NODE = 2;
    /**
     * Index representing the dead state of a node
     */
    public final static int DEAD_NODE      = 3;
    /**
     * Index representing the absent state of a node
     */
    public final static int ABSENT_NODE    = 4;
    /**
     * The number of node status
     */
    public final int NODE_STATUS_COUNT = 5;

}
