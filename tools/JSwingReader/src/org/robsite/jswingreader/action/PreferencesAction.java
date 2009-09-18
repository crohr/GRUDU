/*
 * $Id: PreferencesAction.java,v 1.1 2007/07/13 09:59:38 dloureir Exp $
 */
package org.robsite.jswingreader.action;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.robsite.jswingreader.ui.Main;
import org.robsite.jswingreader.ui.common.JSDialog;
import org.robsite.jswingreader.ui.prefs.PreferencesPanel;


public class PreferencesAction  extends AbstractAction implements UpdatableAction
{
  public PreferencesAction()
  {
    super( "Preferences" );
    this.putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_P ) );
    this.putValue( Action.SMALL_ICON, new ImageIcon( Main.class.getResource( "image/Preferences16.gif" ) ) );
    this.putValue( Action.LONG_DESCRIPTION, "Preferences" );
  }
  
  
  public void actionPerformed( ActionEvent e )
  {
    Component parent = Main.getMainWindow();
    PreferencesPanel content = new PreferencesPanel();
    JSDialog.runDialog( parent, content, "Preferences", JOptionPane.OK_CANCEL_OPTION );
    content.savePreferences();
  }

  public void update( Object source )
  {
  }
  
}