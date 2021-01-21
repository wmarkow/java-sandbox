package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.calc.FishGraph;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FishGraphTest {

    private FishGraph subject;

    @Before
    public void init() {
        subject = new FishGraph();
    }

    @Test
    public void testInit() {
        // no exceptions should be thrown
        subject.init();
    }

    @Test
    public void testGetMinValidTemp() {
        assertEquals(78.17, subject.getMinValidTemp(), 0.000001);
    }

    @Test
    public void testGetMaxValidTemp() {
        assertEquals(99.00, subject.getMaxValidTemp(), 0.000001);
    }

    @Test
    public void testGetLiquidAlcoholVolumeContentForLowerOutOfRangeException() {
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = subject.getMinValidTemp() - q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.getLiquidAlcoholVolumeContent(temp);
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
                subject.getLiquidAlcoholVolumeContent(temp);
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
            subject.getLiquidAlcoholVolumeContent(temp);
        }
    }

    @Test
    public void testGetVaporAlcoholVolumeContentForLowerOutOfRangeException() {
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = subject.getMinValidTemp() - q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.getVaporAlcoholVolumeContent(temp);
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
                subject.getVaporAlcoholVolumeContent(temp);
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
            subject.getVaporAlcoholVolumeContent(temp);
        }
    }

    @Test
    public void createFishGraph() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        for(int q = 0; q < 200 ; q ++) {
            double temp = subject.getMinValidTemp() + q * 0.1;

            if(temp > 99.0) {
                continue;
            }

            double liquidContent = subject.getLiquidAlcoholVolumeContent(temp);
            double vaporContent = subject.getVaporAlcoholVolumeContent(temp);

            bufferedImage.getGraphics().setColor(Color.BLUE);
            bufferedImage.getGraphics().drawLine(q, (int)liquidContent, q, (int)liquidContent);
            bufferedImage.getGraphics().drawLine(q, (int)vaporContent, q, (int)vaporContent);
        }
        bufferedImage.getGraphics().dispose();

        File file = new File("target/fishgraph.png");
        file.mkdirs();
        ImageIO.write(bufferedImage, "png", file);
    }
}
