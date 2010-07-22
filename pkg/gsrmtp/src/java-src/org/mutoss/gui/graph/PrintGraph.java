package org.mutoss.gui.graph;

import java.util.Arrays;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;


public class PrintGraph {
	public static void print(NetzListe nl) {
		final String sCrLf = System.getProperty("line.separator");
		final String sErrNoPrintService = sCrLf
				+ "Es ist kein passender Print-Service installiert.";

		// Set DocFlavor and print attributes:
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(MediaSizeName.ISO_A4);

		try {
			// Print to PrintService (e.g. to Printer):
			PrintService prservDflt = PrintServiceLookup
					.lookupDefaultPrintService();
			PrintService[] prservices = PrintServiceLookup.lookupPrintServices(
					flavor, aset);
			if (prservices == null || 0 >= prservices.length)
				if (prservDflt != null) {
					System.err
							.println("Nur Default-Printer, da lookupPrintServices fehlgeschlagen.");
					prservices = new PrintService[] { prservDflt };
				} else {
					throw new RuntimeException(sErrNoPrintService);					
				}
			System.out.println("Print-Services:");
			for (int i = 0; i < prservices.length; i++)
				System.out.println("  " + i + ":  " + prservices[i]
						+ ((prservDflt != prservices[i]) ? "" : " (Default)"));
			PrintService prserv = null;
			if (!Arrays.asList(prservices).contains(prservDflt))
				prservDflt = null;
			prserv = ServiceUI.printDialog(null, 50, 50, prservices,
					prservDflt, null, aset);

			if (prserv != null) {
				System.out.println("Ausgewaehlter Print-Service:");
				System.out.println("      " + prserv);
				printPrintServiceAttributesAndDocFlavors(prserv);
				DocPrintJob pj = prserv.createPrintJob();

				Doc doc = new SimpleDoc(new PrintableObject(nl), flavor, null);
				pj.print(doc, aset);
				System.out.println("Grafik ist erfolgreich gedruckt.");

			}

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void printPrintServiceAttributesAndDocFlavors(
			PrintService prserv) {
		String s1 = null, s2;
		Attribute[] prattr = prserv.getAttributes().toArray();
		DocFlavor[] prdcfl = prserv.getSupportedDocFlavors();
		if (null != prattr && 0 < prattr.length)
			for (int i = 0; i < prattr.length; i++)
				System.out.println("      PrintService-Attribute[" + i + "]: "
						+ prattr[i].getName() + " = " + prattr[i]);
		if (null != prdcfl && 0 < prdcfl.length)
			for (int i = 0; i < prdcfl.length; i++) {
				s2 = prdcfl[i].getMimeType();
				if (null != s2 && !s2.equals(s1))
					System.out.println("      PrintService-DocFlavor-Mime[" + i
							+ "]: " + s2);
				s1 = s2;
			}
	}
}