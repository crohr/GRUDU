package diet;

import javax.swing.*;
import java.awt.*;

public class AboutDlg extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4477246479903080752L;

	public AboutDlg(JFrame parent) {
	super(parent, true);
	getContentPane().add(new AboutPanel());
	pack();
	setLocationRelativeTo(null);
    }

    private class AboutPanel extends JPanel {
	/**
		 * 
		 */
		private static final long serialVersionUID = 596917030577905680L;

	public AboutPanel() {
	    this.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    
	    String msg = "<HTML><p>Diet DashBoard <br> Abdelkader Amar (Abdelkader.Amar@ens-lyon.fr) <br> David Loureiro (David.Loureiro@ens-lyon.fr)<br>";
	    msg += "GRAAL 2006 <br></p></HTML>";
	    JLabel label1 = new JLabel(msg);
	    c.weightx = 0.25;
	    c.gridx = 0;
	    c.gridy = 1;
	    c.insets = new Insets(10,10, 10,10);
	    add(label1, c);
	}
    }
}
