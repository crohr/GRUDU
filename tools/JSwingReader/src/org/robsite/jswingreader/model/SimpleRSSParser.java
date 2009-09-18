/*
 * $Id: SimpleRSSParser.java,v 1.1 2007/07/13 09:59:40 dloureir Exp $
 */
package org.robsite.jswingreader.model;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.Stack;
import java.util.TimeZone;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is a simple RSS parser. 
 * 
 * @author Robert.Clevenger@oracle.com
 * @author Brian.Duff@oracle.com
 */
public class SimpleRSSParser extends DefaultHandler
{
  private static final String CONTEXT_ITEM = "item";
  private static final String CONTEXT_TEXT = "text";
  
  private static final String DUBLIN_CORE_URI = 
    "http://purl.org/dc/elements/1.1/";
  
  private Channel _channel;
  private final RSSTagContext _context = new RSSTagContext();
  private final Map _handlers = new HashMap();
  
  /**
   * Defines the interface for a tag handler.
   */
  private abstract class RSSTagHandler
  {
    public void handleStartElement( RSSTagContext context ) {}
    public void handleEndElement( RSSTagContext context  ) {}
  }
  
  /**
   * The context passed to tag handlers.
   */
  private class RSSTagContext
  {
    private Map _contextValues = new HashMap();
    private Attributes _attributes;
    private String _uri;
    private String _name;
    private String _qname;
    
    private StringBuffer _text;
    
    /**
     * Gives the path to the current element.
     */
    private Stack _elementPath = new Stack();
    
    
    /**
     * Get the XPath to the current node. 
     * 
     * @return an xpath location identifying the current node's position in
     * the XML document.
     */
    public String getPath()
    {
      StringBuffer pathBuffer = new StringBuffer();
      ArrayList path = new ArrayList( _elementPath );
      pathBuffer.append( "/" );
      for ( Iterator i = path.iterator(); i.hasNext(); )
      {
        pathBuffer.append( String.valueOf( i.next() ) );
        pathBuffer.append( "/" );
      }
      
      return pathBuffer.toString();
    }
    
    /**
     * Get the local name of the parent element, if any. 
     * 
     * @return the local name of the parent element, or the empty string if this
     *    element is the root.
     */
    public String getParentElement()
    {
      if ( !_elementPath.isEmpty() )
      {
        return (String)_elementPath.peek();
      }
      return "";
    }
    
    public void pushPathElement( String name )
    {
      _elementPath.push( name );
    }
    
    public void popPathElement()
    {
      _elementPath.pop();
    }
    
    
    public String getText()
    {
      if ( _text == null ) return null;
      
      return _text.toString();
    }
    
    public void setAttributes( Attributes attributes )
    {
      _attributes = attributes;
    }
    
    public Attributes getAttributes()
    {
      return _attributes;
    }
    
    public void setURI( String uri )
    {
      _uri = uri;
    }
    
    public String getURI()
    {
      return _uri;
    }
    
    public void setName( String name )
    {
      _name = name;
    }
    
    public String getName()
    {
      return _name;
    }
    
    public void setQName( String qname )
    {
      _qname = qname;
    }
    
    public String getQName()
    {
      return _qname;
    }
    
    public Object getValue( Object key )
    {
      return _contextValues.get( key );
    }
    
    public void putValue( Object key, Object value )
    {
      _contextValues.put( key, value );
    }
  }
  
  /**
   * Construct the rss parser for the specified channel.
   * 
   * @param channel
   */
  private SimpleRSSParser( Channel channel )
  {
    if ( channel != null )
    {
      _channel = channel;
    }
    else
    {
      _channel  = new Channel();
    }
    
    registerDefaultHandlers();
  }


  public static Channel parse( String urlString ) throws MalformedURLException, 
    SAXException, ParserConfigurationException, IOException
  {
    return parse( urlString, null );
  }


  public static void parse( Channel channel ) throws MalformedURLException, 
    SAXException, ParserConfigurationException, IOException
  {
    parse( channel.getURL(), channel );
  }


