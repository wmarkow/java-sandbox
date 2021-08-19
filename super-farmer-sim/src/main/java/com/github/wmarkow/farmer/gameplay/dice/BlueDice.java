package com.github.wmarkow.farmer.gameplay.dice;

import java.util.Random;

import com.github.wmarkow.farmer.domain.Animal;

public class BlueDice extends Dice {

	private Random random = new Random(System.currentTimeMillis());

	@Override
	public Animal roll() {
		switch (random.nextInt(12)) {
		case 0:
			return Animal.WOLF;
		case 1:
			return Animal.RABBIT;
		case 2:
			return Animal.RABBIT;
		case 3:
			return Animal.SHEEP;
		case 4:
			return Animal.RABBIT;
		case 5:
			return Animal.SHEEP;
		case 6:
			return Animal.COW;
		case 7:
			return Animal.RABBIT;
		case 8:
			return Animal.RABBIT;
		case 9:
			return Animal.RABBIT;
		case 10:
			return Animal.SHEEP;
		case 11:
			return Animal.PIG;
		}
		
		return Animal.RABBIT;
	}

}
