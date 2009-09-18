/*
 * $Id: AddRSSFeedAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;

import java.awt.Component;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.io.IOException;

import java.net.MalformedURLException;

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
 * Adds a new RSS Channel
 */
public class AddRSSFeedAction extends AbstractAction implements UpdatableAction
{
  private JList _listChannels;
  public AddRSSFeedAction( JList listChannels )
  {
    super( "Add" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_A ) );
    this.putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_A, Event.ALT_MASK ) );
    this.putValue( Action.SMALL_ICON, new ImageIcon( Main.class.getResource( "image/Add16.gif" ) ) );
    this.putValue( Action.LONG_DESCRIPTION, "Add" );
    _listChannels = listChannels;
  }

  public void actionPerformed(ActionEvent e)
  {
    String message = "Enter URL for RSS Feed";
    Component parent = Main.getMainWindow();
    String initialValue = "";
    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable t = c.getContents( this );
    if ( t != null )
    {
      if ( t.isDataFlavorSupported( DataFlavor.stringFlavor ) )
      {
        try
        {
          String s = ( String ) t.getTransferData( DataFlavor.stringFlavor );
          if ( s != null && s.toLowerCase().startsWith("http:") )
          {
            initialValue = s;
          }
        }
        catch (IOException ex)
        {
          // NOP
        }
        catch (UnsupportedFlavorException ex)
        {
          // NOP
        }
      }
    }
    String urlString = JOptionPane.showInputDialog( parent, message, initialValue );
    if ( urlString != null && urlString.length() > 0 )
    {
      try 
      {
        Channel channel = SimpleRSSParser.parse( urlString );
        ChannelListModel model = ( ChannelListModel ) _listChannels.getModel();
        model.addChannel( channel );
        _listChannels.setSelectedValue( channel, true );
      } 
      catch ( MalformedURLException ex ) 
      {
        Main.getMainWindow().setStatusBarText( "Unable to open URL: " + urlString );
      } 
      catch ( Exception ex )
      {
        String text = ex.toString();
        JOptionPane.showMessageDialog( Main.getMainWindow(), text, "JSwingReader Exception", JOptionPane.ERROR_MESSAGE );
      }
    }
  }

  public void update(Object o)
  {
  }
  
}