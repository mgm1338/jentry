package collections.heap;

import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactory_KeyTypeName_;
import core.stub.*;
import core.util.comparator.Comparator_KeyTypeName_;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class Heap_KeyTypeName_
{
    protected final ArrayFactoryInt intFactory;
    protected final Comparator_KeyTypeName_ cmp;
    protected final ArrayFactory_KeyTypeName_ keyFactory;
    protected final GrowthStrategy growthStrategy;


    private int size = 0;
    protected _key_[] keys;
    protected int[] tree;
    protected int[] inverse;
    protected int entryPtr = 0;
    protected int[] freeList;
    int freeListCt = 0;
    int treePtr = 1;


    public Heap_KeyTypeName_ (int initialCapacity, Comparator_KeyTypeName_ cmp)
    {
        this (initialCapacity, cmp, ArrayFactoryInt.defaultIntProvider,
              ArrayFactory_KeyTypeName_.default_KeyTypeName_Provider,
              GrowthStrategy.doubleGrowth);
    }

    public Heap_KeyTypeName_ (
            int initialCapacity, Comparator_KeyTypeName_ cmp, ArrayFactoryInt intFactory,
            ArrayFactory_KeyTypeName_ keyFactory, GrowthStrategy growthStrategy)
    {
        this.cmp = cmp;
        this.intFactory = intFactory;
        this.keyFactory = keyFactory;
        this.growthStrategy = growthStrategy;
        tree = intFactory.alloc (initialCapacity, Const.NO_ENTRY);
        inverse = intFactory.alloc (initialCapacity);
        keys = keyFactory.alloc (initialCapacity);
        freeList = intFactory.alloc (4);
    }

    public int getSize ()
    {
        return size;
    }

    public boolean isEmpty ()
    {
        return size == 0;
    }

    public void removeTop ()
    {
        remove (tree[1]);
    }

    public _key_ peek ()
    {
        return keys[tree[1]];
    }

    public int insert (_key_ key)
    {
        int entry = getNextEntry ();
        keys[entry] = key;
        int treeSpot = getNextFreeTreeSpot ();
        tree[treeSpot] = entry;
        inverse[entry] = treeSpot;
        shiftUp (key, treeSpot);
        size++;
        return entry;
    }

    public boolean remove (int entry)
    {
        int treePtrRemoval = inverse[entry];
        if (entry == Const.NO_ENTRY)
        {
            return false;
        }
        int lastElPtr = --treePtr;
        flip (treePtrRemoval, lastElPtr);
        shiftDown (treePtrRemoval);
        //now remove the treePtrRemoval
        int removalEntry = tree[lastElPtr];
        inverse[removalEntry] = Const.NO_ENTRY;
        tree[lastElPtr] = Const.NO_ENTRY;
        freeList = intFactory.ensureArrayCapacity (freeList, freeListCt + 1,
                                                   growthStrategy, Const.NO_ENTRY
        );
        freeList[freeListCt++] = removalEntry;
        size--;
        return true;
    }

    protected void shiftDown (int curSpot)
    {
        int left = leftChild (curSpot);
        int right = rightChild (curSpot);
        if (left >= treePtr) left = Const.NO_ENTRY;
        if (right >= treePtr) right = Const.NO_ENTRY;

        while (left != Const.NO_ENTRY && right != Const.NO_ENTRY)
        {
            //both valid, compare to the 'greater one'
            if (left != Const.NO_ENTRY && right != Const.NO_ENTRY)
            {
                int cmpResult = cmp.compare (keys[tree[left]], keys[tree[right]]);
                if (cmpResult <= 0) //left is the 'greater' one
                {
                    if (cmp.compare (keys[tree[curSpot]], keys[tree[left]]) > 0)
                    {
                        flip (curSpot, left);
                        curSpot = left;
                    }
                    else
                    {
                        return;
                    }
                }
                else
                {
                    if (cmp.compare (keys[tree[curSpot]], keys[tree[right]]) > 0)
                    {
                        flip (curSpot, right);
                        curSpot = right;
                    }
                    else
                    {
                        return;
                    }
                }
            }
            else if (left != Const.NO_ENTRY)
            {
                if (cmp.compare (keys[tree[curSpot]], keys[tree[left]]) > 0)
                {
                    flip (curSpot, left);
                    curSpot = left;
                }
                else
                {
                    return;
                }
            }
            else // right!=Const.Entry
            {
                if (cmp.compare (keys[tree[curSpot]], keys[tree[right]]) > 0)
                {
                    flip (curSpot, right);
                    curSpot = right;
                }
                else
                {
                    return;
                }
            }
            left = leftChild (curSpot);
            right = rightChild (curSpot);
            if (left >= treePtr) left = Const.NO_ENTRY;
            if (right >= treePtr) right = Const.NO_ENTRY;
        }
    }

    protected void shiftUp (_key_ key, int curSpot)
    {
        int parent = parent (curSpot);
        while (curSpot != 1 && cmp.compare (key, keys[tree[parent]]) <= 0)
        {
            flip (curSpot, parent);
            curSpot = parent;
            parent = parent (parent);
        }
    }

    protected int getNextFreeTreeSpot ()
    {
        tree = intFactory.ensureArrayCapacity (tree, treePtr + 2, growthStrategy, Const.NO_ENTRY);
        inverse = intFactory.ensureArrayCapacity (inverse, treePtr + 2, growthStrategy, Const.NO_ENTRY);
        return treePtr++;
    }

    protected int getNextEntry ()
    {
        if (freeListCt != 0)
        {
            return freeList[--freeListCt];
        }
        int nextEntry = entryPtr++;
        keys = keyFactory.ensureArrayCapacity (keys, nextEntry + 1,
                                               growthStrategy);
        return nextEntry;
    }

    protected void flip (int a, int b)
    {
        int entryA = tree[a];
        int entryB = tree[b];
        inverse[entryA] = b;
        inverse[entryB] = a;
        tree[a] = entryB;
        tree[b] = entryA;
    }

    protected int leftChild (int parent)
    {
        return parent * 2;
    }

    protected int rightChild (int parent)
    {
        return (parent * 2) + 1;
    }

    protected int parent (int child)
    {
        return (child / 2);
    }
}
