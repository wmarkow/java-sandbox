package com.github.wmarkow.hotairballoon;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class HotAirBalloon {

	private static Balloon balloon;

	private static Timer timer;
	private static TimerTask timerTask;

	private static boolean leftArrowPressed = false;
	private static boolean rightArrowPressed = false;

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Hot Air Balloon");
		frame.setMinimumSize(new Dimension(1024, 768));
		frame.setLayout(null);
		balloon = new Balloon();
		balloon.setX(512);
		balloon.setY(500);

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

//error: Kiedy przytrzymam myszke i klikne strzalke, to moge sterowac okienkiem
//pojawia sie czerwony prostokat i mozna nim sterowac strzalkami: prawa i lewa