package store.col;

import core.Types;
import store.col.storage.StorageTypes;
import store.col.storage.block.ColStorageBlockedBool;
import store.col.storage.generic.ColStorage;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 */
public class ColumnUtils
{

    public static int BLOCK_DIVISOR = 8;


    public static Column getTypedColumn( int id, byte type, CharSequence name )
    {
        switch( type )
        {
            case Types.Bool:
                return new ColumnBool( id, name );
            case Types.Byte:
                return new ColumnByte( id, name );
            case Types.Char:
                return new ColumnChar( id, name );
            case Types.Short:
                return new ColumnShort( id, name );
            case Types.Int:
                return new ColumnInt( id, name );
            case Types.Float:
                return new ColumnFloat( id, name );
            case Types.Double:
                return new ColumnDouble( id, name );
            case Types.Long:
                return new ColumnLong( id, name );
            case Types.CharSequence:
                return new ColumnCharSequence( id, name );
            case Types.Object:
                return new ColumnObject( id, name );
            case Types.Unknown:
            default:
        }
        return null;
    }

    //TODO: need FACTORY that provides storage per type
    public static void setTypedStorage( byte columnType, byte storageType, Column c, int numRows )
    {
        switch( storageType )
        {
            case StorageTypes.Blocked:
                switch( columnType )
                {
                    case Types.Bool:
                        ( ( ColumnBool ) c ).setStorage( new ColStorageBlockedBool( numRows / BLOCK_DIVISOR, numRows ) );
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
                break;

            case StorageTypes.SimpleArray:
                switch( columnType )
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
                break;
        }
    }
}
