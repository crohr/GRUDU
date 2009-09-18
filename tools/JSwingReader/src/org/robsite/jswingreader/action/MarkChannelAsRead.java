/*
 * $Id: MarkChannelAsRead.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;

import org.robsite.jswingreader.model.Channel;
import org.robsite.jswingreader.model.Item;


public class MarkChannelAsRead extends AbstractAction implements UpdatableAction
{
  JList _listChannels = null;
  JList _listItems    = null;
  
  public MarkChannelAsRead( JList listChannels, JList listItems )
  { 
    super( "Mark All as Read" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_R ) );
    this.putValue( Action.LONG_DESCRIPTION, "Mark All as Read" );
    _listChannels = listChannels;
    _listItems    = listItems;
  }


  public void actionPerformed( ActionEvent e )
  {
    Object o = _listChannels.getSelectedValue();
    if ( o instanceof Channel )
    {
      Channel channel = ( Channel ) o;
      Iterator iter = channel.getItems().iterator();
      while ( iter.hasNext() )
      {
        Item item = ( Item ) iter.next();
        item.setRead( true );
        
      }
      _listChannels.repaint();
      _listItems.repaint();
    }
  }

  
  public void update( Object o )
  {
    setEnabled( true );
    Channel channel = ( Channel ) _listChannels.getSelectedValue();
    if ( channel == null || channel.getUnreadItemCount() == 0 )
    {
      setEnabled( false );
    }
  }
  
}