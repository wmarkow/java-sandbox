package com.github.wmarkow.radiosonde.tracker.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.ZonedDateTime;

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
    private JLabel landingCountdownRedLabel;
    private JLabel landingCountdownYellowLabel;
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

        DataPoint latestDataPoint = radioSondeMapContent.getSondeLatestDataPoint();

        ZonedDateTime redLandingPrediction = radioSondeMapContent.getAvgLandingTimeRedPrediction();
        if( latestDataPoint != null && redLandingPrediction != null )
        {
            long span = redLandingPrediction.toEpochSecond() - latestDataPoint.utcDateTime.toEpochSecond();
            long minutes = span / 60;
            long seconds = span - minutes * 60;
            landingCountdownRedLabel.setText( String.format( "%s min : %s s", minutes, seconds ) );
        }
        else
        {
            landingCountdownRedLabel.setText( "???" );
        }

        ZonedDateTime yellowLandingPrediction = radioSondeMapContent.getAvgLandingTimeYellowPrediction();
        if( latestDataPoint != null && yellowLandingPrediction != null )
        {
            long span = yellowLandingPrediction.toEpochSecond() - latestDataPoint.utcDateTime.toEpochSecond();
            long minutes = span / 60;
            long seconds = span - minutes * 60;
            landingCountdownYellowLabel.setText( String.format( "%s min : %s s", minutes, seconds ) );
        }
        else
        {
            landingCountdownYellowLabel.setText( "???" );
        }
    }

    private void buildGui()
    {
        setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

        GridBagConstraints gbc = new GridBagConstraints();

        /* Current data */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
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
        gbc.anchor = GridBagConstraints.EAST;
        JLabel altLabel = new JLabel( "Altitude " );
        altLabel.setFont( font );
        add( altLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
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
        gbc.anchor = GridBagConstraints.EAST;
        JLabel fsLabel = new JLabel( "Falling speed " );
        fsLabel.setFont( font );
        add( fsLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
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
        gbc.anchor = GridBagConstraints.EAST;
        JLabel sLabel = new JLabel( "Speed " );
        sLabel.setFont( font );
        add( sLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
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
        gbc.anchor = GridBagConstraints.EAST;
        JLabel cLabel = new JLabel( "Course " );
        cLabel.setFont( font );
        add( cLabel, gbc );
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        courseLabel = new JLabel();
        courseLabel.setFont( font );
        add( courseLabel, gbc );

        /* Prediction RED */
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel predictionRedLabel = new JLabel( "Prediction RED" );
        predictionRedLabel.setFont( fontHeader );
        add( predictionRedLabel, gbc );

        /* Landing countdown (red) */
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lc1Label = new JLabel( "Countdown " );
        lc1Label.setFont( font );
        add( lc1Label, gbc );
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        landingCountdownRedLabel = new JLabel();
        landingCountdownRedLabel.setFont( font );
        add( landingCountdownRedLabel, gbc );

        /* Prediction YELLOW */
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel predictionYellowLabel = new JLabel( "Prediction YELLOW" );
        predictionYellowLabel.setFont( fontHeader );
        add( predictionYellowLabel, gbc );

        /* Landing countdown (yellow) */
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lc2Label = new JLabel( "Countdown " );
        lc2Label.setFont( font );
        add( lc2Label, gbc );
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        landingCountdownYellowLabel = new JLabel();
        landingCountdownYellowLabel.setFont( font );
        add( landingCountdownYellowLabel, gbc );

        JSlider sondeAgeSlider = new JSlider( JSlider.VERTICAL, 0, 100, 0 );
        sondeAgeSlider.setBorder( BorderFactory.createEmptyBorder( 10, 0, 0, 10 ) );
        JSlider predictionAgeSlider = new JSlider( JSlider.VERTICAL, 0, 60, 60 );
        predictionAgeSlider.setBorder( BorderFactory.createEmptyBorder( 10, 0, 0, 10 ) );

        gbc.gridx = 0;
        gbc.gridy = 9;
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
        gbc.gridy = 9;
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
