package store.core;

import core.stub._key_;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStore_KeyTypeName_
{
    _key_ getValue(int row);

    void setValue(int row, _key_ val);
}
