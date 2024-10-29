package vtech.sim.iot.mesh.examples;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public abstract class AbstractGraphExample extends ApplicationFrame {

    private static final float Y_MAX = 100;
    private static final float Y_MIN = 0;
    private static final int COUNT = 120;
    private static final int CHART_WRITE_SPAN_MILLIS = 1000;
    private Timer timer;
    private final static Second TIME_BASE = new Second(0, 58, 23, 1, 1, 2011);
    
    private JScrollPane scrollPane;
    private JPanel graphsPanel;

    private List<DynamicTimeSeriesCollection> datasets = new ArrayList<DynamicTimeSeriesCollection>();

    public AbstractGraphExample(String windowTitle) {
	super(windowTitle);

	init();
    }

    protected abstract SimulationGraphInfo[] getSimulationGraphInfos();
    
    protected abstract float[] getSeriesData(int graphIndex);
    
    protected abstract double getSeriesCurrentTimeMillis(int graphIndex);

    private void init() {
	createContents();

	SimulationGraphInfo[] simulationGraphInfos = getSimulationGraphInfos();

	for (SimulationGraphInfo graphInfo : simulationGraphInfos) {
	    DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(graphInfo.getSeriesNames().length,
		    COUNT, new Second());
	    dataset.setTimeBase(TIME_BASE);
	    datasets.add(dataset);

	    for (int q = 0; q < graphInfo.getSeriesNames().length; q++) {
		dataset.addSeries(new float[] {}, q, graphInfo.getSeriesNames()[q]);
	    }

	    // FIXME: the third series (packet queue size) should have a separate axis (now
	    // it uses % which is wrong)
	    JFreeChart chart = createChart(dataset, graphInfo.getTitle());
	    getGraphsPanel().add(new ChartPanel(chart));
	}

	timer = new Timer(CHART_WRITE_SPAN_MILLIS, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		for (int q = 0; q < getSimulationGraphInfos().length; q++) {
		    
		    DynamicTimeSeriesCollection dataset = datasets.get(q);
		    
		    RegularTimePeriod rtp = dataset.getNewestTime();	    
		    long graphLastMillis = rtp.getLastMillisecond() - TIME_BASE.getLastMillisecond();
		    long graphNextMillis = graphLastMillis + CHART_WRITE_SPAN_MILLIS - COUNT * 1000;
		    double currentSimulationTimeMillis = getSeriesCurrentTimeMillis(q);
		    
		    if(currentSimulationTimeMillis >= graphNextMillis)
		    {
			// save to graph
			float[] seriesData = getSeriesData(q);
			
			dataset.advanceTime();
			dataset.appendData(seriesData);
		    }
		}
	    }
	});

	timer.start();
    }

    private JFreeChart createChart(final XYDataset dataset, String title) {
	final JFreeChart result = ChartFactory.createTimeSeriesChart(title, "hh:mm:ss", "[%]", dataset, true, true,
		false);
	final XYPlot plot = result.getXYPlot();
	ValueAxis domain = plot.getDomainAxis();
	domain.setAutoRange(true);
	ValueAxis range = plot.getRangeAxis();
	range.setRange(Y_MIN, Y_MAX);
	range.setAutoRange(true);
	return result;
    }

    private void createContents() {
	setLayout(new BorderLayout(0, 0));
	add(getScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane getScrollPane() {
	if (scrollPane == null) {
	    scrollPane = new JScrollPane();
	    scrollPane.setViewportView(getGraphsPanel());
	}
	return scrollPane;
    }

    private JPanel getGraphsPanel() {
	if (graphsPanel == null) {
	    graphsPanel = new JPanel();
	    graphsPanel.setLayout(new BoxLayout(graphsPanel, BoxLayout.Y_AXIS));
	}
	return graphsPanel;
    }
}
