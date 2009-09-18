/*
 * $Id: EditRSSFeedsAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;


import java.awt.Component;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.robsite.jswingreader.model.Channel;
import org.robsite.jswingreader.model.ChannelListModel;
import org.robsite.jswingreader.model.SimpleRSSParser;
import org.robsite.jswingreader.ui.Main;


/**
 * Edit a new RSS Channel
 */
public class EditRSSFeedsAction extends AbstractAction implements UpdatableAction
{
  private JList _listChannels;


  public EditRSSFeedsAction( JList listChannels )
  {
    super( "Edit" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_E ) );
    this.putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_E, Event.ALT_MASK ) );
    this.putValue( Action.SMALL_ICON, new ImageIcon( Main.class.getResource( "image/Edit16.gif" ) ) );
    this.putValue( Action.LONG_DESCRIPTION, "Edit" );
    _listChannels = listChannels;

  }


  public void actionPerformed(ActionEvent e)
  {
    String message = "Enter URL for RSS Feed";
    Component parent = Main.getMainWindow();
    String urlString = JOptionPane.showInputDialog( parent, message );
    if ( urlString != null && urlString.length() > 0 )
    {
      try 
      {
        Channel channel = SimpleRSSParser.parse( urlString );
        ChannelListModel model = ( ChannelListModel ) _listChannels.getModel();
        model.removeChannelAt( _listChannels.getSelectedIndex() );
        model.addChannel( channel );
        _listChannels.setSelectedValue( channel, true );
      } 
      catch ( Exception ex ) 
      {
        Main.getMainWindow().setStatusBarText( "Unable to open URL: " + urlString );
      } 
    }
  }

  
  public void update( Object o )
  {
    if ( _listChannels == null || _listChannels.getModel().getSize() == 0 )
    {
      setEnabled( false );
    }
    else if ( _listChannels.getSelectedIndex() == -1 )
    {
      setEnabled( false );
    }
    else
    {
      setEnabled( true );
    }
  }
}
