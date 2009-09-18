/*
 * $Id: BrowserUtils.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 */
package org.robsite.jswingreader.util;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.robsite.jswingreader.ui.Main;


public class BrowserUtils 
{
  public BrowserUtils()
  {
  }
  
  
  /**
   * Opens the OS default web browser on <code>url</code>
   * 
   * @param url The URL to send to the browser
   * @todo Implement this for other OS's
   */
  public static boolean openBrowserOnURL( URL url )
  {
    if ( url == null )
    {
      return false;
    }
    
    Main.getMainWindow().setStatusBarText( "Opening " + url.toString() );
    try {
        Desktop.browse(new URL(url.toString()));
        return true;
    } catch(MalformedURLException e) {
        e.printStackTrace();
        return false;
    } catch (DesktopException e) {
        e.printStackTrace();
        return false;
    }
    
//    try 
//    {
//    	
//      Runtime.getRuntime().exec( "cmd /c start " + url.toString() );
//      return true;
//    } 
//    catch ( IOException ex ) 
//    {
//      ex.printStackTrace();
//    } 
//    return false;
  }
  
  
  /** Test Main */
  public static void main(String[] args)
  {
    try 
    {
      openBrowserOnURL( new URL("http://www.oracle.com") );  
    } 
    catch (Exception ex) 
    {
      ex.printStackTrace();
    } 
    
  }
  
}