package store.col.storage.simple;

import core.Types;
import core.annotations.UncheckedArray;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.*;
import store.col.storage.generic.ColStorage_KeyTypeName_;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 */
public class ColStorageArray_KeyTypeName_ implements ColStorage_KeyTypeName_
{

    protected final GrowthStrategy strategy;
    protected _key_[] data;


    public ColStorageArray_KeyTypeName_( int initialSize )
    {
        this( initialSize, GrowthStrategy.doubleGrowth );
    }

    public ColStorageArray_KeyTypeName_( int initialSize, GrowthStrategy strategy )
    {
        data = new _key_[ initialSize ];
        this.strategy = strategy;
    }


    @UncheckedArray
    @Override
    public _key_ getValue( int row )
    {
        return data[ row ];
    }

    @UncheckedArray
    @Override
    public void setValue( _key_ val, int row )
    {
        data[ row ] = val;
    }

    @Override
    public byte getType()
    {
        return Types._KeyTypeName_;
    }

    @Override
    public void grow( int minSize )
    {
        int curSize = data.length;
        int newSize = strategy.growthRequest( curSize, minSize );
        if( curSize == newSize ) throw new ArrayGrowthException( this.getClass(), curSize, newSize, getType() );
        _key_[] temp = new _key_[ newSize ];
        System.arraycopy( data, 0, temp, 0, curSize );
        data = temp;
    }
}
