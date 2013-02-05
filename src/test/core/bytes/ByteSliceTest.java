package core.bytes;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 1/31/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/31/13
 */
public class ByteSliceTest
{

    ByteSlice test;

    @Test
    public void insert()
    {
        int i = test.insert( "This is my string to insert" );
        TestCase.assertEquals( i, 0 );
//        TestCase.assertEquals( new String( test.getEntry( 0, null ) ), "This is my string to insert" );
        TestCase.assertEquals( test.getSize(), 1 );
        TestCase.assertEquals( test.getSizeOfEntry( 0 ), 27 );

        byte[] insertBytes = new byte[]{ 'a', 'b', 'c', 'd' };
        i = test.insert( insertBytes );
        TestCase.assertEquals( i, 1 );
//        TestCase.assertEquals( new String( test.getEntry( 1, null ) ), "abcd" );
        TestCase.assertEquals( test.getSize(), 2 );
        TestCase.assertEquals( test.getSizeOfEntry( 1 ), 4 );

        //insertion of same array twice, ensure not reference
        i = test.insert( insertBytes, 1, 1 );
        TestCase.assertEquals( i, 2 );
//        TestCase.assertEquals( new String( test.getEntry( 2, null ) ), "b" );
        TestCase.assertEquals( test.getSize(), 3 );
        TestCase.assertEquals( test.getSizeOfEntry( 1 ), 1 );

    }

    @Test
    public void compactKeys()
    {
        insert();
        TestCase.assertTrue( test.remove( 1 ) );
        TestCase.assertTrue( test.insert( "New Stuff" ) == 1 );
//        TestCase.assertEquals( new String( test.getEntry( 1, null ) ), "New Stuff" );
        TestCase.assertEquals( test.getSize(), 3 );
    }

    @Test
    public void compactUsesExtraSpaceUntilCompact()
    {
        insert();
        //assert the current pointer for our next bytes is the size of the three previous
        TestCase.assertEquals( test.curPtr, test.getSizeOfEntry( 0 ) + test.getSizeOfEntry( 1 ) + test.getSizeOfEntry( 2
        ) );
        int sizeOfOne = test.getSizeOfEntry( 1 );
        TestCase.assertTrue( test.remove( 1 ) );
        //same size, with remove, we dont 'squish' this until the line after this
        TestCase.assertEquals( test.curPtr, test.getSizeOfEntry( 0 ) + sizeOfOne + test.getSizeOfEntry( 2 ) );
        test.compact();
        TestCase.assertEquals( test.curPtr, test.getSizeOfEntry( 0 ) + test.getSizeOfEntry( 2 ) );
    }
}
