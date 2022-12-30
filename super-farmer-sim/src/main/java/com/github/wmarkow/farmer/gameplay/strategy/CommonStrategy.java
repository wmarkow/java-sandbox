package com.github.wmarkow.farmer.gameplay.strategy;

import java.util.HashMap;
import java.util.Map;

import com.github.wmarkow.farmer.domain.Animal;
import com.github.wmarkow.farmer.domain.Herd;
import com.github.wmarkow.farmer.gameplay.Shepherd;
import com.github.wmarkow.farmer.gameplay.Strategy;

public class CommonStrategy extends Strategy
{

    private Shepherd shepherd = new Shepherd();

    @Override
    public boolean doRound( Herd mainHerd, Herd playerHerd, Animal blueDiceAnimal, Animal redDiceAnimal )
    {
        System.out.println( String.format( "Begin         : %s", playerHerd.getHerdCounts() ) );

        boolean herdChanged = false;

        // exchange animals
        if( playerHerd.getCount( Animal.RABBIT ) > 12 && mainHerd.getCount( Animal.SHEEP ) > 0 )
        {
            shepherd.punch( playerHerd, mainHerd, Animal.RABBIT, 6 );
            shepherd.punch( mainHerd, playerHerd, Animal.SHEEP, 1 );
            herdChanged = true;
        }

        if( playerHerd.getCount( Animal.SMALL_DOG ) == 0 )
        {
            if( playerHerd.getCount( Animal.SHEEP ) > 1 && mainHerd.getCount( Animal.SMALL_DOG ) > 0 )
            {
                shepherd.punch( playerHerd, mainHerd, Animal.SHEEP, 1 );
                shepherd.punch( mainHerd, playerHerd, Animal.SMALL_DOG, 1 );
                herdChanged = true;
            }
        }

        if( playerHerd.getCount( Animal.SHEEP ) > 3 && mainHerd.getCount( Animal.PIG ) > 0 )
        {
            shepherd.punch( playerHerd, mainHerd, Animal.SHEEP, 2 );
            shepherd.punch( mainHerd, playerHerd, Animal.PIG, 1 );
            herdChanged = true;
        }

        if( playerHerd.getCount( Animal.PIG ) > 3 && mainHerd.getCount( Animal.COW ) > 0 )
        {
            shepherd.punch( playerHerd, mainHerd, Animal.PIG, 3 );
            shepherd.punch( mainHerd, playerHerd, Animal.COW, 1 );
            herdChanged = true;
        }

        if( playerHerd.getCount( Animal.BIG_DOG ) == 0 )
        {
            if( playerHerd.getCount( Animal.COW ) > 1 && mainHerd.getCount( Animal.BIG_DOG ) > 0 )
            {
                shepherd.punch( playerHerd, mainHerd, Animal.COW, 1 );
                shepherd.punch( mainHerd, playerHerd, Animal.BIG_DOG, 1 );
                herdChanged = true;
            }
        }

        if( playerHerd.getCount( Animal.COW ) > 2 && mainHerd.getCount( Animal.HORSE ) > 0 )
        {
            shepherd.punch( playerHerd, mainHerd, Animal.COW, 2 );
            shepherd.punch( mainHerd, playerHerd, Animal.HORSE, 1 );
            herdChanged = true;
        }

        System.out.println( String.format( "After exchange: %s", playerHerd.getHerdCounts() ) );

        // apply FOX
        if( isFox( blueDiceAnimal, redDiceAnimal ) )
        {
            if( playerHerd.getCount( Animal.SMALL_DOG ) > 0 )
            {
                shepherd.punch( playerHerd, mainHerd, Animal.SMALL_DOG, 1 );
            }
            else
            {
                shepherd.punch( playerHerd, mainHerd, Animal.RABBIT, playerHerd.getCount( Animal.RABBIT ) );
                shepherd.punch( mainHerd, playerHerd, Animal.RABBIT, 1 );
            }

            herdChanged = true;
        }

        // apply WOLF
        if( isWolf( blueDiceAnimal, redDiceAnimal ) )
        {
            if( playerHerd.getCount( Animal.BIG_DOG ) > 0 )
            {
                shepherd.punch( playerHerd, mainHerd, Animal.BIG_DOG, 1 );
            }
            else
            {
                shepherd.punch( playerHerd, mainHerd, Animal.SHEEP, playerHerd.getCount( Animal.SHEEP ) );
                shepherd.punch( playerHerd, mainHerd, Animal.PIG, playerHerd.getCount( Animal.PIG ) );
                shepherd.punch( playerHerd, mainHerd, Animal.COW, playerHerd.getCount( Animal.COW ) );
            }
            herdChanged = true;
        }
        System.out.println( String.format( "After FOX/WOLF: %s", playerHerd.getHerdCounts() ) );

        // grow the herd
        Map< Animal, Integer > rolledAnimals = new HashMap<>();
        if( !Animal.FOX.equals( blueDiceAnimal ) && !Animal.WOLF.equals( blueDiceAnimal ) )
        {
            Integer count = rolledAnimals.getOrDefault( blueDiceAnimal, 0 );
            rolledAnimals.put( blueDiceAnimal, count + 1 );
        }
        if( !Animal.FOX.equals( redDiceAnimal ) && !Animal.WOLF.equals( redDiceAnimal ) )
        {
            Integer count = rolledAnimals.getOrDefault( redDiceAnimal, 0 );
            rolledAnimals.put( redDiceAnimal, count + 1 );
        }

        for( Animal rolledAnimal : rolledAnimals.keySet() )
        {
            int count = rolledAnimals.get( rolledAnimal ) + playerHerd.getCount( rolledAnimal );
            int growCount = count / 2;

            int movedCount = shepherd.punch( mainHerd, playerHerd, rolledAnimal, growCount );

            if( movedCount > 0 )
            {
                herdChanged = true;
            }
        }

        System.out.println( String.format( "After grow    : %s", playerHerd.getHerdCounts() ) );

        return herdChanged;
    }

    @Override
    public boolean isGameWon( Herd playerHerd )
    {
        if( playerHerd.getCount( Animal.HORSE ) == 0 )
        {
            return false;
        }
        if( playerHerd.getCount( Animal.COW ) == 0 )
        {
            return false;
        }
        if( playerHerd.getCount( Animal.PIG ) == 0 )
        {
            return false;
        }
        if( playerHerd.getCount( Animal.SHEEP ) == 0 )
        {
            return false;
        }
        if( playerHerd.getCount( Animal.RABBIT ) == 0 )
        {
            return false;
        }

        return true;
    }

    protected boolean isFox( Animal blueDiceAnimal, Animal redDiceAnimal )
    {
        if( Animal.FOX.equals( blueDiceAnimal ) )
        {
            return true;
        }

        if( Animal.FOX.equals( redDiceAnimal ) )
        {
            return true;
        }

        return false;
    }

    protected boolean isWolf( Animal blueDiceAnimal, Animal redDiceAnimal )
    {
        if( Animal.WOLF.equals( blueDiceAnimal ) )
        {
            return true;
        }

        if( Animal.WOLF.equals( redDiceAnimal ) )
        {
            return true;
        }

        return false;
    }
}
