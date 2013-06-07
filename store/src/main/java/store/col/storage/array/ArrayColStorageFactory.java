package store.col.storage.array;

import core.Types;
import core.array.GrowthStrategy;
import store.col.storage.ColStorageFactory;
import store.col.storage.generic.ColStorage;

/**
 * Copyright 6/6/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/6/13
 */
public class ArrayColStorageFactory extends ColStorageFactory
{
    GrowthStrategy strategy;

    public ArrayColStorageFactory( GrowthStrategy strategy )
    {
        this.strategy = strategy;
    }

    @Override
    public ColStorage getStorage( byte type, int numRows )
    {
        switch( type )
        {
            case Types.Bool:
                return new ColStorageArrayBool( numRows, strategy );
            case Types.Byte:
                return new ColStorageArrayByte( numRows, strategy );
            case Types.Char:
                return new ColStorageArrayChar( numRows, strategy );
            case Types.Short:
                return new ColStorageArrayShort( numRows, strategy );
            case Types.Int:
                return new ColStorageArrayInt( numRows, strategy );
            case Types.Float:
                return new ColStorageArrayFloat( numRows, strategy );
            case Types.Double:
                return new ColStorageArrayDouble( numRows, strategy );
            case Types.Long:
                return new ColStorageArrayLong( numRows, strategy );
            case Types.CharSequence:
                return new ColStorageArrayCharSequence( numRows, strategy );
            case Types.Object:
                return new ColStorageArrayObject( numRows, strategy );
            case Types.Unknown:
            default:
                return null;
        }
    }
}
