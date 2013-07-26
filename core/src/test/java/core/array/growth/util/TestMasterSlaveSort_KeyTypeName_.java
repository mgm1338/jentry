package core.array.growth.util;

import core.array.util.masterslave.MasterSlaveSort_KeyTypeName_;
import core.array.util.masterslave.SwappableInt;
import core.stub.IntValueConverter;
import core.stub._key_;
import core.util.comparator.Comparator_KeyTypeName_;
import core.util.comparator.Comparators;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

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

    }


    @Test
    public void errorOnMisMatchedArrays()
    {
        try
        {
        MasterSlaveSort_KeyTypeName_.sort( toSortNormal,
                                           new Comparators._KeyTypeName_Asc() ,
                                           new SwappableInt( new int[0] ) );
            TestCase.fail();
        }
        catch(Exception e)
        {

        }
    }



}
