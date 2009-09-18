package net.sf.jftp;

import net.sf.jftp.*;

import java.applet.*;

import java.security.*;


public class JFtpApplet extends Applet
{
    public JFtpApplet()
    {
        AccessController.doPrivileged(new PrivilegedAction()
            {
                public Object run()
                {
                    JFtp.main(new String[0]);

                    return new Object();
                }
            });
    }

    public void init()
    {
    }
}
