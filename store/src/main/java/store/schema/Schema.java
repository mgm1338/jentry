package store.schema;

import store.col.Column;
import store.col.ColumnDefinition;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class Schema
{
    Column[] columns;
    int size = -1;

    public Schema ()
    {
    }

    public Schema(byte... types)
    {

    }

    public Schema(ColumnDefinition... defs)
    {

    }

}
