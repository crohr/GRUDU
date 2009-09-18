/*
 * $Id: ItemReadTimer.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 */
package org.robsite.jswingreader.ui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.Timer;
import org.robsite.jswingreader.model.Item;


public class ItemReadTimer extends Timer implements ActionListener
{
  private Item _lastItem = null;
  private JList _listChannels = null;
  private JList _listItems = null;
  

  public ItemReadTimer( JList listChannels, JList listItems )
  {
    super( 5000, null );
    _listChannels = listChannels;
    _listItems = listItems;
    
    addActionListener( this );
    setInitialDelay( 5000 );
    setDelay( 5000 );
    setRepeats( false );
    stop();
    
  }
  
  
  public void actionPerformed( ActionEvent e )
  {
    Item curItem = ( Item ) _listItems.getSelectedValue();
    if ( curItem == null )
    {
      _lastItem = null;
      stop();
    }
    else if ( _lastItem == null )
    {
      _lastItem = curItem;
      start();
    }
    else
    {
      if ( _lastItem.equals( curItem ) && !curItem.isRead() )
      {
        curItem.setRead( true );
        _listItems.repaint();
        _listChannels.repaint();
      }
    }
  }
  
  
  public void setLastItem( Item lastItem )
  {
    _lastItem = lastItem;
  }
 
}