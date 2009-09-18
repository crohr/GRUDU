/*
 * $Id: DeleteRSSFeedAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.KeyStroke;

import org.robsite.jswingreader.model.ChannelListModel;
import org.robsite.jswingreader.ui.Main;


/**
 * Deletes the currently selected RSS Channel.
 */
public class DeleteRSSFeedAction  extends AbstractAction implements UpdatableAction
{
  private JList _listChannels;

  public DeleteRSSFeedAction( JList listChannels )
  {
    super( "Delete" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_D ) );
    this.putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_D, Event.ALT_MASK ) );
    this.putValue( Action.SMALL_ICON, new ImageIcon( Main.class.getResource( "image/Delete16.gif" ) ) );
    _listChannels = listChannels;
  }


  public void actionPerformed(ActionEvent e)
  {
    ChannelListModel model = ( ChannelListModel ) _listChannels.getModel();
    model.removeChannelAt( _listChannels.getSelectedIndex() );
    if ( model.getSize() > 0 )
    {
      _listChannels.setSelectedIndex( 0 );
    }
  }

  public void update( Object source )
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