package core.array.factory;

import core.Types;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.DefaultValueProvider;
import core.stub.*;

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
public abstract class ArrayFactory_KeyTypeName_
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
    public abstract _key_[] ensureArrayCapacity (_key_[] array,
                                                 int minSize,
                                                 _key_ defaultValue,
                                                 GrowthStrategy growthStrategy);

    /**
     * <p>
     * When we are not checking to make sure that an array is large enough
     * (see {@link #ensureArrayCapacity(_key_[],
     * int, _key_, GrowthStrategy)}  }, and we would like
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
    public abstract _key_[] grow (_key_[] array, int minSize,
                                  _key_ defaultValue,
                                  GrowthStrategy growthStrategy);



    //STATIC IMPLEMENTATION BELOW

    /**
     * A type-specific static provider that is built-in. Extending
     * and/or changing the default factories allow expanded functionality.
     * In some situations, logging array allocations and other metrics
     * can be useful in tuning or customizing array policies.
     */
    public static final ArrayFactory_KeyTypeName_ default_key_Provider = new
            ArrayProvider_KeyTypeName_Impl ();


    /**
     * Implementation of basic methods above.
     */
    protected static final class ArrayProvider_KeyTypeName_Impl extends
                                                                ArrayFactory_KeyTypeName_
    {

        public _key_[] ensureArrayCapacity (_key_[] array,
                                            int minSize,
                                            _key_ defaultValue,
                                            GrowthStrategy growthStrategy)
        {
            int len = array.length;
            if (minSize > len)
            {
                int newSize = growthStrategy.growthRequest (len, minSize);
                if (newSize < minSize)
                {
                    throw new ArrayGrowthException (ArrayFactory_KeyTypeName_.class, len,
                                                    minSize, Types.Int);
                }
                _key_[] temp = new _key_[newSize];
                System.arraycopy (array, 0, temp, 0, len);
                if (defaultValue != DefaultValueProvider.Default_KeyTypeName_.getValue ())
                {
                    Arrays.fill (temp, len, newSize, defaultValue);
                }
                return temp;
            }
            return array;
        }


        public _key_[] grow (_key_[] array, int minSize,
                             _key_ defaultValue,
                             GrowthStrategy growthStrategy)
        {
            int len = array.length;
            int newSize = growthStrategy.growthRequest (len, minSize);
            if (newSize < minSize)
            {
                throw new ArrayGrowthException (ArrayFactory_KeyTypeName_.class, len,
                                                minSize, Types.Int);
            }
            _key_[] temp = new _key_[newSize];
            System.arraycopy (array, 0, temp, 0, len);
            if (defaultValue != DefaultValueProvider.Default_KeyTypeName_.getValue ())
            {
                Arrays.fill (temp, len, newSize, defaultValue);
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
    public _key_[] alloc (int size)
    {
        return new _key_[size];
    }

    public _key_[] alloc (int size, _key_ fillValue)
    {
        _key_[] t = new _key_[size];
        Arrays.fill (t, fillValue);
        return t;

    }
}
