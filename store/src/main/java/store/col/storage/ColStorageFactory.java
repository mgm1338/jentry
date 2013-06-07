package store.col.storage;

import core.array.GrowthStrategy;
import store.col.storage.array.ArrayColStorageFactory;
import store.col.storage.block.BlockedColStorageFactory;
import store.col.storage.generic.ColStorage;

/**
 * Copyright 6/6/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/6/13
 */
public abstract class ColStorageFactory
{
    public abstract ColStorage getStorage( byte type, int numRows );

    public static final ColStorageFactory defaultArrayStorageFactory = new ArrayColStorageFactory( GrowthStrategy.doubleGrowth );

    public static final ColStorageFactory defaultBlockedStorageFactory = new BlockedColStorageFactory( GrowthStrategy.doubleGrowth,
                                                                                                       8 );

}
