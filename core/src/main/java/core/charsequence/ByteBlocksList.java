package core.charsequence;

import core.array.GrowthStrategy;
import core.array.factory.ArrayFactoryByte;
import core.array.factory.ArrayFactoryInt;
import core.array.util.MasterSlaveIntSort;
import core.util.comparator.Comparators;

/**
 * Copyright 5/16/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/16/13
 * <p/>
 * A collection of {@link ByteBlock} lists that will return a handle for every inserted {@link ByteBlock} upon
 * insertion.
 */
public class ByteBlocksList
{
    /** Default size for a block of bytes, does not restrict the size of the block, just allocates the byte array */
    public static final int DEFAULT_BLOCK_SIZE = 64;

    /** Factory for allocating offsets and lengths */
    protected final ArrayFactoryInt intFactory;
    /** Factory for allocating bytes that blocks making up the {@link ByteBlock}s */
    protected final ArrayFactoryByte byteFactory;
    /** Growth strategy of the bytes, and the number of blocks */
    protected final GrowthStrategy growthStrategy;

    /** The actual byte data of the {@link ByteBlock}s, this collection will own the byte array */
    protected byte[] data;
    /** Parallel arrays that will describe the location and length of {@link ByteBlock}s in list */
    protected int[] offsets;
    protected int[] lengths;
    protected int[] freeList;


    /** Current number of blocks */
    protected int nextBlock = 0;
    /** Number of items in the free list */
    protected int freeListPtr = 0;
    /** After a compact, we can use the free entries, this will denote how many we have left */
    protected int freeListLockPtr = -1;
    /** Now when we are using free list entries, we will use up to <i>freeListLockPtr</i> */
    protected int freeListUsePtr = 0;


    /** Pointer in the bytes array */
    protected int dataPtr = 0;

    /** Scratch array that will grow to necessary size as temporary block holder */
    private byte[] scratch = new byte[ DEFAULT_BLOCK_SIZE ];
    protected int[] offsetScratch = new int[ 8 ];
    protected int[] lenScratch = new int[ 8 ];


    public ByteBlocksList( int numBlocks )
    {
        this( numBlocks, DEFAULT_BLOCK_SIZE );
    }

    public ByteBlocksList( int numBlocks, int blockSize )
    {
        this( numBlocks, blockSize, ArrayFactoryInt.defaultIntProvider, ArrayFactoryByte.defaultByteProvider,
              GrowthStrategy.doubleGrowth );
    }

    public ByteBlocksList( int numBlocks, int blockSize, ArrayFactoryInt intFactory,
                           ArrayFactoryByte byteFactory,
                           GrowthStrategy growthStrategy )
    {
        offsets = intFactory.alloc( numBlocks );
        lengths = intFactory.alloc( numBlocks );
        data = byteFactory.alloc( blockSize * numBlocks );
        this.intFactory = intFactory;
        this.byteFactory = byteFactory;
        this.growthStrategy = growthStrategy;
    }

    public int insert( CharSequence block )
    {
        int len = block.length();
        if( scratch.length < len ) scratch = new byte[ len ]; //dont have to save any data with this
        for( int i = 0; i < len; i++ )
        {
            scratch[ i ] = ( byte ) block.charAt( i );
        }
        return insert( scratch, 0, len );
    }

    public int insert( byte[] block, int offset, int length )
    {
        int offsetsLen = offsets.length;
        int dataLen = data.length;
        //check growth conditions, if we need to grow we do a compact
        if( freeListPtr + nextBlock == offsetsLen )
        {
            compact();
            growOffsetsLengths( offsetsLen );
        }
        if( dataPtr + length > dataLen )
        {
            compact();
            growData( dataLen );
        }
        //entry can either be next block, or if we have compacted we will use freelist entries that were freed
        int entry;
        if( freeListUsePtr < freeListLockPtr )
        {
            entry = freeList[ freeListUsePtr++ ];
        }
        else
        {
            entry = nextBlock++;
        }
        offsets[ entry ] = dataPtr;
        lengths[ entry ] = length;
        System.arraycopy( block, offset, data, dataPtr, length );
        dataPtr += length;
        return entry;
    }

    private void growData( int dataLen )
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void growOffsetsLengths( int offsetsLen )
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void remove( int blockIdx )
    {
        lengths[ blockIdx ] = 0;
        freeListPtr++;
        freeList = intFactory.ensureArrayCapacity( freeList, freeListPtr + 1, growthStrategy );
        freeList[ freeListPtr ] = blockIdx;
    }


    public void compact()
    {
        if( freeListPtr == 0 ) return; //nothing to compact

        if( freeListUsePtr > 0 )    //shifts all un-used free list items to zero
        {
            int numFreeListLeft = freeListPtr - freeListUsePtr;
            System.arraycopy( freeList, freeListUsePtr, freeList, 0, numFreeListLeft );
            freeListPtr = numFreeListLeft;
        }
        //process from 0-freeListPtr, squishing their holes, and decrement dataPtr
        int newDataPtr;
        if (freeListPtr>offsetScratch.length)
        {
            offsetScratch = new int[freeListPtr];
            lenScratch = new int[freeListPtr];
        }
        for( int i = 0; i < freeListPtr; i++ )
        {
            MasterSlaveIntSort.sort( offsets, lengths, new Comparators.IntAsc() );
        }

        freeListLockPtr = freeListPtr; //now we can use up to this pointer
        return;

    }

}
