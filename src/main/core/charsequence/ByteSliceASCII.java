package core.charsequence;

/**
 * Copyright 3/10/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 3/10/13
 * <p/>
 * <p>
 * A Utility collection that acts as a facade over a byte[] to represent a sequence of
 * ASCII characters. Used in collections, ByteSlices are very efficient ways to represent
 * a collection of CharSequences (like {@link String}s), provided that they can be represented
 * by the standard ASCII codes that overlap with a Java byte (0-127). This means that the standard
 * English alphabet, numeric characters, and punctuation are included.
 * </p>
 * <p/>
 * <p>A ByteSlice does NOT OWN ITS UNDERLYING BYTE ARRAY. This is a very important, as modification
 * of this byte array from a ByteSlice may have unintended consequences to other items that are
 * referencing it. In this way, ByteSlices and Strings are somewhat similar in mutability. </p>
 * <p/>
 * <p>For the purpose of Jentry, ByteSlice collections will be commonly used when dealing with
 * CharSequence typed Collections. This limits the ability of Unicode, which may be extended
 * in the future, but will not be supported in early releases </p>
 */
public class ByteSliceASCII implements CharSequence
{
    /**
     * Reference to the array of bytes. The CharSequence only holds a reference to this array, and
     * does not own the array itself
     */
    byte[] bytes;
    /** Offset in the bytes array where this 'slice' starts. */
    int offSet;
    /** Length (in bytes), of this slice of text */
    int len;

    public ByteSliceASCII( byte[] bytes, int offSet, int len )
    {
        this.bytes = bytes;
        this.offSet = offSet;
        this.len = len;
    }

    /**
     * Returns the length of this character sequence in bytes (UNLIKE the actual CharSequence
     * which will return the number of 16-bit characters, this returns the number of bytes).
     *
     * @return the number of bytes in this sequence
     */
    @Override
    public int length()
    {
        return len;
    }

    /**
     * Returns the <code>char</code> value at the specified index.  An index ranges from zero
     * to <tt>length() - 1</tt>.  The first <code>char</code> value of the sequence is at
     * index zero, the next at index one, and so on, as for array indexing. This will only return ASCII characters,
     * so this method uses extra space by boxing the bytes as a character. See  {@link #byteAt(int)} for an
     * alternative that will return the bytes as a byte.
     * <p/>
     *
     * @param index the index of the <code>char</code> value to be returned
     * @return the specified <code>char</code> value
     */
    @Override
    public char charAt( int index )
    {
        return ( char ) bytes[ offSet + index ];
    }

    public byte byteAt( int index )
    {
        return bytes[ offSet + index ];
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
        return new ByteSliceASCII( this.bytes, offSet + start, end - start );
    }


    @Override
    public boolean equals( Object o )
    {
        if( o != null )
        {
            if( ( o instanceof CharSequence ) )
            {
                if( ( ( CharSequence ) o ).length() != this.len )
                {
                    int len = this.len;
                    for( int i = 0; i < len; i++ )
                    {
                        if( charAt( i ) != ( ( CharSequence ) o ).charAt( 0 ) )
                        {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
