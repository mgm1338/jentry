package store.col;

import core.Types;
import store.col.storage.ColStorageFactory;
import store.col.storage.generic.*;

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

    public static void setTypedStorage( byte columnType, Column c, int numRows, ColStorageFactory factory )
    {
        switch( columnType )
        {
            case Types.Bool:
                ( ( ColumnBool ) c ).setStorage( ( ColStorageBool ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Byte:
                ( ( ColumnByte ) c ).setStorage( ( ColStorageByte ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Char:
                ( ( ColumnChar ) c ).setStorage( ( ColStorageChar ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Short:
                ( ( ColumnShort ) c ).setStorage( ( ColStorageShort ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Int:
                ( ( ColumnInt ) c ).setStorage( ( ColStorageInt ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Float:
                ( ( ColumnFloat ) c ).setStorage( ( ColStorageFloat ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Double:
                ( ( ColumnDouble ) c ).setStorage( ( ColStorageDouble ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Long:
                ( ( ColumnLong ) c ).setStorage( ( ColStorageLong ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.CharSequence:
                ( ( ColumnCharSequence ) c ).setStorage( ( ColStorageCharSequence ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Object:
                ( ( ColumnObject ) c ).setStorage( ( ColStorageObject ) factory.getStorage( columnType, numRows ) );
                break;
            case Types.Unknown:
                break;
            default:
        }
    }
}
