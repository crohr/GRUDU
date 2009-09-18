/****************************************************************************/
/* Batch Scheduler abstract class                                           */
/* Implements a default behaviour for some Batch Scheduler function         */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: AbstractBatchScheduler.java,v 1.4 2007/11/19 15:15:47 dloureir Exp $
 * $Log: AbstractBatchScheduler.java,v $
 * Revision 1.4  2007/11/19 15:15:47  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
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
import diet.grid.api.util.BatchSchedulerListener;
import diet.grid.api.util.DefaultBatchSchedulerListener;

public abstract class AbstractBatchScheduler implements BatchScheduler {

    /**
     * Delete a job by its identifier.
     * 
     * @param conn Ssh connection to the gateway
     * @param batch_host The host that execute the batch scheduler
     * @param jobId the identifier of job to remove
     * @param listener batch scheduler listener that handles output messages   
     * @return the output of the command execution
     */
    public abstract String[] delJob(Connection conn, String batch_host, String jobId, BatchSchedulerListener listener);

    /**
     * Delete a job by its identifier.
     * 
     * @param conn Ssh connection to the gateway
     * @param batch_host The host that execute the batch scheduler
     * @param jobId the identifier of job to remove
     * @return the output of the command execution
     */
    public String[] delJob(Connection conn, String batch_host, String jobId) {
        return delJob(conn, batch_host, jobId, new DefaultBatchSchedulerListener());
    }

    /**
     * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
     * objects.
     * 
     * @param conn Ssh connection to the gateway
     * @param listener batch scheduler listener that handles output messages   
     * @return The actives jobs (waiting or running jobs)
     */
    public abstract GridJob[] getJobs(Connection conn, BatchSchedulerListener listener);
    
    /**
     * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
     * objects.
     * 
     * @param conn Ssh connection to the gateway
     * @return The actives jobs (waiting or running jobs)
     */

    public GridJob[] getJobs(Connection conn) {
        return getJobs(conn, new DefaultBatchSchedulerListener());
    }

    /**
     * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
     * objects.
     * 
     * @param conn Ssh connection to the gateway
     * @param batch_host The batch host if different from the gateway
     * @param listener batch scheduler listener that handles output messages   
     * @return The actives jobs (waiting or running jobs)
     */
    public abstract GridJob[] getJobs(Connection conn, String batch_host, BatchSchedulerListener listener);

    /**
     * Check the status of available jobs and return the result as an array of <code>GridJob</code> 
     * objects.
     * 
     * @param conn Ssh connection to the gateway
     * @param batch_host The batch host if different from the gateway
     * @return The actives jobs (waiting or running jobs)
     */
    public GridJob[] getJobs(Connection conn, String batch_host) {
        return getJobs(conn, batch_host, new DefaultBatchSchedulerListener());
    }

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
    public int[] getNodesStatus(Connection conn, String batch_host) {
        return getNodesStatus(conn, batch_host, new DefaultBatchSchedulerListener());
    }

    /**
     *  Return the status of the actives jobs as a string. The content is the result of the status command
     *  execution
     *  
     * @param conn Ssh connection to the gateway
     * @param batch_host The Batch host if different from the gateway
     * @param listener batch scheduler listener that handles output messages   
     * @return The output of status command
     */
    public abstract String[] stat(Connection conn, String batch_host, BatchSchedulerListener listener);

    /**
     *  Return the status of the actives jobs as a string. The content is the result of the status command
     *  execution
     *  
     * @param conn Ssh connection to the gateway
     * @param batch_host The Batch host if different from the gateway
     * @return The output of status command
     */
    public String[] stat(Connection conn, String batch_host) {
        return stat(conn, batch_host, new DefaultBatchSchedulerListener());
    }

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
    public abstract int sub (Connection conn,
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
     * @param script the script to launch
     * @param queue the reservation queue
     * @return the reservation pre-status
     */
    public int sub (Connection conn,
            String user,
            int nodeCount, String startTime, String wallTime,
            String script,
            String queue) {
        return sub(conn, user, nodeCount, startTime, wallTime, queue, script, new DefaultBatchSchedulerListener());
    }

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
    public abstract int sub (Connection conn,
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
            String script) {
        return sub(conn, user, batch_host, nodeCount, startTime, wallTime, queue, params, 
                script,
                new DefaultBatchSchedulerListener());
    }

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
    public abstract int sub (Connection conn,
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
     * @param queue the reservation queue
     * @return the reservation pre-status
     */
    public int sub (Connection conn,
            String user, String batch_host,
            int nodeCount, String startTime, String wallTime,
            String queue,
            String script) {
        return sub (conn, user, batch_host, nodeCount, startTime, wallTime, queue, 
                script, 
                new DefaultBatchSchedulerListener());
    }

    /**
     * Return the Batch Scheduler unique identifier
     */
    public abstract String getID();
    

}
