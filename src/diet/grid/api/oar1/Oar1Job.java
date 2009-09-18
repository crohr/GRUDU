/****************************************************************************/
/* OAR1 job class                                                           */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: Oar1Job.java,v 1.3 2007/11/05 13:58:40 dloureir Exp $
 * $Log: Oar1Job.java,v $
 * Revision 1.3  2007/11/05 13:58:40  dloureir
 * Adding methods in order to get the RSH and RCP methods for the different possible types of jobs
 *
 * Revision 1.2  2007/10/08 14:53:57  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.1  2007/09/28 16:00:57  aamar
 * Initial revision
 *
 ****************************************************************************/

package diet.grid.api.oar1;

import diet.grid.api.GridJob;

/**
 * OAR1 job class.
 * 
 * @author aamar
 *
 */
public class Oar1Job extends GridJob {
	
	/* (non-Javadoc)
	 * @see diet.grid.api.GridJob#getJobRCPCommand()
	 */
	@Override
	public String getJobRCPCommand() {
		// TODO Auto-generated method stub
		return "scp";
	}

	public static final String KEY_OAR1_JOB_COMMENT = "jobComment";
	public static final String DESCRIPTION_OAR1_JOB_COMMENT = "Comment";
	
	public static final String KEY_OAR1_JOB_WEIGHT = "jobWeight";
	public static final String DESCRIPTION_OAR1_JOB_WEIGHT = "Weight";
	
	public static final String KEY_OAR1_JOB_EVENTS = "jobEvents";
	public static final String DESCRIPTION_OAR1_JOB_EVENTS = "Events";	
	
	/* (non-Javadoc)
	 * @see diet.grid.api.GridJob#getJobConnectionCommand()
	 */
	@Override
	public String getJobConnectionCommand() {
		// TODO Auto-generated method stub
		return "oarsub -I -c";
	}

	/**
	 * Default constructor. Create an OAR job and initialize parameters array
	 *
	 */
    public Oar1Job() {
    	super();
    	setId("OAR 1 Job");
    	parametersDescriptions.put(KEY_OAR1_JOB_COMMENT, DESCRIPTION_OAR1_JOB_COMMENT);
    	parametersDescriptions.put(KEY_OAR1_JOB_WEIGHT, DESCRIPTION_OAR1_JOB_WEIGHT);
    	parametersDescriptions.put(KEY_OAR1_JOB_EVENTS, DESCRIPTION_OAR1_JOB_EVENTS);
//        this.myParams = new String[GridJob.GJOB_PARAMS_COUNT];
//        for (int ix=0; ix<this.myParams.length; ix++)
//            this.myParams[ix] = "";
//        this.myHosts = new String[0];
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
//        return this.myParams[GridJob.GJOB_ID];
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getOwner()
//     */
//    public String getOwner() {
//        return this.myParams[GridJob.GJOB_OWNER];
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getParam(int)
//     */
//    @Override
//    public String getParam(int param_idx) {
//        if (param_idx < 0 || param_idx > GridJob.GJOB_PARAMS_COUNT)
//            return null;
//        return this.myParams[param_idx];
//    }

//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getState()
//     */
//    public String getState() {
//        return this.myParams[GridJob.GJOB_STATE];
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
//        GridJob myCopy = new Oar1Job();
//        for (int ix=0; ix<GridJob.GJOB_PARAMS_COUNT; ix++) {
//            myCopy.setParam(ix, this.getParam(ix));
//        }
//        String [] hosts = new String[this.myHosts.length];
//        for (int ix=0; ix<this.myHosts.length; ix++)
//            hosts[ix] = new String(this.myHosts[ix]);
//        myCopy.setHosts(hosts);
//        return myCopy;
//    }
    
    /* (non-Javadoc)
	 * @see diet.grid.api.GridJob#getJobRSHConnectionCommand()
	 */
	@Override
	public String getJobRSHConnectionCommand() {
		// TODO Auto-generated method stub
		return "ssh";
	}

}
