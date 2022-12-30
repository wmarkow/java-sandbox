package com.github.wmarkow.farmer.gameplay.dice;

import java.util.Random;

import com.github.wmarkow.farmer.domain.Animal;

public abstract class Dice {

    protected final static Random random = new Random( System.currentTimeMillis() );

	public abstract Animal roll();
}
