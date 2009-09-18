/****************************************************************************/
/* This class corresponds to the main frame of Grid5000                     */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kFrame.java,v 1.6 2007/03/07 14:13:16 dloureir Exp $
 * $Log: G5kFrame.java,v $
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;

import diet.util.gui.DietGuiTool;

/**
 * This class corresponds to the main frame of Grid5000
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kFrame extends JFrame implements DietGuiTool {
    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = 5079915750480816145L;

    /**
     * Default constructor of the G5kFrame
     *
     */
    public G5kFrame() {
        super("Grid 5000 Console");
        pack();
    }

    /**
     * Method used to launch the frame
     */
    public void go() {
        setSize (750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible (true);

    }
}
