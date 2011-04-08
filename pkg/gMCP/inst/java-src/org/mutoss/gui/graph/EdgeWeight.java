package org.mutoss.gui.graph;

import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.mutoss.config.Configuration;
import org.mutoss.gui.RControl;

public class EdgeWeight {
	
	protected String weightStr = null; 
	protected double[] weight = null;
	
	static DecimalFormat format = new DecimalFormat("#.###");
	static DecimalFormat formatSmall = new DecimalFormat("#.###E0");
	
	public EdgeWeight(String weightStr) {
		weightStr =	weightStr.replace('e', 'ε');
		this.weightStr = weightStr;
	}
	
	public EdgeWeight(double weight) {
		this.weight = new double[] { weight };
		/*if (w.toString().equals("NaN")) return "ε";
		if (w<0.0009) {
			return formatSmall.format(w);
		} else {
			return stringW;			
		}*/
		if (!Configuration.getInstance().getGeneralConfig().showFractions()) {
			weightStr = format.format(weight);
		} else {
			weightStr = RControl.getFraction(weight, true);
		}		
	}
	
	public String toString() {
		return weightStr;
	}
	
	public double[] getWeight(Hashtable<String,Double> ht) {
		String replaceStr = weightStr;
		if (weight!=null) return weight;
		for (Enumeration<String> keys = ht.keys() ; keys.hasMoreElements() ;) {
			String s = keys.nextElement();
			replaceStr = replaceStr.replaceAll(s, ""+ht.get(s));
		}
		replaceStr = replaceStr.replaceAll("ε", "e");
		weight = RControl.getR().eval("gMCP:::parseEpsPolynom(\""+replaceStr+"\")").asRNumeric().getData();
		return weight;
	}
	
	public List<String> getVariables() {
		Vector<String> variables = new Vector<String>();
		for (int i=0; i<26; i++) {
			char l = (char) ('a' + i);
			if (weightStr.lastIndexOf(l)!=-1) {
				variables.add(""+l);
			}				
		}
		if (weightStr.lastIndexOf("ε")!=-1) {
			variables.add("ε");
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
