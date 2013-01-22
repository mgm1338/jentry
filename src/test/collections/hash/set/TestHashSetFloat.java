package collections.hash.set;

import collections.hash.HashFunctions;
import collections.hash.set.HashSetFloat;
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryFloat;
import core.stub.IntValueConverter;
import core.stub.*;
import junit.extensions.TestSetup;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Copyright 1/14/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/14/13
 */
public class TestHashSetFloat
{
    HashSetFloat hashSet;
    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    public static final int TEST_SIZE = 8;


    /**
     * Initially load with TEST_SIZE items, where the initial capacity is set to that size
     * Assert the sizes, and that entries are returned in compact manner
     */
    @Test
    public void loadTest()
    {
        if( template ) return;
        hashSet = new HashSetFloat( TEST_SIZE );
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue( hashSet.getSize() == 0 );

        //initial fill up
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.floatFromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.floatFromInt( i ) ) );
        }

        //fill up exact same will return the exact same entries
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.floatFromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.floatFromInt( i ) ) );
        }


        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.floatFromInt( i ) );
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
        hashSet = new HashSetFloat( 8, 1.00, ArrayFactoryFloat.defaultfloatProvider,
                                            ArrayFactoryInt.defaultintProvider, new SameBucketHashFunctionFloat(),
                                            GrowthStrategy.doubleGrowth );

        TestCase.assertEquals( hashSet.getSize(), 0 );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.floatFromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
            TestCase.assertTrue( hashSet.contains( IntValueConverter.floatFromInt( i ) ) );

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
        hashSet.remove( IntValueConverter.floatFromInt( 0 ) ); //remove first
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.floatFromInt( 0 ) ) == Const.NO_ENTRY );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.floatFromInt( 0 ) ) );


        sameBucketTest(); //reprime
        hashSet.remove( IntValueConverter.floatFromInt( TEST_SIZE - 1 ) ); //remove last
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.floatFromInt( TEST_SIZE - 1 ) ) == Const.NO_ENTRY );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.floatFromInt( TEST_SIZE - 1  ) ) );


        sameBucketTest(); //reprime
        hashSet.remove( IntValueConverter.floatFromInt( TEST_SIZE / 2 ) ); //remove middle
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.floatFromInt( TEST_SIZE / 2 ) ) == Const.NO_ENTRY );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.floatFromInt( TEST_SIZE / 2  ) ) );


        sameBucketTest();
        //remove all three
        hashSet.remove( IntValueConverter.floatFromInt( 0 ) ); //remove first
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 1 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.floatFromInt( 0 ) ) == Const.NO_ENTRY );
        hashSet.remove( IntValueConverter.floatFromInt( TEST_SIZE - 1 ) ); //remove last
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 2 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.floatFromInt( TEST_SIZE - 1 ) ) == Const.NO_ENTRY );
        hashSet.remove( IntValueConverter.floatFromInt( TEST_SIZE / 2 ) ); //remove middle
        TestCase.assertEquals( hashSet.size, TEST_SIZE - 3 );
        TestCase.assertTrue( hashSet.getEntry( IntValueConverter.floatFromInt( TEST_SIZE / 2 ) ) == Const.NO_ENTRY );

        TestCase.assertFalse( hashSet.contains( IntValueConverter.floatFromInt( TEST_SIZE - 1  ) ) );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.floatFromInt( TEST_SIZE /2  ) ) );
        TestCase.assertFalse( hashSet.contains( IntValueConverter.floatFromInt( 0  ) ) );



    }

    /**
     * Remove each item iteratively
     */
    @Test
    public void fullRemove()
    {
        if( template ) return;

        loadTest();//fill up

        //remove each item iteratively, asserting it was removed
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertTrue( hashSet.remove( IntValueConverter.floatFromInt( i ) ));
            TestCase.assertEquals( hashSet.getSize(), ( TEST_SIZE - i - 1 ) );
            TestCase.assertTrue( hashSet.getEntry( IntValueConverter.floatFromInt( i ) ) == Const.NO_ENTRY );
        }
        TestCase.assertTrue( hashSet.getSize() == 0 );
        TestCase.assertTrue( hashSet.isEmpty() );


    }

    /**
     * Load more items into the HashSet than its initial capacity can accommodate
     */
    @Test
    public void growthTest()
    {
        if (template) return;
        hashSet = new HashSetFloat( TEST_SIZE );

        for( int i = 0; i < TEST_SIZE*4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.floatFromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE*4 );
        TestCase.assertFalse( hashSet.isEmpty() );

    }

    //When an item isnt in HashSet, should return false
    @Test
    public void assertRemoveIsNotThereFalse()
    {
        if (template) return;
        fullRemove();
        TestCase.assertFalse( hashSet.remove( IntValueConverter.floatFromInt( 1000 ) ) );

    }

    @Test
    public void freeListCompactNessTest()
    {
        if (template) return;
        loadTest();
        hashSet.remove( IntValueConverter.floatFromInt( 0 ) ); //remove first
        //should take first spot
        TestCase.assertTrue( hashSet.insert( IntValueConverter.floatFromInt( 1000  ))==0 );
        TestCase.assertTrue( hashSet.contains(  IntValueConverter.floatFromInt( 1000  ) ));
        TestCase.assertFalse( hashSet.contains(  IntValueConverter.floatFromInt( 0  ) ));

    }

    @Test
    public void growFreeListTest()
    {
        if (template) return;
        growthTest();

        //doing a remove of all the items
        for( int i = 0; i < TEST_SIZE*4; i++ )
        {
           TestCase.assertTrue(  hashSet.remove( IntValueConverter.floatFromInt( i ) ));
        }
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue( hashSet.freeList.length>=TEST_SIZE*4 );
        //insert all
        for( int i = 0; i < TEST_SIZE*4; i++ )
        {
            int j = hashSet.insert( IntValueConverter.floatFromInt( i ) );
            TestCase.assertTrue( j<(TEST_SIZE*4) ); //compact (although with massive removes will replace
            //last item removed first).
        }

        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE*4 );
        TestCase.assertFalse( hashSet.isEmpty() );


    }


    protected class SameBucketHashFunctionFloat extends HashFunctions.HashFunctionFloat
    {
        @Override
        public int getHashCode( float k )
        {
            return 0;
        }
    }

}
