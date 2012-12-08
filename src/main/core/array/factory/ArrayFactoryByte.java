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
public abstract class ArrayFactoryByte
{

    public abstract byte[] ensureArrayCapacity( byte[] array,
                                                 int minSize,
                                                 byte defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract byte[] grow( byte[] array, int minSize,
                                  byte defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryByte defaultbyteProvider = new
            ArrayProviderByteImpl();

    protected static final class ArrayProviderByteImpl extends
                                                                ArrayFactoryByte
    {

        public byte[] ensureArrayCapacity( byte[] array,
                                            int minSize,
                                            byte defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryByte.class, len,
                                                    minSize, Types.Int );
                }
                byte[] temp = new byte[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultByte.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public byte[] grow( byte[] array, int minSize,
                             byte defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryByte.class, len,
                                                minSize, Types.Int );
            }
            byte[] temp = new byte[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultByte.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public byte[] alloc( int size )
    {
        return new byte[ size ];
    }

    public byte[] alloc( int size, byte fillValue )
    {
        byte[] t = new byte[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
