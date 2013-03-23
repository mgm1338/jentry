package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.DefaultValueProvider;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p/>
 * <p>
 * The main allocating classes for different primitive arrays. In Jentry
 * we try to condense all array allocation to these classes, to that
 * users can handle their allocation to a fine-grained level. For this
 * reason you will often see a noticeable lack of 'new' keywords in classes
 * when dealing with array structures.
 * </p>
 * <p/>
 * Auto-generates for each type of array.
 */
public abstract class ArrayFactoryChar
{

    /**
     * For the <i>array</i> passed, ensure that it has the length
     * to store at least as many items as <i>minSize</i>> If this is not
     * the case, then try to grow the array with {@link GrowthStrategy } passed.
     * If the array has to be grown, then the default value can be passed to
     * override the natural default values (0 for numeric, null for Object,
     * etc...)
     *
     * @param array          the array to check
     * @param minSize        the minimum number of elements this array should be able
     *                       to hold.
     * @param defaultValue   the default value to fill new array elements with
     * @param growthStrategy see {@link GrowthStrategy }
     * @return the array, either the same structure, or the newly allocated
     *         array
     */
    public abstract char[] ensureArrayCapacity( char[] array,
                                                int minSize,
                                                char defaultValue,
                                                GrowthStrategy growthStrategy );

    /**
     * Overloaded method:
     * <p/>
     * For the <i>array</i> passed, ensure that it has the length
     * to store at least as many items as <i>minSize</i>> If this is not
     * the case, then try to grow the array with {@link GrowthStrategy } passed.
     *
     * @param array          the array to check
     * @param minSize        the minimum number of elements this array should be able
     *                       to hold.
     * @param growthStrategy see {@link GrowthStrategy }
     * @return the array, either the same structure, or the newly allocated
     *         array
     */
    public abstract char[] ensureArrayCapacity( char[] array,
                                                int minSize,
                                                GrowthStrategy growthStrategy );

    /**
     * <p>
     * When we are not checking to make sure that an array is large enough
     * (see {@link #ensureArrayCapacity(char[],
     * int, char, GrowthStrategy)}  }, and we would like
     * to grow the array, we use this method. This will use the
     * {@link GrowthStrategy } to grow the <i>array</i> passed.
     * </p>
     * <p>
     * We pass <i>minSize</i> as the number of elements that this array
     * should grow to (depending on the strategy, it may grow past this
     * size, but it is guaranteed to be at least this many elements).
     * </p>
     *
     * @param array          array to grow
     * @param minSize        the minimum number of elements that this array will
     *                       be able to hold after growing
     * @param defaultValue   the default value for new elements (can override
     *                       natural default values)
     * @param growthStrategy see {@link GrowthStrategy }
     * @return the grown array
     */
    public abstract char[] grow( char[] array, int minSize,
                                 char defaultValue,
                                 GrowthStrategy growthStrategy );


    //STATIC IMPLEMENTATION BELOW

    /**
     * A type-specific static provider that is built-in. Extending
     * and/or changing the default factories allow expanded functionality.
     * In some situations, logging array allocations and other metrics
     * can be useful in tuning or customizing array policies.
     */
    public static final ArrayFactoryChar defaultCharProvider = new
            ArrayProviderCharImpl();


    /** Implementation of basic methods above. */
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
                Arrays.fill( temp, len, newSize, defaultValue );
                return temp;
            }
            return array;
        }

        @Override
        public char[] ensureArrayCapacity( char[] array, int minSize,
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

    /**
     * Allocate arrays, simple utility methods to contain all allocations
     * in one place.
     *
     * @param size size of array
     * @return the array
     */
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
