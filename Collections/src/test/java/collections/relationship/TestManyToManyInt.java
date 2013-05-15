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


    /**
     * Do some simple associations with both ManyToManyInts, shown below
     * <p/>
     * L                      R
     * 0   ->1                1   ->0,2,3,4
     * 2   ->1                6   ->25,26,27
     * 3   ->1,19,20,21       19  ->3
     * 4   ->1                20  ->3
     * 25  ->6                21  ->3
     * 26  ->6
     * 27  ->6
     */
    @Test
    public void simpleAssociate()
    {
        TestCase.assertEquals( 0, manyToManyCounts.associate( 0, 1 ) );
        TestCase.assertEquals( 1, manyToManyCounts.associate( 2, 1 ) );
        TestCase.assertEquals( 2, manyToManyCounts.associate( 3, 1 ) );
        TestCase.assertEquals( 3, manyToManyCounts.associate( 4, 1 ) );
        TestCase.assertEquals( 4, manyToManyCounts.getCountForRight( 1 ) );

        TestCase.assertEquals( 4, manyToManyCounts.associate( 3, 19 ) );
        TestCase.assertEquals( 5, manyToManyCounts.associate( 3, 20 ) );
        TestCase.assertEquals( 1, manyToManyCounts.getCountForRight( 20 ) );

        TestCase.assertEquals( 6, manyToManyCounts.associate( 3, 21 ) );
        TestCase.assertEquals( 7, manyToManyCounts.associate( 25, 6 ) );
        TestCase.assertEquals( 8, manyToManyCounts.associate( 26, 6 ) );
        TestCase.assertEquals( 9, manyToManyCounts.associate( 27, 6 ) );
        TestCase.assertEquals( 3, manyToManyCounts.getCountForRight( 6) );


        TestCase.assertEquals( 0, manyToManyNoCounts.associate( 0, 1 ) );
        TestCase.assertEquals( 1, manyToManyNoCounts.associate( 2, 1 ) );
        TestCase.assertEquals( 2, manyToManyNoCounts.associate( 3, 1 ) );
        TestCase.assertEquals( 3, manyToManyNoCounts.associate( 4, 1 ) );
        TestCase.assertEquals( 4, manyToManyNoCounts.getCountForRight( 1 ) );

        TestCase.assertEquals( 4, manyToManyNoCounts.associate( 3, 19 ) );
        TestCase.assertEquals( 5, manyToManyNoCounts.associate( 3, 20 ) );
        TestCase.assertEquals( 1, manyToManyNoCounts.getCountForRight( 20 ) );

        TestCase.assertEquals( 6, manyToManyNoCounts.associate( 3, 21 ) );
        TestCase.assertEquals( 7, manyToManyNoCounts.associate( 25, 6 ) );
        TestCase.assertEquals( 8, manyToManyNoCounts.associate( 26, 6 ) );
        TestCase.assertEquals( 9, manyToManyNoCounts.associate( 27, 6 ) );
        TestCase.assertEquals( 3, manyToManyNoCounts.getCountForRight( 6 ) );


        TestCase.assertEquals( 10, manyToManyCounts.getSize() );
        TestCase.assertEquals( 10, manyToManyNoCounts.getSize() );
    }


    /**
     * Do a manual iteration, and make sure it is insertLeft order and assert size
     * Compare with getting all rights with a null target (should be exact size and same contents)
     */
    @Test
    public void iterateRightsForLeftandLeftsForRight()
    {
        //iterate rights for 3,
        simpleAssociate();
        int i = 0;
        int[] collected = new int[ 4 ];
        int entry = Const.NO_ENTRY;
        while( ( entry = manyToManyNoCounts.getNextRightEntry( 3, entry ) ) != Const.NO_ENTRY )
        {
            collected[ i++ ] = manyToManyNoCounts.getRight( entry );
        }
        TestCase.assertEquals( 1, collected[ 0 ] );
        TestCase.assertEquals( 19, collected[ 1 ] );
        TestCase.assertEquals( 20, collected[ 2 ] );
        TestCase.assertEquals( 21, collected[ 3 ] );
        TestUtilsInt.assertArrayContentsEqual( collected, manyToManyCounts.getAllRightAssociations( 3, null,
                                                                                                    Const.NO_ENTRY ) );
        TestUtilsInt.assertArrayContentsToLen( collected, manyToManyNoCounts.getAllRightAssociations( 3, null,
                                                                                                      Const.NO_ENTRY ), 4
        );

        //iterate lefts for 1,
        i = 0;
        entry = Const.NO_ENTRY;
        while( ( entry = manyToManyNoCounts.getNextLeftEntry( 1, entry ) ) != Const.NO_ENTRY )
        {
            collected[ i++ ] = manyToManyNoCounts.getLeft( entry );
        }

        TestCase.assertEquals( 0, collected[ 0 ] );
        TestCase.assertEquals( 2, collected[ 1 ] );
        TestCase.assertEquals( 3, collected[ 2 ] );
        TestCase.assertEquals( 4, collected[ 3 ] );

        TestUtilsInt.assertArrayContentsEqual( collected, manyToManyCounts.getAllLeftAssociations( 1, null,
                                                                                                   Const.NO_ENTRY ) );
        TestUtilsInt.assertArrayContentsToLen( collected, manyToManyNoCounts.getAllLeftAssociations( 1, null,
                                                                                                     Const.NO_ENTRY ), 4
        );


    }

    /** Assert that we will grow an array that is one off holding the set of associations */
    @Test
    public void getAllLeftRightSmallArrays()
    {
        int[] oneToSmall = new int[ 3 ];
        simpleAssociate();
        TestUtilsInt.assertArrayContentsToLen( manyToManyNoCounts.getAllRightAssociations( 3, null, Const.NO_ENTRY ),
                                               manyToManyNoCounts.getAllRightAssociations( 3, oneToSmall, Const.NO_ENTRY ), 4
        );
        oneToSmall = new int[ 3 ];
        TestUtilsInt.assertArrayContentsToLen( manyToManyCounts.getAllRightAssociations( 3, null, Const.NO_ENTRY ),
                                               manyToManyCounts.getAllRightAssociations( 3, oneToSmall, Const.NO_ENTRY ), 4
        );
        oneToSmall = new int[ 3 ];

        TestUtilsInt.assertArrayContentsToLen( manyToManyNoCounts.getAllLeftAssociations( 1, null, Const.NO_ENTRY ),
                                               manyToManyNoCounts.getAllLeftAssociations( 1, oneToSmall,
                                                                                          Const.NO_ENTRY ), 4
        );
        oneToSmall = new int[ 3 ];
        TestUtilsInt.assertArrayContentsToLen( manyToManyCounts.getAllLeftAssociations( 1, null, Const.NO_ENTRY ),
                                               manyToManyCounts.getAllLeftAssociations( 1, oneToSmall,
                                                                                        Const.NO_ENTRY ), 4
        );

    }


    /**
     * FOR REFERENCE:
     * <p/>
     * L                      R
     * 0   ->1                1   ->0,2,3,4
     * 2   ->1                6   ->25,26,27
     * 3   ->1,19,20,21       19  ->3
     * 4   ->1                20  ->3
     * 25  ->6                21  ->3
     * 26  ->6
     * 27  ->6
     * <p/>
     * Disassociate many pairs and make assertions about the remaining relationships
     */
    @Test
    public void disassociateTest()
    {
        simpleAssociate();


        TestCase.assertEquals( 10, manyToManyCounts.getSize() );
        TestCase.assertEquals( 10, manyToManyNoCounts.getSize() );

        TestCase.assertEquals( 0, manyToManyCounts.disassociate( 0, 1 ) );
        TestCase.assertEquals( 0, manyToManyNoCounts.disassociate( 0, 1 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.disassociate( 0, 1 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyNoCounts.disassociate( 0, 1 ) );
        //removed one
        TestCase.assertEquals( 9, manyToManyCounts.getSize() );
        TestCase.assertEquals( 9, manyToManyNoCounts.getSize() );
        //not valid associations
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.disassociate( 0, 0 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.disassociate( 0, 0 ) );
        //still same size
        TestCase.assertEquals( 9, manyToManyCounts.getSize() );
        TestCase.assertEquals( 9, manyToManyNoCounts.getSize() );
        //no remaining rights for left 0
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.getNextRightEntry( 0, Const.NO_ENTRY ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyNoCounts.getNextRightEntry( 0, Const.NO_ENTRY ) );

        int[] remainingLefts = manyToManyCounts.getAllLeftAssociations( 1, null, -1 );
        int[] remainingLeftsCt = manyToManyNoCounts.getAllLeftAssociations( 1, null, -1 );

        //both are same up to 3
        TestUtilsInt.assertArrayContentsToLen( remainingLefts, remainingLeftsCt, 3 );
        int[] expected = new int[]{ 2, 3, 4 };
        //same as expected (and therefore both are equal to expected)
        TestUtilsInt.assertArrayContentsToLen( expected, remainingLeftsCt, 3 );

        TestCase.assertEquals( 9, manyToManyCounts.getSize() );
        TestCase.assertEquals( 9, manyToManyNoCounts.getSize() );
        TestCase.assertEquals( 4, manyToManyCounts.disassociate( 3, 19 ) );
        TestCase.assertEquals( 4, manyToManyNoCounts.disassociate( 3, 19 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.disassociate( 3, 19 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyNoCounts.disassociate( 3, 19 ) );
        TestCase.assertEquals( 8, manyToManyCounts.getSize() );
        TestCase.assertEquals( 8, manyToManyNoCounts.getSize() );
        TestCase.assertEquals( 5, manyToManyCounts.disassociate( 3, 20 ) );
        TestCase.assertEquals( 5, manyToManyNoCounts.disassociate( 3, 20 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.disassociate( 3, 20 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyNoCounts.disassociate( 3, 20 ) );
        TestCase.assertEquals( 7, manyToManyCounts.getSize() );
        TestCase.assertEquals( 7, manyToManyNoCounts.getSize() );
        TestCase.assertEquals( 6, manyToManyCounts.disassociate( 3, 21 ) );
        TestCase.assertEquals( 6, manyToManyNoCounts.disassociate( 3, 21 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.disassociate( 3, 21 ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyNoCounts.disassociate( 3, 21 ) );
        TestCase.assertEquals( 6, manyToManyCounts.getSize() );
        TestCase.assertEquals( 6, manyToManyNoCounts.getSize() );

        TestCase.assertEquals( Const.NO_ENTRY, manyToManyCounts.getNextLeftEntry( 19, Const.NO_ENTRY ) );
        TestCase.assertEquals( Const.NO_ENTRY, manyToManyNoCounts.getNextLeftEntry( 19, Const.NO_ENTRY ) );

        //(3,1) is entry 2
        TestCase.assertEquals( 2, manyToManyCounts.getNextRightEntry( 3, Const.NO_ENTRY ) );
        TestCase.assertEquals( 2, manyToManyNoCounts.getNextRightEntry( 3, Const.NO_ENTRY ) );

        //adding to previously almost all removed left of 3,
        manyToManyCounts.associate(  40,3  );
        manyToManyNoCounts.associate( 40, 3 );
        manyToManyCounts.associate(  41, 3 );
        manyToManyNoCounts.associate( 41, 3 ); //expect 1,40,41


        int[] remainingRights = manyToManyCounts.getAllLeftAssociations( 3, null, -1 );
        int[] remainingRightsCt = manyToManyNoCounts.getAllLeftAssociations( 3, null, -1 );
        //both are same up to 2
        TestUtilsInt.assertArrayContentsToLen( remainingRights, remainingRightsCt, 2 );


        TestCase.assertEquals( 8, manyToManyCounts.getSize() );
        TestCase.assertEquals( 8, manyToManyNoCounts.getSize() );

        //EXPECTED AT END
        /*
         * L                      R
         * 2   ->1                1   ->2,3,4
         * 3   ->1,40,41          6   ->25,26,27
         * 4   ->1                40  ->3
         * 25  ->6                41  ->3
         * 26  ->6
         * 27  ->6
         */
    }

    /** Assert clear will result in a empty structure */
    @Test
    public void clearTest()
    {
        ManyToManyInt newClearMap = new ManyToManyInt( 8, 16, 8, false, false );
        simpleAssociate();
        manyToManyCounts.clear();
        manyToManyNoCounts.clear();

        TestCase.assertEquals( newClearMap.getSize(), manyToManyCounts.getSize() );
        TestCase.assertEquals( manyToManyNoCounts.getSize(), manyToManyCounts.getSize() );
    }


    @Test
    public void growthTest()
    {
        TestCase.assertEquals( 0, manyToManyCounts.getSize() );
        TestCase.assertEquals( 0, manyToManyNoCounts.getSize() );

        for( int i = 0; i < 100; i++ )
        {
            manyToManyCounts.associate( i, i +200 );
            manyToManyCounts.associate( i +200, i );

            manyToManyNoCounts.associate( i, i +200 );
            manyToManyNoCounts.associate( i +200, i );

            TestCase.assertEquals( ( i + 1 ) * 2, manyToManyCounts.getSize() );
            TestCase.assertEquals( ( i + 1 ) * 2, manyToManyNoCounts.getSize() );
        }

        TestCase.assertEquals( 200, manyToManyCounts.getSize() );
        TestCase.assertEquals( 200, manyToManyNoCounts.getSize() );

    }

    /** Assert Compact Associations that are immediatelyRe-used */
    @Test
    public void testCompactness()
    {
        TestCase.assertEquals( 0, manyToManyCounts.getSize() );
        TestCase.assertEquals( 0, manyToManyNoCounts.getSize() );

        for( int i = 0; i < 100; i++ )
        {
            TestCase.assertEquals( i, manyToManyCounts.associate( i, i + 2 ) );
            TestCase.assertEquals( i, manyToManyNoCounts.associate( i, i + 2 ) );

            TestCase.assertEquals( i + 1, manyToManyCounts.associate( i, i + 4 ) );
            TestCase.assertEquals( i + 1, manyToManyNoCounts.associate( i, i + 4 ) );

            TestCase.assertEquals( i + 1, manyToManyCounts.disassociate( i, i + 4 ) );
            TestCase.assertEquals( i + 1, manyToManyNoCounts.disassociate( i, i + 4 ) );
        }

        TestCase.assertEquals( 100, manyToManyCounts.getSize() );
        TestCase.assertEquals( 100, manyToManyNoCounts.getSize() );


    }

    /** Copy a loaded one to many from a null target */
    @Test
    public void copyFromNull()
    {
        simpleAssociate();
        ManyToManyInt copy = manyToManyCounts.copy( null );
        ManyToManyInt copy1 = manyToManyNoCounts.copy( null );
        //transitive property
        assertEquals(copy, copy1);
        assertEquals(manyToManyNoCounts, copy1);
    }

    /** Copy a loaded one to many from a larger one */
    @Test
    public void copyFromLarger()
    {
        simpleAssociate();
        ManyToManyInt copy = manyToManyCounts.copy( new ManyToManyInt( 64,64,128,false, false ) );
        ManyToManyInt copy1 = manyToManyNoCounts.copy( new ManyToManyInt( 64,64,128,true, true) );
        //transitive property
        assertEquals(copy, copy1);
        assertEquals(manyToManyCounts, copy1);

    }

    /** Copy a loaded one to many from a smaller one */
    @Test
    public void copyFromSmaller()
    {
        simpleAssociate();
        ManyToManyInt copy = manyToManyCounts.copy( new ManyToManyInt( 1,1,2,false, false ) );
        ManyToManyInt copy1 = manyToManyNoCounts.copy( new ManyToManyInt( 1,1,2,true, true) );
        //transitive property
        assertEquals(copy, copy1);
        assertEquals(manyToManyCounts, copy1);
    }

    /** Copy an empty one */
    @Test
    public void copyEmpty()
    {
        ManyToManyInt copy = manyToManyCounts.copy( null );
        ManyToManyInt copy1 = manyToManyNoCounts.copy( null );
        //transitive property
        assertEquals(copy, copy1);
        assertEquals(manyToManyCounts, copy1);
    }


    protected void assertEquals( ManyToManyInt expected, ManyToManyInt actual )
    {
        TestCase.assertEquals( expected.getSize(), actual.getSize() );
        TestUtilsInt.assertArrayContentsToLen( expected.lefts, actual.lefts, expected.leftHighWaterMark );
        TestUtilsInt.assertArrayContentsToLen( expected.leftNexts, actual.leftNexts, expected.getSize() );
        TestUtilsInt.assertArrayContentsToLen( expected.rights, actual.rights, expected.rightHighWaterMark );
        TestUtilsInt.assertArrayContentsToLen( expected.leftNexts, actual.leftNexts, expected.getSize() );
        TestHashSetLong.assertEquals( expected.associations, actual.associations );
    }
}
