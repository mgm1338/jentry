package store.col.storage.block;

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
public class BlockedColStorageFactory extends ColStorageFactory
{
    protected int numInitialBlocks;
    protected GrowthStrategy strategy;

    public BlockedColStorageFactory( GrowthStrategy strategy, int numInitialBlocks )
    {
        this.numInitialBlocks = numInitialBlocks;
    }

    @Override
    public ColStorage getStorage( byte type, int numRows )
    {
        int blockSize = ( numRows > numInitialBlocks ) ? numRows / numInitialBlocks : 1;  //to avoid a 0 block size
        switch( type )
        {
            case Types.Bool:
                return new ColStorageBlockedBool( blockSize, numRows, strategy );
            case Types.Byte:
                return new ColStorageBlockedByte( blockSize, numRows, strategy );
            case Types.Char:
                return new ColStorageBlockedChar( blockSize, numRows, strategy );
            case Types.Short:
                return new ColStorageBlockedShort( blockSize, numRows, strategy );
            case Types.Int:
                return new ColStorageBlockedInt( blockSize, numRows, strategy );
            case Types.Float:
                return new ColStorageBlockedFloat( blockSize, numRows, strategy );
            case Types.Double:
                return new ColStorageBlockedDouble( blockSize, numRows, strategy );
            case Types.Long:
                return new ColStorageBlockedLong( blockSize, numRows, strategy );
            case Types.CharSequence:
                return new ColStorageBlockedCharSequence( blockSize, numRows, strategy );
            case Types.Object:
                return new ColStorageBlockedObject( blockSize, numRows, strategy );
            case Types.Unknown:
            default:
                return null;
        }
    }
}
