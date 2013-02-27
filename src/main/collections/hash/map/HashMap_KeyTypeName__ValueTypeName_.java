package collections.hash.map;

import collections.hash.HashFunctions;
import collections.hash.set.HashSet_KeyTypeName_;
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
public class HashMap_KeyTypeName__ValueTypeName_ extends HashSet_KeyTypeName_
{

    /** Factory that will provide us with value space */
    protected final ArrayFactory_ValueTypeName_ valueFactory;
    /** Equality function that test the equality of the different typed values */
    protected final EqualityFunctions.Equals_ValueTypeName_ equalityFunction = new
            EqualityFunctions.Equals_ValueTypeName_();

    protected _val_ values[];

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
        super( initialSize, loadFactor, keyFactory, intFactory, hashFunction, growthStrategy );
        this.valueFactory = valueFactory;
        values = valueFactory.alloc( initialSize );
    }


    public int insert( _key_ key, _val_ value )
    {
        int entry = super.insert( key );
        values[entry] = value;
        return entry;
    }

    /**
     * Return the next available entry. If one is on the freelist, this should be returned first. Otherwise,
     * get the next entry, which will be compact (the next un-used entry iteratively).
     *
     * @return the next available entry
     */
    @Override
    protected int getNextEntry()
    {
        int entry = super.getNextEntry();
        values = valueFactory.ensureArrayCapacity( values, nextEntry + 1, growthStrategy  );
        return entry;
    }

    public _val_ getValue( int entry )
    {
        return values[entry];
    }


    public HashMap_KeyTypeName__ValueTypeName_ copy( HashMap_KeyTypeName__ValueTypeName_ target )
    {
        if( target == null ) //creating a new one
        {
            target = new HashMap_KeyTypeName__ValueTypeName_( keys.length, loadFactor, keyFactory, intFactory, hashFunction,
                                               growthStrategy, valueFactory );
        }
        target.nextEntry = nextEntry;
        target.loadFactor = loadFactor;
        target.size = size;
        target.loadFactorSize = loadFactorSize;


        //grow keys and freelist to the exact size initially and copy them
        int keyLen = keys.length;
        int freeListLen = freeList.length;
        target.keys = keyFactory.ensureArrayCapacity( target.keys, keyLen, GrowthStrategy.toExactSize );
        target.freeList = intFactory.ensureArrayCapacity( target.freeList, freeListLen, GrowthStrategy.toExactSize );
        System.arraycopy( keys, 0, target.keys, 0, keyLen );
        System.arraycopy( values, 0, target.values, 0, keyLen );
        System.arraycopy( freeList, 0, target.freeList, 0, freeListLen );

        //get a deep copy of the bucket list for the target
        target.bucketList = bucketList.getDeepCopy();
        return target;
    }
}
