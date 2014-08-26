package org.af.gMCP.gui.graph.annotations;

import java.awt.Color;

public class Line {

	final static int NONE = 0;
	final static int ARROW = 1;
	
	int x1, y1, x2, y2;
	Color color;
	int type;
	
	public Line(int x1, int y1, int x2, int y2, int type, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.type = type;
		this.color = color;
	}
	
}
