package store.col;

import core.Types;
import core.annotations.UncheckedArray;
import core.stub.*;
import store.col.storage.generic.*;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class Column_KeyTypeName_ implements Column
{
    protected ColStorage_KeyTypeName_ data;

    protected final int id;
    protected final CharSequence name;
    boolean initialized = false;

    public Column_KeyTypeName_( int id, CharSequence name )
    {
        this.id = id;
        this.name = name;
    }

    @UncheckedArray
    public _key_ get_KeyTypeName_( int row )
    {
        return data.getValue( row );
    }

    @UncheckedArray
    public void set_KeyTypeName_( _key_ val, int row )
    {
        data.setValue( val, row );
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

    @Override
    public CharSequence getName()
    {
        return name;
    }

    public void setStorage(ColStorage_KeyTypeName_ storage)
    {
        this.data = storage;
        initialized = true;
    }

    public boolean isInitialized()
    {
        return initialized;
    }
}
