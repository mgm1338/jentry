package collections.hash.map;

import collections.generic.Map_KeyTypeName__ValueTypeName_;
import collections.hash.HashFunctions;
import collections.hash.set.HashSet_KeyTypeName_;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.*;

import core.stub.*;
import core.util.comparator.EqualityFunctions;

/**
 * Copyright 2/26/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/26/13
 */
public class HashMap_KeyTypeName__ValueTypeName_ implements Map_KeyTypeName__ValueTypeName_
{

    protected  static final double DEFAULT_LOAD_FACTOR = .75;


    /** Factory that will provide us with value space */
    protected final ArrayFactory_ValueTypeName_ valueFactory;
    /** Equality function that test the equality of the different typed values */
    protected final EqualityFunctions.Equals_ValueTypeName_ equalityFunction = new
            EqualityFunctions.Equals_ValueTypeName_();


    protected _val_ values[];

    protected HashSet_KeyTypeName_ set;


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
    }


    @Override
    public boolean containsKey( _key_ key )
    {
        return set.contains( key );
    }

    public int insert( _key_ key, _val_ value )
    {
        int entry = set.insert( key );
        values[ entry ] = value;
        return entry;
    }

    @Override
    public int remove( _key_ key )
    {
        return 1;
    }


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


    public _val_ getValue( int entry )
    {
        return values[ entry ];
    }


    public HashMap_KeyTypeName__ValueTypeName_ copy( HashMap_KeyTypeName__ValueTypeName_ target )
    {
        int size = set.getSize();
        if( target == null ) //creating a new one
        {
            target = new HashMap_KeyTypeName__ValueTypeName_(size);
        }
        target.set = set.copy( new HashSet_KeyTypeName_( size ) );
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

    public void copy( Map_KeyTypeName__ValueTypeName_ target )
    {

    }
}
