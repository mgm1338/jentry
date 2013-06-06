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

        TestCase.assertEquals( new ByteBlock("baz".getBytes(), 0, 3), testObj.getByteBlock( 2, null ));
        TestCase.assertEquals( "baz", testObj.getByteBlock( 2 ));


    }


}