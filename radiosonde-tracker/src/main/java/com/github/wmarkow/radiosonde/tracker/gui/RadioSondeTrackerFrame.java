package com.github.wmarkow.radiosonde.tracker.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.geotools.map.MapContent;
import org.geotools.swing.JMapFrame;

import com.github.wmarkow.radiosonde.tracker.geotools.RadioSondeMapContent;

public class RadioSondeTrackerFrame extends JMapFrame
{
    private RadioSondeMapContent radioSondeMapContent;
    
    public RadioSondeTrackerFrame( RadioSondeMapContent content )
    {
        super( content );
        
        this.radioSondeMapContent = content;
    }

    public static void showMap( final RadioSondeMapContent content )
    {
        if( SwingUtilities.isEventDispatchThread() )
        {
            doShowMap( content );
        }
        else
        {
            SwingUtilities.invokeLater( new Runnable()
            {
                @Override
                public void run()
                {
                    doShowMap( content );
                }
            } );
        }
    }

    @Override
    public void initComponents()
    {
        super.initComponents();

        Component originalMapPanel = getContentPane().getComponent( 0 );
        getContentPane().remove( 0 );

        JPanel newPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        newPanel.setLayout( gridBagLayout );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        newPanel.add( originalMapPanel, gbc );

        JSlider sondeAgeSlider = new JSlider( JSlider.VERTICAL, 0, 100, 0 );
        JSlider predictionAgeSlider = new JSlider( JSlider.VERTICAL, 0, 100, 100 );
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        sondeAgeSlider.setMajorTickSpacing( 10 );
        sondeAgeSlider.setMinorTickSpacing( 5 );
        sondeAgeSlider.setSnapToTicks( true );
        sondeAgeSlider.setPaintTicks( true );
        sondeAgeSlider.setPaintLabels( true );
        sondeAgeSlider.addChangeListener( new ChangeListener()
        {

            @Override
            public void stateChanged( ChangeEvent e )
            {
                int sondeAge = sondeAgeSlider.getValue();
                radioSondeMapContent.recalculateSondeData( sondeAge );
                radioSondeMapContent.recalculatePrediction( predictionAgeSlider.getValue() );
                radioSondeMapContent.recalculateAdvancedPrediction( predictionAgeSlider.getValue() );
            }
        } );
        newPanel.add( sondeAgeSlider, gbc );
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
       
        predictionAgeSlider.setMajorTickSpacing( 10 );
        predictionAgeSlider.setMinorTickSpacing( 5 );
        predictionAgeSlider.setSnapToTicks( false );
        predictionAgeSlider.setPaintTicks( true );
        predictionAgeSlider.setPaintLabels( true );
        predictionAgeSlider.addChangeListener( new ChangeListener()
        {

            @Override
            public void stateChanged( ChangeEvent e )
            {
                int predictionAge = predictionAgeSlider.getValue();
                radioSondeMapContent.recalculatePrediction( predictionAge );
                radioSondeMapContent.recalculateAdvancedPrediction( predictionAge );
            }
        } );
        newPanel.add( predictionAgeSlider, gbc );
        
        

        this.getContentPane().add( newPanel );
    }

    private static void doShowMap( RadioSondeMapContent content )
    {
        final RadioSondeTrackerFrame frame = new RadioSondeTrackerFrame( content );
        frame.enableStatusBar( true );
        frame.enableToolBar( true );
        frame.initComponents();
        frame.setSize( 800, 600 );
        frame.setVisible( true );
    }
}