  public static Channel parse( String urlString, Channel channel ) 
    throws MalformedURLException, SAXException, IOException, 
          ParserConfigurationException
  {
    if ( channel == null )
    {
      channel = new Channel();
    }
  
    // If this fails, it'll throw a MalformedURLException
    URL url = new URL( urlString );
    
    URLConnection conn = url.openConnection();
    // Implement conditional HTTP get...
    if ( conn instanceof HttpURLConnection )
    {
      HttpURLConnection connection = (HttpURLConnection) conn;

      if ( channel.getHTTPLastModified() != null )
      {
        connection.setRequestProperty(
          "If-Modified-Since",
          channel.getHTTPLastModified()
        );
      } 
      if ( channel.getHTTPEtag() != null )
      {
        connection.setRequestProperty(
          "If-None-Match",
          channel.getHTTPEtag()
        );
      }
      
      connection.connect();
      
      int responseCode = connection.getResponseCode();
      
      // The rss feed has not been modified.
      if ( responseCode == 304 )
      {
        connection.disconnect();
        return channel;
      }
      else if ( responseCode == 200 ) // OK
      {
        String lastModified = connection.getHeaderField( "Last-Modified" );
        String etag = connection.getHeaderField( "ETag" );
        channel.setHTTPLastModified( lastModified );
        channel.setHTTPEtag( etag );
      }
      else
      {
        throw new IOException(
          "Could not connect to "+urlString+": "+
          responseCode + " " + connection.getResponseMessage()
        );
      }
    }
    


    Channel result = null;
    result = parse( new InputSource( conn.getInputStream() ), channel );
    result.setURL( urlString );
    result.setOpen( true );
    return result;
  }


  private static Channel parse( InputSource inputSource, Channel channel ) 
    throws SAXException, IOException, ParserConfigurationException
  {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    XMLReader reader = parser.getXMLReader();
    SimpleRSSParser handler = new SimpleRSSParser( channel );
    reader.setContentHandler( handler );
    reader.setErrorHandler( handler );

    reader.parse( inputSource  );
    
    return handler.getChannel();
  }

  public Channel getChannel()
  {
    return _channel;
  }
  
  private void registerHandler( String elementName, RSSTagHandler handler )
  {
    _handlers.put( elementName, handler );
  }
  
  private RSSTagHandler lookupHandler( String elementName )
  {
    return (RSSTagHandler) _handlers.get( elementName );
  }

  private void registerDefaultHandlers()
  {
    registerHandler( "item", new RSSTagHandler() 
    {
      public void handleStartElement( RSSTagContext context )
      {
        context.putValue( CONTEXT_ITEM, new Item() );
      }
      
      public void handleEndElement( RSSTagContext context )
      {
        _channel.addItem( 
          (Item) context.getValue( CONTEXT_ITEM ),
          false
        );
        context.putValue( CONTEXT_ITEM, null );
      }
    } );
    
    registerHandler( "description", new RSSTagHandler() 
    {
      public void handleEndElement( RSSTagContext context )
      {
        Item item = (Item) context.getValue( CONTEXT_ITEM );
        if ( item == null )
        {
          _channel.setDescription( context.getText() );
        }
        else
        {
          item.setDescription( context.getText() );
        }
      }
    });
    
    registerHandler( "link", new RSSTagHandler() 
    {
      public void handleEndElement( RSSTagContext context )
      {
        Item item = (Item) context.getValue( CONTEXT_ITEM );
        if ( item == null )
        {
          _channel.setLink( context.getText() );
        }
        else
        {
          item.setLink( context.getText() );
        }
      }
    } );
    
    registerHandler( "title", new RSSTagHandler() 
    {
      public void handleEndElement( RSSTagContext context )
      {
        Item item = (Item) context.getValue( CONTEXT_ITEM );
        // Check that channel is the parent element. Some feeds (e.g. OTN)
        // use <channel><title>..</title><image><title>...</title></image></channel>
        if ( item == null && "channel".equals( context.getParentElement() ))
        {
          _channel.setTitle( context.getText() );
        }
        else if ( item != null )
        {
          item.setTitle( context.getText() );
        }
      }
    } );
    
    registerHandler( "pubDate", new RSSTagHandler() 
    {
      public void handleEndElement( RSSTagContext context )
      {
        Item item = (Item) context.getValue( CONTEXT_ITEM );
        if ( item != null )
        {
          Date d = parseDate( context.getText() );
          if ( d != null )
          {
            item.setPublishDate( d );
          }
        }
      }
    } );
    
    registerHandler( "guid", new RSSTagHandler()
    {
      public void handleEndElement( RSSTagContext context )
      {
        Item item = (Item) context.getValue( CONTEXT_ITEM );
        if ( item != null )
        {
          item.setGuid( context.getText() );
        }
      }
    } );
    
    // Dublin Core dates
    registerHandler( "date", new RSSTagHandler()
    {
      public void handleEndElement( RSSTagContext context )
      {
        Item item = (Item) context.getValue( CONTEXT_ITEM );
        if ( item != null ) 
        {
          if ( DUBLIN_CORE_URI.equals( context.getURI() ) )
          {
            Date date = parseDate( _context.getText() );
            if ( date != null )
            {
              item.setPublishDate( date );
            }
          }
        }
      }
    } );
  }
  
  
  /**
   * Attempt to parse a date. Dates in RSS are pretty non-standard, 
   * unfortunately. The RSS spec calls for RFC822 dates, but in practice, 
   * this isn't followed by all feed generators. Nothing like standards ;)
   * 
   * @param date
   * @return 
   */
  private Date parseDate( String date )
  {
//    Date d = parseRFC822Date( date );
//    if ( d == null )
//    {
      Date d = parseISODate( date );
//    }
    
    return d;
  }
  
