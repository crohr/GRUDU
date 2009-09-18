/****************************************************************************/
/* Grid job general interface                                               */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: GridJob.java,v 1.1 2007/10/30 10:25:23 dloureir Exp $
 * $Log: GridJob.java,v $
 * Revision 1.1  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.9  2007/10/23 14:44:53  aamar
 * Correction job parameters setting when creating a GenericGridJob
 *
 * Revision 1.8  2007/10/09 13:41:23  dloureir
 * Moving the parsing of the oar history from the GanttChart class to the oar1 scheduler and modifications of the history command for all shceduler in the batchScheduler class
 *
 * Revision 1.7  2007/10/08 14:53:58  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.6  2007/10/06 09:39:09  aamar
 * Adding toString method to display GridJob information
 *
 * Revision 1.5  2007/10/05 16:13:20  dloureir
 * Removing the parameter for the hosts
 *
 * Revision 1.4  2007/10/05 15:23:21  dloureir
 * Adding the hosts to the gridjob parameters
 *
 * Revision 1.2  2007/09/28 16:00:41  aamar
 * Initial revision
 *
 * Revision 1.1  2007/09/21 13:52:35  aamar
 * Initial revision
 *
 ****************************************************************************/
package com.izforge.izpack.panels.g5k_utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Grid job interface
 * 
 * @author aamar
 *
 */
public abstract class GridJob {
	
	protected HashMap<String, String> parametersValues = null;
	protected HashMap<String, String> parametersDescriptions = null;
	private String gridJobId = "Grid Job";
    /**
     * Parameters description
     */
/*    public static final String[] paramDescr = {"Job ID", "Owner", "State", "Nb Res",
                "Queue", "Type", "WallTime", "SubTime", "StartTime", "SchedTime"};
*/    
    /**
     * Job ID 
     */
 //   public static final int GJOB_ID            = 0;
    public static final String DESCRIPTION_GRID_JOB_ID = "Job ID";
    public static final String KEY_GRID_JOB_ID = "jobID";
    
    /**
     * Job Owner 
     */
   // public static final int GJOB_OWNER         = 1;
    public static final String DESCRIPTION_GRID_JOB_OWNER = "Owner";
    public static final String KEY_GRID_JOB_OWNER = "jobOwner";
    /**
     * Job Stat
     */
//    public static final int GJOB_STATE         = 2;
    public static final String DESCRIPTION_GRID_JOB_STATE = "State";
    public static final String KEY_GRID_JOB_STATE = "jobState";
    /**
     * Resources count
     */
//    public static final int GJOB_RES_COUNT     = 3;
    public static final String DESCRIPTION_GRID_JOB_RESOURCES_COUNT = "Resources number";
    public static final String KEY_GRID_JOB_RESOURCES_COUNT = "jobResourcesCount";
    /**
     * Job queue
     */
//    public static final int GJOB_QUEUE          = 4;
    public static final String DESCRIPTION_GRID_JOB_QUEUE = "Queue";
    public static final String KEY_GRID_JOB_QUEUE = "jobQueue";
    
    /**
     * Job type
     */
//    public static final int GJOB_TYPE           = 5;
    public static final String DESCRIPTION_GRID_JOB_TYPE = "Type";
    public static final String KEY_GRID_JOB_TYPE = "jobType";
    
    /**
     * Job walltime 
     */
//    public static final int GJOB_WALLTIME       = 6;
    public static final String DESCRIPTION_GRID_JOB_WALLTIME = "Walltime";
    public static final String KEY_GRID_JOB_WALLTIME = "jobWalltime";
    
    /**
     * Job submission time 
     */
//    public static final int GJOB_SUBTIME        = 7;
    public static final String DESCRIPTION_GRID_JOB_SUBTIME = "Submit time";
    public static final String KEY_GRID_JOB_SUBTIME = "jobSubmitTime";
    
