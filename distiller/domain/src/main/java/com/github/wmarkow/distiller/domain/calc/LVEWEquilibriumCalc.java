package com.github.wmarkow.distiller.domain.calc;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/***
 * Liquid-vapor of ethanol-water equilibrium
 */
public class LVEWEquilibriumCalc {

    private final static String LIQUID_VAPOR_FILE_NAME = "liquid-vapor_ethanol-water_equilibrium.txt";

    private static PolynomialSplineFunction liquidData = null;
    private static PolynomialSplineFunction vaporData = null;
    private static Double minValidTemp = null;
    private static Double maxValidTemp = null;

    /***
     * Calculates liquid-vapor of ethanol-water equilibrium
     * @param temperature
     * @return
     * @throws OutOfRangeException when temperature is out of range (see {@link #isValidPoint(double)}
     */
    public LVEWEquilibrium calculateEquilibrium(double temperature) throws OutOfRangeException {
        init();

        double ethanolLiquidMoleFraction;
        try {
            ethanolLiquidMoleFraction = liquidData.value(temperature);
        } catch (org.apache.commons.math3.exception.OutOfRangeException e) {
            throw new OutOfRangeException(e.getMessage());
        }
        double ethanolVaporMoleFraction = vaporData.value(temperature);

        return new LVEWEquilibrium(temperature, ethanolLiquidMoleFraction, ethanolVaporMoleFraction);
    }

    public boolean isValidPoint(double temp) {
        init();

        return (liquidData.isValidPoint(temp) && vaporData.isValidPoint(temp));
    }

    public double getMinValidTemp() {
        init();

        return  minValidTemp;
    }

    public double getMaxValidTemp() {
        init();

        return maxValidTemp;
    }

    static void init() {
        if(liquidData != null && vaporData != null) {
            return;
        }
        // The class loader that loaded the class
        ClassLoader classLoader = LVEWEquilibriumCalc.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(LIQUID_VAPOR_FILE_NAME);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + LIQUID_VAPOR_FILE_NAME);
        }

        try (InputStreamReader streamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            ArrayList<Double> tempData = new ArrayList<>();
            ArrayList<Double> liquidContentData = new ArrayList<>();
            ArrayList<Double> vaporContentData = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                line = line.trim();

                if(line.isEmpty()) {
                    continue;
                }
                if(line.startsWith("#")) {
                    continue;
                }

                String[] split = line.split(" ");
                if(split.length != 3)
                {
                    throw new IllegalArgumentException(String.format("File %s contains more than 3 columns", LIQUID_VAPOR_FILE_NAME));
                }

                double temp = Double.parseDouble(split[0]);
                double liquidContent = Double.parseDouble(split[1]);
                double vaporContent = Double.parseDouble(split[2]);

                if(minValidTemp == null || temp < minValidTemp) {
                    minValidTemp = temp;
                }
                if(maxValidTemp == null || temp > maxValidTemp) {
                    maxValidTemp = temp;
                }

                tempData.add(temp);
                liquidContentData.add(liquidContent);
                vaporContentData.add(vaporContent);
            }

            double[] tempArray = tempData.stream().mapToDouble(d -> d).toArray();
            double[] liquidArray = liquidContentData.stream().mapToDouble(d -> d).toArray();
            double[] vaporArray = vaporContentData.stream().mapToDouble(d -> d).toArray();

            LinearInterpolator interpolator = new LinearInterpolator();
            liquidData = interpolator.interpolate(tempArray, liquidArray);
            vaporData = interpolator.interpolate(tempArray, vaporArray);
        } catch (IOException e) {
            throw new IllegalArgumentException("file not found! " + LIQUID_VAPOR_FILE_NAME);
        }
    }
}
