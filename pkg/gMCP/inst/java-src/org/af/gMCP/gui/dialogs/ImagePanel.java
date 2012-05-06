package org.af.gMCP.gui.dialogs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * A JPanel that draws an Image object. 
 */
public class ImagePanel extends JPanel {
	Image image;
	
	public ImagePanel(Image image) {
		this.image = image;
		this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.drawImage(image, 0, 0, null);
	}
}
