package org.af.statguitoolkit.gui.datawizard;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.af.commons.Localizer;
import org.af.commons.io.FileExtensionFilter;
import org.af.commons.widgets.MyJFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.spi.wizard.WizardPage;

abstract public class LoadPanel extends WizardPage implements ActionListener {
    private static Log logger = LogFactory.getLog(LoadPanel.class);

    protected JTextField tfFile = new JTextField(30);
    protected JTextArea taPreview;
    protected JButton bSelect;
    protected JTextField tfNA;
    protected JFormattedTextField tfDec;
    protected JCheckBox chbHeader;

    /**
     * Instantiation of Swing-Components.
     */

    protected void makeComponents() {
        tfFile.setEditable(false);
        taPreview = new JTextArea(10, 30);
        taPreview.setEditable(false);
        bSelect = new JButton("Select");
        bSelect.addActionListener(this);
        tfNA = new JTextField("NA", 2);
        try {
            tfDec = new JFormattedTextField(new MaskFormatter("*"));
            tfDec.setText(".");
        } catch (ParseException e) {
            logger.error("This should not happen!",e);
        }
        chbHeader = new JCheckBox("", true);
    }

    abstract protected void doTheLayout();

    protected void selectFile(String[] extensions) {
        MyJFileChooser fc = MyJFileChooser.makeMyJFileChooser();
        fc.removeChoosableFileFilter(fc.getFileFilter());
        for (String e : extensions)
            fc.addChoosableFileFilter(new FileExtensionFilter(e, e, true));
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            setFile(f);
        }
    }

    protected void setFile(File file) {
        setPreview(getPreview(file));
        tfFile.setText(file.getAbsolutePath());
    }    

    protected void setPreview(List<String> preview) {
        taPreview.setText("");
        for (String s:preview) {
            taPreview.append(s + "\n");
        }
        taPreview.setCaretPosition(0);
    }

    protected List<String> readLines(File file, int n) {
        List<String> result = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            for (int i=0; i<n; i++) {
                String s = reader.readLine();
                if (s!=null)
                    result.add(s);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_LOAD_IOERR"));
        }
        return result;
    }

    abstract protected List<String> getPreview(File file);


}
