package collections.hash.set;

import collections.hash.HashFunctions;
import collections.hash.set.HashSet_KeyTypeName_;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactory_KeyTypeName_;
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
public class TestHashSet_KeyTypeName_
{
    HashSet_KeyTypeName_ hashSet;
    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    public static final int TEST_SIZE = 8;

    @Test
    public void loadTest()
    {
        if( template ) return;
        hashSet = new HashSet_KeyTypeName_( TEST_SIZE );
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue( hashSet.getSize() == 0 );

        //initial fill up
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter._key_FromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
        }

        if( template ) return;
        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter._key_FromInt( i ) );
        }
    }

    @Test
    public void sameBucketTest()
    {
        //artificially making every item go into the same bucket
        hashSet = new HashSet_KeyTypeName_( 8, 1.00, ArrayFactory_KeyTypeName_.default_key_Provider,
                                            ArrayFactoryInt.defaultintProvider, new SameBucketHashFunction_KeyTypeName_(),
                                            GrowthStrategy.doubleGrowth);

        TestCase.assertEquals( hashSet.getSize(), 0 );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter._key_FromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
        }
        TestCase.assertEquals( hashSet.getSize(), TEST_SIZE );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getSize() );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getList( 0, null, false ).length  );


    }


    protected class SameBucketHashFunction_KeyTypeName_ extends HashFunctions.HashFunction_KeyTypeName_
    {
        @Override
        public int getHashCode( _key_ k )
        {
            return 0;
        }
    }

}
