package collections.relationship;

import core.Const;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.TestUtilsInt;

/**
 * Copyright 1/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/27/13
 */
public class TestOneToMany
{

    OneToMany oneToMany;

    @Before
    public void setup()
    {
        TestCase.assertTrue( oneToMany.isEmpty() );
        oneToMany = new OneToMany( 8, 16, true );
    }


    /** Do some simple associations, 3 with left of 2, 3 with left of 1 */
    @Test
    public void simpleAssociate()
    {
        TestCase.assertEquals( 0, oneToMany.getSize() );
        TestCase.assertEquals( 0, oneToMany.associate( 2, 5 ) );
        TestCase.assertFalse( oneToMany.isEmpty() );
        TestCase.assertEquals( 1, oneToMany.associate( 2, 8 ) );
        TestCase.assertEquals( 2, oneToMany.associate( 2, 9 ) );

        //repeat
        TestCase.assertEquals( 0, oneToMany.associate( 2, 5 ) );
        TestCase.assertEquals( 3, oneToMany.getSize() );

        TestCase.assertEquals( 3, oneToMany.associate( 1, 2 ) );
        TestCase.assertEquals( 4, oneToMany.associate( 1, 3 ) );
        TestCase.assertEquals( 5, oneToMany.associate( 1, 4 ) );

        TestCase.assertEquals( oneToMany.getCountForLeft( 2 ), 3 );
        TestCase.assertEquals( oneToMany.getCountForLeft( 1 ), 3 );


        //assert all associated
        TestCase.assertTrue( oneToMany.isAssociated( 2, 5 ) );
        TestCase.assertTrue( oneToMany.isAssociated( 2, 8 ) );
        TestCase.assertTrue( oneToMany.isAssociated( 2, 9 ) );
        TestCase.assertTrue( oneToMany.isAssociated( 1, 2 ) );
        TestCase.assertTrue( oneToMany.isAssociated( 1, 3 ) );
        TestCase.assertTrue( oneToMany.isAssociated( 1, 4 ) );

        //a false for each
        TestCase.assertFalse( oneToMany.isAssociated( 2, 2 ) );
        TestCase.assertFalse( oneToMany.isAssociated( 1, 9 ) );
    }

    /**
     * Do a manual iteration, and make sure it is insert order and assert size
     * Compare with getting all rights with a null target (should be exact size and same contents)
     */
    @Test
    public void iterateRightsForLeft()
    {
        simpleAssociate();

        int entry = Const.NO_ENTRY;
        int[] rights = new int[ 3 ];
        int rtCt = 0;
        while( ( entry = oneToMany.getNextRightEntry( 2, entry ) ) != Const.NO_ENTRY )
        {
            int right = oneToMany.getRight( entry );
            rights[ rtCt++ ] = right;
        }
        TestCase.assertEquals( rtCt, 3 );
        //insert order
        TestCase.assertEquals( rights[ 0 ], 5 );
        TestCase.assertEquals( rights[ 1 ], 8 );
        TestCase.assertEquals( rights[ 2 ], 9 );

        TestUtilsInt.assertArrayContentsEqual( rights, oneToMany.getAllRightAssociations( 2, null ) );
    }

    /** Assert that we will grow an array that is one off holding the set of associations */
    @Test
    public void getIterationsWithDifferentTargets()
    {
        simpleAssociate();
        int[] ofByOne = new int[ 2 ];
        //assert that we will grow the ofByOne array, we already know null will get correct array from test above
        TestUtilsInt.assertArrayContentsToLen( oneToMany.getAllRightAssociations( 1, null ),
                                               oneToMany.getAllRightAssociations( 1, ofByOne ), 3 );

    }

