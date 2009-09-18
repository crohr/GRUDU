/**
 * 
 */
package com.izforge.izpack.panels;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
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
public class ConfigurationPresentationPanel extends IzPanel {

	/** The info string. */
    private String info;

    /**
     * The constructor.
     * 
     * @param parent The parent window.
     * @param idata The installation data.
     */
    public ConfigurationPresentationPanel(InstallerFrame parent, InstallData idata)
    {
    	super(parent, idata, new IzPanelLayout());
        // We load the text.
        loadInfo();
        // The text area which shows the info.
        JLabel textArea = new JLabel(info);
        textArea.setEnabled(false);
        //JScrollPane scroller = new JScrollPane(textArea);
        add(textArea, NEXT_LINE);
        // At end of layouting we should call the completeLayout method also they do nothing.
        getLayoutHelper().completeLayout();
    }

    /** Loads the info text. */
    private void loadInfo()
    {
        try
        {
            String resNamePrifix = "ConfigurationPresentationPanel.info";
            info = ResourceManager.getInstance().getTextResource(resNamePrifix);
        }
        catch (Exception err)
        {
            info = "Error : could not load the Configuration Presentation text !";
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
