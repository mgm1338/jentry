package util;

import core.stub.*;
import core.util.comparator.EqualityFunctions;
import junit.framework.TestCase;

/**
 * Copyright 1/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/27/13
 *
 * Type specific assertions.
 */
public class TestUtils_KeyTypeName_
{

    protected static EqualityFunctions.Equals_KeyTypeName_ eq = new EqualityFunctions.Equals_KeyTypeName_();


    public static void assertArrayContentsEqual( _key_[] a, _key_[] b )
    {
        TestCase.assertEquals( "Failed Length Equals: a.length=[" + a.length + "] while b.length =[" + b.length + "]"
                , a.length, b.length );
        int len = a.length;
        for( int i = 0; i < len; i++ )
        {
            TestCase.assertTrue( "At idx [" + i + "], array a has [" + a[ i ] + "] while arary b has [" + b[ i ] + "]",
                                 eq.equals( a[ i ], b[ i ] ) );
        }
    }

}
