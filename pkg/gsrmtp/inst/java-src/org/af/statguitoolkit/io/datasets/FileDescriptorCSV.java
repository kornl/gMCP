package org.af.statguitoolkit.io.datasets;

import java.io.File;

/**
 * Describes dataframes imported from CSV files
 */


public class FileDescriptorCSV extends FileDescriptor{
    //how are fields separated in a row
    public final String separator;

    /**
     * Constructor
     *
     * @param file location of file with data
     * @param headerInFile is a header present, which describes var. names?
     * @param na encoding for missing values
     * @param dec how is the dec point encoded
     * @param separator how are fields separated in a row
     */

    public FileDescriptorCSV(File file, boolean headerInFile, String na, Character dec, String separator) {
        super(file, headerInFile, na, dec);
        this.separator = separator;
    }

    /**
     * Constructor
     *
     * @param path absolute path of file with data
     * @param headerInFile is a header present, which describes var. names?
     * @param na encoding for missing values
     * @param dec how is the dec point encoded
     * @param separator how are fields separated in a row
     */
    public FileDescriptorCSV(String path, boolean headerInFile, String na, Character dec, String separator) {
        super(path, headerInFile, na, dec);
        this.separator = separator;
    }
}
