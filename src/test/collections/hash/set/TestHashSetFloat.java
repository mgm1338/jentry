package collections.hash.set;

import collections.hash.HashFunctions;
import collections.hash.set.HashSetFloat;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryFloat;
import core.stub.IntValueConverter;
import core.stub.*;
import junit.extensions.TestSetup;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

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
        }

        if( template ) return;
        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.floatFromInt( i ) );
        }
    }

    @Test
    public void sameBucketTest()
    {
        //artificially making every item go into the same bucket
        hashSet = new HashSetFloat( 8, 1.00, ArrayFactoryFloat.defaultfloatProvider,
                                            ArrayFactoryInt.defaultintProvider, new SameBucketHashFunctionFloat(),
                                            GrowthStrategy.doubleGrowth);

        TestCase.assertEquals( hashSet.getSize(), 0 );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.floatFromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
        }
        TestCase.assertEquals( hashSet.getSize(), TEST_SIZE );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getSize() );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getList( 0, null, false ).length  );


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
