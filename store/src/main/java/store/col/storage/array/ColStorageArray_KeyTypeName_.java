package store.col.storage.array;

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
    private int capacity;


    public ColStorageArray_KeyTypeName_( int initialSize )
    {
        this( initialSize, GrowthStrategy.doubleGrowth );
    }

    public ColStorageArray_KeyTypeName_( int initialSize, GrowthStrategy strategy )
    {
        data = new _key_[ initialSize ];
        this.strategy = strategy;
        this.capacity = initialSize;
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
        if( capacity >= minSize ) return;
        int newSize = strategy.growthRequest( capacity, minSize );
        if( capacity == newSize ) throw new ArrayGrowthException( this.getClass(), capacity, newSize, getType() );
        _key_[] temp = new _key_[ newSize ];
        System.arraycopy( data, 0, temp, 0, capacity );
        data = temp;
        capacity = newSize;
    }

    /**
     * Return a deep copy of this store.
     *
     * @return a copy of the store
     */
    @Override
    public ColStorageArray_KeyTypeName_ getCopy()
    {
        ColStorageArray_KeyTypeName_ copy = new ColStorageArray_KeyTypeName_( this.capacity, this.strategy );
        System.arraycopy( data, 0, copy.data, 0, this.capacity );
        return copy;
    }

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
    @UncheckedArray
    @Override
    public void copyFrom( ColStorage_KeyTypeName_ source, int srcPos, int destPos, int length )
    {
        for( int i = 0; i < length; i++ )
        {
            this.setValue( source.getValue( srcPos++ ), destPos++ );
        }
    }

    @Override
    public int getCapacity()
    {
        return capacity;
    }

    public GrowthStrategy getStrategy()
    {
        return strategy;
    }
}
