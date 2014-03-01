package org.af.gMCP.poi;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.af.gMCP.gui.CreateGraphGUI;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class GraphDocXWriter {

	CreateGraphGUI gui;
	
	public GraphDocXWriter(CreateGraphGUI gui) {
		this.gui = gui;
	}	
	
	public static void addImage(XWPFParagraph p, BufferedImage image) throws IOException, InvalidFormatException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "gif", os);
		InputStream picIS = new ByteArrayInputStream(os.toByteArray());
		
		XWPFRun run = p.createRun();
		run.addPicture(picIS, XWPFDocument.PICTURE_TYPE_JPEG, "logo.JPG",300,300);
	}
	
	/* Stuff one could use:
	 * run.setSubscript(VerticalAlign.SUBSCRIPT);
	 * run.addBreak();
	 * run.setTextPosition(120);
	 * p.setAlignment(ParagraphAlignment.DISTRIBUTE);
	 * p.setIndentationRight(150);
	 * doc.createParagraph().createRun().addBreak();
	 */
	
	public void createDocXReport(File file) throws IOException, InvalidFormatException {
		XWPFDocument doc = new XWPFDocument();
		
		XWPFParagraph p = doc.createParagraph();
		p.setAlignment(ParagraphAlignment.LEFT);
		setAllBorders(p, Borders.SINGLE);
		
		XWPFRun run = p.createRun();
		run.setFontSize(24);
		run.setBold(true);
		run.setText("gMCP Report");
		
		p = doc.createParagraph();
		addImage(p, gui.getGraphView().getNL().getImage(1));
		
		FileOutputStream fos = new FileOutputStream(file);
		doc.write(fos);
		fos.close();
	}
	
	private static void setAllBorders(XWPFParagraph p, Borders type) {
		p.setBorderBottom(type);
		p.setBorderTop(type);
		p.setBorderRight(type);
		p.setBorderLeft(type);
		p.setBorderBetween(type);		
	}

	public static void createTable(XWPFDocument doc) {
		XWPFTable table = doc.createTable();
		XWPFTableRow row = table.getRow(0);
		row.getCell(0).setText("1 - 1");
		row.addNewTableCell().setText("1 - 2");

		row = table.createRow();
		row.getCell(0).setText("2 - 1");
		row.getCell(1).setText("2 - 2");
	}
}
