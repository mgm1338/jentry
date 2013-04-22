package collections.relationship;

import core.Const;
import core.NumberUtil;
import core.array.GrowthStrategy;

import java.util.Arrays;

/**
 * Copyright 2/8/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/8/13
 * <p/>
 * Extension of the {@link OneToManyInt}, however one may cycle through the associations from either side. Specifically
 * one may get all the left associations for a given right. For structure, see {@link OneToManyInt}, which
 * explains the storage mechanism of the associations.
 */
public class ManyToManyInt extends OneToManyInt
{

    protected boolean countRights;
    protected int[] rightCounts = null;

    /** Array that will hold the first relationship from the index, will be the handle into <b>associations</b> */
    protected int[] rights;
    /** The next pointer that will fill the index of the current entry to the next handle (see class docs) */
    protected int[] rightNexts;
    /** High water mark for the largest right that has been associated */
    protected int rightHighWaterMark = Const.NO_ENTRY;


    /**
     * Constructor
     *
     * @param initialLefts        the initial number of lefts that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialRights       the initial number of rights that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialAssociations the size of initial number of associations we can make.
     * @param countLefts          whether or not to track the counts of our associations per left.
     * @param countRights         whether or not to track the counts of our associations per right.
     */
    public ManyToManyInt( int initialLefts, int initialRights, int initialAssociations, boolean countLefts, boolean countRights )
    {
        this( initialLefts, initialRights, initialAssociations, countLefts, countRights, GrowthStrategy.doubleGrowth,
              ArrayFactoryInt.defaultIntProvider );
    }

    /**
     * Full Constructor
     *
     * @param initialLefts        the initial number of lefts that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialRights       the initial number of rights that we can hold (note that is it compact, so inserting
     *                            above this number will immediately cause a growth)
     * @param initialAssociations the size of initial number of associations we can make.
     * @param countLefts          whether or not to track the counts of our associations per left.
     * @param countRights         whether or not to track the counts of our associations per right.
     * @param growthStrategy      the growth strategy for growing our lefts array, associations array,
     *                            and counts array (if counting the associations)
     * @param intFactory          factory used to grow/allocate the arrays
     */
    public ManyToManyInt( int initialLefts, int initialRights, int initialAssociations, boolean countLefts,
                          boolean countRights, GrowthStrategy growthStrategy, ArrayFactoryInt intFactory )
    {
        super( initialLefts, initialAssociations, countLefts, growthStrategy, intFactory );
        this.countRights = countRights;
        if( countRights )
        {
            rightCounts = intFactory.alloc( initialRights );
        }
        rights = intFactory.alloc( initialRights, Const.NO_ENTRY );
        rightNexts = intFactory.alloc( initialAssociations, Const.NO_ENTRY );
    }

    public int getCountForRight( int right )
    {
        if( countRights ) return rightCounts[ right ];
        int entry = getNextLeftEntry( right, Const.NO_ENTRY );
        int ct = 0;
        while( entry != Const.NO_ENTRY )
        {
            ct++;
            entry = getNextLeftEntry( right, entry );
        }
        return ct;
    }

    public int getNextLeftEntry( int right, int prevEntry )
    {
        if( prevEntry == Const.NO_ENTRY )
        {
            return rights[ right ];
        }
        return rightNexts[ prevEntry ];
    }

    /**<p>Note: this is a mirror of getAllRightAssociations, however it is much more clear and reable
     * to keep these functions separate (merging them together made the common functionality a little hard
     * to understand).</p>
     *
     * For a given right, return the values of the left in the target array. If the target array
     * is null, then we will return an array that is completely full of associations. If the array
     * is larger than the number of associations (or if our growth strategy grows the array past that number),
     * then we will mark the end of the associations with the <b>mark</b> passed.
     *
     * @param right   the right that we would like to get the associations for
     * @param target the target array
     * @return the target array that contains all the associations for a right.
     */
    public int[] getAllLeftAssociations( int right, int[] target, int mark )
    {
        if( countRights )
        {
            int len = rightCounts[ right ];
            if( target == null || target.length < len )
            {
                target = intFactory.alloc( len );
            }
        }
        if( target == null )
        {
            target = intFactory.alloc( DEFAULT_REL_SIZE );
        }
        int entry = getNextLeftEntry( right, Const.NO_ENTRY );
        int ct = 0;
        if( countRights ) //we know we have perfectly sized array
        {
            while( entry != Const.NO_ENTRY )
            {
                target[ ct++ ] = getLeft( entry );
                entry = getNextLeftEntry( right, entry );
            }
        }
        else   //must ensure capacity and add mark
        {
            while( entry != Const.NO_ENTRY )
            {
                target = intFactory.ensureArrayCapacity( target, ct + 1, growthStrategy );
                target[ ct++ ] = getLeft( entry );
                entry = getNextLeftEntry( right, entry );
            }
        }
        if( ct < target.length )
        {
            target[ ct ] = mark;
        }
        return target;
    }


    /**
     * {@inheritDoc}
     *
     * <p>
     * Will ensure capacity of the right, rightNext, rightCount (if applicable) arrays so that
     * we ensure that we can insertLeft the association.
     * </p>
     * <p>
     * This will be a mirror of the ensuring capacity on the left side.
     * </p>
     * @param left   left int of the association
     * @param right  right int of the association
     * @param handle handle into the {@link collections.hash.set.HashSetLong}
     */
    @Override
    protected void ensureCapacity( int left, int right, int handle )
    {
        super.ensureCapacity( left, right, handle );
        if( right > rightHighWaterMark ) //guarding growth checks
        {
            rights = intFactory.ensureArrayCapacity( rights, right + 1, Const.NO_ENTRY, growthStrategy );
            rightHighWaterMark = right;
            if( countRights )
            {
                rightCounts = intFactory.ensureArrayCapacity( rightCounts, right + 1, growthStrategy );
            }
        }
        if( countRights )
        {
            rightCounts[ right ]++;
        }
        //we will never grow the rightNexts past the handle +1 (we use the handle to determine where we are
        //inserting into the array)
        rightNexts = intFactory.ensureArrayCapacity( rightNexts, handle + 1, Const.NO_ENTRY, growthStrategy );
    }


