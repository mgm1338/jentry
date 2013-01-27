package collections.util;

import collections.Collection;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;

import java.util.Arrays;


/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p>A collection of singly linked lists of its, stored together in two arrays.
 * The first item of the list will be inserted into the
 * index of [head], while following items will be inserted after all
 * the heads are used up. See #{@link #insert(int, int)} for more details.
 * <p/>
 * <p>When items are removed, a freelist will store the removed items
 * (using the same arrays that are used for the inserted values), so that the
 * structure will avoid allocating unless it is completely necessary.
 * See {@link #remove(int, int)} for more information.</p>
 * <p/>
 * <p>
 * This is a utility collection because it does not follow the standard
 * conventions of a Jentry collection (there are no entries, or handles upon
 * insertion), but it instead of a building block
 * of many of the collections, including all of the HashSet and HashMap
 * collections.
 * </p>
 * <p/>
 * <p/>
 * Example Representation - 2 heads
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
public class MultiLinkedListInt implements Collection
{
    /** Strategy for growing the MultiList, see {@link GrowthStrategy } */
    private final GrowthStrategy growthStrategy;
    /** Factory for creating new int[] arrays */
    private final ArrayFactoryInt intFactory;

    /** The heads array, will store the actual values */
    protected int[] heads;
    /** Pointer to the next value, or Const.NO_ENTRY if none exists */
    protected int[] nexts;
    /** Pointer to the start of the free list */
    protected int freeListPtr = Const.NO_ENTRY;
    /** The  next (non-head) item to store a value. */
    protected int nextUnusedIdx;
    /** Current max-head item */
    protected int maxHead = -1;
    /** Number of elements inserted into the sturcture */
    protected int size;

    /**
     * Constructor
     *
     * @param initialNumLists the initial number of singly linked list this structure will allocate
     * @param totalSize        the estimated size of all of the lists
     */
    public MultiLinkedListInt( int initialNumLists, int totalSize )
    {
        this( initialNumLists, totalSize,
              GrowthStrategy.doubleGrowth, ArrayFactoryInt.defaultIntProvider );
    }

    /**
     * Full Constructor
     *
     * @param initialListSize the initial number of singly linked list this structure will allocate
     * @param totalSize        the estimated size of all of the lists
     * @param growthStrategy  the growth strategy when growing the set of lists
     * @param intFactory      the factory that will provide the int[] arrays
     */
    public MultiLinkedListInt( int initialListSize, int totalSize,
                               GrowthStrategy growthStrategy,
                               ArrayFactoryInt intFactory )
    {
        this.growthStrategy = growthStrategy;
        this.intFactory = intFactory;
        this.heads = intFactory.alloc( totalSize, Const.NO_ENTRY );
        this.nexts = intFactory.alloc( totalSize, Const.NO_ENTRY );
        maxHead = initialListSize - 1;
        nextUnusedIdx = initialListSize;
    }


    /**
     * <p>
     * Prepend the value at the beginning of the linked list, so that
     * <b>val</b> is in the <b>listHead</b> index of the heads list. Update
     * the next/head so that the old occupant (if any) is now the second
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
     * @param val      the value we are inserting into the list
     */
    public void insert( int listHead, int val )
    {
        if( listHead > maxHead ) //growth check
        {
            growHeads( GrowthStrategy.doubleGrowth, listHead );
        }
        //first item, takes head spot
        if( heads[ listHead ] == Const.NO_ENTRY )
        {
            heads[ listHead ] = val;
            size++;
            return;
        }

        //index to insert value
        int idx;
        if( freeListPtr != Const.NO_ENTRY ) //try free list
        {
            idx = freeListPtr;
            freeListPtr = ( nexts[ idx ] == Const.NO_ENTRY ) ? Const.NO_ENTRY
                                                             : nexts[ idx ];
        }
        else
        {
            idx = nextUnusedIdx++;
            nexts = intFactory.ensureArrayCapacity( nexts,
                                                    nextUnusedIdx,
                                                    Const.NO_ENTRY,
                                                    growthStrategy );
        }
        //Prepend the entry to the linked list
        if( heads.length <= idx )
        {
            heads = intFactory.grow( heads, idx + 1, Const.NO_ENTRY,
                                     growthStrategy );
            nexts = intFactory.grow( nexts, idx + 1, Const.NO_ENTRY,
                                     growthStrategy );
        }
        heads[ idx ] = heads[ listHead ];
        nexts[ idx ] = nexts[ listHead ];
        nexts[ listHead ] = idx;
        heads[ listHead ] = val;
        size++;
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
     * @param newMaxHead     the minSize that is forcing us to grow
     */
    public void growHeads( GrowthStrategy growthStrategy,
                           int newMaxHead )
    {
        int shift = newMaxHead - maxHead;
        int minNewSize = size + newMaxHead + 1; //may overgrow if heads full,
        // but necessary for size 0
        int oldLen = heads.length;
        int copyLen = oldLen - maxHead - 1;
        heads = intFactory.ensureArrayCapacity( heads, minNewSize,
                                                Const.NO_ENTRY,
                                                growthStrategy );
        nexts = intFactory.ensureArrayCapacity( nexts, minNewSize,
                                                Const.NO_ENTRY,
                                                growthStrategy );
        for( int i = 0; i < oldLen; i++ )
        {
            if( nexts[ i ] != Const.NO_ENTRY )
            {
                nexts[ i ] += shift;
            }
        }
        System.arraycopy( heads, maxHead + 1, heads, newMaxHead + 1, copyLen );
        System.arraycopy( nexts, maxHead + 1, nexts, newMaxHead + 1, copyLen );

        Arrays.fill( heads, maxHead + 1, newMaxHead, Const.NO_ENTRY );
        maxHead = newMaxHead;
    }


    /**
     * Remove an item the structure. If the item exists in the listHead
     * specified, then it will remove it and return true, otherwise
     * return false.
     *
     * @param listHead the linked list to check
     * @param value    the value to remove
     * @return true if removed, false otherwise
     */
    public boolean remove( int listHead, int value )
    {
        //when searching, keep the previous entry, to update its next ptr
        int testIdx = listHead;
        int prev = Const.NO_ENTRY;
        while( heads[ testIdx ] != value )
        {
            if( nexts[ testIdx ] == Const.NO_ENTRY ) return false;

            prev = testIdx;
            testIdx = nexts[ testIdx ];

        }
        if( prev == Const.NO_ENTRY ) //removing first item
        {
            int next = ( nexts.length > testIdx ) ? nexts[ testIdx ] : Const.NO_ENTRY;
            if( next != Const.NO_ENTRY ) //more items in list, move to head
            {
                heads[ testIdx ] = heads[ next ];
                heads[ next ] = Const.NO_ENTRY;
                int nextOfNext = nexts[ next ];
                //now that we have new head, we update next if necessary
                if( nextOfNext != Const.NO_ENTRY )
                {
                    nexts[ testIdx ] = nextOfNext;
                }
                else
                {
                    nexts[ testIdx ] = Const.NO_ENTRY;
                }
                updateFreePointer( next );
            }
            else //just head entry, easy clean up, no next
            {
                heads[ testIdx ] = Const.NO_ENTRY;
            }
        }
        //prev is a valid entry, however we are not at end of list
        else if( nexts[ testIdx ] != Const.NO_ENTRY )
        {
            nexts[ prev ] = nexts[ testIdx ];
            heads[ testIdx ] = Const.NO_ENTRY;
            nexts[ testIdx ] = Const.NO_ENTRY;
            updateFreePointer( testIdx );
        }
        else //easy case,end of list
        {
            heads[ testIdx ] = Const.NO_ENTRY;
            nexts[ prev ] = Const.NO_ENTRY;
            updateFreePointer( testIdx );
        }
        size--;
        return true;
    }


    /**
     * Utility method to handle the linked list of 'free' items.
     *
     * @param idx the removed index
     */
    private void updateFreePointer( int idx )
    {
        //must be past the maxHead, the head items are reserved
        if( idx > maxHead )
        {
            if( freeListPtr == Const.NO_ENTRY ) //start list
            {
                freeListPtr = idx;
            }
            else //creates a linked list using the un-used nexts
            {
                nexts[ freeListPtr ] = Const.NO_ENTRY;
                nexts[ idx ] = freeListPtr;
                freeListPtr = idx;
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
     * @param listHead    the head of the list
     * @param targetArray the target array, if null will allocate, if too small
     *                    will grow
     * @return the int[] representation of the list
     */
    public int[] getList( int listHead, int[] targetArray, boolean flagEnd )
    {
        if( listHead > maxHead )
        {
            throw new IllegalArgumentException( "No list for " + listHead );
        }
        if( targetArray == null )    //allocate
        {
            targetArray = intFactory.alloc( size /
                                            maxHead );
        }

        int i = 0;
        int prevIdx = Const.NO_ENTRY;
        int idx = getNextIdxForList( listHead, prevIdx );
        while( idx != Const.NO_ENTRY )
        {
            targetArray = intFactory.    //check size
                    ensureArrayCapacity( targetArray,
                                         i + 1,
                                         Const.NO_ENTRY,
                                         GrowthStrategy.doubleGrowth );

            targetArray[ i++ ] = heads[ idx ];
            prevIdx = idx;
            idx = getNextIdxForList( listHead, prevIdx );
        }
        if( flagEnd && targetArray.length >= i )
        {
            targetArray[ i ] = Const.NO_ENTRY;
        }
        return targetArray;
    }

    /**
     * Using the previous index, get the next index in the heads array
     * for a value. This, used in conjunction with {@link #getHead(int)}
     * is used iterate the items in a list. The first prevIdx used
     * should be Const.NO_ENTRY, and the iteration is over when
     * it is passed back.
     *
     * @param listHead the listHead of the list
     * @param prevIdx  the previous index into the list
     * @return the next index of a valid item, or Const.NO_ENTRY
     */
    public int getNextIdxForList( int listHead, int prevIdx )
    {
        if( prevIdx == Const.NO_ENTRY )
        {
            return listHead;
        }
        if( prevIdx < nexts.length )
        {
            return nexts[ prevIdx ];
        }
        return Const.NO_ENTRY;
    }

    /**
     * Get the value for the index passed. This, used with {@link #getNextIdxForList(int, int)} is
     * used to iterate a particular list.
     *
     * @param idx the index
     * @return the value in the heads array
     */
    public int getHead( int idx )
    {
        return heads[ idx ];
    }

    /**
     * Returns the size of the set of lists (this is the size of all the items in every list)
     *
     * @return the total.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Is the structure empty.
     *
     * @return if the entire set of lists is empty
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Clear all of the lists in the set.
     */
    public void clear()
    {
        Arrays.fill( heads, Const.NO_ENTRY );
        Arrays.fill( nexts, Const.NO_ENTRY );
        freeListPtr = Const.NO_ENTRY;
        size = 0;
        nextUnusedIdx = maxHead + 1;
    }

    /**
     * Create a 'deep copy' of this set of lists and return it. This deep copy will copy
     * all the attributes, instead of a shallow copy, which will simply return a reference to this.
     *
     * @return a deep copy of this object
     */
    public MultiLinkedListInt getDeepCopy()
    {
        MultiLinkedListInt target = new MultiLinkedListInt( maxHead, size, growthStrategy, intFactory );
        //should be close if this list is mostly compact
        int headLen = heads.length;
        int nextsLen = nexts.length;
        target.heads = intFactory.ensureArrayCapacity( target.heads, headLen, Const.NO_ENTRY,
                                                       GrowthStrategy.toExactSize );
        target.nexts = intFactory.ensureArrayCapacity( target.nexts, nextsLen, Const.NO_ENTRY,
                                                       GrowthStrategy.toExactSize );
        System.arraycopy( heads, 0, target.heads, 0, headLen );
        System.arraycopy( nexts, 0, target.nexts, 0, nextsLen );

        target.maxHead = maxHead;
        target.size = size;
        target.nextUnusedIdx = nextUnusedIdx;
        target.freeListPtr = freeListPtr;
        return target;
    }


}
