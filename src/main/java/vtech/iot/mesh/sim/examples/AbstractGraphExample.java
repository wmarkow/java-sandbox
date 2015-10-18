package vtech.iot.mesh.sim.examples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public abstract class AbstractGraphExample extends ApplicationFrame {

  private static final float Y_MAX = 100;
  private static final float Y_MIN = 0;
  private static final int COUNT = 30;;
  private static final int FAST = 1000;
  private Timer timer;

  private List<DynamicTimeSeriesCollection> datasets = new ArrayList<DynamicTimeSeriesCollection>();

  public AbstractGraphExample(String windowTitle) {
    super(windowTitle);

    init();
  }

  protected abstract SimulationGraphInfo[] getSimulationGraphInfos();

  protected abstract float[] getSeriesData(int graphIndex);

  private void init() {
    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

    SimulationGraphInfo[] simulationGraphInfos = getSimulationGraphInfos();

    for (SimulationGraphInfo graphInfo : simulationGraphInfos) {
      DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(graphInfo.getSeriesNames().length, COUNT, new Second());
      dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
      datasets.add(dataset);

      for (int q = 0; q < graphInfo.getSeriesNames().length; q++) {
        dataset.addSeries(new float[] {}, q, graphInfo.getSeriesNames()[q]);
      }

      JFreeChart chart = createChart(dataset);

      getContentPane().add(new ChartPanel(chart));
    }

    timer = new Timer(FAST, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int q = 0; q < getSimulationGraphInfos().length; q++) {
          float[] seriesData = getSeriesData(q);

          DynamicTimeSeriesCollection dataset = datasets.get(q);
          dataset.advanceTime();
          dataset.appendData(seriesData);
        }
      }
    });

    timer.start();
  }

  private JFreeChart createChart(final XYDataset dataset) {
    final JFreeChart result = ChartFactory.createTimeSeriesChart(getSimulationGraphInfos()[0].getTitle(), "hh:mm:ss", "[%]", dataset, true, true, false);
    final XYPlot plot = result.getXYPlot();
    ValueAxis domain = plot.getDomainAxis();
    domain.setAutoRange(true);
    ValueAxis range = plot.getRangeAxis();
    range.setRange(Y_MIN, Y_MAX);
    range.setAutoRange(true);
    return result;
  }
}
