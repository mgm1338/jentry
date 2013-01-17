package collections.hash.set;

import collections.hash.HashFunctions;
import collections.hash.set.HashSetChar;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryChar;
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
public class TestHashSetChar
{
    HashSetChar hashSet;
    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    public static final int TEST_SIZE = 8;

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
            int j = hashSet.insert( IntValueConverter.charFromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
        }

        if( template ) return;
        TestCase.assertTrue( hashSet.getSize() == TEST_SIZE );
        TestCase.assertFalse( hashSet.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter.charFromInt( i ) );
        }
    }

    @Test
    public void sameBucketTest()
    {
        //artificially making every item go into the same bucket
        hashSet = new HashSetChar( 8, 1.00, ArrayFactoryChar.defaultcharProvider,
                                            ArrayFactoryInt.defaultintProvider, new SameBucketHashFunctionChar(),
                                            GrowthStrategy.doubleGrowth);

        TestCase.assertEquals( hashSet.getSize(), 0 );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter.charFromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
        }
        TestCase.assertEquals( hashSet.getSize(), TEST_SIZE );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getSize() );
        TestCase.assertEquals( TEST_SIZE, hashSet.bucketList.getList( 0, null, false ).length  );


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
