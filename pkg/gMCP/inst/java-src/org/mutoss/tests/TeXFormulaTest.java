package org.mutoss.tests;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import be.ugent.caagt.jmathtex.TeXConstants;
import be.ugent.caagt.jmathtex.TeXFormula;
import be.ugent.caagt.jmathtex.TeXIcon;

public class TeXFormulaTest extends JFrame {

	public TeXFormulaTest() {
		JPanel panel = new TeXPanel();
		getContentPane().add(panel);		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TeXFormulaTest f = new TeXFormulaTest();
		f.setSize(800, 600);
		f.setVisible(true);
	}
	
	

}


class TeXPanel extends JPanel {
	
	protected void paintComponent(Graphics g) {		
		System.out.println("Painting panel.");		
		TeXIcon icon = getTeXIcon("1+3/4*ε^2");
		icon = getTeXIcon("1-ε");
		icon.paintIcon(this, g,	100, 100);
	}
	
	private static TeXIcon getTeXIcon(String s) {
		boolean print = true;
		TeXFormula formula = new TeXFormula();
		while (s.length()>0) {			
			int i = getNextOperator(s);
			System.out.println("Next index is "+i+" in string: "+s);
			if (i!=-1) {
				String op = ""+s.charAt(i);
				String start = s.substring(0, i);				
				System.out.println("Start "+start+"; op: "+op);
				s = s.substring(i+1, s.length());
				if (op.equals("+") || op.equals("-") || op.equals("*")) {
					if (print) {
						if (start.equals("ε")) {
							formula.addSymbol("varepsilon");
						} else {
							formula.add(start);
						}	
					}
					if (!op.equals("*")) formula.add(op);
					print = true;
				}
				if (op.equals("/") || op.equals("^")) {
					i = getNextOperator(s);
					String s2;
					if (i!=-1) {
						s2 = s.substring(0, i);
					} else {
						s2 = s;
					}
					if (op.equals("/")) {
						formula.addFraction(start, s2, true);
					}
					if (op.equals("^")) {
						if (start.equals("ε")) {
							formula.addSymbol("varepsilon");
						} else {
							formula.add(start);
						}
						formula.setSuperscript(s2);
					}
					print = false;
				}
			} else {
				if (print) {
					if (s.equals("ε")) {
						formula.addSymbol("varepsilon");
					} else {
						formula.add(s);
					}			
				}
				s = "";
			}
		}
		return formula.createTeXIcon(TeXConstants.ALIGN_CENTER, 16);
	}
	
	private static int getNextOperator(String s) {
		int min = s.length()+1;
		int i = s.indexOf("+");
		if (i!=-1) {
			min = i;
		}
		i = s.indexOf("-");
		if (i!=-1 && min>i) {
			min = i;
		}
		i = s.indexOf("*");
		if (i!=-1 && min>i) {
			min = i;
		}
		i = s.indexOf("/");
		if (i!=-1 && min>i) {
			min = i;
		}
		i = s.indexOf("^");
		if (i!=-1 && min>i) {
			min = i;
		}
		if (min==s.length()+1) return -1;
		return min;
	}
}
