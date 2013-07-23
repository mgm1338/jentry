package core.array.util;

import core.array.util.masterslave.MasterSlaveSort_KeyTypeName_;
import core.util.comparator.Comparators;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 5/19/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/19/13
 */

public class TestMasterSlaveIntSort
{


//    //TODO:
//    @Before
//    public void setup()
//    {
//        //static method testing, no constructor
//    }
//
//    @Test
//    public void standardTest()
//    {
//        int[] master = new int[]{   10, 8,  5,  3,  2,  26, 7, 90,  1 };
//        int[] slave = new int[]{    9,  8,  7,  6,  5,  4,  3, 2,   1 };
//        MasterSlaveSort_KeyTypeName_.sort( master, new Comparators.IntAsc(), slave );
//        assertArrayContents( master, 1,2,3,5,7,8,10,26,90 );
//        assertArrayContents( slave, 1,5,6,7,3,8,9,4,2 );
//
//    }
//
//    @Test
//    public void shortTest() //less than the 7 that does insertion sort
//    {
//        int[] master = new int[]{     5,  3,  2,  26,  90,  1 };
//        int[] slave = new int[]{      7,  6,  5,  4,   2,   1 };
//        MasterSlaveSort_KeyTypeName_.sort( master, new Comparators.IntAsc(), slave );
//        assertArrayContents( master, 1,2,3,5,26,90 );
//        assertArrayContents( slave, 1,5,6,7,4,2 );
//    }
//
//    @Test
//    public void reallyBigTest() //over 40
//    {
//        int[] master = new int[50];
//        int[] slave = new int[50];
//        //arrays going from 100-51, and then 0-49
//        for( int i = 0; i < 50; i++ )
//        {
//            master[i] = 100-i;
//            slave[i] =  i;
//        }
//        MasterSlaveSort_KeyTypeName_.sort( master, new Comparators.IntAsc(), slave );
//        //now completely flipped, the swapping now makes the slave start at 49, while master starts at 51
//        for( int i = 1; i <= 50; i++ )
//        {
//            TestCase.assertEquals( slave[i-1], 50-i );
//            TestCase.assertEquals( master[i-1], 50+i );
//
//        }
//    }
//
//
//    private void assertArrayContents( int[] array, int... values )
//    {
//        int len = values.length;
//        for( int i = 0; i < len; i++ )
//        {
//            TestCase.assertEquals( "Expected array value of " + values[ i ] + ", but was actually " + array[ i ],
//                                   values[ i ],
//                                   array[ i ] );
//        }
//    }


}