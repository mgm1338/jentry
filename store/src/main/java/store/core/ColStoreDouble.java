package store.core;

import core.stub.double;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ColStoreDouble
{
    double getValue(int row);

    void setValue(int row, double val);
}
