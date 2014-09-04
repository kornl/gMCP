package org.af.gMCP.gui.graph.annotations;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.json.stream.JsonGenerator;

public class Ellipse extends Annotation {

	int width;
	int height;
	
	public Ellipse(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void writeObject(JsonGenerator gen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension paintObject(Graphics graphics) {
		// TODO Auto-generated method stub
		return new Dimension(width, height);
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

	@Override
	public boolean inYou(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
}
