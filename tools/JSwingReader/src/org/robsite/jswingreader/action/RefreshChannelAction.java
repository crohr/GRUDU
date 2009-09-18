/*
 * $Id: RefreshChannelAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;


import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.net.MalformedURLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.KeyStroke;

import org.robsite.jswingreader.model.Channel;
import org.robsite.jswingreader.model.ChannelListModel;
import org.robsite.jswingreader.model.SimpleRSSParser;
import org.robsite.jswingreader.ui.Main;


public class RefreshChannelAction extends AbstractAction implements UpdatableAction
{
  private JList _listChannels;


  public RefreshChannelAction( JList listChannels )
  {
    super( "Refresh" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_R ) );
    this.putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_D, Event.ALT_MASK ) );
    this.putValue( Action.SMALL_ICON, new ImageIcon( Main.class.getResource( "image/Refresh16.gif" ) ) );
    _listChannels = listChannels;
  }
  
  
  public void actionPerformed( ActionEvent e )
  {
    ChannelListModel model = ( ChannelListModel ) _listChannels.getModel();
    int selectedIndex = _listChannels.getSelectedIndex();
    if ( selectedIndex > -1 )
    {
      try
      {
        Channel channel = ( Channel ) model.getElementAt( selectedIndex );
        SimpleRSSParser.parse( channel );
        model.replaceChannelAt( selectedIndex, channel );
      }
      catch ( MalformedURLException ex )
      {
        String text = ex.toString();
        Main.getMainWindow().setStatusBarText( "Unable to refresh channel " );
      }
      catch ( Exception ex )
      {
        String text = ex.toString();
        Main.getMainWindow().setStatusBarText( "Unable to refresh channel: " + text );
      }
      finally
      {
        _listChannels.setSelectedIndex( selectedIndex );
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