package store.core;

import core.stub.short;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreShort
{
    short getValue(int row);

    void setValue(int row, short val);
}
