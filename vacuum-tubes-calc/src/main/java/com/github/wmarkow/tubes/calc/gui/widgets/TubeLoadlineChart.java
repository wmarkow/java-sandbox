package com.github.wmarkow.tubes.calc.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.github.wmarkow.tubes.calc.domain.BiasPoint;
import com.github.wmarkow.tubes.calc.domain.LoadlineCalc;
import com.github.wmarkow.tubes.calc.domain.TubeModelIf;

public class TubeLoadlineChart extends TubeOutputCharacteristicChart {

    private static final long serialVersionUID = 4585630417764591616L;

    private XYSeriesCollection loadlineDataset;
    private XYSeriesCollection biasPointDataset;

    private double vcc = 200.0;
    private double load = 50000.0;

    @Override
    public void setTubeModel(TubeModelIf model) {
	super.setTubeModel(model);
    }

    /***
     * Sets the tube's load and power supply voltage
     * 
     * @param load
     *            tubes'load [ohm]
     * @param vcc
     *            power supply voltage [V]
     */
    public void setLoadAndSupplyVoltage(double load, double vcc) {
	this.vcc = vcc;
	this.load = load;

	loadlineDataset.removeAllSeries();

	// recalculate loadline
	loadlineDataset.addSeries(createLoadline(vcc, load));
    }

    public void setBiasResistance(double resistance) {
	biasPointDataset.removeAllSeries();

	biasPointDataset.addSeries(createBiasPoint(resistance, load, vcc));
    }

    @Override
    protected JFreeChart createChart() {
	JFreeChart chart = super.createChart();
	XYPlot plot = chart.getXYPlot();

	loadlineDataset = new XYSeriesCollection();
	XYLineAndShapeRenderer loadlineRenmderer = new XYLineAndShapeRenderer(true, false);
	loadlineRenmderer.setAutoPopulateSeriesPaint(false);
	loadlineRenmderer.setDefaultPaint(Color.red);

	int lastUsedIndex = plot.getDatasetCount() - 1;
	plot.setDataset(lastUsedIndex + 1, loadlineDataset);
	plot.setRenderer(lastUsedIndex + 1, loadlineRenmderer);

	biasPointDataset = new XYSeriesCollection();
	XYLineAndShapeRenderer biasPointRenderer = new XYLineAndShapeRenderer(true, false);
	biasPointRenderer.setAutoPopulateSeriesStroke(false);
	biasPointRenderer.setDefaultPaint(Color.red);
	biasPointRenderer.setDefaultStroke(new BasicStroke(10.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	lastUsedIndex = plot.getDatasetCount() - 1;
	plot.setDataset(lastUsedIndex + 1, biasPointDataset);
	plot.setRenderer(lastUsedIndex + 1, biasPointRenderer);

	return chart;
    }

    private XYSeries createLoadline(double vcc, double load) {

	LoadlineCalc loadlineCalc = new LoadlineCalc(getTubeModel());

	XYSeries series = new XYSeries("Loadline", true, false);
	series.add(0.0, loadlineCalc.calculateCurrentForZeroVa(vcc, load));
	series.add(vcc, loadlineCalc.calculateCurrentForMaxVa());

	return series;
    }

    private XYSeries createBiasPoint(double biasResistance, double loadResistance, double vcc) {

	LoadlineCalc loadlineCalc = new LoadlineCalc(getTubeModel());
	BiasPoint biasPoint = loadlineCalc.calculateBiasPoint(biasResistance, loadResistance, vcc);

	XYSeries series = new XYSeries("Bias point", true, false);

	if (biasPoint != null) {
	    series.add(biasPoint.getVa(), biasPoint.getIa());
	    series.add(biasPoint.getVa() + 0.001, biasPoint.getIa());
	} else {
	    System.out.println("null bias point");
	}

	return series;
    }

}
