package store.schema;

import collections.hash.set.HashSetCharSequence;
import core.Const;
import store.col.Column;
import store.col.ColumnDefinition;
import store.col.ColumnUtils;
import store.col.storage.ColStorageFactory;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class Schema
{

    /**
     * Is the Schema initialized. Column additions and removals may only be made before initialization. After
     * the columns are set, the Schema is locked and storage to the column allocated by {@link #initialize(int)}
     */
    protected boolean initialized = false;
    /**
     * Set of the names of the columns. Must be unique. The handles to these names will be the column's id.
     * Inserting a column of the same name will overwrite the column that was originally inserted.
     */
    protected HashSetCharSequence colNames;
    /** Array of columns, index and/or id of each column is determined by entry in <i>colNames</i> */
    protected Column[] columns;
    /** Number of columns (this is not the size of <i>columns</i>, which may have holes in it */
    protected int numColumns = -1;


    public Schema( ColumnDefinition... defs )
    {
        numColumns = defs.length;
        colNames = new HashSetCharSequence( numColumns );
        columns = new Column[ numColumns ];
        for( int i = 0; i < numColumns; i++ )
        {
            addColumn( defs[ i ] );
        }
    }


    public void initialize( int numRows )
    {
        initialize( numRows, ColStorageFactory.defaultBlockedStorageFactory);
    }

    public void initialize( int numRows, ColStorageFactory factory )
    {
        initialized = true;
        int colsAllocated = 0;
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

    }

    public void addColumn( ColumnDefinition colDef )
    {
        if( initialized )
        {
            throw new IllegalStateException( "Schemas are immutable after initialization" );
        }
        CharSequence name = colDef.getName();
        int entry = colNames.insert( name );
        if( entry == columns.length ) //exact growth, column storage can become massive
        {
            Column[] temp = new Column[ entry + 1 ];
            System.arraycopy( columns, 0, temp, 0, entry );
            columns = temp;
        }
        columns[ entry ] = ColumnUtils.getTypedColumn( entry, colDef.getType(), name );
        numColumns++;
    }

    public void removeColumn( int id )
    {
        if( initialized )
        {
            throw new IllegalStateException( "Schemas are immutable after initialization" );
        }
        colNames.removeByEntry( id );
        columns[ id ] = null;
        numColumns--;
    }

    public void removeColumn( CharSequence name )
    {
        if( initialized )
        {
            throw new IllegalStateException( "Schemas are immutable after initialization" );
        }
        int entry = colNames.getEntry( name );
        if( entry == Const.NO_ENTRY )
            throw new IllegalArgumentException( "Column with name [" + name + "] does not exist." );
        removeColumn( entry );
    }


}
