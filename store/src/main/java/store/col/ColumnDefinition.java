package store.col;

import core.Types;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public class ColumnDefinition
{
    byte type = Types.Unknown;
    CharSequence name;

    public ColumnDefinition( byte type, CharSequence name )
    {
        this.type = type;
        this.name = name;
    }

    public byte getType()
    {
        if (type==Types.Unknown)
        {
            throw new IllegalStateException( "Column defintition must specify a well-defined type" );
        }
        return type;
    }

    public CharSequence getName()
    {
        if ( name==null )
        {
            throw new IllegalStateException("Column must have a unique name" );
        }
        return name;
    }
}
