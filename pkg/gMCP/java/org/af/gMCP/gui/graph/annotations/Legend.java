package org.af.gMCP.gui.graph.annotations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.json.stream.JsonGenerator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.af.gMCP.gui.graph.NetList;

public class Legend extends Annotation {

	List<String> lines = new Vector<String>();
	List<Color> colors = new Vector<Color>();
	boolean header = true;
	Font f;
	List<Annotation> av = new Vector<Annotation>();
	
	public Legend(int x, int y, List<String> lines, List<Color> colors, AnnotationPanel nl) {		
		this(x, y, lines, colors, nl, new Font("Arial", Font.PLAIN, 14));
		//TODO Can we use UIManager.getDefaults().getFont("JLabel.font") as default? Or do we want "Helvetica"?
	}
	
	public Legend(int x, int y, List<String> lines, List<Color> colors, AnnotationPanel nl, Font font) {
		this.x = x;
		this.y = y;
		this.lines = lines;
		this.colors = colors;
		this.nl = nl;
		if (lines.size()!=colors.size()) throw new RuntimeException("Number of lines and colors does not match.");
		
		for (int i=0; i<lines.size(); i++) {
			if (i==0 && header) {
				f = font.deriveFont(Font.BOLD);
			} else {
				f = font;
			}
			av.add(new Text(x+10, y+(i+1)*20, lines.get(i), colors.get(i), f, nl));
						 
		}
	}
	
	public void add(int index, String line, Color color) {
		lines.add(index, line);
		colors.add(index, color);
		av.add(index, new Text(x+10, y+(index+1)*20, line, color, f, nl));
	}
	
	public void rm(int index) {
		lines.remove(index);
		colors.remove(index);
		av.remove(index);
	}

	public Dimension paintObject(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		
		int width = 0;
		Dimension d = null;
		
		for (Annotation a : av) {
			d = a.paintObject(g);
			width = Math.max(d.width, width); 
		}
		
		if (!(av.get(av.size()-1) instanceof Rectangle)) {
			Rectangle r = new Rectangle(x, y, width+20, lines.size()*20+10, nl);
			av.add(r);
			d = r.paintObject(g);
		}
		
		return d;

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
		return av.get(av.size()-1).inYou(x, y);
	}
	
	@Override
	public void setX(int x) {
		for (Annotation a : av) {
			a.setX(a.x+(x-this.x));
		}
		super.setX(x);				
	}
	
	@Override
	public void setY(int y) {
		for (Annotation a : av) {
			a.setY(a.y+(y-this.y));
		}		
		super.setY(y);		
	}
	
	
}

class TestPanel extends JPanel implements AnnotationPanel {
	
	Legend l;

	TestPanel() {
		l = new Legend(100, 100,
				Arrays.asList(new String[]{
						"Component Weights",
						"Component Graph 1: 0.5",
						"Component Graph 2: 0.3",
						"Component Graph 3: 0.2"
				}), Arrays.asList(new Color[]{
						Color.BLACK,
						Color.RED,
						Color.GREEN,
						Color.BLUE
				}), this);
	}
	
	public void paintComponent(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		l.paintObject(g);
	}

	public double getZoom() {		
		return 1;
	}
	
}