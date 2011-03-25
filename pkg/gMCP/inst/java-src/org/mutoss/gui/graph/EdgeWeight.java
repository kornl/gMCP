package org.mutoss.gui.graph;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.mutoss.gui.RControl;

public class EdgeWeight {
	
	protected String weightStr = null; 
	protected Double weight = null;
	
	public EdgeWeight(String weightStr) {
		this.weightStr = weightStr;
	}
	
	public EdgeWeight(double weight) {
		this.weight = weight;
	}
	
	public String toString() {
		if (weightStr!=null) return weightStr;
		return ""+weight;
	}
	
	public double getWeight(Hashtable<String,Double> ht) {
		String replaceStr = weightStr;
		if (weight!=null) return weight;		
		for (Enumeration<String> keys = ht.keys() ; keys.hasMoreElements() ;) {
			String s = keys.nextElement();
			replaceStr = replaceStr.replaceAll(s, ""+ht.get(s));
		}
		return RControl.getR().eval(replaceStr).asRNumeric().getData()[1];
	}
	
	public List<String> getVariables() {
		Vector<String> variables = new Vector<String>();
		for (int i=0; i<26; i++) {
			char l = (char) ('a' + i);
			if (weightStr.lastIndexOf(l)!=-1) {
				variables.add(""+l);
			}				
		}
		return variables;
	}
	
}
