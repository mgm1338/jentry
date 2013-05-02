package store.col.storage.block;

import core.stub._key_;

/**
 * Copyright 5/1/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/1/13
 *
 * <p>
 * A type of storage that is set up as an array of arrays (blocks). Instead of one long continuous array,
 * the Blocked Storage can allocate with much more control, and tends to perform best in most Jentry tables.
 * Recommended for all but the smallest tables. Compared to the simple array storage, the blocked storage
 * <ul>
 *     <li>Disadvantages: Will take slightly longer to write and read in smaller stores. </li>
 *     <li>Advantages: Can grow with more control, with less copying, which is much quicker.
 *     As columns become larger, the slight cost for read/write startsto outperform simple
 *     smaller collections, from past performance, this happens around ~750k rows (TODO:perf)
 *     </li>
 * </ul>
 * </p>
 *
 *    <b>Structure</b>
 * <p>The blocked storage will take part of the index as the 'block' we are writing into, and the latter
 * half as the index into the block. Say we have a block size of 1024 (block sizes usually perform best with
 * powers of 2), it will take 10 bits to represent the 0-1023 indeces in the block. The other 22 bits in
 * will be used to map to the correct block. If you are inserting into index 504,634, (111101100||1100111010),
 * we are inserting into index 1100111010 (826) of block 111101100 (492), as shown by the split in the first
 * binary representation.</p>
 *
 * <p>From the structure, the following equations can be used to determine the block/index in the block:
 * <br>
 *     Retrieval
 *     <pre>
 *         public _key_ get(index)
 *         {
 *             return data[index >> (number of bits that represent the block)]
 *             [index & a full mask of the number of bits that represent the block]
 *         }
 *     </pre>
 *     In our example the number of bits to represent the block is 10 <i>bitsPerBlock</i>;
 *     <pre>
 *         public _key_ get(504634)
 *         {
 *             return data[504634>>10][1023 & 504634];
 *         }
 *     </pre>
 * </br>
 *
 * </p>
 */
public class ColStoreBlocked_KeyTypeName_
{
    /** As shown above, this will be the (2^bitPerBlock-1), mask that when 'anded' gets index in the block*/
    int bitsMask;
    /** Number of bits per block, the block size will be 2^(bitsPerBlock)*/
    int bitsPerBlock;
    /** The block size */
    int blockSize;
    /** size of store (analagous to normal array.length)*/
    int size;

    _key_[][] data;

    public ColStoreBlocked_KeyTypeName_( int bitsPerBlock, int size )
    {
        this.bitsPerBlock = bitsPerBlock;
        this.size = size;
        blockSize = 1<<bitsPerBlock;
        bitsMask = blockSize-1;
    }
}
