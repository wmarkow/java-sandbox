package com.github.wmarkow.distiller.domain.calc;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LVEWEquilibriumCalcTest {

    private LVEWEquilibriumCalc subject;

    @Before
    public void init() {
        subject = new LVEWEquilibriumCalc();
    }

    @Test
    public void testInit() {
        // no exceptions should be thrown
        subject.init();
    }

    @Test
    public void testGetMinValidTemp() {
        assertEquals(78.15, subject.getMinValidTemp(), 0.000001);
    }

    @Test
    public void testGetMaxValidTemp() {
        assertEquals(100.00, subject.getMaxValidTemp(), 0.000001);
    }

    @Test
    public void testGetEquilibriumForLowerOutOfRangeException() {
        for (int q = 1; q < 100; q++) {
            double temp = subject.getMinValidTemp() - q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.calculateEquilibrium(temp);
                fail("OutOfRangeException should be thrown");
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetEquilibriumForHigherOutOfRangeException() {
        for (int q = 1; q < 100; q++) {
            double temp = subject.getMaxValidTemp() + q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.calculateEquilibrium(temp);
                fail("OutOfRangeException should be thrown");
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetEquilibriumForValidTemperature() throws OutOfRangeException {
        double steps = 100;
        double delta = (subject.getMaxValidTemp() - subject.getMinValidTemp()) / steps;
        double minTemp = subject.getMinValidTemp();

        for (int q = 0; q < steps; q++) {
            double temp = minTemp + q * delta;
            assertTrue(subject.isValidPoint(temp));
            subject.calculateEquilibrium(temp);
        }
    }

    @Test
    public void testGetLiquidMoleFraction() throws OutOfRangeException {
        assertEquals(0.8943, subject.calculateEquilibrium(78.15).ethanolLiquidMoleFraction, 0.0001);
        assertEquals(0.5079, subject.calculateEquilibrium(79.8).ethanolLiquidMoleFraction, 0.0001);
        assertEquals(0.3273, subject.calculateEquilibrium(81.5).ethanolLiquidMoleFraction, 0.0001);
        assertEquals(0.1661, subject.calculateEquilibrium(84.1).ethanolLiquidMoleFraction, 0.0001);
        assertEquals(0.0, subject.calculateEquilibrium(100.0).ethanolLiquidMoleFraction, 0.0001);
    }

    @Test
    public void testGetVaporMoleFraction() throws OutOfRangeException {
        assertEquals(0.8943, subject.calculateEquilibrium(78.15).ethanolVaporMoleFraction, 0.0001);
        assertEquals(0.6564, subject.calculateEquilibrium(79.8).ethanolVaporMoleFraction, 0.0001);
        assertEquals(0.5826, subject.calculateEquilibrium(81.5).ethanolVaporMoleFraction, 0.0001);
        assertEquals(0.5089, subject.calculateEquilibrium(84.1).ethanolVaporMoleFraction, 0.0001);
        assertEquals(0.0, subject.calculateEquilibrium(100.0).ethanolVaporMoleFraction, 0.0001);
    }

    @Test
    public void createFishPlots() throws IOException, OutOfRangeException {
        int pixels = 200;
        BufferedImage xytImage = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_RGB);
        BufferedImage xyImage = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_RGB);

        for (int q = 0; q < pixels; q++) {
            double temp = subject.getMinValidTemp() + q * 0.1;

            if (temp > 99.0) {
                continue;
            }

            int y = pixels - q;
            int xLiquid = (int) (subject.calculateEquilibrium(temp).ethanolLiquidMoleFraction * pixels);
            int xVapor = (int) (subject.calculateEquilibrium(temp).ethanolVaporMoleFraction * pixels);

            // create XY-T plot
            xytImage.getGraphics().drawLine(xLiquid, y, xLiquid, y);
            xytImage.getGraphics().drawLine(xVapor, y, xVapor, y);

            // create XY plot
            xyImage.getGraphics().drawLine(xLiquid, pixels - xVapor, xLiquid, pixels - xVapor);
        }
        xytImage.getGraphics().dispose();
        xyImage.getGraphics().dispose();

        File xytFile = new File("target/fishgraph-xyt.png");
        xytFile.mkdirs();
        ImageIO.write(xytImage, "png", xytFile);

        File xyFile = new File("target/fishgraph-xy.png");
        xyFile.mkdirs();
        ImageIO.write(xyImage, "png", xyFile);
    }

    @Test
    public void printFishTableAsText() throws OutOfRangeException {
        int maxSteps = 500;
        double deltaTemp = (subject.getMaxValidTemp() - subject.getMinValidTemp()) / maxSteps;

        EthanolSolutionCalc esc = new EthanolSolutionCalc();
        esc.calculateVolumeConcentration(0, 0);

        System.out.println("Liquid %   Temp   Vapour %");

        for (int q = 0; q <= maxSteps; q++) {
            double temp = subject.getMaxValidTemp() - q * deltaTemp;

            LVEWEquilibrium equlibrium = subject.calculateEquilibrium(temp);
            double ethanolLiquidVolumeConcentration = esc.calculateVolumeConcentration(equlibrium.ethanolLiquidMoleFraction, temp);
            double ethanolVapourVolumeConcentration = esc.calculateVolumeConcentration(equlibrium.ethanolVaporMoleFraction, temp);

            String text = String.format("%-10.2f %-6.2f %-4.1f", ethanolLiquidVolumeConcentration, temp, ethanolVapourVolumeConcentration);
            System.out.println(text);
        }
    }
}
