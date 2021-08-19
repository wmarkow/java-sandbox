package com.github.wmarkow.hotairballoon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Balloon extends JLabel {

	private int x;
	private int y;

	public Balloon() {
		super();
		this.setMinimumSize(new Dimension(64, 128));
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(0, 0, 64, 128);
	}

	public void setX(int x) {
		this.x = x;

		this.setBounds(x, y, 64, 128);
	}

	public void setY(int y) {
		this.y = y;
		this.setBounds(x, y, 64, 128);
	}
}
