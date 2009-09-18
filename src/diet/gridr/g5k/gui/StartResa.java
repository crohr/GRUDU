/****************************************************************************/
/* Introduction Dialog for Reservation                                     */
/*                                                                          */
/*  Author(s):                                                              */
/*    - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                       */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: StartResa.java,v 1.5 2007/07/12 15:13:22 dloureir Exp $
 * $Log: StartResa.java,v $
 * Revision 1.5  2007/07/12 15:13:22  dloureir
 * This class is now deprecated
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.3  2006/12/18 13:07:18  dloureir
 * Adding serial version UIDs
 *
 * Revision 1.2  2006/12/10 13:48:59  aamar
 * Some display modifications before the demo
 *
 * Revision 1.1.1.1  2006/12/08 18:11:12  aamar
 * Import DIET DashBoard without svn files and unnecessary files.
 *
 ****************************************************************************/

package diet.gridr.g5k.gui;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

/**
 * Introduction Dialog for Reservation
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 * 
 * @deprecated This class is no more used in GRUDU
 *
 */
public class StartResa extends JDialog implements ActionListener {
    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = 3404479261800457669L;
    /**
     * parent frame
     */
    private JFrame myParent;
    /**
     * next button
     */
    private JButton next;
    /**
     * cancel button
     */
    private JButton cancel;
    /**
     * introduction string
     */
    final static String intro = "The reservation process is available only for use in GRID5000 Platform " + "\n" +
    "Currently, only the Lyon cluster is accessible for reservation";
    /**
     * instance of the StartResa
     */
    private static StartResa instance;
    /**
     * status of the start resa
     */
    private static int status;
    /**
     * Default constructor
     *
     * @param parent parent frame
     */
    private StartResa(JFrame parent) {
        super(parent, "Start a reservation", true);
        this.myParent = parent;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create a panel for the components
        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        JTextArea textArea = new JTextArea(intro);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        topPanel.add(textArea, BorderLayout.CENTER);

        getContentPane().add( topPanel , BorderLayout.CENTER );

        this.next = new JButton("Next");
        this.next.addActionListener(this);
        this.next.setActionCommand("NEXT");
        this.cancel = new JButton("Cancel");
        this.cancel.addActionListener(this);
        this.cancel.setActionCommand("CANCEL");

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        btnPanel.add(this.next);
        btnPanel.add(this.cancel);

        getContentPane().add( btnPanel , BorderLayout.SOUTH );
        pack();
    }

    /**
     * Method used to manage the events for this object
     */
    public void actionPerformed (ActionEvent event) {
        if (event.getActionCommand().equals("NEXT")) {
            status = JOptionPane.OK_OPTION;
            dispose();
        }
        if (event.getActionCommand().equals("CANCEL")) {
            status = JOptionPane.CANCEL_OPTION;
            dispose();
        }
    }

    /**
     * Method used to launch the object
     *
     * @param parent parent frame
     * @return status of the instance
     */
    public static int start(JFrame parent) {
        instance = new StartResa(parent);
        instance.setLocationRelativeTo(null);
        instance.setVisible(true);
        return status;
    }

    /**
     * Method used to return the parent frame
     *
     * @return the myParent the parent frame
     */
    public JFrame getMyParent() {
        return myParent;
    }

    /**
     * Method used to set the parent frame
     *
     * @param myParent the myParent to set
     */
    public void setMyParent(JFrame myParent) {
        this.myParent = myParent;
    }
}
