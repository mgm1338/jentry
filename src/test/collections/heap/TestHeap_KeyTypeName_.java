package collections.heap;

import core.stub.IntValueConverter;
import core.stub._key_;
import core.util.comparator.Comparator_KeyTypeName_;
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
public class TestHeap_KeyTypeName_
{

    private static final int TEST_SIZE = 5;
    Heap_KeyTypeName_ heap;

    @Before
    public void setup ()
    {
        heap = new Heap_KeyTypeName_ (8, new Comparators._KeyTypeName_Asc ());
    }

    @Test
    public void testInsertCompact ()
    {
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

    @Test
    public void testReverseInsertionCorrectOrder ()
    {
        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (TEST_SIZE - i)));
        }
        assertInAscOrder (heap);
    }

    @Test
    public void testRandomInsertionCorrectOrder ()
    {
        Random random = new Random (42);
        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (random.nextInt (16))));
        }
    }

    @Test
    public void testBackwardsWithDoubleGrowth ()
    {
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (TEST_SIZE - i)));
        }
        TestCase.assertEquals (heap.keys.length, 32);
        TestCase.assertEquals (heap.tree.length, 32);
        TestCase.assertEquals (heap.inverse.length, 32);
        TestCase.assertEquals (24, heap.getSize ());
        assertInAscOrder (heap);
    }

    @Test
    public void testRandomWithDoubleGrowth ()
    {
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

    @Test
    public void manyWithSameOneHigher ()
    {
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (4)));
        }

        heap.insert (IntValueConverter._key_FromInt (5));
        TestCase.assertEquals (IntValueConverter._key_FromInt (5), heap.peek ());
        TestCase.assertEquals (25, heap.getSize ());
    }

    @Test
    public void manyWithSameOneLower ()
    {
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter._key_FromInt (4)));
        }

        heap.insert (IntValueConverter._key_FromInt (3));

        for (int i = 0; i < 24; i++)
        {
            heap.removeTop ();
        }
        TestCase.assertEquals (IntValueConverter._key_FromInt (3), heap.peek ());
        TestCase.assertEquals (1, heap.getSize ());
    }

    @Test
    public void fullRemoval ()
    {
        testRandomWithDoubleGrowth ();
        while (!heap.isEmpty ())
        {
            heap.removeTop ();
            assertInAscOrder (heap);
        }
        TestCase.assertTrue (heap.isEmpty ());
        TestCase.assertEquals (0, heap.getSize ());
    }

    //test free list, and internal equals with copy methods


    protected void assertInAscOrder (Heap_KeyTypeName_ heap)
    {
        _key_[] collected = new _key_[heap.getSize ()];
        int ct = 0;
        while (!heap.isEmpty ())
        {
            collected[ct++] = heap.peek ();
            heap.removeTop ();
        }
        _key_[] copy = new _key_[collected.length];
        System.arraycopy (collected, 0, copy, 0, collected.length);
        Arrays.sort (copy);
        TestUtils_KeyTypeName_.assertArrayContentsEqual (collected, copy);
    }

}
