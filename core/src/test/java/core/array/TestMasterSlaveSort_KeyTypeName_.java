package core.array;

import core.stub.IntValueConverter;
import core.stub.*;
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
        if (template) return;
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

    @Test
    public void sortOfJustMaster() //not sure what use this would ACTUALLY have
    {
        if (template) return;
        MasterSlaveSort_KeyTypeName_.sort( toSortNormal, new Comparators._KeyTypeName_Asc(),
                                           new SwappableInt(new int[8]) );
    }





}