    /**
     *
     * <p>
     * Do the insertion of the item into the linked list of the right side. If this is the first item
     * to be associated to this left, then it is inserted into the <i>lefts array</i>. Otherwise,
     * iterate through the <i>leftNexts</i>, inserting the handle at the end of the chain.
     * </p>
     *
     *
     * @param right right int of the association
     * @param handle the handle we are going to insertLeft for this association
     */
    protected void insertRight(int right, int handle)
    {
       insertIntoLinkedList( right, handle, rights, rightNexts );
    }

    /**
     * <p>
     * Associate two integers, insertLeft the composed long into our associations. Insert the resulting handle into our
     * singly linked list type of structure. See {@link collections.relationship.OneToManyInt} base description for a more verbose description
     * of the insertion process via example.
     * </p>
     * <p>
     * The insertion gives us the ability to  do iterations from both the left and right. If we only require
     * iterating from one side, use the {@link OneToManyInt}. We return a handle to this association,
     * so parallel information related to this association can be stored in a compact array (similar to what we do
     * internally in the structure).
     * </p>
     *
     * @param left  left integer, this is the "one" side
     * @param right right integer, this is one of the 'many' rights associated with the specified left
     * @return the handle to this association
     */
    @Override
    public int associate( int left, int right )
    {
        long composed = NumberUtil.packLong( left, right );
        int preSize = associations.getSize();
        int handle = associations.insert( composed );
        if( associations.getSize() == preSize ) //does insertion check without re-hashing long
        {
            return handle;
        }
        ensureCapacity( left, right, handle );
        insertLeft( left, handle );
        insertRight(right, handle);
        size++;
        return handle;
    }


    /**
     * Remove an item from the linked list formed from the <i>rights</i> and <i>rightNexts</i>
     * arrays.
     *
     * @param right int for the left association
     * @param entry entry in the list
     * @return the entry, or -1 if we could not find the item
     */
    protected int removeRight(int right, int entry)
    {
        return removeFromLinkedList( right, entry, rights, rightNexts );
    }

    /**
     * Disassociate the two integers and return the entry that holds their association.
     * If the two numbers are not associated, return -1.
     *
     * @param left  the left int
     * @param right the right int
     * @return the entry of the association, or -1 if not assocaited
     */
    @Override
    public int disassociate( int left, int right )
    {
        long val = NumberUtil.packLong( left, right );
        int entry = associations.getEntry( NumberUtil.packLong( left, right ) );
        //check existence, return false if doesn't
        if( entry == Const.NO_ENTRY )
        {
            return -1;
        }
        associations.remove( val );
        size--;
        if( countLefts )
        {
            leftCounts[ left ]--;
        }
        if( countRights )
        {
            rightCounts[ right ]--;
        }
        int removedLeft = removeLeft( left, entry );
        if( removeRight( right, entry ) != removedLeft )
        {
            throw new IllegalStateException( "Many to Many found different entries for same removal." );
        }
        return removedLeft;
    }

    /**
     * Creates a deep copy of this ManyToManyInt by copying all of its attributes to the target. If the target is null,
     * then this method will create a new HashSet to copy all of its attributes to. Note that if it is not null,
     * all final attributes (Growth Strategy and IntFactory) cannot be copied.
     *
     * @param target the target HashSet, may be null
     * @return the deep copy of this
     */
    public ManyToManyInt copy( ManyToManyInt target )
    {
        int leftLen = lefts.length;
        int rightLen = rights.length;
        if( target == null )
        {
            target = new ManyToManyInt( leftLen, rightLen,
                                        associations.getSize(), this.countLefts, this.countRights, growthStrategy,
                                        intFactory );
        }
        copyOneToManyState( target, leftLen );
        if( countRights )     //cannot change counting
        {
            if( !target.countRights )  //if we didnt count, need to create array
            {
                target.rightCounts = target.intFactory.alloc( rightLen );
                target.countRights = true;

            }
            else  //just make sure same size
            {
                target.rightCounts = intFactory.ensureArrayCapacity( target.rightCounts, rightLen,
                                                                     GrowthStrategy.toExactSize );
            }
            System.arraycopy( rightCounts, 0, target.rightCounts, 0, rightLen);
        }
        target.rights = intFactory.ensureArrayCapacity( target.rights, rightLen, GrowthStrategy.toExactSize );
        System.arraycopy( rights, 0, target.rights, 0, rightLen );
        int nextLen = rightNexts.length;
        target.rightNexts = intFactory.ensureArrayCapacity( target.rightNexts, nextLen, GrowthStrategy.toExactSize );
        System.arraycopy( rightNexts, 0, target.rightNexts, 0, nextLen );
        return target;
    }


    /** Clear the collection, empty out all of its contents. */
    @Override
    public void clear()
    {
        super.clear();
        rightHighWaterMark++; //need extra size for Arrays.fill (last idx is exclusive)
        Arrays.fill( rights, 0, rightHighWaterMark, Const.NO_ENTRY );
        Arrays.fill( rightNexts, Const.NO_ENTRY );
        size = 0;
        if( countLefts )
        {
            Arrays.fill( rightCounts, 0, rightHighWaterMark, 0 );
        }
        rightHighWaterMark = Const.NO_ENTRY;
    }
}
