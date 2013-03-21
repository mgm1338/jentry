package collections.hash.set;

import java.util.Random;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class HashSetInsertTest
{
    public static final int ONE_MILLION = 1000000;
    protected static Random random = new Random (42);
    protected static int[] randomLoads;

    /**
     * TEST BASELINE ~ 2610-2630 milliseconds.
     *
     */
    public static void main (String[] args)
    {
        //the cost of random is significant in comparison, determine inserts before timing
        randomLoads = new int[ ONE_MILLION ];
        for (int i = 0; i < ONE_MILLION; i++)
        {
            randomLoads[i] = random.nextInt (10000);
        }

        //insert as quickly as possible into small HashSet
        HashSetInt set = new HashSetInt (8);
        long startTime = System.nanoTime ();
        for (int i = 0; i < ONE_MILLION; i++)
        {
            set.insert (randomLoads[i]);
        }
        long endTime = System.nanoTime ();
        System.out.println("Insertion with growth took ["+(endTime-startTime)/1000000+"] millis");
    }
}
