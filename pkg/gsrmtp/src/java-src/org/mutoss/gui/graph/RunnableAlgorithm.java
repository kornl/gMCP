package org.mutoss.gui.graph;

import java.util.Random;
import java.util.Vector;

/**
 * \brief Dies ist eine Algorithmus-Klasse für die NetzListe.
 * Sie besteht zum einen aus statischen Methoden zur Anordnung der Knoten,
 * und zum anderen kann sie als Thread gestartet über ein VS-Objekt gesteuert
 * kräftebasierte Verfahren auf die Knoten ausüben.
 */

/**
 * @version 12 Jan 2002
 * @author Kornelius Walter
 * @see NetzListe
 */

public class RunnableAlgorithm extends Thread {

	Vector<Node> knoten;
	Vector<Edge> edges;
	VS vs;
	NetzListe nl;
	static Random zufall = new Random();
	static boolean change = false;

	public RunnableAlgorithm(Vector<Node> knoten, Vector<Edge> edges,	VS vs, NetzListe nl) {
		this.setPriority(MIN_PRIORITY);
		this.knoten = knoten;
		this.edges = edges;
		this.vs = vs;
		this.nl = nl;
	}

	boolean force;

	public void run() {
		while (true) {
			change = false;
			if (force)
				forceBasedSort();
			moveInQuadrantOne(knoten);
			if (change)
				nl.refresh();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Berechnet den Abstand zwischen zwei Knoten
	 * 
	 * @param k1
	 *            Erster Knoten
	 * @param k2
	 *            Zweiter Knoten
	 * @return Abstand der beiden Knoten
	 */

	public static double d(Node k1, Node k2) {
		return Math.sqrt((k1.x - k2.x) * (k1.x - k2.x) + (k1.y - k2.y)
				* (k1.y - k2.y));
	}

	/**
	 * Verschiebt alle Knoten, so dass die Koordinaten positiv sind.
	 * 
	 * @param knoten
	 *            Knoten des Graphen
	 * @param anzahl
	 *            Anzahl der Knoten
	 */

	public static void moveInQuadrantOne(Vector<Node> knoten) {
		if (knoten.size() > 0) {
			long minX = knoten.get(0).x;
			long minY = knoten.get(0).y;
			for (int i = 0; i < knoten.size(); i++) {
				if (knoten.get(i).x < minX)
					minX = knoten.get(i).x;
				if (knoten.get(i).y < minY)
					minY = knoten.get(i).y;
			}
			if (minX<5 || minY<5 || minX > 200 || minY > 200) {
				for (int i = 0; i < knoten.size(); i++) {
					knoten.get(i).x += ((-1) * minX) + 5;
					knoten.get(i).y += ((-1) * minY) + 5;
				}
			}
		}
	}

	/**
	 * Schüttelt die Knoten
	 * 
	 * @param knoten
	 *            Knoten des Graphen
	 * @param anzahl
	 *            Anzahl der Knoten
	 */

	public static void shake(Node[] knoten, int anzahl, int beben) {
		for (int i = 0; i < anzahl; i++) {
			if (!knoten[i].fix && !knoten[i].drag) {
				knoten[i].x += zufall.nextInt(beben) - (beben / 2);
				knoten[i].y += zufall.nextInt(beben) - (beben / 2);
			}
		}
		change = true;
	}

	/**
	 * Kräftebasiertes Verfahren zur Anordnung der Knoten. Dabei werden direkt
	 * die übergebenen Objekte manipuliert.
	 * 
	 * @param knoten
	 *            Knoten des Graphen
	 * @param kanten
	 *            Kanten des Graphen
	 * @param anzahl
	 *            Anzahl der Knoten
	 * @param kanzahl
	 *            Anzahl der Kanten
	 */

	public void forceBasedSort() {
		double k1 = 1.0;
		double k2 = 250000;
		double l = 120.0;
		double epsilon1 = 0.05;
		// Kraft: Vektor des R2 mit X und Y Komponente der auf einen Knoten
		// wirkt.
		double[][] kraft = new double[knoten.size()][2];
		Node u;
		Node v;
		for (int i = 0; i < knoten.size(); i++) {
			kraft[i][0] = 0;
			kraft[i][1] = 0;
		} // Kräfte berechnen:
		for (int i = 0; i < knoten.size(); i++) {
			v = knoten.get(i);
			for (int k = 0; k < knoten.size(); k++) {
				u = knoten.get(k);
				if (u != v && d(u, v) < 120) { // Zweite Bedingung, damit nicht
												// zusammenhängende Graphenteile
												// nicht uneendlich auseinander
												// driften.
					// Skalierung der Abstossung:
					double s = 2;
					// Kraft durch Abstoßung:
					kraft[k][0] += (-1) * (k2 / (d(u, v) * d(u, v)))
							* (v.x - u.x) / d(u, v) * s;
					kraft[k][1] += (-1) * (k2 / (d(u, v) * d(u, v)))
							* (v.y - u.y) / d(u, v) * s;
				}
			}
			for (int k = 0; k < edges.size(); k++) {
				if (edges.get(k).von == knoten.get(i)) {
					// Kraft durch Federn:
					u = edges.get(k).nach;
					kraft[knoten.indexOf(edges.get(k).nach)][0] += 1d / 2d * (k1 * (d(u, v) - l))
							* (v.x - u.x) / d(u, v);
					kraft[knoten.indexOf(edges.get(k).nach)][1] += 1d / 2d * (k1 * (d(u, v) - l))
							* (v.y - u.y) / d(u, v);
					kraft[i][0] += -1d / 2d * (k1 * (d(u, v) - l))
							* (v.x - u.x) / d(u, v);
					kraft[i][1] += -1d / 2d * (k1 * (d(u, v) - l))
							* (v.y - u.y) / d(u, v);
				}
			}
		} // Bewegen:
		for (int i = 0; i < knoten.size(); i++) {
			if (!knoten.get(i).fix && !knoten.get(i).drag) {
				v = knoten.get(i);
				v.x = v.x + (int) (kraft[i][0] * epsilon1);
				v.y = v.y + (int) (kraft[i][1] * epsilon1);
			}
		}
		change = true;
	}

	/**
	 * Kräftebasiertes Verfahren zur Anordnung der Knoten mit externer Kraft
	 * nach Südosten und fixierten ersten Knoten. Dabei werden direkt die
	 * übergebenen Objekte manipuliert.
	 * 
	 * @param knoten
	 *            Knoten des Graphen
	 * @param kanten
	 *            Kanten des Graphen
	 * @param anzahl
	 *            Anzahl der Knoten
	 * @param kanzahl
	 *            Anzahl der Kanten
	 */

	public void forceBasedSortSO() {
		double f = 50;
		double epsilon1 = 0.05;
		// Kraft: Vektor des R2 mit X und Y Komponente der auf einen Knoten
		// wirkt.
		double[][] kraft = new double[knoten.size()][2];
		Node v;
		for (int i = 0; i < knoten.size(); i++) {
			kraft[i][0] = f;
			kraft[i][1] = f;
			if (!knoten.get(i).fix && !knoten.get(i).drag) {
				v = knoten.get(i);
				v.x = v.x + (int) (kraft[i][0] * epsilon1);
				v.y = v.y + (int) (kraft[i][1] * epsilon1);
			}
		}
		change = true;
	}

	/**
	 * Hierarchisches Verfahren zur Anordnung der Knoten. Vorrausgesetzt wird,
	 * daß es ein gerichteter azyklischer Graph ist. Dabei werden direkt die
	 * übergebenen Objekte manipuliert.
	 * 
	 * @param knoten
	 *            Knoten des Graphen
	 * @param kanten
	 *            Kanten des Graphen
	 * @param anzahl
	 *            Anzahl der Knoten
	 * @param kanzahl
	 *            Anzahl der Kanten
	 */

	public static void hierarchicallySort(Vector<Node> knoten, Vector<Edge> kanten, VS vs) {
		int anzahl = knoten.size();
		int[] pos = new int[anzahl];
		int[] newpos = new int[anzahl];
		for (int i = 0; i < anzahl; i++) { // Alle auf Null setzen:
			newpos[i] = 0;
		}
		for (int k = 0; k < kanten.size(); k++) { // Oberste bestimmen:
			newpos[knoten.indexOf(kanten.get(k).nach)] = 1;
		}
		for (int i = 1; i <= anzahl; i++) {
			pos = copy(newpos);
			for (int k = 0; k < kanten.size(); k++) { // Wenn es noch einen auf gleicher Höhe gibt, von dem er abhängig ist:
				if (pos[knoten.indexOf(kanten.get(k).nach)] == i && pos[knoten.indexOf(kanten.get(k).von)] == i)
					newpos[knoten.indexOf(kanten.get(k).nach)] = i + 1;
			}
		}

		pos = copy(newpos);
		int y = 0;
		for (int p = 0; p <= anzahl; p++) {
			int x = 0;
			y += 100;
			for (int i = 0; i < anzahl; i++) {
				if (pos[i] == p) {
					x += 100;
					knoten.get(i).x = x;
					knoten.get(i).y = y;
				}
			}
		}
		moveInQuadrantOne(knoten);
	}

	public static int[] copy(int[] a) {
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++)
			b[i] = a[i];
		return b;
	}

	public static int getFirstInt(Node[] knoten, int anzahl, int[][] kanten,
			int kanzahl) {
		int[] newpos = new int[anzahl];
		for (int i = 0; i < anzahl; i++) { // Alle auf Null setzen:
			newpos[i] = 0;
		}
		for (int k = 0; k < kanzahl; k++) { // Oberste bestimmen:
			newpos[kanten[k][1]] = 1;
		}
		int first = -1;
		for (int i = 0; i < anzahl; i++) {
			if (first == -1 && newpos[i] == 0)
				first = i;
			// if (first != -1 && knoten[i].critic && newpos[i] == 0) first=i;
		}
		return first;
	}
}
