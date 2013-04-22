package collections.heap;

import collections.generic.heap.Heap_KeyTypeName_;
import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactory_KeyTypeName_;
import core.array.factory.*;
import core.stub.*;
import core.util.comparator.Comparator_KeyTypeName_;

import java.util.Arrays;

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
 * from a Binary tree), so the Heap should not be used for searching. The Heap's
 * purpose is collecting the 'greatest' item as its top element. Another typical
 * use is sorting items with a relatively small memory footprint.
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
 * extra space. When visualizing a tree from an array, the indices in the array are as follows:
 * <p/>
 * <pre>
 *          1
 *        /   \
 *       2     3
 *      / \   / \
 *     4  5  6   7
 * </pre>
 * and so on.
 * </p>
 * <p>
 * The tree array will store the entries of the collection that are inserted, not the values themselves. This requires
 * an array that will store the values (<i>key</i> array). We also need an 'inverse array', which for a given key
 * , will store the spot in the tree. This last array will allow us to remove a key without having to search. Like
 * the other Jentry structure, upon insertion, a handle to the key will be returned. This handle, when
 * used to retrieve an item, is guaranteed to return the key inserted. Upon removal of a key, the corresponding
 * handle will be returned.
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
 * 0     7       -1        1
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
 * 0     7       -1        2
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
 * 0     7       -1        2
 * 1     3        1        1
 * 2     10       0        3
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

    /**
     * Clear all the items in the heap, so that it may be re-used.
     * Note that the keys array is not actually cleared, but without
     * references to the entries, they will simply be overwritten upon
     * re-use.
     */
    @Override
    public void clear()
    {
        //clear tree array from 1 to treePtr
        Arrays.fill( tree, 1, treePtr, Const.NO_ENTRY );
        treePtr = 1;
        //clear inverse based on entry (entryPtr)
        Arrays.fill( inverse, 0, entryPtr, Const.NO_ENTRY );
        entryPtr = 0;

        Arrays.fill( freeList, Const.NO_ENTRY );
        freeListCt = 0;
        size = 0;
    }

    /**
     * Remove the top element (the 'greatest') element). If we only want to view the top
     * element, use {@link #peek()}
     */
    @Override
    public void removeGreatest()
    {
        if( size == 0 ) return;
        remove( tree[ 1 ] );
    }

    /**
     * Return the value of 'greatest' element. Will not modify the structure.
     *
     * @return the 'greatest' key.
     */
    @Override
    public _key_ peek()
    {
        return keys[ tree[ 1 ] ];
    }

    /**
     * Insert an item into the Heap. This consists of inserting the key
     * into the next entry, and then inserting the item into the last tree
     * spot. Following the insertion into the last tree spot, the tree
     * will test the node against its parent until the item is located in a
     * correct spot, see {@link #shiftUp(_key_, int)}  }.
     *
     * @param key the key to insert
     * @return the handle to this key
     */
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
     * Get the value inserted for a particular entry. See {@link #insert(_key_)}
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

    /**
     * <p>
     * Remove an item from the heap by entry. There is a related
     * convenience method for the greatest item (see {@link #removeGreatest()}  }.
     * This remove method is meant when data is associated with the entry
     * of the heap, and must be removed. Typically this method would not be
     * used by a traditional heap.
     * </p>
     * <p/>
     * <p>Steps are to find the spot in the tree of the entry (this is the
     * purpose of the <i>inverse array</i>, direct access of this step). After
     * removing the item from the tree, we flip the removed entry and the
     * last item in the tree (by array position). We shift-down this flipped
     * item with its children (and repeat the process), until the item
     * is in a correct place. See {@link #shiftDown(int)}  } for implementation.
     * </p>
     *
     * @param entry the entry of the item to remove
     * @return the entry that we have removed (not very useful here)
     *         or -1 if the entry was empty already.
     */
    @Override
    public int remove( int entry )
    {
        int treePtrRemoval = inverse[ entry ];
        //no item in the entry
        if( entry == Const.NO_ENTRY )
        {
            return Const.NO_ENTRY;
        }
        //flip removed spot with the last item in the tree
        int lastElPtr = --treePtr;
        flip( treePtrRemoval, lastElPtr );
        shiftDown( treePtrRemoval );
        //now remove the item and referenced to the item, which is in the last
        //spot in the tree
        int removalEntry = tree[ lastElPtr ];
        inverse[ removalEntry ] = Const.NO_ENTRY;
        tree[ lastElPtr ] = Const.NO_ENTRY;
        //add to free list
        freeList = intFactory.ensureArrayCapacity( freeList, freeListCt + 1, Const.NO_ENTRY,
                                                   growthStrategy
        );
        freeList[ freeListCt++ ] = removalEntry;
        size--;
        return entry;
    }

    /**
     * <p>
     * When an item is removed, the last element in the tree will replace it, and must be
     * 'shifted down' to its correct slot. Will compare the element to it's children. If it
     * is 'greater' than both children, or no children exist, no movement will occur.
     * </p>
     * <p>Otherwise, we compare the item to its children. We will determine the 'greatest'
     * of the children, and flip it with the parent. We now compare the parent to its
     * new children, and continue flipping it 'down' the tree until it is greater than
     * its children, or there are no children left.</p>
     *
     * @param parentPtr a pointer to the point in the tree where the parent exists.
     */
    protected void shiftDown( int parentPtr )
    {
        int left = leftChild( parentPtr );
        int right = rightChild( parentPtr );

        //while we have children, if its in the correct slot, will return in loop
        while( left != Const.NO_ENTRY || right != Const.NO_ENTRY )
        {
            //both valid, compare to the 'greater one'
            if( left != Const.NO_ENTRY && right != Const.NO_ENTRY )
            {
                int cmpResult = cmp.compare( keys[ tree[ left ] ], keys[ tree[ right ] ] );
                if( cmpResult > 0 ) //compare with left
                {
                    if( cmp.compare( keys[ tree[ parentPtr ] ], keys[ tree[ left ] ] ) < 0 )
                    {
                        flip( parentPtr, left );
                        parentPtr = left;
                    }
                    else
                    {
                        return;
                    }
                }
                else //compare with right
                {
                    if( cmp.compare( keys[ tree[ parentPtr ] ], keys[ tree[ right ] ] ) < 0 )
                    {
                        flip( parentPtr, right );
                        parentPtr = right;
                    }
                    else
                    {
                        return;
                    }
                }
            }
            else if( left != Const.NO_ENTRY )   //only have left child
            {
                if( cmp.compare( keys[ tree[ parentPtr ] ], keys[ tree[ left ] ] ) < 0 )
                {
                    flip( parentPtr, left );
                    parentPtr = left;
                }
                else
                {
                    return;
                }
            }
            else // only have right child
            {
                if( cmp.compare( keys[ tree[ parentPtr ] ], keys[ tree[ right ] ] ) < 0 )
                {
                    flip( parentPtr, right );
                    parentPtr = right;
                }
                else
                {
                    return;
                }
            }
            left = leftChild( parentPtr );
            right = rightChild( parentPtr );
        }
    }

    /**
     * Given a key and a spot in the tree, compare the item with its parent. If the item is greater than
     * its parent, flip it with its parent. Continue this process until this item is less than its parent
     * or it is at the root of the tree. This is used by {@link #insert(core.stub._key_)}; we always
     * insert at the end of the tree and shift items up until they are in the correct place.
     *
     * @param key     the key we are inserting
     * @param treePtr a pointer to the node in the tree
     */
    protected void shiftUp( _key_ key, int treePtr )
    {
        int parent = parent( treePtr );
        while( treePtr != 1 && cmp.compare( key, keys[ tree[ parent ] ] ) > 0 )
        {
            flip( treePtr, parent );
            treePtr = parent;
            parent = parent( treePtr );
        }
    }

    /**
     * Return the next free spot in the tree array. If there are no more spots in the array, we will attempt
     * to grow the tree in order to create more spots, and return the next one.
     *
     * @return the next free idx in the tree array
     */
    protected int getNextFreeTreeSpot()
    {
        tree = intFactory.ensureArrayCapacity( tree, treePtr + 2, Const.NO_ENTRY, growthStrategy );
        return treePtr++;
    }

    /**
     * Get the next free entry for key insertion. Uses a freelist to guarantee that entries are compact.
     *
     * @return the next free entry
     */
    protected int getNextEntry()
    {
        if( freeListCt != 0 )
        {
            return freeList[ --freeListCt ];
        }
        int nextEntry = entryPtr++;
        int minSize = nextEntry + 1;
        inverse = intFactory.ensureArrayCapacity( inverse, minSize, Const.NO_ENTRY, growthStrategy );
        keys = keyFactory.ensureArrayCapacity( keys, minSize, growthStrategy );
        return nextEntry;
    }

    /**
     * Flip the entries in the tree and the inverse arrays. A and b denote idx in the tree.
     *
     * @param a first node
     * @param b second node
     */
    protected void flip( int a, int b )
    {
        int entryA = tree[ a ];
        int entryB = tree[ b ];
        inverse[ entryA ] = b;
        inverse[ entryB ] = a;
        tree[ a ] = entryB;
        tree[ b ] = entryA;
    }

    /**
     * Return the left child of the parent given. If no child exists, return -1.
     *
     * @param parent the parent idx
     * @return the left child idx in the tree
     */
    protected int leftChild( int parent )
    {
        int left = parent * 2;
        if( left >= treePtr ) return Const.NO_ENTRY;
        return left;
    }

    /**
     * Return the right child of the parent given. If no child exists, return -1.
     *
     * @param parent the parent idx
     * @return the right child idx in the tree
     */
    protected int rightChild( int parent )
    {
        int right = ( parent * 2 ) + 1;
        if( right >= treePtr ) return Const.NO_ENTRY;
        return right;
    }

    /**
     * Return the parent of the child. Does NOT protect against a child of 0, while
     * the first valid parent is actually 1 by our convention.
     *
     * @param child the child idx
     * @return the parent idx in the tree
     */
    protected int parent( int child )
    {
        return ( child / 2 );
    }

    /**
     * Get the comparator that this Heap uses to order keys.
     *
     * @return the {@link Comparator_KeyTypeName_}
     */
    public Comparator_KeyTypeName_ getCmp()
    {
        return cmp;
    }
}
