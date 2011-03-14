package org.mutoss.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.af.commons.errorhandling.ErrorDialog;
import org.af.commons.logging.ApplicationLog;
import org.af.commons.logging.LoggingSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.FormLayout;

public class ErrorDialogSGTK extends ErrorDialog {

    protected static Log logger = LogFactory.getLog(ErrorDialogSGTK.class);

    protected ApplicationLog al;
    protected JCheckBox chbAttachDf;

	public ErrorDialogSGTK(String msg, Object e, boolean fatal) {
		super(msg, e, fatal);		
	}

    protected JPanel getOptionalPanel() {
    	al = LoggingSystem.getInstance().getApplicationLog();
        /* chbAttachDf = new JCheckBox();
        chbAttachDf.setSelected(true);
        chbAttachDf.setEnabled(al.getDataFrame() != null);*/
        JPanel p = new JPanel();
        String cols = "left:pref, 5dlu, pref:grow";
        String rows = "pref";
        FormLayout layout = new FormLayout(cols, rows);
        p.setLayout(layout);    

        /* 
         CellConstraints cc = new CellConstraints();
         p.add(new JLabel(
                "attach Data=")
         ),                               cc.xy(1, 1));
         p.add(chbAttachDf,               cc.xy(3, 1)); */
        return p;
    }

    protected Hashtable<String, File> getAttachedFiles() throws IOException {
    	Hashtable<String, File> files = new Hashtable<String, File>();
    	files.put("rcommands", makeLogFile("r_commands.txt", collapseStringList(RControl.getR().getHistory(),"\n")));
        files.put("sessioninfo", makeLogFile("session_info.txt", getRSessionInfo()));
        files.put("roptions", makeLogFile("r_options.txt", getROptions()));
        //files.put("packageinfo", makeLogFile("package_info.txt", getRPackageInfo()));
        files.put("systeminfo", makeLogFile("system_info.txt", getSystemInfo()));
        files.put("log", getReadableLogFile());        

        /* if (chbAttachDf.isSelected() && al.getDataFrame()!=null)
            files.put("", al.getDataFrame()); */
        return files;
    }
    
    private String getSystemInfo() {		
		return al.getSystemInfo();
	}

	private String getROptions() {		
		return collapseStringArray(RControl.getR().eval("paste(capture.output(options()), collapse=\"\\n\")").asRChar().getData());
	}

	private String getRSessionInfo() {
		return collapseStringArray(RControl.getR().eval("paste(capture.output(sessionInfo()), collapse=\"\\n\")").asRChar().getData());
	}

	/**
     * @return the location of the (human readable) log file
     */
    public File getReadableLogFile() {
        return new File(getReadableLogFileAppender().getFile());
    }
    
    /**
     * @return the FileAppender for the (human readable) log file 
     */
    public FileAppender getReadableLogFileAppender() {
        return (FileAppender)Logger.getRootLogger().getAppender("READABLE_FILE");
    }

    protected File makeLogFile(String fileName, String content) throws IOException{
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File output = new File(tempDir, fileName);
        FileWriter fw = new FileWriter(output);
        fw.write(content);
        fw.close();
        return output;
    }
    
    private String collapseStringArray(String[] ss, String sep) {
        String result = "";
        for (String s : ss) {
            result  += s + sep;
        }
        return result;
    }
    
    private String collapseStringList(List<String> ss, String sep) {
        String result = "";
        for (String s : ss) {
            result  += s + sep;
        }
        return result;
    }

    private String collapseStringArray(String[] ss) {
        return collapseStringArray(ss, "\n");
    }

    protected Hashtable<String, String> getInfoTable() {
    	Hashtable<String, String> table = super.getInfoTable();
    	table.put("Subject", "gMCP bug report from "+System.getProperty("user.name", "<unknown user name>")+" on "+System.getProperty("os.name", "<unknown OS>"));
    	return table;
    }
  
    
}

