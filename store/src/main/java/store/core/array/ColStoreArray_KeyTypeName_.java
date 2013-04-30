package store.core.array;

import core.Types;
import core.annotations.UncheckedArray;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.*;
import store.core.ColStore_KeyTypeName_;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 */
public class ColStoreArray_KeyTypeName_ implements ColStore_KeyTypeName_
{
    public static final int DEFAULT_INIT_SIZE = 128;
    _key_[] data;

    public ColStoreArray_KeyTypeName_()
    {
        this( DEFAULT_INIT_SIZE );
    }

    public ColStoreArray_KeyTypeName_( int initialSize )
    {
        data = new _key_[ initialSize ];
    }


    @UncheckedArray
    @Override
    public _key_ getValue( int row )
    {
        return data[ row ];
    }

    @UncheckedArray
    @Override
    public void setValue( int row, _key_ val )
    {
        data[ row ] = val;
    }

    @Override
    public byte getType()
    {
        return Types._KeyTypeName_;
    }

    @Override
    public void grow( int minSize, GrowthStrategy strategy )
    {
        int curSize = data.length;
        int newSize = strategy.growthRequest( curSize, minSize );
        if (curSize==newSize) throw new ArrayGrowthException( this.getClass(), curSize, newSize, getType() );
        _key_[] temp = new _key_[newSize];
        System.arraycopy( data, 0, temp, 0, curSize );
        data = temp;
    }
}
