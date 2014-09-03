package org.af.gMCP.gui.graph.annotations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.json.stream.JsonGenerator;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Legend extends Annotation {

	List<String> lines = new Vector<String>();
	List<Color> colors = new Vector<Color>();
	Font f = new Font("Helvetica", 1, 12);
	boolean header = true;
	
	public Legend(List<String> lines, List<Color> colors) {
		this.lines = lines;
		this.colors = colors;
		if (lines.size()!=colors.size()) throw new RuntimeException("Number of lines and colors does not match.");
	}
	
	public void paintLegend(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		//GlyphVector gv = g.getFont().createGlyphVector(g.getFontRenderContext(), "Component graph 1");
		//Shape shape = gv.getGlyphOutline(1);

		AffineTransform transform = g.getTransform();		 
		transform.translate(100, 100);		 
		g.transform(transform);
		
		double width = 0;
		
		for (int i=0; i<lines.size(); i++) {			
			String s = lines.get(i);
			f = g.getFont();
			if (i==0) f = f.deriveFont(Font.BOLD);
			TextLayout textLayout = new TextLayout(s, f, g.getFontRenderContext());		   
			Shape outline = textLayout.getOutline(null);
			width = Math.max(width, outline.getBounds().getWidth());
			
			transform = new AffineTransform();
			transform.translate(0, 20);		
			g.transform(transform);

			//g.setColor(Color.BLACK);
			//g.draw(outline);
			
			g.setColor(colors.get(i));			
			g.fill(outline);			
		}
		
		g.draw3DRect(-10, -20*lines.size(), (int)width+20, 20*lines.size()+10, true);

	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setVisible(true);
		f.setSize(800, 600);
		TestPanel tp = new TestPanel();
		f.setContentPane(tp);		
		System.out.println(tp.l.saveToJSON());
	}

	@Override
	public void writeObject(JsonGenerator gen) {
		gen.write("lastName", "Java");
		/*
		.write("postalCode", "12345")
		.writeStartArray("phoneNumbers")
		.writeStartObject()
		.write("type", "mobile")
		.write("number", "111-111-1111")
		.writeEnd()
		.writeStartObject()
		.write("type", "home")
		.write("number", "222-222-2222")
		.writeEnd()
		.writeEnd()
		.writeEnd();*/		
		
	}
	
}

class TestPanel extends JPanel {
	
	Legend l = new Legend(Arrays.asList(new String[]{
		"Component Weights",
		"Component Graph 1: 0.5",
		"Component Graph 2: 0.3",
		"Component Graph 3: 0.2"
	}), Arrays.asList(new Color[]{
			Color.BLACK,
			Color.RED,
			Color.GREEN,
			Color.BLUE
	}));
	
	public void paintComponent(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		l.paintLegend(g);
	}
	
}