/*
 * $Id: NullIcon.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 */
package org.robsite.jswingreader.ui;


import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;


/**
 * Blank Icon.
 */
public class NullIcon implements Icon
{
  private int _width;
  private int _height;

  /**
   * Creates a blank icon.
   * 
   * @param size The height and width of the icon
   */
  public NullIcon( int size )
  {
    _width = size;
    _height = size;
  }

  /**
   * Creates a blank icon.
   * 
   * @param width The width of the icon
   * @param height The height of the icon
   */
  public NullIcon( int height, int width )
  {
    _width = width;
    _height = height;
  }


  /**
   * Draw the icon at the specified location.  
   * 
   * This implementation draws nothing.
   */
  public void paintIcon( Component c, Graphics g, int x, int y) 
  {
  }

  
  /**
   * Returns the icon's width.
   *
   * @return an int specifying the fixed width of the icon.
   */
  public int getIconWidth() 
  { 
    return _width;
  }


  /**
   * Returns the icon's height.
   *
   * @return an int specifying the fixed height of the icon.
   */
  public int getIconHeight() 
  { 
    return _height; 
  }
  
}