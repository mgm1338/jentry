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
public abstract class ArrayFactoryCharSequence
{

    public abstract CharSequence[] ensureArrayCapacity( CharSequence[] array,
                                                 int minSize,
                                                 CharSequence defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract CharSequence[] grow( CharSequence[] array, int minSize,
                                  CharSequence defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryCharSequence defaultCharSequenceProvider = new
            ArrayProviderCharSequenceImpl();

    protected static final class ArrayProviderCharSequenceImpl extends
                                                                ArrayFactoryCharSequence
    {

        public CharSequence[] ensureArrayCapacity( CharSequence[] array,
                                            int minSize,
                                            CharSequence defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryCharSequence.class, len,
                                                    minSize, Types.Int );
                }
                CharSequence[] temp = new CharSequence[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultCharSequence.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public CharSequence[] grow( CharSequence[] array, int minSize,
                             CharSequence defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryCharSequence.class, len,
                                                minSize, Types.Int );
            }
            CharSequence[] temp = new CharSequence[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultCharSequence.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public CharSequence[] alloc( int size )
    {
        return new CharSequence[ size ];
    }

    public CharSequence[] alloc( int size, CharSequence fillValue )
    {
        CharSequence[] t = new CharSequence[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
