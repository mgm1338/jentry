package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.byte;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class ArrayFactoryByte
{
    public static final ArrayFactoryByte byteProvider = new
            ArrayFactoryByte();

    protected static final class ArrayProviderbyte
    {
        public static byte[] ensureArrayCapacity( byte[] array, int minSize,
                                                 int defaultValue,
                                                 GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayProviderbyte.class, len,
                                                    minSize, Types.Int );
                }
                byte[] temp = new byte[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != 0 )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public static byte[] grow( byte[] array, int minSize,
                                  int defaultValue,
                                  GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayProviderbyte.class, len,
                                                minSize, Types.Int );
            }
            byte[] temp = new byte[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != 0 )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }
}
