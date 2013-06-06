package store.col.storage;

import core.array.GrowthStrategy;
import store.col.storage.array.*;
import store.col.storage.block.ColStorageBlocked_KeyTypeName_;
import store.col.storage.generic.*;

/**
 * Copyright 6/5/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/5/13
 */
public abstract class ColumnAllocationStrategy
{

    public abstract ColStorage_KeyTypeName_ getStorage_KeyTypeName_( int numRows );

    public abstract ColStorageBool getStorageBool( int numRows );

    public abstract ColStorageByte getStorageByte( int numRows );

    public abstract ColStorageChar getStorageChar( int numRows );

    public abstract ColStorageCharSequence getStorageCharSequence( int numRows );

    public abstract ColStorageDouble getStorageDouble( int numRows );

    public abstract ColStorageFloat getStorageFloat( int numRows );

    public abstract ColStorageInt getStorageInt( int numRows );

    public abstract ColStorageLong getStorageLong( int numRows );

    public abstract ColStorageObject getStorageObject( int numRows );

    public abstract ColStorageShort getStorageShort( int numRows );


    public static final ColumnAllocationStrategy arrayFactoryDoubleGrowth = new ArrayColumnStrategy();
    public static final ColumnAllocationStrategy blockedStorageDoubleGrowth = new BlockedStorageColumnStrategy();



    public static class ArrayColumnStrategy extends ColumnAllocationStrategy
    {

        GrowthStrategy growthStrategy = GrowthStrategy.doubleGrowth;

        public ArrayColumnStrategy()
        {
        }

        public ArrayColumnStrategy( GrowthStrategy growthStrategy )
        {
            this.growthStrategy = growthStrategy;
        }

        @Override
        public ColStorage_KeyTypeName_ getStorage_KeyTypeName_( int numRows )
        {
            return new ColStorageArray_KeyTypeName_( numRows, growthStrategy );
        }

        @Override
        public ColStorageBool getStorageBool( int numRows )
        {
            return new ColStorageArrayBool( numRows, growthStrategy );
        }

        @Override
        public ColStorageByte getStorageByte( int numRows )
        {
            return new ColStorageArrayByte( numRows, growthStrategy );
        }

        @Override
        public ColStorageChar getStorageChar( int numRows )
        {
            return new ColStorageArrayChar( numRows, growthStrategy );
        }

        @Override
        public ColStorageCharSequence getStorageCharSequence( int numRows )
        {
            return new ColStorageArrayCharSequence( numRows, growthStrategy );
        }

        @Override
        public ColStorageDouble getStorageDouble( int numRows )
        {
            return new ColStorageArrayDouble( numRows, growthStrategy );
        }

        @Override
        public ColStorageFloat getStorageFloat( int numRows )
        {
            return new ColStorageArrayFloat( numRows, growthStrategy );
        }

        @Override
        public ColStorageInt getStorageInt( int numRows )
        {
            return new ColStorageArrayInt( numRows, growthStrategy );
        }

        @Override
        public ColStorageLong getStorageLong( int numRows )
        {
            return new ColStorageArrayLong( numRows, growthStrategy );
        }

        @Override
        public ColStorageObject getStorageObject( int numRows )
        {
            return new ColStorageArrayObject( numRows, growthStrategy );
        }

        @Override
        public ColStorageShort getStorageShort( int numRows )
        {
            return new ColStorageArrayShort( numRows, growthStrategy );
        }
    }


    public static class BlockedStorageColumnStrategy extends ColumnAllocationStrategy
    {
        GrowthStrategy growthStrategy = GrowthStrategy.doubleGrowth;

        public BlockedStorageColumnStrategy()
        {
        }

        public BlockedStorageColumnStrategy( GrowthStrategy growthStrategy )
        {
            this.growthStrategy = growthStrategy;
        }

        @Override
        public ColStorage_KeyTypeName_ getStorage_KeyTypeName_( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageBool getStorageBool( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageByte getStorageByte( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageChar getStorageChar( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageCharSequence getStorageCharSequence( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageDouble getStorageDouble( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageFloat getStorageFloat( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageInt getStorageInt( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageLong getStorageLong( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageObject getStorageObject( int numRows )
        {
            return null;
        }

        @Override
        public ColStorageShort getStorageShort( int numRows )
        {
            return null;
        }
    }
}
