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
public abstract class ArrayFactoryFloat
{

    public abstract float[] ensureArrayCapacity( float[] array,
                                                 int minSize,
                                                 float defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract float[] grow( float[] array, int minSize,
                                  float defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryFloat defaultfloatProvider = new
            ArrayProviderFloatImpl();

    protected static final class ArrayProviderFloatImpl extends
                                                                ArrayFactoryFloat
    {

        public float[] ensureArrayCapacity( float[] array,
                                            int minSize,
                                            float defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryFloat.class, len,
                                                    minSize, Types.Int );
                }
                float[] temp = new float[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultFloat.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public float[] grow( float[] array, int minSize,
                             float defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryFloat.class, len,
                                                minSize, Types.Int );
            }
            float[] temp = new float[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultFloat.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public float[] alloc( int size )
    {
        return new float[ size ];
    }

    public float[] alloc( int size, float fillValue )
    {
        float[] t = new float[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
