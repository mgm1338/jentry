package collections.util;

import core.Const;
import core.array.GrowthStrategy;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class TestMultiSLList
{


    MultiLinkedListInt lists;
    MultiLinkedListInt dataLoadedLists;

    /**
     * Loaded test data:
     * <pre>
     * idx  heads   nexts
     * 0    8        3
     * 1    15       4
     * 2    6       -1
     * 3    9       -1
     * 4    16       5
     * 5    17      -1
     *
     * listHead     contents
     * 0          = 8->9
     * 2          = 6
     * 1          = 15->16-17
     * </pre>
     */
    @Before
    public void setup ()
    {
        lists = new MultiLinkedListInt(8, 16);
        //we know the set of lists/data, this is example of what constructor
        // parameters mean, note that in this case, 0,1,
        // 2 must be the head values
        dataLoadedLists = new MultiLinkedListInt(3, 6);
        TestCase.assertEquals (dataLoadedLists.getSize (), 0);
        dataLoadedLists.insert (0, 9);
        dataLoadedLists.insert (1, 17);
        dataLoadedLists.insert (2, 6);
        dataLoadedLists.insert (0, 8);
        dataLoadedLists.insert (1, 16);
        dataLoadedLists.insert (1, 15);
        TestCase.assertEquals (dataLoadedLists.getSize (), 6);


        assertListContents (dataLoadedLists, 0, 9, 8);
        assertListContents (dataLoadedLists, 1, 17, 16, 15);
        assertListContents (dataLoadedLists, 2, 6);
    }

    /**
     * Assert the contents of the list, not the order. This will
     * do basic assertions on the items in the list. More specific ordering
     * tests will follow around boundary cases.
     *
     * @param list   list to test
     * @param head   head entry
     * @param values expected values (can be passed null, but will be converted
     *               to size 0)
     */
    protected void assertListContents (MultiLinkedListInt list,
                                       int head, int... values)
    {
        //collect list
        int ptr = head;
        ArrayList contents = new ArrayList ();
        while (ptr != -1 && list.heads[ptr] != Const.NO_ENTRY)
        {
            contents.add (list.heads[ptr]);
            ptr = (list.nexts.length > ptr) ? list.nexts[ptr] : -1;

        }

        if (values == null) values = new int[0];
        if (contents.size () != values.length)
        {
            TestCase.fail ("Do not have the same number of items in the list " +
                                   "for head value of [" + head + "], " +
                                   "we expected [" + values.length + "] however there " +
                                   "were [" + contents.size () + "]");
        }

        int len = values.length;
        for (int i = 0; i < len; i++)
        {

            if (!contents.contains (values[i]))
            {

                TestCase.fail ("For head value [" + head + "] we expected to " +
                                       "find value [" + values[i] + "] but did not " +
                                       "find it");
            }
        }
    }

    /**
     * Testing loading the data with the initial contents in {@link #setup }
     * This will do ordering tests, for our assertings, that we actual
     * prepend
     */
    @Test
    public void testDataLoad ()
    {
        //list 0, inserted 9, then 8, we assume that it will actually be
        //8, follow next to 9
        TestCase.assertEquals (dataLoadedLists.heads[0], 8);
        TestCase.assertEquals (dataLoadedLists.heads[dataLoadedLists.nexts[0]], 9);

        //one item
        TestCase.assertEquals (dataLoadedLists.heads[2], 6);

        //3
        TestCase.assertEquals (dataLoadedLists.heads[1], 15);
        TestCase.assertEquals (dataLoadedLists.heads[dataLoadedLists.nexts[1]], 16);
        //very verbose, formal next following
        TestCase.assertEquals (dataLoadedLists.heads[dataLoadedLists.nexts
                [dataLoadedLists.nexts[1]]], 17);

        //since we have all three heads fill, and indexes after heads fill
        //iteratively, heads should be completely full (we started with
        //size 6, shouldnt have had to grow)
        TestCase.assertEquals (dataLoadedLists.heads.length, 6);

    }


    //Remove Tests

    @Test
    public void removeFromSingleList ()
    {
        TestCase.assertTrue (dataLoadedLists.getSize () == 6);
        dataLoadedLists.remove (2, 6);
        //free list ptr cannot be head ptr
        TestCase.assertTrue (dataLoadedLists.freeListPtr == Const.NO_ENTRY);
        assertListContents (dataLoadedLists, 2, null);
        TestCase.assertTrue (dataLoadedLists.getSize () == 5);


    }


    @Test
    public void removeFirstEntryForList ()
    {
        TestCase.assertTrue (dataLoadedLists.getSize () == 6);
        dataLoadedLists.remove (0, 8);
        //first item list with 2 items, will take spot maxHeads+1
        TestCase.assertTrue (dataLoadedLists.freeListPtr == 3);
        assertListContents (dataLoadedLists, 0, 9);
        TestCase.assertTrue (dataLoadedLists.getSize () == 5);
    }

    @Test
    public void removeLastEntryForList ()
    {
        TestCase.assertTrue (dataLoadedLists.getSize () == 6);
        dataLoadedLists.remove (0, 9);
        assertListContents (dataLoadedLists, 0, 8);
        TestCase.assertTrue (dataLoadedLists.getSize () == 5);
    }

    @Test
    public void removeMiddleFromList ()
    {
        TestCase.assertTrue (dataLoadedLists.getSize () == 6);
        dataLoadedLists.remove (1, 16);
        assertListContents (dataLoadedLists, 1, 15, 17);
        TestCase.assertTrue (dataLoadedLists.getSize () == 5);
    }

    @Test
    public void removeFromWrongHead ()
    {
        TestCase.assertFalse (dataLoadedLists.remove (0, 16));
        TestCase.assertEquals (dataLoadedLists.size, 6);
    }

    @Test
    public void removeNonexistentValue ()
    {
        TestCase.assertFalse (dataLoadedLists.remove (0, 0));
        TestCase.assertEquals (dataLoadedLists.size, 6);
    }

    /**
     * Missing this if in case, will need more expansive list, the test
     * is to prove that when removing first item in a long chain, the
     * next pointers remain valid.
     */
    @Test
    public void removeMiddleFromLongList ()
    {
        //data load
        MultiLinkedListInt longListChain = new MultiLinkedListInt(2, 16);
        for (int i = 0; i < 8; i++)
        {
            longListChain.insert (0, i);
        }
        assertListContents (longListChain, 0, 0, 1, 2, 3, 4, 5, 6, 7);
        assertListContents (longListChain, 1, null);

        longListChain.remove (0, 7);//due to the nature of the lists, prepending
        //the first item is that last one inserted
        assertListContents (longListChain, 0, 0, 1, 2, 3, 4, 5, 6);
        assertListContents (longListChain, 1, null);
    }

    //Free list
    @Test
    public void oneItemOnFreeListPtr ()
    {
        TestCase.assertEquals (dataLoadedLists.freeListPtr, Const.NO_ENTRY);
        removeLastEntryForList ();
        //removed first item after heads (0,1,2)
        TestCase.assertEquals (dataLoadedLists.freeListPtr, 3);
        dataLoadedLists.insert (2, 200);

        assertListContents (dataLoadedLists, 0, 8);
        assertListContents (dataLoadedLists, 1, 17, 16, 15);
        assertListContents (dataLoadedLists, 2, 6, 200);
    }

    @Test
    public void FullListOnFreeListPtr ()
    {

        //remove list 1, create a 3 list free list
        assertListContents (dataLoadedLists, 0, 9, 8);
        assertListContents (dataLoadedLists, 1, 17, 16, 15);
        assertListContents (dataLoadedLists, 2, 6);

        dataLoadedLists.remove (1, 15);
        dataLoadedLists.remove (1, 16);
        dataLoadedLists.remove (1, 17);

        //asser the the free list has 2 items, 4 and 5, with remove
        //order, first should be 4 (then 5)
        assertListContents (dataLoadedLists, 1, null);
        TestCase.assertEquals (dataLoadedLists.freeListPtr, 4);
        TestCase.assertTrue (dataLoadedLists.nexts[4] != Const.NO_ENTRY);
        TestCase.assertTrue (dataLoadedLists.nexts[4] == 5);
        TestCase.assertTrue (dataLoadedLists.nexts[5] == Const.NO_ENTRY);

        //should take first spot in free list
        dataLoadedLists.insert (2, 200);

        //one item, 5
        TestCase.assertTrue (dataLoadedLists.freeListPtr == 5);
        TestCase.assertTrue (dataLoadedLists.nexts[5] == Const.NO_ENTRY);
        //new item on list 2, no item
        TestCase.assertTrue (dataLoadedLists.nexts[4] == Const.NO_ENTRY);
        assertListContents (dataLoadedLists, 0, 9, 8);
        assertListContents (dataLoadedLists, 1, null);
        assertListContents (dataLoadedLists, 2, 6, 200);

        //should take first spot in free list
        dataLoadedLists.insert (2, 201);
        TestCase.assertEquals (dataLoadedLists.freeListPtr, Const.NO_ENTRY);

        assertListContents (dataLoadedLists, 0, 9, 8);
        assertListContents (dataLoadedLists, 1, null);
        assertListContents (dataLoadedLists, 2, 6, 200, 201);

        //head spot, just assert it doesnt break
        dataLoadedLists.insert (1, 1);

        assertListContents (dataLoadedLists, 0, 9, 8);
        assertListContents (dataLoadedLists, 1, 1);
        assertListContents (dataLoadedLists, 2, 6, 200, 201);
    }

    @Test
    public void growthTest ()
    {
        for (int i = 0; i < 1000; i++)
        {
            dataLoadedLists.insert (0, i);
        }
        TestCase.assertEquals (1006, dataLoadedLists.size);

        for (int i = 0; i < 1000; i++)
        {
            TestCase.assertTrue (dataLoadedLists.remove (0, i));
        }
    }

    @Test
    public void getListWithNullArray ()
    {
        int[] list = dataLoadedLists.getList (0, null, false);
        TestCase.assertEquals (list[0], 8);
        TestCase.assertEquals (list[1], 9);

        list = dataLoadedLists.getList (1, list, false);
        TestCase.assertEquals (list[0], 15);
        TestCase.assertEquals (list[1], 16);
        TestCase.assertEquals (list[2], 17);
    }

    @Test
    public void getListNoBucket ()
    {
        try
        {
            int[] list = dataLoadedLists.getList (4, null, false);
            TestCase.fail ();
        }
        catch (Exception e)
        {

        }
    }

    @Test
    public void flagEndOfArray()
    {
        int[] list = dataLoadedLists.getList (0, null, true);
        TestCase.assertEquals (list[0], 8);
        TestCase.assertEquals (list[1], 9);
        TestCase.assertEquals (list[2], Const.NO_ENTRY);
    }



    @Test
    public void growHeadsTest()
    {
        dataLoadedLists.growHeads (GrowthStrategy.doubleGrowth, 6);

        assertListContents (dataLoadedLists, 0, 9, 8);
        assertListContents (dataLoadedLists, 1, 17, 16, 15);
        assertListContents (dataLoadedLists, 2, 6);

        TestCase.assertTrue (dataLoadedLists.maxHead==6);
        //must be old size + size (this can overgrow, but necessary for 0)
        TestCase.assertTrue (dataLoadedLists.heads.length>=12);
        TestCase.assertTrue (dataLoadedLists.nexts.length>=12);
    }

    @Test
    public void growNewList()
    {
        MultiLinkedListInt list = new MultiLinkedListInt(1, 1);
        list.growHeads (GrowthStrategy.doubleGrowth, 8);
        TestCase.assertTrue (list.heads.length>=8);
        TestCase.assertTrue (list.nexts.length>=8);

    }

    @Test
    public void growHeadsByInsert()
    {
        dataLoadedLists.insert (6, 0);

        assertListContents (dataLoadedLists, 0, 9, 8);
        assertListContents (dataLoadedLists, 1, 17, 16, 15);
        assertListContents (dataLoadedLists, 2, 6);
        assertListContents (dataLoadedLists, 6, 0);


        TestCase.assertTrue (dataLoadedLists.maxHead==6);
        //must be old size + size (this can overgrow, but necessary for 0)
        TestCase.assertTrue (dataLoadedLists.heads.length>=12);
        TestCase.assertTrue (dataLoadedLists.nexts.length>=12);
    }

}
