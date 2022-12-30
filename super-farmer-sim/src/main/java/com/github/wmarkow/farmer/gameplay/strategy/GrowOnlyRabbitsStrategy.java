package com.github.wmarkow.farmer.gameplay.strategy;

import com.github.wmarkow.farmer.domain.Animal;
import com.github.wmarkow.farmer.domain.Herd;
import com.github.wmarkow.farmer.gameplay.Shepherd;
import com.github.wmarkow.farmer.gameplay.Strategy;

public class GrowOnlyRabbitsStrategy extends Strategy {

	private Shepherd shepherd = new Shepherd();
	private boolean ignoreFox = false;
	
	
	public void setIgnoreFox(boolean ignoreFox) {
		this.ignoreFox = ignoreFox;
	}
	
	@Override
	public boolean doRound(Herd mainHerd, Herd playerHerd, Animal blueDiceAnimal, Animal redDiceAnimal) {
		
		if(ignoreFox == false) {
			if(Animal.FOX.equals(blueDiceAnimal) || Animal.FOX.equals(redDiceAnimal)) {
				int rabbitCount = playerHerd.getCount(Animal.RABBIT);
				
				shepherd.punch(playerHerd, mainHerd, Animal.RABBIT, rabbitCount);
				shepherd.punch(mainHerd, playerHerd, Animal.RABBIT, 1);
				
				return true;
			}
		}
		
        if( Animal.WOLF.equals( blueDiceAnimal ) || Animal.WOLF.equals( redDiceAnimal ) )
        {
            return false;
        }

        if( !Animal.RABBIT.equals( blueDiceAnimal ) && !Animal.RABBIT.equals( redDiceAnimal ) )
        {
            // no rabbits
            return false;
        }

		int rabbitCount = playerHerd.getCount(Animal.RABBIT);
		
		if(Animal.RABBIT.equals(blueDiceAnimal))
		{
			rabbitCount++;
		}
		
		if(Animal.RABBIT.equals(redDiceAnimal))
		{
			rabbitCount++;
		}
		
		int newRabbits = rabbitCount / 2;
		
		
		int movedCount = shepherd.punch(mainHerd, playerHerd, Animal.RABBIT, newRabbits);
		
		if(movedCount > 0)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isGameWon(Herd playerHerd) {
		return playerHerd.getCount(Animal.RABBIT) == 60;
	}
}
