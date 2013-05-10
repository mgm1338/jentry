package store.col.storage;

import store.col.storage.block.ColStoreBlockedInt;
import store.col.storage.generic.ColStoreInt;
import store.col.storage.simple.ColStoreArrayInt;

/**
 * Copyright 5/9/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 5/9/13
 */
public class StorageComparisonTest
{

    protected static ColStoreArrayInt simpleStore;
    protected static ColStoreBlockedInt blockedStore;

    public static void main( String[] args )
    {
        //simple array for small values is much quicker when growing not involved
        simpleStore = new ColStoreArrayInt( 1048576 );
        blockedStore = new ColStoreBlockedInt( 12, 1048576 );
        long simple = iterativeFillNoGrow( simpleStore, 1048576 );
        long blocked = iterativeFillNoGrow( blockedStore, 1048576 );
        System.out.print( "Simple Insertion, no Growth\n" );
        System.out.println( "Simple = " + simple / 100000 );
        System.out.println( "Blocked = " + blocked / 100000 );

        //With growth, simple array starts to become close at around 1 million rows (when blockSize is optimal)
        simpleStore = new ColStoreArrayInt( 1024 );
        blockedStore = new ColStoreBlockedInt( 20, 1024 );
        simple = iterativeWithGrowth( simpleStore, 1048576, 1024 );
        blocked = iterativeWithGrowth( blockedStore, 1048576, 1024 );
        System.out.print( "1 million insertion, Double Growth\n" );
        System.out.println( "Simple = " + simple / 100000 );
        System.out.println( "Blocked = " + blocked / 100000 );

        //With WRONG bits per block, can totally mess it up for large data sets
        simpleStore = new ColStoreArrayInt( 1024 );
        blockedStore = new ColStoreBlockedInt( 12, 1024 );
        simple = iterativeWithGrowth( simpleStore, 8388608, 1024 );
        blocked = iterativeWithGrowth( blockedStore, 8388608, 1024 );
        System.out.print( "8 million insertion, Double Growth, Bad BlockSize\n" );
        System.out.println( "Simple = " + simple / 100000 );
        System.out.println( "Blocked = " + blocked / 100000 );

        //Now correct bits per block, start to see that blocked wins
        simpleStore = new ColStoreArrayInt( 1024 );
        blockedStore = new ColStoreBlockedInt( 24, 1024 );
        simple = iterativeWithGrowth( simpleStore, 8388608, 1024 );
        blocked = iterativeWithGrowth( blockedStore, 8388608, 1024 );
        System.out.print( "8 million insertion, Double Growth, Good BlockSize\n" );
        System.out.println( "Simple = " + simple / 100000 );
        System.out.println( "Blocked = " + blocked / 100000 );

        //multi-million index stores, blocked storage wins handily
        simpleStore = new ColStoreArrayInt( 1024 );
        blockedStore = new ColStoreBlockedInt( 26, 1024 );
        simple = iterativeWithGrowth( simpleStore, 33554432, 1024 );
        blocked = iterativeWithGrowth( blockedStore, 33554432, 1024 );
        System.out.print( "33 million insertion, Double Growth, Good BlockSize\n" );
        System.out.println( "Simple = " + simple / 100000 );
        System.out.println( "Blocked = " + blocked / 100000 );


    }


    protected static long iterativeFillNoGrow( ColStoreInt target, int numFill )
    {
        long start = System.nanoTime();
        for( int i = 0; i < numFill; i++ )
        {
            target.setValue( i, i );
        }
        long end = System.nanoTime();
        return end - start;
    }

    protected static long iterativeWithGrowth( ColStoreInt target, int numFill, int curSize )
    {
        long start = System.nanoTime();
        for( int i = 0; i < numFill; i++ )
        {
            if( i == curSize )
            {
                target.grow( i +1);
                curSize *=2;
            }
            target.setValue( i, i );
        }
        long end = System.nanoTime();
        return end - start;
    }



}
