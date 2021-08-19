package com.github.wmarkow.farmer.gameplay;

import org.junit.Test;

import com.github.wmarkow.farmer.gameplay.strategy.DoNothingStrategy;

public class DoNothingGameplay {

	private Strategy strategy = new DoNothingStrategy();

	@Test
	public void test() {

		Player player = new Player(strategy);
		Gameplay gameplay = new Gameplay(player);

		do {
			gameplay.doRound();
		} while (gameplay.isGameOver() == false);
		
		System.out.println("Game over!");
	}
}
