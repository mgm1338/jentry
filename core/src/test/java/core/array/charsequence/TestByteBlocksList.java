package core.array.charsequence;

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


}