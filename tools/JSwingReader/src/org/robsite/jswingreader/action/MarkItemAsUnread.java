/*
 * $Id: MarkItemAsUnread.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;

import org.robsite.jswingreader.model.Item;


public class MarkItemAsUnread extends AbstractAction implements UpdatableAction
{
  JList _listItems = null;


  public MarkItemAsUnread( JList listItems )
  {
    super( "Mark as Unread" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_U ) );
    this.putValue( Action.LONG_DESCRIPTION, "Mark as Unread" );
    _listItems = listItems;
  }
  
  
  public void actionPerformed( ActionEvent e )
  {
    Object o = _listItems.getSelectedValue();
    if ( o instanceof Item )
    {
      Item item = ( Item ) o;
      item.setRead( false );
      _listItems.repaint();
    }
  }
  
  
  public void update( Object o )
  {
    setEnabled( true );
    if ( _listItems == null || _listItems.getModel().getSize() == 0 )
    {
      setEnabled( false );
    }
    else if ( _listItems.getSelectedIndex() == -1 )
    {
      setEnabled( false );
    }
    else 
    {
      Item item = ( Item ) _listItems.getSelectedValue();
      setEnabled( item.isRead() );
    }
  }
  
  
}