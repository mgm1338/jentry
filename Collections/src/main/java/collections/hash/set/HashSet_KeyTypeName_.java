package collections.hash.set;

import collections.generic.Collection_KeyTypeName_;
import collections.hash.HashFunctions;
import collections.util.MultiLinkedListInt;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactory_KeyTypeName_;
import core.stub.*;
import core.util.comparator.EqualityFunctions;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 *
 * <p>The HashSet for the Jentry Collections. Similar to the JDK HashSet in searching for items in linear time,
 * and ensuring uniqueness of items in the collection. The added benefit is that items inserted into the set
 * provide compact keys. This last bit is used all over the place in Jentry.</p>
 *
 * <b>Common Use</b>
 *  <ul>
 *      The paradigm of insertion into a HashSet to provide a storage location for items.
 *      Suppose we are storing market orders in memory:
 *      <pre>
 *          HashSetInt orders = new HashSet(1000000);
 *
 *          for () //loading a bunch of orders
 *          {
 *              int idx = orders.insert(order.getId());
 *              prices[idx] = order.getPrice();
 *              shares[idx] = orders.getShares();
 *              counterPartyIds[idx] = order.getCounterPartyId();
 *              /etc..
 *          }
 *
 *
 *      </pre>
 *
 *      Now because this is a HashSet, we can find that order in linear time, and we know exactly how to get
 *      all of its information:
 *
 *      <pre>
 *          int idx = orders.getEntry(42013);
 *          if (idx==-1) return; //doesnt exist
 *          long price = prices[idx];
 *          long shares = shares[idx];
 *          etc...
 *      </pre>
 *  </ul>
 *
 *  <b>Compactness</b>
 *  <p>As mentioned above, items stored in the HashSet are 'compact'. In our setting, what this means
 *  is that the entries, or handles inserted into the HashSet start with zero, and continue with 1, 2,
 *  etc... When an item is removed, its entry is the next one used. These entries add a level of indirection,
 *  which has a memory cost, however the speed increase of the direct array access outweighs this cost.
 *  This is a very important point of Jentry, which is pervasive in the Jentry Collections and structures that
 *  employ them. See the documentation for more justification and explanation of these concepts.</p>
 *
 *  <b>Structure</b>
 *  <p>
 *      The underlying structure of the Hashset consists of one of our utility collections, the
 *      {@link MultiLinkedListInt}, which will hold a collection of singly linked lists. These singly linked
 *      lists will hold the entries per hash bucket. We cover the structure of that collection there, so it will be
 *      abbreviated in this example.
 *
 *  The HashSet will hold the actual keys to the set in the <i>keys</i>  array. The utility structure will point
 *  to this the entry (location) of the key into this array. Following the following example:
 *  </p>
 *  <p>
 *  <b>Example</b>
 *
 *  Assume a Hashet with 4 buckets, where the bucket is simply determined by % 4.
 * <br /><br />
 * 1.
 *  Insert 457  (bucket 1)  <br />
 *  HashSet
 *  <pre>
 *      keys
 *    0 457;
 *  </pre>
 *  MultiLinkedListInt
 *  <pre>
        0 : 0
 *  </pre>
 *
 * 2.
 *  Insert 400  (bucket 0)      <br />
 *  HashSet
 *  <pre>
 *      keys
 *    0 457;
 *    1 400
 *  </pre>
 *  MultiLinkedListInt
 *  <pre>
 *    0: 0
 *    1: 1
 *  </pre>
 *
 * 3.
 *   Insert 401  (bucket 1)    <br />
 *  HashSet
 *  <pre>
 *      keys
 *    0 457;
 *    1 400
 *    2 401
 *  </pre>
 *  MultiLinkedListInt
 *  <pre>
 *   0: 0
 *   1: 2, 1
 *  </pre>
 *
 * 4.
 *   Insert 405  (bucket 1)   <br />
 *  HashSet
 *  <pre>
 *      keys
 *    0 457;
 *    1 400
 *    2 401
 *    3 405
 *  </pre>
 *  MultiLinkedListInt
 *  <pre>
 *   0: 0
 *   1: 3, 2, 1
 *  </pre>
 *
 *  </p>
 *
 *  <p>The keys holds the actual values that are inserted in the set, while the linked list holds the entries
 *  of the keys. Upon removal, and insertion, keys are re-used.</p>
 *
 *  5.
 *   Remove 401 <br />
 *  HashSet
 *  <pre>
 *      keys
 *    0 457;
 *    1 400
 *    2 -1
 *    3 405
 *  </pre>
 *  MultiLinkedListInt
 *  <pre>
 *   0: 0
 *   1: 3, 1
 *  </pre>
 *
 *   6.
 *   Insert 404<br />
 *  HashSet
 *  <pre>
 *      keys
 *    0 457;
 *    1 400
 *    2 404
 *    3 405
 *  </pre>
 *  MultiLinkedListInt
 *  <pre>
 *   0: 2, 0
 *   1: 3, 1
 *  </pre>
 *
 */
