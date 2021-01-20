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
    public void testGetLiquidAlcoholVolumeContentForLowerOutOfRangeException() {
        double initialTemp = 78.17;
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = initialTemp - q * 0.1;
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
        double initialTemp = 99.0;
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = initialTemp + q * 0.1;
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
        double temp = 78.17;

        while(true) {
            if(temp > 99.0)
            {
                return;
            }
            subject.getLiquidAlcoholVolumeContent(temp);

            temp += 0.001;
        }
    }

    @Test
    public void testGetVaporAlcoholVolumeContentForLowerOutOfRangeException() {
        double initialTemp = 78.17;
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = initialTemp - q * 0.1;
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
        double initialTemp = 99.0;
        for(int q = 1 ; q < 100 ; q++)
        {
            double temp = initialTemp + q * 0.1;
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
        double temp = 78.17;

        while(true) {
            if(temp > 99.0)
            {
                return;
            }
            subject.getVaporAlcoholVolumeContent(temp);

            temp += 0.001;
        }
    }

    @Test
    public void createFishGraph() throws IOException {
        double initialTemp = 78.17;

        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        for(int q = 0; q < 200 ; q ++) {
            double temp = initialTemp + q * 0.1;

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
