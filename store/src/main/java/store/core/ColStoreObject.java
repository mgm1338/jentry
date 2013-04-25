package store.core;

import core.stub.Object;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreObject
{
    Object getValue(int row);

    void setValue(int row, Object val);
}
