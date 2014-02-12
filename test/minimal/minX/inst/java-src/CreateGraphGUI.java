
import javax.swing.JFrame;

public class CreateGraphGUI extends JFrame {
	 
	public CreateGraphGUI() {
		setVisible(true);

		for (int i=0; i<10; i++) {
			new Thread(new Runnable() {
				public void run() {
					for (int i=0; i<20; i++) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}				
					}
				}
			}).start();
		}
	}
	
	public static void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CreateGraphGUI();
			}
		});		
	}	
}