public class HashSet_KeyTypeName_ implements Collection_KeyTypeName_
{
    protected final static double DEFAULT_LOAD_FACTOR = .75;
    protected static final int DEFAULT_FREE_LIST_SIZE = 16;


    /** Factory that will provide us with value space */
    protected final ArrayFactory_KeyTypeName_ keyFactory;
    /** Int Factory to provide us with freeList and bucket list */
    protected final ArrayFactoryInt intFactory;
    /** Hash function used to hash our values to an int bucket */
    protected final HashFunctions.HashFunction_KeyTypeName_ hashFunction;
    /** Growth strategy of our set, freelist, and bucket set */
    protected final GrowthStrategy growthStrategy;
    /** Equality function that test the equality of the different typed values */
    protected final EqualityFunctions.Equals_KeyTypeName_ equalityFunction = new
            EqualityFunctions.Equals_KeyTypeName_();

    //this list will hold indexes into the set array
    protected MultiLinkedListInt bucketList;
    /** Array of the values in the set, for example, a HashSetByte will hold the inserted bytes here */
    protected _key_ keys[];

    /** Next empty entry in the <b>keys</b> array */
    protected int nextEntry = 0;
    /** List of free entries (in the <b>keys</b> array) */
    protected int[] freeList;
    /** Pointer in the free list (that points to the remaining free entries) */
    protected int freeListPtr = 0;
    /** Number of buckets in the HashSet */
    protected int numBuckets;
    /** Number of inserted values */
    protected int size = 0;
    /** Load factor size, when we hit this size, we shall grow and re-hash items */
    protected int loadFactorSize;
    /** Load factor (0 to 1), that will indicate when we re-hash */
    protected double loadFactor;

