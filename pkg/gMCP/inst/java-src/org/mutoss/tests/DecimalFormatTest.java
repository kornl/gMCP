package org.mutoss.tests;

import java.text.DecimalFormat;

public class DecimalFormatTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DecimalFormat format = new DecimalFormat("#.###");
		System.out.println(format.format(0.99999999999999999));

	}

}
