/*
 * $Id: PreferencesPage.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.ui.prefs;


import java.awt.Component;
import java.util.Map;
import javax.swing.Icon;


public interface PreferencesPage 
{
  Component getPage();
  String getName();
  Icon getIcon();
  void onEntry();
  void onExit();
  
  Map getProperties();
  void setProperties( Map map );
  Object getKey();
}