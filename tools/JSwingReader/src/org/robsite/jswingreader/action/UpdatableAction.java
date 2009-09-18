/*
 * $Id: UpdatableAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;

import javax.swing.Action;


/**
 *  Action which is told when to update it's internal state.
 */
public interface UpdatableAction extends Action
{
  /**
   * Causes this action to update it's state (enabled, icon, etc)
   * 
   * @param source The object calling this method
   */
  void update( Object source );
}