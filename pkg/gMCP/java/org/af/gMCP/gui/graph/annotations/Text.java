package org.af.gMCP.gui.graph.annotations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.json.stream.JsonGenerator;

public class Text extends Annotation {

	String text;
	
	public Text(int x, int y, String text, Color color, int fontsize) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
	}

	@Override
	public void writeObject(JsonGenerator gen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension paintObject(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(color);
		g.drawString(text, x, y);
		FontMetrics fm = g.getFontMetrics();
		return new Dimension(fm.stringWidth(text), fm.getHeight());
	}

	@Override
	public String getLaTeX() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation readJSON(String json) {
		// TODO Auto-generated method stub
		return null;
	}
}
