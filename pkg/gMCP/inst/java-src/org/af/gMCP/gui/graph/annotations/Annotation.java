package org.af.gMCP.gui.graph.annotations;

import java.io.StringReader;

import javax.json.Json;
import javax.json.stream.JsonParser;

public class Annotation {

	int x, y;
	
	public String saveToR() {
		String s = this.getClass().getSimpleName();		
		return s;
	}
	
	public static Annotation createAnnotation(String s) {
		JsonParser parser = Json.createParser(new StringReader(s));
		//Annotation a = ;
		return null;
	}
	
}
