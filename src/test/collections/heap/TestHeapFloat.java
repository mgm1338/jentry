package collections.heap;

import core.stub.IntValueConverter;
import core.stub.*;
import core.util.comparator.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.TestUtilsFloat;

import java.util.Arrays;
import java.util.Random;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class TestHeapFloat
{

    private static final int TEST_SIZE = 5;
    HeapFloat heap;
    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    @Before
    public void setup ()
    {
        heap = new HeapFloat (8, new Comparators.FloatAsc());
    }

    @Test
    public void testInsertCompact ()
    {
        if (template) return;
        TestCase.assertTrue (heap.isEmpty ());
        TestCase.assertEquals (0, heap.getSize ());

        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.floatFromInt (i)));
        }
        TestCase.assertEquals (TEST_SIZE, heap.getSize ());


        heap.remove (3);
        TestCase.assertEquals (3,
                               heap.insert (IntValueConverter.floatFromInt (3)));
        TestCase.assertEquals (TEST_SIZE, heap.getSize ());

    }

    @Test
    public void testReverseInsertionCorrectOrder ()
    {
        if (template) return;
        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.floatFromInt (TEST_SIZE - i)));
        }
        assertInAscOrder (heap);
    }

    @Test
    public void testRandomInsertionCorrectOrder ()
    {
        if (template) return;
        Random random = new Random (42);
        for (int i = 0; i < TEST_SIZE; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.floatFromInt (random.nextInt (16))));
        }
    }

    @Test
    public void testBackwardsWithDoubleGrowth ()
    {
        if (template) return;
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.floatFromInt (24 - i)));
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
        if (template) return;
        Random random = new Random (42);
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.floatFromInt (random.nextInt (16))));
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
        if (template) return;
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.floatFromInt (4)));
        }

        heap.insert (IntValueConverter.floatFromInt (5));
        TestCase.assertEquals (IntValueConverter.floatFromInt (4), heap.peek ());
        TestCase.assertEquals (25, heap.getSize ());
        for (int i = 0; i < 24; i++)
        {
            heap.removeTop ();
        }
        TestCase.assertEquals (IntValueConverter.floatFromInt (5), heap.peek ());
    }

    @Test
    public void manyWithSameOneLower ()
    {
        if (template) return;
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.floatFromInt (4)));
        }
        heap.insert (IntValueConverter.floatFromInt (3));
        TestCase.assertEquals (IntValueConverter.floatFromInt (3), heap.peek ());
    }

    @Test
    public void fullRemoval ()
    {
        if (template) return;
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


    protected void assertInAscOrder (HeapFloat heap)
    {
        if (template) return;
        float[] collected = new float[heap.getSize ()];
        int ct = 0;
        while (!heap.isEmpty ())
        {
            collected[ct++] = heap.peek ();
            heap.removeTop ();
        }
        float[] copy = new float[collected.length];
        System.arraycopy (collected, 0, copy, 0, collected.length);
        Arrays.sort (copy);
        TestUtilsFloat.assertArrayContentsEqual (collected, copy);
    }

}
