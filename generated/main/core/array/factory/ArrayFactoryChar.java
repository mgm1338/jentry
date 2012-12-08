package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.char;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class ArrayFactoryChar
{
    public static final ArrayFactoryChar charProvider = new
            ArrayFactoryChar();

    protected static final class ArrayProviderchar
    {
        public static char[] ensureArrayCapacity( char[] array, int minSize,
                                                 int defaultValue,
                                                 GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayProviderchar.class, len,
                                                    minSize, Types.Int );
                }
                char[] temp = new char[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != 0 )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public static char[] grow( char[] array, int minSize,
                                  int defaultValue,
                                  GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayProviderchar.class, len,
                                                minSize, Types.Int );
            }
            char[] temp = new char[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != 0 )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }
}
