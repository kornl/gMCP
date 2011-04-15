package org.mutoss.tests;

import java.text.DecimalFormat;

public class FormatTest {
	
	public static void main(String[] args) {
		DecimalFormat format = new DecimalFormat("#.####");
		System.out.println(format.format(0.0001));
		System.out.println(format.format(0.9999));		
		System.out.println(format.format(0.00001));
		System.out.println(format.format(0.99999));
		System.out.println(format.format(1.0E-4));
		format = new DecimalFormat("#.#####");
		System.out.println();
		System.out.println(format.format(0.0001));
		System.out.println(format.format(0.9999));		
		System.out.println(format.format(0.00001));
		System.out.println(format.format(0.99999));
		System.out.println(format.format(1.0E-4));
		format = new DecimalFormat("#.###");
		System.out.println();
		System.out.println(format.format(0.0001));
		System.out.println(format.format(0.9999));		
		System.out.println(format.format(0.00001));
		System.out.println(format.format(0.99999));
		System.out.println(format.format(1.0E-4));
	}
}
