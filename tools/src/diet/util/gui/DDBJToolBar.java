package diet.util.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;

import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.plaf.ToolBarUI;

/**
 * @author david
 *
 */
public class DDBJToolBar extends JToolBar implements MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8559184030544386854L;

	/**
	 * 
	 */
	public DDBJToolBar() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param orientation
	 */
	public DDBJToolBar(String name, int orientation) {
		super(name, orientation);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public DDBJToolBar(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		repaint();
		
	}

	public DDBJToolBar(int a){
		super(a);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#add(javax.swing.Action)
	 */
	@Override
	public JButton add(Action a) {
		// TODO Auto-generated method stub
		return super.add(a);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#addImpl(java.awt.Component, java.lang.Object, int)
	 */
	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		// TODO Auto-generated method stub
		super.addImpl(comp, constraints, index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#addSeparator()
	 */
	@Override
	public void addSeparator() {
		// TODO Auto-generated method stub
		super.addSeparator();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#addSeparator(java.awt.Dimension)
	 */
	@Override
	public void addSeparator(Dimension size) {
		// TODO Auto-generated method stub
		super.addSeparator(size);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#createActionChangeListener(javax.swing.JButton)
	 */
	@Override
	protected PropertyChangeListener createActionChangeListener(JButton b) {
		// TODO Auto-generated method stub
		return super.createActionChangeListener(b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#createActionComponent(javax.swing.Action)
	 */
	@Override
	protected JButton createActionComponent(Action a) {
		// TODO Auto-generated method stub
		return super.createActionComponent(a);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getAccessibleContext()
	 */
	@Override
	public AccessibleContext getAccessibleContext() {
		// TODO Auto-generated method stub
		return super.getAccessibleContext();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getComponentAtIndex(int)
	 */
	@Override
	public Component getComponentAtIndex(int i) {
		// TODO Auto-generated method stub
		return super.getComponentAtIndex(i);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getComponentIndex(java.awt.Component)
	 */
	@Override
	public int getComponentIndex(Component c) {
		// TODO Auto-generated method stub
		return super.getComponentIndex(c);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getMargin()
	 */
	@Override
	public Insets getMargin() {
		// TODO Auto-generated method stub
		return super.getMargin();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getOrientation()
	 */
	@Override
	public int getOrientation() {
		// TODO Auto-generated method stub
		return super.getOrientation();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getUI()
	 */
	@Override
	public ToolBarUI getUI() {
		// TODO Auto-generated method stub
		return super.getUI();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getUIClassID()
	 */
	@Override
	public String getUIClassID() {
		// TODO Auto-generated method stub
		return super.getUIClassID();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#isBorderPainted()
	 */
	@Override
	public boolean isBorderPainted() {
		// TODO Auto-generated method stub
		return super.isBorderPainted();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#isFloatable()
	 */
	@Override
	public boolean isFloatable() {
		// TODO Auto-generated method stub
		return super.isFloatable();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#isRollover()
	 */
	@Override
	public boolean isRollover() {
		// TODO Auto-generated method stub
		return super.isRollover();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#paintBorder(java.awt.Graphics)
	 */
	@Override
	protected void paintBorder(Graphics g) {
		// TODO Auto-generated method stub
		super.paintBorder(g);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#paramString()
	 */
	@Override
	protected String paramString() {
		// TODO Auto-generated method stub
		return super.paramString();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#setBorderPainted(boolean)
	 */
	@Override
	public void setBorderPainted(boolean b) {
		// TODO Auto-generated method stub
		super.setBorderPainted(b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#setFloatable(boolean)
	 */
	@Override
	public void setFloatable(boolean b) {
		// TODO Auto-generated method stub
		super.setFloatable(b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#setLayout(java.awt.LayoutManager)
	 */
	@Override
	public void setLayout(LayoutManager mgr) {
		// TODO Auto-generated method stub
		super.setLayout(mgr);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#setMargin(java.awt.Insets)
	 */
	@Override
	public void setMargin(Insets m) {
		// TODO Auto-generated method stub
		super.setMargin(m);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#setOrientation(int)
	 */
	@Override
	public void setOrientation(int o) {
		// TODO Auto-generated method stub
		super.setOrientation(o);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#setRollover(boolean)
	 */
	@Override
	public void setRollover(boolean rollover) {
		// TODO Auto-generated method stub
		super.setRollover(rollover);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#setUI(javax.swing.plaf.ToolBarUI)
	 */
	@Override
	public void setUI(ToolBarUI ui) {
		// TODO Auto-generated method stub
		super.setUI(ui);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#updateUI()
	 */
	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
		super.updateUI();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2D = (Graphics2D)g;
		Rectangle rec = this.getBounds();
		this.setOpaque(true);
		/* Désactivation de l'anti-aliasing */
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		/* Demande de rendu rapide */
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f ));
		g2D.setPaint(new GradientPaint(rec.x, rec.y, new Color(95,125,213), rec.x+rec.width, rec.y+rec.height,new Color(206,238,251), false ));
		g2D.fill(rec);
		paintChildren(g);
		paintBorder(g);
	}

	
	
}
