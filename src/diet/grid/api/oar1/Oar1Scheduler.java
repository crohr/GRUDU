/****************************************************************************/
/* OAR1 utility class                                                       */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: Oar1Scheduler.java,v 1.8 2007/11/19 15:15:46 dloureir Exp $
 * $Log: Oar1Scheduler.java,v $
 * Revision 1.8  2007/11/19 15:15:46  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.7  2007/11/07 10:39:24  dloureir
 * Correcting the call to the addOutput method of the listener
 *
 * Revision 1.6  2007/10/17 15:51:45  dloureir
 * Adding a little "hack" for the support of the classic ssh usage with OAR2
 *
 * Revision 1.5  2007/10/12 13:40:54  dloureir
 * Removing unused import causing compilation failures
 *
 * Revision 1.4  2007/10/09 13:41:23  dloureir
 * Moving the parsing of the oar history from the GanttChart class to the oar1 scheduler and modifications of the history command for all shceduler in the batchScheduler class
 *
 * Revision 1.3  2007/10/08 14:53:57  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.2  2007/10/05 13:52:36  aamar
 * Adding script parameter to submission.
 *
 * Revision 1.1  2007/09/28 16:00:57  aamar
 * Initial revision
 *
 ****************************************************************************/

package diet.grid.api.oar1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import diet.grid.api.AbstractBatchScheduler;
import diet.grid.api.GridJob;
import diet.grid.api.util.*;
import diet.logging.LoggingManager;

/**
 * A <code>Oar1Scheduler</code> is a class to manipulate Oar batch scheduler using remote ssh connection
 * made by ganymed library.
 *
 * @author Abdelkader Amar
 *
 */
public class Oar1Scheduler extends AbstractBatchScheduler {

