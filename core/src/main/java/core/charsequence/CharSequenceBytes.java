package core.charsequence;

/**
 * Copyright 6/15/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/15/13
 */
public class CharSequenceBytes implements CharSequence
{
    byte[] data;
    int len;


    public CharSequenceBytes( byte[] data )
    {
        this.data = data;
        this.len = data.length;
    }


    /**
     * Returns the length of this character sequence.  The length is the number
     * of 8-bit <code>char</code>s in the sequence.</p>
     *
     * @return the number of <code>char</code>s in this sequence
     */
    @Override
    public int length()
    {
        return len;
    }

    /**
     * Returns the <code>char</code> value at the specified index.  An index ranges from zero
     * to <tt>length() - 1</tt>.  The first <code>char</code> value of the sequence is at
     * index zero, the next at index one, and so on, as for array
     * indexing. </p>
     * <p/>
     * <p>If the <code>char</code> value specified by the index is a
     * <a href="Character.html#unicode">surrogate</a>, the surrogate
     * value is returned.
     *
     * @param index the index of the <code>char</code> value to be returned
     * @return the specified <code>char</code> value
     * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative or not less than
     *                                   <tt>length()</tt>
     */
    @Override
    public char charAt( int index )
    {
        return ( char ) data[ index ];
    }

    /**
     * Returns a new <code>CharSequence</code> that is a subsequence of this sequence.
     * The subsequence starts with the <code>char</code> value at the specified index and
     * ends with the <code>char</code> value at index <tt>end - 1</tt>.  The length
     * (in <code>char</code>s) of the
     * returned sequence is <tt>end - start</tt>, so if <tt>start == end</tt>
     * then an empty sequence is returned. </p>
     *
     * @param start the start index, inclusive
     * @param end   the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if <tt>start</tt> or <tt>end</tt> are negative,
     *                                   if <tt>end</tt> is greater than <tt>length()</tt>,
     *                                   or if <tt>start</tt> is greater than <tt>end</tt>
     */
    @Override
    public CharSequence subSequence( int start, int end )
    {
        int len = end - start;
        byte[] sub = new byte[ len ];
        System.arraycopy( data, start, sub, 0, len );
        return new CharSequenceBytes( sub );
    }

    @Override
    public boolean equals( Object o )
    {
        if( o != null || ( !( o instanceof CharSequence ) ) )
        {
            if( ( ( CharSequence ) o ).length() == this.len )
            {
                int len = this.len;
                for( int i = 0; i < len; i++ )
                {
                    if( charAt( i ) != ( ( CharSequence ) o ).charAt( i ) )
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
