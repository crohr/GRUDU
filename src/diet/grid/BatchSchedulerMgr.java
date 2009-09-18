/****************************************************************************/
/* Batch schedulers manager                                                 */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: BatchSchedulerMgr.java,v 1.4 2007/11/19 15:15:47 dloureir Exp $
 * $Log: BatchSchedulerMgr.java,v $
 * Revision 1.4  2007/11/19 15:15:47  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.3  2007/10/09 13:41:23  dloureir
 * Moving the parsing of the oar history from the GanttChart class to the oar1 scheduler and modifications of the history command for all shceduler in the batchScheduler class
 *
 * Revision 1.2  2007/10/05 13:52:35  aamar
 * Adding script parameter to submission.
 *
 * Revision 1.1  2007/09/28 15:59:56  aamar
 * Initial revision
 *
 ****************************************************************************/
package diet.grid;

import java.util.*;
import java.lang.reflect.*;
import com.trilead.ssh2.Connection;
import diet.grid.api.*;
import diet.grid.api.util.*;
import diet.plugins.PluginUtil;

public class BatchSchedulerMgr  {

    /**
     * Delete a batch job
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to gateway
     * @param batch_host Host running the batch scheduler
     * @param jobId Job identifier
     * @param listener Listener to display messages
     * @return
     */
    public static String[] delJob(String schedulerID,
            Connection conn, String batch_host, String jobId,
            BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.delJob(conn, batch_host, jobId, listener);			
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
        }
        return null;
    }

    /**
     * Get the cuurent jobs in batch schedulers queues
     * 
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to gateway
     * @param listener Listener to display messages
     * @return List of jobs if succeed, null if batch scheduler is not registred
     */
    public static GridJob[] getJobs(String schedulerID, Connection conn, BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.getJobs(conn, listener);
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
        }
        return null;
    }

    
    /**
     * Get the cuurent jobs in batch schedulers queues
     * 
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to gateway
     * @param batch_host Host running the batch scheduler
     * @param listener Listener to display messages
     * @return List of jobs if succeed, null if batch scheduler is not registred
     */
    public static GridJob[] getJobs(String schedulerID, 
            Connection conn, String batch_host, 
            BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.getJobs(conn, batch_host, listener);
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
        }
        return null;
    }

    /**
     * Get the status of nodes
     * 
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to gateway
     * @param batch_host Host running the batch scheduler
     * @param listener Listener to display messages
     * @return An integer array representing the status of nodes, 
     *         if batch scheduler is not found, the function returns null
     */
    public static int[] getNodesStatus(String schedulerID,
            Connection conn, String batch_host,
            BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.getNodesStatus(conn, batch_host, listener);
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
        }
        return null;
    }

    /**
     * Execute a stat operation
     * 
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to the gateway
     * @param batch_host Host running the batch scheduler
     * @param listener Listener to display messages
     * @return
     */
    public static String[] stat(String schedulerID,
            Connection conn, String batch_host,
            BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.stat(conn, batch_host, listener);
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
        }
        return null;
    }

    /**
     * Submit a job to the batch scheduler
     * 
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to the gateway
     * @param user User identifier
     * @param nodeCount Requested nodes count
     * @param startTime Requested reservation start time
     * @param wallTime Requested allocation time
     * @param queue Requested reservation queue
     * @param script The script to launch
     * @param listener Listener to display messages
     * @return true if submission succeed, otherwise or if the batch scheduler 
     *         is not registred it returns false
     */
    public static int sub(String schedulerID,
            Connection conn, String user, int nodeCount, String startTime, String wallTime, String queue,
            String script,
            BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.sub(conn, user, nodeCount, startTime, wallTime, queue, 
                    script, listener);
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
            return -1;
        }
    }

    /**
     * Submit a job to the batch scheduler
     * 
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to the gateway
     * @param user User identifier
     * @param batch_host Host running the batch scheduler
     * @param nodeCount Requested nodes count
     * @param startTime Requested reservation start time
     * @param wallTime Requested allocation time
     * @param queue Requested reservation queue
     * @param params Submission additional parameters
     * @param script The script to launch
     * @param listener Listener to display messages
     * @return true if submission succeed, otherwise or if the batch scheduler 
     *         is not registred it returns false
     */
    public static int sub(String schedulerID,
            Connection conn, String user, 
            String batch_host,
            int nodeCount, String startTime, String wallTime, String queue, 
            String[] params,
            String script,
            BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.sub(conn, user, batch_host, nodeCount, startTime, wallTime, queue, params, 
                    script,
                    listener);
        }
        return -1;
    }

    /**
     * Submit a job to the batch scheduler
     * 
     * @param schedulerID Used scheduler identifier as defined by its getId method
     * @param conn Ssh connection to the gateway
     * @param user User identifier
     * @param batch_host Host running the batch scheduler
     * @param nodeCount Requested nodes count
     * @param startTime Requested reservation start time
     * @param wallTime Requested allocation time
     * @param queue Requested reservation queue
     * @param script The script to launch
     * @param listener Listener to display messages
     * @return true if submission succeed, otherwise or if the batch scheduler 
     *         is not registred it returns false
     */
    public static int sub(String schedulerID,
            Connection conn, String user, String batch_host, int nodeCount, String startTime, String wallTime, String queue,
            String script,
            BatchSchedulerListener listener) {
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.sub(conn, user, batch_host, nodeCount, startTime, wallTime, queue, 
                    script, listener);
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
        }
        return -1;
    }

    /**
     * Default constructor.
     */
    protected BatchSchedulerMgr() {
        init();
    }

    /**
     * Initialize the batch scheduler by registring the available batch schedulers
     * in diet.grid.api package
     */
    private void init() {
        this.myBatchSchedulers = new HashMap<String, BatchScheduler>();
        try {
            ArrayList<Class> classes = PluginUtil.getClassesForPackage("diet.grid.api", BatchScheduler.class);
            System.out.println(classes.size() + " batch scheduler were found");
            for (Class c : classes) {
                System.out.println("  ** " + c.getModifiers());
                if (!Modifier.isAbstract(c.getModifiers())) {
                    System.out.println("Try to create " + c.getName());
                    BatchScheduler batchScheduler = (BatchScheduler)c.newInstance();
                    if (batchScheduler != null) {
                        System.out.println("Registring Batch Scheduler " + batchScheduler.getID());
                        this.myBatchSchedulers.put(batchScheduler.getID(), batchScheduler);
                    }
                }
            } // end for
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a singleton reference to the Batch scheduler
     * 
     * @return a reference to BatchSchedulerMgr singleton
     */
    public static BatchSchedulerMgr instance() {
        if (myInstance == null)
            myInstance = new BatchSchedulerMgr();
        return myInstance;
    }

    /**
     * Get the registred schedulers identifiers
     * 
     * @return The registred schedulers identifiers
     */
    public static String[] getSchedulersIDs() {
        ArrayList<String> schedulersIDs = new ArrayList<String>();
        Iterator<BatchScheduler> p = instance().myBatchSchedulers.values().iterator();
        while (p.hasNext()) {
            BatchScheduler scheduler = p.next();
            if (scheduler != null) {
                    schedulersIDs.add(scheduler.getID());
            }
        }
        return schedulersIDs.toArray(new String[schedulersIDs.size()]);
    }

    /**
     * 
     */
    public static ArrayList<GridJob> 
    history(String schedulerID, Connection connection,
    		ArrayList<GridJob> aJobsList,
    		String batch_host,
            Date startDate, Date endDate, 
            BatchSchedulerListener listener) {
        ArrayList<GridJob> lines = new ArrayList<GridJob>();
        if (instance().myBatchSchedulers.containsKey(schedulerID)) {
            BatchScheduler scheduler = instance().myBatchSchedulers.get(schedulerID);
            return scheduler.history(connection, aJobsList, batch_host, startDate, endDate, listener);
        }
        else {
            listener.addError("Can't find the batch scheduler " + schedulerID);
        }
        
        return lines;
    }
    
    /**
     * Singleton reference
     */
    private static BatchSchedulerMgr myInstance;

    /**
     * Schedulers references
     */
    private Map<String, BatchScheduler> myBatchSchedulers;
}
