package collections.list;

import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;

import java.util.Arrays;


/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p>A collection of singly linked int heads, together in two arrays
 * structure. The first item of the list will be inserted into the
 * index of [head], while following items will be inserted after all
 * the heads are used up. Therefore when inserting items into this list,
 * ensure that the items that you insert to are compact (mostly sequential
 * starting from 0).
 * <p/>
 * <p/>
 * <p>
 * An example of usage of this list will be a Hash collection using a modulus
 * function. If using a HashSetInt where there are 8 'buckets', then doing
 * a modulus of 8 will result in the items being in lists 0-7. This structure
 * is used for this purpose by the Hash collections in Jentry.
 * </p>
 * <p/>
 * <p/>
 * Representation - 2 heads
 * <pre>
 *   heads nexts
 * 0 [9]   [2 ]
 * 1 [80]  [4]
 * 2 [8]   [3]
 * 3 [17 ] [-1]
 * 4 [45]   [-1]
 * </pre>
 * <p/>
 * Is equivalent to one list with values 9,8,17, and one with values 80,45.
 */
public class MultiListInt
{
    /** Strategy for growing the MultiList, see {@link GrowthStrategy } */
    private static GrowthStrategy growthStrategy = GrowthStrategy.doubleGrowth;
    /** Factory for creating new int[] arrays */
    private static ArrayFactoryInt intFactory = ArrayFactoryInt
            .defaultintProvider;

    protected int[] heads;
    protected int[] nexts;
    protected int freeListPtr = Const.NO_ENTRY;
    protected int nextUnusedEntry;
    protected int maxHead = -1;
    protected int size;

    /**
     * Constructor
     *
     * @param initialListSize the initial size of heads
     * @param totalEntrySize  the expected size of total entries (assume a
     *                        hashing function that will create linked lists
     *                        of the same size).
     */
    public MultiListInt( int initialListSize, int totalEntrySize )
    {
        this.heads = intFactory.alloc( initialListSize, Const.NO_ENTRY );
        this.nexts = intFactory.alloc( totalEntrySize - initialListSize,
                                       Const.NO_ENTRY );
        maxHead = initialListSize - 1;
        nextUnusedEntry = initialListSize;
    }

