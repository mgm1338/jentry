package store.schema;

import store.col.Column;
import store.col.ColumnDefinition;
import store.col.ColumnUtils;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class Schema
{
    protected static final int DEFAULT_NUM_COLS = 4;

    protected Column[] columns;
    protected int numColumns = -1;
    protected boolean initialized = false;

    public Schema( ColumnDefinition... defs )
    {
        int len = defs.length;
        int initSize = (Math.max( DEFAULT_NUM_COLS, len ));
        columns = new Column[initSize];
        for( int i = 0; i < initSize; i++ )
        {
            columns[i] = ColumnUtils.getTypedColumn( i, defs[ i ].getType(), defs[ i ].getName() );

        }
    }

    public void initialize()
    {

        initialized = true;
    }


    public void addColumn( ColumnDefinition colDef )
    {
        if( initialized )
        {
            throw new IllegalStateException( "Schemas are immutable after initialization" );
        }

    }

    public void removeColumn( int idx)
    {
        if (initialized)
        {
            throw new IllegalStateException( "Schemas are immutable after initialization" );
        }
    }



}
