package core.array;

import core.array.Swappable;
import core.stub.*;

/**
 * Copyright 7/22/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 7/22/13
 *
 * Simple class that will wrap an array in the {@link core.array.Swappable} interface.
 *
 */
public class Swappable_KeyTypeName_ implements Swappable
{
    protected _key_[] data;

    public Swappable_KeyTypeName_( _key_[] data )
    {
        this.data = data;
    }

    /**
     * {@inheritDoc}
     *
     * @param a first index
     * @param b second index
     */
    @Override
    public void swap( int a, int b )
    {
        _key_ t = data[ a ];
        data[ a ] = data[ b ];
        data[ b ] = t;
    }

    @Override
    public int getLength()
    {
        return data.length;
    }

    public _key_[] getData()
    {
        return data;
    }


}
