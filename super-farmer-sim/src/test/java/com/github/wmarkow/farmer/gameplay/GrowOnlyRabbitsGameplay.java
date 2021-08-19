package com.github.wmarkow.farmer.gameplay;

import org.junit.Test;

import com.github.wmarkow.farmer.gameplay.strategy.GrowOnlyRabbitsStrategy;

public class GrowOnlyRabbitsGameplay {

	private Strategy strategy = new GrowOnlyRabbitsStrategy();

	@Test
	public void testSinglePlayer() {
		Player player = new Player(strategy);
		Gameplay gameplay = new Gameplay(player);

		do {
			gameplay.doRound();
		} while (gameplay.isGameOver() == false);

		System.out.println("Game over!");
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
