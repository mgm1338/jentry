package store.core;

import core.stub.long;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreLong
{
    long getValue(int row);

    void setValue(int row, long val);
}
