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
public abstract class ArrayFactoryBool
{

    public abstract boolean[] ensureArrayCapacity( boolean[] array,
                                                 int minSize,
                                                 boolean defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract boolean[] grow( boolean[] array, int minSize,
                                  boolean defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryBool defaultbooleanProvider = new
            ArrayProviderBoolImpl();

    protected static final class ArrayProviderBoolImpl extends
                                                                ArrayFactoryBool
    {

        public boolean[] ensureArrayCapacity( boolean[] array,
                                            int minSize,
                                            boolean defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryBool.class, len,
                                                    minSize, Types.Int );
                }
                boolean[] temp = new boolean[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultBool.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public boolean[] grow( boolean[] array, int minSize,
                             boolean defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryBool.class, len,
                                                minSize, Types.Int );
            }
            boolean[] temp = new boolean[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultBool.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public boolean[] alloc( int size )
    {
        return new boolean[ size ];
    }

    public boolean[] alloc( int size, boolean fillValue )
    {
        boolean[] t = new boolean[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
