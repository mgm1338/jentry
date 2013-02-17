package collections.heap;

import core.util.comparator.Comparator_KeyTypeName_;
import core.util.comparator.Comparators;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class TestHeap_KeyTypeName_
{

    Heap_KeyTypeName_ heap;

    @Before
    public void setup()
    {
        heap = new Heap_KeyTypeName_ (8, new Comparators._KeyTypeName_Asc ());
    }

    //Start insert tests
    @Test
    public void testInsertCompact()
    {

    }


    protected void assertInOrder(Heap_KeyTypeName_ heap)
    {

    }

}
