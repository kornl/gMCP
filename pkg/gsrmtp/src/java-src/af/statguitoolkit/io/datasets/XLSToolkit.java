package org.af.statguitoolkit.io.datasets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Toolkit class to transform XLS files to CSV files.
 * Was necessary as there are (apparently) no mature methods in R available
 * to read XLS data files in a Unix OS. For example, read.xls in gdata does not
 * like XLS files which were created with Open Office.
 * This class uses the POI library from apache.
 */
public class XLSToolkit {

    /**
     * Convert XLS file to CSV file
     *
     * @param xls location of XLS file
     * @param sheetNr nr of sheet to convert
     * @param outFile location of CSV output file
     * @param sep CSV separator for fields in a row
     * @throws IOException when IO goes wrong
     */
    public static void convertXls2Csv(File xls, int sheetNr, File outFile, String sep) throws IOException {

        // load XLS file as workbook,get the sheet and all rows
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(xls));
        HSSFSheet sheet = wb.getSheetAt(sheetNr);
        Iterator rowIt = sheet.rowIterator();

        // nr of nonempty cols
        int nonEmpty = Integer.MIN_VALUE;
        // loop thru rows and calc. number of nonmepty columns
        while (rowIt.hasNext()) {
            // get all cells from row
            HSSFRow row = (HSSFRow) rowIt.next();
            Iterator cellIt = row.cellIterator();
            // length of row
            int len = 0;
            // nr of empty cells on right margin
            int empt = 0;

            while (cellIt.hasNext()) {
                String x = cellIt.next().toString();
                len++;
                if (x.trim().equals(""))
                    empt++;
                else
                    // if non-empty cell found start counting empties again
                    empt = 0;
            }
            nonEmpty = Math.max(nonEmpty, len - empt);
        }


        // loop again and write them to output
        rowIt = sheet.rowIterator();
        // writer for output
        BufferedWriter w = new BufferedWriter(new FileWriter(outFile));
        while (rowIt.hasNext()) {
            // get all cells from row
            HSSFRow row = (HSSFRow) rowIt.next();
            Iterator cellIt = row.cellIterator();
            int count = 0;
            // write cells + seps, but not more cells than nonmpty nr of cols
            while (cellIt.hasNext() && (count < nonEmpty)) {
                w.write(cellIt.next().toString());
                count++;
                if (cellIt.hasNext())
                    w.write(sep);
            }
            // to few cells --> append empty cells
            if (count < nonEmpty) {
                for (int i=0; i<nonEmpty-count;i++)
                    w.write(sep);
            }
            w.newLine();
        }
        w.close();
    }

    /**
     * Reads table names from a XLS File
     *
     * @param file XLS File from which table names will be read.
     * @return List of XLS-table names
     */
    public static List<String> getXLSTables(File file) throws FileNotFoundException, IOException {
        // list for table names
        List<String> result = new ArrayList<String>();
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
        // read all sheet names
        for (int i=0; i<wb.getNumberOfSheets(); i++) {
            result.add(wb.getSheetName(i));
        }
        return result;
    }
    
    /**
     * Reads table names from a XLSX File
     *
     * @param file XLSX File from which table names will be read.
     * @return List of XLSX-table names
     */
    public static List<String> getXLSXTables(File file) throws FileNotFoundException, IOException {
    	return null;
    }
    
    /**
     * Convert XLSX file to CSV file
     *
     * @param xls location of XLSX file
     * @param sheetNr nr of sheet to convert
     * @param outFile location of CSV output file
     * @param sep CSV separator for fields in a row
     * @throws IOException when IO goes wrong
     */
    public static void convertXlsX2Csv(File xlsx, int sheetNr, File outFile, String sep) throws IOException {
    	
    }
}