  private Date parseISODate( String date )
  {
    try
    {
      // Check the 6th character from the end. If this is + or -, we have 
      // a specific timezone. Otherwise, it should be 'Z', representing UTC
      String formatString = null;
      TimeZone timeZone = null;
      
      // Is it an ISO date without a time?
      if ( date.indexOf( 'T' ) == -1 )
      {
        formatString = "yyyy-MM-dd";
      }
      else
      {
        if ( date.length() >= 7 )
        {        
          char tzChar = date.charAt( date.length() - 6 );
          if ( tzChar == 'Z' )
          {
            date = date.substring( 0, date.length() - 6 );
          }
          else if ( tzChar == '+' || tzChar == '-' )
          {
            String tzId = "GMT" + date.substring( date.length() - 6 );
            timeZone = TimeZone.getTimeZone( tzId );
            date = date.substring( 0, date.length() - 6 );
          }
          
          // If the date contains a comma, it has milliseconds.
          if ( date.indexOf( "," ) >= 0 )
          {
            formatString = "yyyy-MM-dd'T'HH:mm:ss,SSS";
          }
          else
          {
            formatString = "yyyy-MM-dd'T'HH:mm:ss";
          }
        }
      }      
      
      if ( formatString == null )
      {
        throw new Exception( "Date does not parse" );
      }
        
      // Parse a date in the ISO8601 format
      DateFormat f = new SimpleDateFormat( formatString );
      if ( timeZone == null )
      {
        timeZone = TimeZone.getTimeZone( "UTC" );
      }
      f.setTimeZone( timeZone );
      
      return f.parse( date );
    }
    catch ( Exception e )
    {
      return null;
    }
  }

  public void startElement (String uri, String name, String qName, Attributes atts)
  {
    
    _context._text = new StringBuffer();
    _context.setURI( uri );
    _context.setName( name );
    _context.setQName( qName );
    _context.setAttributes( atts );

    final String handlerName = ( name != null && name.length() > 0 ) ? name : qName;
    RSSTagHandler handler = lookupHandler( handlerName );
    if ( handler != null )
    {
      handler.handleStartElement( _context );
    }
    
    _context.pushPathElement( handlerName );
  }
  
  public void endElement (String uri, String name, String qName)
  {
    final String handlerName = ( name != null && name.length() > 0 ) ? name : qName;
    _context.setURI( uri );
    _context.setName( name );
    _context.setQName( qName );
    // This shouldn't be accessible from handleEndElement.
    _context.setAttributes( null );
  
    RSSTagHandler handler = lookupHandler( handlerName );
    _context.popPathElement();    
    if ( handler != null )
    {
      handler.handleEndElement( _context );
    }
    
    _context._text = null;
  }


  public void characters (char ch[], int start, int length)
  {
    if ( _context._text != null )
    {
      _context._text.append( new String( ch, start, length ) );
    }
  }



  public static void main(String[] args)
  {
    try
    {
      Channel channel = null;
      System.out.println( "*** BEGIN OTN NEWS ***" );
      channel = SimpleRSSParser.parse( "http://www.orablogs.com/duffblog/index.xml" );
      System.out.println( channel.dumpString() );
      System.out.println( "*** END OTN NEWS ***" );
      System.out.println( "" );
      System.out.println( "" );

//      System.out.println( "*** BEGIN DIVE INTO BC4J NEWS ***" );
//      channel = SimpleRSSParser.parse( "http://radio.weblogs.com/0118231/rss.xml" );
//      System.out.println( channel.dumpString() );
//      System.out.println( "*** END DIVE INTO BC4J NEWS ***" );
//      System.out.println( "" );
//      System.out.println( "" );

      System.out.println( "*** BEGIN ***" );
      channel = SimpleRSSParser.parse( "http://radio.weblogs.com/0129487/rss.xml" );
      //System.out.println( channel.dumpString() );
      System.out.println( channel.dumpString() );
      System.out.println( "*** END ROBSITE.ORG NEWS ***" );
      System.out.println( "" );
      System.out.println( "" );
    }
    catch ( Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
}