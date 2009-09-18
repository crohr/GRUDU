/****************************************************************************/
/* Utility class to manage oarstat classic call properties                  */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: Oar2StatClassicProperties.java,v 1.2 2007/11/23 14:43:50 dloureir Exp $
 * $Log: Oar2StatClassicProperties.java,v $
 * Revision 1.2  2007/11/23 14:43:50  dloureir
 * Removing the call to the Oar2StatClassicProperties.java because it was no more useful
 *
 * Revision 1.1  2007/11/06 11:18:19  aamar
 * Initial revision
 *
 ****************************************************************************/
package diet.grid.api.oar2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import diet.grid.api.GridJob;


/**
 * 
 * @deprecated this class is no more used for properties recuperation
 * 
 * @author david Loureiro
 *
 */
public final class Oar2StatClassicProperties {

    public final static String job_Id = "Job_Id";
    public final static String name = "name";
    public final static String project = "project";
    public final static String owner = "owner";
    public final static String state = "state";
    public final static String wanted_resources = "wanted_resources";
    public final static String types = "types";
    public final static String assigned_resources = "assigned_resources";
    public final static String assigned_hostnames = "assigned_hostnames";
    public final static String queue = "queue";
    public final static String command = "command";
    public final static String launchingDirectory = "launchingDirectory";
    public final static String jobType = "jobType";
    public final static String properties = "properties";
    public final static String reservation = "reservation";
    public final static String walltime = "walltime";
    public final static String submissionTime = "submissionTime";
    public final static String startTime = "startTime";
    public final static String cpuset_name ="cpuset_name"; 
    public final static String message = "message";
    public final static String scheduledStart = "scheduledStart";
    public final static String resubmit_job_id = "resubmit_job_id";
    public final static String events = "events";

    public final static Map<String, String> myProperties = initProperties();
    
    /**
     * Initialize the properties map
     * @return Map of properties
     */
    private static Map<String, String> initProperties() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Oar2StatClassicProperties.job_Id, GridJob.KEY_GRID_JOB_ID);
        map.put(Oar2StatClassicProperties.owner, GridJob.KEY_GRID_JOB_OWNER);
        map.put(Oar2StatClassicProperties.state, GridJob.KEY_GRID_JOB_STATE);
        // Resource count is not a separate properties but a subproperty of
        // wanted_resources
        // map.put(GridJob.KEY_GRID_JOB_RESOURCES_COUNT, Oar2StatClassicProperties.jobResourcesCount, );
        map.put(Oar2StatClassicProperties.queue, GridJob.KEY_GRID_JOB_QUEUE);
        map.put(Oar2StatClassicProperties.jobType, GridJob.KEY_GRID_JOB_TYPE);
        map.put(Oar2StatClassicProperties.walltime, GridJob.KEY_GRID_JOB_WALLTIME);
        map.put(Oar2StatClassicProperties.submissionTime, GridJob.KEY_GRID_JOB_SUBTIME);
        map.put(Oar2StatClassicProperties.startTime, GridJob.KEY_GRID_JOB_STARTTIME);
        map.put(Oar2StatClassicProperties.scheduledStart, GridJob.KEY_GRID_JOB_SCHEDTIME);
        map.put(Oar2StatClassicProperties.properties, GridJob.KEY_GRID_JOB_PROPERTIES);
        map.put(Oar2StatClassicProperties.reservation, GridJob.KEY_GRID_JOB_RESERVATION);
        map.put(Oar2StatClassicProperties.launchingDirectory, GridJob.KEY_GRID_JOB_LAUNCHING_DIRECTORY);
        map.put(Oar2StatClassicProperties.command, GridJob.KEY_GRID_JOB_COMMAND);
        map.put(Oar2StatClassicProperties.assigned_hostnames, GridJob.KEY_GRID_JOB_HOSTS);
        return map;
    }

    /**
     * Get the properties map key set
     * @return properties key set
     */
    public static ArrayList<String> getProperties() {
        return new ArrayList<String>(myProperties.keySet());
    }
    
    /**
     * Get the property of a given key 
     * @param key property key
     * @return the property value if key is found, null otherwise
     */
    public static String getProperty(String key) {
        if (myProperties.containsKey(key))
            return myProperties.get(key);
        return null;
    }
}
