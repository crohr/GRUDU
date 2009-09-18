/*
 * $Id: MainWindow.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 */
package org.robsite.jswingreader.ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.robsite.jswingreader.action.AboutAction;
import org.robsite.jswingreader.action.AddRSSFeedAction;
import org.robsite.jswingreader.action.DeleteRSSFeedAction;
import org.robsite.jswingreader.action.EditRSSFeedsAction;
import org.robsite.jswingreader.action.ExitAction;
import org.robsite.jswingreader.action.LaunchBrowserOnItem;
import org.robsite.jswingreader.action.MarkChannelAsRead;
import org.robsite.jswingreader.action.MarkChannelAsUnread;
import org.robsite.jswingreader.action.MarkItemAsRead;
import org.robsite.jswingreader.action.MarkItemAsUnread;
import org.robsite.jswingreader.action.PreferencesAction;
import org.robsite.jswingreader.action.RefreshChannelAction;
import org.robsite.jswingreader.action.UpdatableAction;
import org.robsite.jswingreader.model.Channel;
import org.robsite.jswingreader.model.ChannelListModel;
import org.robsite.jswingreader.model.Item;
import org.robsite.jswingreader.model.SimpleRSSParser;
import org.robsite.jswingreader.ui.util.ContextMenuMouseAdapter;


/**
 * Main Application Window
 */
public final class MainWindow extends JFrame 
{
  private JButton buttonAbout = new JButton();
  private JButton buttonRemove = new JButton();
  private JButton buttonAdd = new JButton();
  private JButton buttonEdit = new JButton();
  private JButton buttonRefresh = new JButton();
  private JToolBar toolBar = new JToolBar();
  private JLabel statusBar = new JLabel();
  private JMenuItem menuFileExit = new JMenuItem();
  private JMenuItem menuFileAdd = new JMenuItem();
  private JMenuItem menuFileDel = new JMenuItem();
  private JMenuItem menuFileRefresh = new JMenuItem();
  private JMenuItem menuHelpAbout = new JMenuItem();
  private JMenu menuFile = new JMenu();
  private JMenuItem menuEditPreferences = new JMenuItem();
  private JMenu menuEdit = new JMenu();
  private JMenu menuHelp = new JMenu();
  private JMenuBar menuBar = new JMenuBar();
  private JPanel panelMain = new JPanel();
  private BorderLayout layoutMain = new BorderLayout();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JSplitPane leftRightSplitPane = new JSplitPane();
  private JPanel leftPanel = new JPanel();
  private JPanel rightPanel = new JPanel();
  private JPanel topPanel = new JPanel();
  private JPanel bottomPanel = new JPanel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JSplitPane topBottomSplitPane = new JSplitPane();
  private JList listItems = new JList( new DefaultListModel() );
  
  private BlogContentPane textDescription = new BlogContentPane();
  private BorderLayout borderLayout4 = new BorderLayout();
  private BorderLayout borderLayout5 = new BorderLayout();
  private JList listChannels = new JList();
  private ChannelListModel _channelModel = null;

  private Action addAction = new AddRSSFeedAction( listChannels );
  private Action refreshAction = new RefreshChannelAction( listChannels );
  private Action editAction = new EditRSSFeedsAction( listChannels );
  private UpdatableAction delAction = new DeleteRSSFeedAction( listChannels );
  private UpdatableAction prefsAction = new PreferencesAction();
  private Action aboutAction = new AboutAction(); 
  private Action exitAction = new ExitAction();
  private JMenuItem menuFileEdit = new JMenuItem();
  private JLabel lblChannels = new JLabel();
  private List _updatableActions = new ArrayList();
  private ItemReadTimer _itemTimer = null;
  private JPopupMenu _popupItems = null;
  private JPopupMenu _popupChannels = null;


