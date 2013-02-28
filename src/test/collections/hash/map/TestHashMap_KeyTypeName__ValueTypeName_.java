package collections.hash.map;

import collections.hash.HashFunctions;
import collections.heap.Heap_KeyTypeName_;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactory_KeyTypeName_;
import core.stub.ArrayFactory_ValueTypeName_;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 2/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/27/13
 */
public class TestHashMap_KeyTypeName__ValueTypeName_
{

    private static final int LARGE_TEST_SIZE = 100000   ;
    private HashMap_KeyTypeName__ValueTypeName_ map;

    @Before
    public void setup()
    {
        HashMap_KeyTypeName__ValueTypeName_ shortCon = new HashMap_KeyTypeName__ValueTypeName_( 8 );
        map =   new HashMap_KeyTypeName__ValueTypeName_( 8, .75,
                                                         ArrayFactory_KeyTypeName_.default_KeyTypeName_Provider,
                                                         ArrayFactoryInt.defaultIntProvider,
                                                         HashFunctions.hashFunction_KeyTypeName_,
                                                         GrowthStrategy.doubleGrowth,
                                                         ArrayFactory_ValueTypeName_.default_ValueTypeName_Provider );


    }



}
