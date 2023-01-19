package com.github.wmarkow.tubes.calc.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.github.wmarkow.tubes.calc.domain.ECL84PentodeModel;
import com.github.wmarkow.tubes.calc.gui.widgets.TubeLoadlineCalculatorWidget;

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

	TubeLoadlineCalculatorWidget loadlineWidget = new TubeLoadlineCalculatorWidget();
	loadlineWidget.setTubeModel(new ECL84PentodeModel());
	// loadlineWidget.setTubeModel(new ECL84TriodeModel());
	frame.getContentPane().add(loadlineWidget);

	frame.pack();
	frame.setVisible(true);
    }

}
