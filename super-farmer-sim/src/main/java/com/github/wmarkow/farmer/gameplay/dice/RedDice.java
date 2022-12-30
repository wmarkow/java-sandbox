package com.github.wmarkow.farmer.gameplay.dice;

import com.github.wmarkow.farmer.domain.Animal;

public class RedDice extends Dice {

	@Override
	public Animal roll() {
		switch (random.nextInt(12)) {
		case 0:
			return Animal.FOX;
		case 1:
			return Animal.RABBIT;
		case 2:
			return Animal.RABBIT;
		case 3:
			return Animal.SHEEP;
		case 4:
			return Animal.RABBIT;
		case 5:
			return Animal.PIG;
		case 6:
			return Animal.HORSE;
		case 7:
			return Animal.RABBIT;
		case 8:
			return Animal.RABBIT;
		case 9:
			return Animal.PIG;
		case 10:
			return Animal.RABBIT;
		case 11:
			return Animal.SHEEP;
		}
		
		return Animal.RABBIT;
	}

}
