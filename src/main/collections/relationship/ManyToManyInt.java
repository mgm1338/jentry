package collections.relationship;

import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;

/**
 * Copyright 2/8/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/8/13
 */
public class ManyToManyInt extends OneToManyInt
{

    protected boolean countRights;
    protected int[] rightCounts = null;

    /** Array that will hold the first relationship from the index, will be the handle into <b>associations</b> */
    protected int[] rights;
    /** The next pointer that will fill the index of the current entry to the next handle (see class docs) */
    protected int[] rightNexts;


    /**
     * Constructor
     *
     * @param initialLefts        the initial number of lefts that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialRights       the initial number of rights that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialAssociations the size of initial number of associations we can make.
     * @param countLefts          whether or not to track the counts of our associations per left.
     * @param countRights         whether or not to track the counts of our associations per right.
     */
    public ManyToManyInt( int initialLefts, int initialRights, int initialAssociations, boolean countLefts, boolean countRights )
    {
        this( initialLefts, initialRights, initialAssociations, countLefts, countRights, GrowthStrategy.doubleGrowth,
              ArrayFactoryInt.defaultIntProvider );
    }

    /**
     * Full Constructor
     *
     * @param initialLefts        the initial number of lefts that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialRights       the initial number of rights that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialAssociations the size of initial number of associations we can make.
     * @param countLefts          whether or not to track the counts of our associations per left.
     * @param countRights         whether or not to track the counts of our associations per right.
     * @param growthStrategy      the growth strategy for growing our lefts array, associations array,
     *                            and counts array (if counting the associations)
     * @param intFactory          factory used to grow/allocate the arrays
     */
    public ManyToManyInt( int initialLefts, int initialRights, int initialAssociations, boolean countLefts,
                          boolean countRights, GrowthStrategy growthStrategy, ArrayFactoryInt intFactory )
    {
        super( initialLefts, initialAssociations, countLefts, growthStrategy, intFactory );
        this.countRights = countRights;
        if( countRights )
        {
            rightCounts = intFactory.alloc( initialRights );
        }
        rights = intFactory.alloc( initialRights, Const.NO_ENTRY );
        rightNexts = intFactory.alloc( initialAssociations, Const.NO_ENTRY );
    }

    public int getCountForRight( int right )
    {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    public int getNextLeftEntry( int right, int prevEntry)
    {
        return 0;
    }

    public int[] getAllLeftAssociations( int right, int[] target, int mark )
    {
        return new int[ 0 ];  //To change body of created methods use File | Settings | File Templates.
    }
}
