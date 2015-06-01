package apps;

import gui.MultipleSeriesOriginalInduction;
import gui.SingleSeriesOriginalInduction;

import java.awt.EventQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CollectGUIApp {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
	    BasicConfigurator.configure();
	    Logger.getRootLogger().setLevel(Level.WARN);
	    Logger.getLogger(CollectGUIApp.class).setLevel(Level.INFO);
	    
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MultipleSeriesOriginalInduction mainWindow= new MultipleSeriesOriginalInduction();
					mainWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
