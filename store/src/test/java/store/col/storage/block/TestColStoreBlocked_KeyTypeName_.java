package store.col.storage.block;

import core.array.GrowthStrategy;
import core.stub.IntValueConverter;
import core.stub.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 5/7/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/7/13
 */
public class TestColStoreBlocked_KeyTypeName_
{

    boolean template = ( this.getClass().getCanonicalName().contains( "_" ) );

    protected ColStoreBlocked_KeyTypeName_ store;

    @Before
    public void setup()
    {
        store = new ColStoreBlocked_KeyTypeName_( 1024, 1024 );
    }


    public void testSizeBitsInBlock()
    {
        TestCase.assertEquals( 8, store.nextPowerOfTwo( 7 ));
        TestCase.assertEquals( 8, store.nextPowerOfTwo( 8 ));
        TestCase.assertEquals( 1, store.getBitsInBlock( 2 ) );
        TestCase.assertEquals( 2, store.getBitsInBlock( 4 ) );
        TestCase.assertEquals( 4, store.getBitsInBlock( 16 ) );
        TestCase.assertEquals( 5, store.getBitsInBlock( 32 ) );

    }

    //Assert on construction, that block sizes will be the next power of two larger
    @Test
    public void initTest()
    {
        if( template ) return;

        store = new ColStoreBlocked_KeyTypeName_( 1024, 401 );
        TestCase.assertEquals( 1024, store.getBlockSize() ); //4 bits, can represent 0-15 (or 0-F)
        TestCase.assertEquals( 1024, store.getSize() ); //size in blocks, must fit initial size
        store = new ColStoreBlocked_KeyTypeName_( 4096, 1024 ); //block size is 2^12
        TestCase.assertEquals( 4096, store.getSize() ); //size is at least one block


    }

    //Assert growth on the store
    @Test
    public void growthTests()
    {
        if( template ) return;

        //dont grow
        store.grow( 300 );
        store.grow( 1024 );
        TestCase.assertEquals( 1024, store.getSize() );

        store.grow( 1025 );
        store.setValue( IntValueConverter._key_FromInt( 16 ), 1025 );
        TestCase.assertEquals( 2048, store.getSize() ); //default double growth

        //new store with exact size growth
        store = new ColStoreBlocked_KeyTypeName_( 16, 1024, GrowthStrategy.toExactSize );
        store.grow( 1025 );
        store.setValue( IntValueConverter._key_FromInt( 16 ), 1025 );
        TestCase.assertEquals( 1040, store.getSize() ); //growth of one more block

        //checking will not grow here
        store.setValue( IntValueConverter._key_FromInt( 13 ), 1039 );
        TestCase.assertEquals( 1040, store.getSize() ); //growth of one more block

        //bounds check, should grow here
        store.grow( 1041 );
        store.setValue( IntValueConverter._key_FromInt( 14 ), 1040 );
        TestCase.assertEquals( 1056, store.getSize() ); //growth of one more block

        //larger growth, up to double
        store.grow( 2048 );
        store.setValue( IntValueConverter._key_FromInt( 15 ), 2047 );
        TestCase.assertEquals( 2048, store.getSize() );
    }

    /** Basic filling of the store */
    @Test
    public void fillTest()
    {
        if( template ) return;
        //fill values
        for( int i = 0; i < 1024; i++ )
        {
            store.setValue( IntValueConverter._key_FromInt( i % 4 ), i );
        }
        //check all values
        for( int i = 0; i < 1024; i++ )
        {
            TestCase.assertEquals( IntValueConverter._key_FromInt( i % 4 ), store.getValue( i ) );
        }

        //insert same values in a different store with different block size
        ColStoreBlocked_KeyTypeName_ oneBlockStore = new ColStoreBlocked_KeyTypeName_( 1024, 1024 );  //one block
        TestCase.assertEquals( 1024, oneBlockStore.getSize() );
        TestCase.assertEquals( 1024, oneBlockStore.getBlockSize() );
        for( int i = 0; i < 1024; i++ )
        {
            store.setValue( IntValueConverter._key_FromInt( i % 4 ), i );
        }
        //assert equals
        assertSame( store, oneBlockStore, 0, 1024 );
    }

    /** Basic overwriting of values (relatively stupid test) */
    @Test
    public void overrideTest()
    {
        if( template ) return;
        fillTest();
        for( int i = 100; i < 300; i++ )
        {
            store.setValue( IntValueConverter._key_FromInt( 10 ), i );
        }
        assertValues( IntValueConverter._key_FromInt( 10 ), 100, 300 );

    }

