package store.table.rowtracker;

import java.util.BitSet;

/**
 * Copyright 8/12/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 8/12/13
 * <p/>
 * Implementation of {@link RowTracker}, that will use a Java {@link BitSet} to track the rows. The
 * indices in the BitSet will represent the row ids. Most Operators will use compact ids starting from 0, so
 * this will be the most common type of {@link RowTracker} used.
 */
public class RowTrackerBits implements RowTracker
{
    /** A bitset that will store the active rows */
    BitSet rows;
    /** The number of rows that are currently active in the row tracker */
    int numActiveRows;

    int highWaterMark = -1;

    public RowTrackerBits( int numRows )
    {
        this.highWaterMark = numRows;
        this.rows = new BitSet( numRows );
    }

    /**
     * {@inheritDoc}
     *
     * @return the number of active rows in the tracker
     */
    @Override
    public int getRowCount()
    {
        return numActiveRows;
    }

    /**
     * {@inheritDoc}
     *
     * @param rowId the id of the row
     */
    @Override
    public void addRow( int rowId )
    {
        if( rows.get( rowId ) )
        {
            throw new IllegalArgumentException( "Row already in RowTracker" );
        }
        if( rowId > highWaterMark ) highWaterMark = rowId;
        rows.set( rowId );
        numActiveRows++;
    }

    /**
     * {@inheritDoc}. If we are removing our maximum row, find the new one.
     *
     * @param rowId the id of the row
     */
    @Override
    public void removeRow( int rowId )
    {
        if( rows.get( rowId ) )
        {
            numActiveRows--;
            rows.set( rowId, false );
            if( rowId == highWaterMark )     //removing the high water mark, get a new one
            {
                int testBit = numActiveRows - 1; //must be at least this bit
                int nextBit;
                while( ( nextBit = rows.nextSetBit( testBit ) ) != -1 )
                {
                    testBit = nextBit;
                }
                highWaterMark = testBit;
            }
            return;
        }
        throw new IllegalArgumentException( "Row now in the RowTracker, cannot be removed" );
    }

    /**
     * {@inheritDoc}
     *
     * @param rowId the row id
     * @return true if the rowtracker contains the row, false otherwise
     */
    @Override
    public boolean containsRow( int rowId )
    {
        return rows.get( rowId );
    }

    /**
     * {@inheritDoc}
     *
     * @return the maximum row id. Will return -1 when no rows in the tracker.
     */
    @Override
    public int getMaxRowId()
    {
        return highWaterMark;
    }
}
