package store.table.rowtracker;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 8/12/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 8/12/13
 */

public class TestRowTrackerBits
{

    protected RowTrackerBits rowTracker;

    /** Test initialization */
    @Before
    public void setup()
    {
        rowTracker = new RowTrackerBits( 0 );
        TestCase.assertEquals( 0, rowTracker.highWaterMark );
        TestCase.assertEquals( 0, rowTracker.numActiveRows );
        rowTracker = new RowTrackerBits( 16 );
    }

    /** Initialize the rowtracker, assert max rows and number of rows each time */
    @Test
    public void initTest()
    {
        for( int i = 0; i < 16; i++ )
        {
            TestCase.assertEquals( i, rowTracker.getRowCount() );
            rowTracker.addRow( i );
            TestCase.assertEquals( i, rowTracker.getMaxRowId() );
        }
        for( int i = 0; i < 16; i++ )
        {
            TestCase.assertTrue( rowTracker.containsRow( i ) );
        }
        TestCase.assertFalse( rowTracker.containsRow( 17 ) );
        rowTracker.addRow( 17 );
        TestCase.assertTrue( rowTracker.containsRow( 17 ) );
        TestCase.assertEquals( 17, rowTracker.getMaxRowId() );


    }

    /**
     * Test the high water mark doesnt change when removing all the rows below the max one. Remove that
     * last row and assert that it changes to -1 (same as clear or empty). Clear and then add a much greater
     * row and assert max row id.
     */
    @Test
    public void highWaterMarkTest()
    {
        initTest();
        for( int i = 15; i >= 0; i-- )
        {
            rowTracker.removeRow( i );
            TestCase.assertEquals( 17, rowTracker.getMaxRowId() );
        }
        rowTracker.removeRow( 17 );
        TestCase.assertEquals( -1, rowTracker.getMaxRowId() );
        rowTracker.clear();
        TestCase.assertEquals( -1, rowTracker.getRowCount() );
        TestCase.assertEquals( -1, rowTracker.getMaxRowId() );

        rowTracker.addRow( 1 );
        rowTracker.addRow( 99 );
        TestCase.assertEquals( 99, rowTracker.getMaxRowId() );
        TestCase.assertEquals( 2, rowTracker.getRowCount() );
    }

    /** Recalc the max row on each removal and assert that it is done correctly. */
    @Test
    public void highWaterRecalcTest()
    {
        initTest();
        rowTracker.removeRow( 17 );
        for( int i = 15; i >= 0; i-- )
        {
            TestCase.assertEquals( i, rowTracker.getMaxRowId() );
            rowTracker.removeRow( i );
        }

    }

    /** Try to add the same row twice and fail */
    @Test
    public void doubleAdd()
    {
        rowTracker.addRow( 0 );
        try
        {
            rowTracker.addRow( 0 );
            TestCase.fail();
        }
        catch( Exception e )
        {

        }
    }

    /** Try to remove a row not in the set and fail */
    @Test
    public void removeNotIn()
    {
        try
        {
            rowTracker.removeRow( 0 );
            TestCase.fail();
        }
        catch( Exception e )
        {

        }
    }


}