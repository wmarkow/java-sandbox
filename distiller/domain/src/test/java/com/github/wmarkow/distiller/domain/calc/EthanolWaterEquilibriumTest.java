package com.github.wmarkow.distiller.domain.calc;

import org.apache.commons.math3.exception.OutOfRangeException;
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

public class EthanolWaterEquilibriumTest {

    private EthanolWaterEquilibrium subject;

    @Before
    public void init() {
        subject = new EthanolWaterEquilibrium();
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
    public void testGetLiquidAlcoholVolumeContentForLowerOutOfRangeException() {
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = subject.getMinValidTemp() - q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.getLiquidMoleFraction(temp);
                fail("OutOfRangeException should be thrown");
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetLiquidAlcoholVolumeContentForHigherOutOfRangeException() {
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = subject.getMaxValidTemp() + q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.getLiquidMoleFraction(temp);
                fail("OutOfRangeException should be thrown");
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetLiquidAlcoholVolumeContentForValidTemperature() {
        double steps = 100;
        double delta = (subject.getMaxValidTemp() - subject.getMinValidTemp()) / steps;
        double minTemp = subject.getMinValidTemp();

        for(int q = 0 ; q < steps ; q ++) {
            double temp = minTemp + q * delta;
            assertTrue(subject.isValidPoint(temp));
            subject.getLiquidMoleFraction(temp);
        }
    }

    @Test
    public void testGetVaporAlcoholVolumeContentForLowerOutOfRangeException() {
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = subject.getMinValidTemp() - q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.getVaporMoleFraction(temp);
                fail("OutOfRangeException should be thrown");
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetVaporAlcoholVolumeContentForHigherOutOfRangeException() {
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = subject.getMaxValidTemp() + q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.getVaporMoleFraction(temp);
                fail("OutOfRangeException should be thrown");
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetVaporAlcoholVolumeContentForValidTemperature() {
        double steps = 100;
        double delta = (subject.getMaxValidTemp() - subject.getMinValidTemp()) / steps;
        double minTemp = subject.getMinValidTemp();

        for(int q = 0 ; q < steps ; q ++) {
            double temp = minTemp + q * delta;
            assertTrue(subject.isValidPoint(temp));
            subject.getVaporMoleFraction(temp);
        }
    }

    @Test
    public void testGetLiquidMoleFraction() {
        assertEquals(0.8943, subject.getLiquidMoleFraction(78.15), 0.0001);
        assertEquals(0.5079, subject.getLiquidMoleFraction(79.8), 0.0001);
        assertEquals(0.3273, subject.getLiquidMoleFraction(81.5), 0.0001);
        assertEquals(0.1661, subject.getLiquidMoleFraction(84.1), 0.0001);
        assertEquals(0.0, subject.getLiquidMoleFraction(100.0), 0.0001);
    }

    @Test
    public void testGetVaporMoleFraction() {
        assertEquals(0.8943, subject.getVaporMoleFraction(78.15), 0.0001);
        assertEquals(0.6564, subject.getVaporMoleFraction(79.8), 0.0001);
        assertEquals(0.5826, subject.getVaporMoleFraction(81.5), 0.0001);
        assertEquals(0.5089, subject.getVaporMoleFraction(84.1), 0.0001);
        assertEquals(0.0, subject.getVaporMoleFraction(100.0), 0.0001);
    }

    @Test
    public void createFishPlots() throws IOException {
        int pixels = 200;
        BufferedImage xytImage = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_RGB);
        BufferedImage xyImage = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_RGB);

        for(int q = 0; q < pixels ; q ++) {
            double temp = subject.getMinValidTemp() + q * 0.1;

            if(temp > 99.0) {
                continue;
            }

            int y = pixels - q;
            int xLiquid = (int)(subject.getLiquidMoleFraction(temp) * pixels);
            int xVapor = (int)(subject.getVaporMoleFraction(temp) * pixels);

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
}