    /**
     * Exception case
     */
    @Test
    public void setOutOfBounds()
    {
        if( template ) return;
        store = new ColStoreBlocked_KeyTypeName_( 2, 4 );
        try
        {
            store.setValue( IntValueConverter._key_FromInt( 3 ), 5 );
            TestCase.fail();
        }
        catch( Exception e )
        {
        }
        store.setValue( IntValueConverter._key_FromInt( 3 ), 3 );
    }

    /**
     * Copy tests, will test internals (shhh, dont tell), copy from empty, and full.
     */
    @Test
    public void copyTests()
    {
        if( template ) return;
        ColStoreBlocked_KeyTypeName_ store2 = store.getCopy();
        assertEquals( store, store2 );

        fillTest();
        store2 = store.getCopy();
        assertEquals( store, store2 );
    }



    /** Fill entire set, then fill a block over the bridge of the blocks with a different value, assert values. */
    @Test
    public void fillTests()
    {
        if( template ) return;
        store = new ColStoreBlocked_KeyTypeName_( 1024, 128 );
        store.fill( IntValueConverter._key_FromInt( 2 ) );
        store.fill( IntValueConverter._key_FromInt( 5 ), 10, 20 );

        assertValues( IntValueConverter._key_FromInt( 2 ), 0, 10 );
        assertValues( IntValueConverter._key_FromInt( 5 ), 10, 20 );
        assertValues( IntValueConverter._key_FromInt( 2 ), 20, 128 );
    }


    @Test
    public void copyOverRanges()
    {
        if( template ) return;
        ColStoreBlocked_KeyTypeName_ source = new ColStoreBlocked_KeyTypeName_( 1024, 512 );
        source.fill( IntValueConverter._key_FromInt( 1 ) );

        source.fill( IntValueConverter._key_FromInt( 2 ), 100, 200 );
        source.fill( IntValueConverter._key_FromInt( 3 ), 250, 350 );
        source.fill( IntValueConverter._key_FromInt( 4 ), 350, 450 );

        ColStoreBlocked_KeyTypeName_ copyOfSource = source.getCopy();
        assertEquals( source, copyOfSource );
        copyOfSource.fill( IntValueConverter._key_FromInt( 6 ), 500, 512 );

        assertSame( source, copyOfSource, 0, 500 );

        //copy 0-400 to store
        store.copyFrom( copyOfSource, 0, 0, 400 );
        assertSame(store, copyOfSource, 0, 400);

        //source 100-512
        store.copyFrom( source, 0, 100, 412 );
        assertSame(store, copyOfSource, 0, 100);
        assertSame(store, source, 100, 512);

        //copy 0-512
        store.copyFrom( copyOfSource, 0, 0, 512 );
        assertSame(store, copyOfSource, 0, 512);
    }


    @Test
    public void insertMinMaxValues()
    {
        //TODO:
    }



    protected void assertValues( _key_ value, int startIdx, int endIdx )
    {
        int idxPtr = startIdx;
        while( idxPtr < endIdx ) //asserts up to one before endIdx, as a Java convention
        {
            TestCase.assertEquals( value, store.getValue( idxPtr++ ) );
        }
    }

    protected void assertSame( ColStoreBlocked_KeyTypeName_ a, ColStoreBlocked_KeyTypeName_ b, int startIdx, int endIdx )
    {
        if( a.getSize() < endIdx )
            TestCase.fail( "a is not large enough, it only has size of " + a.getSize() + " which" +
                           "will not hold index " + endIdx );
        if( b.getSize() < endIdx )
            TestCase.fail( "b is not large enough, it only has size of " + b.getSize() + " which" +
                           "will not hold index " + endIdx );

        int idxPtr = startIdx;
        while( idxPtr < endIdx ) //asserts up to one before endIdx, as a Java convention
        {
            TestCase.assertEquals( store.getValue( idxPtr ), store.getValue( idxPtr ) );
            idxPtr++;
        }
    }


    protected void assertEquals( ColStoreBlocked_KeyTypeName_ a, ColStoreBlocked_KeyTypeName_ b )
    {
        TestCase.assertEquals( a.getSize(), b.getSize() );
        TestCase.assertEquals( a.bitsMask, b.bitsMask );
        TestCase.assertEquals( a.growthStrategy, b.growthStrategy );
        TestCase.assertEquals( a.blockSize, b.blockSize );
        TestCase.assertEquals( a.bitsPerBlock, b.bitsPerBlock );
        assertSame( a, b, 0, a.getSize() - 1 );
    }
}
