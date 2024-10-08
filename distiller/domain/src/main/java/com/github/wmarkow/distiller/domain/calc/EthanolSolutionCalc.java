package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Ethanol;
import com.github.wmarkow.distiller.domain.constants.Water;

import org.apache.commons.math3.analysis.interpolation.BicubicInterpolatingFunction;
import org.apache.commons.math3.analysis.interpolation.BicubicInterpolator;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EthanolSolutionCalc {
    private final static String ETHANOL_SOLUTION_FILE_NAME = "ethanol-water_solution.txt";

    private static double minValidTemp;
    private static double maxValidTemp;
    private static double minValidMolarFraction;
    private static double maxValidMolarFraction;

    private static BicubicInterpolatingFunction densityData = null;

    /***
     * Calculates the density of the solution for a given ethanol molar fraction concentration and temperature.
     *
     * @param ethanolMolarFraction ethanol molar fraction concentration
     * @param temperature solution temperature in Celsius degree
     * @return density in kg/l
     */
    public double calculateDensity(double ethanolMolarFraction, double temperature) {
        init();
        return densityData.value(temperature, ethanolMolarFraction);
    }

    /***
     * Calculates the volume ethanol concentration for a given ethanol molar fraction concentration and temperature.
     * @param ethanolMolarFraction ethanol molar fraction concentration
     * @param temperature solution temperature in Celsius degree
     * @return ethanol concentration in % vol
     */
    public double calculateVolumeConcentration(double ethanolMolarFraction, double temperature) {
        final double waterMolarFraction = 1.0 - ethanolMolarFraction;

        double ethanolMassInKg = Ethanol.MOLAR_MASS * ethanolMolarFraction / 1000;
        double waterMassInKg = Water.MOLAR_MASS * waterMolarFraction / 1000;
        double solutionMassInKg = ethanolMassInKg + waterMassInKg;
        double solutionDensityInKgPerL = calculateDensity(ethanolMolarFraction, temperature);

        double solutionVolumeInL = solutionMassInKg / solutionDensityInKgPerL;
        double ethanolDensityInKgPerL = calculateDensity(1.0, temperature);
        double ethanolVolumeInL = ethanolMassInKg / ethanolDensityInKgPerL;

        return 100 * ethanolVolumeInL / solutionVolumeInL;
    }

    /***
     * Gets minimal valid temperature
     * @return temperature in Celsius degree
     */
    public double getMinValidTemp() {
        init();

        return minValidTemp;
    }

    /***
     * Gets maximal valid temperature
     * @return temperature in Celsius degree
     */
    public double getMaxValidTemp() {
        init();

        return maxValidTemp;
    }

    static void init() {
        if (densityData != null) {
            return;
        }
        // The class loader that loaded the class
        ClassLoader classLoader = LVEWEquilibriumCalc.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(ETHANOL_SOLUTION_FILE_NAME);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + ETHANOL_SOLUTION_FILE_NAME);
        }

        try (InputStreamReader streamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            double[] temps = null;
            double[] molarFractions = null;
            double[][] densities = null;

            ArrayList<String[]> dataLines = new ArrayList<String[]>();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("#")) {
                    continue;
                }

                String[] split = line.split(" +");
                if ("TEMP:".equals(split[0])) {
                    // temperature row found
                    temps = new double[split.length - 1];
                    for(int q = 1 ; q < split.length ; q ++) {
                        temps[q-1] = Double.parseDouble(split[q]);
                    }
                    minValidTemp = temps[0];
                    maxValidTemp = temps[temps.length - 1];

                    continue;
                }

                if (split.length != temps.length + 1) {
                    throw new IllegalArgumentException(String.format("In file %s: data row '%s' contains different number of data as the header", ETHANOL_SOLUTION_FILE_NAME, line));
                }
                dataLines.add(split);
            }

            molarFractions = new double[dataLines.size()];
            densities = new double[temps.length][dataLines.size()];
            for(int y = 0 ; y < dataLines.size() ; y ++) {
                String[] dataLine = dataLines.get(y);
                molarFractions[y] = Double.parseDouble(dataLine[0]);
                for(int x = 0 ; x < temps.length ; x ++) {
                    densities[x][y] = Double.parseDouble(dataLine[x + 1]);
                }
            }
            minValidMolarFraction = molarFractions[0];
            maxValidMolarFraction = molarFractions[molarFractions.length - 1];

            BicubicInterpolator interpolator = new BicubicInterpolator();
            densityData = interpolator.interpolate(temps, molarFractions, densities);
        } catch (IOException e) {
            throw new IllegalArgumentException("file not found! " + ETHANOL_SOLUTION_FILE_NAME);
        }
    }
}
