package collections.list;

import core.Const;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class TestMultiSLList
{




    MultiListInt lists;
    MultiListInt dataLoadedLists;

    /**
     * Loaded test data:
     * <pre>
     * idx  heads   nexts
     * 0    8        3
     * 1    15       4
     * 2    6       -1
     * 3    9       -1
     * 4    16       5
     * 5    17      -1
     *
     * listHead     contents
     * 0          = 8->9
     * 2          = 6
     * 1          = 15->16-17
     * </pre>
     */
    @Before
    public void setup()
    {
        lists = new MultiListInt( 8, 16 );
        //we know the set of lists/data, this is example of what constructor
        // parameters mean, note that in this case, 0,1,
        // 2 must be the head values
        dataLoadedLists = new MultiListInt( 3, 6 );
        dataLoadedLists.insert( 0, 9 );
        dataLoadedLists.insert( 1, 17 );
        dataLoadedLists.insert( 2, 6 );
        dataLoadedLists.insert( 0, 8 );
        dataLoadedLists.insert( 1, 16 );
        dataLoadedLists.insert( 1, 15 );

        assertListContents( dataLoadedLists, 0, 9, 8 );
        assertListContents( dataLoadedLists, 1, 17, 16, 15 );
        assertListContents( dataLoadedLists, 2, 6 );


    }


    protected void assertListContents( MultiListInt list,
                                       int head, int... values )
    {
        //collect list
        int ptr = head;
        ArrayList contents = new ArrayList();
        while( ptr != -1 && list.heads[ ptr ] != Const.NO_ENTRY )
        {
            contents.add( list.heads[ ptr ] );
            ptr = ( list.nexts.length > ptr ) ? list.nexts[ ptr ] : -1;

        }

        if( values == null ) values = new int[ 0 ];
        if( contents.size() != values.length )
        {
            TestCase.fail( "Do not have the same number of items in the list " +
                           "for head value of [" + head + "], " +
                           "we expected [" + values.length + "] however there " +
                           "were [" + contents.size() + "]" );
        }

        int len = values.length;
        for( int i = 0; i < len; i++ )
        {

            if( !contents.contains( values[ i ] ) )
            {

                TestCase.fail( "For head value [" + head + "] we expected to " +
                               "find value [" + values[ i ] + "] but did not " +
                               "find it" );
            }
        }
    }

    /**
     * Testing loading the data with the initial contents in {@link #setup }
     */
    @Test
    public void testDataLoad()
    {

    }


    @Test
    public void removeFromSingleList()
    {
        TestCase.assertTrue( dataLoadedLists.getSize() == 6 );
        dataLoadedLists.removeEntryFromList( 2, 6 );
        //free list ptr cannot be head ptr
        TestCase.assertTrue( dataLoadedLists.freeListPtr == Const.NO_ENTRY );
        assertListContents( dataLoadedLists, 2, null );
        TestCase.assertTrue( dataLoadedLists.getSize() == 5 );


    }


    @Test
    public void removeFirstEntryForList()
    {
        TestCase.assertTrue( dataLoadedLists.getSize() == 6 );
        dataLoadedLists.removeEntryFromList( 0, 8 );
        TestCase.assertTrue( dataLoadedLists.freeListPtr == Const.NO_ENTRY );
        assertListContents( dataLoadedLists, 0, 9 );
        TestCase.assertTrue( dataLoadedLists.getSize() == 5 );
    }

    @Test
    public void removeLastEntryForList()
    {
        TestCase.assertTrue( dataLoadedLists.getSize() == 6 );
        dataLoadedLists.removeEntryFromList( 0, 9 );
        TestCase.assertTrue( dataLoadedLists.freeListPtr == Const.NO_ENTRY );
        assertListContents( dataLoadedLists, 0, 8 );
        TestCase.assertTrue( dataLoadedLists.getSize() == 5 );
    }

    @Test
    public void removeMiddleFromList()
    {
        TestCase.assertTrue( dataLoadedLists.getSize() == 6 );
        dataLoadedLists.removeEntryFromList( 1, 16 );
        TestCase.assertTrue( dataLoadedLists.freeListPtr == Const.NO_ENTRY );
        assertListContents( dataLoadedLists, 1, 15, 17 );
        TestCase.assertTrue( dataLoadedLists.getSize() == 5 );
    }

    @Test
    public void removeFromWrongHead()
    {
        try
        {
            dataLoadedLists.removeEntryFromList( 0, 16 );
            TestCase.fail();

        }
        catch( RuntimeException e )
        {

        }
    }

    @Test
    public void removeNonexistentValue()
    {
        try
        {
            dataLoadedLists.removeEntryFromList( 0, 0 );
            TestCase.fail();

        }
        catch( RuntimeException e )
        {

        }
    }

    //free list ptr test, make list with exact sizes, remove one at end
    //then add it, assert didnt grow or anything


}
