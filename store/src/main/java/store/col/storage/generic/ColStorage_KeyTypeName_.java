package store.col.storage.generic;

import core.stub.*;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStorage_KeyTypeName_
{
    /**
     * Get the value for the row desired.
     *
     * @param row the row in the column
     * @return the value
     */
    _key_ getValue( int row );

    /**
     * Set a value for a particular row in the column.
     *
     * @param val value for the row
     * @param row the row index
     */
    void setValue( _key_ val, int row );

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
