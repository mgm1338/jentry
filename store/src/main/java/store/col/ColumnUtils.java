package store.col;

import core.Types;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 */
public class ColumnUtils
{


    public static Column getTypedColumn( int id, byte type, CharSequence name )
    {
        switch( type )
        {
            case Types.Bool:
                break;
            case Types.Byte:
                break;
            case Types.Char:
                break;
            case Types.Short:
                break;
            case Types.Int:
                break;
            case Types.Float:
                break;
            case Types.Double:
                break;
            case Types.Long:
                break;
            case Types.CharSequence:
                break;
            case Types.Object:
                break;
            case Types.Unknown:
                break;
            default:
        }
        return null;
    }
}
