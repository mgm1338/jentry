package store.core;

import core.stub.boolean;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreBool
{
    boolean getValue(int row);

    void setValue(int row, boolean val);
}
