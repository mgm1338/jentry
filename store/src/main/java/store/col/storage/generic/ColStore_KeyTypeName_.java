package store.col.storage.generic;

import core.stub.*;
import store.col.storage.generic.*;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStore_KeyTypeName_ extends TypedColumnStorage
{
    _key_ getValue(int row);

    void setValue(_key_ val, int row);

}
