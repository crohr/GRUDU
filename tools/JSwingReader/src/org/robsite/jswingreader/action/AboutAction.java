/*
 * $Id: AboutAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.robsite.jswingreader.ui.Main;
import org.robsite.jswingreader.ui.MainWindow;


/**
 * Displays the About Box
 */
public class AboutAction  extends AbstractAction
{
  /** 
   * Version Number of JSwingReader 
   */
  private static String VERSION_NUMBER = "0.3";
  
  
  public AboutAction()
  {
    super( "About" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_B ) );
    this.putValue( Action.SMALL_ICON, new ImageIcon( Main.class.getResource( "image/About16.gif" ) ) );
    this.putValue( Action.LONG_DESCRIPTION, "About" );
  }


  public void actionPerformed( ActionEvent e )
  {
    final Component parentComponent = Main.getMainWindow();
    String message = MessageFormat.format( "JSwingReader v{0}", new Object[] { AboutAction.VERSION_NUMBER } );
    JOptionPane.showMessageDialog( parentComponent, message );
  } 
}