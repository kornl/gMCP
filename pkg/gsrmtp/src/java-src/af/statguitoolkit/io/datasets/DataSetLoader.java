package org.af.statguitoolkit.io.datasets;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.af.jhlir.call.RDataFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to load all different versions of data sets.
 */
public class DataSetLoader {
    private static final Log logger = LogFactory.getLog(DataSetLoader.class);

    // singelton
    private static DataSetLoader instance = null;
    // TODO maybe remove this and make Rcontrol singelton too?
    // Rcontrol for reading csv files and loading R data sets
    // for creation of names of temp files
    private Random random = new Random();

    /**
     * Private constructor for singelton
     */
    private DataSetLoader() {
    }

    /**
     * @return singelton instance
     * @throws RuntimeException when RControl was not registered before
     */
    public static DataSetLoader getInstance() {
        if (instance == null) {
            instance = new DataSetLoader();
        }
        return instance;
    }

    /**
     * Loads an XLS-File.
     * Converts it to CSV then uses RControl.
     *
     * @param file    XLS-File to read
     * @param sheet   sheet to read from
     * @param header  is header present?
     * @param dec     decimal point in numbers
     * @param missing character string to represent missing values
     * @return R data frame with data from xls-file
     * @throws RFileFormatException when the file could not be read by R (which will probably beecause it's not in XLS formart) .
     * @throws IOException          when conversion to CSV failed
     */

    private RDataFrame loadXLS(File file, int sheet, boolean header, Character dec, String missing) throws RFileFormatException, IOException {
        logger.info("Converting to CSV: " + file.getAbsolutePath());
        logger.info("Sheet: " + sheet);
        long nr = Math.abs(random.nextLong());
        String name = "tempcsv_" + nr + ".csv";
        File csv = new File(getProjectDataSetsPath(), name);
        logger.info("Output CSV: " + csv.getAbsolutePath());
        XLSToolkit.convertXls2Csv(file, sheet, csv, ";");
        logger.info("Loading CSV");
        RDataFrame dfR = loadCSV(csv, header, ";", dec, missing);
      //TODO CleanUpOnShutDown.getInstance().registerTempFile(csv);
        return dfR;
    }


    private RDataFrame loadCSV(File csv, boolean header, String string,
			Character dec, String missing) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getProjectDataSetsPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public RDataFrame load(DataFrameDescriptor dfd) throws RFileFormatException, IOException {
        RDataFrame dfR;
        if (dfd instanceof RDataFrameDescriptor) {
            RDataFrameDescriptor rdfd = (RDataFrameDescriptor) dfd;
            dfR = loadRDataSet(rdfd.getName(), rdfd.getRPackage());
            /* File tempDir = new File(Configuration.getInstance().getGeneralConfig().getTempDir());
            File f = new File(tempDir, "r_dataframe_" + rdfd.getRPackage() + "_" + rdfd.getName());
            f.createNewFile(); */
            //LoggingSystemSGTK.getInstance().getApplicationLog().logDataFrame(f);
        } else if (dfd instanceof RVariableDescriptor) {
            dfR = ((RVariableDescriptor) dfd).getDataFrame();
            //LoggingSystemSGTK.getInstance().getApplicationLog().logDataFrame(null);
        } else if (dfd instanceof FileDescriptorXLS) {
            FileDescriptorXLS xls = (FileDescriptorXLS) dfd;
            dfR = loadXLS(xls.file, xls.sheet, xls.headerInFile, xls.dec, xls.na);
            //LoggingSystemSGTK.getInstance().getApplicationLog().logDataFrame(xls.file);
         } else {
            FileDescriptorCSV csv = (FileDescriptorCSV) dfd;
            dfR = loadCSV(csv.file, csv.headerInFile, csv.separator, csv.dec, csv.na);
            //LoggingSystemSGTK.getInstance().getApplicationLog().logDataFrame(csv.file);
        }        
        //dfR.setName(dfd.getTitle());
        return dfR;
    }

	private RDataFrame loadRDataSet(String name, String rPackage) {
		// TODO Auto-generated method stub
		return null;
	}

}


