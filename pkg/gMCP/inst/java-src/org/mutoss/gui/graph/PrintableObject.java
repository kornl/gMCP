package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;


public class PrintableObject implements Printable {
	private NetList nl;

	public PrintableObject(NetList nl) {
		this.nl = nl;
	}

	public int print(Graphics g, PageFormat pf, int iPage) throws PrinterException {		
		if (0 != iPage)
			return NO_SUCH_PAGE;
		try {

			double width = pf.getImageableWidth();
			double height = pf.getImageableHeight();
			Graphics2D g2 = (Graphics2D) g;
			Dimension dim = nl.getSize();

			g2.translate(pf.getImageableX(), pf.getImageableY());
			System.out.println("" + width + " " + dim.getWidth() + " " + height
					+ " " + dim.getHeight());
			double scale = Math.min(width / dim.getWidth(), height / dim.getHeight());
			g2.scale(scale, scale);
			nl.setBackground(Color.white);
			nl.paintComponent(g);
		} catch (Exception ex) {
			throw new PrinterException(ex.getMessage());
		}
		return PAGE_EXISTS;
	}

}
