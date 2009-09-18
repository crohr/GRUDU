package net.sf.jftp.net;

import java.util.Vector;

import com.trilead.ssh2.Connection;



public class SftpTransfer implements Runnable
{
    private String host;
    private String localPath;
    private String remotePath;
    private String file;
    private String user;
    private String pass;
    private SftpConnection con = null;
    private String type;
    public Thread runner;
    private Vector listeners;
//    private SshConnectionProperties props;

    public SftpTransfer(Connection conn, String frontale, String preferredFrontale, String localPath, String remotePath,
                        String file, String user, String pass,
                        Vector listeners, String type)
    {
//        this.props = props;
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.file = file;
        this.user = user;
        this.pass = pass;
        this.type = type;
        this.listeners = listeners;

        prepare();
    }

    public void prepare()
    {
        runner = new Thread(this);
        runner.setPriority(Thread.MIN_PRIORITY);
        runner.start();
    }

    public void run()
    {
//        con = new SftpConnection("","");
//        con.setConnectionListeners(listeners);
//        con.login(user, pass);
//        con.setLocalPath(localPath);
//        con.chdir(remotePath);
//
//        if(type.equals(Transfer.DOWNLOAD))
//        {
//            con.download(file);
//        }
//        else if(type.equals(Transfer.UPLOAD))
//        {
//            con.upload(file);
//        }
    }

    public SftpConnection getSftpConnection()
    {
        return con;
    }
}
