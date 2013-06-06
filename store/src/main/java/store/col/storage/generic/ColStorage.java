package store.col.storage.generic;

/**
 * Copyright 6/6/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/6/13
 */
public interface ColStorage
{

    /**
     * Grow the Storage to be able to store at least <i>minSize</i> number of elements.
     *
     * @param minSize the minimum number of elements
     */
    void checkGrowth( int minSize );

    /**
     * Return the current capacity of the store, in elements.
     *
     * @return the capacity of the store
     */
    int getCapacity();


    /**
     * Return the Jentry type of the storage that is being held. See {@link core.Types}
     *
     * @return the type
     */
    byte getType();
}
