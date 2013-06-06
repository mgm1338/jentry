package store.col.storage.block;

import core.Types;
import core.annotations.UncheckedArray;
import core.array.ArrayGrowthException;
import core.array.GrowthStrategy;
import core.stub.*;
import store.col.storage.generic.ColStorage_KeyTypeName_;

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
 * the blocked storage's growth will become necessary (imagine doubling an array of 200 million records, it
 * is much better to grow it slowly in blocks). There are also significant gains due to caching, when
 * sequential rows are accessed (many times an entire block can be cached). The trade-off is access and
 * insertion does incur a slight extra cost.
 * </p>
 * <p/>
 * <p/>
 * Note: The Storage here has many dangerous methods that are unchecked for sake of efficiency. Wrapper
 * classes and extensions handle bounds checking and error conditions. Take care when using this class directly.</p>
 * <p/>
 * <p/>
 * <b>Structure</b>
 * <p>The blocked storage will use the least significant bits as the block index,
 * and the most significant bits to determine which block we are writing to.
 * </p>
 * <b>Example:</b>
 * <p>
 * Say we have a block size of 1024 (block sizes will always be a power of 2, or else we would be wasting bits),
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
public class ColStorageBlocked_KeyTypeName_ implements ColStorage_KeyTypeName_
{

    /** Growth strategy of the store */
    protected final GrowthStrategy growthStrategy;
    /** As shown above, this will be the (2^bitPerBlock-1), mask that when 'anded' gets index in the block */
    protected int bitsMask;
    /** Number of bits per block, the block size will be 2^(bitsPerBlock) */
    protected int bitsPerBlock;
    /** The block size */
    protected int blockSize;
    /** Array of format [block][index in block] that stores our data */
    protected _key_[][] data;
    /** Number of our active blocks */
    protected int numBlocks = 0;

    /**
     * Short Constructor. Uses double growth for all growth requests.
     *
     * @param blockSize size of a block (will automatically be converted to the next power of 2, if not one).
     * @param storeSize the size of that storage. If not a multiple of the blockSize, will be the next multiple after
     */
    public ColStorageBlocked_KeyTypeName_( int blockSize, int storeSize )
    {
        this( blockSize, storeSize, GrowthStrategy.doubleGrowth );
    }

    /**
     * Fully Qualified Constructor
     *
     * @param blockSize      size of a block (will automatically be converted to the next power of 2, if not one).
     * @param size           the size of that storage. If not a multiple of the blockSize, will be the next multiple after
     *                       the size passed.
     * @param growthStrategy the growth strategy of the store.
     */
    public ColStorageBlocked_KeyTypeName_( int blockSize, int size, GrowthStrategy growthStrategy )
    {
        this.growthStrategy = growthStrategy;
        if( ( blockSize & ( blockSize - 1 ) ) != 0 ) //if not a power of 2, little bit of bit trickery
        {
            blockSize = nextPowerOfTwo( blockSize );
        }
        this.blockSize = blockSize;
        this.bitsPerBlock = getBitsInBlock( this.blockSize );
        bitsMask = this.blockSize - 1;
        //if size is not multiple of blocks, we add one so we can accompany size
        int numBlocks = ( size % this.blockSize == 0 ) ? size / this.blockSize : ( size / this.blockSize ) + 1;
        //size will be a multiple of blockSize
        data = new _key_[ numBlocks ][];
        for( int i = 0; i < numBlocks; i++ )
        {
            data[ i ] = new _key_[ this.blockSize ];
        }
        this.numBlocks = numBlocks;
    }


    /**
     * Get the blockSize, will return the number of items one block may accommodate
     *
     * @return the block size
     */
    public int getBlockSize()
    {
        return blockSize;
    }

    /**
     * {@inheritDoc}
     * Sets the row to the computed block/index in the store. Unchecked method, store must be correct
     * capacity to hold hte value.
     *
     * @param value the value
     * @param idx   index into the store
     */
    @Override
    @UncheckedArray
    public void setValue( _key_ value, int idx )
    {
        data[ idx >> bitsPerBlock ][ idx & bitsMask ] = value;
    }

    /**
     * Get the total size of the store. This will always be a multiple of <i>blockSize</i>.
     *
     * @return the size of the store
     */
    @Override
    public int getCapacity()
    {
        return blockSize * numBlocks;
    }

    /**
     * Get the value at the index specified. Unchecked, ensure that is not outside of bounds
     * of the store.
     *
     * @param idx index
     * @return the value at the index
     */
    @Override
    @UncheckedArray
    public _key_ getValue( int idx )
    {
        return data[ idx >> bitsPerBlock ][ idx & bitsMask ];
    }

    /**
     * {@inheritDoc}
     *
     * @return the type
     */
    @Override
    public byte getType()
    {
        return Types._KeyTypeName_;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <p>This structure grows by blockSize, so as long as the {@link GrowthStrategy} allows
     * for growth, this will checkGrowth it to the new size to a multiple of blocksize.</p>
     *
     * @param minSize the minimum new size of the store
     */
    public void checkGrowth( int minSize )
    {
        int size = getCapacity();
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
            temp[ i ] = ( i < this.numBlocks ) ? data[ i ] : new _key_[ blockSize ]; //update or allocate new blocks
        }
        this.numBlocks = newNumBlocks;
        data = temp;
    }


    /**
     * Analogous to {@link java.util.Arrays#fill}. Will fill the store with the value <i>val</i>
     * specified.
     *
     * @param val the value to fill the entire store with
     */
    public void fill( _key_ val )
    {
        int len = data.length;
        for( int i = 0; i < len; i++ )
        {
            Arrays.fill( data[ i ], val );
        }
    }

    /**
     * Analogous to {@link Arrays#fill}, will fill every index from <i>fromIndex</i> (inclusive)
     * to <i>toIndex</i> (exclusive), with the value specified. Unchecked array bounds, as many
     * of the methods manipulating with the storage.
     *
     * @param val
     * @param fromIndex
     * @param toIndex
     */
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

    /**
     * {@inheritDoc}
     *
     * @return a copy of this store
     */
    @Override
    public ColStorageBlocked_KeyTypeName_ getCopy()
    {
        ColStorageBlocked_KeyTypeName_ copy = new ColStorageBlocked_KeyTypeName_( this.blockSize, this.getCapacity(),
                                                                                  this.growthStrategy );
        copy.copyFrom( this, 0, 0, this.getCapacity() );
        return copy;
    }

    /**
     * {@inheritDoc}.
     * <p>Note We do not check the bounds of the array for this method.</p>
     *
     * @param source  source of the data to copy from
     * @param srcPos  index in the source to start copying from
     * @param destPos index in <i>this</i> to start copying to
     * @param length  number of items to copy
     */
    @UncheckedArray
    @Override
    public void copyFrom( ColStorage_KeyTypeName_ source, int srcPos, int destPos, int length )
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

    protected int nextPowerOfTwo( int n )
    {
        n = n - 1;
        n = n | ( n >> 1 );
        n = n | ( n >> 2 );
        n = n | ( n >> 4 );
        n = n | ( n >> 8 );
        n = n | ( n >> 16 );
        return n + 1;
    }

    protected int getBitsInBlock( int blockSize )
    {
        int bits = 0;
        while( ( blockSize >> bits ) != 1 )
        {
            bits++;
        }
        return bits;

    }

}
