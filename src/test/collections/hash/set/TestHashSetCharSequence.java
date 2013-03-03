package collections.hash.set;

import collections.hash.HashFunctions;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryCharSequence;
import core.stub.IntValueConverter;
import core.stub.*;
import junit.framework.TestCase;
import org.junit.Test;
import util.TestUtilsInt;
import util.TestUtilsCharSequence;

/**
 * Copyright 1/14/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/14/13
 */
public class TestHashSetCharSequence
{
    HashSetCharSequence hashSet;
    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    public static final int TEST_SIZE = 8;
    //when adding values around 0,1 can get results that would indicate correct behavior
    public static final int OFFSET_FROM_ZERO = 10;


    /**
     * Initially load with TEST_SIZE items, where the initial capacity is set to that size
     * Assert the sizes, and that entries are returned in compact manner
     */
    @Test
    public void loadTest()
    {
        if( template ) return;
        hashSet = new HashSetCharSequence( TEST_SIZE );
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue( hashSet.getSize() == 0 );

        //initial fill up
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) ) == i );
        }

        //fill up exact same will return the exact same entries
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) ) == i );
        }


        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
        }
    }

    /**
     * Load TEST_SIZE entries into the same bucket into the HashSet, this will show that collisions
     * do not effect insertion.
     */
    @Test
    public void sameBucketTest()
    {
        if( template ) return;

        //artificially making every item go into the same bucket
        hashSet = new HashSetCharSequence( 8, 1.00, ArrayFactoryCharSequence.defaultCharSequenceProvider,
                                            ArrayFactoryInt.defaultIntProvider, new SameBucketHashFunctionCharSequence(),
                                            GrowthStrategy.doubleGrowth );

        TestCase.assertEquals( hashSet.getSize(), 0 );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) ) == i );

        }
        TestCase.assertEquals( TEST_SIZE, hashSet.getSize() );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getSize() );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getList( 0, null, false ).length );


    }

    /**
     * Similar to test above, assert removing when many in same bucket doesnt trip up when removing first, last
     * or middle
     */
    @Test
    public void removeFromSameBucket()
    {
        if( template ) return;

        sameBucketTest();
        hashSet.remove( IntValueConverter.CharSequenceFromInt( 0 + OFFSET_FROM_ZERO ) ); //remove first
        TestCase.assertEquals( TEST_SIZE - 1, hashSet.size );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.CharSequenceFromInt( 0 + OFFSET_FROM_ZERO ) ) == Const.NO_ENTRY );
        TestCase.assertEquals( Const.NO_ENTRY, hashSet.contains( IntValueConverter.CharSequenceFromInt( 0 + OFFSET_FROM_ZERO
        ) ) );


        sameBucketTest(); //reprime
        hashSet.remove( IntValueConverter.CharSequenceFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) ); //remove last
        TestCase.assertEquals( TEST_SIZE - 1, hashSet.size );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.CharSequenceFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );
        TestCase.assertEquals( Const.NO_ENTRY, hashSet.contains( IntValueConverter.CharSequenceFromInt( TEST_SIZE - 1 +
                                                                                                 OFFSET_FROM_ZERO ) ) );


        sameBucketTest(); //reprime
        hashSet.remove( IntValueConverter.CharSequenceFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) ); //remove middle
        TestCase.assertEquals( TEST_SIZE - 1, hashSet.size );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.CharSequenceFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );
        TestCase.assertEquals( Const.NO_ENTRY, hashSet.contains( IntValueConverter.CharSequenceFromInt( TEST_SIZE / 2 +
                                                                                                 OFFSET_FROM_ZERO
        ) ) );


        sameBucketTest();
        //remove all three
        hashSet.remove( IntValueConverter.CharSequenceFromInt( 0 + OFFSET_FROM_ZERO ) ); //remove first
        TestCase.assertEquals( TEST_SIZE - 1, hashSet.size );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.CharSequenceFromInt( 0 + OFFSET_FROM_ZERO ) ) == Const.NO_ENTRY );
        hashSet.remove( IntValueConverter.CharSequenceFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) ); //remove last
        TestCase.assertEquals( TEST_SIZE - 2, hashSet.size );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.CharSequenceFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );
        hashSet.remove( IntValueConverter.CharSequenceFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) ); //remove middle
        TestCase.assertEquals( TEST_SIZE - 3, hashSet.size );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.CharSequenceFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );

        TestCase.assertEquals( Const.NO_ENTRY, hashSet.contains( IntValueConverter.CharSequenceFromInt( TEST_SIZE - 1 +
                                                                                                 OFFSET_FROM_ZERO ) ) );
        TestCase.assertEquals( Const.NO_ENTRY, hashSet.contains( IntValueConverter.CharSequenceFromInt( TEST_SIZE / 2 +
                                                                                                 OFFSET_FROM_ZERO ) ) );
        TestCase.assertEquals( Const.NO_ENTRY, hashSet.contains( IntValueConverter.CharSequenceFromInt( 0 + OFFSET_FROM_ZERO
        ) ) );


    }

    /** Remove each item iteratively */
    @Test
    public void fullRemove()
    {
        if( template ) return;

        loadTest();//fill up

        //remove each item iteratively, asserting it was removed
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertNotSame( hashSet.remove( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) ), Const.NO_ENTRY );
            TestCase.assertEquals( ( TEST_SIZE - i - 1 ), hashSet.getSize() );
            TestCase.assertTrue( hashSet.getEntry( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) )
                                 == Const.NO_ENTRY );
        }
        TestCase.assertTrue( hashSet.getSize() == 0 );
        TestCase.assertTrue( hashSet.isEmpty() );


    }

    /** Load more items into the HashSet than its initial capacity can accommodate */
    @Test
    public void growthTest()
    {
        if( template ) return;
        hashSet = new HashSetCharSequence( TEST_SIZE );

        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE * 4 );
        TestCase.assertFalse( hashSet.isEmpty() );

    }

    /** Basic test, assert item returns false when not present (duh) */
    @Test
    public void assertRemoveIsNotThereFalse()
    {
        if( template ) return;
        fullRemove();
        TestCase.assertEquals( Const.NO_ENTRY, hashSet.remove( IntValueConverter.CharSequenceFromInt( 1000 ) ) );

    }

    /** Test that after removing, next insert will use that vacated entry */
    @Test
    public void freeListCompactNessTest()
    {
        if( template ) return;
        loadTest();
        hashSet.remove( IntValueConverter.CharSequenceFromInt( 0 + OFFSET_FROM_ZERO ) ); //remove first
        //should take first spot
        TestCase.assertTrue( hashSet.insert( IntValueConverter.CharSequenceFromInt( 1000 ) ) == 0 );
        TestCase.assertTrue( hashSet.contains( IntValueConverter.CharSequenceFromInt( 1000 ) )==0 );
        TestCase.assertEquals( Const.NO_ENTRY, hashSet.contains( IntValueConverter.CharSequenceFromInt( 0 ) ) );

    }

    /** Remove enough items to have to grow the free list, and assert its contents */
    @Test
    public void growFreeListTest()
    {
        if( template ) return;
        growthTest();

        //doing a remove of all the items
        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            TestCase.assertNotSame( Const.NO_ENTRY, hashSet.remove( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO
            ) ) );
        }
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue( hashSet.freeList.length >= TEST_SIZE * 4 );
        //insert all
        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertTrue( j < ( TEST_SIZE * 4 ) ); //compact (although with massive removes will replace
            //last item removed first).
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE * 4 );
        TestCase.assertFalse( hashSet.isEmpty() );


    }

    /** Test our clear method, that it will result in an empty set */
    @Test
    public void clearTest()
    {
        if( template ) return;
        hashSet = new HashSetCharSequence( TEST_SIZE );
        //fill
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) )==i );
        }


        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ), hashSet.get( i ) );
        }
        //clear
        hashSet.clear();
        //assert size and empty

        TestCase.assertTrue( hashSet.getSize() == 0 );
        TestCase.assertTrue( hashSet.isEmpty() );

        // fill up again more stuff
        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) )==i );
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE * 4 );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.CharSequenceFromInt( i + OFFSET_FROM_ZERO ) );
        }
        hashSet.clear();

        TestCase.assertTrue( hashSet.getSize() == 0 );
        TestCase.assertTrue( hashSet.isEmpty() );

    }


    //free list, exactly same with same pointer
    //keys, same keys
    //size is same, rehash size is same, load factor same
    //same growth, factories
    @Test
    public void fullCopyValidSetTest()
    {
        if( template ) return;
        loadTest();
        HashSetCharSequence copy = hashSet.copy( null );
        assertEquals( hashSet, copy );
    }

    /** Copy to a larger HashSet */
    @Test
    public void copyToLargerSet()
    {
        if( template ) return;
        loadTest();
        HashSetCharSequence copy = hashSet.copy( new HashSetCharSequence( 4096 ) );
        assertEquals( hashSet, copy );
    }


    /** Copy to a smaller set */
    @Test
    public void copyFromSmaller()
    {
        if( template ) return;
        loadTest();
        HashSetCharSequence copy = hashSet.copy( new HashSetCharSequence( 1 ) );
        assertEquals( hashSet, copy );

    }


    /**
     * Test the equality of all state of the HashSet. If we are copying to a much larger set,
     * we will assert up to the expected results. If copying from smaller, we expect it to grow.
     *
     * @param expected expected results
     * @param actual   what we actually have
     */
    public static void assertEquals( HashSetCharSequence expected, HashSetCharSequence actual )
    {
        TestUtilsCharSequence.assertArrayContentsToLen( expected.keys, actual.keys, expected.keys.length );
        TestUtilsInt.assertArrayContentsEqual( expected.freeList, actual.freeList );
        TestCase.assertEquals( expected.loadFactor, actual.loadFactor );
        TestCase.assertEquals( expected.loadFactorSize, actual.loadFactorSize );
        TestCase.assertEquals( expected.getSize(), actual.getSize() );
        TestCase.assertEquals( expected.getNextEntry(), actual.getNextEntry() );

    }

    protected class SameBucketHashFunctionCharSequence extends HashFunctions.HashFunctionCharSequence
    {
        @Override
        public int getHashCode( CharSequence k )
        {
            return 0;
        }
    }

}
