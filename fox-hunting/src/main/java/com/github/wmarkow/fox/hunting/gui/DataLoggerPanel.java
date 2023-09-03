package com.github.wmarkow.fox.hunting.gui;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DataLoggerPanel extends JPanel
{
    private JTextField descriptionTextField;
    private JButton button;

    private boolean isRecording = false;
    private FileWriter fileWriter;
    private PrintWriter printWriter;

    public DataLoggerPanel()
    {
        FlowLayout layout = new FlowLayout();
        setLayout( layout );
        setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT );

        descriptionTextField = new JTextField();
        descriptionTextField.setColumns( 80 );
        add( descriptionTextField );

        button = new JButton( "Start recording" );
        isRecording = false;
        add( button );
        button.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent aE )
            {
                if( isRecording == false )
                {
                    startRecording();
                }
                else
                {
                    stopRecording();
                }
            }
        } );
    }

    @Override
    public void setEnabled( boolean enabled )
    {
        descriptionTextField.setEnabled( enabled );
        button.setEnabled( enabled );
    }

    public void onRssiPreambleSignalMeassure( long milliseconds, int rssi )
    {
        if( printWriter != null )
        {
            printWriter.println( String.format( "%s;%s", milliseconds, rssi ) );
        }
    }

    public void stopRecording()
    {
        if( isRecording == false )
        {
            return;
        }
        isRecording = false;
        // close the file
        if( printWriter != null )
        {
            printWriter.flush();
            printWriter.close();
            printWriter = null;
        }
        if( fileWriter != null )
        {
            try
            {
                fileWriter.close();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
            fileWriter = null;
        }

        // update the GUI
        descriptionTextField.setEnabled( true );
        button.setText( "Start recording" );
    }

    protected void startRecording()
    {
        if( isRecording )
        {
            return;
        }

        isRecording = true;
        // open the file for writing
        File customDir = new File( System.getProperty( "user.home" ) + File.separator + ".fox-hunting" );
        customDir.mkdirs();
        String sufix = descriptionTextField.getText().trim().replaceAll( " ", "_" );
        File customFile =
            new File( customDir,
                new SimpleDateFormat( "yyyy-MM-dd-HHmmss" ).format( new Date() ) + "_" + sufix + ".csv" );

        try
        {
            fileWriter = new FileWriter( customFile );
            printWriter = new PrintWriter( fileWriter, true ); // auto flush

            printWriter.println( "Timestamp;RSSI" );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        // update the GUI
        descriptionTextField.setEnabled( false );
        button.setText( "Stop recording" );
    }
}
