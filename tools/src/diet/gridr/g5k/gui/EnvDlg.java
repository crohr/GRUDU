/****************************************************************************/
/* This class corresponds to the environment dialog frame                   */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: EnvDlg.java,v 1.6 2007/07/12 12:53:50 dloureir Exp $
 * $Log: EnvDlg.java,v $
 * Revision 1.6  2007/07/12 12:53:50  dloureir
 * This class is now setted as deprecated because it is no more used.
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * This class corresponds to the environment dialog frame
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 * @deprecated This class is no more used
 *
 */
public class EnvDlg extends JDialog implements ActionListener {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = 1248552364152881587L;
    /**
     * ok button
     */
    protected JButton ok;
    /**
     * cancel button
     */
    private JButton cancel;
    /**
     * Panel containing the information on the environment
     */
    protected EnvPanel myEnvPanel;
    /**
     * status for the environment
     */
    protected boolean status;

    /**
     * Default constructor taking the parent fram in argument
     *
     * @param parent parent frame of this dialog frame
     */
    public EnvDlg(JFrame parent) {
        super(parent, "Add a cluster element", true);
        this.status = false;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.myEnvPanel = new EnvPanel();
        getContentPane().add(this.myEnvPanel, BorderLayout.CENTER);
        initBtn();
        setSize(400, 240);
    }

    /**
     * Method initializing the buttons
     *
     */
    void initBtn() {
        this.ok = new JButton("OK");
        this.ok.addActionListener(this);
        this.ok.setActionCommand("OK");
        this.cancel = new JButton("Cancel");
        this.cancel.addActionListener(this);
        this.cancel.setActionCommand("CANCEL");

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        btnPanel.add(this.ok);
        btnPanel.add(this.cancel);

        getContentPane().add( btnPanel , BorderLayout.SOUTH );
    }

    /**
     * Class defining the environment
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class EnvPanel extends JPanel {

        /**
         * serialVersionUID defines the version for the serialisation of
         * this object
         */
        private static final long serialVersionUID = -1255511029659895585L;
        /**
         * JTextField for the path selection
         */
        private JTextField pathEdit;
        /**
         * JTextField for the LD LIBRARY PATH selection
         */
        private JTextField ldEdit;

        /**
         * Default constructor of an EnvPanel
         *
         */
        public EnvPanel() {
            super();
            this.pathEdit = new JTextField();
            this.ldEdit = new JTextField();
            setLayout(new GridLayout(2,2));
            add(new JLabel("PATH "));
            add(this.pathEdit);
            add(new JLabel("LD_LIBRARY_PATH "));
            add(this.ldEdit);
        }

        /**
         * Method returning the path selected
         *
         * @return the path
         */
        public String getPath() {
            return this.pathEdit.getText();
        }

        /**
         * Method returning the LD LIBRARY PATH selected
         *
         * @return the LD LIBRARY PATH
         */
        public String getLd() {
            return this.ldEdit.getText();
        }
    }

    /**
     * Method returning the path selected
     *
     * @return the path
     */
    public String getPath() {
        return this.myEnvPanel.getPath();
    }

    /**
     * Method returning the LD LIBRARY PATH selected
     *
     * @return the LD LIBRARY PATH
     */
    public String getLd() {
        return this.myEnvPanel.getLd();
    }

    /**
     * Method returning the status of the EnvDlg
     *
     * @return the status
     */
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Method allowing the management of the event fo this dialog frame
     *
     * @param event an event
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.ok) {
            this.status = true;
            dispose();
        }
    }
}
