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

public class RadioSondeTrackerFrame extends JMapFrame
{
    public RadioSondeTrackerFrame( MapContent content )
    {
        super( content );
    }

    public static void showMap( final MapContent content )
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
        gbc.weightx = 0.9;
        gbc.weighty = 1.0;
        newPanel.add( originalMapPanel, gbc );

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        JSlider predictionAgeSlider = new JSlider( JSlider.VERTICAL, 0, 100, 100 );
        predictionAgeSlider.setMajorTickSpacing( 10 );
        predictionAgeSlider.setMinorTickSpacing( 5 );
        predictionAgeSlider.setPaintTicks( true );
        predictionAgeSlider.setPaintLabels( true );
        predictionAgeSlider.addChangeListener( new ChangeListener()
        {

            @Override
            public void stateChanged( ChangeEvent e )
            {
                // TODO Auto-generated method stub

            }
        } );
        newPanel.add( predictionAgeSlider, gbc );

        this.getContentPane().add( newPanel );
    }

    private static void doShowMap( MapContent content )
    {
        final RadioSondeTrackerFrame frame = new RadioSondeTrackerFrame( content );
        frame.enableStatusBar( true );
        frame.enableToolBar( true );
        frame.initComponents();
        frame.setSize( 800, 600 );
        frame.setVisible( true );
    }
}
