package core.array.util;

import core.util.comparator.ComparatorInt;

/**
 * Copyright 5/19/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/19/13
 */
public class MasterSlaveIntSort
{


    public static void sort( int[] master, int[] slave, ComparatorInt cmp )
    {
        if( master.length != slave.length )
            throw new IllegalStateException( "Master and Slave Arrays must have same length" );
        sort( master, 0, master.length, slave, cmp );

    }

    /**
     * Stolen Sort from {@link java.util.Arrays#sort(int[])}.
     *
     * @param master
     * @param off
     * @param len
     * @param slave
     * @param cmp
     */
    private static void sort( int[] master, int off, int len, int[] slave, ComparatorInt cmp )
    {
        // Insertion sort on smallest arrays
        if( len < 7 )
        {
            for( int i = off; i < len + off; i++ )
                for( int j = i; j > off && cmp.compare( master[ j - 1 ], master[ j ] ) > 0; j-- )
                    swap( master, j, j - 1, slave );
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
        int v = master[ m ];

        // Establish Invariant: v* (<v)* (>v)* v*
        int a = off, b = a, c = off + len - 1, d = c;
        while( true )
        {
            while( b <= c && cmp.compare( master[ b ], v ) <= 0 )
            {
                if( master[ b ] == v )
                    swap( master, a++, b, slave );
                b++;
            }
            while( c >= b && cmp.compare( master[ c ], v ) >= 0 )
            {
                if( master[ c ] == v )
                    swap( master, c, d--, slave );
                c--;
            }
            if( b > c )
                break;
            swap( master, b++, c--, slave );
        }

        // Swap partition elements back to middle
        int s, n = off + len;
        s = Math.min( a - off, b - a );
        vecswap( master, off, b - s, s, slave );
        s = Math.min( d - c, n - d - 1 );
        vecswap( master, b, n - s, s, slave );

        // Recursively sort non-partition-elements
        if( ( s = b - a ) > 1 )
            sort( master, off, s, slave, cmp );
        if( ( s = d - c ) > 1 )
            sort( master, n - s, s, slave, cmp );
    }

    private static int med3( int[] x, int a, int b, int c, ComparatorInt cmp )
    {
        return ( cmp.compare( x[ a ], x[ b ] ) ) < 0 ?
               ( cmp.compare( x[ b ], x[ c ] ) < 0 ? b : cmp.compare( x[ a ], x[ c ] ) < 0 ? c : a ) :
               ( cmp.compare( x[ b ], x[ c ] ) > 0 ? b : cmp.compare( x[ a ], x[ c ] ) > 0 ? c : a );
    }

    private static void swap( int[] master, int a, int b, int[] slave )
    {
        int t = master[ a ];
        master[ a ] = master[ b ];
        master[ b ] = t;

        int t2 = slave[ a ];
        slave[ a ] = slave[ b ];
        slave[ b ] = t2;
    }

    private static void vecswap( int[] x, int a, int b, int n, int slave[] )
    {
        for( int i = 0; i < n; i++, a++, b++ )
            swap( x, a, b, slave );
    }


}
