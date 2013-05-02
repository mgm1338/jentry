package store.col.storage.simple;

import core.Types;
import core.annotations.UncheckedArray;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.*;
import store.col.storage.generic.ColStore_ValueTypeName_;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 */
public class ColStoreArray_ValueTypeName_ implements ColStore_ValueTypeName_
{
    public static final int DEFAULT_INIT_SIZE = 128;
    _val_[] data;

    public ColStoreArray_ValueTypeName_()
    {
        this( DEFAULT_INIT_SIZE );
    }

    public ColStoreArray_ValueTypeName_( int initialSize )
    {
        data = new _val_[ initialSize ];
    }


    @UncheckedArray
    @Override
    public _val_ getValue( int row )
    {
        return data[ row ];
    }

    @UncheckedArray
    @Override
    public void setValue( int row, _val_ val )
    {
        data[ row ] = val;
    }

    @Override
    public byte getType()
    {
        return Types._ValueTypeName_;
    }

    @Override
    public void grow( int minSize, GrowthStrategy strategy )
    {
        int curSize = data.length;
        int newSize = strategy.growthRequest( curSize, minSize );
        if (curSize==newSize) throw new ArrayGrowthException( this.getClass(), curSize, newSize, getType() );
        _val_[] temp = new _val_[newSize];
        System.arraycopy( data, 0, temp, 0, curSize );
        data = temp;
    }
}
