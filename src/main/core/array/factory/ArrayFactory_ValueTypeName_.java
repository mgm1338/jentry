package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.DefaultValueProvider;
import core.stub._key_;
import core.stub._val_;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p/>
 * <p/>
 * Stub class
 */
public abstract class ArrayFactory_ValueTypeName_
{


    public abstract _val_[] ensureArrayCapacity( _val_[] array,
                                                 int minSize,
                                                 _val_ defaultValue,
                                                 GrowthStrategy growthStrategy );


    public abstract _val_[] ensureArrayCapacity( _val_[] array,
                                                 int minSize,
                                                 GrowthStrategy growthStrategy );


    public abstract _val_[] grow( _val_[] array, int minSize,
                                  _val_ defaultValue,
                                  GrowthStrategy growthStrategy );


    public static final ArrayFactory_ValueTypeName_ default_ValueTypeName_Provider = new
            ArrayProvider_ValueTypeName_Impl();


    protected static final class ArrayProvider_ValueTypeName_Impl extends
                                                                  ArrayFactory_ValueTypeName_
    {

        public _val_[] ensureArrayCapacity( _val_[] array,
                                            int minSize,
                                            _val_ defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactory_KeyTypeName_.class, len,
                                                    minSize, Types.Int );
                }
                _val_[] temp = new _val_[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                Arrays.fill( temp, len, newSize, defaultValue );
                return temp;
            }
            return array;
        }

        @Override
        public _val_[] ensureArrayCapacity( _val_[] array, int minSize,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactory_ValueTypeName_.class, len,
                                                    minSize, Types.Int );
                }
                _val_[] temp = new _val_[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                return temp;
            }
            return array;
        }


        public _val_[] grow( _val_[] array, int minSize,
                             _val_ defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactory_KeyTypeName_.class, len,
                                                minSize, Types.Int );
            }
            _val_[] temp = new _val_[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.Default_ValueTypeName_.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public _val_[] alloc( int size )
    {
        return new _val_[ size ];
    }

    public _val_[] alloc( int size, _key_ fillValue )
    {
        _val_[] t = new _val_[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
