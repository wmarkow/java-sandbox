package com.github.wmarkow.farmer.gameplay;

import java.util.ArrayList;
import java.util.List;

import com.github.wmarkow.farmer.domain.Animal;
import com.github.wmarkow.farmer.domain.Herd;
import com.github.wmarkow.farmer.gameplay.dice.BlueDice;
import com.github.wmarkow.farmer.gameplay.dice.Dice;
import com.github.wmarkow.farmer.gameplay.dice.RedDice;

public class Gameplay {

	private Dice redDice = new RedDice();
	private Dice blueDice = new BlueDice();
	private Herd mainHerd = new Herd();

	private List<Player> players = new ArrayList<Player>();

	private int countOfUnsuccessfullRounds = 0;
	private int roundCount = 0;
	private boolean playerWon = false;

	public Gameplay(Player... players) {
		mainHerd.setCount(Animal.SMALL_DOG, 4);
		mainHerd.setCount(Animal.BIG_DOG, 2);
		mainHerd.setCount(Animal.RABBIT, 60);
		mainHerd.setCount(Animal.SHEEP, 24);
		mainHerd.setCount(Animal.PIG, 20);
		mainHerd.setCount(Animal.COW, 12);
		mainHerd.setCount(Animal.HORSE, 6);
		
		
		Shepherd shepherd = new Shepherd();
		
		for (Player player : players) {
			this.players.add(player);
			
			// move one rabbit from main herd to player's herd
			shepherd.punch(mainHerd, player.getHerd(), Animal.RABBIT, 1);
		}
	}

	/***
	 * Performs a single game round
	 */
	public void doRound() {
		roundCount ++;
		System.out.println(String.format("Round %s", roundCount));
		
		boolean roundResult = false;
		
		for(Player player : players) {
			Animal redDiceAnimal = redDice.roll();
			Animal blueDiceAnimal = blueDice.roll();
			
            // display players dice rolls
            System.out.println( String.format( "[%s][%s]", blueDiceAnimal, redDiceAnimal ) );

			roundResult |= player.doRound(mainHerd, blueDiceAnimal, redDiceAnimal);
			
			if(player.isGameWon()) {
                playerWon = true;
			}
				
		}
		
		if(roundResult)
		{
			countOfUnsuccessfullRounds = 0;
			
		} else {
			countOfUnsuccessfullRounds ++;
		}
	}
	
	public boolean isGameOver() {
		if(playerWon) {
			return true;
		}
		
		if(countOfUnsuccessfullRounds >= 10) {
			return true;
		}
		
		return false;
	}

    public boolean isPlayerWon()
    {
        return playerWon;
    }

    public int getRoundCount()
    {
        return this.roundCount;
    }
}
