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


    @Test
    public void iterateLeftsForRight()
    {

        int entry = Const.NO_ENTRY;
        int[] rights = new int[ 4 ];
        int rtCt = 0;
        while( ( entry = manyToManyNoCounts.getNextLeftEntry( 1, entry ) ) != Const.NO_ENTRY )
        {
            int right = manyToManyNoCounts.getRight( entry );
            rights[ rtCt++ ] = right;
        }
        TestCase.assertEquals( rtCt, 4 );
        //insert order
        TestCase.assertEquals(  0, rights[ 0 ] );
        TestCase.assertEquals(  2, rights[ 1 ] );
        TestCase.assertEquals(  3, rights[ 2 ] );
        TestCase.assertEquals(  4, rights[ 2 ] );


        TestUtilsInt.assertArrayContentsToLen( rights, manyToManyNoCounts.getAllLeftAssociations( 2, null,
                                                                                             Const.NO_ENTRY ), 3 );
    }


    /** Assert that we will grow an array that is one off holding the set of associations */
    @Test
    public void getIterationsWithDifferentTargets()
    {
        int[] ofByOne = new int[ 3 ];
        //assert that we will grow the ofByOne array, we already know null will get correct array from test above
        TestUtilsInt.assertArrayContentsToLen( manyToManyNoCounts.getAllLeftAssociations( 1, null, Const.NO_ENTRY ),
                                               manyToManyNoCounts.getAllLeftAssociations( 1, ofByOne,
                                                                                          Const.NO_ENTRY ), 4 );

    }





}
