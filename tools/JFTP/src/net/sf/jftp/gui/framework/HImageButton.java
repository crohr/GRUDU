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
package net.sf.jftp.gui.framework;

import net.sf.jftp.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class HImageButton extends JButton implements MouseListener
{
    public String label = "";
    public ActionListener who = null;
    private boolean entered = false;
    private Color cacheBack = GUIDefaults.light;

    //private Color back = cacheBack;
    private Color swing = new Color(155, 155, 205);

    //private Color swing2 = new Color(75,75,125);
    //private Color front = GUIDefaults.front;
    //private Font font = GUIDefaults.font;
    //private Image img;
    //private String image = null;
    private String cmd = "default";
    private boolean drawBorder = true;
    private JButton button;

    public HImageButton(String image, String cmd, String label,
                        ActionListener who)
    {
        //this.image = image;
        this.cmd = cmd;
        this.label = label;
        this.who = who;

        //cacheBack = getBackground();
        //back = cacheBack;
        //button = new JButton(new ImageIcon(HImage.getImage(this,image)));
        setIcon(new ImageIcon(HImage.getImage(this, image)));

        //add(button, BorderLayout.CENTER);
        addMouseListener(this);

        //addMouseListener(this);
        setVisible(true);
        setMinimumSize(new Dimension(25, 25));
        setPreferredSize(new Dimension(25, 25));
        setMaximumSize(new Dimension(25, 25));
    }

    /*
        public void paintComponent(Graphics g)
        {
            g.setColor(back);
            g.fillRect(0,0,getSize().width,getSize().height);

            if(drawBorder)
            {
                    g.setColor(front);
                    g.drawRect(0,0,getSize().width-1,getSize().height-1);
                    //g.setColor(swing2);
                    g.drawRect(0,0,getSize().width-2,getSize().height-2);
                    g.setColor(back);
                    g.fillRect(0,0,1,1);
                    g.fillRect(0,getSize().height,1,getSize().height-1);
                    g.fillRect(getSize().width-1,0,getSize().width,1);
                    g.fillRect(getSize().width-1,getSize().height,getSize().width,getSize().height-1);
            }
            g.drawImage(img,2,2,this);
        }
    */
    public void update(Graphics g)
    {
        paintComponent(g);
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
        //back = cacheBack;
        who.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                            cmd));

        //repaint();
    }

    public void mouseEntered(MouseEvent e)
    {
        entered = true;
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        //back = swing;
        //button.setBackground(swing);
        //paintImmediately(0,0,getSize().width,getSize().height);
        JFtp.statusP.status(label);
    }

    public void mouseExited(MouseEvent e)
    {
        entered = false;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        //back = cacheBack;
        //button.setBackground(cacheBack);
        //paintImmediately(0,0,getSize().width,getSize().height);
        JFtp.statusP.status("");
    }

    public void setBorder(boolean what)
    {
        drawBorder = what;
    }
}
