package core.array;

import core.stub.IntValueConverter;
import core.stub.*;
import core.util.comparator.Comparators;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.TestUtilsByte;
import util.TestUtilsInt;
import util.TestUtils_KeyTypeName_;

import java.util.Arrays;

/**
 * Copyright 7/26/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 7/26/13
 *
 * Not stressing the sorting test (we are stealing that from Java, testing the arrays stay parallel
 * and that we fail if the arrays are different lengths and such. Simple tests.
 */
public class TestMasterSlaveSort_KeyTypeName_
{
    boolean template = (this.getClass ().getCanonicalName ().contains ("_"));

    _key_[] toSortNormal;
    byte[] byteArray;
    int[] intArray;

    @Before
    public void setup()
    {
        //Sorted = 1,2,4,6,7,9,13,99
        toSortNormal = new _key_[]{
                IntValueConverter._key_FromInt( 6 ),
                IntValueConverter._key_FromInt( 1 ),
                IntValueConverter._key_FromInt( 9 ),
                IntValueConverter._key_FromInt( 2 ),
                IntValueConverter._key_FromInt(13  ),
                IntValueConverter._key_FromInt( 7 ),
                IntValueConverter._key_FromInt( 99 ),
                IntValueConverter._key_FromInt( 4 ),
        };

        // o, b, a, f, r, o, a, b
        byteArray = new byte[]
                {
                        'f', //6
                        'o', //1
                        'o', //9
                        'b', //2
                        'a', //13
                        'r', //7
                        'b', //99
                        'a', //4
                };

        //30, 30, 30, 20, 15, 64, 6, 20
        intArray = new int[]
                {
                        20, //6
                        30, //1
                        64, //9
                        30, //2
                        6, //13
                        15, //7
                        20, //99
                        30, //4
                };

    }


    @Test
    public void errorOnMisMatchedArrays()
    {
        if (template) return;
        try
        {
            MasterSlaveSort_KeyTypeName_.sort( toSortNormal,
                                           Comparators._key_Asc,
                                           new SwappableInt( new int[0] ) );
            TestCase.fail();
        }
        catch(Exception e)
        {
        }
    }

    @Test
    public void sortOfJustMaster() //not sure what use this would ACTUALLY have
    {
        if (template) return;
        MasterSlaveSort_KeyTypeName_.sort( toSortNormal, Comparators._key_Asc,
                                           new SwappableInt(new int[8]) );
        TestUtils_KeyTypeName_.assertArrayContentsSorted( toSortNormal, Comparators._key_Asc );
    }


    @Test
    public void testWrongNumberPassed()
    {
        if (template) return;
        MasterSlaveSort_KeyTypeName_.sort( toSortNormal, 0, 8, Comparators._key_Asc,
                                           1, new SwappableByte( byteArray ),
                                           new SwappableInt( intArray ));

        TestUtils_KeyTypeName_.assertArrayContentsSorted( toSortNormal, Comparators._key_Asc );
        TestUtilsByte.assertArrayContentsEqual( byteArray, new byte[]{
                'o', 'b', 'a', 'f', 'r', 'o', 'a', 'b'
        } );
        TestCase.assertEquals( intArray[0], 20 ); //not sorted, not effected



    }





}
