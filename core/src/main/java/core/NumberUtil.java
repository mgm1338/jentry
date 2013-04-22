package core;

/**
 * Copyright 1/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/27/13
 */
public class NumberUtil
{

    /**
     * Mask used for right, for when inserting need to cast as long. This will convert the right
     * to a long in twos complement. This viewed as hex is 0000 0000 FFFF FFFF.
     */
    public static final long mask = -1L >>> 32;

    /**
     * Load two ints into one long. Visually the long will be formatted as
     * LLLLRRRR, with each letter being a byte. Normally used in conjunction with
     * {@link #getLeft(long)}  and {@link #getRight(long)}
     *
     * @param left  the left int will be placed into the first 4 bytes
     * @param right the right int will be placed into the last 4 bytes
     * @return the long with the two ints loaded into it
     */
    public final static long packLong( int left, int right )
    {
        return ( ( long ) left << 32 ) | ( ( long ) right & mask );
    }

    /**
     * In a long, compose an int from the 4 left bytes. Normally used in conjunction with
     * {@link #packLong(int, int)}  and {@link #getRight(long)}. Visually return the 4 Ls
     * from LLLLRRRR where each letter is a byte.
     *
     * @param l the long
     * @return the int making up the first 4 bytes.
     */
    public final static int getLeft( long l )
    {
        return ( int ) ( l >> 32 );
    }

    /**
     * In a long, compose and return an int from the right 4 bytes. Normally used
     * in conjunction with {@link #packLong(int, int)} and {@link #getRight(long)} .Visually returns the
     * 4 R bytes from a long or LLLLRRRR where each letter represents a byte.
     *
     * @param l the long
     * @return an int composed of the last 4 bytes.
     */
    public final static int getRight( long l )
    {
        return ( int ) ( l & mask );
    }
}
