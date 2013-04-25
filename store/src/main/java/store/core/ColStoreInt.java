package store.core;

import core.stub.int;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreInt
{
    int getValue(int row);

    void setValue(int row, int val);
}
