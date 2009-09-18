/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DefaultBatchSchedulerListener.java,v 1.1 2007/10/30 10:25:22 dloureir Exp $
 * $Log: DefaultBatchSchedulerListener.java,v $
 * Revision 1.1  2007/10/30 10:25:22  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.3  2007/10/08 14:53:57  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.2  2007/10/08 08:34:27  dloureir
 * Adding logging features to the default batch scheduler listener
 *
 * Revision 1.1  2007/09/28 16:02:07  aamar
 * Initial revision
 *
 ****************************************************************************/
package com.izforge.izpack.panels.g5k_utils;

import java.util.logging.Level;

public class DefaultBatchSchedulerListener implements BatchSchedulerListener {
    
    public void addOutput(String msg) {
    	
    }
    
    public void addError(String msg) {
    	
    }

}
