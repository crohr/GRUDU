/****************************************************************************/
/* This class corresponds to an OutputStream that can be used to capture    */
/* the stream of information of the console.                                */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ConsoleOutputStream.java,v 1.2 2007/03/05 16:06:34 dloureir Exp $
 * $Log: ConsoleOutputStream.java,v $
 * Revision 1.2  2007/03/05 16:06:34  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

import diet.logging.LoggingManager;

/**
 * This class corresponds to an OutputStream that can be used to capture
 * the stream of information of the console.
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ConsoleOutputStream extends BufferedOutputStream {

    /**
     * Level of logging
     */
    private Level level;
    /**
     * Module from which we are logging
     */
    private String module;
    /**
     * Class from which we are logging
     */
    private String myClass;
    /**
     * Method from which we are logging
     */
    private String method;

    /**
     * The internal buffer where data is stored.
     */
    protected byte buf[];

    /**
     * The number of valid bytes in the buffer. This value is always
     * in the range <tt>0</tt> through <tt>buf.length</tt>; elements
     * <tt>buf[0]</tt> through <tt>buf[count-1]</tt> contain valid
     * byte data.
     */
    protected int count;

    /**
     * Creates a new buffered output stream to write data to
     * LoggingManager witha predefined level, module and class
     *
     * @param aLevel level of logging
     * @param aModule module from which we are logging
     * @param aClass class from which we are logging
     * @param aMethod method from which we are logging
     */
    public ConsoleOutputStream(Level aLevel,String aModule, String aClass, String aMethod){
        super(null);
        level = aLevel;
        module = aModule;
        myClass = aClass;
        method = aMethod;
        buf = new byte[8192];
    }

    /**
     * Creates a new buffered output stream to write data to the
     * specified underlying output stream.
     *
     * @param   out   the underlying output stream.
     */
    public  ConsoleOutputStream(OutputStream out) {
    this(out, 8192);
    }

    /**
     * Creates a new buffered output stream to write data to the
     * specified underlying output stream with the specified buffer
     * size.
     *
     * @param   out    the underlying output stream.
     * @param   size   the buffer size.
     * @exception IllegalArgumentException if size &lt;= 0.
     */
    public  ConsoleOutputStream(OutputStream out, int size) {
    super(out);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
    buf = new byte[size];
    }

    /** Flush the internal buffer */
    private void flushBuffer() throws IOException {
        if (count > 0) {
            String msg = new String(buf);
            LoggingManager.log(level, module, myClass, method, msg);
            buf = new byte[8192];
        count = 0;
        }
    }

    /**
     * Writes the specified byte to this buffered output stream.
     *
     * @param      b   the byte to be written.
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void write(int b) throws IOException {
    if (count >= buf.length) {
        flushBuffer();
    }
    buf[count++] = (byte)b;
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this buffered output stream.
     *
     * <p> Ordinarily this method stores bytes from the given array into this
     * stream's buffer, flushing the buffer to the underlying output stream as
     * needed.  If the requested length is at least as large as this stream's
     * buffer, however, then this method will flush the buffer and write the
     * bytes directly to the underlying output stream.  Thus redundant
     * <code>BufferedOutputStream</code>s will not copy data unnecessarily.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void write(byte b[], int off, int len) throws IOException {
    if (len >= buf.length) {
        /* If the request length exceeds the size of the output buffer,
               flush the output buffer and then write the data directly.
               In this way buffered streams will cascade harmlessly. */
        flushBuffer();
        byte[] temp = new byte[len];
        for(int i = 0 ;i < len ; i ++){
            temp[i] = b[off+i];
        }
        String msg = new String(temp);
        LoggingManager.log(level, module, myClass, method, msg);
        //out.write(b, off, len);
        return;
    }
    if (len > buf.length - count) {
        flushBuffer();
    }
    System.arraycopy(b, off, buf, count, len);
    count += len;
    }

    /**
     * Flushes this buffered output stream. This forces any buffered
     * output bytes to be written out to the underlying output stream.
     *
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FilterOutputStream#out
     */
    public synchronized void flush() throws IOException {
        flushBuffer();
    //out.flush();
    }

}
