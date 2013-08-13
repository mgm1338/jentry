package store.table;

import store.col.Column;
import store.col.ColumnUtils;
import store.col.storage.ColStorageFactory;
import store.schema.Schema;
import store.table.change.ChangeQueue;
import store.table.rowtracker.RowTracker;
import store.table.rowtracker.RowTrackerBits;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public abstract class BaseTable
{

    protected Schema schema;
    protected ChangeQueue changeQueue;
    protected RowTracker rowTracker;

    protected int rowCount;
    public int capacity;
    boolean initialized;

    public BaseTable( Schema schema )
    {
        this.schema = schema;
    }

    public void setStorage( int numRows )
    {
        setStorage( numRows, ColStorageFactory.defaultBlockedStorageFactory );
        this.rowTracker = new RowTrackerBits(numRows);
    }

    public void setStorage( int numRows, ColStorageFactory factory )
    {
        if( !initialized )
        {
            initialized = true;
            int colsAllocated = 0;
            Column[] columns = schema.getColumns();
            int numColumns = schema.getNumColumns();
            int len = columns.length;
            Column col;
            for( int i = 0; i < len && colsAllocated != numColumns; i++ )
            {
                col = columns[ i ];
                if( col != null )
                {
                    ColumnUtils.setTypedStorage( col.getType(), col, numRows, factory );
                }
            }
            schema.setLocked( true );
        }
    }


    public abstract int beginAddRow();

    public abstract void endAddRow();

    public abstract void beginChangeRow( int row );

    public abstract void endChangeRow();

    public abstract void removeRow( int row );

}
