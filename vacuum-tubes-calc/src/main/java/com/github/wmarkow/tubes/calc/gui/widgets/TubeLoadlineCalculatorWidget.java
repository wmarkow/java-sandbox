package com.github.wmarkow.tubes.calc.gui.widgets;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.wmarkow.tubes.calc.domain.TubeModelIf;

public class TubeLoadlineCalculatorWidget extends JPanel {

    private static final long serialVersionUID = -4338135663541844076L;

    private final static int DEFAULT_MAX_LOAD_RESISTOR_VALUE = 60000;
    private final static int DEFAULT_MAX_BIAS_RESISTOR_VALUE = 1000;

    private TubeLoadlineChart chart;
    private JSlider loadSlider;
    private JSlider powerSupplySlider;
    private JSlider biasResistorSlider;

    public TubeLoadlineCalculatorWidget() {
	super();

	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	chart = new TubeLoadlineChart();
	loadSlider = new JSlider();
	loadSlider.setMinimum(0);
	loadSlider.setMaximum(DEFAULT_MAX_LOAD_RESISTOR_VALUE);
	loadSlider.setMajorTickSpacing(5000);
	loadSlider.setMinorTickSpacing(1000);
	loadSlider.setPaintTicks(true);
	loadSlider.setPaintLabels(true);
	loadSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent aE) {
		JSlider source = (JSlider) aE.getSource();
		double load = source.getValue();
		double voltage = powerSupplySlider.getValue();

		chart.setLoadAndSupplyVoltage(load, voltage);
		chart.setBiasResistance(biasResistorSlider.getValue());
	    }
	});

	powerSupplySlider = new JSlider();
	powerSupplySlider.setMajorTickSpacing(50);
	powerSupplySlider.setMinorTickSpacing(10);
	powerSupplySlider.setPaintTicks(true);
	powerSupplySlider.setPaintLabels(true);
	powerSupplySlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent aE) {
		JSlider source = (JSlider) aE.getSource();
		double load = loadSlider.getValue();
		double voltage = source.getValue();

		chart.setLoadAndSupplyVoltage(load, voltage);
		chart.setBiasResistance(biasResistorSlider.getValue());
	    }
	});

	biasResistorSlider = new JSlider();
	biasResistorSlider.setMinimum(0);
	biasResistorSlider.setMaximum(DEFAULT_MAX_BIAS_RESISTOR_VALUE);
	biasResistorSlider.setMajorTickSpacing(50);
	biasResistorSlider.setMinorTickSpacing(10);
	biasResistorSlider.setPaintTicks(true);
	biasResistorSlider.setPaintLabels(true);
	biasResistorSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent aE) {
		JSlider source = (JSlider) aE.getSource();
		double resistance = source.getValue();

		chart.setBiasResistance(resistance);
	    }
	});

	add(chart);
	add(loadSlider);
	add(powerSupplySlider);
	add(biasResistorSlider);
    }

    public void setTubeModel(TubeModelIf model) {
	chart.setTubeModel(model);

	int maxVoltage = (int) model.getMaxV_A();
	powerSupplySlider.setMinimum(0);
	powerSupplySlider.setMaximum(maxVoltage);
	powerSupplySlider.setValue(maxVoltage / 2);

	loadSlider.setValue(DEFAULT_MAX_LOAD_RESISTOR_VALUE / 2);

	biasResistorSlider.setValue(DEFAULT_MAX_BIAS_RESISTOR_VALUE / 2);
    }
}
