package org.af.gMCP.gui.graph;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.af.gMCP.config.Configuration;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class LaTeXTool {

	public static String StringToLaTeX(String s) {
		String latex = "";
		if (s.indexOf("E-")!=-1) {
			latex = s.replaceAll("E-", "}{10^{");
			latex = "\\frac{"+latex+"}}";
		} else {
			int openBracket = 0;
			boolean waitingForDenominator = false;
			String nominator = "";			
			s.replaceAll("ε", "\\varepsilon");	
			s.replaceAll(" ", "");
			for (int i=0;i<s.length(); i++) {
				String c = ""+s.charAt(i);	
				if (c.equals("(")) openBracket++;				
				if (c.equals(")")) openBracket--;				
				if ( (c.equals("+") || c.equals("-") || c.equals("*") || 
						(c.equals(")") &&  (i+1)<s.length() && !(s.charAt(i+1)+"").equals("/")) ) && openBracket == 0) {
					String start = s.substring(0, i+1);										
					if (waitingForDenominator) {
						if (c.equals(")")) {
							latex += "\\frac{"+nominator+"}{"+start+"}";
						} else {
							latex += "\\frac{"+nominator+"}{"+start.substring(0, i)+"}"+c;
						}
						waitingForDenominator = false;
					} else {
						latex += start;
					}
					s = s.substring(i+1, s.length());
					i=-1;
				}
				if (c.equals("/")) {					
					nominator = s.substring(0, i);
					s = s.substring(i+1, s.length());
					i=-1;
					waitingForDenominator = true;
				}
			}
			if (waitingForDenominator) {
				latex += "\\frac{"+nominator+"}{"+s+"}";				
			} else {
				latex += s;
			}			
			latex = latex.replaceAll("\\*", Configuration.getInstance().getGeneralConfig().getTimesSymbol());			
			latex = latex.replaceAll("\\(", "{(");
			latex = latex.replaceAll("\\)", ")}");
		}
		//logger.debug("LaTeX string:"+latex);	
		return latex;
	}
	
	/**
	 * This function takes a string and creates a TeXIcon from this.
	 * @param s String to be parsed.
	 * @return
	 */
	public static TeXIcon getTeXIcon(JFrame parent, String s, int points) {
		try {	
			String latex = StringToLaTeX(s);
			TeXFormula formula = new TeXFormula(latex);//
			formula = new TeXFormula("\\mathbf{"+latex+"}");		
			TeXIcon result = formula.createTeXIcon(TeXConstants.ALIGN_CENTER, points);
			// TODO What about getIconHeight()/
			if (result.getIconWidth()>60) {
				result = formula.createTeXIcon(TeXConstants.ALIGN_CENTER, (int) (points*0.7));
			}
			//if (latex.indexOf("frac")==-1 && latex.length()>4) points = (int) (points*0.7);
			return result;
		} catch(Exception e) {
			//e.printStackTrace();
			//System.out.println("Error: "+latex);
			//TODO This is not allowed while painting:
			//JOptionPane.showMessageDialog(parent, "Invalid weight string:\n"+latex+"\nError:\n"+e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
			TeXFormula formula = new TeXFormula("Syntax Error");
			return formula.createTeXIcon(TeXConstants.ALIGN_CENTER, (int) (points*0.7)); 
		}		
	}

	public static Component panel = new JPanel();

}
