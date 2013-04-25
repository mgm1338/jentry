package store.col;

import core.Types;
import core.stub.*;
import store.core.ColStore_ValueTypeName_;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class Column_ValueTypeName_
{
    protected final ColStore_ValueTypeName_ storage;
    protected final int id;

    public Column_ValueTypeName_( ColStore_ValueTypeName_ storage, int id )
    {
        this.storage = storage;
        this.id = id;
    }

    _val_ get_ValueTypeName_(int row)
    {
        return storage.getValue(row);
    }

    void set_ValueTypeName_(_val_ val_, int row)
    {
        storage.setValue(row, val_  );
    }

    byte getType()
    {
        return Types._ValueTypeName_;
    }

}
