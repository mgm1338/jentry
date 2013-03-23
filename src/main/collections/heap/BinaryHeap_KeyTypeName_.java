package collections.heap;

import collections.generic.heap.Heap_KeyTypeName_;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactory_KeyTypeName_;
import core.stub._key_;
import core.util.comparator.Comparator_KeyTypeName_;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p>A Binary heap of keys</p>
 * <p/>
 * <p>The Heap will take a {@link Comparator_KeyTypeName_} ordering it
 * so that the parent item will always evalute to be greater than the children.
 * For instance, the {@link core.util.comparator.Comparators._KeyTypeName_Asc}
 * will ensure that the parent item will be less (in value) than the children items
 * .<p>
 * <p>The following heaps (using int as type) are both valid for an IntAsc
 * Comparator.
 * <pre>
 *            1
 *          /  \
 *         3    2
 *        / \
 *      10  11
 * </pre>
 * and also,
 * <pre>
 *         1
 *       /  \
 *      10   2
 *          / \
 *         3  11
 * </pre>
 * <p/>
 * Note that the same elements can take different orders in a Heap (differing
 * from a Binary tree), so the Heap should used for searching. The Heap's
 * purpose is collecting the 'greatest' item as its top element. Another typical
 * use is that removing items from a heap results in a sorted collection, with a
 * relatively small memory footprint.
 * </p>
 * <p/>
 * <p>The singly-typed heap is simply a collection of keys, that are guaranteed
 * to be sorted as described above. The Heap follows the general convention
 * of other Jentry structures, that insertion will yield a handle to that item.
 * The Heap guarantees that retrieval using that entry will remain consistent,
 * and that upon removal, the next entry will re-use the vacated spot.</p>
 * <p/>
 * <p/>
 * <p>The Heap has the typical insertion and removal conventions, such that
 * the insertion and removal is done in log(n). To insert, we insert
 * into the next natural leaf position and flip the item with its parents
 * until it is in the correct position. When removing, we flip the removed
 * item with the top position, and switch the item with its children
 * until it is in the correct position.</p>
 * <p/>
 * <p/>
 * <p><strong>Structure</strong></p>
 * <p/>
 * Internally the Heap will store its items in a binary tree formed as an array.
 * The array will start at index 1 for the sole reason that doing parent
 * and child math is quicker than starting from 0, and it doesn't use much
 * extra space. When visualizing a tree from an array, the indexes are as follows:
 * <p/>
 * <pre>
 *          1
 *        /   \
 *       2    3
 *      / \  / \
 *     4  5  6  7
 * </pre>
 * and so on.
 * </p>
 * <p>
 * The tree elements will actually store the entries of the collection that
 * are inserted. Therefore the actual key elements do not move once they
 * are inserted, only the 'pointers' to the key array (example following).
 * There is one more array, that will store the 'inverse' tree array. For
 * a given entry, upon removal it is necessary to know where in the tree
 * the entry is currently located. The extra state in the inverse array
 * allows us to remove without having to search through the heap.
 * </p>
 * <p/>
 * <p><b>Example: Simple Insertion</b></p>
 * <p/>
 * Assume HeapInt and Comparator IntAsc.
 * <p/>
 * Insert 7, 3, 10, 2.
 * <p/>
 * Insert 7:
 * <pre>
 *  tree:
 *          7
 *
 *  arrays:
 *      keys    tree    inverse
 * 0     7       -1        1      <- denotes entry 0 is at spot 1 in tree array
 * 1              0
 * 2
 * 3
 * 4
 * 5
 * </pre>
 * <p/>
 * Insert 3:
 * <pre>
 *  tree:
 *          3
 *         /
 *        7
 *
 *  arrays:
 *      keys    tree    inverse
 * 0     7       -1        0
 * 1     3        1        1
 * 2              0
 * 3
 * 4
 * 5
 * </pre>
 * <p/>
 * Insert 10:
 * <pre>
 *  tree:
 *          3
 *         / \
 *        7  10
 *
 *  arrays:
 *      keys    tree    inverse
 * 0     7       -1        0
 * 1     3        1        1
 * 2     10       0        2
 * 3              2
 * 4
 * 5
 * </pre>
 * <p/>
 * Insert 2:
 * <pre>
 *  tree:
 *          2
 *         / \
 *        3  10
 *       /
 *      7
 *
 *  arrays:
 *      keys    tree    inverse
 * 0     7       -1        4
 * 1     3        3        2
 * 2     10       1        3
 * 3     2        2        1
 * 4              0
 * 5
 * </pre>
 */
