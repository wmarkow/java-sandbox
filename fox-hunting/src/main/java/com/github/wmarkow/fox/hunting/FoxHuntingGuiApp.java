package com.github.wmarkow.fox.hunting;

import com.github.wmarkow.fox.hunting.gui.FoxHuntingFrame;

public class FoxHuntingGuiApp
{

    public static void main( String[] args )
    {
        javax.swing.SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                createAndShowGUI();
            }
        } );
    }

    private static void createAndShowGUI()
    {
        FoxHuntingFrame frame = new FoxHuntingFrame();

        frame.pack();
        frame.setVisible( true );

    }
}
