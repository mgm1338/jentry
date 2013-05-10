package store.col.storage.block;

import core.Types;
import core.annotations.UncheckedArray;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.*;
import store.col.storage.generic.ColStore_KeyTypeName_;

import java.util.Arrays;

/**
 * Copyright 5/1/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/1/13
 * <p/>
 * <p>
 * A type of storage that is set up as an array of arrays (blocks). Compared to one long continuous array, this
 * structure will not require as much copying when growing. For smaller (under a million rows),
 * the a simple array structure is usually more efficient, however more multi-million record columns,
 * the blocked storage starts to way outperform simple arrays. Insertion and retrieval are more expensive, so
 * these structures are intended to store very large data sets. The control of growth also allows users to grow
 * much closer to their heap sizes without blowing up.
 * </p>
 * <p/>
 * <b>Structure</b>
 * <p>The blocked storage will use the least significant bits as the block index,
 * and the most significant bits to determine which block we are writing to.
 * </p>
 * <b>Example:</b>
 * <p>
 * Say we have a block size of 1024 (block sizes usually perform best with powers of 2),
 * it will take 10 bits to represent the 0-1023 indices in the block. Assume we are using ints, the other 22
 * bits will be be used to map to the correct block. Suppose  inserting into index
 * 504,634, or 1111011001100111010, we are inserting into index 1100111010 (826) of block 111101100 (492).
 * With a simple computation, we arrive at 504,634 = (492*1024) + 826. More formally, the basic formulas:</p>
 * <br>
 * Retrieval:
 * <pre>
 *         public _key_ get(index)
 *         {
 *             return data[index >> (number of bits that represent the block)]
 *             [index & a full mask of the number of bits that represent the block]
 *         }
 *     </pre>
 * In our example the number of bits to represent the block is 10 <i>bitsPerBlock</i>;
 * <pre>
 *         public _key_ get(504634)
 *         {
 *             return data[504634>>10][1023 & 504634];
 *         }
 *     </pre>
 * </br>
 *
 * </p>
 */
public class ColStoreBlocked_KeyTypeName_ implements ColStore_KeyTypeName_
{

    protected final GrowthStrategy growthStrategy;

    /** As shown above, this will be the (2^bitPerBlock-1), mask that when 'anded' gets index in the block */
    protected int bitsMask;
    /** Number of bits per block, the block size will be 2^(bitsPerBlock) */
    protected int bitsPerBlock;
    /** The block size */
    protected int blockSize;

    _key_[][] data;

    int numBlocks = 0;

    /**
     * Short Constructor. Uses double growth for all growth requests.
     *
     * @param bitsPerBlock the number of bits that represent indices in the block
     * @param size         the size of that storage. If not a multiple of the blockSize, will be the next multiple after
     *                     the size passed.
     */
    public ColStoreBlocked_KeyTypeName_( int bitsPerBlock, int size )
    {
        this( bitsPerBlock, size, GrowthStrategy.doubleGrowth );
    }

    /**
     * Fully Qualified Constructor
     *
     * @param bitsPerBlock   the number of bits that represent indices in the block
     * @param size           the size of that storage. If not a multiple of the blockSize, will be the next multiple after
     *                       the size passed.
     * @param growthStrategy the growth strategy of the store.
     */
    public ColStoreBlocked_KeyTypeName_( int bitsPerBlock, int size, GrowthStrategy growthStrategy )
    {
        this.growthStrategy = growthStrategy;
        this.bitsPerBlock = bitsPerBlock;
        blockSize = 1 << bitsPerBlock;
        bitsMask = blockSize - 1;
        //if size is not multiple of blocks, we add one so we can accompany size
        int numBlocks = ( size % blockSize == 0 ) ? size / blockSize : ( size / blockSize ) + 1;
        //size will be a multiple of blockSize
        data = new _key_[ numBlocks ][];
        for( int i = 0; i < numBlocks; i++ )
        {
            data[ i ] = new _key_[ blockSize ];
        }
        this.numBlocks = numBlocks;
    }


    public int getBlockSize()
    {
        return blockSize;
    }

    @Override
    @UncheckedArray
    public void setValue( _key_ value, int idx )
    {
        data[ idx >> bitsPerBlock ][ idx & bitsMask ] = value;
    }

    public int getSize()
    {
        return blockSize * numBlocks;
    }

    @Override
    @UncheckedArray
    public _key_ getValue( int idx )
    {
        return data[ idx >> bitsPerBlock ][ idx & bitsMask ];
    }

    @Override
    public byte getType()
    {
        return Types._KeyTypeName_;
    }

    /**
     * <p>
     * Grow the store to be able to store <i>minSize</i>.
     * </p>
     * <p>This structure grows by blockSize, so as long as the {@link GrowthStrategy} allows
     * for growth, this will grow it to the new size to a multiple of blocksize.</p>
     *
     * @param minSize the minimum new size of the store
     */
    public void grow( int minSize )
    {
        int size = getSize();
        if( size >= minSize )
        {
            return;
        }
        int newSize = growthStrategy.growthRequest( size, minSize );
        if( newSize == size ) throw new ArrayGrowthException( this.getClass(), size, minSize, Types._KeyTypeName_ );
        int newNumBlocks = newSize / blockSize;
        if( newSize % blockSize != 0 ) newNumBlocks++;

        _key_[][] temp = new _key_[ newNumBlocks ][ blockSize ];
        for( int i = 0; i < newNumBlocks; i++ )
        {
            temp[ i ] = ( i <= this.numBlocks ) ? data[ i ] : new _key_[ blockSize ];
        }
        this.numBlocks = newNumBlocks;
        data = temp;
    }


    public void fill( _key_ val )
    {
        int len = data.length;
        for( int i = 0; i < len; i++ )
        {
            Arrays.fill( data[ i ], val );
        }
    }

    @UncheckedArray
    public void fill( _key_ val, int fromIndex, int toIndex )
    {
        int startBlock = getBlock( fromIndex );
        int startIdx = getBlockIdx( fromIndex );
        int endBlock = getBlock( toIndex );
        int endIdx = getBlockIdx( toIndex );
        if( endBlock == startBlock ) //same block, from start to end
        {
            Arrays.fill( data[ startBlock ], startIdx, endIdx, val );
            return;
        }
        //multiple blocks
        Arrays.fill( data[ startBlock ], startIdx, blockSize, val );
        while( ++startBlock < endBlock )
        {
            Arrays.fill( data[ startBlock ], val );
        }
        if( endIdx != 0 ) //if zero, we are done
            Arrays.fill( data[ endBlock ], 0, endIdx, val );
    }

    public ColStoreBlocked_KeyTypeName_ getCopy()
    {
        ColStoreBlocked_KeyTypeName_ copy = new ColStoreBlocked_KeyTypeName_( this.bitsPerBlock, this.getSize(),
                                                                              this.growthStrategy );
        copy.copyFrom( this, 0, 0, this.getSize() );
        return copy;
    }

    public void copyFrom( ColStoreBlocked_KeyTypeName_ source, int srcPos, int destPos, int length )
    {
        for( int i = 0; i < length; i++ )
        {
            this.setValue( source.getValue( srcPos++ ), destPos++ );
        }
    }

    protected int getBlock( int idx )
    {
        return idx >> bitsPerBlock;
    }

    protected int getBlockIdx( int idx )
    {
        return idx & bitsMask;
    }
}
