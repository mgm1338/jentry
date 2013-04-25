package store.core;

import core.stub.char;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreChar
{
    char getValue(int row);

    void setValue(int row, char val);
}
