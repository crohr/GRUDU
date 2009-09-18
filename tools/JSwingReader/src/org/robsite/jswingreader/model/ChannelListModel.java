/*
 * $Id: ChannelListModel.java,v 1.1 2007/07/13 09:59:40 dloureir Exp $
 */
package org.robsite.jswingreader.model;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Collection;

import javax.swing.AbstractListModel;


public class ChannelListModel extends AbstractListModel
{
  /**
   * The Channels model delegate
   */
  private Channels _channels;

  
  public ChannelListModel()
  {
    _channels = new Channels();
  }


  public ChannelListModel( Collection channels )
  {
    _channels.getChannelsList().addAll( channels );
    fireIntervalAdded( this, 0, getSize() );
  }

  public int getSize()
  {
    return _channels.getChannelsList().size();
  }
  

  public Object getElementAt( int index )
  {
    return _channels.getChannelsList().get( index );
  }
  
  
  /**
   * Adds {@link Channel} to the model.
   * @param channel The Adds {@link Channel} to add
   */
  public void addChannel( Channel channel )
  {
    int size = _channels.getChannelsList().size();
    _channels.getChannelsList().add( channel );
    fireIntervalAdded( this, size, size );
  }


  /**
   * Removed {@link Channel} from the model.
   * 
   * @param channel The {@link Channel} to be removed.
   * @return <code>true<code> if the {@link Channel} existed in the model
   */
  public boolean removeChannel( Channel channel )
  {
    int index = _channels.getChannelsList().indexOf( channel );
    if ( index > -1 )
    {
      _channels.getChannelsList().remove( index );
      fireIntervalRemoved( this, index, index );
      return true;
    }
    return false;
  }
  
  
  public void replaceChannelAt( int index, Channel channel )
  {
    _channels.getChannelsList().remove( index );
    fireIntervalRemoved( this, index, index );
    _channels.getChannelsList().add( index, channel );  
    fireIntervalAdded( this, index, index );
  }
  
  
  public void removeChannelAt( int index )
  {
    _channels.getChannelsList().remove( index );
    fireIntervalRemoved( this, index, index );
  }
  
  
  public void clear()
  {
    int index = _channels.getChannelsList().size() - 1;
    _channels.getChannelsList().clear();
    
    if ( index >= 0 )
    {
      fireIntervalRemoved( this, 0, index );
    }
  }
  
  
  public Channels getChannels()
  {
    return _channels;
  }
  
  
  public void setChannels( Channels channels )
  {
    clear();
    _channels = channels;
    int index = _channels.getChannelsList().size();
    fireIntervalAdded( this, 0, index );
  }
  
  
  // Serialization
  public void load( InputStream inStream ) throws IOException 
  {
    XMLDecoder decoder = new XMLDecoder( inStream );
    Object o = decoder.readObject();
    decoder.close();
    
    if ( o instanceof Channels )
    {
      Channels channels = ( Channels ) o;
      setChannels( channels );
    }
  }
  
  
  public void save( OutputStream out ) throws IOException
  {
    XMLEncoder encoder = new XMLEncoder( out );
    encoder.writeObject( getChannels() );
    encoder.close();
  }
  
  
  public static void main(String[] args)
  {
    ChannelListModel model = new ChannelListModel();
    Channel c = new Channel();
    c.setTitle( "One" );
    model.addChannel( c );
    c = new Channel();
    c.setTitle( "Two" );
    model.addChannel( c );
    c = new Channel();
    c.setTitle( "Three" );
    model.addChannel( c );
    
    Channel[] channels = model.getChannels().getChannels();
    int i = 0;
    
    for( i=0; i < channels.length; i++ )
    {
      System.out.println( channels[i].getTitle() );
    }
    
    System.out.println( "***************************************" );
    
    c = new Channel();
    c.setTitle( "Two2" );
    model.replaceChannelAt( 1, c );
    channels = model.getChannels().getChannels();
    
    for( i=0; i < channels.length; i++ )
    {
      System.out.println( channels[i].getTitle() );
    }
    
    
  }
  
  
  
}