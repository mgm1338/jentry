package collections;

import core.stub.*;

import java.util.Collection;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p>Basic Collection with type Object</p>
 * <p/>
 * <p>Upon insertion, a contract is made between the user and implementation
 * of a Jentry Collection that a compact int will be returned. Compact
 * means that ints will be returned in a consecutive order starting with 0.
 * When items are removed, their ints are re-used. One may use these ints
 * to quickly retrieve the inserted values.</p>
 * <p/>
 * <p>The compactness of these int 'handles' means that parallel
 * information can be stored in an array directly, without wasting space.
 * The retrieval of this information is usually a direct
 * array access.
 * </p>
 * <p>This pattern uses more space in storing the objects, however the
 * compact-ness usually ends up saving space for related structures, and
 * like most array-based collections, can out-perform JDK structures at large
 * sizes and when garbage collection is taken into account.
 * </p>
 */
public interface CollectionObject extends collections.Collection
{

    /**
     * Entry where the collection contains <i>value</i>, returns
     * Const.NO_ENTRY (-1) if the item is not in the collection.
     *
     * @param value the value
     * @return entry or Const.NO_ENTRY (-1)
     */
    int contains( Object value );

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
    int insert( Object value );

    /**
     * Remove a value from the collection. Will return true if the item
     * is removed, will return false if the item does not exist or could
     * not be removed.
     *
     * @param value the value to remove
     * @return true if removed the item, false if the item is not in the
     *         collection or it could not be removed.
     */
    boolean remove( Object value );


    /**
     * Get the value inserted for a particular entry. See {@link #insert(Object)}
     * for description of the handles, called entries. For the entry passed
     * here, is guaranteed to return the same value that was returned upon
     * insertion.
     *
     * @param entry the entry for the item
     * @return the value of the item
     */
    Object get( int entry );


}
