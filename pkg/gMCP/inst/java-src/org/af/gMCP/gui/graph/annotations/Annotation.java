package org.af.gMCP.gui.graph.annotations;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

public abstract class Annotation {

	int x, y;
	
	final static String CLASS = "Class";

	public String saveToJSON() {
		StringWriter sw = new StringWriter();
		JsonGenerator gen = Json.createGenerator(sw);
		gen.writeStartObject()
		.write(CLASS, this.getClass().getCanonicalName());//.getSimpleName());
		writeObject(gen);	
		gen.writeEnd();
		gen.close();
		return sw.toString();
	}

	public abstract void writeObject(JsonGenerator gen);

	public static Annotation createAnnotation(String s) {
		JsonParser parser = Json.createParser(new StringReader(s));
		String key = "";
		Annotation a = null;
		try {
			while (parser.hasNext()) {
				JsonParser.Event event = parser.next();
				switch(event) {
				case START_ARRAY:
				case END_ARRAY:
				case START_OBJECT:
				case END_OBJECT:
				case VALUE_FALSE:
				case VALUE_NULL:
				case VALUE_TRUE:				
					break;
				case KEY_NAME:
					key = parser.getString();
					break;
				case VALUE_STRING:
				case VALUE_NUMBER:
					String v = parser.getString();
					if (key.equals(CLASS)) {
						System.out.println("Creating object of class "+ v);
						a = (Annotation)Class.forName(v).getConstructor().newInstance();
					}
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	public static void main(String[] args) {
		Legend l = new Legend();
		String s = l.saveToJSON();
		System.out.println(s);
		//createAnnotation("[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]");
		createAnnotation(s);
	}
	
}
