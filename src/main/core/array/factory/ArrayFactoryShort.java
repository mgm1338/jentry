package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.DefaultValueProvider;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public abstract class ArrayFactoryShort
{

    public abstract short[] ensureArrayCapacity( short[] array,
                                                 int minSize,
                                                 short defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract short[] grow( short[] array, int minSize,
                                  short defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryShort defaultshortProvider = new
            ArrayProviderShortImpl();

    protected static final class ArrayProviderShortImpl extends
                                                                ArrayFactoryShort
    {

        public short[] ensureArrayCapacity( short[] array,
                                            int minSize,
                                            short defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryShort.class, len,
                                                    minSize, Types.Int );
                }
                short[] temp = new short[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultShort.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public short[] grow( short[] array, int minSize,
                             short defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryShort.class, len,
                                                minSize, Types.Int );
            }
            short[] temp = new short[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultShort.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public short[] alloc( int size )
    {
        return new short[ size ];
    }

    public short[] alloc( int size, short fillValue )
    {
        short[] t = new short[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
