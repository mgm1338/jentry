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

    @Before
    public void setup()
    {
        rowTracker = new RowTrackerBits(0);
        TestCase.assertEquals(0, rowTracker.highWaterMark);
        TestCase.assertEquals(0, rowTracker.numActiveRows);
        rowTracker = new RowTrackerBits( 16 );
    }

    @Test
    public void initTest()
    {
        for (int i=0; i<16; i++)
        {
            rowTracker.addRow( i );
            TestCase.assertEquals( i, rowTracker.getMaxRowId() );
        }
        for (int i=0; i<16; i++)
        {
            TestCase.assertTrue( rowTracker.containsRow( i ) );
        }
        TestCase.assertFalse( rowTracker.containsRow( 17 ) );
        rowTracker.addRow( 17 );
        TestCase.assertTrue( rowTracker.containsRow( 17 ) );
        TestCase.assertEquals( 17, rowTracker.getMaxRowId());


    }


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
        TestCase.assertEquals( 0, rowTracker.getMaxRowId() );
        rowTracker.clear();
        TestCase.assertEquals( 0, rowTracker.getRowCount() );
        TestCase.assertEquals( -1, rowTracker.getMaxRowId() );

        rowTracker.addRow( 1);
        rowTracker.addRow( 99);
        TestCase.assertEquals( 99, rowTracker.getMaxRowId() );
        TestCase.assertEquals( 2, rowTracker.getRowCount() );
    }

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