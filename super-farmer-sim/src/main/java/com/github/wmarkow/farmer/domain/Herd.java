package com.github.wmarkow.farmer.domain;

import java.util.HashMap;
import java.util.Map;

public class Herd {

	private Map<Animal, Integer> animalsCount = new HashMap<Animal, Integer>();

	public int getCount(Animal animal) {
		if (animalsCount.containsKey(animal)) {
			return animalsCount.get(animal);
		}

		return 0;
	}

	public void setCount(Animal animal, int count) {
		animalsCount.put(animal, count);
	}

	public void add(Animal animal, int count) {
		int currentCount = getCount(animal);
		currentCount += count;
		setCount(animal, currentCount);
	}

	public int remove(Animal animal, int count) {
		int currentCount = getCount(animal);

		if (currentCount < count) {
			setCount(animal, 0);

			return currentCount;
		}

		int newCount = currentCount - count;
		setCount(animal, newCount);

		return count;
	}

    public String getHerdCounts()
    {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("[Small dog %02d]", getCount(Animal.SMALL_DOG)));
		sb.append(String.format("[Big dog %02d]", getCount(Animal.BIG_DOG)));
		sb.append(String.format("[Rabbit %02d]", getCount(Animal.RABBIT)));
		sb.append(String.format("[Sheep %02d]", getCount(Animal.SHEEP)));
		sb.append(String.format("[Pig %02d]", getCount(Animal.PIG)));
		sb.append(String.format("[Cow %02d]", getCount(Animal.COW)));
		sb.append(String.format("[Horse %02d]", getCount(Animal.HORSE)));

        return sb.toString();
	}
}
