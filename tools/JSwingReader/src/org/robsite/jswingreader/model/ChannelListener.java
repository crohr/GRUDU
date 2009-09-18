/*
 * $Id: ChannelListener.java,v 1.1 2007/07/13 09:59:40 dloureir Exp $
 */
package org.robsite.jswingreader.model;


import java.util.EventListener;


public interface ChannelListener extends EventListener 
{
  void itemsChanged( ChannelEvent ce );
}