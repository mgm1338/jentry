package core.charsequence;

import core.charsequence.ByteBlock;
import core.charsequence.ByteBlocksList;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 6/5/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/5/13
 */

public class TestByteBlocksList
{

    protected ByteBlocksList testObj;

    @Before
    public void setup()
    {
        testObj = new ByteBlocksList( 8 );
    }

    /** Common use for this class, loading Strings */
    @Test
    public void loadStringsTest()
    {
        TestCase.assertEquals( 0, testObj.getSize() );
        TestCase.assertEquals( testObj.insert( "foo" ), 0 );
        TestCase.assertEquals( testObj.insert( "bar" ), 1 );
        TestCase.assertEquals( testObj.insert( "baz" ), 2 );
        TestCase.assertEquals( testObj.insert( "quux" ), 3 );
        TestCase.assertEquals( 4, testObj.getSize() );

        TestCase.assertEquals( testObj.getByteBlock( 2 ), "baz" );
    }

    @Test
    public void compactNoRemovesTest()
    {
        TestCase.assertEquals( 0, testObj.getSize() );
        TestCase.assertEquals( testObj.insert( "foo" ), 0 );
        TestCase.assertEquals( testObj.insert( "bar" ), 1 );
        TestCase.assertEquals( testObj.insert( "baz" ), 2 );
        TestCase.assertEquals( testObj.insert( "quux" ), 3 );
        testObj.compact();
        TestCase.assertEquals( testObj.getByteBlock( 0 ), "foo" );
        TestCase.assertEquals( testObj.getByteBlock( 1 ), "bar" );
        TestCase.assertEquals( testObj.getByteBlock( 2 ), "baz" );
        TestCase.assertEquals( testObj.getByteBlock( 3 ), "quux" );
    }

    @Test
    public void simpleGrowthTest()
    {
        testObj = new ByteBlocksList( 1 );
        TestCase.assertEquals( 0, testObj.getSize() );
        TestCase.assertEquals( testObj.insert( "foo" ), 0 );
        TestCase.assertEquals( testObj.insert( "bar" ), 1 );
        TestCase.assertEquals( testObj.insert( "baz" ), 2 );
        TestCase.assertEquals( testObj.insert( "quux" ), 3 );
        TestCase.assertEquals( 4, testObj.getSize() );

        TestCase.assertEquals( testObj.getByteBlock( 2 ), "baz" );
    }

    @Test
    public void simpleCompactTest()
    {
        loadStringsTest();
        //removing valid item "baz"
        testObj.remove( 2 );
        TestCase.assertEquals( null, testObj.getByteBlock( 2 ) );
        TestCase.assertEquals( 3, testObj.getSize() );
        int dataSize = testObj.dataPtr;

        //asserting a bunch of the internal state, we assume that the structure is
        //data      0   1   2   3   4   5    6    7    8    10  11   12   13
        //        {'f','o','o','b','a','r', 'b', 'a', 'z', 'q','u', 'u', 'x'},
        //lens      0   1  2  3
        //         {3 , 3, 0, 4
        //offsets
        //         {0,  3, 6, 10}
        {
            TestCase.assertEquals( testObj.freeListPtr, 1 );
            TestCase.assertEquals( testObj.freeList[0], 2 );
            TestCase.assertEquals( testObj.data[6], (byte)'b' );
            TestCase.assertEquals( testObj.data[7], (byte)'a' );
            TestCase.assertEquals( testObj.data[8], (byte)'z' );
        }
        testObj.compact();
        //we want 'quux' to move down to the space formally occupied by 'baz'
        {
            TestCase.assertEquals( testObj.freeListPtr, 1 );
            TestCase.assertEquals( testObj.freeList[0], 2 );
            TestCase.assertEquals( testObj.data[6], (byte)'q' );
            TestCase.assertEquals( testObj.data[7], (byte)'u' );
            TestCase.assertEquals( testObj.data[8], (byte)'u' );
        }

        TestCase.assertEquals( null, testObj.getByteBlock( 2 ) );
        TestCase.assertEquals( 3, testObj.getSize() );
        //removed space for "baz", should be 3 less
        TestCase.assertEquals( dataSize-3, testObj.dataPtr );

        TestCase.assertEquals( 2, testObj.insert( "newBaz" ) );

        //internal assertions again
        {
            TestCase.assertEquals( testObj.data[10], (byte)'n' );
            TestCase.assertEquals( testObj.data[11], (byte)'e' );
            TestCase.assertEquals( testObj.data[12], (byte)'w' );
        }



    }

    @Test
    public void compactWithFreeListLeft()
    {
        loadStringsTest();
        testObj.remove( 1 );
        testObj.remove( 2 );
        //now its fooquux
        testObj.compact();
        TestCase.assertEquals( testObj.getSize(), 2 );
        TestCase.assertEquals( 1, testObj.insert( "newBar" ) );
        TestCase.assertEquals( 2, testObj.insert( "newBaz" ) );

        TestCase.assertEquals( testObj.getByteBlock( 0 ), "foo" );
        TestCase.assertEquals( testObj.getByteBlock( 1 ), "newBar" );
        TestCase.assertEquals( testObj.getByteBlock( 2 ), "newBaz" );
        TestCase.assertEquals( testObj.getByteBlock( 3 ), "quux" );



    }


}