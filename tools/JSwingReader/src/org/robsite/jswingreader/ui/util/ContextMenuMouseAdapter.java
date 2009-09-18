/*
 * $Id: ContextMenuMouseAdapter.java,v 1.1 2007/07/13 09:59:39 dloureir Exp $
 */
package org.robsite.jswingreader.ui.util;


import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.robsite.jswingreader.action.UpdatableAction;


public class ContextMenuMouseAdapter extends MouseAdapter
{
  JPopupMenu _popup = null;
  
  
  public ContextMenuMouseAdapter( JPopupMenu popup )
  {
    _popup = popup;
  }
  
  
  public void mousePressed( MouseEvent e ) 
  {
      maybeShowPopup( e );
  }
  

  public void mouseReleased( MouseEvent e ) 
  {
      maybeShowPopup( e );
  }
  

  private void maybeShowPopup( MouseEvent e ) 
  {
    if ( e.isPopupTrigger() ) 
    {
      Object source = e.getSource();
      if ( source instanceof JList )
      {
        JList list = ( JList ) source;
        int index = list.locationToIndex( e.getPoint() );
        if ( index == -1 )
        {
          return;
        }
        list.setSelectedIndex( index );
      }    
      Component[] components = _popup.getComponents();
      for( int i=0; i < components.length; i++ )
      {
        Component c = components[i];
        if ( c instanceof JMenuItem )
        {
          JMenuItem m = ( JMenuItem ) c;
          Action a = m.getAction();
          if ( a instanceof UpdatableAction )
          {
            UpdatableAction u = ( UpdatableAction ) a;
            u.update( this );
          }
        }
      }
      _popup.show( e.getComponent(), e.getX(), e.getY() );

    }
  }  
}