  MainWindow()
  {
    _channelModel = Main.getChannelModel();
    try
    {
      jbInit();
      postInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }


  private void jbInit() throws Exception
  {
    listChannels.setModel( _channelModel );
    this.setJMenuBar( menuBar );
    this.getContentPane().setLayout( layoutMain );
    panelMain.setLayout( borderLayout1 );
    leftRightSplitPane.setOrientation( JSplitPane.HORIZONTAL_SPLIT );
    leftPanel.setLayout (borderLayout2 );
    rightPanel.setLayout( borderLayout3 );
    topPanel.setLayout( borderLayout5 );
    bottomPanel.setLayout( borderLayout4 );
    topBottomSplitPane.setOrientation( JSplitPane.VERTICAL_SPLIT );
    topBottomSplitPane.setDividerLocation( 100 );
    topBottomSplitPane.setTopComponent( topPanel );
    topBottomSplitPane.setBottomComponent( bottomPanel );
    JScrollPane spTextDescription = new JScrollPane( textDescription );
    textDescription.setText( "" );
    this.setSize( new Dimension( 750, 550 ) );
    this.setTitle( "JSwingReader - RSS News Feed Reader of GRUDU" );
    menuFile.setText( "File" );
    menuEdit.setText( "Edit" );
    menuHelp.setText( "Help" );
    menuFileExit.setAction( exitAction );
    menuFileAdd.setAction( addAction );
    menuFileEdit.setAction( editAction );
    menuFileDel.setAction( delAction );
    menuFileRefresh.setAction( refreshAction );
    menuEditPreferences.setAction( prefsAction );
    menuHelpAbout.setAction( aboutAction );
    statusBar.setText( " [Status] " );
    buttonAdd.setAction( addAction );
    buttonEdit.setAction( editAction );
    buttonRemove.setAction( delAction );
    buttonAbout.setAction( aboutAction );
    buttonRefresh.setAction( refreshAction );
    menuFile.add( menuFileAdd );
    menuFile.add( menuFileDel );
    menuFile.add( menuFileEdit );
    menuFile.addSeparator();
    menuFile.add( menuFileRefresh );
    menuFile.addSeparator();
    menuFile.add( menuFileExit );
    menuEdit.add( menuEditPreferences );
    menuBar.add( menuFile );
    menuBar.add( menuEdit );
    menuHelp.add( menuHelpAbout );
    menuBar.add( menuHelp );
    this.getContentPane().add( statusBar, BorderLayout.SOUTH );
    toolBar.add( buttonAdd );
    toolBar.add( buttonEdit );
    toolBar.add( buttonRemove );
    toolBar.addSeparator();
    toolBar.add( buttonRefresh );
    toolBar.addSeparator();
    toolBar.add( buttonAbout );
    toolBar.setFloatable( false );
    this.getContentPane().add( toolBar, BorderLayout.NORTH );
    leftRightSplitPane.setLeftComponent( leftPanel );
    leftRightSplitPane.setDividerLocation( 200 );
    leftRightSplitPane.setRightComponent( rightPanel );
    panelMain.add( leftRightSplitPane, BorderLayout.CENTER );
    this.getContentPane().add( panelMain, BorderLayout.CENTER );
    rightPanel.add( topBottomSplitPane, BorderLayout.CENTER );
    JScrollPane spListItems = new JScrollPane( listItems );
    listItems.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
    topPanel.add( spListItems, BorderLayout.CENTER );
    bottomPanel.add( spTextDescription, BorderLayout.CENTER );
    listChannels.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
    JScrollPane spListChannels = new JScrollPane( listChannels );
    listItems.addMouseListener( new java.awt.event.MouseAdapter()
      {
        public void mouseClicked( MouseEvent e )
        {
          listItems_mouseClicked( e );
        }
      });
    _popupItems = _buildItemsPopupMenu();
    _popupChannels = _buildChannelsPopupMenu();
    ContextMenuMouseAdapter popupAdapter = new ContextMenuMouseAdapter( _popupItems );
    ContextMenuMouseAdapter popupChannelsAdapter = new ContextMenuMouseAdapter( _popupChannels );
    listItems.addMouseListener( popupAdapter );
    listChannels.addMouseListener( popupChannelsAdapter );
    listItems.setCellRenderer( new ItemsRenderer() );
    listChannels.setCellRenderer( new ChannelsRenderer() );
    menuFileDel.setText( "Delete Channel" );
    menuFileAdd.setText( "Add Channel" );
    menuFileEdit.setText( "Edit Channels" );
    menuFileRefresh.setText( "Refresh Channel" );
    lblChannels.setText( "Channels" );
    leftPanel.add( spListChannels, BorderLayout.CENTER );
    leftPanel.setPreferredSize( new Dimension( 200, 200 ) );
    leftPanel.add( lblChannels, BorderLayout.NORTH );
  }


  private void postInit()
  {
    this.addWindowListener( new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
//          ActionEvent actionEvent = new ActionEvent( this, ActionEvent.ACTION_FIRST, ( String ) exitAction.getValue( Action.NAME ) );
//          exitAction.actionPerformed( actionEvent );
        	dispose();
        }
        public void windowOpened( WindowEvent e )
        {
          listChannels.requestFocusInWindow();
        }
      });
  
