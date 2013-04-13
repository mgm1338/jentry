package collections.heap;

import core.stub.IntValueConverter;
import core.stub.*;
import core.util.comparator.Comparators;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils_KeyTypeName_;

import java.util.Arrays;
import java.util.Random;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class TestBinaryHeap_KeyTypeName_
{

    private static final int TEST_SIZE = 5;
    BinaryHeap_KeyTypeName_ heap;
    boolean template = (this.getClass ().getCanonicalName ().contains ("_"));

    @Before
    public void setup ()
    {
        heap = new BinaryHeap_KeyTypeName_ (8, new Comparators._KeyTypeName_Asc ());
    }

    /**
     * Test multiple inserts will insert in order, starting from 0.
     * Then test first removed is first insertion, remove an item, assert
     * its spot will be taken upon next insertion.
     */
    @Test
    public void testInsertCompact ()
    {
        if (template) return;
        TestCase.assertTrue (heap.isEmpty ());
        TestCase.assertEquals (0, heap.getSize ());

        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (i)));
        }
        TestCase.assertEquals (TEST_SIZE, heap.getSize ());


        heap.remove (3);
        TestCase.assertEquals (3,
                               heap.insert (IntValueConverter._key_FromInt (3)));
        TestCase.assertEquals (TEST_SIZE, heap.getSize ());

    }

    /**
     * Insert items from least to greatest, assert that order will be
     * from greatest to least.
     */
    @Test
    public void testReverseInsertionCorrectOrder ()
    {
        if (template) return;
        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (TEST_SIZE - i)));
        }
        assertInAscOrder (heap);
    }

    /**
     * Insert psuedo-random entries, assert that they will be ordered correctly.
     */
    @Test
    public void testRandomInsertionCorrectOrder ()
    {
        if (template) return;
        Random random = new Random (42);
        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (random.nextInt (16))));
        }
    }

    /**
     * Same as {@link #testReverseInsertionCorrectOrder()}  }, with a growth
     * in the middle through insertion of more than initial capacity.
     */
    @Test
    public void testBackwardsWithDoubleGrowth ()
    {
        if (template) return;
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (24 - i)));
        }
        TestCase.assertEquals (heap.keys.length, 32);
        TestCase.assertEquals (heap.tree.length, 32);
        TestCase.assertEquals (heap.inverse.length, 32);
        TestCase.assertEquals (24, heap.getSize ());
        assertInAscOrder (heap);
    }

    /**
     * Same as {@link #testRandomInsertionCorrectOrder()}  }, with a growth
     * past the initial capacity.
     */
    @Test
    public void testRandomWithDoubleGrowth ()
    {
        if (template) return;
        Random random = new Random (42);
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (random.nextInt (16))));
        }
        TestCase.assertEquals (heap.keys.length, 32);
        TestCase.assertEquals (heap.tree.length, 32);
        TestCase.assertEquals (heap.inverse.length, 32);
        TestCase.assertEquals (24, heap.getSize ());
        assertInAscOrder (heap);
    }

    /**
     * Test of insertion of many of the same value, with one higher.  This
     * higher should end up at the 'bottom' of the tree. We will remove all
     * of the same value, resulting in the last entry being the different value.
     */
    @Test
    public void manyWithSameOneHigher ()
    {
        if (template) return;
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (4)));
        }

        heap.insert (IntValueConverter._key_FromInt (5));
        TestCase.assertEquals (IntValueConverter._key_FromInt (4), heap.peek ());
        TestCase.assertEquals (25, heap.getSize ());
        for (int i = 0; i < 24; i++)
        {
            heap.removeGreatest ();
        }
        TestCase.assertEquals (IntValueConverter._key_FromInt (5), heap.peek ());
    }

    /**
     * Test that with many of the same value, inserting one that is lower
     * than the rest will end up in the peek() position.
     */
    @Test
    public void manyWithSameOneLower ()
    {
        if (template) return;
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (4)));
        }
        heap.insert (IntValueConverter._key_FromInt (3));
        TestCase.assertEquals (IntValueConverter._key_FromInt (3), heap.peek ());
    }


    /**
     * Clear tests. Run many times with different sets to mitigate the risk
     * that left over state will ruin re-use of the heap.
     */
    @Test
    public void clearTests ()
    {
        if (template) return;
        heap.clear (); //clearing empty
        TestCase.assertEquals (0, heap.getSize ());
        TestCase.assertTrue (heap.isEmpty ());

        testRandomInsertionCorrectOrder (); //random insertion
        TestCase.assertEquals (TEST_SIZE, heap.getSize ());
        heap.clear ();
        TestCase.assertEquals (0, heap.getSize ());
        TestCase.assertTrue (heap.isEmpty ());

        //stolen from random growth test
        Random random = new Random (42);
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (random.nextInt (16))));
        }
        TestCase.assertEquals (24, heap.getSize ());
        heap.clear (); //insertion with growth
        TestCase.assertEquals (0, heap.getSize ());
        TestCase.assertTrue (heap.isEmpty ());

        //stolen from backwards growth test
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (24 - i)));
        }
        TestCase.assertEquals (24, heap.getSize ());
        heap.clear ();
        TestCase.assertEquals (0, heap.getSize ());
        TestCase.assertTrue (heap.isEmpty ());


    }


    /**
     * Removing each item from the head position. Ensures that empty
     * and size reflect correctly after removing each item.
     */
    @Test
    public void fullRemoval ()
    {
        if (template) return;
        testRandomWithDoubleGrowth ();
        while (!heap.isEmpty ())
        {
            heap.removeGreatest ();
            assertInAscOrder (heap);
        }
        TestCase.assertTrue (heap.isEmpty ());
        TestCase.assertEquals (0, heap.getSize ());
    }

    /**
     * Utility method that will test the ordering of the items in the heap.
     * Will collect each key and use the jdk {@link Arrays#sort }
     * against the removed order that we got by removing each item
     * from the head.
     *
     * @param heap heap that we will ensure has correct ordering of elements.
     */
    protected void assertInAscOrder (BinaryHeap_KeyTypeName_ heap)
    {
        if (template) return;
        _key_[] collected = new _key_[heap.getSize ()];
        int ct = 0;
        while (!heap.isEmpty ())
        {
            collected[ct++] = heap.peek ();
            heap.removeGreatest ();
        }
        _key_[] copy = new _key_[collected.length];
        System.arraycopy (collected, 0, copy, 0, collected.length);
        Arrays.sort (copy);
        TestUtils_KeyTypeName_.assertArrayContentsEqual (collected, copy);
    }

}
