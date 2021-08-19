package com.github.wmarkow.farmer.gameplay;

import com.github.wmarkow.farmer.domain.Animal;
import com.github.wmarkow.farmer.domain.Herd;

public class Player {

	private Herd herd = new Herd();
	
	private Strategy strategy;
	
	public Player(Strategy strategy) {
		if(strategy == null) {
			throw new IllegalArgumentException("Strategy must not be null");
		}
		
		this.strategy = strategy;
	}
	
	public boolean doRound(Herd mainHerd, Animal blueDiceAnimal, Animal redDiceAnimal) {
		return strategy.doRound(mainHerd, herd, blueDiceAnimal, redDiceAnimal);
	}
	
	public Herd getHerd() {
		return herd;
	}
	
	public boolean isGameWon() {
		return strategy.isGameWon(herd);
	}
}