	/* (non-Javadoc)
	 * @see grid.api.BatchScheduler#delJob(com.trilead.ssh2.Connection, java.lang.String, java.lang.String)
	 */
	public String[] delJob(Connection conn, String batch_host, String jobId, BatchSchedulerListener listener) {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			Session sess = conn.openSession();

			sess.execCommand("ssh -o ConnectTimeout=5 " + batch_host
					+ " \"oardel " + jobId + "\"");

			InputStream stdout = new StreamGobbler(sess.getStdout());
			InputStream stderr = new StreamGobbler(sess.getStderr());

			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));
			BufferedReader brerr = new BufferedReader(new InputStreamReader(
					stderr));

			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				lines.add("-- " + line);
                listener.addOutput(line);
			} // end while
			while (true) {
				String line = brerr.readLine();
				if (line == null)
					break;
				lines.add("** " + line);
                listener.addError(line);
			} // end while
			brerr.close();
			br.close();
			sess.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lines.toArray(new String[lines.size()]);
	}

	/* (non-Javadoc)
	 * @see grid.api.BatchScheduler#getJobs(com.trilead.ssh2.Connection)
	 */
	public GridJob[] getJobs(Connection conn, BatchSchedulerListener listener) {
		// TODO Auto-generated method stub
        listener.addError("METHOD NOT IMPLEMENTED YET!!");
		return null;
	} // end getJobs

	/* (non-Javadoc)
	 * @see grid.api.BatchScheduler#getJobs(com.trilead.ssh2.Connection, java.lang.String)
	 */
	public GridJob[] getJobs(Connection conn, String batch_host, BatchSchedulerListener listener) {
		String[] content = this.stat(conn, batch_host);
		ArrayList<GridJob> jobList = new ArrayList<GridJob>();
		for (int ix = 0; ix < content.length; ix++) {
			if (content[ix].startsWith("Job Id: ")) {
				// Job Id, Owner, Status
				GridJob job = new Oar1Job();
				String line = "";
				String value = "";
				do {
					line = content[ix].trim();
					if (line.contains("="))
						value = line.split("=", 2)[1];
                    if (value != null)
                        value = value.trim();
					if (line.startsWith("Job_Name = ")) {
						job.setParameterValue(GridJob.KEY_GRID_JOB_ID, value);
					}
					if (line.startsWith("Job_Owner =")) {
						job.setParameterValue(GridJob.KEY_GRID_JOB_OWNER, value);
					}
					if (line.startsWith("job_state = ")) {
						job.setParameterValue(GridJob.KEY_GRID_JOB_STATE, value);
					}
					if (line.startsWith("comment =")){
						job.setParameterValue(Oar1Job.KEY_OAR1_JOB_COMMENT,value);
					}
					if (line.startsWith("weight =")){
						job.setParameterValue(Oar1Job.KEY_OAR1_JOB_WEIGHT, value);
					}
					if( line.startsWith("events =")){
						job.setParameterValue(Oar1Job.KEY_OAR1_JOB_EVENTS, value);
					}
					if( line.startsWith("command =")){
						job.setParameterValue(GridJob.KEY_GRID_JOB_COMMAND,value);
					}
					if( line.startsWith("launchingDirectory =")){
						job.setParameterValue(GridJob.KEY_GRID_JOB_LAUNCHING_DIRECTORY,value);
					}
					if( line.startsWith("properties =")){
						job.setParameterValue(GridJob.KEY_GRID_JOB_PROPERTIES,value);
					}
					if( line.startsWith("reservation =")){
						job.setParameterValue(GridJob.KEY_GRID_JOB_RESERVATION,value);
					}
                    if (line.startsWith("nbNodes = ")) {
                    	job.setParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT, value);    
                    }
                    if (line.startsWith("queue = ")) {
                    	job.setParameterValue(GridJob.KEY_GRID_JOB_QUEUE, value);    
                    }
                    if (line.startsWith("jobType = ")) {
                    	job.setParameterValue(GridJob.KEY_GRID_JOB_TYPE, value);    
                    }
                    if (line.startsWith("walltime = ")) {
                    	job.setParameterValue(GridJob.KEY_GRID_JOB_WALLTIME, value);    
                    }
                    if (line.startsWith("submissionTime = ")) {
                    	job.setParameterValue(GridJob.KEY_GRID_JOB_SUBTIME, value);    
                    }                    
                    if (line.startsWith("startTime = ")) {
                    	job.setParameterValue(GridJob.KEY_GRID_JOB_STARTTIME, value);    
                    }
                    if (line.startsWith("scheduledStart = ")) {
                    	job.setParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME, value);    
                    }
                    if (line.startsWith("exec_host = ")) {
                    	job.setHostsFromArray(value.split("\\+"));
                    }
					ix++;
				} while ((ix < content.length)
						&& (!content[ix].startsWith("Job Id: ")));
				jobList.add(job);
				listener.addOutput("  ** Job = " + job.getParameterValue(job.KEY_GRID_JOB_ID) + ", owner = "
						+ job.getParameterValue(job.KEY_GRID_JOB_OWNER) + " , state = " + job.getParameterValue(job.KEY_GRID_JOB_STATE));
				ix--;
			} // end if startsWith("Job Id: ");
		} // end for ix

		return jobList.toArray(new GridJob[jobList.size()]);
	}

	/* (non-Javadoc)
	 * @see grid.api.BatchScheduler#getNodesStatus(com.trilead.ssh2.Connection, java.lang.String)
	 */
	public int[] getNodesStatus(Connection conn, String batch_host, BatchSchedulerListener listener) {
        //int[] result = { -1,-1,-1,-1,-1,-1};
		// TODO change this if it does not work well
		int[] result = {0,0,0,0,0,0};
        ArrayList<String> lines = new ArrayList<String>();
        try {
            // getting the information from the oarnodes
            Session sess = conn.openSession();
            sess.execCommand("ssh " + batch_host + " oarnodes");

            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                lines.add(line);
                //listener.addOutput(line);
            } // end while
            while ( true ) {
                String line = brerr.readLine();
                if (line == null)
                    break;
                listener.addError(line);
            } // end while
            brerr.close();
            br.close();
            sess.close();
            Iterator<String> iter;
            //result = "|";
            int total = 0;
            for(int i = 0 ; i < infos.length ; i ++){
                int value = 0;
                iter = lines.iterator();
                while(iter.hasNext()){
                    String ligne = iter.next();
                    if(ligne.toLowerCase().contains(infos[i].toLowerCase())){
                        value++;
                    }
                }
                result[i] = value;
                total += value;
            }
            result[5] = total;
        }
        catch(Exception e){
            listener.addError(e.toString());
        }
        return result;
	}

	/* (non-Javadoc)
	 * @see grid.api.BatchScheduler#stat(com.trilead.ssh2.Connection, java.lang.String)
	 */
	public String[] stat(Connection conn, String batch_host, BatchSchedulerListener listener) {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			Session sess = conn.openSession();
			sess.execCommand("ssh -o ConnectTimeout=5 " + batch_host
					+ " \"oarstat -f\"");

			InputStream stdout = new StreamGobbler(sess.getStdout());
			InputStream stderr = new StreamGobbler(sess.getStderr());

			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));
			BufferedReader brerr = new BufferedReader(new InputStreamReader(
					stderr));
			String line;
			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				lines.add(line);
                //listener.addOutput(line);
			} // end while
			while (true) {
				line = brerr.readLine();
				if (line == null)
					break;
                listener.addError(line);
			} // end while
			brerr.close();
			br.close();
			sess.close();

		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return lines.toArray(new String[lines.size()]);
	} // end stat

	/* (non-Javadoc)
	 * @see diet.grid.api.AbstractBatchScheduler#sub(com.trilead.ssh2.Connection, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, diet.grid.api.util.BatchSchedulerListener)
	 */
	public int sub(Connection conn, String user, int nodeCount,
			String startTime, String wallTime, String queue, 
			String script, 
			BatchSchedulerListener listener) {
        listener.addError("METHOD NOT IMPLEMENTED YET");
        return -1;
	}

	/* (non-Javadoc)
	 * @see diet.grid.api.AbstractBatchScheduler#sub(com.trilead.ssh2.Connection, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, diet.grid.api.util.BatchSchedulerListener)
	 */
	public int sub(Connection conn, String user, 
	        String batch_host,
	        int nodeCount,
			String startTime, String wallTime, String queue, String[] params, 
			String script,
			BatchSchedulerListener listener) {
		listener.addError("METHOD NOT IMPLEMENTED YET");
		return -1;
	} // end sub

	/* (non-Javadoc)
	 * @see diet.grid.api.AbstractBatchScheduler#sub(com.trilead.ssh2.Connection, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, diet.grid.api.util.BatchSchedulerListener)
	 */
	public int sub(Connection conn, String user, String batch_host,
			int nodeCount, String startTime, String wallTime, String queue, 
			String script,
			BatchSchedulerListener listener) {
		String realQueue = null;
		if(queue.equalsIgnoreCase("default")){
			realQueue = "default";
		}
		else{
			if(queue.equalsIgnoreCase("deploy")){
				realQueue = "deploy";
			}
			else{
				realQueue = "default";
			}
		}
		String subCmd = // Reservation cmd
		"ssh -o ConnectTimeout=10 " + batch_host
				+ " \"oarsub -r " + "\\\""
				+ startTime + "\\\"" + " -q " + realQueue + " -l " + "nodes="
				+ nodeCount + "," + "walltime=" + wallTime + "\"";
		listener.addOutput(subCmd);
		String lines = new String();
		int idJob = -1;
		try {
			Session sess = conn.openSession();
			// Exec the submission command
			sess.execCommand(subCmd);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			InputStream stderr = new StreamGobbler(sess.getStderr());

			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));
			BufferedReader brerr = new BufferedReader(new InputStreamReader(
					stderr));

			boolean substate = false;
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
                //listener.addOutput(line);
				if (line.contains("OK") && line.contains("Reservation valid"))
					substate = true;
		        if(line.contains("IdJob =")){
		            int equalIndex = line.indexOf("=");
		            idJob = Integer.parseInt(line.substring(equalIndex+1, line.length()).trim());
		          }

				lines += line;
			}

			while (true) {
				String line = brerr.readLine();
				if (line == null)
					break;
				listener.addError(line);

			} // end while
			brerr.close();
			br.close();
			sess.close();
		} catch (IOException e) {
			listener.addError(e.toString());
		}

		return idJob;
	} // end sub

	/* (non-Javadoc)
	 * @see diet.grid.api.BatchScheduler#getID()
	 */
	public String getID() {
		return "OAR1";
	}

	public Class getJobType() {
	    return Oar1Job.class;
	}
	/**
	 * Array containing the information for each state
	 */
	private final static String[] infos =  {"state = free",
	    "state = job",
	    "state = suspected",
	    "state = dead",
	    "state = absent"
	};
    public ArrayList<GridJob> history(Connection connection, 
    		ArrayList<GridJob> aJobList,
    		String batch_host,
            Date startDate, Date endDate,
            BatchSchedulerListener listener) {
    	ArrayList<GridJob> jobsList = new ArrayList<GridJob>();
    	for (int i = 0 ; i < aJobList.size() ; i++){
    		jobsList.add(aJobList.get(i).copy());
    	}
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Session sess = connection.openSession();
            sess.execCommand("ssh " + batch_host + " \" oarstat -h \\\"" + getOARDateFromDate(startDate) + ", " + getOARDateFromDate(endDate) +" \\\" \"");
            InputStream stdout = new StreamGobbler(sess.getStdout());
			InputStream stderr = new StreamGobbler(sess.getStderr());

			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				lines.add(line);
			}
			br.close();
			while (true) {
				String line = brerr.readLine();
				if (line == null)
					break;
				listener.addError(line);
			}
			brerr.close();
			sess.close();
			Iterator<String> iter = lines.iterator();
            /*
             * The history is separated into three parts:
             *
             *  - the first is the free nodes written as follows :
             *  'node1' 'weightOfNode1' 'node2' 'weightOfNode2' ...
             *   eg :
             *  'sagittaire-12.lyon.grid5000.fr' '2' 'sagittaire-13.lyon.grid5000.fr' '2'
             *
             *  - the second is a list of jobs :
             *  'job id' 'type' 'user' 'state' 'command' 'properties' 'start time' 'queue' 'submission time' 'end time' 'node1' 'weightOfNode1' 'node2' 'weightOfNode2' ...
             *   e.g. :
             *   '20081' 'PASSIVE' 'ssoudan' 'Running' '/bin/sleep 72000' '(p.deploy = "YES") AND p.deploy = "YES" ' '2007-03-23 10:18:48' 'deploy' '2007-03-23 10:19:03' '2007-03-24 06:18:48' 'sagittaire-19.lyon.grid5000.fr' '2' 'sagittaire-24.lyon.grid5000.fr' '2'
             *
             *  - the last one is a list of dead jobs :
             *  the last part as the same pattern as the jobs
             *  '0' 'Dead' 'oar' 'null' 'null' 'null' '0000-00-00 00:00:00' 'default' '2007-03-22 19:30:02' '2007-03-24 14:00:00' 'sagittaire-60.lyon.grid5000.fr' '2'
             *
             */
            // for all jobs
            while(iter.hasNext()){
                String historyLine = iter.next();
                historyLine = historyLine.replace("''","' '");
                StringTokenizer tokenizer = new StringTokenizer(historyLine,"'");
                ArrayList<String> information = new ArrayList<String>();
                while(tokenizer.hasMoreTokens()){
                    information.add(tokenizer.nextToken());
                }
                int jobNumber=-1;
                try{
                    // if no exception is thrown then it means that we are no! in the first
                    // part of the history
                    jobNumber = Integer.parseInt(information.get(0));
                }
                catch(Exception e){
                    LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);
                }
                if(jobNumber == 0){
                    // this is a dead of absent node
                    GridJob aJob = new Oar1Job();
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_HOSTS, information.get(20));
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_ID, "0");
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_OWNER, "");
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_QUEUE, "");
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_STATE, "");
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT, "1");
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME,
                            getOARDateFromDate(Calendar.getInstance().getTime()));//information.get(12);
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_STARTTIME,
                            getOARDateFromDate(Calendar.getInstance().getTime()));
                    long duration = endDate.getTime() - startDate.getTime();
                    aJob.setParameterValue(GridJob.KEY_GRID_JOB_WALLTIME,
                            getWallTimeFromDate(duration));//ntheWallTime;
                    jobsList.add(aJob);
                }
                else if (jobNumber != -1){
                    // this is not a dead or absent or suspected job but a classical job
                    GridJob aJob = null;
                    //searching the job
                    for(int j = 0 ;j < jobsList.size() ; j++){
                        aJob = jobsList.get(j);
                        String firstParam = aJob.getParameterValue(GridJob.KEY_GRID_JOB_ID);
                        if(jobNumber == Integer.parseInt(firstParam)){
                            j=jobsList.size();
                        }
                    }
                    // if the job is waiting
                    if(aJob.getParameterValue(GridJob.KEY_GRID_JOB_STATE).substring(0, 1).equalsIgnoreCase("w")){
                        ArrayList<String> theNodes = new ArrayList<String>();
                        for(int index = 20 ; index < information.size() ; index +=4){
                            theNodes.add(information.get(index));
                        }

                        if(theNodes.size() >= 1){
//                            String nodesOfTheJob = theNodes.get(0);
//                            for(int anIndex = 1 ; anIndex < theNodes.size(); anIndex ++){
//                                nodesOfTheJob += "+" + theNodes.get(anIndex);
//                            }
                            // TODO: check this operation (don't understand how use it)
                            aJob.setHostsFromArray(theNodes.toArray(new String[0]));
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            listener.addError("oarstat " + e);
        }
        for(int i = 0 ; i < jobsList.size() ; i ++){
        	GridJob aJob = jobsList.get(i);
        	if(aJob.getParameterValue(GridJob.KEY_GRID_JOB_STARTTIME).startsWith("0000")) aJob.setParameterValue(GridJob.KEY_GRID_JOB_STARTTIME,aJob.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME));
        	long duration = getDateFromOARDate(aJob.getParameterValue(GridJob.KEY_GRID_JOB_STARTTIME)).getTime();
        	aJob.setParameterValue(GridJob.KEY_GRID_JOB_STARTTIME, Long.toString(duration) );
        	
        	duration = getDateFromOARDate(aJob.getParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME)).getTime();
        	aJob.setParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME, Long.toString(duration) );
        	duration = getDateFromOARDate(aJob.getParameterValue(GridJob.KEY_GRID_JOB_SUBTIME)).getTime();
        	aJob.setParameterValue(GridJob.KEY_GRID_JOB_SUBTIME, Long.toString(duration) );
        	duration = getDateFromWallTime(aJob.getParameterValue(GridJob.KEY_GRID_JOB_WALLTIME));
        	aJob.setParameterValue(GridJob.KEY_GRID_JOB_WALLTIME, Long.toString(duration) );
        }
        return jobsList;
    }
    
    /**
     * Method returning the Date in the OAR Date format
     *
     * @param aDate a Date
     * @return the date formatted for OAR
     */
    public static String getOARDateFromDate(Date aDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(aDate);
    }

    /**
     * Method returning the date in the OAR format given in argument in the classical java format
     *
     * @param aFormattedDate an OAR Formatted date
     * @return a Date in the classical java format
     */
    public static Date getDateFromOARDate(String aFormattedDate){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(aFormattedDate);
        }catch(Exception e){
            return new Date();
        }
    }

    /**
     * Method returning a duration from the walltime
     *
     * @param aFormattedWallTime a walltime
     * @return a duration corresponding to the walltime
     */
    public static long getDateFromWallTime(String aFormattedWallTime){
        StringTokenizer tokenizer = new StringTokenizer(aFormattedWallTime,":");
        int hours = Integer.parseInt(tokenizer.nextToken());
        int minutes = Integer.parseInt(tokenizer.nextToken());
        int seconds = Integer.parseInt(tokenizer.nextToken());
        return (hours*HistoryUtil.HOUR + minutes*HistoryUtil.MINUTE + seconds*HistoryUtil.SECOND);
    }

    /**
     * Method returning a walltime from a duration
     *
     * @param aTimeInMillis a duration
     * @return a walltime
     */
    public static String getWallTimeFromDate(long aTimeInMillis){
        int hours=(int)(aTimeInMillis/HistoryUtil.HOUR);
        long temp = aTimeInMillis - hours*HistoryUtil.HOUR;
        int minutes = (int)(temp/HistoryUtil.MINUTE);
        int seconds = (int)((temp - minutes*HistoryUtil.MINUTE)/HistoryUtil.SECOND);
        NumberFormat formatter = new DecimalFormat("##");
        String result = formatter.format(hours);
        result +=":" + formatter.format(minutes);
        result +=":" + formatter.format(seconds);
        return result;
    }
}
