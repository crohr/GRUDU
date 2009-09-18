/**
 * 
 */
package com.izforge.izpack.panels;

import java.awt.GridBagLayout;
import java.awt.LayoutManager2;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.installer.IzPanel;
import com.izforge.izpack.installer.ResourceManager;

/**
 * @author david
 *
 */
public class CHANGELOGPanel extends IzPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7947688082918977302L;
	/** The info string. */
    private String info;

    /**
     * The constructor.
     * 
     * @param parent The parent window.
     * @param idata The installation data.
     */
    public CHANGELOGPanel(InstallerFrame parent, InstallData idata)
    {
    	super(parent, idata, new IzPanelLayout());
		// We load the text.
        loadInfo();
        // The info label.
        add(LabelFactory.create(parent.langpack.getString("InfoPanel.info"), parent.icons
                .getImageIcon("edit"), LEADING), NEXT_LINE);
        // The text area which shows the info.
        JTextArea textArea = new JTextArea(info);
        textArea.setCaretPosition(0);
        textArea.setEditable(false);
        JScrollPane scroller = new JScrollPane(textArea);
        add(scroller, NEXT_LINE);
        // At end of layouting we should call the completeLayout method also they do nothing.
        getLayoutHelper().completeLayout();
    }

    /** Loads the info text. */
    private void loadInfo()
    {
        try
        {
            String resNamePrifix = "CHANGELOGPanel.info";
            info = ResourceManager.getInstance().getTextResource(resNamePrifix);
        }
        catch (Exception err)
        {
            info = "Error : could not load the CHANGELOG text !";
        }
    }

    /**
     * Indicates wether the panel has been validated or not.
     * 
     * @return Always true.
     */
    public boolean isValidated()
    {
        return true;
    }
	
}
