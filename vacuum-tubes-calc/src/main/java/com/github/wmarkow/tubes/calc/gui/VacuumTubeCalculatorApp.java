//******************************************************************
//                                                                 
//  VacuumTubeCalculatorApp.java                                               
//  Copyright 2023 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.tubes.calc.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.github.wmarkow.tubes.calc.gui.widgets.TubeOutputCharacteristicChart;

public class VacuumTubeCalculatorApp {

    public static void main(String[] args) {
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		createAndShowGUI();
	    }
	});
    }

    private static void createAndShowGUI() {
	JFrame frame = new JFrame("Vacuum tube calculator");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setMinimumSize(new Dimension(1024, 768));

	TubeOutputCharacteristicChart chart = new TubeOutputCharacteristicChart();
	frame.getContentPane().add(chart);

	frame.pack();
	frame.setVisible(true);
    }

}
