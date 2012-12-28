package collections.util;

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
 * conventions of a Jentry collection, but it instead of a building block
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
public class MultiListInt
{
    /**
     * Strategy for growing the MultiList, see {@link GrowthStrategy }
     */
    private final GrowthStrategy growthStrategy;
    /**
     * Factory for creating new int[] arrays
     */
    private final ArrayFactoryInt intFactory;

    /**
     * The heads array, will store the actual values
     */
    protected int[] heads;
    /**
     * Pointer to the next value, or Const.NO_ENTRY if none exists
     */
    protected int[] nexts;
    /**
     * Pointer to the start of the free list
     */
    protected int freeListPtr = Const.NO_ENTRY;
    /**
     * The  next (non-head) item to store a value.
     */
    protected int nextUnusedEntry;
    /**
     * Current max-head item
     */
    protected int maxHead = -1;
    /**
     * Number of elements inserted into the sturcture
     */
    protected int size;


    public MultiListInt (int initialListSize, int totalEntrySize)
    {
        this (initialListSize, totalEntrySize,
              GrowthStrategy.doubleGrowth, ArrayFactoryInt.defaultintProvider);
    }

    public MultiListInt (int initialListSize, int totalEntrySize,
                         GrowthStrategy growthStrategy,
                         ArrayFactoryInt intFactory)
    {
        this.growthStrategy = growthStrategy;
        this.intFactory = intFactory;
        this.heads = intFactory.alloc (initialListSize, Const.NO_ENTRY);
        this.nexts = intFactory.alloc (totalEntrySize - initialListSize,
                                       Const.NO_ENTRY);
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
    public void insert (int listHead, int val)
    {
        if (listHead > maxHead) //growth check
        {
            growHeads (GrowthStrategy.doubleGrowth, listHead);
        }
        //first item, takes head spot
        if (heads[listHead] == Const.NO_ENTRY)
        {
            heads[listHead] = val;
            size++;
            return;
        }

        //entry is idx to insert value
        int entry;
        if (freeListPtr != Const.NO_ENTRY) //try free list
        {
            entry = freeListPtr;
            freeListPtr = (nexts[entry] == Const.NO_ENTRY) ? Const.NO_ENTRY
                                                           : nexts[entry];
        }
        else
        {
            entry = nextUnusedEntry++;
            nexts = intFactory.ensureArrayCapacity (nexts,
                                                    nextUnusedEntry,
                                                    Const.NO_ENTRY,
                                                    growthStrategy);
        }
        //Prepend the entry to the linked list
        if (heads.length <= entry)
        {
            heads = intFactory.grow (heads, entry + 1, Const.NO_ENTRY,
                                     growthStrategy);
            nexts = intFactory.grow (nexts, entry + 1, Const.NO_ENTRY,
                                     growthStrategy);
        }
        heads[entry] = heads[listHead];
        nexts[entry] = nexts[listHead];
        nexts[listHead] = entry;
        heads[listHead] = val;
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
    public void growHeads (GrowthStrategy growthStrategy,
                              int newMaxHead)
    {
        int shift = newMaxHead - maxHead;
        int minNewSize = size + newMaxHead + 1; //may overgrow if heads full,
        // but necessary for size 0
        int oldLen = heads.length;
        int copyLen = oldLen - maxHead - 1;
        heads = intFactory.ensureArrayCapacity (heads, minNewSize,
                                                Const.NO_ENTRY,
                                                growthStrategy);
        nexts = intFactory.ensureArrayCapacity (nexts, minNewSize,
                                                Const.NO_ENTRY,
                                                growthStrategy);
        for (int i = 0; i < oldLen; i++)
        {
            if (nexts[i] != Const.NO_ENTRY)
            {
                nexts[i] += shift;
            }
        }
        System.arraycopy (heads, maxHead + 1, heads, newMaxHead + 1, copyLen);
        System.arraycopy (nexts, maxHead + 1, nexts, newMaxHead + 1, copyLen);

        Arrays.fill (heads, maxHead + 1, newMaxHead, Const.NO_ENTRY);
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
    public boolean remove (int listHead, int value)
    {
        //when searching, keep the previous entry, to update its next ptr
        int entry = listHead;
        int prev = Const.NO_ENTRY;
        while (heads[entry] != value)
        {
            if (nexts[entry] == Const.NO_ENTRY) return false;

            prev = entry;
            entry = nexts[entry];

        }
        if (prev == Const.NO_ENTRY) //removing first item
        {
            int next = nexts[entry];
            if (next != Const.NO_ENTRY) //more items in list, move to head
            {
                heads[entry] = heads[next];
                heads[next] = Const.NO_ENTRY;
                int nextOfNext = nexts[next];
                //now that we have new head, we update next if necessary
                if (nextOfNext != Const.NO_ENTRY)
                {
                    nexts[entry] = nextOfNext;
                }
                else
                {
                    nexts[entry] = Const.NO_ENTRY;
                }
                updateFreePointer (next);
            }
            else //just head entry, easy clean up, no next
            {
                heads[entry] = Const.NO_ENTRY;
            }
        }
        //prev is a valid entry, however we are not at end of list
        else if (nexts[entry] != Const.NO_ENTRY)
        {
            nexts[prev] = nexts[entry];
            heads[entry] = Const.NO_ENTRY;
            nexts[entry] = Const.NO_ENTRY;
            updateFreePointer (entry);
        }
        else //easy case,end of list
        {
            heads[entry] = Const.NO_ENTRY;
            nexts[prev] = Const.NO_ENTRY;
            updateFreePointer (entry);
        }
        size--;
        return true;
    }


    /**
     * Utility method to handle the linked list of 'free' items.
     *
     * @param entry the removed entry
     */
    private void updateFreePointer (int entry)
    {
        //must be past the maxHead, the head items are reserved
        if (entry > maxHead)
        {
            if (freeListPtr == Const.NO_ENTRY) //start list
            {
                freeListPtr = entry;
            }
            else //creates a linked list using the un-used nexts
            {
                nexts[freeListPtr] = Const.NO_ENTRY;
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
     * @param listHead    the head of the list
     * @param targetArray the target array, if null will allocate, if too small
     *                    will grow
     * @return the int[] representation of the list
     */
    public int[] getList (int listHead, int[] targetArray, boolean flagEnd)
    {
        if (listHead > maxHead)
        {
            throw new IllegalArgumentException ("No list for " + listHead);
        }
        if (targetArray == null)    //allocate
        {
            targetArray = intFactory.alloc (size /
                                                    maxHead);
        }

        int i = 0;
        int prevEntry = Const.NO_ENTRY;
        int entry = getNextEntryForList (listHead, prevEntry);
        while (entry != Const.NO_ENTRY)
        {
            targetArray = intFactory.    //check size
                    ensureArrayCapacity (targetArray,
                                         i,
                                         Const.NO_ENTRY,
                                         GrowthStrategy.doubleGrowth);

            targetArray[i++] = heads[entry];
            prevEntry = entry;
            entry = getNextEntryForList (listHead, prevEntry);
        }
        if (flagEnd && targetArray.length >= i)
        {
            targetArray[i] = Const.NO_ENTRY;
        }
        return targetArray;
    }

    /**
     * <p>For the list signified by <i>listHead</i>, return the next entry. If
     * the previous entry is Const.NoEntry, will return the head entry,
     * otherwise return the next of the previous entry.
     * </p>
     * <p/>
     * <p>If using this to iterate an entire list, consider using
     * {@link #getList(int, int[], boolean)}  }</p>
     *
     * @param listHead  the listHead of the list
     * @param prevEntry the previous entry (Const.NO_ENTRY) if none
     * @return the next entry for the listHead
     */
    public int getNextEntryForList (int listHead, int prevEntry)
    {
        if (prevEntry == Const.NO_ENTRY)
        {
            return listHead;
        }
        return nexts[prevEntry];
    }

    public int getSize ()
    {
        return size;
    }
}
