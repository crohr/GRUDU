/****************************************************************************/
/* OAR2 job class                                                           */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: Oar2Job.java,v 1.1 2007/10/30 10:25:23 dloureir Exp $
 * $Log: Oar2Job.java,v $
 * Revision 1.1  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.3  2007/10/08 15:24:21  dloureir
 * Adding the name as a property of an OAR2 job
 *
 * Revision 1.2  2007/10/08 14:53:57  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.1  2007/09/28 16:01:08  aamar
 * Initial revision
 *
 ****************************************************************************/

package com.izforge.izpack.panels.g5k_utils;

/**
 * OAR2 job class
 * @author aamar
 *
 */
public class Oar2Job extends com.izforge.izpack.panels.g5k_utils.GridJob {
    
	public static final String KEY_OAR2_JOB_CPUSET_NAME = "jobCpuset_Name";
	public static final String DESCRIPTION_OAR2_JOB_CPUSET_NAME = "CPU set Name";
	
	public static final String KEY_OAR2_JOB_EXIT_CODE = "jobExitCode";
	public static final String DESCRIPTION_OAR2_JOB_EXIT_CODE = "Exit Code";
	
	public static final String KEY_OAR2_JOB_MESSAGE = "jobMessage";
	public static final String DESCRIPTION_OAR2_JOB_MESSAGE = "Message";
	
	public static final String KEY_OAR2_JOB_PROJECT = "jobProject";
	public static final String DESCRIPTION_OAR2_JOB_PROJECT = "Project";
	
	public static final String KEY_OAR2_JOB_RESUBMIT_JOB_ID = "jobResubmit_Job_Id";
	public static final String DESCRIPTION_OAR2_JOB_RESUBMIT_JOB_ID = "Resubmit Job Id";
	
	public static final String KEY_OAR2_JOB_WANTED_RESOURCES = "jobWantedResources";
	public static final String DESCRIPTION_OAR2_JOB_WANTED_RESOURCES = "Wanted Resources";
	
	public static final String KEY_OAR2_JOB_NAME = "jobName";
	public static final String DESCRIPTION_OAR2_JOB_NAME ="Name";
	
	
	
    /* (non-Javadoc)
	 * @see diet.grid.api.GridJob#getJobConnectionCommand()
	 */
	@Override
	public String getJobConnectionCommand() {
		// TODO Auto-generated method stub
		return "oarsub -C";
	}

	/**
     * Default constructor
     */
    public Oar2Job() {
    	super();
    	setId("OAR2 Job");
    	parametersDescriptions.put(KEY_OAR2_JOB_CPUSET_NAME, DESCRIPTION_OAR2_JOB_CPUSET_NAME);
    	parametersDescriptions.put(KEY_OAR2_JOB_EXIT_CODE, DESCRIPTION_OAR2_JOB_EXIT_CODE);
    	parametersDescriptions.put(KEY_OAR2_JOB_MESSAGE, DESCRIPTION_OAR2_JOB_MESSAGE);
    	parametersDescriptions.put(KEY_OAR2_JOB_PROJECT, DESCRIPTION_OAR2_JOB_PROJECT);
    	parametersDescriptions.put(KEY_OAR2_JOB_RESUBMIT_JOB_ID, DESCRIPTION_OAR2_JOB_RESUBMIT_JOB_ID);
    	parametersDescriptions.put(KEY_OAR2_JOB_WANTED_RESOURCES, DESCRIPTION_OAR2_JOB_WANTED_RESOURCES);
    	parametersDescriptions.put(KEY_OAR2_JOB_NAME, DESCRIPTION_OAR2_JOB_NAME);
    }
    
    /* (non-Javadoc)
     * @see diet.grid.api.GridJob#deserialize(java.lang.String)
     */
    public void deserialize(String stringValue) {
        // TODO Auto-generated method stub
        
    }

//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getId()
//     */
//    public String getId() {
//        if (this.myParams[GridJob.GJOB_ID] != null)
//            return this.myParams[GridJob.GJOB_ID];
//        return "";
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getOwner()
//     */
//    public String getOwner() {
//        if (this.myParams[GridJob.GJOB_OWNER] != null)
//            return this.myParams[GridJob.GJOB_OWNER];
//        return "";
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getParam(int)
//     */
//    @Override
//    public String getParam(int param_idx) {
//        if (param_idx < 0 || param_idx > GridJob.GJOB_PARAMS_COUNT)
//            return null;
//        if (this.myParams[param_idx] != null)
//            return this.myParams[param_idx];
//        return "";
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getState()
//     */
//    public String getState() {
//        if (this.myParams[GridJob.GJOB_STATE] != null)
//            return this.myParams[GridJob.GJOB_STATE];
//        return "";
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getValues()
//     */
//    public String[] getValues() {
//        return this.myParams;
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#setParam(int, java.lang.String)
//     */
//    @Override
//    public void setParam(int param_idx, String value) {
//        if (param_idx > GridJob.GJOB_PARAMS_COUNT)
//            return;
//        this.myParams[param_idx] = value;
//    }

//    public GridJob copy() {
//        GridJob myCopy = new Oar2Job();
//        for (int ix=0; ix<GridJob.GJOB_PARAMS_COUNT; ix++) {
//            myCopy.setParam(ix, this.getParam(ix));
//        }
//        String [] hosts = new String[this.myHosts.length];
//        for (int ix=0; ix<this.myHosts.length; ix++)
//            hosts[ix] = new String(this.myHosts[ix]);
//        myCopy.setHosts(hosts);
//        return myCopy;
//    }
}
