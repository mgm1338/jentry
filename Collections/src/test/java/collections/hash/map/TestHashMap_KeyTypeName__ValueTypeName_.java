package collections.hash.map;

import collections.hash.HashFunctions;
import collections.hash.set.TestHashSet_KeyTypeName_;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryObject;
import core.array.factory.ArrayFactoryLong;
import core.array.factory.*;
import core.stub.IntValueConverter;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils_ValueTypeName_;

/**
 * Copyright 2/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/27/13
 */
public class TestHashMap_KeyTypeName__ValueTypeName_
{
    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );
    public static final int TEST_SIZE = 8;
    private static final int LARGE_TEST_SIZE = 128;
    private HashMap_KeyTypeName__ValueTypeName_ map;

    @Before
    public void setup()
    {
        if( template ) return;
        HashMap_KeyTypeName__ValueTypeName_ shortConstructor = new HashMap_KeyTypeName__ValueTypeName_( 8 );
        map = new HashMap_KeyTypeName__ValueTypeName_( 8, .75,
                                                       ArrayFactory_KeyTypeName_.default_KeyTypeName_Provider,
                                                       ArrayFactoryInt.defaultIntProvider,
                                                       HashFunctions.hashFunction_KeyTypeName_,
                                                       GrowthStrategy.doubleGrowth,
                                                       ArrayFactory_ValueTypeName_.default_ValueTypeName_Provider );


    }

    /**
     * Initially load with TEST_SIZE items, where the initial capacity is set to that size
     * Assert the sizes, and that entries are returned in compact manner
     */
    @Test
    public void loadTest()
    {
        if( template ) return;
        TestCase.assertEquals( map.get( IntValueConverter._key_FromInt( 5 ), IntValueConverter._val_FromInt( -1 ) ),
                               IntValueConverter._val_FromInt( -1 ) );

        TestCase.assertEquals( map.getSize(), 0 );
        TestCase.assertTrue( map.isEmpty() );

        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int entry = map.insert( IntValueConverter._key_FromInt( i ), IntValueConverter._val_FromInt( i + 100 ) );
            TestCase.assertEquals( i, entry ); //test compact
            TestCase.assertEquals( map.get( IntValueConverter._key_FromInt( i ), IntValueConverter._val_FromInt( -1 ) ),
                                   IntValueConverter._val_FromInt( i + 100 ) );
        }
        TestCase.assertEquals( map.getSize(), TEST_SIZE );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertTrue( map.containsKey( IntValueConverter._key_FromInt( i ) ) == i );
        }
        TestCase.assertFalse( map.isEmpty() );

    }

    /** Remove each item iteratively */
    @Test
    public void fullRemove()
    {
        if( template ) return;
        TestCase.assertEquals( map.getSize(), 0 );
        loadTest();
        TestCase.assertEquals( map.getSize(), TEST_SIZE );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int entry = map.remove( IntValueConverter._key_FromInt( i ) );
            TestCase.assertEquals( i, entry ); //test compact
        }
        TestCase.assertEquals( map.getSize(), 0 );

        //assert all gone
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( map.get( IntValueConverter._key_FromInt( i ), IntValueConverter._val_FromInt( -1
            ) ),
                                   IntValueConverter._val_FromInt( -1 ) );
        }

    }

    /** Load more items into the HashSet than its initial capacity can accommodate */
    @Test
    public void growthTest()
    {
        if( template ) return;
        for( int i = 0; i < LARGE_TEST_SIZE; i++ )
        {
            int entry = map.insert( IntValueConverter._key_FromInt( i ), IntValueConverter._val_FromInt( i + 100 ) );
            TestCase.assertEquals( i, entry ); //test compact
            TestCase.assertEquals( map.get( IntValueConverter._key_FromInt( i ), IntValueConverter._val_FromInt( -1 ) ),
                                   IntValueConverter._val_FromInt( i + 100 ) );
        }

        TestCase.assertEquals( LARGE_TEST_SIZE, map.getSize() );
    }

    /** Basic test, assert item returns false when not present (duh) */
    @Test
    public void assertRemoveIsNotThereFalse()
    {
        if( template ) return;
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int entry = map.remove( IntValueConverter._key_FromInt( i ) );
            TestCase.assertEquals( entry, -1 ); //test compact
        }
    }


    /** Test our clear method, that it will result in an empty set */
    @Test
    public void clearTest()
    {
        if( template ) return;
        loadTest();
        map.clear();
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( IntValueConverter._val_FromInt( -1 ),
                                   map.get( IntValueConverter._key_FromInt( i ), IntValueConverter._val_FromInt( -1 ) )
            );
        }
        TestCase.assertTrue( map.isEmpty() );
        TestCase.assertEquals( 0, map.getSize() );
        loadTest();


    }

    /** Copy with a null target, assert the copy is functionally the same as the original. */
    @Test
    public void copyFromNull()
    {
        if( template ) return;
        loadTest();
        HashMap_KeyTypeName__ValueTypeName_ copy = map.copy( null );
        assertEquals( map, copy );

    }

    /** Standard copy from one map to another. */
    @Test
    public void fullCopyValidSetTest()
    {
        if( template ) return;
        loadTest();
        HashMap_KeyTypeName__ValueTypeName_ copy = new HashMap_KeyTypeName__ValueTypeName_( map.getSize() );
        map.copy( copy );
        assertEquals( map, copy );
    }

    /** Copy to a larger HashSet */
    @Test
    public void copyToLargerSet()
    {
        if( template ) return;
        loadTest();
        HashMap_KeyTypeName__ValueTypeName_ copy = new HashMap_KeyTypeName__ValueTypeName_( map.getSize() * 100 );
        map.copy( copy );

    }

    /** Copy to a smaller set */
    @Test
    public void copyFromSmaller()
    {
        if( template ) return;
        loadTest();
        HashMap_KeyTypeName__ValueTypeName_ copy = new HashMap_KeyTypeName__ValueTypeName_( map.getSize() / 4 );
        map.copy( copy );

    }

    protected void assertEquals( HashMap_KeyTypeName__ValueTypeName_ orig, HashMap_KeyTypeName__ValueTypeName_ copy )
    {
        //little messy to be asserting equality with another test
        TestHashSet_KeyTypeName_.assertEquals( orig.set, copy.set );
        //values equal up to original length
        TestUtils_ValueTypeName_.assertArrayContentsToLen( orig.values, copy.values, orig.values.length );

    }
}
