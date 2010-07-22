package org.mutoss.gui.graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Latex {

	public static void writeHeader() {
		System.out.println("\\documentclass[a4paper]{article}");
		System.out.println("\\usepackage{amsmath}\\pagestyle{empty}");
		System.out.println("\\begin{document}");
	}

	public static void writeEnd() {
		System.out.println("\\end{document}");
	}

	public static void printMap(int[] map) {
		System.out.print("\\begin{bmatrix}");
		for (int i = 0; i < map.length; i++) {
			System.out.print(i + 1);
			if (i != map.length - 1)
				System.out.print("&");
		}
		System.out.print("\\\\");
		for (int i = 0; i < map.length; i++) {
			System.out.print((map[i] + 1));
			if (i != map.length - 1)
				System.out.print("&");
		}
		System.out.println("\\end{bmatrix}");
	}

	public static void printClassSize(Vector g, String name) {
		System.out.println("\\ \\\\$|$" + name + "$(G)|=$" + g.size());
	}

	public static void printClass(Vector g, String name) {
		printClassSize(g, name);
		int[] map;
		System.out.println(name + "$(G)=\\{$ \\\\$");
		for (int i = 0; i < g.size(); i++) {
			map = ((int[]) g.get(i));
			printMap(map);
		}
		System.out.print("\\}.$");
	}

	public static void printTable(int[][] t) {
		System.out.print("\\begin{tabular}{c||");
		for (int i = 0; i < t.length; i++) {
			System.out.print("c|");
		}
		System.out.println("c}$\\circ$&");
		for (int j = 0; j < t.length; j++) {
			System.out.print("$\\phi_{" + j + "}$& ");
		}
		System.out.println("\\\\\\hline\\hline");
		for (int i = 0; i < t.length; i++) {
			System.out.print("$\\phi_{" + i + "}$& ");
			for (int j = 0; j < t.length; j++) {
				System.out.print("$\\phi_{" + t[i][j] + "}$& ");
			}
			System.out.println("\\\\");
		}
		System.out.println("\\end{tabular}");
	}

	public static void newLine(FileWriter out) throws IOException {
		out.write(13);
		out.write(10);
	}

	static File textFile;
	static FileWriter out;

	public static void openFile(String fileName) {
		try {
			out = new FileWriter(fileName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void closeFile() {
		try {
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void printFTable(int[][] t) {
		try {
			out.write("\\begin{tabular}{c||");
			for (int i = 0; i < t.length; i++) {
				out.write("c|");
			}
			out.write("c}$\\circ$&");
			newLine(out);
			for (int j = 0; j < t.length; j++) {
				out.write("$\\phi_{" + j + "}$& ");
			}
			out.write("\\\\\\hline\\hline");
			newLine(out);
			for (int i = 0; i < t.length; i++) {
				out.write("$\\phi_{" + i + "}$& ");
				for (int j = 0; j < t.length; j++) {
					out.write("$\\phi_{" + t[i][j] + "}$& ");
				}
				out.write("\\\\");
				newLine(out);
			}
			out.write("\\end{tabular}");
			newLine(out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void writeFHeader2() {
		try {
			out.write("\\documentclass[a4paper]{article}");
			newLine(out);
			out
					.write("\\usepackage{amsmath}\\usepackage{pstricks}\\usepackage{pst-node}\\pagestyle{empty}");
			newLine(out);
			out.write("\\begin{document}");
			newLine(out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void writeFHeader() {
		try {
			out.write("\\documentclass[a4paper]{article}");
			newLine(out);
			out.write("\\usepackage{amsmath}\\pagestyle{empty}");
			newLine(out);
			out.write("\\begin{document}");
			newLine(out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void printlnF(String s) {
		try {
			out.write(s);
			newLine(out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void printF(String s) {
		try {
			out.write(s);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void writeFEnd() {
		try {
			out.write("\\end{document}");
			newLine(out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void printFMap(int[] map) {
		try {
			out.write("\\begin{bmatrix}");
			for (int i = 0; i < map.length; i++) {
				out.write("" + (i + 1));
				if (i != map.length - 1)
					out.write("&");
			}
			out.write("\\\\");
			for (int i = 0; i < map.length; i++) {
				out.write("" + (map[i] + 1));
				if (i != map.length - 1)
					out.write("&");
			}
			out.write("\\end{bmatrix}");
			newLine(out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void printFClassSize(Vector g, String name) {
		try {
			out.write("\\ \\\\$|$" + name + "$(G)|=$" + g.size());
			newLine(out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void printFClass(Vector g, String name) {
		try {
			printFClassSize(g, name);
			int[] map;
			out.write(name + "$(G)=\\{$ \\\\$");
			newLine(out);
			for (int i = 0; i < g.size(); i++) {
				map = ((int[]) g.get(i));
				printFMap(map);
			}
			System.out.print("\\}.$");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
