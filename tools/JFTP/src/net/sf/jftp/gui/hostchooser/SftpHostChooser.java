/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.sf.jftp.gui.hostchooser;

import net.sf.jftp.*;
import net.sf.jftp.config.*;
import net.sf.jftp.gui.framework.*;
import net.sf.jftp.net.*;
import net.sf.jftp.system.logging.Log;
import net.sf.jftp.tools.*;
import net.sf.jftp.util.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import javax.swing.*;


public class SftpHostChooser extends HFrame implements ActionListener,
                                                       WindowListener
{
    public HTextField host = new HTextField("Host:", "localhost");
    public HTextField user = new HTextField("Username:", "guest");
    public HTextField port = new HTextField("Port:", "22");
    public HPasswordField pass = new HPasswordField("Password/Phrase:",
                                                    "nopasswd");
    public JComboBox enc = new JComboBox();
    public JComboBox cs = new JComboBox();
    public JComboBox keys = new JComboBox();
    public JLabel encL = new JLabel("Pref. Encryption");
    public JLabel csL = new JLabel("Pref. Message Auth.");
    public JLabel keysL = new JLabel("Pref. Public Key");
    public JLabel keyfileL = new JLabel("(No File)");
    private HPanel okP = new HPanel();
    private HPanel keyP = new HPanel();
    private HButton ok = new HButton("Connect");
    private HButton keyfile = new HButton("Choose Key File");
    private ComponentListener listener = null;
    private boolean useLocal = false;
    private boolean shell = false;
    private String keyfileName = null;

    public SftpHostChooser(ComponentListener l, boolean local)
    {
        listener = l;
        useLocal = local;
        init();
    }

    public SftpHostChooser(ComponentListener l)
    {
        listener = l;
        init();
    }

    public SftpHostChooser()
    {
        init();
    }

    public SftpHostChooser(boolean shell)
    {
        this.shell = shell;
        initOld();
    }

    public void init(){
        setLocationRelativeTo(null);
        setTitle("Connection Manager ...");
        setBackground(okP.getBackground());

    }

    public void initOld()
    {
        //setSize(500, 200);
        setLocation(100, 150);
        setTitle("Sftp Connection...");
        setBackground(okP.getBackground());
        getContentPane().setLayout(new GridLayout(6, 2, 5, 3));

        //***MY CHANGES
        try
        {
            File f = new File(Settings.appHomeDir);
            f.mkdir();

            File f1 = new File(Settings.login);
            f1.createNewFile();

            File f2 = new File(Settings.login_def_sftp);
            f2.createNewFile();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        String[] login = LoadSet.loadSet(Settings.login_def_sftp);

        if((login[0] != null) && (login.length > 1))
        {
            host.setText(login[0]);
            user.setText(login[1]);
        }
        if(Settings.getStorePasswords())
        {
            if((login != null) && (login.length > 2) && (login[2] != null))
            {
                pass.setText(login[2]);
            }
        }
        else
        {
            pass.setText("");
        }

        enc.addItem("3des-cbc");
        enc.addItem("blowfish-cbc");

        cs.addItem("hmac-sha1");
        cs.addItem("hmac-sha1-96");
        cs.addItem("hmac-md5");
        cs.addItem("hmac-md5-96");

        keys.addItem("ssh-rsa");
        keys.addItem("ssh-dss");

        getContentPane().add(host);
        getContentPane().add(port);
        getContentPane().add(user);
        getContentPane().add(pass);
        getContentPane().add(encL);
        getContentPane().add(enc);
        getContentPane().add(csL);
        getContentPane().add(cs);
        getContentPane().add(keysL);
        getContentPane().add(keys);
        getContentPane().add(keyP);
        getContentPane().add(okP);

        keyP.add(keyfileL);
        keyP.add(keyfile);
        okP.add(new JLabel("        "));
        okP.add(ok);
        ok.addActionListener(this);
        keyfile.addActionListener(this);

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        pass.text.addActionListener(this);

        pack();
        setModal(false);
        setVisible(false);
        addWindowListener(this);
    }

    public void update()
    {
	fixLocation();
        setVisible(true);
        toFront();
        host.requestFocus();
    }

    public void setShell(boolean shell)
    {
        this.shell = shell;
    }

    public boolean getShell()
    {
        return shell;
    }

    public void actionPerformed(ActionEvent e)
    {
        if((e.getSource() == ok) || (e.getSource() == pass.text))
        {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            SftpConnection con = null;

            String htmp = host.getText().trim();
            String utmp = user.getText().trim();
            String ptmp = pass.getText();

            //***port number: to be initialized in a future version?
            int potmp = 22;

            try
            {
                potmp = Integer.parseInt(port.getText());
            }
            catch(Exception ex)
            {
                Log.debug("Error: Not a number!");
            }

            String potmpString = new String("" + potmp);

            try
                {
                    boolean status;

                    SaveSet s = new SaveSet(Settings.login_def_sftp, htmp,
                                            utmp, ptmp, potmpString, "null",
                                            "null");

//                    status = StartConnection.startCon("SFTP", htmp, utmp, ptmp,
//                                                      potmp, "", useLocal);
                }
                catch(Exception ex)
                {
                    Log.debug("Could not create SftpConnection, does this distribution come with j2ssh?");
                }

            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.dispose();
            JFtp.mainFrame.setVisible(true);
            JFtp.mainFrame.toFront();

            if(listener != null)
            {
                listener.componentResized(new ComponentEvent(this, 0));
            }
        }
        else if(e.getSource() == keyfile)
        {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(this);

            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                keyfileName = chooser.getSelectedFile().getPath();

                if(keyfileName != null)
                {
                    keyfileL.setText("(File present)");
                }
            }
        }
    }

    public void windowClosing(WindowEvent e)
    {
        this.dispose();
    }

    public void windowClosed(WindowEvent e)
    {
    }

    public void windowActivated(WindowEvent e)
    {
    }

    public void windowDeactivated(WindowEvent e)
    {
    }

    public void windowIconified(WindowEvent e)
    {
    }

    public void windowDeiconified(WindowEvent e)
    {
    }

    public void windowOpened(WindowEvent e)
    {
    }

    public Insets getInsets()
    {
        Insets std = super.getInsets();

        return new Insets(std.top + 10, std.left + 10, std.bottom + 10,
                          std.right + 10);
    }

    public void pause(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch(Exception ex)
        {
        }
    }
}