//******************************************************************
//                                                                 
//  FoxHuntingFrame.java                                               
//  Copyright 2023 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.fox.hunting.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.github.wmarkow.fox.hunting.domain.AverageValueCalculator;
import com.github.wmarkow.fox.hunting.domain.FpsCalculator;
import com.github.wmarkow.fox.hunting.domain.ThreeSigmaAverageValueCalculator;
import com.github.wmarkow.fox.hunting.serial.FoxHuntingSerialPort;
import com.github.wmarkow.fox.hunting.serial.FoxHuntingSerialPortListener;

public class FoxHuntingFrame extends JFrame
{
    private FoxHuntingSerialPort serialPort = new FoxHuntingSerialPort();
    private TimeSeries noSignalSeries;
    private TimeSeries signalSeries;
    private TimeSeries signalAvgSeries;
    private TimeSeries signalAvg3SigmaSeries;
    private TimeSeries avgRcvCountSeries;

    private AverageValueCalculator avgCalc = new AverageValueCalculator();
    private AverageValueCalculator avg3SigmaCalc = new ThreeSigmaAverageValueCalculator();
    private FpsCalculator fpsCalc = new FpsCalculator();

    private DataLoggerPanel dataLoggerpanel;

    public FoxHuntingFrame()
    {
        super( "Fox hunting GUI" );

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setMinimumSize( new Dimension( 1024, 768 ) );
        getContentPane().setLayout( new BorderLayout() );

        JButton connectButton = new JButton( "Connect" );
        connectButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent aE )
            {
                serialPort.setListener( new FoxHuntingSerialPortListener()
                {

                    @Override
                    public void onRssiNoSignalMeassure( int rssi )
                    {
                        noSignalSeries.add( new Millisecond(), rssi );
                    }

                    @Override
                    public void onRssiPreambleSignalMeassure( int rssi )
                    {
                        Millisecond millis = new Millisecond();

                        signalSeries.add( new Millisecond(), rssi );

                        int avgRssi = avgCalc.calculate( rssi );
                        signalAvgSeries.add( new Millisecond(), avgRssi );

                        int avg3SigmaRssi = avg3SigmaCalc.calculate( rssi );
                        signalAvg3SigmaSeries.add( new Millisecond(), avg3SigmaRssi );

                        fpsCalc.frameReceived();
                        avgRcvCountSeries.add( new Millisecond(), fpsCalc.getCurrentFps() );

                        dataLoggerpanel.onRssiPreambleSignalMeassure( millis.getFirstMillisecond(), rssi );
                    }

                    @Override
                    public void onSerialPortDisconnected()
                    {
                        dataLoggerpanel.setEnabled( false );
                        dataLoggerpanel.stopRecording();
                    }
                } );

                if( serialPort.discoverAndConnect() )
                {
                    dataLoggerpanel.setEnabled( true );
                }
            }
        } );

        noSignalSeries = new TimeSeries( "No signal series" );
        signalSeries = new TimeSeries( "Signal series" );
        signalAvgSeries = new TimeSeries( "Signal average series" );
        signalAvg3SigmaSeries = new TimeSeries( "Signal average 3s series" );
        avgRcvCountSeries = new TimeSeries( "Average receive fps" );
        ChartPanel chartPanel = new ChartPanel( createChart() );
        chartPanel.setPreferredSize( new java.awt.Dimension( 500, 270 ) );

        dataLoggerpanel = new DataLoggerPanel();
        dataLoggerpanel.setEnabled( false );

        getContentPane().add( connectButton, BorderLayout.NORTH );
        getContentPane().add( chartPanel, BorderLayout.CENTER );
        getContentPane().add( dataLoggerpanel, BorderLayout.SOUTH );
    }

    private JFreeChart createChart()
    {
        ValueAxis timeAxis = new DateAxis( "Time" );
        timeAxis.setLowerMargin( 0.02 );
        timeAxis.setUpperMargin( 0.02 );
        timeAxis.setAutoRange( true );
        timeAxis.setFixedAutoRange( 5000.0 );
        NumberAxis valueAxis = new NumberAxis( "RSSI" );
        valueAxis.setAutoRangeIncludesZero( false );
        valueAxis.setRange( 0.0, 600.0 );

        XYPlot plot = new XYPlot();
        plot.setDomainAxis( timeAxis );
        plot.setRangeAxis( valueAxis );

        TimeSeriesCollection noSignalDataset = new TimeSeriesCollection( noSignalSeries );
        XYLineAndShapeRenderer noSignalRenderer = new XYLineAndShapeRenderer( true, false );
        noSignalRenderer.setAutoPopulateSeriesPaint( false );
        noSignalRenderer.setDefaultPaint( Color.red );
        plot.setDataset( 0, noSignalDataset );
        plot.setRenderer( 0, noSignalRenderer );

        TimeSeriesCollection signalDataset = new TimeSeriesCollection( signalSeries );
        XYLineAndShapeRenderer signalRenderer = new XYLineAndShapeRenderer( true, false );
        signalRenderer.setAutoPopulateSeriesPaint( false );
        signalRenderer.setDefaultPaint( Color.blue );
        plot.setDataset( 1, signalDataset );
        plot.setRenderer( 1, signalRenderer );

        TimeSeriesCollection signalAvgDataset = new TimeSeriesCollection( signalAvgSeries );
        XYLineAndShapeRenderer signalAvgRenderer = new XYLineAndShapeRenderer( true, false );
        signalAvgRenderer.setAutoPopulateSeriesPaint( false );
        signalAvgRenderer.setAutoPopulateSeriesStroke( false );
        signalAvgRenderer.setDefaultPaint( Color.green );
        signalAvgRenderer
            .setDefaultStroke( new BasicStroke( 5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
        plot.setDataset( 2, signalAvgDataset );
        plot.setRenderer( 2, signalAvgRenderer );

        TimeSeriesCollection signalAvg3sigmaDataset = new TimeSeriesCollection( signalAvg3SigmaSeries );
        XYLineAndShapeRenderer signalAvg3sigmaRenderer = new XYLineAndShapeRenderer( true, false );
        signalAvg3sigmaRenderer.setAutoPopulateSeriesPaint( false );
        signalAvg3sigmaRenderer.setAutoPopulateSeriesStroke( false );
        signalAvg3sigmaRenderer.setDefaultPaint( Color.black );
        signalAvg3sigmaRenderer
            .setDefaultStroke( new BasicStroke( 5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
        plot.setDataset( 3, signalAvg3sigmaDataset );
        plot.setRenderer( 3, signalAvg3sigmaRenderer );

        TimeSeriesCollection signalAvgRcvCountDataset = new TimeSeriesCollection( avgRcvCountSeries );
        XYLineAndShapeRenderer signalAvgRcvCountRenderer = new XYLineAndShapeRenderer( true, false );
        signalAvgRcvCountRenderer.setAutoPopulateSeriesPaint( false );
        signalAvgRcvCountRenderer.setAutoPopulateSeriesStroke( false );
        signalAvgRcvCountRenderer.setDefaultPaint( Color.gray );
        signalAvgRcvCountRenderer
            .setDefaultStroke( new BasicStroke( 5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
        plot.setDataset( 4, signalAvgRcvCountDataset );
        plot.setRenderer( 4, signalAvgRcvCountRenderer );

        JFreeChart chart = new JFreeChart( "Title", JFreeChart.DEFAULT_TITLE_FONT, plot, true );

        return chart;
    }
}
