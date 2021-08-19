package com.github.wmarkow.hotairballoon;

import java.awt.Dimension;

import javax.swing.JFrame;

public class HotAirBalloon {

	private static Balloon balloon;
	
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Hot Air Balloon");
		frame.setMinimumSize(new Dimension(1024, 768));
		frame.setLayout(null);
		balloon = new Balloon();
		balloon.setBounds(0,0,64,128);
		frame.getContentPane().add(balloon);
		frame.pack();
		frame.setVisible(true);

		
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
