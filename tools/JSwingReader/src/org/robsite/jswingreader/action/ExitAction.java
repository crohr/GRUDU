/*
 * $Id: ExitAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;


import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.robsite.jswingreader.model.ChannelListModel;
import org.robsite.jswingreader.ui.Main;
import org.robsite.jswingreader.ui.NullIcon;


/**
 * Exits the application
 */
public class ExitAction extends AbstractAction
{

  public ExitAction()
  {
    super( "Exit" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_X ) );
    this.putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_X, Event.ALT_MASK ) );
    this.putValue( SMALL_ICON, new NullIcon( 16 )  );
  }

  public void actionPerformed( ActionEvent e )
  {
    ChannelListModel channelModel = Main.getChannelModel();
    try 
    {
      Main.getPreferencesDirectory().mkdirs();
      File prefs = new File( Main.getPreferencesDirectory(), "channels.xml" );
      FileOutputStream out = new FileOutputStream( prefs );
      channelModel.save( out );
      out.close();
    } 
    catch ( Exception ex ) 
    {
      ex.printStackTrace();
    } 
    finally
    {
      System.exit( 0 );
    }
  }
  
}