package collections.generic.heap;

import collections.generic.Collection;
import core.stub.*;
import core.util.comparator.Comparator_KeyTypeName_;

/**
 * Copyright 3/2/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 3/2/13
 * <p/>
 * <p>Interface describing the limited methods for a Heap. The methods are limited to constrain the heap
 * for its intended purpose of maintaining a sorted order of keys.</p>
 * <p/>
 * <p>Finding an item in a heap has been removed to differentiate the Heap from a basic tree, where searching
 * is an efficient operation. Although the heap has a similar structure, the loose constraint of the parent/child
 * relationship causes all search operations to be linear (O(n)), and not O(log(n)), as one may expect.
 * </p>
 * <p/>
 * <p>Searching for items should be done through Hashing the item and using either a HashSet or Map. A Jentry-style
 * AVL binary tree has been designed in the past, however was not used enough to warrant it. TODO: still true?</p>
 */
public interface Heap_KeyTypeName_ extends Collection
{
    /**
     * Return the value of the 'greatest' value of the Heap. The 'greatest is determined by the
     * {@link Comparator_KeyTypeName_} we are using. See {@link core.util.comparator.Comparators}, for instance,
     * if we are using {@link core.util.comparator.Comparators.IntAsc}, we will determine that 3 is 'greater' than 4,
     * for we assume that the standard item 3 while typically considered less than 4, is actually greater for this
     * sorting. See the test classes for more examples.
     *
     * @return the greatest element value
     */
    _key_ peek();

    /**
     * Remove the top item in the heap. This will remove the 'greatest' element, if any elements exist. If no
     * items exist, will do nothing. Calling this item consecutively will perform a HeapSort, which has
     * the advantage of other log(n) sort operations using relatively little space.
     */
    void removeGreatest();

    /**
     * Insert an item into the heap. The act of insertion, like other Jentry collections, will return an
     * entry that can be used to retrieve the item.
     *
     * @param key the key to insert
     * @return the entry of insertion
     */
    int insert( _key_ key );

    /**
     * Removes an item at the entry specified. As noted earlier, the Heap is not good at searching for items,
     * so the entry must be provided for removal (as opposed to the value). It necessary to store the
     * upon insertion (see {@link #insert(core.stub._key_)} to use this method, for searching for the entry
     * is intentionally not implemented.
     *
     * @param entry the entry of the item to remove
     * @return the passed entry if the item has been successfully removed, -1 if the entry did not exist
     */
    int remove( int entry );

    /**
     * Getter for the {@link Comparator_KeyTypeName_} this heap is using. This follows the same convention
     * of the {@link java.util.Comparator}, and many of the common comparators are provided in
     * {@link core.util.comparator.Comparators} for use in sorting primitives.
     *
     * @return the {@link Comparator_KeyTypeName_}
     */
    Comparator_KeyTypeName_ getCmp();
}
