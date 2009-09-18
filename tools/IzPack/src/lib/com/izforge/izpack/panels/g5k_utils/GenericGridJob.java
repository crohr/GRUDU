/****************************************************************************/
/* Generic grid job                                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: GenericGridJob.java,v 1.1 2007/10/30 10:25:23 dloureir Exp $
 * $Log: GenericGridJob.java,v $
 * Revision 1.1  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.3  2007/10/23 14:44:53  aamar
 * Correction job parameters setting when creating a GenericGridJob
 *
 * Revision 1.2  2007/10/08 14:53:58  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.1  2007/10/06 09:36:41  aamar
 * Initial revision
 *
 ****************************************************************************/
/**
 * 
 */
package com.izforge.izpack.panels.g5k_utils;

import java.util.*;

/**
 * @author aamar
 *
 */
public class GenericGridJob extends GridJob {

    /* (non-Javadoc)
     * @see diet.grid.api.GridJob#copy()
     */
//    @Override
//    public GridJob copy() {
//        // TODO Auto-generated method stub
//        return null;
//    }

    /* (non-Javadoc)
     * @see diet.grid.api.GridJob#deserialize(java.lang.String)
     */
    @Override
    public void deserialize(String stringValue) {
        // TODO Auto-generated method stub

    }

//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getId()
//     */
//    @Override
//    public String getId() {
//        return "Generic Job";
//    }

//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getOwner()
//     */
//    @Override
//    public String getOwner() {
//        return this.myParams[GridJob.GJOB_OWNER];
//    }

//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getParam(int)
//     */
//    @Override
//    public String getParam(int param_idx) {
//        if (param_idx < 0 || param_idx>= GridJob.GJOB_PARAMS_COUNT)
//            return null;
//        return this.myParams[param_idx];
//    }

//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getState()
//     */
//    @Override
//    public String getState() {
//        return this.myParams[GridJob.GJOB_STATE];
//    }
//
//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#getValues()
//     */
//    @Override
//    public String[] getValues() {
//        // TODO Auto-generated method stub
//        return null;
//    }

//    /* (non-Javadoc)
//     * @see diet.grid.api.GridJob#setParam(int, java.lang.String)
//     */
//    @Override
//    public void setParam(int param_idx, String value) {
//        if ( (param_idx < 0) || (param_idx>= GridJob.GJOB_PARAMS_COUNT) )
//            return;
//        this.myParams[param_idx] = value;
//    }

    /**
     * Default constructor of an Generic Job from an array of lines
     *
     * @param lines String describing the job
     */
    public GenericGridJob(String aString) {
    	super();
//        this.myParams = new String[GridJob.GJOB_PARAMS_COUNT];
//        for (int ix=0; ix<this.myParams.length; ix++)
//            this.myParams[ix] = "";
//        this.myHosts = new String[0];
    	setId("Generic Job");
        StringTokenizer cutter = new StringTokenizer(aString,"\n");
        
        while (cutter.hasMoreTokens()) {
            String token = cutter.nextToken();
            int pos = token.indexOf(":");
            if (pos != -1) {
                String desc = token.substring(0, pos-1).trim();
                String value = token.substring(pos+1).trim();
                String key = this.getKeyOfDesc(desc);
                if (key != null) {
                    this.setParameterValue(key, value);
                    System.out.println("|" + key + "| " + value);
                }
            }
        }
        
        
/*        String tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_ID, tac);
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_OWNER,tac);
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_STATE, tac);
        tac = cutter.nextToken();
        setHostsFromArray(tac.split("\\+"));
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_QUEUE,tac);
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_RESOURCES_COUNT, tac);
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_WALLTIME,tac);
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_SUBTIME,tac);
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_STARTTIME,tac);
        tac = cutter.nextToken();
        setParameterValue(KEY_GRID_JOB_SCHEDTIME,tac);
*/
    }

	/* (non-Javadoc)
	 * @see diet.grid.api.GridJob#getJobConnectionCommand()
	 */
	@Override
	public String getJobConnectionCommand() {
		// TODO Auto-generated method stub
		return null;
	}

}
