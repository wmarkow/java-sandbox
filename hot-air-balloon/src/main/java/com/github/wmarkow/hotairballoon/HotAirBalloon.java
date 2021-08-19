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
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftArrowPressed = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightArrowPressed = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftArrowPressed = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightArrowPressed = false;
				}
			}
		});

		int requiredFps = 30;
		long period = 1000 / requiredFps;

		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				onFrame();
			}
		};

		timer.scheduleAtFixedRate(timerTask, 0, period);
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
	
	private static void onFrame() {
		if (rightArrowPressed == true) {
			int newX = balloon.getX() + 10;
			if (newX > 1024 - 64) {
				newX = 1024 - 64;
			}
			balloon.setX(newX);

		}

		if (leftArrowPressed == true) {
			int newX = balloon.getX() - 10;
			if (newX < 0) {
				newX = 0;
			}
			balloon.setX(newX);
		}
	}
}

//error: Kiedy przytrzymam myszke i klikne strzalke, to moge sterowac okienkiem
//pojawia sie czerwony prostokat i mozna nim sterowac strzalkami: prawa i lewa