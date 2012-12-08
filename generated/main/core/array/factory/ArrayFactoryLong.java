package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.long;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class ArrayFactoryLong
{
    public static final ArrayFactoryLong longProvider = new
            ArrayFactoryLong();

    protected static final class ArrayProviderlong
    {
        public static long[] ensureArrayCapacity( long[] array, int minSize,
                                                 int defaultValue,
                                                 GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayProviderlong.class, len,
                                                    minSize, Types.Int );
                }
                long[] temp = new long[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != 0 )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public static long[] grow( long[] array, int minSize,
                                  int defaultValue,
                                  GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayProviderlong.class, len,
                                                minSize, Types.Int );
            }
            long[] temp = new long[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != 0 )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }
}
