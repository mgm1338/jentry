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
public abstract class ArrayFactoryInt
{

    public abstract int[] ensureArrayCapacity( int[] array,
                                                 int minSize,
                                                 int defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract int[] grow( int[] array, int minSize,
                                  int defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryInt defaultintProvider = new
            ArrayProviderIntImpl();

    protected static final class ArrayProviderIntImpl extends
                                                                ArrayFactoryInt
    {

        public int[] ensureArrayCapacity( int[] array,
                                            int minSize,
                                            int defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryInt.class, len,
                                                    minSize, Types.Int );
                }
                int[] temp = new int[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultInt.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public int[] grow( int[] array, int minSize,
                             int defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryInt.class, len,
                                                minSize, Types.Int );
            }
            int[] temp = new int[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultInt.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public int[] alloc( int size )
    {
        return new int[ size ];
    }

    public int[] alloc( int size, int fillValue )
    {
        int[] t = new int[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
