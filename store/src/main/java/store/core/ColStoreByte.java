package store.core;

import core.stub.byte;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreByte
{
    byte getValue(int row);

    void setValue(int row, byte val);
}
