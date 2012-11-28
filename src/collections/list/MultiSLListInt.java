package collections.list;

import core.Const;
import core.Types;
import core.array.ArrayGrowthException;
import core.array.ArrayUtil;
import core.array.GrowthStrategy;


/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p>A collection of singly linked int heads, together in two arrays
 * structure. Used mostly as a utility in Jentry structures when
 * multiple integer linked heads are required.</p>
 * <p/>
 * More formal example - 2 heads
 * <pre>
 *   heads nexts
 * 0 [9]   [2 ]
 * 1 [80]  [4]
 * 2 [8]   [3]
 * 3 [17 ] [-1]
 * 4 [45]   [-1]
 * </pre>
 * <p/>
 * Is equivalent to one list with values 9,8,17, and one with values 80,
 * 45.
 */
public class MultiSLListInt
{
    private static GrowthStrategy growthStrategy = GrowthStrategy.doubleGrowth;

    protected int[] heads;
    protected int[] nexts;
    protected int freeListPtr = Const.NO_ENTRY;
    protected int nextUnusedEntry = 0;
    protected int maxHead = -1;
    protected int size;

    public MultiSLListInt( int initialListSize, int totalEntrySize )
    {
        this.heads = new int[ initialListSize ];
        this.nexts = new int[ totalEntrySize - initialListSize ];
    }

    /**
     * With the listHead provided, get the next available entry (index)
     * to insert the value.
     *
     * @param listHead the head of the list
     * @return the entry the next free entry
     */
    protected int getNextEntry( int listHead )
    {
        if( listHead > maxHead ) //if we are greater, may have to grow heads
        {
            growHeads( GrowthStrategy.doubleGrowth, listHead );
        }

        //entry is spot where we are to insert the value
        int entry;
        if( freeListPtr != Const.NO_ENTRY ) //have a free one
        {
            entry = freeListPtr;
            freeListPtr = nexts[ entry ];
        }
        else
        {
            entry = nextUnusedEntry++;
            nexts = ArrayUtil.ArrayProviderInt.ensureArrayCapacity( nexts,
                                                                    nextUnusedEntry,
                                                                    Const.NO_ENTRY,
                                                                    growthStrategy );
        }
        //Prepend the entry to the linked list, //TODO: make function, comment
        // and test
        heads[entry] = heads[listHead];
        nexts[entry] = nexts[listHead];
        nexts[listHead] = entry;
        return entry;
    }

    /**
     * <p>The method will grow the set of available heads. This will consist of
     * first and foremost moving the heads and nexts.</p>
     * <p/>
     * For example, if the lists are as follows (see TestMultiSList):
     * <pre>
     * idx  heads   nexts
     * 0    8       3
     * 1    15      4
     * 2    6       -1
     * 3    9       -1
     * 4    16      5
     * 5    17      -1
     *
     * and we would like to grow the available heads from the set of {0,1,2}
     * to double the heads to {0,1,2,3,4,5}, then the resulting array
     * will have to be of the form:
     *
     * 0    8       6
     * 1    15      7
     * 2    6      -1
     * 3    x      -1
     * 4    x      -1
     * 5    x      -1       (x denotes doesn't matter, will disregard)
     * 6    9      -1
     * 7    15      8
     * 8    17     -1
     * </pre>
     * <p/>
     * <p>This example should show that in the resulting list,
     * the heads are simply shifted down by the increase in heads (3),
     * while the nexts are the original next value + the increase in heads
     * (3)</p>
     *
     * @param growthStrategy the strategy for growing the list heads (the
     *                       default is double.
     * @param listHead       the listHead that is forcing us to grow
     */
    protected void growHeads( GrowthStrategy growthStrategy,
                              int listHead )
    {
        int newHeadSize = growthStrategy.growthRequest( maxHead, listHead );
        //must be able to grow
        if( newHeadSize < listHead )
        {
            throw new ArrayGrowthException( this.getClass(), maxHead,
                                            listHead, Types.Int );
        }
        maxHead = newHeadSize;
    }

    public int insert( int listHead, int value )
    {
        int entry = getNextEntry( listHead );
        heads[ entry ] = value;
        return entry;
    }


    //update the free list
    public void removeEntryFromList( int listHead, int entry )
    {

    }

    /**
     * For the list signified by <i>listHead</i>, return the next entry. If
     * the previous entry is Const.NoEntry, will return the head entry,
     * otherwise return the next of the previous entry.
     *
     * @param listHead  the listHead of the list
     * @param prevEntry the previous entry (Const.NO_ENTRY) if none
     * @return the next entry for the listHead
     */
    public int getNextEntryForList( int listHead, int prevEntry )
    {
        if( prevEntry == Const.NO_ENTRY )
        {
            return heads[ listHead ];
        }
        return nexts[ prevEntry ];
    }

    public int getSize()
    {
        return size;
    }
}
