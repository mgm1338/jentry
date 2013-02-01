package core.bytes;

import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryByte;
import core.array.factory.ArrayFactoryInt;

/**
 * Copyright 1/31/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/31/13
 */
public class ByteSlice
{

    protected static final int DEFAULT_FREE_LIST_SIZE = 4;

    protected final ArrayFactoryByte arrayFactoryByte;
    protected final ArrayFactoryInt arrayFactoryInt;
    protected final GrowthStrategy growthStrategy;

    protected byte[] array;
    protected int[] startIdx;
    protected int[] lens;

    protected int curPtr = 0;
    protected int size;
    protected int[] freeList;
    protected int freeListPtr = 0;


    public ByteSlice( int numSlices, int bytesLength )
    {
        this( numSlices, bytesLength, GrowthStrategy.doubleGrowth, ArrayFactoryByte.
                defaultByteProvider, ArrayFactoryInt.defaultIntProvider );
    }


    public ByteSlice( int numSlices, int bytesLength, GrowthStrategy growthStrategy, ArrayFactoryByte arrayFactoryByte,
                      ArrayFactoryInt arrayFactoryInt )
    {
        this.arrayFactoryByte = arrayFactoryByte;
        this.arrayFactoryInt = arrayFactoryInt;
        this.growthStrategy = growthStrategy;
        array = arrayFactoryByte.alloc( bytesLength );
        startIdx = arrayFactoryInt.alloc( numSlices );
        lens = arrayFactoryInt.alloc( numSlices );
        freeList = arrayFactoryInt.alloc( DEFAULT_FREE_LIST_SIZE );
    }

    protected int preInsert()
    {
        int entry = getNextEntry();
        int minSize = entry + 1;
        startIdx = arrayFactoryInt.ensureArrayCapacity( startIdx, minSize, growthStrategy );
        lens = arrayFactoryInt.ensureArrayCapacity( lens, minSize, growthStrategy );
        return entry;
    }

    public int insert( CharSequence s )
    {
        int entry = preInsert();
        startIdx[ entry ] = curPtr;
        int len = s.length();
        array = arrayFactoryByte.ensureArrayCapacity( array, curPtr + len, growthStrategy );
        for( int i = 0; i < len; i++ )
        {
            array[ curPtr++ ] = ( byte ) s.charAt( i );
        }
        lens[ entry ] = len;
        return entry;
    }

    private int getNextEntry()
    {
        if( freeListPtr == 0 )
        {
            return size;
        }
        else
        {
            return freeList[ freeListPtr-- ];
        }
    }

    public int insert( byte[] bytes )
    {
        int entry = preInsert();
        startIdx[ entry ] = curPtr;
        int len = bytes.length;
        array = arrayFactoryByte.ensureArrayCapacity( array, curPtr + len, growthStrategy );
        System.arraycopy( bytes, 0, array, curPtr, len );
        curPtr += len;
        lens[ entry ] = len;
        return entry;
    }

    public int insert( byte[] bytes, int offset, int len )
    {
        int entry = preInsert();
        startIdx[ entry ] = curPtr;
        array = arrayFactoryByte.ensureArrayCapacity( array, curPtr + len, growthStrategy );
        System.arraycopy( bytes, offset, array, curPtr, len );
        curPtr += len;
        lens[ entry ] = len;
        return entry;
    }

    public void compact()
    {

    }

    public void getEntry( int i, byte[] target )
    {
    }

    public CharSequence getEntry( int i )
    {
        return null;
    }

    public int getSize()
    {
        return size;
    }

    public int getSizeOfEntry( int entry )
    {
        return 0;
    }

    public boolean remove( int entry )
    {
        return false;
    }
}
