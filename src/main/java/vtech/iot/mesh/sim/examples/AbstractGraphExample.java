package vtech.iot.mesh.sim.examples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

  public AbstractGraphExample(String windowTitle) {
    super(windowTitle);

    init();
  }

  protected abstract String getGraphTitle();

  protected abstract int getSeriesCount();

  protected abstract String getSeriesName(int seriesIndex);

  protected abstract float getSeriesData(int seriesIndex);

  private void init() {
    this.setTitle(getGraphTitle());

    final DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(getSeriesCount(), COUNT, new Second());
    dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));

    for (int q = 0; q < getSeriesCount(); q++) {
      dataset.addSeries(new float[] {}, q, getSeriesName(q));
    }

    JFreeChart chart = createChart(dataset);

    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    getContentPane().add(new ChartPanel(chart));

    timer = new Timer(FAST, new ActionListener() {

      float[] newData = new float[getSeriesCount()];

      @Override
      public void actionPerformed(ActionEvent e) {

        for (int q = 0; q < getSeriesCount(); q++) {
          newData[q] = getSeriesData(q);
        }

        dataset.advanceTime();
        dataset.appendData(newData);
      }
    });

    timer.start();
  }

  private JFreeChart createChart(final XYDataset dataset) {
    final JFreeChart result = ChartFactory.createTimeSeriesChart(getGraphTitle(), "hh:mm:ss", "[%]", dataset, true, true, false);
    final XYPlot plot = result.getXYPlot();
    ValueAxis domain = plot.getDomainAxis();
    domain.setAutoRange(true);
    ValueAxis range = plot.getRangeAxis();
    range.setRange(Y_MIN, Y_MAX);
    range.setAutoRange(true);
    return result;
  }
}
