package collections.hash.set;

import collections.CollectionFloat;
import collections.hash.HashFunctions;
import collections.util.MultiListInt;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactoryFloat;
import core.stub.DefaultValueProvider;
import core.stub.*;

import java.util.Arrays;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class HashSetFloat implements CollectionFloat
{
    private static final int DEFAULT_FREE_LIST_SIZE = 16;


    /**
     * Factory that will provide us with
     */
    protected final ArrayFactoryFloat valFactory;
    protected final ArrayFactoryInt intFactory;
    protected final HashFunctions.HashFunctionFloat hashFunction;
    protected final GrowthStrategy growthStrategy;

    //this list will hold indexes into the set array
    protected MultiListInt bucketList;
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
    public HashSetFloat (int initialSize)
    {
        this (initialSize, .75,
              ArrayFactoryFloat.defaultfloatProvider,
              ArrayFactoryInt.defaultintProvider,
              HashFunctions.hashFunctionFloat,
              GrowthStrategy.doubleGrowth);
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
    public HashSetFloat (int initialSize, double loadFactor,
                                 ArrayFactoryFloat valFactory,
                                 ArrayFactoryInt intFactory,
                                 HashFunctions.HashFunctionFloat hashFunction,
                                 GrowthStrategy growthStrategy)
    {
        this.valFactory = valFactory;
        this.intFactory = intFactory;
        bucketList = new MultiListInt (initialSize, initialSize);
        freeList = intFactory.alloc (DEFAULT_FREE_LIST_SIZE);
        this.numBuckets = initialSize;
        this.hashFunction = hashFunction;
        this.growthStrategy = growthStrategy;
        this.loadFactor = loadFactor;
        this.loadFactorSize = (int) (initialSize * loadFactor);
    }

    @Override
    public int getSize ()
    {
        return size;
    }

    @Override
    public boolean isEmpty ()
    {
        return size == 0;
    }

    @Override
    public int contains (float value)
    {
        int bucket = getBucket (value);
        return inBucketList (bucket, value);
    }

    @Override
    public void clear ()
    {
        Arrays.fill (keys, 0, nextEntry,
                     DefaultValueProvider.DefaultFloat.getValue ());
    }

    /**
     * Unchecked method to retrieve an item in the set.
     *
     * @param entry the entry into the set
     * @return the value
     */
    @Override
    public float get (int entry)
    {
        return keys[entry];
    }

    @Override
    public int insert (float key)
    {
        int bucket = getBucket (key);
        int entry;
        //if our key exists in linked list, return its entry
        if ((entry = inBucketList (bucket, key)) != Const.NO_ENTRY)
        {
            return entry;
        }
        entry = getNextEntry ();
        keys[entry] = key;
        bucketList.insert (bucket, entry);
        size++;
        if (size == loadFactorSize)
        {
            reHash ();
        }
        return entry;
    }

    @Override
    public boolean remove (float value)
    {
        int bucket = getBucket (value);
        int entry = inBucketList (bucket, value);
        if (entry == Const.NO_ENTRY)
        {
            return false;
        }
        bucketList.remove (bucket, entry);
        size--;
        addEntryToFreeList (entry);
        return true;
    }

    /**
     * <p>When we hit the load factor, we double our bucket size and
     * rehash the items in the collection. Notice that this does
     * not effect the items in the keys at all, we are just re-mapping
     * the entries to different buckets.</p>
     */
    private void reHash ()
    {
        int newSize = GrowthStrategy.doubleGrowth.growthRequest (size, size + 1);
        MultiListInt newBucketList = new MultiListInt (newSize,
                                                       newSize);
        for (int i = 0; i < numBuckets; i++)
        {
            int prevEntry = Const.NO_ENTRY;
            int entry;
            int bucket;
            //iterate through old buckets rather than keys to ensure we
            //only get valid items
            while ((entry = bucketList.getNextEntryForList (i, prevEntry))
                    != Const.NO_ENTRY)
            {
                bucket = getBucket (keys[entry]);
                newBucketList.insert (bucket, entry);
            }
        }
        bucketList = newBucketList;
        loadFactorSize = (int) (newSize * loadFactor);
    }

    /**
     * Add an item to the freelist. If the freelist has run out of
     * entries, grow it. Otherwise, insert the item in the array a
     * at spot <i>freeListPtr</i> and increment the ptr.
     *
     * @param entry the entry to add to the free list of entries
     */
    private void addEntryToFreeList (int entry)
    {
        int curLen = freeList.length;
        if (freeListPtr >= freeList.length)
        {
            intFactory.grow (freeList, curLen * 2, 0, growthStrategy);
        }
        freeList[freeListPtr++] = entry;
    }


    protected int getNextEntry ()
    {
        if (freeListPtr != 0)
        {
            return freeList[freeListPtr--];
        }
        nextEntry++;
        //only do check if we are not on freelist
        valFactory.ensureArrayCapacity (keys, nextEntry, growthStrategy);
        return nextEntry;
    }


    /**
     * Return a bucket that this key will hash to. This will be within
     * the set of our possible buckets.
     *
     * @return the bucket
     */
    private int getBucket (float key)
    {
        return hashFunction.getHashCode (key) % numBuckets;
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
    protected int inBucketList (int bucket, float key)
    {
        int testEntry = bucketList.getNextEntryForList (bucket, Const.NO_ENTRY);
        while (testEntry != Const.NO_ENTRY)
        {
            if (keys[testEntry] == key) return testEntry;
            testEntry = bucketList.getNextEntryForList (bucket, testEntry);
        }
        return Const.NO_ENTRY;
    }
}
