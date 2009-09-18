/*
 * $Id: LaunchBrowserOnItem.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;

import org.robsite.jswingreader.model.Item;
import org.robsite.jswingreader.ui.Main;
import org.robsite.jswingreader.util.BrowserUtils;


public class LaunchBrowserOnItem extends AbstractAction implements UpdatableAction
{
  JList _listItems = null;
  
  public LaunchBrowserOnItem( JList listItems )
  {
    super( "Open in Browser" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_O ) );
    this.putValue( Action.LONG_DESCRIPTION, "Open in Browser" );
    _listItems = listItems;
  }

  
  public void actionPerformed( ActionEvent e )
  {
    Object o = _listItems.getSelectedValue();
    if ( o instanceof Item )
    {
      Item item = ( Item ) o;
      item.setRead( true );
      _listItems.repaint();

      try
      {
        URL url = new URL( item.getLink() );
        BrowserUtils.openBrowserOnURL( url );
      }
      catch ( MalformedURLException ex )
      {
        Main.getMainWindow().setStatusBarText( ex.getMessage() );
      }
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
  }
   
  
}