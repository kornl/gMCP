package org.af.gMCP.gui.graph.annotations;

import java.awt.Color;
import java.awt.Graphics;

import javax.json.stream.JsonGenerator;

public class Note extends Annotation {

	String text;
	int fontsize;
	Color color;
	int xP, yP, x, y; 
	int width, height;
	
	public Note(String text, int xP, int yP, int x, int y, int width, int height, Color color, int fontsize) {
		this.text = text;
		this.xP = xP;
		this.yP = yP;
		this.x = x;
		this.y = y;
		this.color = color;
		this.fontsize = fontsize;
	}

	@Override
	public void writeObject(JsonGenerator gen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paintObject(Graphics graphics) {
		// TODO Auto-generated method stub
		
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