    /**
     * <p>
     * Prepend the value at the beginning of the linked list, so that
     * <i>val</i> is in the <i>listHead</i> index of the heads list. Update
     * the next/head so that the old occupant (if any) is not the second
     * item in the list. Ensure that the second item keeps its same next
     * pointer.
     * </p>
     * <p/>
     * For Example, in a MultiList with 2 lists
     * <pre>
     * Start
     * idx      head        next
     *  0        8           2
     *  1       -1          -1
     *  2       10          -1
     *  3       -1          -1
     *
     *  Insert 12 into list 0.
     *
     *  1. Move 8 to entry 3 and update the next
     *  idx      head        next
     *  0        8           2
     *  1       -1          -1
     *  2       10          -1
     *  3        8           2
     *
     *  2. Insert 12 and update the next
     *  idx     head        next
     *  0       12           3
     *  1       -1          -1
     *  2       10          -1
     *  3        8           2
     * </pre>
     * <p/>
     * <p>
     * Note the list for listHead 0 initially was 8,10.
     * The new list is 12,8,10.
     * </p>
     *
     * @param listHead the head of the list
     * @return the entry the next free entry
     */
    protected int insert( int listHead, int val )
    {
        if( listHead > maxHead ) //grow check
        {
            growHeads( GrowthStrategy.doubleGrowth, listHead );
        }
        if( heads[ listHead ] == Const.NO_ENTRY )
        {
            heads[ listHead ] = val;
            size++;
            return listHead;
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
            nexts = intFactory.ensureArrayCapacity( nexts,
                                                    nextUnusedEntry,
                                                    Const.NO_ENTRY,
                                                    growthStrategy );
        }
        //Prepend the entry to the linked list
        if( heads.length <= entry ) growHeads( GrowthStrategy.doubleGrowth,
                                               entry +1);
        heads[ entry ] = heads[ listHead ];
        nexts[ entry ] = nexts[ listHead ];
        nexts[ listHead ] = entry;
        heads[ listHead ] = val;
        size++;
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
     * @param minSize        the minSize that is forcing us to grow
     */
    protected void growHeads( GrowthStrategy growthStrategy,
                              int minSize )
    {
        heads = intFactory.grow( heads, minSize,
                                 Const.NO_ENTRY,
                                 growthStrategy );
    }


    public boolean remove( int listHead, int value )
    {

        int entry = listHead;
        int prev = Const.NO_ENTRY;
        while (heads[entry]!=value )
        {
            if (nexts[entry]==Const.NO_ENTRY) return false;

            prev = entry;
            entry = nexts[entry];

        }
        if (prev==Const.NO_ENTRY) //removing first item
        {
            int next = nexts[entry];
            if (next!=Const.NO_ENTRY) //we have another item after this, move this to head
            {
                heads[entry] = heads[next];
                heads[next] = Const.NO_ENTRY;
                int nextOfNext = nexts[next];
                //now that we have new head, we update next if necessary
                if (nextOfNext!=Const.NO_ENTRY)
                {
                    nexts[entry] = nextOfNext;
                    updateFreePointer(next);
                }
                else
                {
                    nexts[entry] = Const.NO_ENTRY;
                }
                nexts[next] = Const.NO_ENTRY;
            }
            else //just head entry, easy clean up
            {
                heads[entry] = Const.NO_ENTRY;
            }
        }//prev is a valid entry, if nexts is valid, we switch the next of the prev to be new item
        else if (nexts[entry]!=Const.NO_ENTRY)
        {
            nexts[prev] = nexts[entry];
            heads[entry] = Const.NO_ENTRY;
            nexts[entry] = Const.NO_ENTRY;
        }
        else //easy case, no clean up, no next
        {
            heads[entry] = Const.NO_ENTRY;
        }
        updateFreePointer(entry);
        size--;
        return true;
    }

    private void updateFreePointer(int entry)
    {
        if (entry > maxHead)
        {
            if (freeListPtr==Const.NO_ENTRY)
            {
                freeListPtr = entry;
            }
            else //creates a linked list using un-used nexts nodes
            {
                nexts[entry] = freeListPtr;
                freeListPtr = entry;
            }
        }
    }


    /**
     * <p>>Copies an int[] representation of one list, starting from
     * the initial value at heads[<i>listHead</i>], to the targetArray.
     * If the target array is not large enough, will grow the array using
     * a {@link GrowthStrategy#doubleGrowth} strategy.
     * </p
     *
     * @param listHead
     * @param targetArray
     * @return
     */
    public int[] returnList( int listHead, int[] targetArray, boolean flagEnd )
    {
        if( targetArray == null ) targetArray = intFactory.alloc( size /
                                                                  maxHead );
        int i = 0;
        int prevEntry = Const.NO_ENTRY;
        while( heads[ listHead ] != Const.NO_ENTRY )
        {
            targetArray = intFactory.
                    ensureArrayCapacity( targetArray,
                                         i,
                                         Const.NO_ENTRY,
                                         GrowthStrategy.doubleGrowth );

            int entry = getNextEntryForList( listHead, prevEntry );
            targetArray[ i++ ] = heads[ entry ];
            prevEntry = entry;
        }
        if( flagEnd && targetArray.length >= i )
        {
            targetArray[ i ] = Const.NO_ENTRY;
        }
        return targetArray;
    }

    /**
     * Given an entry, return the value. To cycle through the items in a
     * particular list, use this method accompanied by
     * {@link #getNextEntryForList(int, int)}  }
     *
     * @param entry the entry
     * @return the value for the entry
     */
    public int getValue( int entry )
    {
        return heads[ entry ];
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
            return listHead;
        }
        return nexts[ prevEntry ];
    }

    public int getSize()
    {
        return size;
    }
}
