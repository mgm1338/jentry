package collections.list;

import core.Const;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.scope.ConstructorScope;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class TestMultiSLList
{

    /**
     * Arrays for loaded test data
     * <p/>
     * idx  heads   nexts
     * 0    8       3
     * 1    15      4
     * 2    6       -1
     * 3    9       -1
     * 4    16      5
     * 5    17      -1
     * <p/>
     * 0 = 8->9
     * 2 = 6
     * 1 = 15->16-17
     */

    MultiSLListInt lists;
    MultiSLListInt dataLoadedLists;

    @Before
    public void setup()
    {
        lists = new MultiSLListInt( 8, 16 );
        //we know the set of lists/data, this is example of what constructor
        // parameters mean, note that in this case, 0,1,
        // 2 must be the head values
        dataLoadedLists = new MultiSLListInt( 3, 6 );
        dataLoadedLists.insert( 0, 9 );
        dataLoadedLists.insert( 1, 17 );
        dataLoadedLists.insert( 2, 6 );
        dataLoadedLists.insert( 0, 8 );
        dataLoadedLists.insert( 1, 16 );
        dataLoadedLists.insert( 1, 15 );

        //assert our loading data from the sample load
        int[] expectedHeads = { 8, 15, 6, 9, 16, 17 };
        int[] expectedNexts = { 3, 4, Const.NO_ENTRY, Const.NO_ENTRY, 5,
                                Const.NO_ENTRY };
        for( int i = 0; i < 6; i++ )
        {
            TestCase.assertEquals( dataLoadedLists.heads[ i ],
                                   expectedHeads[ i ] );
            TestCase.assertEquals( dataLoadedLists.nexts[ i ],
                                   expectedNexts[ i ] );

        }
    }

    @Test
    public void testDataLoad()
    {

    }

    @Test
    public void basicAdd()
    {
        TestCase.assertEquals( 0, lists.getNextEntry( 0 ) );
        TestCase.assertEquals( lists.getNextEntryForList( 0, Const.NO_ENTRY ), 0 );
    }

    @Test
    public void growListsAndEntries()
    {
        int nextEntry = 0;
        for( int i = 0; i < 16; i++ )
        {
            for( int j = 0; j < 8; j++ )
            {
                TestCase.assertEquals( lists.getNextEntry( i ), nextEntry );
                nextEntry++;
            }
        }
        TestCase.assertEquals( 16 * 8, lists.getSize() );
    }


    @Test
    public void addOneRemoveTwoError()
    {
        basicAdd();
        lists.removeEntryFromList( 0, 0 );
        TestCase.assertEquals( 0, lists.getSize() );
        try
        {
            lists.removeEntryFromList( 0, 0 );
            TestCase.fail();
        }
        catch( Exception e )
        {

        }

    }


}