    /**
     * Job start time
     */
//    public static final int GJOB_STARTTIME      = 8;
    public static final String DESCRIPTION_GRID_JOB_STARTTIME = "Start time";
    public static final String KEY_GRID_JOB_STARTTIME = "jobStartTime";
    
    /**
     * Job scheduling time
     */
//    public static final int GJOB_SCHEDTIME      = 9;
    public static final String DESCRIPTION_GRID_JOB_SCHEDTIME = "Scheduled time";
    public static final String KEY_GRID_JOB_SCHEDTIME = "jobScheduledTime";
    
    public static final String KEY_GRID_JOB_PROPERTIES = "jobProperties";
    public static final String DESCRIPTION_GRID_JOB_PROPERTIES = "Properties";
    
    public static final String KEY_GRID_JOB_RESERVATION = "jobReservation";
    public static final String DESCRIPTION_GRID_JOB_RESERVATION = "Reservation";
    
    public static final String KEY_GRID_JOB_LAUNCHING_DIRECTORY = "jobLaunchingDirectory";
    public static final String DESCRIPTION_GRID_JOB_LAUNCHING_DIRECTORY = "Launching Directory";
    
    public static final String KEY_GRID_JOB_COMMAND = "jobCommand";
    public static final String DESCRIPTION_GRID_JOB_COMMAND = "Command";
    
    public static final String DESCRIPTION_GRID_JOB_HOSTS = "Hosts";
    public static final String KEY_GRID_JOB_HOSTS = "jobHosts";
    
    public static final String HOSTS_SEPARATOR = "|";
    
    /**
     * Job parameters count
     */
//    public static final int GJOB_PARAMS_COUNT  = 10; 
    
    
    /**
     * Job parameters
     */
//    protected String [] myParams;
    
    /**
     * Job allocated hosts
     */
//    protected String [] myHosts;
    
    public GridJob(){
    	parametersDescriptions = new HashMap<String, String>();
    	parametersValues = new HashMap<String, String>();
    	parametersDescriptions.put(KEY_GRID_JOB_HOSTS, DESCRIPTION_GRID_JOB_HOSTS);
    	parametersDescriptions.put(KEY_GRID_JOB_ID, DESCRIPTION_GRID_JOB_ID);
    	parametersDescriptions.put(KEY_GRID_JOB_OWNER, DESCRIPTION_GRID_JOB_OWNER);
    	parametersDescriptions.put(KEY_GRID_JOB_QUEUE, DESCRIPTION_GRID_JOB_QUEUE);
    	parametersDescriptions.put(KEY_GRID_JOB_RESOURCES_COUNT, DESCRIPTION_GRID_JOB_RESOURCES_COUNT);
    	parametersDescriptions.put(KEY_GRID_JOB_SCHEDTIME, DESCRIPTION_GRID_JOB_SCHEDTIME);
    	parametersDescriptions.put(KEY_GRID_JOB_STARTTIME, DESCRIPTION_GRID_JOB_STARTTIME);
    	parametersDescriptions.put(KEY_GRID_JOB_STATE, DESCRIPTION_GRID_JOB_STATE);
    	parametersDescriptions.put(KEY_GRID_JOB_SUBTIME, DESCRIPTION_GRID_JOB_SUBTIME);
    	parametersDescriptions.put(KEY_GRID_JOB_TYPE, DESCRIPTION_GRID_JOB_TYPE);
    	parametersDescriptions.put(KEY_GRID_JOB_WALLTIME, DESCRIPTION_GRID_JOB_WALLTIME);
    	parametersDescriptions.put(KEY_GRID_JOB_PROPERTIES, DESCRIPTION_GRID_JOB_PROPERTIES);
    	parametersDescriptions.put(KEY_GRID_JOB_LAUNCHING_DIRECTORY, DESCRIPTION_GRID_JOB_LAUNCHING_DIRECTORY);
    	parametersDescriptions.put(KEY_GRID_JOB_RESERVATION, DESCRIPTION_GRID_JOB_RESERVATION);
    	parametersDescriptions.put(KEY_GRID_JOB_COMMAND, DESCRIPTION_GRID_JOB_COMMAND);
    }
    
