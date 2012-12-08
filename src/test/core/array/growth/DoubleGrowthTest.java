package core.array.growth;

import core.array.GrowthStrategy;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */

public class DoubleGrowthTest
{
    GrowthStrategy dG = GrowthStrategy.doubleGrowth;

    @Before
    public void setup()
    {
        TestCase.assertNotNull( dG );
    }

    //tests setup and tricks funny build tools that require 
    //at least one test
    @Test
    public void initTest()
    {
        TestCase.assertEquals( 0, dG.growthRequest( 0, 0 ) );
    }

    @Test
    public void testNegativeGrowth()
    {
        TestCase.assertEquals( 0, dG.growthRequest( 0, -1 ) );
    }

    @Test
    public void testGrowthFromZero()
    {
        TestCase.assertEquals( 2, dG.growthRequest( 0, 2 ) );
        TestCase.assertEquals( 8, dG.growthRequest( 0, 6 ) );
        TestCase.assertEquals( 8, dG.growthRequest( 0, 5 ) );
        TestCase.assertEquals( 16, dG.growthRequest( 0, 9 ) );
    }

    @Test
    public void testGrowthFromNonZero()
    {
        TestCase.assertEquals( 6, dG.growthRequest( 3, 5 ) );
        TestCase.assertEquals( 6, dG.growthRequest( 3, 4 ) );
        TestCase.assertEquals( 16, dG.growthRequest( 8, 9 ) );
    }

    @Test
    public void testNoGrowthCorrect()
    {
        TestCase.assertEquals( 1, dG.growthRequest( 1, 1 ) );
        TestCase.assertEquals( 0, dG.growthRequest( 0, 0 ) );
        TestCase.assertEquals( 50, dG.growthRequest( 50, 49 ) );
        TestCase.assertEquals( 50, dG.growthRequest( 50, 2) );




    }



}
