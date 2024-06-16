package com.github.wmarkow.radiosonde.tracker.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.wmarkow.radiosonde.tracker.geotools.RadioSondeMapContent;

public class RadioSondePredictorConsolePanel extends JPanel
{
    private static final long serialVersionUID = 5754181361955635533L;

    private RadioSondeMapContent radioSondeMapContent;

    public RadioSondePredictorConsolePanel()
    {
        super( new GridBagLayout() );

        buildGui();
    }

    public void setMapContent( RadioSondeMapContent mapContent )
    {
        this.radioSondeMapContent = mapContent;
    }

    private void buildGui()
    {
        setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

        GridBagConstraints gbc = new GridBagConstraints();
        JSlider sondeAgeSlider = new JSlider( JSlider.VERTICAL, 0, 100, 0 );
        sondeAgeSlider.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 10 ) );
        JSlider predictionAgeSlider = new JSlider( JSlider.VERTICAL, 0, 100, 100 );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1.0;
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
        add( sondeAgeSlider, gbc );

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1.0;
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
        add( predictionAgeSlider, gbc );
    }
}
