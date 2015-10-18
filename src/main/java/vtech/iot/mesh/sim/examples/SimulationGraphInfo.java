package vtech.iot.mesh.sim.examples;

public class SimulationGraphInfo {

  private String title;
  private String[] seriesNames;

  public SimulationGraphInfo(String title, String[] seriesNames) {
    this.title = title;
    this.seriesNames = seriesNames;
  }

  public String getTitle() {
    return title;
  }

  public String[] getSeriesNames() {
    return seriesNames;
  }
}
