package collections;

import core.stub.*;

import java.util.Collection;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * Similar to the JDK Collection, this will describe the collections
 * in the Jentry collections package. Very similar to the JDK collections,
 * with the one addition of entries.
 * <p/>
 * <p>Upon insertion, a contract is made between the user and implementation
 * of a Jentry Collection that a compact int will be returned. Compact
 * means that ints will be returned in a consecutive order starting with 0.
 * When items are removed, their ints are re-used. Upon retrieval of these
 * items, one may just use the entry and quickly access the value.</p>
 * <p/>
 * <p>The compactness of these int 'handles' means that parallel
 * information can be stored in an array directly, without wasting space.
 * The retrieval of this information from these ints is usually a direct
 * array access (or maybe one level of indirection), which is very
 * quick. </p>
 * <p>This pattern uses more space in storing the objects, however the
 * compact-ness usually ends up saving space for related structures, and
 * performs many operations much more efficiently than JDK structures.
 * </p>
 * <p>A second advantage to Jentry Collections is their Object
 * 'stingyness', in that it gives users great control over their memory,
 * and it deals with primitives. This becomes a huge advantage vs inserting
 * and removing Objects, which must be cleaned up, for very large collections.
 * Instead of wasting time with Object allocation and garbage cleaning,
 * the collection will be long-lived until it is completely
 * unnecessary.</p>
 */
public interface CollectionShort
{

    /**
     * Entry where the collection contains <i>value</i>, returns
     * Const.NO_ENTRY (-1) if the item is not in the collection.
     *
     * @param value the value
     * @return entry or Const.NO_ENTRY (-1)
     */
    int contains (short value);

    /**
     * Get the current size of the collection.
     *
     * @return the size
     */
    int getSize ();

    /**
     * Is the current collection empty.
     *
     * @return true if collection is empty, false otherwise
     */
    boolean isEmpty ();

    /**
     * <p>
     * Insert an item into the collection. The int returned acts as a handle to
     * the value inserted. This handle can be used to retrieve the item
     * later, and it is guaranteed to return the value inserted.
     * </p>
     * <p/>
     * <p>
     * These int handles will be compact, in a new collection they will start
     * with 0 and increase sequentially, which is useful when associating
     * items in the collection to items in other arrays.
     * </p>
     *
     * @param value the value inserted
     * @return the entry - a handle that can be used to get the value
     */
    int insert (short value);

    /**
     * Remove a value from the collection. Will return true if the item
     * is removed, will return false if the item does not exist or could
     * not be removed.
     *
     * @param value the value to remove
     * @return true if removed the item, false if the item is not in the
     *         collection or it could not be removed.
     */
    boolean remove (short value);

    /**
     * Clear the collection, empty out all of its contents.
     */
    void clear ();

    /**
     * Get the value inserted for a particular entry. See {@link #insert(short)}
     * for description of the handles, called entries. For the entry passed
     * here, is guaranteed to return the same value that was returned upon
     * insertion.
     *
     * @param entry the entry for the item
     * @return the value of the item
     */
    short get (int entry);


}
