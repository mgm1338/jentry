package collections.generic.map;

import collections.generic.Collection;
import core.stub.*;

/**
 * Copyright 2/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/27/13
 */
public interface Map_KeyTypeName__ValueTypeName_ extends Collection
{
    /**
     * Entry where the collection contains <i>key</i>, returns
     * Const.NO_ENTRY (-1) if the item is not in the collection.
     *
     * @param key the value
     * @return the entry where our key is, -1 otherwise
     */
    public int containsKey( _key_ key );

    /**
     * Insert a key/value pair into the map. The key must be unique, while a map may contain many duplicate values.
     * When inserting a new value for the same key, the old value will be replaced. The resulting entry
     * will be the location of both the key and value in their respective arrays.
     *
     * @param key   key the key of the pair
     * @param value value the value associated with the key
     * @return the entry of both the key and value
     */
    public int insert( _key_ key, _val_ value );

    /**
     * Remove the key/value pair using the key value. As the keys are unique, this will remove at most one pair.
     * The int returned will either be the entry where the key and value are removed or -1 if the key does not exist.
     *
     * @param key the key we will use to search for the key/value pair
     * @return the entry of the pair removed, or -1 otherwise
     */
    public int remove( _key_ key );

    /**
     * Retrieve the value at the entry. If the key does not exist, we will return
     * the provided <i>nullValue</i> (will default to null for Object-based maps, however
     * primitives cannot return null).
     *
     * @param key       the key to retrieve the value for
     * @param nullValue the null value to return if they key does not exist
     * @return the value associated with the key, or <i>nullValue</i> otherwise
     */
    public _val_ get( _key_ key, _val_ nullValue );
}
