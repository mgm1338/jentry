package store.table;

import store.schema.Schema;
import store.table.change.ChangeQueue;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class BaseTable
{

    protected Schema schema;
    protected ChangeQueue changeQueue;
    protected int rowCount;
    public int capacity;

    public BaseTable( Schema schema )
    {
        this.schema = schema;
    }

    public int beginAddRow()
    {
        return rowCount++;
    }

    public void endAddRow()
    {
    }

    public void beginChangeRow(int row)
    {
    }

    public void endChangeRow()
    {
    }

    public void removeRow(int row)
    {
    }

}
