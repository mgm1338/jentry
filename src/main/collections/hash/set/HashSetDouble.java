package collections.hash.set;

import collections.CollectionDouble;
import collections.hash.HashFunctions;
import collections.util.MultiLinkedListInt;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryDouble;
import core.stub.DefaultValueProvider;
import core.stub.*;
import core.util.comparator.EqualityFunctions;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class HashSetDouble implements CollectionDouble
{
    protected final static double DEFAULT_LOAD_FACTOR = .75;
    protected static final int DEFAULT_FREE_LIST_SIZE = 16;


    /** Factory that will provide us with */
    protected final ArrayFactoryDouble valFactory;
    protected final ArrayFactoryInt intFactory;
    protected final HashFunctions.HashFunctionDouble hashFunction;
    protected final GrowthStrategy growthStrategy;
    protected final EqualityFunctions.EqualsDouble equalityFunction = new
            EqualityFunctions.EqualsDouble();

    //this list will hold indexes into the set array
    protected MultiLinkedListInt bucketList;
    protected double keys[];

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
    public HashSetDouble( int initialSize )
    {
        this( initialSize, DEFAULT_LOAD_FACTOR,
              ArrayFactoryDouble.defaultDoubleProvider,
              ArrayFactoryInt.defaultIntProvider,
              HashFunctions.hashFunctionDouble,
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
     * @param valFactory     the factory that allocates the double arrays
     * @param intFactory     the factory allocating the int arrays
     * @param hashFunction   hash function to use for the hash set
     * @param growthStrategy strategy for growing the structures
     */
    public HashSetDouble( int initialSize, double loadFactor,
                                 ArrayFactoryDouble valFactory,
                                 ArrayFactoryInt intFactory,
                                 HashFunctions.HashFunctionDouble hashFunction,
                                 GrowthStrategy growthStrategy )
    {
        this.valFactory = valFactory;
        this.intFactory = intFactory;
        bucketList = new MultiLinkedListInt( initialSize, initialSize );
        freeList = intFactory.alloc( DEFAULT_FREE_LIST_SIZE );
        keys = ArrayFactoryDouble.defaultDoubleProvider.alloc( initialSize,
                                                                     IntValueConverter.doubleFromInt( Const.NO_ENTRY ) );
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
    public boolean contains( double value )
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
    public int getEntry( double val )
    {
        int bucket = getBucket( val );
        return inBucketList( bucket, val );
    }

    /**
     * UNCHECKED method to retrieve an item in the set. This should be used with caution, as it may potentially
     * return a value that was removed. See {@link #getEntry(double)}  above for getting a specific value.
     *
     * @param entry the entry into the set
     * @return the value
     */
    @Override
    public double get( int entry )
    {
        return keys[ entry ];
    }


    @Override
    public int insert( double key )
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
    public boolean remove( double value )
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
    private int getBucket( double key )
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
    protected int inBucketList( int bucket, double key )
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


    public HashSetDouble copy()
    {

        HashSetDouble target = new HashSetDouble( ( int ) ( size / loadFactor ), loadFactor,
                                                                this.valFactory, this.intFactory, this.hashFunction,
                                                                this.growthStrategy );
        int keyLen = keys.length;
        double[] targetKeys = target.keys;
        valFactory.ensureArrayCapacity( targetKeys, keyLen, IntValueConverter.doubleFromInt( Const.NO_ENTRY ),
                                        GrowthStrategy.toExactSize );
        System.arraycopy( keys, 0, targetKeys, 0, keyLen );


        intFactory.ensureArrayCapacity( target.freeList, this.freeList.length, GrowthStrategy.toExactSize );

        return target;

    }
}