public class BinaryHeap_KeyTypeName_ implements Heap_KeyTypeName_
{
    /**
     * Factory providing the int arrays that will make up the tree
     * and inverse arrays.
     */
    protected final ArrayFactoryInt intFactory;
    /** {@link Comparator_KeyTypeName_} that will order the keys from greatest to least */
    protected final Comparator_KeyTypeName_ cmp;
    /** Key factory that will allocate new typed keys */
    protected final ArrayFactory_KeyTypeName_ keyFactory;
    /** Growth strategy for growing the arrays in the heap */
    protected final GrowthStrategy growthStrategy;


    /** current size of the heap (in elements) */
    private int size = 0;
    /** Entry to the key values (not sorted array) */
    protected _key_[] keys;
    /** The binary tree node to the entry it stores */
    protected int[] tree;
    /** For each entry (as index), the node it occupies in the tree */
    protected int[] inverse;
    /** Pointer to the next entry that is unoccupied */
    protected int entryPtr = 0;
    /** List of vacated entries, that will be used first upon insertions */
    protected int[] freeList;
    /** Pointer to the next free entry to use in the free list */
    int freeListCt = 0;
    /**
     * Pointer to the next free item in the tree (we start with 1 to simplify parent/child operations).
     * The tree will always be vacant in index 0
     */
    int treePtr = 1;

    /**
     * Constructor
     *
     * @param initialCapacity the initial capacity of the heap
     * @param cmp             {@link Comparator_KeyTypeName_} that will order the elements from greatest to least
     */
    public BinaryHeap_KeyTypeName_( int initialCapacity, Comparator_KeyTypeName_ cmp )
    {
        this( initialCapacity, cmp, ArrayFactoryInt.defaultIntProvider,
              ArrayFactory_KeyTypeName_.default_KeyTypeName_Provider,
              GrowthStrategy.doubleGrowth );
    }

    /**
     * Full Constructor
     *
     * @param initialCapacity the initial capacity of the heap
     * @param cmp             {@link Comparator_KeyTypeName_} that will order the elements from greatest to least
     * @param intFactory      factory that will allocate arrays for the tree, inverse, and freelist
     * @param keyFactory      the factory that allocates the keys for the Heap
     * @param growthStrategy  strategy for growing the arrays in this Heap
     */
    public BinaryHeap_KeyTypeName_(
            int initialCapacity, Comparator_KeyTypeName_ cmp, ArrayFactoryInt intFactory,
            ArrayFactory_KeyTypeName_ keyFactory, GrowthStrategy growthStrategy )
    {
        this.cmp = cmp;
        this.intFactory = intFactory;
        this.keyFactory = keyFactory;
        this.growthStrategy = growthStrategy;
        tree = intFactory.alloc( initialCapacity, Const.NO_ENTRY );
        inverse = intFactory.alloc( initialCapacity );
        keys = keyFactory.alloc( initialCapacity );
        freeList = intFactory.alloc( 4 );
    }

    /**
     * Get the current size (in elements) of the heap.
     *
     * @return the number of keys in the heap
     */
    @Override
    public int getSize()
    {
        return size;
    }

    /**
     * Returns true if the Heap is empty, false otherwise.
     *
     * @return true if the Heap is empty, false otherwise.
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public void removeGreatest()
    {
        if( size == 0 ) return;
        remove( tree[ 1 ] );
    }

    @Override
    public _key_ peek()
    {
        return keys[ tree[ 1 ] ];
    }


    @Override
    public int insert( _key_ key )
    {
        int entry = getNextEntry();
        keys[ entry ] = key;
        int treeSpot = getNextFreeTreeSpot();
        tree[ treeSpot ] = entry;
        inverse[ entry ] = treeSpot;
        shiftUp( key, treeSpot );
        size++;
        return entry;
    }


    /**
     * Get the value inserted for a particular entry. See {@link #insert(core.stub._key_)}
     * for description of the handles, called entries. For the entry passed
     * here, is guaranteed to return the same value that was returned upon
     * insertion.
     *
     * @param entry the entry for the item
     * @return the value of the item
     */
    public _key_ get( int entry )
    {
        return keys[ entry ];
    }

