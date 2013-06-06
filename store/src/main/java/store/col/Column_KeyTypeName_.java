package store.col;

import core.Types;
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
    protected final ColStorage_KeyTypeName_ storage;
    protected final int id;
    protected final CharSequence name;
    protected ColStorage_KeyTypeName_ data;

    public Column_KeyTypeName_( ColStorage_KeyTypeName_ storage, int id, CharSequence name )
    {
        this.storage = storage;
        this.id = id;
        this.name = name;
    }

    public _key_ get_KeyTypeName_( int row )
    {
        return storage.getValue( row );
    }

    public void set_KeyTypeName_( _key_ val, int row )
    {
        storage.setValue( val, row );
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
    }
}
