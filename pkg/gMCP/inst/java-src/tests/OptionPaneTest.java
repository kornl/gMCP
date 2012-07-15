package tests;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class OptionPaneTest extends JFrame {
	public static void main(String[] args) {
		JCheckBox tellMeAgain = new JCheckBox("Don't show me this info again.");
		String message = "This test is appropriate if the p-values\n" +
				"belong to one-sided test-statistics with a joint\n" +
				"multivariate normal null distribution.";
		JOptionPane.showConfirmDialog(new JFrame(), new Object[] {message, tellMeAgain}, "Info", JOptionPane.OK_OPTION);
		System.out.println(!tellMeAgain.isSelected());
	}

}
