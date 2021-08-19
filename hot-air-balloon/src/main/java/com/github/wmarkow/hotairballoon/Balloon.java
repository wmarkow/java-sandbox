package com.github.wmarkow.hotairballoon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Balloon extends JLabel {

	public Balloon() {
		super();
		this.setMinimumSize(new Dimension(64, 128));
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(0, 0, 64, 128);
	}
}
