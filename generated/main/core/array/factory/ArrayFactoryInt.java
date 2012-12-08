package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.int;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class ArrayFactoryInt
{
    public static final ArrayFactoryInt intProvider = new
            ArrayFactoryInt();

    protected static final class ArrayProviderint
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
                    throw new ArrayGrowthException( ArrayProviderint.class, len,
                                                    minSize, Types.Int );
                }
                int[] temp = new int[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != 0 )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public static int[] grow( int[] array, int minSize,
                                  int defaultValue,
                                  GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayProviderint.class, len,
                                                minSize, Types.Int );
            }
            int[] temp = new int[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != 0 )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }
}
