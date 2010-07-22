package org.af.statguitoolkit.io.datasets;

import java.io.File;

/**
 * Describes dataframes imported from files
 */


public class FileDescriptor extends DataFrameDescriptor {
    // members. can be public as they are final

    // location of file with data
    public final File file;
    // is a header present, which describes var. names?
    public final boolean headerInFile;
    // na encoding for missing values
    public final String na;
    // how is the dec point encoded
    public final Character dec;

    /**
     * Constructor
     *
     * @param file location of file with data
     * @param headerInFile is a header present, which describes var. names?
     * @param na encoding for missing values
     * @param dec how is the dec point encoded
     */
    public FileDescriptor(File file, boolean headerInFile, String na, Character dec) {
        this.file = file;
        this.headerInFile = headerInFile;
        this.na = na;
        this.dec = dec;
    }

    /**
     * Constructor
     *
     * @param path absolute path of file with data 
     * @param headerInFile is a header present, which describes var. names?
     * @param na encoding for missing values
     * @param dec how is the dec point encoded
     */
    public FileDescriptor(String path, boolean headerInFile, String na, Character dec) {
        this(new File(path), headerInFile, na, dec);
    }

    public String getTitle() {
        return file.getName();
    }
}
