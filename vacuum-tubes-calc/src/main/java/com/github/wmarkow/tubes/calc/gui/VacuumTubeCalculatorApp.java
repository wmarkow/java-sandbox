package com.github.wmarkow.tubes.calc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.github.wmarkow.tubes.calc.domain.AbstractTubeModel;
import com.github.wmarkow.tubes.calc.domain.ECL84PentodeModel;
import com.github.wmarkow.tubes.calc.domain.ECL84TriodeModel;
import com.github.wmarkow.tubes.calc.domain.EL84Model;
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
	frame.getContentPane().setLayout(new BorderLayout());
	
	TubeLoadlineCalculatorWidget loadlineWidget = new TubeLoadlineCalculatorWidget();

	JComboBox<AbstractTubeModel> comboBox = new JComboBox<AbstractTubeModel>();
	comboBox.addItem(new EL84Model());
	comboBox.addItem(new ECL84TriodeModel());
	comboBox.addItem(new ECL84PentodeModel());
	comboBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent aE) {
		AbstractTubeModel atm = (AbstractTubeModel) comboBox.getSelectedItem();

		loadlineWidget.setTubeModel(atm);
	    }
	});
	comboBox.setSelectedIndex(0);

	frame.getContentPane().add(comboBox, BorderLayout.NORTH);
	frame.getContentPane().add(loadlineWidget, BorderLayout.CENTER);

	frame.pack();
	frame.setVisible(true);
    }

}
