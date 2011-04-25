package org.mutoss.gui.graph;

import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;
import org.mutoss.gui.RControl;

public class EdgeWeight {
	
	private static final Log logger = LogFactory.getLog(EdgeWeight.class);
	
	protected String weightStr = null; 
	protected double[] weight = null;
	
	static DecimalFormat formatSmall = new DecimalFormat("#.###E0");
	
	public EdgeWeight(String weightStr) {
		//weightStr =	weightStr.replace('e', 'ε');
		this.weightStr = weightStr;
	}
	
	public EdgeWeight(double weight) {
		this.weight = new double[] { weight };
		setWeightStr(weight);	
	}
	
	private void setWeightStr(double weight) {
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		if (!Configuration.getInstance().getGeneralConfig().showFractions()) {
			if (weight!=0 && weight < Math.pow(0.1, Configuration.getInstance().getGeneralConfig().getDigits())) {
				weightStr = formatSmall.format(weight);
			} else {
				weightStr = format.format(weight);
			}
		} else {
			if (weight!=0 && weight < Math.pow(0.1, Configuration.getInstance().getGeneralConfig().getDigits())) {
				weightStr = formatSmall.format(weight);
			} else {
				weightStr = RControl.getFraction(weight, true);
			}
		}	
	}

	public String toString() {
		return weightStr;
	}
	
	public double[] getWeight(Hashtable<String,Double> ht) {
		try {
			String replaceStr = weightStr;
			if (weight!=null) return weight;
			for (Enumeration<String> keys = ht.keys() ; keys.hasMoreElements() ;) {
				String s = keys.nextElement();
				for (int i=0; i <greek.length; i++) {
					if ((""+greek[i]).equals(s)) {
						replaceStr = replaceStr.replaceAll(""+greekLaTeX[i], ""+ht.get(s));		
					}
				}								
			}
			for (Enumeration<String> keys = ht.keys() ; keys.hasMoreElements() ;) {
				String s = keys.nextElement();
				replaceStr = replaceStr.replaceAll(s, ""+ht.get(s));				
			}
			replaceStr = replaceStr.replaceAll("\\\\epsilon", "epsilon");
			weight = RControl.getR().eval("gMCP:::parseEpsPolynom(\""+replaceStr.replaceAll("\\\\", "\\\\\\\\")+"\")").asRNumeric().getData();
			return weight;
		} catch (Exception e) {
			logger.warn("Error parsing edge weight:\n"+e.getMessage());
			return new double[] {Double.NaN};
		}
	}
	
	public static final String[] greekLaTeX = {
		"\\\\alpha", "\\\\beta", "\\\\gamma", "\\\\delta", "\\\\epsilon", "\\\\zeta", "\\\\eta", 
		"\\\\theta", "\\\\iota", "\\\\kappa", "\\\\lambda", "\\\\mu", "\\\\nu", "\\\\xi", 
		"\\\\omicron", "\\\\pi", "\\\\rho", "\\\\sigma", "\\\\tau", "\\\\nu", "\\\\phi",
		 "\\\\chi", "\\\\psi", "\\\\omega"
	};
	
	public static final char[] greek = {
			'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 
			'θ', 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 
			'ο', 'π', 'ρ', 'σ', 'τ', 'υ', 'φ',
			'χ', 'ψ', 'ω'
	};
	
	public List<String> getVariables() {
		String replaceStr = weightStr;
		Vector<String> variables = new Vector<String>();
		for (int i=0; i<greek.length; i++) {
			replaceStr = replaceStr.replaceAll(greekLaTeX[i], ""+greek[i]);
			if (replaceStr.lastIndexOf(greek[i])!=-1) {
				variables.add(""+greek[i]);
			}
		}
		for (int i=0; i<26; i++) {
			char l = (char) ('a' + i);
			if (replaceStr.lastIndexOf(l)!=-1) {
				variables.add(""+l);
			}				
		}		
		return variables;
	}

	public String getLaTeXStr() {
		if (weight != null) {
			return RControl.getR().eval("gMCP:::getLaTeXFraction("+weight+")").asRChar().getData()[0];
		}
		return weightStr.replace("ε", "$\\epsilon$");
	}
	
}