    @Override
    public int remove( int entry )
    {
        int treePtrRemoval = inverse[ entry ];
        if( entry == Const.NO_ENTRY )
        {
            return Const.NO_ENTRY;
        }
        int lastElPtr = --treePtr;
        flip( treePtrRemoval, lastElPtr );
        shiftDown( treePtrRemoval );
        //now remove the treePtrRemoval
        int removalEntry = tree[ lastElPtr ];
        inverse[ removalEntry ] = Const.NO_ENTRY;
        tree[ lastElPtr ] = Const.NO_ENTRY;
        freeList = intFactory.ensureArrayCapacity( freeList, freeListCt + 1, Const.NO_ENTRY,
                                                   growthStrategy
        );
        freeList[ freeListCt++ ] = removalEntry;
        size--;
        return entry;
    }

    protected void shiftDown( int curSpot )
    {
        int left = leftChild( curSpot );
        int right = rightChild( curSpot );
        if( left >= treePtr ) left = Const.NO_ENTRY;
        if( right >= treePtr ) right = Const.NO_ENTRY;

        while( left != Const.NO_ENTRY || right != Const.NO_ENTRY )
        {
            //both valid, compare to the 'greater one'
            if( left != Const.NO_ENTRY && right != Const.NO_ENTRY )
            {
                int cmpResult = cmp.compare( keys[ tree[ left ] ], keys[ tree[ right ] ] );
                if( cmpResult > 0 ) //compare with left
                {
                    if( cmp.compare( keys[ tree[ curSpot ] ], keys[ tree[ left ] ] ) < 0 )
                    {
                        flip( curSpot, left );
                        curSpot = left;
                    }
                    else
                    {
                        return;
                    }
                }
                else //compare with right
                {
                    if( cmp.compare( keys[ tree[ curSpot ] ], keys[ tree[ right ] ] ) < 0 )
                    {
                        flip( curSpot, right );
                        curSpot = right;
                    }
                    else
                    {
                        return;
                    }
                }
            }
            else if( left != Const.NO_ENTRY )
            {
                if( cmp.compare( keys[ tree[ curSpot ] ], keys[ tree[ left ] ] ) < 0 )
                {
                    flip( curSpot, left );
                    curSpot = left;
                }
                else
                {
                    return;
                }
            }
            else // right!=Const.Entry
            {
                if( cmp.compare( keys[ tree[ curSpot ] ], keys[ tree[ right ] ] ) < 0 )
                {
                    flip( curSpot, right );
                    curSpot = right;
                }
                else
                {
                    return;
                }
            }
            left = leftChild( curSpot );
            right = rightChild( curSpot );
            if( left >= treePtr ) left = Const.NO_ENTRY;
            if( right >= treePtr ) right = Const.NO_ENTRY;
        }
    }

    protected void shiftUp( _key_ key, int curSpot )
    {
        int parent = parent( curSpot );
        while( curSpot != 1 && cmp.compare( key, keys[ tree[ parent ] ] ) > 0 )
        {
            flip( curSpot, parent );
            curSpot = parent;
            parent = parent( parent );
        }
    }

    protected int getNextFreeTreeSpot()
    {
        tree = intFactory.ensureArrayCapacity( tree, treePtr + 2, Const.NO_ENTRY, growthStrategy );
        inverse = intFactory.ensureArrayCapacity( inverse, treePtr + 2, Const.NO_ENTRY, growthStrategy );
        return treePtr++;
    }

    protected int getNextEntry()
    {
        if( freeListCt != 0 )
        {
            return freeList[ --freeListCt ];
        }
        int nextEntry = entryPtr++;
        keys = keyFactory.ensureArrayCapacity( keys, nextEntry + 1,
                                               growthStrategy );
        return nextEntry;
    }

    protected void flip( int a, int b )
    {
        int entryA = tree[ a ];
        int entryB = tree[ b ];
        inverse[ entryA ] = b;
        inverse[ entryB ] = a;
        tree[ a ] = entryB;
        tree[ b ] = entryA;
    }

    protected int leftChild( int parent )
    {
        return parent * 2;
    }

    protected int rightChild( int parent )
    {
        return ( parent * 2 ) + 1;
    }

    protected int parent( int child )
    {
        return ( child / 2 );
    }

    public Comparator_KeyTypeName_ getCmp()
    {
        return cmp;
    }
}
