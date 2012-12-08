package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.float;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class ArrayFactoryFloat
{
    public static final ArrayFactoryFloat floatProvider = new
            ArrayFactoryFloat();

    protected static final class ArrayProviderfloat
    {
        public static float[] ensureArrayCapacity( float[] array, int minSize,
                                                 int defaultValue,
                                                 GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayProviderfloat.class, len,
                                                    minSize, Types.Int );
                }
                float[] temp = new float[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != 0 )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public static float[] grow( float[] array, int minSize,
                                  int defaultValue,
                                  GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayProviderfloat.class, len,
                                                minSize, Types.Int );
            }
            float[] temp = new float[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != 0 )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }
}
