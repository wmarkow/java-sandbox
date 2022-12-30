package com.github.wmarkow.farmer.gameplay;

import org.junit.Test;

import com.github.wmarkow.farmer.gameplay.strategy.CommonStrategy;

public class CommonGameplay {

    private Strategy strategy = new CommonStrategy();

	@Test
	public void testSinglePlayer() {
		Player player = new Player(strategy);
		Gameplay gameplay = new Gameplay(player);

		do {
			gameplay.doRound();
		} while (gameplay.isGameOver() == false);

        System.out
            .println( String.format( "Game over! Player has %s", gameplay.isPlayerWon() ? "won" : "lost" ) );
	}
	
	@Test
    public void testSinglePlayerAverage()
    {
        double summOfRounds = 0;
        double countOfRounds = 0;
        double minCountOfRounds = Integer.MAX_VALUE;

        for( int count = 0; count < 1000; count++ )
        {
            Player player = new Player( strategy );
            Gameplay gameplay = new Gameplay( player );

            do
            {
                gameplay.doRound();
            }
            while( gameplay.isGameOver() == false );

            countOfRounds++;

            if( gameplay.isPlayerWon() )
            {
                summOfRounds += gameplay.getRoundCount();
                if( gameplay.getRoundCount() < minCountOfRounds )
                {
                    minCountOfRounds = gameplay.getRoundCount();
                }
            }
            else
            {

            }
        }

        System.out.println( "Average rounds: = " + summOfRounds / countOfRounds );
        System.out.println( "    Min rounds: = " + minCountOfRounds );
    }

    @Test
	public void test4Players() {
		Player player1 = new Player(strategy);
		Player player2 = new Player(strategy);
		Player player3 = new Player(strategy);
		Player player4 = new Player(strategy);
		
		Gameplay gameplay = new Gameplay(player1, player2, player3, player4);

		do {
			gameplay.doRound();
		} while (gameplay.isGameOver() == false);

		System.out.println("Game over!");
	}
}
