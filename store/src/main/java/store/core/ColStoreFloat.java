package store.core;

import core.stub.float;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreFloat
{
    float getValue(int row);

    void setValue(int row, float val);
}
