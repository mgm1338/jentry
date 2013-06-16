package store.schema;

import collections.hash.set.HashSetCharSequence;
import core.Const;
import store.col.Column;
import store.col.ColumnDefinition;
import store.col.ColumnUtils;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 *
 * <p>A Schema is the description of a set of Columns. </p>
 *
 * <p>A Schema can only be changed before it has been locked. Locking usually occurs when
 * storage has been allocated for the columns. </p>
 */
public class Schema
{

    /**
     * Is the Schema locked. A schema is normally locked when storage is set for the columns, afterwards
     * no columns can be removed or added.
     */
    protected boolean locked = false;
    /**
     * Set of the names of the columns. Must be unique. The handles to these names will be the column's id.
     * Inserting a column of the same name will overwrite the column that was originally inserted.
     */
    protected HashSetCharSequence colNames;
    /** Array of columns, index and/or id of each column is determined by entry in <i>colNames</i> */
    protected Column[] columns;
    /** Number of columns (this is not the size of <i>columns</i>, which may have holes in it */
    protected int numColumns = -1;


    /**
     * Constructor the the Schema. The constructor is simply a array of {@link ColumnDefinition} items.
     *
     *
     * @param defs
     */
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


    public void addColumn( ColumnDefinition colDef )
    {
        if( locked )
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
        if( locked )
        {
            throw new IllegalStateException( "Schemas are immutable after initialization" );
        }
        colNames.removeByEntry( id );
        columns[ id ] = null;
        numColumns--;
    }

    public void removeColumn( CharSequence name )
    {
        if( locked )
        {
            throw new IllegalStateException( "Schemas are immutable after initialization" );
        }
        int entry = colNames.getEntry( name );
        if( entry == Const.NO_ENTRY )
            throw new IllegalArgumentException( "Column with name [" + name + "] does not exist." );
        removeColumn( entry );
    }


    public Column[] getColumns()
    {
        return columns;
    }

    public int getNumColumns()
    {
        return numColumns;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked( boolean locked )
    {
        this.locked = locked;
    }
}