    listChannels.addListSelectionListener( new ListSelectionListener() 
    {
      public void valueChanged( ListSelectionEvent e )
      {
        if ( e.getValueIsAdjusting() == false )
        {
          channel_ValueChanged( listChannels, listItems );
        }
      }
    });

    listItems.addListSelectionListener( new ListSelectionListener() 
    {
      public void valueChanged( ListSelectionEvent e )
      {
        if ( e.getValueIsAdjusting() == false )
        {
          _itemsValueChanged( listItems );
        }
      }
    });

    _updatableActions.add( delAction );
    _updatableActions.add( editAction );
    _updatableActions.add( prefsAction );
    _updatableActions.add( refreshAction );

    if ( listChannels.getModel().getSize() > 0 )
    {
      listChannels.setSelectedIndex( 0 );
      listChannels.requestFocus( true );
    }
    
    _updateAllActions();
    listChannels.requestFocusInWindow();
    _updateToolbarButtons();

    _itemTimer = new ItemReadTimer( listChannels, listItems );    
    _itemsValueChanged( listItems );
  }


  private JPopupMenu _buildItemsPopupMenu()
  {
    JPopupMenu popup = new JPopupMenu();
    popup.add( new JMenuItem( new LaunchBrowserOnItem( listItems ) ) );
    popup.addSeparator();
    popup.add( new JMenuItem( new MarkItemAsRead( listItems ) ) );
    popup.add( new JMenuItem( new MarkItemAsUnread( listItems ) ) );
    return popup;
  }


  private JPopupMenu _buildChannelsPopupMenu()
  {
    JPopupMenu popup = new JPopupMenu();
    popup.add( new JMenuItem( new MarkChannelAsRead( listChannels, listItems ) ) );
    popup.add( new JMenuItem( new MarkChannelAsUnread( listChannels, listItems ) ) );
    return popup;
  }


  private void channel_ValueChanged( JList channelList, JList itemList )
  {
    Channel channel = ( Channel ) channelList.getSelectedValue();
    
    if ( channel == null  )
    {
      channel = new Channel();
    }
    if ( !channel.isOpen() && channel.getURL() != null )
    {
      try
      {
        SimpleRSSParser.parse( channel );
      }
      catch ( Exception ex )
      {
        ex.printStackTrace();
      }
    }
    DefaultListModel itemsModel = ( DefaultListModel ) listItems.getModel();
    itemsModel.clear();
    Iterator iter = ( channel.getItems() != null ) ? channel.getItems().iterator() : Collections.EMPTY_LIST.iterator();
    while ( iter.hasNext() )
    {
      itemsModel.addElement( iter.next() );
    }
    if ( itemsModel.getSize() > 0 )
    {
      listItems.setSelectedIndex( 0 );
      _itemsValueChanged( listItems );
    }
    setStatusBarText( channel.getURL() );
    _updateAllActions();
  }


  private void _itemsValueChanged( JList itemList )
  {
    Item item = ( Item ) itemList.getSelectedValue();
    if ( item == null  )
    {
      if ( itemList.getModel().getSize() > 0 )
      {
        item = ( Item )  itemList.getModel().getElementAt( 0 );
      }
      if ( item == null )
      {
        item = new Item();
      }
      else
      {
        itemList.setSelectedIndex( 0 );
      }
    }
    
    if ( _itemTimer != null )
    {
      _itemTimer.start();
      _itemTimer.setLastItem( item );
    }

    setStatusBarText( item.getLink() );
    textDescription.setBlogText( item );
    _updateAllActions();
  }


  public void setStatusBarText( String text )
  {
    statusBar.setText( text );
  }
  
  
  private void _updateAllActions()
  {
    Iterator iter = _updatableActions.iterator();
    while( iter.hasNext() )
    {
      UpdatableAction action = ( UpdatableAction ) iter.next();
      action.update( this );
    }
  }
  
  
  private void _updateToolbarButtons()
  {
    Map general = ( Map ) Main.getPreferences().get( "general" );
    if ( general == null )
    {
      return;
    }
    
    Component[] components = toolBar.getComponents();
    for( int i=0; i < components.length; i++ )
    {
      Component component = components[i];
      if ( component instanceof JButton )
      {
        JButton button = ( JButton ) component;
        if ( Boolean.toString( false ).equals( general.get( "useToolBarText" ) ) )
        {
          // Remove the text if preferences state no toolbar text
          button.setText( "" );
        }
        if ( Boolean.toString( true ).equals( general.get( "radioTextBelow" ) ) )
        {
          button.setVerticalTextPosition( AbstractButton.BOTTOM );
          button.setHorizontalTextPosition( AbstractButton.CENTER );
        }
        else if ( Boolean.toString( true ).equals( general.get( "radioTextRight" ) ) )
        {
          button.setVerticalTextPosition( AbstractButton.CENTER );
          button.setHorizontalTextPosition( AbstractButton.RIGHT );
        }
      }
    }
  }


  private void listItems_mouseClicked( MouseEvent e )
  {
    if ( e.getClickCount() == 2 && e.getModifiersEx() == MouseEvent.NOBUTTON )
    {
      Item item = ( Item ) listItems.getSelectedValue();
      item.setRead( true );
      if ( _itemTimer != null )
      {
        _itemTimer.stop();
      }
      
      Action action = new LaunchBrowserOnItem( listItems );
      ActionEvent event = new ActionEvent( this, ActionEvent.ACTION_PERFORMED, "LaunchBrowserOnItem" );
      action.actionPerformed( event );
    }
  }
}


class ChannelsRenderer extends DefaultListCellRenderer 
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
      component.setIcon( ChannelsRenderer._icon );
      if ( value instanceof Channel )
      {
        Channel channel = ( Channel ) value;
        component.setText( channel.getTitle() + " (" + channel.getUnreadItemCount() + ")" );
        component.setToolTipText( channel.getURL() );
      }
      return component;
    }
}


class ItemsRenderer extends DefaultListCellRenderer 
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
      component.setIcon( ItemsRenderer._icon );
      if ( value instanceof Item )
      {
        Item item = ( Item ) value;
        component.setToolTipText( item.getLink() );
        if ( !item.isRead() )
        {
          component.setFont( component.getFont().deriveFont( Font.BOLD ) );
        }
        else
        {
          component.setFont( component.getFont().deriveFont( Font.PLAIN ) );
        }
      }
      return component;
    }
}

