package store.core.factory;

import store.core.TypedColumnStorage;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 */
public interface ColumnStorageFactory
{
    public TypedColumnStorage getTypedStorage(byte type);
}
