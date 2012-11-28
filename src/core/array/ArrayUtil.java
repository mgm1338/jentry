package core.array;

import core.Types;

import java.util.Arrays;

/**
 * Copyright Max Miller
 * 2012
 */
public class ArrayUtil
{
    public static final class ArrayProviderInt
    {
        public static int[] ensureArrayCapacity( int[] array, int minSize,
                                                 int defaultValue,
                                                 GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayProviderInt.class, len,
                                                    minSize, Types.Int );
                }
                int[] temp = new int[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != 0 )
                {
                    Arrays.fill( temp, len, newSize - 1, defaultValue );
                }
                return temp;
            }
            return array;
        }
    }
}
