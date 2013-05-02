package store.col.storage.generic;

import core.array.GrowthStrategy;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 */
public interface TypedColumnStorage
{
    public byte getType();

    public void grow(int minSize, GrowthStrategy strategy);
}
