package collections.heap.ungen;

import core.stub.IntValueConverter;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 *
 * Object is not generated for comparison between Objects is already well-defined
 * within JDK, and we wouldnt want to mess with that
 */
public class TestBinaryHeapObject
{

    private static final int TEST_SIZE = 5;
    BinaryHeapObject heap;
    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    @Before
    public void setup()
    {
        heap = new BinaryHeapObject( 8, new ObjectAsc() );
    }

    @Test
    public void testInsertCompact()
    {
        if( template ) return;
        TestCase.assertTrue( heap.isEmpty() );
        TestCase.assertEquals( 0, heap.getSize() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( i,
                                   heap.insert( IntValueConverter.ObjectFromInt( i ) ) );
        }
        TestCase.assertEquals( TEST_SIZE, heap.getSize() );


        heap.remove( 3 );
        TestCase.assertEquals( 3,
                               heap.insert( IntValueConverter.ObjectFromInt( 3 ) ) );
        TestCase.assertEquals( TEST_SIZE, heap.getSize() );

    }

    @Test
    public void testReverseInsertionCorrectOrder()
    {
        if( template ) return;
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( i,
                                   heap.insert( IntValueConverter.ObjectFromInt( TEST_SIZE - i ) ) );
        }
        assertInAscOrder( heap );
    }

    @Test
    public void testRandomInsertionCorrectOrder()
    {
        if( template ) return;
        Random random = new Random( 42 );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( i,
                                   heap.insert( IntValueConverter.ObjectFromInt( random.nextInt( 16 ) ) ) );
        }
    }

    @Test
    public void testBackwardsWithDoubleGrowth()
    {
        if( template ) return;
        for( int i = 0; i < 24; i++ )
        {
            TestCase.assertEquals( i,
                                   heap.insert( IntValueConverter.ObjectFromInt( 24 - i ) ) );
        }
        TestCase.assertEquals( 24, heap.getSize() );
        assertInAscOrder( heap );
    }

    @Test
    public void testRandomWithDoubleGrowth()
    {
        if( template ) return;
        Random random = new Random( 42 );
        for( int i = 0; i < 24; i++ )
        {
            TestCase.assertEquals( i,
                                   heap.insert( IntValueConverter.ObjectFromInt( random.nextInt( 16 ) ) ) );
        }
        TestCase.assertEquals( 24, heap.getSize() );
        assertInAscOrder( heap );
    }

    @Test
    public void manyWithSameOneHigher()
    {
        if( template ) return;
        for( int i = 0; i < 24; i++ )
        {
            TestCase.assertEquals( i,
                                   heap.insert( IntValueConverter.ObjectFromInt( 4 ) ) );
        }

        heap.insert( IntValueConverter.ObjectFromInt( 5 ) );
        TestCase.assertEquals( IntValueConverter.ObjectFromInt( 4 ), heap.peek() );
        TestCase.assertEquals( 25, heap.getSize() );
        for( int i = 0; i < 24; i++ )
        {
            heap.removeGreatest();
        }
        TestCase.assertEquals( IntValueConverter.ObjectFromInt( 5 ), heap.peek() );
    }

    @Test
    public void manyWithSameOneLower()
    {
        if( template ) return;
        for( int i = 0; i < 24; i++ )
        {
            TestCase.assertEquals( i,
                                   heap.insert( IntValueConverter.ObjectFromInt( 4 ) ) );
        }
        heap.insert( IntValueConverter.ObjectFromInt( 3 ) );
        TestCase.assertEquals( IntValueConverter.ObjectFromInt( 3 ), heap.peek() );
    }

    @Test
    public void fullRemoval()
    {
        if( template ) return;
        testRandomWithDoubleGrowth();
        while( !heap.isEmpty() )
        {
            heap.removeGreatest();
            assertInAscOrder( heap );
        }
        TestCase.assertTrue( heap.isEmpty() );
        TestCase.assertEquals( 0, heap.getSize() );
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
                                   heap.insert (IntValueConverter.ObjectFromInt (random.nextInt (16))));
        }
        TestCase.assertEquals (24, heap.getSize ());
        heap.clear (); //insertion with growth
        TestCase.assertEquals (0, heap.getSize ());
        TestCase.assertTrue (heap.isEmpty ());

        //stolen from backwards growth test
        for (int i = 0; i < 24; i++)
        {
            TestCase.assertEquals (i,
                                   heap.insert (IntValueConverter.ObjectFromInt (24 - i)));
        }
        TestCase.assertEquals (24, heap.getSize ());
        heap.clear ();
        TestCase.assertEquals (0, heap.getSize ());
        TestCase.assertTrue (heap.isEmpty ());


    }



    //test free list, and internal equals with copy methods
    protected void assertInAscOrder( BinaryHeapObject heap )
    {
        if( template ) return;
        Object[] collected = new Object[ heap.getSize() ];
        int ct = 0;
        while( !heap.isEmpty() )
        {
            collected[ ct++ ] = heap.peek();
            heap.removeGreatest();
        }
        Object[] copy = new Object[ collected.length ];
        System.arraycopy( collected, 0, copy, 0, collected.length );
        Arrays.sort( copy );
        TestUtilsObject.assertArrayContentsEqual( collected, copy );
    }

    private final static class ObjectAsc implements ComparatorObject
    {

        /**
         * Type specific comparator for the primitive types.
         * Conforms to the same convention as {@link Comparable }, returns a
         * negative number if a is less than b, a positive number if a is greater
         * than b, and 0 if the two are equal.
         *
         * @param a first item
         * @param b second item
         * @return negative if a less than b, positive if b less than a, zero
         *         if equal
         */
        @Override
        public int compare( Object a, Object b )
        {
            return IntValueConverter.toInt( b ) - IntValueConverter.toInt( a );
        }
    }

}
