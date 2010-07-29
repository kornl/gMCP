package org.af.statguitoolkit.io.datasets;

/**
 * Superclass. Is returned by the DataWizard / Import Step. Derived
 * classes describe dataframes from various file formats (xls, csv)
 * or data sets from R
 */

// TODO the design of the descriptors seems to be a bit awkward
// especially the loading part: instanceof is used
// maybe a descriptor should be able to load itself?


public abstract class DataFrameDescriptor {
    /**
     * @return short textual description
     */
    public abstract String getTitle();
}
