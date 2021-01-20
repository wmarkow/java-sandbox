package com.github.wmarkow.distiller.domain.calc;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class FishGraph {

    private final static String LIQUID_VAPOR_FILE_NAME = "liquid_vapor.txt";

    private static PolynomialSplineFunction liquidData = null;
    private static PolynomialSplineFunction vaporData = null;

    public double getLiquidAlcoholVolumeContent(double boilingTemp) {
        init();

        return liquidData.value(boilingTemp);
    }

    public double getVaporAlcoholVolumeContent(double vaporTemp) {
        init();

        return vaporData.value(vaporTemp);
    }

    static void init() {
        if(liquidData != null && vaporData != null) {
            return;
        }
        // The class loader that loaded the class
        ClassLoader classLoader = FishGraph.class.getClassLoader();
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

                double liquidContent = Double.parseDouble(split[0]);
                double temp = Double.parseDouble(split[1]);
                double vaporContent = Double.parseDouble(split[2]);

                tempData.add(temp);
                liquidContentData.add(liquidContent);
                vaporContentData.add(vaporContent);
            }

            Collections.reverse(tempData);
            Collections.reverse(liquidContentData);
            Collections.reverse(vaporContentData);

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
