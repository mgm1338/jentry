package collections.relationship;


import collections.Collection;
import collections.hash.set.HashSetLong;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;

/**
 * Copyright 1/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/27/13
 * <p/>
 * <p>
 * A collection of One (left) to Many (right) ints. This is a useful structure used in conjunction
 * with the Jentry collections, associating the int handles together. The One to Many allows
 * for iteration over the associations from a left, going in order, as well as checking to see if a left
 * and right are associated.
 * </p>
 */
public class OneToMany implements Collection
{
    protected final GrowthStrategy growthStrategy;
    protected final ArrayFactoryInt intFactory;


    protected HashSetLong associations;
    protected int[] lefts;
    protected int[] leftNexts;
    protected int[] leftCounts;
    protected boolean countLefts = false;

    public OneToMany( int initialLefts, int initialAssociations, boolean countLefts )
    {
        this( initialLefts, initialAssociations, countLefts, GrowthStrategy.doubleGrowth,
              ArrayFactoryInt.defaultIntProvider );
    }

    public OneToMany( int initialLefts, int initialAssociations, boolean countLefts,
                      GrowthStrategy growthStrategy, ArrayFactoryInt intFactory )
    {
        this.growthStrategy = growthStrategy;
        this.intFactory = intFactory;
    }

    public int associate( int left, int right )
    {
        return Const.NO_ENTRY;
    }

    /**
     * Get the current size of the collection.
     *
     * @return the size
     */
    @Override
    public int getSize()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Is the current collection empty.
     *
     * @return true if collection is empty, false otherwise
     */
    @Override
    public boolean isEmpty()
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /** Clear the collection, empty out all of its contents. */
    @Override
    public void clear()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isAssociated( int left, int right )
    {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    public int getNextRightEntry( int left, int prevEntry )
    {
        return Const.NO_ENTRY;
    }

    public int getRight( int entry )
    {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    public int[] getAllRightAssociations( int left, int[] target )
    {
        return null;
    }

    public boolean disassociate( int left, int right )
    {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    public int getCountForLeft( int left )
    {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }
}
