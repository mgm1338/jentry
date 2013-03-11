package collections.relationship;

import core.Const;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.TestUtilsInt;

/**
 * Copyright 2/9/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/9/13
 */
public class TestManyToManyInt 
{
    ManyToManyInt manyToManyNoCounts;
    ManyToManyInt manyToManyCounts;


    @Before
    public void setup()
    {
        manyToManyNoCounts = new ManyToManyInt( 8, 8, 8, false, false );
        manyToManyCounts = new ManyToManyInt( 8, 8, 8, true, true );

        TestCase.assertEquals( 0, manyToManyCounts.getSize() );
        TestCase.assertEquals( 0, manyToManyNoCounts.getSize() );
    }


    /** Do some simple associations with both ManyToManyInts, shown below
     *
     *  L                      R
     *  0   ->1                1   ->0,2,3,4
     *  2   ->1                6   ->25,26,27
     *  3   ->1,19,20,21       19  ->3
     *  4   ->1                20  ->3
     *  25  ->6                21  ->3
     *  26  ->6
     *  27  ->6
     * */
    
    public void simpleAssociate()
    {
        manyToManyCounts.associate( 0, 1 );
        manyToManyCounts.associate( 2, 1 );
        manyToManyCounts.associate( 3, 1 );
        manyToManyCounts.associate( 4, 1 );
        TestCase.assertEquals( 4, manyToManyCounts.getCountForRight( 1 ) );
        manyToManyCounts.associate( 3, 19 );
        manyToManyCounts.associate( 3, 20 );
        TestCase.assertEquals( 1, manyToManyCounts.getCountForRight( 20 ) );
        manyToManyCounts.associate( 3, 21 );
        manyToManyCounts.associate( 25, 6 );
        manyToManyCounts.associate( 26, 6 );
        manyToManyCounts.associate( 27, 6 );
        TestCase.assertEquals( 3, manyToManyCounts.getCountForRight( 3 ) );



        manyToManyNoCounts.associate( 0, 1 );
        manyToManyNoCounts.associate( 2, 1 );
        manyToManyNoCounts.associate( 3, 1 );
        manyToManyNoCounts.associate( 4, 1 );
        TestCase.assertEquals( 4, manyToManyNoCounts.getCountForRight( 1 ) );
        manyToManyNoCounts.associate( 3, 19 );
        manyToManyNoCounts.associate( 3, 20 );
        TestCase.assertEquals( 1, manyToManyNoCounts.getCountForRight( 20 ) );
        manyToManyNoCounts.associate( 3, 21 );
        manyToManyNoCounts.associate( 25, 6 );
        manyToManyNoCounts.associate( 26, 6 );
        manyToManyNoCounts.associate( 27, 6 );
        TestCase.assertEquals( 3, manyToManyNoCounts.getCountForRight( 3 ) );


        TestCase.assertEquals( 10, manyToManyCounts.getSize() );
        TestCase.assertEquals( 10, manyToManyNoCounts.getSize() );
    }



    /**
     * Do a manual iteration, and make sure it is insert order and assert size
     * Compare with getting all rights with a null target (should be exact size and same contents)
     */
    
    public void iterateRightsForLeft()
    {

    }

    /** Assert that we will grow an array that is one off holding the set of associations */
    
    public void getIterationsWithDifferentTargets()
    {

    }

    
    public void disassociateTest()
    {

    }

    /** Assert clear will result in a empty structure */
    
    public void clearTest()
    {

    }

    /** Grow Lefts and Associations Test */
    
    public void growthTest()
    {

    }

    /** Assert Compact Associations that are immediatelyRe-used */
    
    public void testCompactness()
    {

    }

    /** Copy a loaded one to many from a null target */
    
    public void copyFromNull()
    {

    }

    /** Copy a loaded one to many from a larger one */
    
    public void copyFromLarger()
    {

    }

    /** Copy a loaded one to many from a smaller one */
    
    public void copyFromSmaller()
    {

    }

    /** Copy an empty one */
    
    public void copyEmpty()
    {

    }


    protected void assertEquals( OneToManyInt expected, OneToManyInt actual )
    {

    }
}
