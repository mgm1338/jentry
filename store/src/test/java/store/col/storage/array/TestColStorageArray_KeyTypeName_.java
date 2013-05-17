package store.col.storage.array;

import com.sun.deploy.util.ArrayUtil;
import core.Types;
import core.array.GrowthStrategy;
import core.stub.IntValueConverter;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 5/13/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/13/13
 */

public class TestColStorageArray_KeyTypeName_
{

    public static final int TEST_SIZE = 128;
    protected ColStorageArray_KeyTypeName_ store;

    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    @Before
    public void setup()
    {
        store = new ColStorageArray_KeyTypeName_( TEST_SIZE );
        TestCase.assertEquals( Types._KeyTypeName_, store.getType() );
    }

    /**
     * Load the storage, and assert that exception thrown when overloaded
     */
    @Test
    public void loadTest()
    {
        if( template ) return;
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            store.setValue( IntValueConverter._key_FromInt( i ), i );
        }

        try
        {
            store.setValue( IntValueConverter._key_FromInt( 0 ), TEST_SIZE );
            TestCase.fail();
        }
        catch( Exception e )
        {

        }
    }

    @Test
    public void testLoading()
    {
        if( template ) return;
        loadTest();
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( IntValueConverter._key_FromInt( i ), store.getValue( i ) );
        }
    }

    /**
     * A variety of growths, assert that doesnt grow if already large enough,
     * assert that growth copies correctly, and that follows strategy correctly.
     */
    @Test
    public void growthTest()
    {
        if( template ) return;
        loadTest();
        store.grow( TEST_SIZE / 2 );
        TestCase.assertEquals( TEST_SIZE, store.getCapacity() );
        store.grow( TEST_SIZE );
        TestCase.assertEquals( TEST_SIZE, store.getCapacity() );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( IntValueConverter._key_FromInt( i ), store.getValue( i ) );
        }

        store.grow( TEST_SIZE + 1 );
        TestCase.assertEquals( TEST_SIZE * 2, store.getCapacity() );
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            TestCase.assertEquals( IntValueConverter._key_FromInt( i ), store.getValue( i ) );
        }
        store = new ColStorageArray_KeyTypeName_( TEST_SIZE, GrowthStrategy.toExactSize );
        store.grow( TEST_SIZE + 1 );
        TestCase.assertEquals( TEST_SIZE + 1, store.getCapacity() );
    }

    /**
     * Test copying empty and copying a loaded store
     */
    @Test
    public void copyTest()
    {
        if (template) return;
        store = new ColStorageArray_KeyTypeName_( TEST_SIZE, GrowthStrategy.toExactSize );
        ColStorageArray_KeyTypeName_ copy = store.getCopy();
        assertEquals( store, copy );
        testLoading();
        copy = store.getCopy();
        assertEquals( store, copy );
    }


    /**
     * Test copy from, assert the correct values are copied, and doesn't overwrite
     * any correct existing data.
     */
    @Test
    public void testCopyFrom()
    {
        if (template) return;
        loadTest();
        ColStorageArray_KeyTypeName_ newStore= new ColStorageArray_KeyTypeName_( 8 );
        for (int i=0; i<8; i++)
        {
            newStore.setValue( IntValueConverter._key_FromInt( 0 ), i );
        }
        store.copyFrom( newStore, 0, 3, 8 );
        for (int i=0; i<3; i++)
        {
            TestCase.assertEquals( IntValueConverter._key_FromInt( i ), store.getValue( i ) );
        }
        for (int i=3; i<11; i++)
        {
            TestCase.assertEquals( IntValueConverter._key_FromInt( 0 ), store.getValue( i ) );
        }
        for (int i=11; i<TEST_SIZE; i++)
        {
            TestCase.assertEquals( IntValueConverter._key_FromInt( i ), store.getValue( i ) );
        }
    }


    protected void assertEquals(ColStorageArray_KeyTypeName_ a, ColStorageArray_KeyTypeName_ b)
    {
        TestCase.assertEquals( a.getType(), b.getType() );
        TestCase.assertEquals( a.getStrategy(), b.getStrategy() );
        TestCase.assertEquals( a.getCapacity(), b.getCapacity() );
        int len = a.data.length;
        for( int i = 0; i < len; i++ )
        {
            TestCase.assertEquals( a.getValue( i ), b.getValue( i ) );
        }
    }


}