    /**
     * Initialize the job with a string value
     * @param stringValue
     */
    public abstract void deserialize(String stringValue);
    
    /**
     * Get the job identifier
     * @return the job identifier
     */
    public String getGridJobId(){
    	return gridJobId;
    }
    
    public void setId(String id){
    	gridJobId = id;
    }
    
    /**
     * Get the job owner
     * @return the job owner or null if not defined
     */
 //   public abstract String getOwner();
    
    /**
     * Get a job parameter
     * @param param_idx parameter index
     * @return the requested job parameter or null if the index is invalid
     */
 //   public abstract String getParam(int param_idx);
    
    /**
     * Get the job state
     * @return the job state or null if not defined
     */
 //   public abstract String getState();
    
    /**
     * Return the job parameters as an array of String
     * @return the jobs parameters
     */
 //   public abstract String [] getValues();
    
    /**
     * Set a job parameter
     * @param param_idx parameter index
     * @param value parameter value
     */
 //   public abstract void setParam(int param_idx, String value);


    /**
     * Return an array of allocated resources names
     * 
     * @return an array of host
     */
 /*   public String[] getHosts() {
        return this.myHosts;
    }*/

    /**
     * Set the job allocated hosts
     * 
     * @param hosts allocated hosts
     */
 /*   public void setHosts(String [] hosts) {
        this.myHosts = hosts;
    }*/
    
    /**
     * Get the job host as a string. 
     * 
     *  @param separator
     *  @return the hosts list as a single string
     */
 /*   public String getHostsAsString(String separator) {
        String h = "";
        for (int ix=0; ix<this.myHosts.length-1; ix++)
            h += this.myHosts[ix] + separator;
        
        h+=this.myHosts[this.myHosts.length-1];
        return h;
    }
    */
    
    /**
     * 
     * @param job
     */
    public void update(GridJob job) {
//        for (int ix=0; ix<this.myParams.length; ix++)
//            this.myParams[ix] = job.getParam(ix);
//        this.myHosts = job.getHosts();
        
        Iterator<String> iterator = job.parametersValues.keySet().iterator();
        if(parametersValues == null) parametersValues = new HashMap<String, String>();
        while(iterator.hasNext()){
        	String key = iterator.next();
        	String value = job.parametersValues.get(key);
        	parametersValues.put(key, value);
        }
    }
    /**
     * Return an XML representation of the Grid job 
     */
    public String toXml() {
//        String xml = "<GridJob " + newline;
//        for (int ix=0; ix<GridJob.GJOB_PARAMS_COUNT; ix++) {
//            xml = xml + "\t" + paramDescr[ix] + "=\"" + this.myParams[ix] + "\" " + newline;
//        }
//        xml += "hosts=\"" + getHostsAsString(" + ") + "\"";
//        xml += "\t/>"; 
    	String xml = "<GridJob " + newline;
    	Iterator<String> iterator = parametersValues.keySet().iterator();
    	while(iterator.hasNext()){
    		String key = iterator.next();
    		String value = parametersValues.get(key);
    		xml += "\t" + key + "=\"" + value + "\" " + newline;
    	}
    	xml+="\t>";
        return xml;
    }
    
