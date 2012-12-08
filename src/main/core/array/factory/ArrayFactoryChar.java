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
public abstract class ArrayFactoryChar
{

    public abstract char[] ensureArrayCapacity( char[] array,
                                                 int minSize,
                                                 char defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract char[] grow( char[] array, int minSize,
                                  char defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryChar defaultcharProvider = new
            ArrayProviderCharImpl();

    protected static final class ArrayProviderCharImpl extends
                                                                ArrayFactoryChar
    {

        public char[] ensureArrayCapacity( char[] array,
                                            int minSize,
                                            char defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryChar.class, len,
                                                    minSize, Types.Int );
                }
                char[] temp = new char[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultChar.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public char[] grow( char[] array, int minSize,
                             char defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryChar.class, len,
                                                minSize, Types.Int );
            }
            char[] temp = new char[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultChar.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public char[] alloc( int size )
    {
        return new char[ size ];
    }

    public char[] alloc( int size, char fillValue )
    {
        char[] t = new char[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
