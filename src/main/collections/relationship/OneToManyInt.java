package collections.relationship;


import collections.Collection;
import collections.hash.set.HashSetLong;
import core.Const;
import core.NumberUtil;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;

/**
 * Copyright 1/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/27/13
 * <p/>
 * <p/>
 * <p>IMPORTANT! THIS STRUCTURE ASSUME THAT THE LEFTS INSERTED ARE (Mostly) COMPACT. IF THEY ARE NOT, INSERT
 * INTO A JENTRY COLLECTION AND INSERT THE HANDLES.</p>
 * <p>
 * A collection of One (left) to Many (right) ints. This is used in conjunction
 * with the Jentry collections, associating the int handles together. The One to Many allows
 * for iteration over the associations from a left, going in order, as well as checking to see if a left
 * and right are associated.
 * </p>
 * <p/>
 * <p>The actual associations are held in a HashSetLong, using {@link core.NumberUtil#packLong(int, int)}, where
 * the right and left compose a long, which helps ensure that the same association is not added twice. The entries
 * in the HashSet become the key components to our collection. </p>
 * <p/>
 * <p>The internal structure of the OneToManyInt will take in two ints, compose them into a Long. The entries
 * into this HashSet will be stored in a unique way. The first inserted entry will take the index in lefts for
 * the actual left value (for instance, if we are associating (0,2), then index 0 will hold the HashSet entry
 * for the long composed of 0 and 2 (2). This fact is why the important note of usage, that we do not want to
 * associate only lefts that are sparse, for it will quickly allocate the lefts array.</p>
 * <p/>
 * <p>
 * Simple diagram inserting {1,3}, {1,4},{1,5},{0,1}. Followed by removal of {1,4}. Lastly re-use this index by
 * inserting {0,6}.
 * <p/>
 * Inserting {1-3}, gets handle of 0 from <b>associations</b>.
 * Insert 0 into lefts[1].
 * <pre>
 *      lefts   leftNexts
 * 0
 * 1     0
 * 2
 * </pre>
 * Inserting {1-4}, gets handle of 1 from <b>associations</b>
 * Follow the index of lefts[1], insert 1 into leftNexts[0]
 * <pre>
 *      lefts   leftNexts
 * 0               1
 * 1     0
 * 2
 * </pre>
 * Inserting {1-5}, gets handle of 2 from <b>associations</b>.We follow the list, and insert leftNexts into
 * index 2.
 * <pre>
 *      lefts   leftNexts
 * 0               1
 * 1     0         2
 * 2
 * </pre>
 * Inserting {0-1}, gets handle of 3 from <b>associations</b>. This is another simple entry of 3 into lefts[0].
 * <pre>
 *      lefts   leftNexts
 * 0     3         1
 * 1     0         2
 * 2
 * </pre>
 * Now, finally we have a removal of the second item of left of 1. In this case, we 'bubble' up the 2 to leftNexts[0].
 * <pre>
 *      lefts   leftNexts
 * 0     3         2
 * 1     0
 * 2
 * </pre>
 * <p/>
 * Note that an insertion now will result in index 1. We can continue to insert as normal,
 * and it will re-use the index.  We insert {0,6}, which gets handle 1.
 * <pre>
 *      lefts   leftNexts
 * 0     3         2
 * 1     0
 * 2
 * 3               1
 * </pre>
 * </p>
 */
public class OneToManyInt implements Collection
{
    /** Growth strategy for growing the lefts, nexts, and counts(if tracking) */
    protected final GrowthStrategy growthStrategy;
    /** Factory used to allocate and grow the int arrays */
    protected final ArrayFactoryInt intFactory;

    /** HashSet of longs that will hold the associations of the OneToMany */
    protected HashSetLong associations;
    /** Array that will hold the first relationship from the index, will be the handle into <b>associations</b> */
    protected int[] lefts;
    /** The next pointer that will fill the index of the current entry to the next handle (see class docs) */
    protected int[] leftNexts;
    /** An optional parallel array that will track the count of the associations for each left */
    protected int[] leftCounts = null;
    /** By default, we do not use this array */
    protected final boolean countLefts;
    /** Associations size tracker */
    protected int size = 0;

    /**
     * Constructor
     *
     * @param initialLefts        the initial number of lefts that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialAssociations the size of initial number of associations we can make.
     * @param countLefts          whether or not to track the counts of our associations per left.
     */
    public OneToManyInt( int initialLefts, int initialAssociations, boolean countLefts )
    {
        this( initialLefts, initialAssociations, countLefts, GrowthStrategy.doubleGrowth,
              ArrayFactoryInt.defaultIntProvider );
    }

