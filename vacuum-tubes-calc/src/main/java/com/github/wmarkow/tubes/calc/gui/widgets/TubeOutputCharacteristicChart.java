package com.github.wmarkow.tubes.calc.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.github.wmarkow.tubes.calc.domain.TubeCalc;
import com.github.wmarkow.tubes.calc.domain.TubeModelIf;

public class TubeOutputCharacteristicChart extends JPanel {

    private static final long serialVersionUID = -984753627161473014L;

    private XYSeriesCollection seriesCollection;
    private JFreeChart chart;
    private TextTitle chartTitle;

    private TubeModelIf tubeModel;

    public TubeOutputCharacteristicChart() {
	seriesCollection = new XYSeriesCollection();
	chart = createChart(seriesCollection);
	ChartPanel chartPanel = new ChartPanel(chart);
	chartPanel.setMouseWheelEnabled(true);

	this.setLayout(new BorderLayout());
	this.add(chartPanel, BorderLayout.CENTER);
    }

    public void setTubeModel(TubeModelIf model) {
	this.tubeModel = model;

	// recalculate the graphs
	seriesCollection.removeAllSeries();
	XYSeriesCollection outputCharacteristicsSeriesCollection = createOutputCharacteristicsSeries(tubeModel);
	for (int index = 0; index < outputCharacteristicsSeriesCollection.getSeriesCount(); index++) {
	    XYSeries series = outputCharacteristicsSeriesCollection.getSeries(index);
	    seriesCollection.addSeries(series);
	}

	chartTitle.setText(String.format("%s output characteristics", model.getName()));
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

    private JFreeChart createChart(XYDataset dataset) {
	JFreeChart chart = ChartFactory.createXYLineChart(null, "Anode voltage [V]", "Anode current [A]", dataset,
		PlotOrientation.VERTICAL, true, true, false);
	chartTitle = new TextTitle("Output characteristics", new Font("SansSerif", Font.BOLD, 14));
	chart.addSubtitle(chartTitle);
	XYPlot plot = (XYPlot) chart.getPlot();
	plot.setDomainPannable(true);
	plot.setRangePannable(true);
	NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
	domainAxis.setUpperMargin(0.12);
	domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	rangeAxis.setAutoRangeIncludesZero(false);
	return chart;
    }

}
