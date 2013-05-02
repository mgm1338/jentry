package store.col;

import core.Types;
import core.stub.*;
import store.col.storage.generic.ColStore_ValueTypeName_;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class Column_ValueTypeName_ implements Column
{
    protected final ColStore_ValueTypeName_ storage;
    protected final int id;
    protected final CharSequence name;

    public Column_ValueTypeName_( ColStore_ValueTypeName_ storage, int id, CharSequence name )
    {
        this.storage = storage;
        this.id = id;
        this.name = name;
    }

    public _val_ get_ValueTypeName_(int row)
    {
        return storage.getValue(row);
    }

    public void set_ValueTypeName_(_val_ val_, int row)
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
        return Types._ValueTypeName_;
    }

    @Override
    public CharSequence getName()
    {
        return name;
    }
}
