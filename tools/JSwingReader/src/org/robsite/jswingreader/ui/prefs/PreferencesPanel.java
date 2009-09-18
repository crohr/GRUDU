/*
 * $Id: PreferencesPanel.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.ui.prefs;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.robsite.jswingreader.ui.Main;


public class PreferencesPanel extends JPanel
{
  private JSplitPane _splitPane = new JSplitPane();
  private JPanel _contentPanel = new JPanel();
  private CardLayout _contentLayout = new CardLayout();
  private BorderLayout _layout = new BorderLayout();
  private JList _categoryList = new JList();
  private JScrollPane _scrollPane = new JScrollPane( _categoryList );


  public PreferencesPanel()
  {
    super();
    try
    {
      jbInit();
      _postInit();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }

  }


  private void jbInit() throws Exception
  {
    _splitPane.setOrientation( JSplitPane.HORIZONTAL_SPLIT );
    _splitPane.setLeftComponent( _scrollPane );
    _splitPane.setRightComponent( _contentPanel );
    _contentPanel.setPreferredSize( new Dimension( 400, 300 ) );
    _contentPanel.setLayout( _contentLayout );
    _scrollPane.setPreferredSize( new Dimension( 125, 300 ) );
    _scrollPane.setMinimumSize( new Dimension( 120, 100 ) );

    this.setLayout( _layout );
    this.add( _splitPane, BorderLayout.CENTER );
  }
  
  
  private void _postInit() throws Exception
  {
    DefaultListModel model = new DefaultListModel();
    _categoryList.setModel( model );
    _categoryList.setCellRenderer( new PrefsItemsRenderer() );
    
    _categoryList.addListSelectionListener( new ListSelectionListener() 
    {
      public void valueChanged( ListSelectionEvent e )
      {
        if ( e.getValueIsAdjusting() == false )
        {
          _pageChanged();
        }
      }
    });

    registerPage( new GeneralPage() );
    registerPage( new ProxyPage() );

    _categoryList.setSelectedIndex( 0 );
  }
  
  
  public void registerPage( PreferencesPage page )
  {
    DefaultListModel model = ( DefaultListModel ) _categoryList.getModel();  
    model.addElement( page );
    //_contentLayout.addLayoutComponent( page.getPage(), page.getName() );
    _contentPanel.add( page.getPage(), page.getName() );
    Map properties = ( Map ) Main.getPreferences().get( page.getKey() );
    if ( properties != null )
    {
      page.setProperties( properties );
    }
  }
  
  
  public void savePreferences()
  {
    int numItems = _categoryList.getModel().getSize();
    for( int i=0; i < numItems; i++ )
    {
      PreferencesPage page = ( PreferencesPage ) _categoryList.getModel().getElementAt( i );
      Main.getPreferences().put( page.getKey(), page.getProperties() );
    }
    Main.savePreferences();
  }
  
  
  private void _pageChanged()
  {
    PreferencesPage  page = ( PreferencesPage ) _categoryList.getSelectedValue();
    if ( page == null )
    {
      return;
    }
    _contentLayout.show( _contentPanel, page.getName() );
    
    
  }

  
  
}

class PrefsItemsRenderer extends DefaultListCellRenderer 
{
  private final static Icon _icon = new ImageIcon( Main.class.getResource( "image/ComposeMail16.gif" ) );
  
  public Component getListCellRendererComponent(
      JList list,
      Object value,
      int index,
      boolean isSelected,
      boolean cellHasFocus)
    {
      JLabel component = ( JLabel ) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
      if ( value instanceof PreferencesPage )
      {
        PreferencesPage page = ( PreferencesPage ) value;
        component.setIcon( page.getIcon() );
        component.setToolTipText( page.getName() );
        component.setText( page.getName() );
        component.setVerticalTextPosition( JLabel.BOTTOM );
        component.setHorizontalTextPosition( JLabel.CENTER );
        component.setHorizontalAlignment( JLabel.CENTER );
      }
      return component;
    }
}

