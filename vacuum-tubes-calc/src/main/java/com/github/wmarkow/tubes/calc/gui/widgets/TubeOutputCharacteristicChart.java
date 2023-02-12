package com.github.wmarkow.tubes.calc.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.github.wmarkow.tubes.calc.domain.TubeCalc;
import com.github.wmarkow.tubes.calc.domain.TubeModelIf;

public class TubeOutputCharacteristicChart extends JPanel {

    private static final long serialVersionUID = -984753627161473014L;

    protected final static int OUTPUT_CHARACTERISTICS_DATASET_INDEX = 0;
    protected final static int MAX_POWER_DISSIPATION_DATASET_INDEX = 1;

    private XYSeriesCollection outputCharacteristicsDataset;
    private XYSeriesCollection maxPowerDissipationDataset;

    private JFreeChart chart;
    private TextTitle chartTitle;

    private TubeModelIf tubeModel;

    public TubeOutputCharacteristicChart() {

	chart = createChart();
	ChartPanel chartPanel = new ChartPanel(chart);
	chartPanel.setMouseWheelEnabled(true);

	this.setLayout(new BorderLayout());
	this.add(chartPanel, BorderLayout.CENTER);
    }

    public void setTubeModel(TubeModelIf model) {
	this.tubeModel = model;

	// recalculate the graphs
	outputCharacteristicsDataset.removeAllSeries();
	maxPowerDissipationDataset.removeAllSeries();

	// recalculate output characteristics
	XYSeriesCollection outputCharacteristicsSeriesCollection = createOutputCharacteristicsSeries(tubeModel);
	for (int index = 0; index < outputCharacteristicsSeriesCollection.getSeriesCount(); index++) {
	    XYSeries series = outputCharacteristicsSeriesCollection.getSeries(index);
	    outputCharacteristicsDataset.addSeries(series);
	}

	// recalculate max power curve
	maxPowerDissipationDataset.addSeries(createMaxPowerDissipation(model));

	chartTitle.setText(String.format("%s output characteristics", model.getName()));
    }

    public TubeModelIf getTubeModel() {
	return tubeModel;
    }

    protected double getMaxCurrentVisibleOnGraph(TubeModelIf model) {
	TubeCalc tubeCalc = new TubeCalc(model);

	double maxAnodeCurrent = tubeCalc.calculateMaxAnodeCurrent();
	double maxAnodeCurrentOnGraph = 1.2 * maxAnodeCurrent;

	return maxAnodeCurrentOnGraph;
    }

    private XYSeriesCollection createOutputCharacteristicsSeries(TubeModelIf model) {

	XYSeriesCollection result = new XYSeriesCollection();

	double vg1 = model.getMinV_G1();
	final double dvg = 0.5;

	while (vg1 <= model.getMaxV_G1()) {
	    XYSeries series = createOutputCharacteristicSeries(vg1, model);
	    result.addSeries(series);

	    vg1 += dvg;
	}

	return result;
    }

    private XYSeries createMaxPowerDissipation(TubeModelIf model) {
	XYSeries series = new XYSeries("Max power", true, false);

	double va = 0;
	double dva = 3.0;

	TubeCalc tubeCalc = new TubeCalc(model);

	double maxAnodeCurrentOnGraph = getMaxCurrentVisibleOnGraph(model);

	while (va <= model.getMaxV_A()) {
	    double anodeCurrent = tubeCalc.calculateMaxPowerDissipationAnodeCurrent(va);
	    if (anodeCurrent <= maxAnodeCurrentOnGraph) {
		series.add(va, anodeCurrent);
	    }

	    va += dva;
	}

	return series;
    }

    private XYSeries createOutputCharacteristicSeries(double vg1, TubeModelIf model) {

	XYSeries series = new XYSeries(String.valueOf(vg1), true, false);

	double va = 0;
	double dva = 3.0;

	TubeCalc tubeCalc = new TubeCalc(model);

	while (va <= model.getMaxV_A()) {
	    double anodeCurrent = tubeCalc.calculateAnodeCurrent(vg1, va);
	    series.add(va, anodeCurrent);

	    va += dva;
	}

	return series;
    }

    protected JFreeChart createChart() {

	NumberAxis domainAxis = new NumberAxis("Anode voltage [V]");
	domainAxis.setAutoRangeIncludesZero(false);
	domainAxis.setUpperMargin(0.12);
	domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	NumberAxis rangeAxis = new NumberAxis("Anode current [A]");
	rangeAxis.setAutoRangeIncludesZero(false);

	XYPlot plot = new XYPlot();
	plot.setDomainAxis(domainAxis);
	plot.setRangeAxis(rangeAxis);
	plot.setDomainPannable(true);
	plot.setRangePannable(true);

	maxPowerDissipationDataset = new XYSeriesCollection();
	XYLineAndShapeRenderer outputCharacteristicsRenderer = new XYLineAndShapeRenderer(true, false);
	outputCharacteristicsRenderer.setAutoPopulateSeriesPaint(false);
	outputCharacteristicsRenderer.setDefaultPaint(Color.red);
	plot.setDataset(MAX_POWER_DISSIPATION_DATASET_INDEX, maxPowerDissipationDataset);
	plot.setRenderer(MAX_POWER_DISSIPATION_DATASET_INDEX, outputCharacteristicsRenderer);

	outputCharacteristicsDataset = new XYSeriesCollection();
	XYLineAndShapeRenderer maxPowerRenderer = new XYLineAndShapeRenderer(true, false);
	maxPowerRenderer.setAutoPopulateSeriesPaint(false);
	maxPowerRenderer.setDefaultPaint(Color.blue);
	plot.setDataset(OUTPUT_CHARACTERISTICS_DATASET_INDEX, outputCharacteristicsDataset);
	plot.setRenderer(OUTPUT_CHARACTERISTICS_DATASET_INDEX, maxPowerRenderer);

	JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

	chartTitle = new TextTitle("Output characteristics", new Font("SansSerif", Font.BOLD, 14));
	chart.addSubtitle(chartTitle);

	return chart;
    }

}
