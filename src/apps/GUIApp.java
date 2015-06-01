package apps;

import gui.SingleSeriesOriginalInduction;

import java.awt.EventQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class GUIApp {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
	    BasicConfigurator.configure();
	    Logger.getRootLogger().setLevel(Level.WARN);
	    Logger.getLogger(GUIApp.class).setLevel(Level.INFO);
	    
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SingleSeriesOriginalInduction mainWindow= new SingleSeriesOriginalInduction();
					mainWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
