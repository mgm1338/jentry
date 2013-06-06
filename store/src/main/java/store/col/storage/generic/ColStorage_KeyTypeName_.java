package store.col.storage.generic;

import core.stub.*;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStorage_KeyTypeName_ extends ColStorage
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
     * Return a deep copy of this store.
     *
     * @return a copy of the store
     */
    ColStorage_KeyTypeName_ getCopy();

    /**
     * Copy a portion (or all) data from <i>source</i> to this store. Starting copying
     * at index <i>srcPos</i> in the source, to <i>destPos</i> in this store for <i>length</i>
     * items.
     *
     * @param source  source of the data to copy from
     * @param srcPos  index in the source to start copying from
     * @param destPos index in <i>this</i> to start copying to
     * @param length  number of items to copy
     */
    void copyFrom( ColStorage_KeyTypeName_ source, int srcPos, int destPos, int length );

}
