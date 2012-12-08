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
public abstract class ArrayFactoryObject
{

    public abstract Object[] ensureArrayCapacity( Object[] array,
                                                 int minSize,
                                                 Object defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract Object[] grow( Object[] array, int minSize,
                                  Object defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryObject defaultObjectProvider = new
            ArrayProviderObjectImpl();

    protected static final class ArrayProviderObjectImpl extends
                                                                ArrayFactoryObject
    {

        public Object[] ensureArrayCapacity( Object[] array,
                                            int minSize,
                                            Object defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryObject.class, len,
                                                    minSize, Types.Int );
                }
                Object[] temp = new Object[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultObject.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public Object[] grow( Object[] array, int minSize,
                             Object defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryObject.class, len,
                                                minSize, Types.Int );
            }
            Object[] temp = new Object[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultObject.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public Object[] alloc( int size )
    {
        return new Object[ size ];
    }

    public Object[] alloc( int size, Object fillValue )
    {
        Object[] t = new Object[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
