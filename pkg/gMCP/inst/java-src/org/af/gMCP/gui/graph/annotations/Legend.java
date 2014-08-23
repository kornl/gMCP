package org.af.gMCP.gui.graph.annotations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import javax.json.stream.JsonGenerator;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Legend extends Annotation {

	public Legend() {
		
	}
	
	public void paintLegend(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		//GlyphVector gv = g.getFont().createGlyphVector(g.getFontRenderContext(), "Component graph 1");
		//Shape shape = gv.getGlyphOutline(1);
		 Font f = new Font("Helvetica", 1, 12);
		 
		 String s = new String("Component graph 1");
		 TextLayout textLayout = new TextLayout(s, f, g.getFontRenderContext());		   
		 Shape outline = textLayout.getOutline(null);
		 
		 AffineTransform transform = g.getTransform();		 
		 transform.translate(100, 100);		 
		 g.transform(transform);		 
		 
		 g.setColor(Color.BLUE);
		 g.fill(outline);
		 
		 g.setColor(Color.BLACK);
		 g.draw(outline);
		 
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setVisible(true);
		f.setSize(800, 600);
		f.setContentPane(new TestPanel());
		Legend l = new Legend();
		System.out.println(l.saveToJSON());
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
	
	Legend l = new Legend();
	
	public void paintComponent(Graphics g) {
		l.paintLegend(g);
	}
	
}