    public GridJob copy(){
    	GridJob aJob = null;
    	try{ 
    		aJob = (GridJob)Class.forName(getClass().getName()).newInstance();
    		Iterator<String> iterator = parametersValues.keySet().iterator();
    		while(iterator.hasNext()){
    			String key = iterator.next();
    			String value = parametersValues.get(key);
    			String description = parametersDescriptions.get(key);
    			aJob.parametersValues.put(key, new String(value));
    			aJob.parametersDescriptions.put(key, new String(description));
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return aJob;
    }

    public String toString() {
//        String result = "";
//        result += this.myParams[GridJob.GJOB_ID] +"\n";
//        result += this.myParams[GridJob.GJOB_OWNER] + "\n";
//        result += this.myParams[GridJob.GJOB_STATE] + "\n";
//        // result +=jobComment+"\n";
//        result += this.getHostsAsString("+") +"\n";
//        result += this.myParams[GridJob.GJOB_QUEUE] + "\n";
//        result += this.myParams[GridJob.GJOB_RES_COUNT] + "\n";
//        // result += jobWeight + "\n";
//        // result +=jobCommand+"\n";
//        // result +=jobLaunchingDir+"\n";
//        // result +=jobType+"\n";
//        // result +=jobProperties+"\n";
//        // result +=jobReservation+"\n";
//        result += this.myParams[GridJob.GJOB_WALLTIME] + "\n";
//        result += this.myParams[GridJob.GJOB_SUBTIME] + "\n";
//        result += this.myParams[GridJob.GJOB_STARTTIME] + "\n";
//        result += this.myParams[GridJob.GJOB_SCHEDTIME];
    	String result = "";
    	Iterator<String> iterator = parametersValues.keySet().iterator();
    	String key = null;
    	String value = null;
    	String description = null;
    	while(iterator.hasNext()){
    		key = iterator.next();
    		value = parametersValues.get(key);
    		description = parametersDescriptions.get(key);
    		result += description+ " : " + value + "\n";
    	}
        return result;
    }
    
    public String[] getHostsAsArray(){
    	String[] hosts = null;
    	StringTokenizer tokenizer = new StringTokenizer(getParameterValue(KEY_GRID_JOB_HOSTS),HOSTS_SEPARATOR);
    	hosts = new String[tokenizer.countTokens()];
    	int i = 0;
    	while(tokenizer.hasMoreTokens()){
    		hosts[i] = tokenizer.nextToken();
    		i++;
    	}
    	return hosts;
    }
    
    public String getHostsAsString(String separator){
    	return getParameterValue(KEY_GRID_JOB_HOSTS).replace(HOSTS_SEPARATOR, separator);
    }
    
    public void setHostsFromArray(String[] hostsArray){
    	String hosts = "";
    	if(hostsArray.length >0){
    		for(int i = 0 ; i < hostsArray.length-1 ; i++){
    			hosts+= hostsArray[i] + HOSTS_SEPARATOR;
    		}
    		hosts+= hostsArray[hostsArray.length-1];
    	}
    	setParameterValue(KEY_GRID_JOB_HOSTS, hosts);
    }
    
    public void setParameterValue(String key, String value){
    	parametersValues.put(key, value);
    }
    
    public String getParameterValue(String key){
    	String retour = parametersValues.get(key);
    	if(retour == null) return "";
    	else return retour;
    }
    
    public void setParameterDescription(String key, String value){
    	parametersDescriptions.put(key, value);
    }
    
    public String getParameterDescription(String key){
    	String retour = parametersDescriptions.get(key);
    	if(retour == null) return "";
    	else return retour;
    }
    
    public Set<String> getKeys(){
    	return parametersValues.keySet();
    }

    /**
     * New line
     */
    public static String newline = System.getProperty("line.separator");

    public abstract String getJobConnectionCommand();
    
    public int getParametersValuesCount(){
    	return parametersValues.size();
    }
    
    public String getKeyOfDesc(String desc) {
        if (this.parametersDescriptions.containsValue(desc)) {
            Set<String> keys = this.parametersDescriptions.keySet();
            Iterator<String> p = keys.iterator(); 
            while (p.hasNext()) {
                String k = p.next();
                if (this.parametersDescriptions.get(k).equals(desc))
                    return k;
            }
        }
        return null;
    }
}
