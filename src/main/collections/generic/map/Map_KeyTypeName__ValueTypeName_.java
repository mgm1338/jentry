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
    public int containsKey(_key_ key);

    /**
     *
     *
     * @param key
     * @param value
     * @return
     */
    public int insert(_key_ key, _val_ value);

    public int remove(_key_ key);

    public _val_ get(_key_ key, _val_ nullValue);
}
