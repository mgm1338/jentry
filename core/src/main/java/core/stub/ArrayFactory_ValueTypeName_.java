package core.stub;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
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
                    throw new ArrayGrowthException( ArrayFactory_ValueTypeName_.class, len,
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
                throw new ArrayGrowthException( ArrayFactory_ValueTypeName_.class, len,
                                                minSize, Types.Int );
            }
            _val_[] temp = new _val_[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
//            if (defaultValue != DefaultValueProvider.Default_ValueTypeName_.getValue ())
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

    public _val_[] alloc( int size, _val_ fillValue )
    {
        _val_[] t = new _val_[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}