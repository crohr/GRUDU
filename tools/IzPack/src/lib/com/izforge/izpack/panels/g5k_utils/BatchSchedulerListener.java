/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: BatchSchedulerListener.java,v 1.1 2007/10/30 10:25:23 dloureir Exp $
 * $Log: BatchSchedulerListener.java,v $
 * Revision 1.1  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.1  2007/09/28 16:02:07  aamar
 * Initial revision
 *
 ****************************************************************************/
package com.izforge.izpack.panels.g5k_utils;

public interface BatchSchedulerListener {

    void addOutput(String msg);
    
    void addError(String msg);
}
