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
package net.sf.jftp.gui.base;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.sf.jftp.JFtp;
import net.sf.jftp.config.Settings;
import net.sf.jftp.gui.base.dir.DirCellRenderer;
import net.sf.jftp.gui.base.dir.DirEntry;
import net.sf.jftp.gui.framework.HImageButton;
import net.sf.jftp.gui.framework.HPanel;
import net.sf.jftp.net.ConnectionHandler;
import net.sf.jftp.net.DataConnection;
import net.sf.jftp.net.HttpTransfer;
import net.sf.jftp.net.Transfer;
import net.sf.jftp.system.LocalIO;
import net.sf.jftp.system.UpdateDaemon;
import net.sf.jftp.system.logging.Log;


public class DownloadList extends HPanel implements ActionListener
{
    //private HImageButton rotate = new HImageButton(Settings.cmdImage,"rotate","Toggle selected transfer...",this);
    //private int rotator = 0;
    //private static final String SEP = "--> ";
    public Hashtable sizeCache = new Hashtable();

    //private JTextArea text = new JTextArea();
    private JList text = new JList();
    private Hashtable downloads = new Hashtable();
    //private boolean working = false;
    private long oldtime = 0;
    private HImageButton resume = new HImageButton(Settings.resumeImage,
                                                   "resume",
                                                   "Resume selected transfer...",
                                                   this);
    private HImageButton pause = new HImageButton(Settings.pauseImage, "pause",
                                                  "Pause selected transfer...",
                                                  this);
    private HImageButton cancel = new HImageButton(Settings.deleteImage,
                                                   "delete",
                                                   "Cancel selected transfer...",
                                                   this);
    private HImageButton clear = new HImageButton(Settings.clearImage, "clear",
                                                  "Remove old/stalled items from output...",
                                                  this);

    public DownloadList()
    {
        setLayout(new BorderLayout());

        text.setCellRenderer(new DirCellRenderer());

        HPanel cmdP = new HPanel();

        //	cmdP.add(rotate);
        cmdP.add(clear);

        cmdP.add(new JLabel("   "));

        cmdP.add(resume);
        cmdP.add(pause);

        cmdP.add(new JLabel("   "));

        cmdP.add(cancel);

        clear.setSize(24, 24);
        clear.setBorder(true);
        cancel.setSize(24, 24);
        cancel.setBorder(true);

        //        rotate.setSize(24,24);
        //	rotate.setBorder(true);
        resume.setSize(24, 24);
        resume.setBorder(true);
        pause.setSize(24, 24);
        pause.setBorder(true);

        //*** MY ADDITIONS
        clear.setToolTipText("Remove old/stalled items from output...");

        resume.setToolTipText("Resume selected transfer...");
        pause.setToolTipText("Pause selected transfer...");
        cancel.setToolTipText("Cancel selected transfer...");

        //***
        JScrollPane dP = new JScrollPane(text);
        add("South", cmdP);
        add("Center", dP);
    }

    public void fresh()
    {
        downloads = new Hashtable();
        updateArea();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("delete"))
        {
            deleteCon();
        }
        else if(e.getActionCommand().equals("clear") ||
                    (e.getSource() == AppMenuBar.clearItems))
        {
            //FtpConnection con = JFtp.getControlConnection();
            //JFtp.getConnectionHandler().fireProgressClear(con);
            fresh();
        }

