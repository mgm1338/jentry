package collections.heap;

import core.Const;
import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryInt;
import core.array.factory.ArrayFactory_KeyTypeName_;
import core.stub._key_;
import core.util.comparator.Comparator;
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
        inverse[entry] = treeSpot;
        shiftUp (key, treeSpot);
        size++;
        return entry;
    }

    public boolean remove (int entry)
    {
        int spot = inverse[entry];
        if (entry == Const.NO_ENTRY)
        {
            return false;
        }
        else
        {
            shiftDown (spot);
        }
        size--;
        return true;
    }

    protected void shiftDown (int curSpot)
    {
        int left = leftChild (curSpot);
        int right = rightChild (curSpot);
        while (left != Const.NO_ENTRY && right != Const.NO_ENTRY)
        {
            if (left == Const.NO_ENTRY)
            {
                flip (curSpot, left);
            }
            else if (right == Const.NO_ENTRY)
            {
                flip (curSpot, right);
            }
            else
            {
                int cmpResult = cmp.compare (keys[left], keys[right]);
                if (cmpResult <= 0)
                {
                    flip (curSpot, left);
                }
                else
                {
                    flip (curSpot, right);
                }
            }
            left = leftChild (curSpot);
            right = rightChild (curSpot);
        }
    }

    protected void shiftUp (_key_ key, int curSpot)
    {
        int parent = parent (curSpot);
        while (cmp.compare (key, keys[tree[parent]]) <= 0 && curSpot != 1)
        {
            flip (curSpot, parent);
            curSpot = parent;
            parent = parent (parent);
        }
    }

    protected int getNextFreeTreeSpot ()
    {
        int spot = 1;
        while ((spot * 2 < size))
        {
            spot *= 2;
        }
        //add another level
        tree = intFactory.ensureArrayCapacity
                (tree, spot * 2, Const.NO_ENTRY, growthStrategy);
        inverse = intFactory.ensureArrayCapacity (inverse, spot * 2, growthStrategy);
        while (tree[spot++] != Const.NO_ENTRY)
        {
        }
        return spot;
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
        int temp = tree[b];
        tree[b] = tree[a];
        tree[a] = temp;
        temp = inverse[b];
        inverse[a] = inverse[b];
        inverse[b] = temp;
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
