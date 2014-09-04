package org.af.gMCP.gui.graph.annotations;

import java.awt.Color;
import java.awt.Graphics;

import javax.json.stream.JsonGenerator;

public class Text extends Annotation {

	public Text(int x, int y, String text, Color color, int fontsize) {
		
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
