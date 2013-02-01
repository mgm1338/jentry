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
public class TestUtilsBool
{

    protected static EqualityFunctions.EqualsBool eq = new EqualityFunctions.EqualsBool();


    public static void assertArrayContentsEqual( boolean[] a, boolean[] b )
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

    public static void assertArrayContentsToLen( boolean[] a, boolean[] b, int len )
    {
        if (a.length< len) TestCase.fail("Array a only has length of ["+a.length+"]");
        if (b.length< len) TestCase.fail("Array b only has length of ["+a.length+"]");

        for( int i = 0; i < len; i++ )
        {
            TestCase.assertTrue( "At idx [" + i + "], array a has [" + a[ i ] + "] while arary b has [" + b[ i ] + "]",
                                 eq.equals( a[ i ], b[ i ] ) );
        }
    }

}