    /**
     * Full Constructor
     *
     * @param initialLefts        the initial number of lefts that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialAssociations the size of initial number of associations we can make.
     * @param countLefts          whether or not to track the counts of our associations per left.
     * @param growthStrategy      the growth strategy for growing our lefts array, associations array,
     *                            and counts array (if counting the associations)
     * @param intFactory          factory used to grow/allocate the arrays
     */
    public OneToManyInt( int initialLefts, int initialAssociations, boolean countLefts,
                         GrowthStrategy growthStrategy, ArrayFactoryInt intFactory )
    {
        this.growthStrategy = growthStrategy;
        this.intFactory = intFactory;
        this.countLefts = countLefts;
        if( countLefts )
        {
            leftCounts = intFactory.alloc( initialLefts );
        }
        lefts = intFactory.alloc( initialLefts );
        leftNexts = intFactory.alloc( initialAssociations );

    }

    /**
     * <p>
     * Associate two integers, insert the composed long into our associations. Insert the resulting handle into our
     * singly linked list type of structure. See {@link OneToManyInt} base description for a more verbose description
     * of the insertion process via example.
     * </p>
     * <p>
     * This associates one 'left' to 0-n 'rights', so we can iterate over the associations from one left. If we need
     * the ability to do iterations from both the left and right, use the //TODO{@link ManyToManyInt} structure.
     * We return a handle to this association, so parallel information related to this association can be stored in
     * a compact array (similar to what we do internally in the structure).
     * </p>
     *
     * @param left  left integer, this is the "one" side
     * @param right right integer, this is one of the 'many' rights associated with the specified left
     * @return the handle to this association
     */
    public int associate( int left, int right )
    {
        long composed = NumberUtil.packLong( left, right );
        int handle = associations.insert( composed );
        lefts = intFactory.ensureArrayCapacity( lefts, left + 1, Const.NO_ENTRY, growthStrategy );
        if( countLefts )
        {
            leftCounts = intFactory.ensureArrayCapacity( leftCounts, left + 1, growthStrategy );
            leftCounts[ left ]++;
        }
        //we will never grow the leftNexts past the handle +1 (we use the handle to determine where we are
        //inserting into the array)
        leftNexts = intFactory.ensureArrayCapacity( leftNexts, handle + 1, Const.NO_ENTRY, growthStrategy );

        int testInsert = lefts[ left ];
        if( testInsert == Const.NO_ENTRY ) //first item, insert into the left array
        {
            lefts[ left ] = handle;
        }
        else
        {
            testInsert = leftNexts[ handle ]; //cycle through leftNexts
            while( leftNexts[ testInsert ] != Const.NO_ENTRY )
            {
                testInsert = leftNexts[ handle ];
            }
            leftNexts[ testInsert ] = handle;
        }
        size++;
        return handle;
    }

    /**
     * Get the current size of the collection.
     *
     * @return the size
     */
    @Override
    public int getSize()
    {
        return size;
    }

    /**
     * Is the current collection empty.
     *
     * @return true if collection is empty, false otherwise
     */
    @Override
    public boolean isEmpty()
    {
        return size==0;
    }

    /** Clear the collection, empty out all of its contents. */
    @Override
    public void clear()
    {

    }

    /**
     * Check if two numbers are associated. Simple check against HashSetLong.
     *
     * @param left left to check
     * @param right right to check
     * @return true if associated, false otherwise
     */
    public boolean isAssociated( int left, int right )
    {
        return associations.contains( NumberUtil.packLong( left, right ) );
    }

    /**
     * <p>
     * Method used for iterating through the rights for one left. Will return the next entry that contains our
     * desired right, requiring the previous entry, as well as the left. If we are just starting to iterate, the
     * <b>prevEntry</b> should be Const.NO_ENTRY.
     * </p>
     * @param left
     * @param prevEntry
     * @return
     */
    public int getNextRightEntry( int left, int prevEntry )
    {
       if (prevEntry==Const.NO_ENTRY)
       {
           return lefts[left];
       }
       return leftNexts[prevEntry];
    }

    /**
     * Get the right integer in an entry of our collection.
     *
     * @param entry the entry of the association
     * @return the right integer in the association
     */
    public int getRight( int entry )
    {
        return NumberUtil.getRight( associations.get( entry ));
    }

    /**
     * Get the left integer in an entry of our collection.
     *
     * @param entry the entry of the association
     * @return the left integer in the association
     */
    public int getLeft( int entry )
    {
        return NumberUtil.getLeft( associations.get( entry ));
    }

    /**
     * For a given left, return the values of the right in the target array.
     *
     * @param left
     * @param target
     * @return
     */
    public int[] getAllRightAssociations( int left, int[] target )
    {
        return null;
    }

    /**
     * For a given left, return the values of the right in the target array.
     *
     * @param left
     * @param target
     * @return
     */
    public int[] getAllRightAssociations( int left, int[] target, int mark )
    {
        return null;
    }

    public boolean disassociate( int left, int right )
    {
        return true;
    }

    public int getCountForLeft( int left )
    {
        return 0;
    }

    public OneToManyInt copy( OneToManyInt target )
    {
        return null;
    }
}
