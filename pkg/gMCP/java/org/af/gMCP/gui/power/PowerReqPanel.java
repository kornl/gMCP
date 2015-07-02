package org.af.gMCP.gui.power;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.DesktopPaneBG;
import org.af.gMCP.gui.graph.LaTeXTool;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PowerReqPanel extends JPanel implements ActionListener {
	List<PowerReq> gv = new Vector<PowerReq>();
	JButton addScenario = new JButton("Add scenario");
	JButton rmScenario = new JButton("Remove last scenario");
	
	SampleSizeDialog sd;
	
	JButton jbHelp;
	
	JPanel panel = new JPanel();
	
	public PowerReqPanel(SampleSizeDialog sd) {
		this.sd = sd;
		gv.add(new PowerReq(sd, "Power requirement "+(gv.size()+1)));
		setUpLayout();
	}
	
	
	// Add θ and standard error of θ.
	// Allocation ratio?
	
	public void setUpLayout() {

		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, fill:min:grow, pref, 5dlu, pref, 5dlu";
		String rows = "5dlu, fill:min:grow, 5dlu, pref, 5dlu";
		
		setLayout(new FormLayout(cols, rows));

		add(new JScrollPane(getMainPanel()), cc.xyw(2, row, 4));
		
		row += 2;
		
		add(addScenario, cc.xy(3, row));
		add(rmScenario, cc.xy(5, row));
		addScenario.addActionListener(this);
		rmScenario.addActionListener(this);
		rmScenario.setEnabled(false);
		
		try {
			jbHelp = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
							.getResource("/org/af/gMCP/gui/graph/images/edge.png"))));
		} catch (IOException e) {
			ErrorHandler.getInstance().makeErrDialog(e.getMessage(), e);
			jbHelp = new JButton("Help!");
		}
		jbHelp.addActionListener(this);
		
	}

	public JPanel getMainPanel() {
		panel.removeAll();

		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
		String rows = "5dlu, pref, 5dlu";
		for (Node n : sd.getNodes()) {
			cols += ", pref, 5dlu";
		}
		for (PowerReq g : gv) {
			rows += ", pref, 5dlu";
		}

		panel.setLayout(new FormLayout(cols, rows));

		int col = 2;
		panel.add(new JLabel("Scenario name"), cc.xy(col, row));

		for (Node n : sd.getParent().getGraphView().getNL().getNodes()) {
			col += 2;
			panel.add(new JLabel("NCP "+ LaTeXTool.LaTeX2UTF(n.getName())+"    "), cc.xy(col, row));
		}

		for (PowerReq g : gv) {
			row += 2;
			g.addComponents(panel, cc, row);
		}
		return panel;
	}

	public String getNCPString() {
		String sList = ", list(";
		for (PowerReq g : gv) {
			//sList += g.getNCPString()+", ";
		}
		return sList.substring(0, sList.length()-2)+")";
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==addScenario) {
			gv.add(new PowerReq(sd, "Group "+(gv.size()+1)));
			getMainPanel();
			revalidate();
			repaint();
			rmScenario.setEnabled(true);
		} else if (e.getSource()==rmScenario) {
			if (gv.size()>1) {
				gv.remove(gv.size()-1);
				getMainPanel();
				revalidate();
				repaint();
			}
			if (gv.size()==1) {
				rmScenario.setEnabled(false);
			}
		}		
	}	
	
}
