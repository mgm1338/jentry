package collections.hash.set;

import java.util.Random;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class HashSetInsertTest
{
    public static final int INSERTION_SIZE = 1000000;
    protected static Random random = new Random (42);
    protected static int[] randomLoads;

    /**
     * [2616-2630]
     *
     * @param args
     */
    public static void main (String[] args)
    {
        HashSetInt set = new HashSetInt (8);
        randomLoads = new int[INSERTION_SIZE];
        for (int i = 0; i < INSERTION_SIZE; i++)
        {
            randomLoads[i] = random.nextInt (10000);
        }

        long startTime = System.nanoTime ();
        for (int i = 0; i < INSERTION_SIZE; i++)
        {
            set.insert (randomLoads[i]);
        }
        long endTime = System.nanoTime ();
        System.out.print ("["+(endTime-startTime)/1000000+"] millis");

    }
}
