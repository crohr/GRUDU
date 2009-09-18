/*
 * $Id: GeneralPage.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.ui.prefs;


import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.robsite.jswingreader.ui.Main;


public class GeneralPage extends JPanel implements PreferencesPage
{
  private final static Icon _icon = new ImageIcon( Main.class.getResource( "image/PrefsSystem.gif" ) );
  private JCheckBox chkUseToolbarText = new JCheckBox();
  private JRadioButton radioTextRight = new JRadioButton();
  private JRadioButton radioTextBelow = new JRadioButton();
  private JLabel lblToolbar = new JLabel();
  private GridBagLayout _layout = new GridBagLayout();
  private ButtonGroup _buttonGroup = new ButtonGroup();
  
  
  public GeneralPage()
  {
    try
    {
      jbInit();
      _updateUseTextState();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }  
  }


  private void jbInit() throws Exception
  {
    this.setLayout( _layout );
    chkUseToolbarText.setText( "Display Text on Toolbar Buttons" );
    chkUseToolbarText.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        _updateUseTextState();
      }
    });

    radioTextRight.setText( "Display Text to the Right of Icon" );
    radioTextBelow.setText( "Display Text Below Icon" );
    _buttonGroup.add( radioTextBelow );
    _buttonGroup.add( radioTextRight );
    lblToolbar.setText( "Toolbar Options" );
    this.add( lblToolbar, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets( 5, 5, 5, 5 ), 0, 0 ) );
    this.add( radioTextBelow, new GridBagConstraints( 0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets( 5, 20, 5, 5 ), 0, 0) );
    this.add( radioTextRight, new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets( 5, 20, 5, 5 ), 0, 0) );
    this.add( chkUseToolbarText, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets( 5, 10, 5, 5 ), 0, 0) );
  }


  public Component getPage()
  {
    return this;
  }


  public Icon getIcon()
  {
    return _icon;
  }

  public String getName()
  {
    return "General Preferences";
  }


  public Map getProperties()
  {
    Map map = new HashMap();
    map.put( "useToolBarText", Boolean.toString( chkUseToolbarText.isSelected() ) );
    map.put( "radioTextBelow", Boolean.toString( radioTextBelow.isSelected() ) );
    map.put( "radioTextRight", Boolean.toString( radioTextRight.isSelected() ) );
    return map;
  }
  
  
  public void setProperties( Map map )
  {
    String value = ( String ) map.get( "useToolBarText" );
    chkUseToolbarText.setSelected( Boolean.valueOf( value ).booleanValue() );
    value = ( String ) map.get( "radioTextBelow" );
    radioTextBelow.setSelected( Boolean.valueOf( value ).booleanValue() );
    value = ( String ) map.get( "radioTextRight" );
    radioTextRight.setSelected( Boolean.valueOf( value ).booleanValue() );
  }
  
  
  public Object getKey()
  {
    return "general";
  }


  public void onEntry()
  {
  }


  public void onExit()
  {
  }
  
  
  private void _updateUseTextState()
  {
    Enumeration e = _buttonGroup.getElements();
    while( e.hasMoreElements() )
    {
      AbstractButton b = ( AbstractButton ) e.nextElement();
      b.setEnabled( chkUseToolbarText.isSelected() );
    }
  }
  
}