package com.github.wmarkow.farmer.gameplay;

import com.github.wmarkow.farmer.domain.Animal;
import com.github.wmarkow.farmer.domain.Herd;

public class Shepherd {

	public int punch(Herd srcHerd, Herd dstHerd, Animal animal, int count) {		
		int removedCount = srcHerd.remove(animal, count);
		
		dstHerd.add(animal, removedCount);

		return removedCount;
	}
}
