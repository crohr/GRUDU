/*
 * $Id: Item.java,v 1.1 2007/07/13 09:59:40 dloureir Exp $
 */
package org.robsite.jswingreader.model;

import java.util.Date;


public class Item 
{
  private String _title;
  private String _link;
  private String _description;
  private boolean _read;
  private Date _pubDate;
  private String _guid;
  
  
  public Item()
  {
  }
  
  public String getGuid()
  {
    if ( _guid == null )
    {
      return getLink();
    }
    return _guid;
  }
  
  public void setGuid( String guid )
  {
    _guid = guid;
  }

  public void setPublishDate( Date date )
  {
    _pubDate = date;
  }
  
  public Date getPublishDate()
  {
    return _pubDate;
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
  
  
  public void setRead( boolean read )
  {
    _read = read;
  }
  
  
  public boolean isRead()
  {
    return _read;  
  }
  

  public String dumpString()
  {
    return "[Item] {PubDate: " + String.valueOf( _pubDate ) + "} {Title: " + _title + "} {Link: " + _link + "} {Description: " + _description + "}\n";
  }


  public String toString()
  {
    if ( _title == null || _title.length() == 0 )
    {
      if ( _description == null || _description.length() == 0 )
      {
        return "(none)";
      }
      return _description.substring( 0, 60 );
    }
    return _title;
  }


  public int hashCode()
  {
    if ( _link != null )
    {
      return _link.hashCode();
    }
    if ( _title != null )
    {
      return _title.hashCode();
    }
    return super.hashCode();
  }


  public boolean equals( Object obj )
  {
    if ( obj instanceof Item )
    {
      Item other = ( Item ) obj;
      if ( _link != null && other._link != null )
      {
        return _link.equals( other._link );
      }
      else if ( _title != null && other._title != null )
      {
        return _title.equals( other._title );
      }
    }
    return false;
  }

}