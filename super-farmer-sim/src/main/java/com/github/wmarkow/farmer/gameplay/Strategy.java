package com.github.wmarkow.farmer.gameplay;

import com.github.wmarkow.farmer.domain.Animal;
import com.github.wmarkow.farmer.domain.Herd;

public abstract class Strategy {

	/***
	 * Performs a single game round. It should return true if one of the herds has been changed.
	 * 
	 * @param mainHerd
	 * @param playerHerd
	 * @param blueDiceAnimal
	 * @param redDiceAnimal
	 * @return
	 */
	public abstract boolean doRound(Herd mainHerd, Herd playerHerd, Animal blueDiceAnimal, Animal redDiceAnimal);
	
	/***
	 * Checks if the player won the game.
	 * 
	 * @param playerHerd
	 * @return true if the player won the game, false otherwise
	 */
	public abstract boolean isGameWon(Herd playerHerd);
}