    /**
     * Basic constructor
     *
     * @param initialSize the expected size that the HashSet will have to
     *                    hold. If this is known, then make this initial
     *                    capacity large enough so that the default load
     *                    factor (.75) does not cause growth.
     */
    public HashSet_KeyTypeName_( int initialSize )
    {
        this( initialSize, DEFAULT_LOAD_FACTOR,
              ArrayFactory_KeyTypeName_.default_KeyTypeName_Provider,
              ArrayFactoryInt.defaultIntProvider,
              HashFunctions.hashFunction_KeyTypeName_,
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
     * @param keyFactory     the factory that allocates the _key_ arrays
     * @param intFactory     the factory allocating the int arrays
     * @param hashFunction   hash function to use for the hash set
     * @param growthStrategy strategy for growing the structures
     */
    public HashSet_KeyTypeName_( int initialSize, double loadFactor,
                                 ArrayFactory_KeyTypeName_ keyFactory,
                                 ArrayFactoryInt intFactory,
                                 HashFunctions.HashFunction_KeyTypeName_ hashFunction,
                                 GrowthStrategy growthStrategy )
    {
        this.keyFactory = keyFactory;
        this.intFactory = intFactory;
        bucketList = new MultiLinkedListInt( initialSize, initialSize, growthStrategy, intFactory );
        freeList = intFactory.alloc( DEFAULT_FREE_LIST_SIZE );
        keys = ArrayFactory_KeyTypeName_.default_KeyTypeName_Provider.alloc( initialSize );
        this.numBuckets = initialSize;
        this.hashFunction = hashFunction;
        this.growthStrategy = growthStrategy;
        this.loadFactor = loadFactor;
        this.loadFactorSize = ( int ) ( initialSize * loadFactor );
    }

    /**
     * Return the current size of the HashSet, the number of unique values.
     *
     * @return the size
     */
    @Override
    public int getSize()
    {
        return size;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Does the Set contain <b>value</b>.
     *
     * @param value the value
     * @return {@inheritDoc}
     */
    @Override
    public int contains( _key_ value )
    {
        int bucket = getBucket( value );
        return inBucketList( bucket, value );
    }

    /** {@inheritDoc} */
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
     * @param key the value
     * @return the entry of the item (handle), or Const.NO_ENTRY
     */
    public int getEntry( _key_ key )
    {
        int bucket = getBucket( key );
        return inBucketList( bucket, key );
    }

    /**
     * UNCHECKED method to retrieve an item in the set. This should be used with caution, as it may potentially
     * return a value that was removed. See {@link #getEntry(_key_)}  above for getting a specific value.
     *
     * @param entry the entry into the set
     * @return the value
     */
    @Override
    public _key_ get( int entry )
    {
        return keys[ entry ];
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <p>
     * Inserting an item into the HashSet guarantees that if the item is already in the collection,
     * this will not insert the item, and instead return the handle of the already inserted keys. This
     * will satisfy the nature of a Set to contain only unique keys.
     * </p>
     * <p/>
     * <p>The construct of this method allows for a quick check to see if an item exists in a HashSet. If
     * one tries to insert an item, and the size does not change, then the item did exist in the Set. This
     * will only perform the HashFunction once on the item, and heavy users of the HashSet will find this
     * useful for performance reasons.
     * </p>
     *
     * @param key the key to insert
     * @return the handle to this key, whether already inserted or not
     */
    @Override
    public int insert( _key_ key )
    {
        if( size == loadFactorSize )
        {
            reHash();
        }
        int bucket = getBucket( key );
        int entry;
        //if our key exists in linked list, return its entry
        if( ( entry = inBucketList( bucket, key ) ) != Const.NO_ENTRY )
        {
            return entry;
        }
        entry = getNextEntry();
        keys[ entry ] = key;
        bucketList.insert( bucket, entry );   // we insert the entry into the bucket list, this means that when
        // we iterate over bucket, we get entries that will point to <b>keys</b>
        // array
        size++;
        return entry;
    }

    /**
     * {@inheritDoc}
     *
     * @param value the value to remove
     * @return true if we removed the item, false otherwise
     */
    @Override
    public int remove( _key_ value )
    {
        int bucket = getBucket( value );
        int entry = inBucketList( bucket, value );
        if( entry == Const.NO_ENTRY )
        {
            return entry;
        }
        bucketList.remove( bucket, entry );
        size--;
        addEntryToFreeList( entry );
        return entry;
    }


    /**
     * <p>When we hit the load factor, we double our bucket size and
     * rehash the items in the collection. Notice that this does
     * not effect the items in the keys at all, we are just re-mapping
     * the entries to different buckets.</p>
     */
    private void reHash()
    {
        int newSize = growthStrategy.growthRequest( size, size + 1 );
        MultiLinkedListInt newBucketList = new MultiLinkedListInt( numBuckets * 2,
                                                                   newSize + numBuckets );
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
                newBucketList.uncheckedInsert( bucket, entry );
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
        keys = keyFactory.ensureArrayCapacity( keys, nextEntry + 1, growthStrategy );
        return nextEntry++;
    }


    /**
     * Return a bucket that this key will hash to. This will be within
     * the set of our possible buckets.
     *
     * @return the bucket
     */
    private int getBucket( _key_ key )
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
    protected int inBucketList( int bucket, _key_ key )
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
    public HashSet_KeyTypeName_ copy( HashSet_KeyTypeName_ target )
    {
        if( target == null ) //creating a new one
        {
            target = new HashSet_KeyTypeName_( keys.length, loadFactor, keyFactory, intFactory, hashFunction,
                                               growthStrategy );
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
        System.arraycopy( freeList, 0, target.freeList, 0, freeListLen );

        //get a deep copy of the bucket list for the target
        target.bucketList = bucketList.getDeepCopy();
        return target;

    }

    public GrowthStrategy getGrowthStrategy()
    {
        return growthStrategy;
    }
}
