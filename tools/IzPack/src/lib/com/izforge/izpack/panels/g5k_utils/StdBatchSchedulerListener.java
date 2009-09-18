/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: StdBatchSchedulerListener.java,v 1.1 2007/10/30 10:25:22 dloureir Exp $
 * $Log: StdBatchSchedulerListener.java,v $
 * Revision 1.1  2007/10/30 10:25:22  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.1  2007/09/28 16:02:07  aamar
 * Initial revision
 *
 ****************************************************************************/
package com.izforge.izpack.panels.g5k_utils;

public class StdBatchSchedulerListener implements BatchSchedulerListener {
    /**
     * New line
     */
    public static String newline = System.getProperty("line.separator");

    public void addOutput(String msg) {
        System.out.println(msg + newline);
    }
    
    public void addError(String msg) {
        System.err.println(msg + newline);
    }
}
