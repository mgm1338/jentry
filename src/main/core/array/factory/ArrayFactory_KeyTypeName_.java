package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.DefaultValueProvider;
import core.stub.*;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public abstract class ArrayFactory_KeyTypeName_
{

    public abstract _key_[] ensureArrayCapacity( _key_[] array,
                                                 int minSize,
                                                 _key_ defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract _key_[] grow( _key_[] array, int minSize,
                                  _key_ defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactory_KeyTypeName_ default_key_Provider = new
            ArrayProvider_KeyTypeName_Impl();

    protected static final class ArrayProvider_KeyTypeName_Impl extends
                                                                ArrayFactory_KeyTypeName_
    {

        public _key_[] ensureArrayCapacity( _key_[] array,
                                            int minSize,
                                            _key_ defaultValue,
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
                _key_[] temp = new _key_[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.Default_KeyTypeName_.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public _key_[] grow( _key_[] array, int minSize,
                             _key_ defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactory_KeyTypeName_.class, len,
                                                minSize, Types.Int );
            }
            _key_[] temp = new _key_[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.Default_KeyTypeName_.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public _key_[] alloc( int size )
    {
        return new _key_[ size ];
    }

    public _key_[] alloc( int size, _key_ fillValue )
    {
        _key_[] t = new _key_[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
