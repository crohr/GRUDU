/****************************************************************************/
/* This interface specifies the method that should be called when launching */
/* a GuiThread                                                              */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DietGuiTool.java,v 1.2 2007/03/05 13:18:56 dloureir Exp $
 * $Log: DietGuiTool.java,v $
 * Revision 1.2  2007/03/05 13:18:56  dloureir
 * Adding documentation
 *
 ****************************************************************************/
package diet.util.gui;

/**
* This interface specifies the method that should be called when launching
* a GuiThread
*
* $LICENSE$
*
**/
public interface DietGuiTool {
    /**
     * Method that should be called when launching a GuiThread
     *
     */
    public void go();
}
