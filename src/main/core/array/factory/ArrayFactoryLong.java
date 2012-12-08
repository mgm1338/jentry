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
public abstract class ArrayFactoryLong
{

    public abstract long[] ensureArrayCapacity( long[] array,
                                                 int minSize,
                                                 long defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract long[] grow( long[] array, int minSize,
                                  long defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryLong defaultlongProvider = new
            ArrayProviderLongImpl();

    protected static final class ArrayProviderLongImpl extends
                                                                ArrayFactoryLong
    {

        public long[] ensureArrayCapacity( long[] array,
                                            int minSize,
                                            long defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryLong.class, len,
                                                    minSize, Types.Int );
                }
                long[] temp = new long[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultLong.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public long[] grow( long[] array, int minSize,
                             long defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryLong.class, len,
                                                minSize, Types.Int );
            }
            long[] temp = new long[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultLong.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public long[] alloc( int size )
    {
        return new long[ size ];
    }

    public long[] alloc( int size, long fillValue )
    {
        long[] t = new long[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
