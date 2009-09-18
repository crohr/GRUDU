/****************************************************************************/
/* This class corresponds to a custom Thread that contains a DietGuiTool    */
/* launched thourgh the go() method ran when start() is called              */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: GuiThread.java,v 1.2 2007/03/05 13:18:56 dloureir Exp $
 * $Log: GuiThread.java,v $
 * Revision 1.2  2007/03/05 13:18:56  dloureir
 * Adding documentation
 *
 ****************************************************************************/
package diet.util.gui;

/**
 * This class corresponds to a custom Thread that contains a DietGuiTool
 * launched thourgh the go() method ran when start() is called
 *
 * $LICENSE$
 *
 **/

public class GuiThread extends Thread {

    /**
     * DietGuiTool launched when the star method is called
     */
    private DietGuiTool myGuiTool;

    /**
     * Default constructor that initialized the DietGuiTool
     * that should be run when calling the start method
     *
     * @param guiTool DietGuiTool to run
     */
    public GuiThread(DietGuiTool guiTool) {
        this.myGuiTool = guiTool;
    }

    /**
     * Method that launched the DietGuiTool
     */
    public void start() {
        this.myGuiTool.go();
    }
}
