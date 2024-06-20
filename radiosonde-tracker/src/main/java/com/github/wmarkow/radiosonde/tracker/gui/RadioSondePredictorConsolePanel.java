package com.github.wmarkow.radiosonde.tracker.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;
import com.github.wmarkow.radiosonde.tracker.geotools.RadioSondeMapContent;

public class RadioSondePredictorConsolePanel extends JPanel
{
    private static final long serialVersionUID = 5754181361955635533L;

    private RadioSondeMapContent radioSondeMapContent;
    private JLabel altitudeLabel;
    private JLabel fallingSpeedLabel;
    private JLabel speedLabel;
    private JLabel courseLabel;
    private Font fontHeader = new Font( "serif", Font.BOLD, 20 );
    private Font font = new Font( "serif", Font.PLAIN, 18 );

    public RadioSondePredictorConsolePanel()
    {
        super( new GridBagLayout() );

        buildGui();
    }

    public void setMapContent( RadioSondeMapContent mapContent )
    {
        this.radioSondeMapContent = mapContent;
    }

    public void refreshGui()
    {
        DataPoint youngestDataPoint = radioSondeMapContent.getSondeDataSet().getYoungestDataPoint();
        altitudeLabel.setText( String.format( "%s m", youngestDataPoint.altitude_m ) );
        fallingSpeedLabel.setText( String.format( "%s m/s", Math.abs( youngestDataPoint.climbing_m_s ) ) );
        speedLabel.setText( String.format( "%s km/h", Math.abs( youngestDataPoint.speed_km_h ) ) );
        courseLabel.setText( String.format( "%s deg", Math.abs( youngestDataPoint.course ) ) );
    }

    private void buildGui()
    {
        setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        
        /* Current data */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        JLabel currentDataLabel = new JLabel( "Current data" );
        currentDataLabel.setFont( fontHeader );
        add( currentDataLabel, gbc );

        /* Altitude */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JLabel altLabel = new JLabel( "Altitude " );
        altLabel.setFont( font );
        add( altLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        altitudeLabel = new JLabel();
        altitudeLabel.setFont( font );
        add( altitudeLabel, gbc );

        /* Falling speed */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JLabel fsLabel = new JLabel( "Falling speed " );
        fsLabel.setFont( font );
        add( fsLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        fallingSpeedLabel = new JLabel();
        fallingSpeedLabel.setFont( font );
        add( fallingSpeedLabel, gbc );

        /* Speed */
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JLabel sLabel = new JLabel( "Speed " );
        sLabel.setFont( font );
        add( sLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        speedLabel = new JLabel();
        speedLabel.setFont( font );
        add( speedLabel, gbc );

        /* Course */
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JLabel cLabel = new JLabel( "Course " );
        cLabel.setFont( font );
        add( cLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        courseLabel = new JLabel();
        courseLabel.setFont( font );
        add( courseLabel, gbc );

        JSlider sondeAgeSlider = new JSlider( JSlider.VERTICAL, 0, 100, 0 );
        sondeAgeSlider.setBorder( BorderFactory.createEmptyBorder( 10, 0, 0, 10 ) );
        JSlider predictionAgeSlider = new JSlider( JSlider.VERTICAL, 0, 60, 60 );
        predictionAgeSlider.setBorder( BorderFactory.createEmptyBorder( 10, 0, 0, 10 ) );

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
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

                refreshGui();
            }
        } );
        add( sondeAgeSlider, gbc );

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        predictionAgeSlider.setMajorTickSpacing( 5 );
        predictionAgeSlider.setMinorTickSpacing( 1 );
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

                refreshGui();
            }
        } );
        add( predictionAgeSlider, gbc );
    }
}
