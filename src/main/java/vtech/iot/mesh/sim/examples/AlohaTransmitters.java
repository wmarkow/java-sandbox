package vtech.iot.mesh.sim.examples;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
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
import org.jfree.ui.RefineryUtilities;

import vtech.iot.mesh.sim.MeshSim;
import vtech.iot.mesh.sim.domain.devices.AlohaDevice;

public class AlohaTransmitters extends ApplicationFrame {
  private static final String TITLE = "Aloha Simulation";

  private static final float Y_MAX = 100;
  private static final float Y_MIN = 0;
  private static final int COUNT = 2 * 60;
  private static final int FAST = 1000;
  private Timer timer;

  private MeshSim meshSim = new MeshSim();

  public AlohaTransmitters(final String title) {
    super(title);

    meshSim.addDevice(new AlohaDevice(10));
    meshSim.addDevice(new AlohaDevice(10));

    final DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(1, COUNT, new Second());
    dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
    dataset.addSeries(new float[] {}, 0, "Network load");
    JFreeChart chart = createChart(dataset);

    this.add(new ChartPanel(chart), BorderLayout.CENTER);
    JPanel btnPanel = new JPanel(new FlowLayout());

    this.add(btnPanel, BorderLayout.SOUTH);

    timer = new Timer(FAST, new ActionListener() {

      float[] newData = new float[1];

      @Override
      public void actionPerformed(ActionEvent e) {
        newData[0] = (float) meshSim.getMediumBusyPercentage();
        dataset.advanceTime();
        dataset.appendData(newData);
      }
    });
  }

  private JFreeChart createChart(final XYDataset dataset) {
    final JFreeChart result = ChartFactory.createTimeSeriesChart(TITLE, "hh:mm:ss", "[%]", dataset, true, true, false);
    final XYPlot plot = result.getXYPlot();
    ValueAxis domain = plot.getDomainAxis();
    domain.setAutoRange(true);
    ValueAxis range = plot.getRangeAxis();
    range.setRange(Y_MIN, Y_MAX);
    range.setAutoRange(true);
    return result;
  }

  public void start() {
    timer.start();
  }

  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        AlohaTransmitters demo = new AlohaTransmitters(TITLE);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
        demo.start();
      }
    });
  }
}
