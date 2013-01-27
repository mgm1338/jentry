package collections.hash.set;

import collections.hash.HashFunctions;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryChar;
import core.stub.IntValueConverter;
import core.stub.*;
import junit.framework.TestCase;
import org.junit.Test;
import util.TestUtilsChar;

/**
 * Copyright 1/14/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/14/13
 */
public class TestHashSetChar
{
    HashSetChar hashSet;
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
        hashSet = new HashSetChar( TEST_SIZE );
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue( hashSet.getSize() == 0 );

        //initial fill up
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) ) );
        }

        //fill up exact same will return the exact same entries
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) ) );
        }


        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
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
        hashSet = new HashSetChar( 8, 1.00, ArrayFactoryChar.defaultCharProvider,
                                            ArrayFactoryInt.defaultIntProvider, new SameBucketHashFunctionChar(),
                                            GrowthStrategy.doubleGrowth );

        TestCase.assertEquals( hashSet.getSize(), 0 );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) ) );

        }
        TestCase.assertEquals( hashSet.getSize(), TEST_SIZE );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getSize() );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getList( 0, null, false ).length );


    }

    @Test
    public void removeFromSameBucket()
    {
        if( template ) return;

        sameBucketTest();
        hashSet.remove( IntValueConverter.charFromInt( 0 + OFFSET_FROM_ZERO ) ); //remove first
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.charFromInt( 0 + OFFSET_FROM_ZERO ) ) == Const.NO_ENTRY );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.charFromInt( 0 + OFFSET_FROM_ZERO ) ) );


        sameBucketTest(); //reprime
        hashSet.remove( IntValueConverter.charFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) ); //remove last
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.charFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.charFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) ) );


        sameBucketTest(); //reprime
        hashSet.remove( IntValueConverter.charFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) ); //remove middle
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.charFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.charFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) ) );


        sameBucketTest();
        //remove all three
        hashSet.remove( IntValueConverter.charFromInt( 0 + OFFSET_FROM_ZERO ) ); //remove first
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.charFromInt( 0 + OFFSET_FROM_ZERO ) ) == Const.NO_ENTRY );
        hashSet.remove( IntValueConverter.charFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) ); //remove last
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 2 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.charFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );
        hashSet.remove( IntValueConverter.charFromInt( TEST_SIZE / 2+OFFSET_FROM_ZERO ) ); //remove middle
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 3 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.charFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) )
                             == Const.NO_ENTRY );

        TestCase.assertFalse( hashSet.contains( IntValueConverter.charFromInt( TEST_SIZE - 1 + OFFSET_FROM_ZERO ) ) );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.charFromInt( TEST_SIZE / 2 + OFFSET_FROM_ZERO ) ) );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.charFromInt( 0 + OFFSET_FROM_ZERO ) ) );


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
            TestCase.assertTrue( hashSet.remove( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) ) );
            TestCase.assertEquals( hashSet.getSize(), ( TEST_SIZE - i - 1 ) );
            TestCase.assertTrue( hashSet.getEntry( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) )
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
        hashSet = new HashSetChar( TEST_SIZE );

        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE * 4 );
        TestCase.assertFalse( hashSet.isEmpty() );

    }

    //When an item isnt in HashSet, should return false
    @Test
    public void assertRemoveIsNotThereFalse()
    {
        if( template ) return;
        fullRemove();
        TestCase.assertFalse( hashSet.remove( IntValueConverter.charFromInt( 1000 ) ) );

    }

    @Test
    public void freeListCompactNessTest()
    {
        if( template ) return;
        loadTest();
        hashSet.remove( IntValueConverter.charFromInt( 0 + OFFSET_FROM_ZERO ) ); //remove first
        //should take first spot
        TestCase.assertTrue( hashSet.insert( IntValueConverter.charFromInt( 1000 ) ) == 0 );
        TestCase.assertTrue( hashSet.contains( IntValueConverter.charFromInt( 1000 ) ) );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.charFromInt( 0 ) ) );

    }

    @Test
    public void growFreeListTest()
    {
        if( template ) return;
        growthTest();

        //doing a remove of all the items
        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            TestCase.assertTrue( hashSet.remove( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) ) );
        }
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue( hashSet.freeList.length >= TEST_SIZE * 4 );
        //insert all
        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertTrue( j < ( TEST_SIZE * 4 ) ); //compact (although with massive removes will replace
            //last item removed first).
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE * 4 );
        TestCase.assertFalse( hashSet.isEmpty() );


    }

    @Test
    public void clearTest()
    {
        if( template ) return;
        hashSet = new HashSetChar( TEST_SIZE );
        //fill
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) ) );
        }


        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
        }
        //clear
        hashSet.clear();
        //assert size and empty

        TestCase.assertTrue( hashSet.getSize() == 0 );
        TestCase.assertTrue( hashSet.isEmpty() );

        // fill up again more stuff
        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) ) );
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE * 4 );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE * 4; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.charFromInt( i + OFFSET_FROM_ZERO ) );
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
        HashSetChar copy = hashSet.copy( null );




    }

    @Test
    public void fullCopyEmptySet()
    {
        if( template ) return;

    }

    @Test
    public void fullCopyNullTest()
    {
        if( template ) return;

    }

    //tests, copy, make one null, assert old the same
    //insert 1/2 into one copy, insert rest of 1/2, assert both

    @Test
    public void randomInsertionTest()
    {
        if( template ) return;

    }


    protected void assertEquals(HashSetChar a, HashSetChar b)
    {
        TestUtilsChar.assertArrayContentsEqual( a.keys, b.keys );


    }

    protected class SameBucketHashFunctionChar extends HashFunctions.HashFunctionChar
    {
        @Override
        public int getHashCode( char k )
        {
            return 0;
        }
    }

}