    @Test
    public void disassociateTest()
    {
        TestCase.assertFalse( oneToMany.disassociate( 0, 0 ) );
        simpleAssociate();
        //remove 5 (first association)
        TestCase.assertTrue( oneToMany.disassociate( 2, 5 ) );
        TestCase.assertFalse( oneToMany.disassociate( 2, 5 ) );
        int[] rights = oneToMany.getAllRightAssociations( 2, null );
        TestCase.assertEquals( rights[ 0 ], 8 );
        TestCase.assertEquals( rights[ 0 ], 9 );
        TestCase.assertEquals( oneToMany.getCountForLeft( 2 ), 2 );
        TestCase.assertEquals( oneToMany.getCountForLeft( 1 ), 3 );

        //remove 9 (last association)
        TestCase.assertTrue( oneToMany.disassociate( 2, 9 ) );
        TestCase.assertFalse( oneToMany.disassociate( 2, 9 ) );
        rights = oneToMany.getAllRightAssociations( 2, rights );
        TestCase.assertEquals( rights[ 0 ], 8 );
        TestCase.assertEquals( rights[ 0 ], Const.NO_ENTRY ); //make sure we null full
        TestCase.assertEquals( oneToMany.getCountForLeft( 2 ), 1 );
        TestCase.assertEquals( oneToMany.getCountForLeft( 1 ), 3 );

        //remove 8 (sole association left)
        TestCase.assertTrue( oneToMany.disassociate( 2, 8 ) );
        TestCase.assertFalse( oneToMany.disassociate( 2, 8 ) );
        rights = oneToMany.getAllRightAssociations( 2, rights );
        TestCase.assertEquals( rights[ 0 ], Const.NO_ENTRY );  //make sure we null full
        TestCase.assertEquals( rights[ 0 ], Const.NO_ENTRY );
        TestCase.assertEquals( oneToMany.getCountForLeft( 2 ), 0 );
        TestCase.assertEquals( oneToMany.getCountForLeft( 1 ), 3 );

        //remove 3 (middle item) first
        TestCase.assertTrue( oneToMany.disassociate( 1, 3 ) );
        TestCase.assertFalse( oneToMany.disassociate( 1, 3 ) );
        rights = oneToMany.getAllRightAssociations( 1, rights );
        TestCase.assertEquals( rights[ 0 ], 2 );
        TestCase.assertEquals( rights[ 0 ], 4 );
        TestCase.assertEquals( oneToMany.getCountForLeft( 2 ), 0 );
        TestCase.assertEquals( oneToMany.getCountForLeft( 1 ), 2 );

    }

    /** Assert clear will result in a empty structure */
    @Test
    public void clearTest()
    {
        OneToMany newOneToMany = new OneToMany( 8, 16, false );
        try
        {
            TestCase.assertEquals( oneToMany.getCountForLeft( 2 ), 0 );
            TestCase.fail( "Not counting lefts, set countLefts to true if want to keep track of counts." );
        }
        catch( IllegalStateException e )
        {

        }
        simpleAssociate();
        oneToMany.clear();
        TestCase.assertTrue( oneToMany.isEmpty() );
        TestCase.assertEquals( newOneToMany.getSize(), oneToMany.getSize() );
        TestCase.assertEquals( newOneToMany.getAllRightAssociations( 1, null ), oneToMany.getAllRightAssociations( 1,
                                                                                                                   null
        ) );
        TestCase.assertEquals( newOneToMany.getAllRightAssociations( 2, null ), oneToMany.getAllRightAssociations( 2,
                                                                                                                   null
        ) );
        TestCase.assertTrue( oneToMany.getSize() == 0 );

    }

    /** Grow Lefts and Associations Test */
    public void growthTest()
    {
        OneToMany newOneToMany = new OneToMany( 8, 16, false );
        for( int i = 0; i < 600; i++ )
        {
            newOneToMany.associate( i, i );
            newOneToMany.associate( i, i + 4500 );
            newOneToMany.associate( i, i + 19000 );
        }
        TestCase.assertEquals( newOneToMany.getSize(), 1800 );
    }

    /** Assert Compact Associations that are immediatelyRe-used */
    @Test
    public void testCompactness()
    {
        OneToMany newOneToMany = new OneToMany( 8, 16, false );
        for( int i = 0; i < 600; i++ )
        {
            //assert compact,
            TestCase.assertEquals( i, newOneToMany.associate( i, i ) );
            //remove
            newOneToMany.disassociate( i, i );
            //assert immediate re-use
            TestCase.assertEquals( i, newOneToMany.associate( i, i + 1900 ) );
        }
        TestCase.assertEquals( newOneToMany.getSize(), 1800 );
    }

    //TODO: copy tests


}
