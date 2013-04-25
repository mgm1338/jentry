package store.col;

import core.Types;
import core.stub.*;
import store.core.ColStore_KeyTypeName_;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class Column_KeyTypeName_ implements Column
{
    protected final ColStore_KeyTypeName_ storage;
    protected final int id;

    public Column_KeyTypeName_( ColStore_KeyTypeName_ storage, int id )
    {
        this.storage = storage;
        this.id = id;
    }

    public _key_ get_KeyTypeName_(int row)
    {
        return storage.getValue(row);
    }

    public void set_KeyTypeName_(_key_ val_, int row)
    {
        storage.setValue(row, val_  );
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public byte getType()
    {
        return Types._KeyTypeName_;
    }

}
