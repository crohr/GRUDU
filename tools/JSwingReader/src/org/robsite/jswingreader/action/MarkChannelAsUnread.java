/*
 * $Id: MarkChannelAsUnread.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
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


public class MarkChannelAsUnread extends AbstractAction implements UpdatableAction
{
  JList _listChannels = null;
  JList _listItems    = null;

  
  public MarkChannelAsUnread( JList listChannels, JList listItems )
  {
    super( "Mark All as Unread" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_U ) );
    this.putValue( Action.LONG_DESCRIPTION, "Mark All as Unread" );
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
        item.setRead( false );
        
      }
      _listChannels.repaint();
      _listItems.repaint();
    }
  }

  
  public void update( Object o )
  {
    setEnabled( true );
    
    Channel channel = ( Channel ) _listChannels.getSelectedValue();
    if ( channel == null )
    {
      setEnabled( false );
    }
    
    int numItems = channel.getItems().size();
    int numUnread = channel.getUnreadItemCount();
    if ( numItems == numUnread )
    {
      setEnabled( false );
    }
  }
  
}