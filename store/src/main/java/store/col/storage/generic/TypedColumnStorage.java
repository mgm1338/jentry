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
    /**
     * Return the Jentry type of the storage that is being held. See {@link core.Types}
     *
     * @return the type
     */
    public byte getType();

    /**
     * Grow the Storage to be able to store at least <i>minSize</i> number of elements.
     *
     * @param minSize the minimum number of elements
     */
    public void grow( int minSize );
}
