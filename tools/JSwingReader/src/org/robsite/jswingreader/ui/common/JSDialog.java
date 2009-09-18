/*
 * $Id: JSDialog.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 */
package org.robsite.jswingreader.ui.common;


import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;


public class JSDialog
{
  private JSDialog() 
  {
  
  }
  /**
   * 
   * @param parentComponent Parent component
   * @param content Content Pane for the Dialog
   * @param title Dialog Title
   * @param optionType Same as the {@link JOptionPane} optionTypes
   * @return <code>true</code>if OK is selected, <code>false</code> otherwise.
   */
  public static boolean runDialog( Component parentComponent, Component content, String title, int optionType )
  {
    JOptionPane pane = new JOptionPane( content, JOptionPane.PLAIN_MESSAGE, optionType );
    JDialog dialog = pane.createDialog( parentComponent, title );
    dialog.setResizable( true );
    dialog.pack();
    dialog.show();
    Object value = pane.getValue();
    if ( value instanceof Integer )
    {
      Integer integer = ( Integer ) value;
      return ( integer.intValue() == JOptionPane.OK_OPTION ) ? true : false;
    }
    return false;
  }

}