        //	else if(e.getActionCommand().equals("rotate"))
        //	{
        //		rotator++;
        //		if(rotator >= downloads.size()) rotator = 0;
        //	}
        else if(e.getActionCommand().equals("pause"))
        {
            pauseCon();
        }
        else if(e.getActionCommand().equals("resume"))
        {
            resumeCon();
        }
    }

    private void deleteCon()
    {
        try
        {
            String cmd = getActiveItem();

            if(cmd == null)
            {
                return;
            }

            if((cmd.indexOf(Transfer.QUEUED) >= 0) ||
                   (cmd.indexOf(Transfer.PAUSED) >= 0))
            {
                cmd = getFile(cmd);

                try
                {
                    Transfer d = (Transfer) JFtp.getConnectionHandler()
                                                .getConnections().get(cmd);

                    if(d == null)
                    {
                        return;
                    }

                    d.work = false;
                    d.pause = false;
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                cmd = getFile(cmd);

                ConnectionHandler h = JFtp.getConnectionHandler();

                //Enumeration e =h.getConnections().keys();
                //while(e.hasMoreElements()) Log.out("available transfer: " + (String) e.nextElement());
                Object o = h.getConnections().get(cmd);

                Log.out("aborting  transfer: " + cmd);
                Log.out("connection handler present: " + h + ", pool size: " +
                        h.getConnections().size());

                if(o instanceof HttpTransfer)
                {
                    Transfer d = (Transfer) o;
                    d.work = false;
                    updateList(cmd, DataConnection.FAILED, -1, -1);

                    return;
                }
                else
                {
                    Transfer d = (Transfer) o;

                    DataConnection con = d.getDataConnection();
                    con.getCon().work = false;

                    try
                    {
                        con.sock.close();

                        //con.getCon().abort();
                        //if(Settings.getEnableMultiThreading()) con.getCon().disconnect();
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();

                        //System.out.println("no socket close");
                    }
                }

                LocalIO.pause(500);
                updateList(getRawFile(getActiveItem()), DataConnection.FAILED,
                           -1, -1);
            }
        }
        catch(Exception ex)
        {
            Log.debug("Action is not supported for this connection.");
            ex.printStackTrace();
        }
    }

    // fake pause, it disconnects instead
    private void pauseCon()
    {
        try
        {
            String cmd = getActiveItem();

            if(cmd == null)
            {
                return;
            }

            if((cmd.indexOf(DataConnection.GET) >= 0) ||
                   (cmd.indexOf(DataConnection.PUT) >= 0))
            {
                cmd = getFile(cmd);

                Object o = JFtp.getConnectionHandler().getConnections().get(cmd);

                if(o == null)
                {
                    return;
                }

                Transfer d = (Transfer) o;
                d.pause = true;

                DataConnection con = d.getDataConnection();

                try
                {
                    con.sock.close();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                d.prepare();
            }
        }
        catch(Exception ex)
        {
            Log.debug("Action is not supported for this connection.");
        }
    }

    private void resumeCon()
    {
        try
        {
            String cmd = getActiveItem();

            if(cmd == null)
            {
                return;
            }

            if((cmd.indexOf(Transfer.PAUSED) >= 0) ||
                   (cmd.indexOf(Transfer.QUEUED) >= 0))
            {
                cmd = getFile(cmd);

                try
                {
                    Object o = JFtp.getConnectionHandler().getConnections().get(cmd);

                    if(o == null)
                    {
                        return;
                    }

                    Transfer d = (Transfer) o;
                    d.work = true;
                    d.pause = false;
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        catch(Exception ex)
        {
            Log.debug("Action is not supported for this connection.");
        }
    }

    private String getActiveItem()
    {
        String tmp = (String) text.getSelectedValue().toString();

        if(tmp == null)
        {
            return "";
        }
        else
        {
            return tmp;
        }
    }

    public synchronized void updateList(String file, String type, long bytes,
                                        long size)
    {
        String message = type + ": <" + file + "> ";

        if(!safeUpdate())
        {
            if(!type.startsWith(DataConnection.DFINISHED) &&
                   !type.startsWith(DataConnection.FINISHED) &&
                   !type.startsWith(DataConnection.FAILED) &&
                   !type.startsWith(Transfer.PAUSED) &&
                   !type.startsWith(Transfer.REMOVED))
            {
                return;
            }
        }

        // directory
        int count = 0;

        if(type.startsWith(DataConnection.GETDIR) ||
               type.startsWith(DataConnection.PUTDIR) ||
               type.startsWith(DataConnection.PUTDIR) ||
               type.startsWith(DataConnection.DFINISHED))
        {
            //System.out.println(type);
            String tmp = type.substring(type.indexOf(":") + 1);
            type = type.substring(0, type.indexOf(":"));
            count = Integer.parseInt(tmp);
            message = type + ": <" + file + "> ";
        }

        // ---------------
        //System.out.print(size+":");
        String tmp;
        long s = (long) (size / 1024);

        if(s > 0)
        {
            tmp = Long.toString(s);
        }
        else
        {
            tmp = "?";
        }

        //System.out.println(message);
        if(type.equals(DataConnection.GET) || type.equals(DataConnection.PUT))
        {
            message = message + (int) (bytes / 1024) + " / " + tmp + " kb";
        }
        else if(type.equals(DataConnection.GETDIR) ||
                    type.equals(DataConnection.PUTDIR))
        {
            message = message + (int) (bytes / 1024) + " kb of file #" + count;
        }
        else if(type.startsWith(DataConnection.DFINISHED))
        {
            message = message + " " + count + " files.";
        }

        if(type.equals(DataConnection.FINISHED) ||
               type.startsWith(DataConnection.DFINISHED))
        {
            try
            {
                JFtp.getConnectionHandler().removeConnection(file);
            }
            catch(Exception ex)
            {
                // SMB does not need this
            }

            UpdateDaemon.updateCall();
        }
        else if(type.equals(DataConnection.FAILED))
        {
            UpdateDaemon.updateCall();
        }

        downloads.put(file, message);

        updateArea();
    }

    private synchronized DirEntry[] toArray()
    {
        DirEntry[] f = new DirEntry[downloads.size()];
        int i = 0;

        Enumeration k = downloads.elements();

        while(k.hasMoreElements())
        {
            String tmp = (String) k.nextElement();
            DirEntry d = new DirEntry(tmp, null);

            if(getFile(tmp).endsWith("/"))
            {
                d.setDirectory();
            }

            d.setNoRender();
            f[i] = d;
            i++;
        }

        return f;
    }

    private synchronized void updateArea()
    {
        int idx = text.getSelectedIndex();

        DirEntry[] f = toArray();

        text.setListData(f);

        if((f.length == 1) && (idx < 0))
        {
            text.setSelectedIndex(0);
        }
        else
        {
            text.setSelectedIndex(idx);
        }
    }

    private String getFile(String msg)
    {
        String f = msg.substring(msg.indexOf("<") + 1);
        f = f.substring(0, f.lastIndexOf(">"));

        //System.out.println(f);
        return getRealName(f);
    }

    private String getRealName(String file)
    {
        //System.out.println(">>>"+file);
        try
        {
            Enumeration e = JFtp.getConnectionHandler().getConnections().keys();

            while(e.hasMoreElements())
            {
                String tmp = (String) e.nextElement();

                //System.out.println(tmp);
                if(tmp.endsWith(file))
                {
                    return tmp;
                }
            }
        }
        catch(Exception ex)
        {
            // SMB does not need this
        }

        return file;
    }

    private String getRawFile(String msg)
    {
        String f = msg.substring(msg.indexOf("<") + 1);
        f = f.substring(0, f.lastIndexOf(">"));

        //System.out.println(f);
        return f;
    }

    private boolean safeUpdate()
    {
        long time = System.currentTimeMillis();

        if((time - oldtime) < Settings.refreshDelay)
        {
            return false;
        }

        oldtime = time;

        return true;
    }
}
