package collections.hash.map;

import collections.generic.map.Map_KeyTypeName__ValueTypeName_;
import collections.hash.HashFunctions;
import collections.hash.set.HashSet_KeyTypeName_;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactory_KeyTypeName_;
import core.array.factory.ArrayFactory_ValueTypeName_;
import core.stub.*;
import core.util.comparator.EqualityFunctions;

/**
 * Copyright 2/26/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/26/13
 * <p/>
 * <p>
 * The HashMap is similar to the JDK HashMap, where one key is related to a value. The values
 * may be repeating, and inserting a new value for the same key will replace the old associated value.
 * The Jentry HashMap is built upon the HashSet, with a parallel array composed on the keys.
 * See {@link HashSet_KeyTypeName_}
 * </p>
 */
public class HashMap_KeyTypeName__ValueTypeName_ implements Map_KeyTypeName__ValueTypeName_
{

    /** Default load factor before re-hashing the keys */
    protected static final double DEFAULT_LOAD_FACTOR = .75;


    /** Factory that will provide us with value space */
    protected final ArrayFactory_ValueTypeName_ valueFactory;
    /** Equality function that test the equality of the different typed values */
    protected final EqualityFunctions.Equals_ValueTypeName_ equalityFunction = new
            EqualityFunctions.Equals_ValueTypeName_();

    /** The parallel array to keys, each valid key will have a parallel value at the same index */
    protected _val_ values[];
    /** The HashSet that we use to deal with the hashed keys */
    protected HashSet_KeyTypeName_ set;

    protected GrowthStrategy growthStrategy;


    /**
     * Basic constructor
     *
     * @param initialSize the expected size that the HashSet will have to
     *                    hold. If this is known, then make this initial
     *                    capacity large enough so that the default load
     *                    factor (.75) does not cause growth.
     */
    public HashMap_KeyTypeName__ValueTypeName_( int initialSize )
    {
        this( initialSize, DEFAULT_LOAD_FACTOR, ArrayFactory_KeyTypeName_.default_KeyTypeName_Provider,
              ArrayFactoryInt.defaultIntProvider, HashFunctions.hashFunction_KeyTypeName_,
              GrowthStrategy.doubleGrowth, ArrayFactory_ValueTypeName_.default_ValueTypeName_Provider );

    }

    /**
     * Full Constructor
     *
     * @param initialSize    the expected size that the HashSet will have to
     *                       hold. If this is known, then make this initial
     *                       capacity large enough so that the load factor does
     *                       not cause growth.
     * @param loadFactor     the portion of the capacity of the hashset
     *                       items before re-hashing to new buckets. If the initial
     *                       size is 10 and the load factor is .5, then we
     *                       re-hash after 5 items.
     * @param keyFactory     the factory that allocates the _key_ array
     * @param intFactory     the factory allocating the int arrays
     * @param hashFunction   hash function to use for the hash set
     * @param growthStrategy strategy for growing the structures
     * @param valueFactory   the factory that allocates the value array
     */
    public HashMap_KeyTypeName__ValueTypeName_( int initialSize, double loadFactor, ArrayFactory_KeyTypeName_ keyFactory,
                                                ArrayFactoryInt intFactory, HashFunctions.HashFunction_KeyTypeName_ hashFunction,
                                                GrowthStrategy growthStrategy, ArrayFactory_ValueTypeName_ valueFactory )
    {
        set = new HashSet_KeyTypeName_( initialSize, loadFactor, keyFactory, intFactory, hashFunction,
                                        growthStrategy );
        this.valueFactory = valueFactory;
        values = valueFactory.alloc( initialSize );
        this.growthStrategy = set.getGrowthStrategy();
    }

    /**
     * {@inheritDoc}
     *
     * @param key the key who's existence we are checking in the map
     * @return the location of the key, -1 otherwise
     */
    @Override
    public int containsKey( _key_ key )
    {
        return set.contains( key );
    }

    /**
     * Insert the key and value pair into the HashMap. Will insert the key into the HashSet, and
     * use the entry returned to store the value. This will replace any previous existing values
     * for the key passed.
     *
     * @param key   the key we will hash to use for key,value lookup
     * @param value value associated with the key
     * @return the entry we are inserting the key and the value
     */
    public int insert( _key_ key, _val_ value )
    {
        int entry = set.insert( key );
        values = valueFactory.ensureArrayCapacity( values, entry + 1, growthStrategy );
        values[ entry ] = value;
        return entry;
    }

    /**
     * Remove the key/value pair from the set.
     * <p/>
     * <p>
     * Note: this does not remove the values from the key/value array. For this reason, the get
     * method may return old values and must always be used with valid entries.
     * </p>
     *
     * @param key the key of the key/value pair we want to remove
     * @return the entry removing, or -1 if the key does not exist
     */
    @Override
    public int remove( _key_ key )
    {
        return set.remove( key );
    }

    /**
     * {@inheritDoc}
     *
     * @param key       the key to retrieve the value for
     * @param nullValue the null value to return if they key does not exist
     * @return the value, or <i>nullValue</i>
     */
    @Override
    public _val_ get( _key_ key, _val_ nullValue )
    {
        int entry = set.getEntry( key );
        if( entry == Const.NO_ENTRY )
        {
            return nullValue;
        }
        return values[ entry ];
    }

    /**
     * <p>
     * Create and return a deep-copy of the HashMap. If the target passed is null, will create a new HashMap.
     * </p>
     *
     * @param target the target HashMap, may be null
     * @return a deep copy of <i>this</i>
     */
    public HashMap_KeyTypeName__ValueTypeName_ copy( HashMap_KeyTypeName__ValueTypeName_ target )
    {
        int size = set.getSize();
        if( target == null ) //creating a new one
        {
            target = new HashMap_KeyTypeName__ValueTypeName_( size );
        }
        target.set = this.set.copy( target.set );
        target.values = valueFactory.ensureArrayCapacity( target.values, values.length, growthStrategy );
        System.arraycopy( values, 0, target.values, 0, values.length );
        return target;
    }

    /**
     * Get the current size of the collection.
     *
     * @return the size
     */
    @Override
    public int getSize()
    {
        return set.getSize();
    }

    /**
     * Is the current collection empty.
     *
     * @return true if collection is empty, false otherwise
     */
    @Override
    public boolean isEmpty()
    {
        return set.isEmpty();
    }

    /** Clear the collection, empty out all of its contents. */
    @Override
    public void clear()
    {
        set.clear();
    }

}
