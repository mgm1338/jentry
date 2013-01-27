package collections.hash.set;

import collections.CollectionFloat;
import collections.hash.HashFunctions;
import collections.util.MultiLinkedListInt;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryFloat;
import core.stub.DefaultValueProvider;
import core.stub.*;
import core.util.comparator.EqualityFunctions;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class HashSetFloat implements CollectionFloat
{
    protected final static double DEFAULT_LOAD_FACTOR = .75;
    protected static final int DEFAULT_FREE_LIST_SIZE = 16;


    /** Factory that will provide us with */
    protected final ArrayFactoryFloat valFactory;
    protected final ArrayFactoryInt intFactory;
    protected final HashFunctions.HashFunctionFloat hashFunction;
    protected final GrowthStrategy growthStrategy;
    protected final EqualityFunctions.EqualsFloat equalityFunction = new
            EqualityFunctions.EqualsFloat();

    //this list will hold indexes into the set array
    protected MultiLinkedListInt bucketList;
    protected float keys[];

    protected int nextEntry = 0;
    protected int[] freeList;
    protected int freeListPtr = 0;
    protected int numBuckets;
    protected int size = 0;
    protected int loadFactorSize;
    protected double loadFactor;

    /**
     * Basic constructor
     *
     * @param initialSize the expected size that the HashSet will have to
     *                    hold. If this is known, then make this initial
     *                    capacity large enough so that the default load
     *                    factor (.75) does not cause growth.
     */
    public HashSetFloat( int initialSize )
    {
        this( initialSize, DEFAULT_LOAD_FACTOR,
              ArrayFactoryFloat.defaultFloatProvider,
              ArrayFactoryInt.defaultIntProvider,
              HashFunctions.hashFunctionFloat,
              GrowthStrategy.doubleGrowth );
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
     * @param valFactory     the factory that allocates the float arrays
     * @param intFactory     the factory allocating the int arrays
     * @param hashFunction   hash function to use for the hash set
     * @param growthStrategy strategy for growing the structures
     */
    public HashSetFloat( int initialSize, double loadFactor,
                                 ArrayFactoryFloat valFactory,
                                 ArrayFactoryInt intFactory,
                                 HashFunctions.HashFunctionFloat hashFunction,
                                 GrowthStrategy growthStrategy )
    {
        this.valFactory = valFactory;
        this.intFactory = intFactory;
        bucketList = new MultiLinkedListInt( initialSize, initialSize );
        freeList = intFactory.alloc( DEFAULT_FREE_LIST_SIZE );
        keys = ArrayFactoryFloat.defaultFloatProvider.alloc( initialSize,
                                                                             IntValueConverter.floatFromInt( Const.NO_ENTRY ) );
        this.numBuckets = initialSize;
        this.hashFunction = hashFunction;
        this.growthStrategy = growthStrategy;
        this.loadFactor = loadFactor;
        this.loadFactorSize = ( int ) ( initialSize * loadFactor );
    }

    @Override
    public int getSize()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public boolean contains( float value )
    {
        int bucket = getBucket( value );
        return inBucketList( bucket, value ) != Const.NO_ENTRY;
    }

    @Override
    public void clear()
    {
        bucketList.clear();
        size = 0;
        freeListPtr = nextEntry = 0;
    }

    /**
     * Method for checking to see if an item is in the HashSet. This will retrieve the entry
     * for the item, or return Const.NO_ENTRY if the item is not in the set.
     *
     * @param val the value
     * @return the entry of the item (handle), or Const.NO_ENTRY
     */
    public int getEntry( float val )
    {
        int bucket = getBucket( val );
        return inBucketList( bucket, val );
    }

    /**
     * UNCHECKED method to retrieve an item in the set. This should be used with caution, as it may potentially
     * return a value that was removed. See {@link #getEntry(float)}  above for getting a specific value.
     *
     * @param entry the entry into the set
     * @return the value
     */
    @Override
    public float get( int entry )
    {
        return keys[ entry ];
    }


    @Override
    public int insert( float key )
    {
        int bucket = getBucket( key );
        int entry;
        //if our key exists in linked list, return its entry
        if( ( entry = inBucketList( bucket, key ) ) != Const.NO_ENTRY )
        {
            return entry;
        }
        entry = getNextEntry();
        keys[ entry ] = key;
        bucketList.insert( bucket, entry );
        size++;
        if( size == loadFactorSize )
        {
            reHash();
        }
        return entry;
    }

    /**
     * @param value the value to remove
     * @return
     */
    @Override
    public boolean remove( float value )
    {
        int bucket = getBucket( value );
        int entry = inBucketList( bucket, value );
        if( entry == Const.NO_ENTRY )
        {
            return false;
        }
        bucketList.remove( bucket, entry );
        size--;
        addEntryToFreeList( entry );
        return true;
    }

    /**
     * <p>When we hit the load factor, we double our bucket size and
     * rehash the items in the collection. Notice that this does
     * not effect the items in the keys at all, we are just re-mapping
     * the entries to different buckets.</p>
     */
    private void reHash()
    {
        int newSize = GrowthStrategy.doubleGrowth.growthRequest( size, size + 1 );
        MultiLinkedListInt newBucketList = new MultiLinkedListInt( newSize,
                                                                   newSize );
        for( int i = 0; i < numBuckets; i++ )
        {
            int prevIdx = Const.NO_ENTRY;
            int idx;
            int entry;
            int bucket;
            //iterate through old buckets rather than keys to ensure we
            //only get valid items
            while( ( idx = bucketList.getNextIdxForList( i, prevIdx ) )
                   != Const.NO_ENTRY )
            {
                entry = bucketList.getHead( idx );
                if( entry == Const.NO_ENTRY ) break;
                bucket = getBucket( keys[ entry ] );
                newBucketList.insert( bucket, entry );
                prevIdx = idx;
            }
        }
        bucketList = newBucketList;
        loadFactorSize = ( int ) ( newSize * loadFactor );
    }

    /**
     * Add an item to the freelist. If the freelist has run out of
     * entries, grow it. Otherwise, insert the item in the array a
     * at spot <i>freeListPtr</i> and increment the ptr.
     *
     * @param entry the entry to add to the free list of entries
     */
    private void addEntryToFreeList( int entry )
    {
        int curLen = freeList.length;
        if( freeListPtr >= curLen )
        {
            freeList = intFactory.grow( freeList, curLen * 2, 0, growthStrategy );
        }
        freeList[ freeListPtr++ ] = entry;
    }

    /**
     * Return the next available entry. If one is on the freelist, this should be returned first. Otherwise,
     * get the next entry, which will be compact (the next un-used entry iteratively).
     *
     * @return the next available entry
     */
    protected int getNextEntry()
    {
        if( freeListPtr != 0 )
        {
            return freeList[ --freeListPtr ];
        }
        //not on freelist, need growth check
        keys = valFactory.ensureArrayCapacity( keys, nextEntry + 1, growthStrategy );
        return nextEntry++;
    }


    /**
     * Return a bucket that this key will hash to. This will be within
     * the set of our possible buckets.
     *
     * @return the bucket
     */
    private int getBucket( float key )
    {
        return hashFunction.getHashCode( key ) % numBuckets;
    }

    /**
     * Check to see if item already in HashSet. This will iterate the items
     * in a bucket, testing to see if the value we are testing is already in
     * our set. If it is, it will return the index of the item, otherwise
     * it will return NO_ENTRY.
     *
     * @param bucket the bucket to check
     * @param key    the key we are checking
     * @return the entry of the item, or Const.NO_ENTRY
     */
    protected int inBucketList( int bucket, float key )
    {
        //get key for the head
        int bucketListEntry = bucketList.getNextIdxForList( bucket, Const.NO_ENTRY );
        while( bucketListEntry != Const.NO_ENTRY )
        {
            int keyEntry = bucketList.getHead( bucketListEntry );
            if( keyEntry == Const.NO_ENTRY ) return Const.NO_ENTRY;
            //check equals
            if( equalityFunction.equals( keys[ keyEntry ], key ) ) return keyEntry;
            bucketListEntry = bucketList.getNextIdxForList( bucket, bucketListEntry );
        }
        return Const.NO_ENTRY;
    }

    /**
     * Creates a deep copy of this HashSet by copying all of its attributes to the target. If the target is null,
     * then this method will create a new HashSet to copy all of its attributes to.
     *
     * @param target the target HashSet, may be null
     * @return the deep copy of this
     */
    public HashSetFloat copy( HashSetFloat target )
    {
        if( target == null ) //creating a new one
        {
            target = new HashSetFloat( keys.length, loadFactor, valFactory, intFactory, hashFunction,
                                               growthStrategy );
        }
        target.nextEntry = nextEntry;
        target.loadFactor = loadFactor;
        target.size = size;
        target.loadFactorSize = loadFactorSize;


        //grow keys and freelist to the exact size initially and copy them
        int keyLen = keys.length;
        int freeListLen = freeList.length;
        target.keys = valFactory.ensureArrayCapacity(  target.keys, keyLen, GrowthStrategy.toExactSize );
        target.freeList = intFactory.ensureArrayCapacity( target.freeList, freeListLen, GrowthStrategy.toExactSize );
        System.arraycopy( keys, 0, target.keys, 0, keyLen );
        System.arraycopy( freeList, 0, target.freeList, 0, freeListLen );

        //get a deep copy of the bucket list for the target
        target.bucketList = bucketList.getDeepCopy();
        return target;

    }
}
