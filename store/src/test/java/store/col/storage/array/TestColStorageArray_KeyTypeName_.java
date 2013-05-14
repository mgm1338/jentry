package store.col.storage.array;

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
    }

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


}