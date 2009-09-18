/*
 * $Id: BlogContentPane.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 */
package org.robsite.jswingreader.ui;


import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLEditorKit;

import org.robsite.jswingreader.model.Item;
import org.robsite.jswingreader.util.BrowserUtils;


public class BlogContentPane extends JTextPane 
{
  private HTMLEditorKit _editorKit = new HTMLEditorKit();
  
  
  public BlogContentPane()
  {
    super();
    try
    {
      jbInit();
      postInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception
  {
    setEditorKit( _editorKit );
    setEditable( false );
  }


  private void postInit() throws Exception
  {
    this.addHyperlinkListener( new HyperlinkListener() 
    {
      public void hyperlinkUpdate( HyperlinkEvent e )
      {
        if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED )
        {
          BrowserUtils.openBrowserOnURL( e.getURL() );
        }
      }
    });
  }  
  
  /**
   * Sets the text in the text component for the {@link Item} given.
   * 
   * @param item The {@link Item} to display
   */
  public void setBlogText( Item item )
  {
    StringBuffer text = new StringBuffer( 512 );
    if ( item != null && item.getDescription() != null && item.getLink() != null )
    {
      String description = item.getDescription();
      text.append( "<table cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#dddddd\" width=\"100%\" border=\"0\">" );
      text.append( "<tr><td>" );
      text.append( "<strong>" + item.toString() + "</strong><br>" );
      text.append( "<a href=\"" + item.getLink() + "\">" + item.getLink() + "</a>" );
      text.append( "</td></tr>" );
      text.append( "</table><br>" );    
      text.append( description );
    }    
    setText( text.toString() );
    setCaretPosition( 0 );
  }
  
  
  /** Test Main **/
  public static void main(String[] args)
  {
    JFrame f = new JFrame();
    BlogContentPane pane = new BlogContentPane();
    Item item = new Item();
    item.setDescription( "This is the description<br>\nThis is the description<br>\nThis is the description<br>\nThis is the description<br>\nThis is the description<br>\nThis is the description<br>\n" );
    item.setLink( "http://www.robsite.org/somelink.html" );
    item.setTitle( "Blog Title" );
    pane.setBlogText( item );
    f.getContentPane().setLayout( new BorderLayout() );
    f.getContentPane().add( new JScrollPane( pane ), BorderLayout.CENTER );
    f.setSize( 700, 500 );
    f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    f.show();
  }
  
}