/*
 * $Id: Main.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 * JSwingReader - Swing based RSS NewsFeed Reader
 * <br>
 * See LICENSE for licensing terms.
 */
package org.robsite.jswingreader.ui;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.lang.reflect.Method; 
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.UIManager;
import org.robsite.jswingreader.model.ChannelListModel;
import org.robsite.jswingreader.ui.prefs.PreferencesPanel;


/**
 * Main Application Entry Point.
 */
public class Main 
{
  private static MainWindow _mainWindow;
  private static Map _prefs = null;
  private static ChannelListModel _channelModel = null;


  private static void _showMainWindow()
  {
    _mainWindow = new MainWindow();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = _mainWindow.getSize();
    frameSize.height = Math.min( frameSize.height, screenSize.height );
    frameSize.width = Math.min( frameSize.width, screenSize.width );
    _mainWindow.setLocation( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
    _mainWindow.setVisible( true );
  }
  
  
  public static File getPreferencesDirectory()
  { 
    String prefsDirName = ".diet/jswingreader";
    try
    {
      String userHome = System.getProperty( "user.home" );
      File dir = new File( userHome, prefsDirName );
      return dir;
    }
    catch ( Exception ex )
    {
      ex.printStackTrace();
    }
    return new File( File.listRoots()[0], prefsDirName );
  }


  public static MainWindow getMainWindow()
  {
    return _mainWindow;
  }
  
  
  public static synchronized Map getPreferences()
  {
    if ( _prefs == null )
    {
      _prefs = _loadPreferences();
      if ( _prefs != null && _prefs.size() == 0 )
      {
        // temporary till prefs are moved away from UI
        new PreferencesPanel().savePreferences();
        _prefs = _loadPreferences();
      }
    }
    return _prefs; 
  }
  
  
  public static Map _loadPreferences()
  {
    try
    {
      File prefs = new File( getPreferencesDirectory(), "preferences.xml" );
      FileInputStream in = new java.io.FileInputStream( prefs );
      XMLDecoder decoder = new XMLDecoder( in );
      Object o = decoder.readObject();
      decoder.close();
      
      if ( o instanceof Map )
      {
        Map map = ( Map ) o;
        return map;
      }
    }
    catch ( IOException ex )
    {
      ex.printStackTrace();
    }
    return new HashMap();
  }
  
  
  public static void savePreferences()
  {
    try
    {
      Main.getPreferencesDirectory().mkdirs();
      File prefs = new File( Main.getPreferencesDirectory(), "preferences.xml" );
      FileOutputStream out = new FileOutputStream( prefs );
      XMLEncoder encoder = new XMLEncoder( out );
      encoder.writeObject( getPreferences() );
      encoder.close();
    }
    catch ( IOException ex )
    {
      ex.printStackTrace();
    }
    
  }
  
  
  
  public static synchronized ChannelListModel getChannelModel()
  {
    if ( _channelModel == null )
    {
      _channelModel = new ChannelListModel();
      try 
      {
        File prefs = new File( getPreferencesDirectory(), "channels.xml" );
        FileInputStream in = new FileInputStream( prefs );
        _channelModel.load( in );
        in.close();
      } 
      catch( FileNotFoundException ex )
      {
        // Do nothing, the file was simply not there.
      }
      catch ( Exception ex )
      {
        ex.printStackTrace();
      } 
    }
    return _channelModel;
  }
  
  
  private static void _setProxySettings()
  {
    Map proxy = ( Map ) getPreferences().get( "proxy" );
    if ( proxy != null )
    {
      String enabled = ( String ) proxy.get( "enabled" );
      if ( Boolean.valueOf( enabled ).booleanValue() == true )
      {
        String proxyHost = ( String ) proxy.get( "proxyHost" );
        if ( proxyHost != null )
        {
          System.setProperty( "http.proxyHost", proxyHost );
        }
        String proxyPort = ( String ) proxy.get( "proxyPort" );
        if ( proxyPort != null )
        {
          System.setProperty( "http.proxyPort", proxyPort );
        }
      }
      else
      {
        System.getProperties().remove( "http.proxyHost" );
        System.getProperties().remove( "http.proxyPort" );
      }
    }    
  }


  public static void main( String[] args )
  {
    try
    {
//      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
//      if ( System.getProperty( "os.name", "UNIX" ).startsWith( "Win" ) )
//      {
//        try 
//        {
//          Class c = Class.forName( "net.java.plaf.LookAndFeelPatchManager" );
//          Object o = c.newInstance();
//          Method m = c.getMethod( "initialize", null );
//          m.invoke( o, new Object[0] );
//          //net.java.plaf.LookAndFeelPatchManager.initialize();
//        } 
//        catch (Exception ex) 
//        {
//          //ex.printStackTrace();
//          Logger.global.info( "Not setting WinLaf" );
//        } 
//      }
      System.setProperty( "org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl" );
      
      _setProxySettings();
      _showMainWindow();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
  
  
  
}