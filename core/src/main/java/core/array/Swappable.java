package core.array;

import core.annotations.UncheckedArray;

/**
 * Copyright 7/22/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 7/22/13
 *
 * Interface that describes a collection that can swap its items.
 */
public interface Swappable
{
    /**
     *  Perform a swap of the item in index a, with the item in index b (will usually
     *  be used in the case of an array).
     *
     * @param a first index
     * @param b second index
     */
    @UncheckedArray
    public void swap(int a, int b);

    int getLength();
}
