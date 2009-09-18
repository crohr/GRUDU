/*
 * $Id: ProxyPage.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.ui.prefs;


import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.robsite.jswingreader.ui.Main;


public class ProxyPage extends JPanel implements PreferencesPage 
{
  private final static Icon _icon = new ImageIcon( Main.class.getResource( "image/PrefsProxy.gif" ) );
  private JCheckBox chkUseProxy = new JCheckBox();
  private JLabel lblProxyPort = new JLabel();
  private JTextField taProxyPort = new JTextField();
  private JLabel lblProxyName = new JLabel();
  private JTextField taProxyHostName = new JTextField();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();


  public ProxyPage()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }


  public Component getPage()
  {
    return this;
  }


  public String getName()
  {
    return "Proxy Server";
  }
  
  
  public Map getProperties()
  {
    HashMap map = new HashMap();
    map.put( "enabled", Boolean.toString( chkUseProxy.isSelected() ) );
    map.put( "proxyHost", taProxyHostName.getText() );
    map.put( "proxyPort", taProxyPort.getText() );
    return map;
  }
  
  
  public void setProperties( Map map )
  {
    String value = ( String ) map.get( "enabled" );
    chkUseProxy.setSelected( Boolean.valueOf( value ).booleanValue() );
    taProxyHostName.setText(  ( String ) map.get( "proxyHost" ) );
    taProxyPort.setText( ( String ) map.get( "proxyPort" ) );
    _updateProxyEnabledState();
  }
  
  
  public Object getKey()
  {
    return "proxy";  
  }


  public Icon getIcon()
  {
    return _icon;
  }


  public void onEntry()
  {
  }


  public void onExit()
  {
  }


  private void jbInit() throws Exception
  {
    this.setLayout(gridBagLayout1);
    chkUseProxy.setText("Use Proxy Server");
    chkUseProxy.addChangeListener(new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          _updateProxyEnabledState();
        }
      });
    lblProxyPort.setText("HTTP Proxy Server Port Number:");
    lblProxyName.setText("HTTP Proxy Server Host Name:");
    this.add(chkUseProxy, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.add(lblProxyName, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 5, 5), 0, 0));
    this.add(taProxyHostName, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 5, 5), 0, 0));
    this.add(lblProxyPort, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 5, 5), 0, 0));
    this.add(taProxyPort, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 5, 5), 0, 0));
  }

  
  private void _updateProxyEnabledState()
  {
    taProxyHostName.setEnabled( chkUseProxy.isSelected() );
    taProxyPort.setEnabled( chkUseProxy.isSelected() );

    if ( chkUseProxy.isSelected() )
    {
      System.setProperty( "http.proxyHost", taProxyHostName.getText() );
      System.setProperty( "http.proxyPort", taProxyPort.getText() );
    }
    else
    {
      System.getProperties().remove( "http.proxyHost" );
      System.getProperties().remove( "http.ProxyPort" );
    }
  }


}