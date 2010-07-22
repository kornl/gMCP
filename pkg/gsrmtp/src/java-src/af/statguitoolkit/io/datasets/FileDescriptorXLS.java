package org.af.statguitoolkit.io.datasets;

import java.io.File;

/**
 * Describes dataframes imported from XLS files
 */


public class FileDescriptorXLS extends FileDescriptor{
    // nr of sheet in XLS file
    public final int sheet;
    public final String sheetname;

    /**
     * Constructor
     *
     * @param file location of file with data
     * @param sheet nr of sheet in XLS file
     * @param headerInFile is a header present, which describes var. names?
     * @param na encoding for missing values
     * @param dec how is the dec point encoded
     */

    public FileDescriptorXLS(File file, int sheet, String sheetname, boolean headerInFile, String na, Character dec) {
        super(file, headerInFile,  na, dec);
        this.sheet = sheet;
        this.sheetname = sheetname;
    }

    /**
     * Constructor
     *
     * @param path absolute path of file with data
     * @param sheet nr of sheet in XLS file
     * @param headerInFile is a header present, which describes var. names?
     * @param na encoding for missing values
     * @param dec how is the dec point encoded
     */

    public FileDescriptorXLS(String path, int sheet, String sheetname, boolean headerInFile, String na, Character dec) {
        this(new File(path), sheet, sheetname, headerInFile,  na, dec);
    }
    
//	public String getSourcePath() {
//		return file.getName()+", sheet: "+sheet;
//	}

    @Override
    public String getTitle() {
		return file.getAbsolutePath()+", sheet: "+sheetname+" ("+sheet+")";
    }
}
