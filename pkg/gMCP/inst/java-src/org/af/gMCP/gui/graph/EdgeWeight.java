package org.af.gMCP.gui.graph;

import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.RControl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EdgeWeight {
	
	private static final Log logger = LogFactory.getLog(EdgeWeight.class);
	
	protected String weightStr = null; 
	protected double[] weight = null;
	
	static DecimalFormat formatSmall = new DecimalFormat("#.###E0");
	
	public EdgeWeight(String weightStr) {
		if (weightStr==null) throw new RuntimeException("weigthStr is not allowed to be null!");
		this.weightStr = weightStr;
	}
	
	public EdgeWeight(double weight) {
		this.weight = new double[] { weight };
		setWeightStr(weight);	
	}
	
	public String getPreciseWeightStr() {
		if (weight != null) return ""+weight[0];
		return toString();
	}
	
	private void setWeightStr(double weight) {
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		if (!Configuration.getInstance().getGeneralConfig().showFractions()) {
			if (weight!=0 && Math.abs(weight) < Math.pow(0.1, Configuration.getInstance().getGeneralConfig().getDigits())) {
				weightStr = formatSmall.format(weight);
			} else {
				weightStr = format.format(weight);
			}
		} else {
			if (weight!=0 && Math.abs(weight) < Math.pow(0.1, 3)) {
				weightStr = formatSmall.format(weight);
			} else {
				weightStr = RControl.getFraction(weight, true);
			}
		}	
	}

	public String toString() {
		return weightStr;
	}
	
	public double getWeight(Hashtable<String,Double> ht) {
		try {
			String replaceStr = weightStr;
			if (weight!=null) return weight[0];
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
			weight = RControl.getR().eval(replaceStr).asRNumeric().getData();
			return weight[0];
		} catch (Exception e) {
			//logger.warn("Error parsing edge weight:\n"+e.getMessage());
			return Double.NaN;
		}
	}
	
	public static final String[] greekLaTeX = {
		"alpha", "beta", "gamma", "delta", "epsilon", "zeta", "eta", 
		"theta", "iota", "kappa", "lambda", "mu", "nu", "xi", 
		/*"omicron",*/ "pi", "rho", "sigma", "tau", "upsilon", "phi",
		"chi", "psi", "omega"
	};
	
	public static final char[] greek = {
			'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 
			'θ', 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 
			/*'ο',*/ 'π', 'ρ', 'σ', 'τ', 'υ', 'φ',
			'χ', 'ψ', 'ω'
	};
	
	public static String UTF2LaTeX(char greekC) {
		for (int i=0; i<greek.length; i++) {
			if (greekC == greek[i]) return greekLaTeX[i];
		}
		return "Error";
	}
	
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
		if (weight != null && weight.length==1) {
			String weightStr;
			if (weight[0] !=0 && weight[0] < Math.pow(0.1, 3.1)) {
				weightStr = formatSmall.format(weight[0]);
				if (weightStr.indexOf("E-")!=-1) {
					weightStr = weightStr.replaceAll("E-", "}{10^{");
					weightStr = "\\frac{"+weightStr+"}}";
				}
				return weightStr;
			}
			return RControl.getR().eval("gMCP:::getLaTeXFraction("+weight[0]+")").asRChar().getData()[0];
		}
		String replaceStr = weightStr;
		for (int i=0; i<greek.length; i++) {
			replaceStr = replaceStr.replaceAll(""+greek[i], greekLaTeX[i]);
		}
		return replaceStr;
	}
	
}
