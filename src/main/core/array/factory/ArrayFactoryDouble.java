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
public abstract class ArrayFactoryDouble
{

    public abstract double[] ensureArrayCapacity( double[] array,
                                                 int minSize,
                                                 double defaultValue,
                                                 GrowthStrategy growthStrategy );

    public abstract double[] grow( double[] array, int minSize,
                                  double defaultValue,
                                  GrowthStrategy growthStrategy );

    public static final ArrayFactoryDouble defaultdoubleProvider = new
            ArrayProviderDoubleImpl();

    protected static final class ArrayProviderDoubleImpl extends
                                                                ArrayFactoryDouble
    {

        public double[] ensureArrayCapacity( double[] array,
                                            int minSize,
                                            double defaultValue,
                                            GrowthStrategy growthStrategy )
        {
            int len = array.length;
            if( minSize > len )
            {
                int newSize = growthStrategy.growthRequest( len, minSize );
                if( newSize < minSize )
                {
                    throw new ArrayGrowthException( ArrayFactoryDouble.class, len,
                                                    minSize, Types.Int );
                }
                double[] temp = new double[ newSize ];
                System.arraycopy( array, 0, temp, 0, len );
                if( defaultValue != DefaultValueProvider.DefaultDouble.getValue() )
                {
                    Arrays.fill( temp, len, newSize, defaultValue );
                }
                return temp;
            }
            return array;
        }


        public double[] grow( double[] array, int minSize,
                             double defaultValue,
                             GrowthStrategy growthStrategy )
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest( len, minSize );
            if( newSize < minSize )
            {
                throw new ArrayGrowthException( ArrayFactoryDouble.class, len,
                                                minSize, Types.Int );
            }
            double[] temp = new double[ newSize ];
            System.arraycopy( array, 0, temp, 0, len );
            if( defaultValue != DefaultValueProvider.DefaultDouble.getValue() )
            {
                Arrays.fill( temp, len, newSize, defaultValue );
            }
            return temp;
        }
    }

    public double[] alloc( int size )
    {
        return new double[ size ];
    }

    public double[] alloc( int size, double fillValue )
    {
        double[] t = new double[ size ];
        Arrays.fill( t, fillValue );
        return t;

    }
}
