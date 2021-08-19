package com.github.wmarkow.farmer.gameplay.strategy;

import com.github.wmarkow.farmer.domain.Animal;
import com.github.wmarkow.farmer.domain.Herd;
import com.github.wmarkow.farmer.gameplay.Strategy;

public class DoNothingStrategy extends Strategy {

	@Override
	public boolean doRound(Herd mainHerd, Herd playerHerd, Animal blueDiceAnimal, Animal redDiceAnimal) {
		return false;
	}

	@Override
	public boolean isGameWon(Herd playerHerd) {
		return false;
	}
	
}
