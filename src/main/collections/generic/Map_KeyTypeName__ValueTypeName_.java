package collections.generic;

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

    public boolean containsKey(_key_ key);

    public int insert(_key_ key, _val_ value);

    public int remove(_key_ key);

    public _val_ get(_key_ key, _val_ nullValue);
}
