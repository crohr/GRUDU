/*
 * $Id: Channel.java,v 1.1 2007/07/13 09:59:40 dloureir Exp $
 */
package org.robsite.jswingreader.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

public class Channel 
{
  private String  _title;
  private String  _link;
  private String  _description;
  private List    _items;
  private boolean _open;
  private String  _url;
  
  // Implement HTTP conditional get, as documented here:
  // http://fishbowl.pastiche.org/2002/10/21/http_conditional_get_for_rss_hackers
  private String  _httpLastModified;
  private String  _httpEtag;
  
  /**
   * Future flag to indicate a channel added by the user. These won't be
   * automatically deleted when we synch with the channel directory.
   */
  private boolean _isUserChannel;
  
  private ArrayList _channelListeners = new ArrayList( 1 );
  
  
  public Channel()
  {
    _open = false;
  }


  public void setHTTPLastModified( String httpLastModified )
  {
    _httpLastModified = httpLastModified;
  }
  
  public String getHTTPLastModified()
  {
    return _httpLastModified;
  }
  
  public void setHTTPEtag( String httpEtag )
  {
    _httpEtag = httpEtag;
  }
  
  public String getHTTPEtag()
  {
    return _httpEtag;
  }
  
  public boolean isUserChannel()
  {
    return _isUserChannel;
  }
  
  public void setUserChannel( boolean isUserChannel )
  {
    _isUserChannel = isUserChannel;
  }
  
  public boolean equals( Object that )
  {
    if ( that instanceof Channel )
    {
      String thatURL = ((Channel)that).getURL();
      if ( thatURL  != null && this._url != null)
      {
        return this._url.equals( thatURL );
      }
      else
      {
        return this._url == null && thatURL == null;
      }
    }
    
    return false;
  }
  
  public int hashCode()
  {
    if ( _url != null )
    {
      return _url.hashCode();
    }
    return super.hashCode();
  }
  
  public void addChannelListener( ChannelListener cl )
  {
    _channelListeners.add( cl );
  }
  
  public void removeChannelListener( ChannelListener cl )
  {
    _channelListeners.remove( cl );
  }
  
  public void fireItemChanged( Item item )
  {
    final int index = _items.indexOf( item );
    if ( index >= 0 )
    {
      fireItemChanged( index );
    }
  }
  
  
  public void fireItemChanged( final int index )
  {
    // Events should be fired on the event thread
    SwingUtilities.invokeLater( new Runnable() { 
      public void run() 
      {
        // Always dup listener list for safety
        int count = _channelListeners.size();
        if ( count > 0 )
        {
          ChannelListener[] copy = new ChannelListener[ count ];
          _channelListeners.toArray( copy );
          
          ChannelEvent event = new ChannelEvent( Channel.this, index );
          
          for ( int i=0; i < count; i++ )
          {
            copy[ i ].itemsChanged( event );
          }
        }
        
      }
    });
  }
  
  public void fireItemsChanged( Collection items )
  {
    // Could be more efficient
    for ( Iterator i=items.iterator(); i.hasNext(); )
    {
      Item item = (Item) i.next();
      fireItemChanged( item );
    }
  }
  
  
  public void fireItemsChanged( )
  {
    // Events should be fired on the event thread
    SwingUtilities.invokeLater( new Runnable() { 
      public void run() 
      {
        // Always dup listener list for safety
        int count = _channelListeners.size();
        if ( count > 0 )
        {
          ChannelListener[] copy = new ChannelListener[ count ];
          _channelListeners.toArray( copy );
          
          ChannelEvent event = new ChannelEvent( Channel.this );
          
          for ( int i=0; i < count; i++ )
          {
            copy[ i ].itemsChanged( event );
          }
        }
        
      }
    });
  }


  public void setTitle( String title )
  {
    _title = title;
  }


  public String getTitle()
  {
    return _title;
  }


  public void setLink( String link )
  {
    _link = link;
  }


  public String getLink()
  {
    return _link;
  }
  

  public void setDescription( String description )
  {
    _description = description;
  }
  

  public String getDescription()
  {
    return _description;
  }


  public void setItems( List items )
  {
    _items = items;
  }
  
  
  public List getItems()
  {
    return _items;
  }
  
  
  public int getUnreadItemCount()
  {
    if ( _items == null )
    {
      return 0;
    }
    
    int unreadCount = 0;
    Iterator iter = _items.iterator();
    while( iter.hasNext() )
    {
      Item item = ( Item ) iter.next();
      if ( !item.isRead() )
      {
        unreadCount ++;
      }
    }
    return unreadCount;
  }


  public void addItem( Item item, boolean overwrite )
  {
    if ( _items == null )
    {
      _items = new ArrayList( 10 );
    }
    if ( overwrite )
    {
      if ( _items.contains( item ) )
      {
        _items.remove( item );
      }
      _items.add( item );
    }
    else
    {
      if ( !_items.contains( item ) )
      {
        _items.add( item );
      }
    }
  }
  
  
  public void addAllItems( Channel fromChannel )
  {
    Iterator iter = fromChannel.getItems().iterator();
    while( iter.hasNext() )
    {
      addItem( ( Item ) iter.next(), true );
    }
  }


  public String dumpString()
  {
    StringBuffer b = new StringBuffer(1000);
    b.append( "[Channel] {Title: " + _title + "} {Link: " + _link + "} {Description: " + _description + "}\n" );
    Iterator i = ( _items != null ) ? _items.iterator() : Collections.EMPTY_LIST.iterator();
    while ( i.hasNext() )
    {
      Item item = ( Item ) i.next();
      b.append( "  " );
      b.append( item.dumpString() );
    }
    return b.toString();
  }
  

  public String toString()
  {
    if ( _title == null || _title.length() == 0 )
    {
      if ( _description == null || _description.length() == 0 )
      {
        return getURL();
      }
      if ( _description.length() > 60 )
      {
        return _description.substring( 0, 60 );
      }
      return _description;
    }
    return _title;
  }


  public void setOpen( boolean open )
  {
    _open = open;
  }

  public boolean isOpen()
  {
    return _open;
  }
  
  
  public void setURL( String url )
  {
    _url = url;
  }
  
  
  public String getURL()
  {
    return _url;
  }
  
  
}