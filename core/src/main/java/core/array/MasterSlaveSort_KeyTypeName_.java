package core.array;

import core.stub.*;
import core.util.comparator.Comparator_KeyTypeName_;

/**
 * Copyright 5/19/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/19/13
 */
public class MasterSlaveSort_KeyTypeName_
{

    /**
     * Sort <i>master</i> using the {@link Comparator_KeyTypeName_} passed. Keep the {@link Swappable}
     * Objects (usually arrays) associated with their master item. For example, assume the following master
     * and slave items (int master used for clarity) with a {@link core.util.comparator.Comparators.IntAsc}:
     * <p/>
     * master:
     * <pre>
     *          {   15,     4,      8,      1,      9}
     * </pre>
     * slaves:
     * <pre>
     *          {   'f',    'o',    'o',    'b',    'a' }
     *          {   "hi",   "be",   "of,    "i",    "see"}
     *          {    0,      1,      3,      2,      4}
     *          ...
     * </pre>
     * <p/>
     * Results in:
     * <p/>
     * master:
     * <pre>
     *          {    1,     4,      8,      9,      15}
     * </pre>
     * slaves:
     * <pre>
     *          {   'b',    'o',    'o',    'a',    'f' }
     *          {   "i",   "be",   "of,    "see",   "hi"}
     *          {    2,      1,      3,      4,      0}
     *          ...
     * </pre>
     *
     * @param master the master array to sort
     * @param cmp    the comparator used to sort the array
     * @param slaves the slave {@link Swappable} items to keep in parallel order
     */
    public static void sort( _key_[] master, Comparator_KeyTypeName_ cmp, Swappable... slaves )
    {
        int numSlaves = slaves.length;
        int masterLen = master.length;
        for( int i = 0; i < numSlaves; i++ )
        {
            if( slaves[ i ].getLength() != masterLen )
            {
                throw new IllegalStateException( "Master and Slave Arrays must have same length," +
                                                 "Master has length of [" + masterLen + "], while slave" +
                                                 "of index [" + i + "] has length of [" + slaves[ i ].getLength() + "]" );
            }
        }
        sort( master, 0, master.length, cmp, numSlaves, slaves );
    }

    /**
     * Stolen Sort from {@link java.util.Arrays#sort(int[])}.
     * <p/>
     * See above for usage, this will do the same thing up to a certain index in the master.
     *
     * @param master    the master array to sort
     * @param off       offset in master to start sorting
     * @param len       length from offset to sort
     * @param cmp       comparator used to sort
     * @param numSlaves number of slave elements (computed once for efficiency)
     * @param slaves    the slave {@link Swappable} items to keep in parallel order
     */
    public static void sort( _key_[] master, int off, int len, Comparator_KeyTypeName_ cmp, int numSlaves, Swappable... slaves )
    {

        // Insertion sort on smallest arrays
        if( len < 7 )
        {
            for( int i = off; i < len + off; i++ )
                for( int j = i; j > off && cmp.compare( master[ j - 1 ], master[ j ] ) > 0; j-- )
                    swap( master, j, j - 1, numSlaves, slaves );
            return;
        }

        // Choose a partition element, v
        int m = off + ( len >> 1 );       // Small arrays, middle element
        if( len > 7 )
        {
            int l = off;
            int n = off + len - 1;
            if( len > 40 )
            {        // Big arrays, pseudomedian of 9
                int s = len / 8;
                l = med3( master, l, l + s, l + 2 * s, cmp );
                m = med3( master, m - s, m, m + s, cmp );
                n = med3( master, n - 2 * s, n - s, n, cmp );
            }
            m = med3( master, l, m, n, cmp ); // Mid-size, med of 3
        }
        _key_ v = master[ m ];

        // Establish Invariant: v* (<v)* (>v)* v*
        int res;
        int a = off, b = a, c = off + len - 1, d = c;
        while( true )
        {
            while( b <= c && cmp.compare( master[ b ], v ) <= 0 )
            {
                if( master[ b ] == v )
                    swap( master, a++, b, numSlaves, slaves );
                b++;
            }
            while( c >= b && cmp.compare( master[ c ], v ) >= 0 )
            {
                if( master[ c ] == v )
                    swap( master, c, d--, numSlaves, slaves );
                c--;
            }
            if( b > c )
                break;
            swap( master, b++, c--, numSlaves, slaves );
        }

        // Swap partition elements back to middle
        int s, n = off + len;
        s = Math.min( a - off, b - a );
        vecswap( master, off, b - s, s, numSlaves, slaves );
        s = Math.min( d - c, n - d - 1 );
        vecswap( master, b, n - s, s, numSlaves, slaves );

        // Recursively sort non-partition-elements
        if( ( s = b - a ) > 1 )
            sort( master, off, s, cmp, 0, slaves );
        if( ( s = d - c ) > 1 )
            sort( master, n - s, s, cmp, 0, slaves );
    }

    private static int med3( _key_[] x, int a, int b, int c, Comparator_KeyTypeName_ cmp )
    {
        return ( cmp.compare( x[ a ], x[ b ] ) ) < 0 ?
               ( cmp.compare( x[ b ], x[ c ] ) < 0 ? b : cmp.compare( x[ a ], x[ c ] ) < 0 ? c : a ) :
               ( cmp.compare( x[ b ], x[ c ] ) > 0 ? b : cmp.compare( x[ a ], x[ c ] ) > 0 ? c : a );
    }

    private static void swap( _key_[] master, int a, int b, int numSlaves, Swappable... slave )
    {
        _key_ t = master[ a ];
        master[ a ] = master[ b ];
        master[ b ] = t;

        for( int i = 0; i < numSlaves; i++ )
        {
            slave[ i ].swap( a, b );
        }
    }

    private static void vecswap( _key_[] x, int a, int b, int n, int numSlaves, Swappable... slaves )
    {
        for( int i = 0; i < n; i++, a++, b++ )
            swap( x, a, b, numSlaves, slaves );
    }


}
