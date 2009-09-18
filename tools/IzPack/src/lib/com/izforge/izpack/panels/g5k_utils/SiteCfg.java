/****************************************************************************/
/* Oar utility class                                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: SiteCfg.java,v 1.3 2007/10/30 10:25:23 dloureir Exp $
 * $Log: SiteCfg.java,v $
 * Revision 1.3  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.7  2007/07/12 15:45:41  dloureir
 * Some clean up with commented unused code.
 *
 * Revision 1.6  2007/07/09 14:30:06  dloureir
 * Correction of a javadoc tag and some commented text has been removed
 *
 * Revision 1.5  2007/06/26 15:06:13  dloureir
 * The ways the environment variables are stored has been modified to be compliant with GoDIET (the last version). You can now supply all the variables you want for each sites.
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.3  2007/02/22 14:47:33  aamar
 * Some changes.
 *
 ****************************************************************************/

package com.izforge.izpack.panels.g5k_utils;

import java.util.HashMap;
import java.util.Set;

/**
 * Oar utility class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class SiteCfg {
    
	/**
	 * Default element that must be present in the properties of the siteCfg for the scratch RUNTIME
	 */
	public static final String SCRATCH_RUNTIME = "scratch_runtime";
	
    /**
     * HashMap containing the parameters for a site
     */
    HashMap<String, String> parametersHashMap = new HashMap<String, String>();
    
    /**
     * Return site parameter by key.
     *
     * @param key the key of the parameter to return
     * @return the requested parameter or empty string if the index is incorrect
     */
    public String get(String key) {
    	return parametersHashMap.get(key);
    }
    
    public void set(String key, String value){
    	parametersHashMap.put(key, value);
    }
    
    public Set<String> getKeySet(){
    	return parametersHashMap.keySet();
    }
}