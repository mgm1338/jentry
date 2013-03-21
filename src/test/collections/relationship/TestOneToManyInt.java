package collections.relationship;

import collections.hash.set.TestHashSetLong;
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
public class TestOneToManyInt
{

    OneToManyInt oneToManyInt;

    @Before
    public void setup()
    {
        oneToManyInt = new OneToManyInt( 8, 16, true );
        TestCase.assertTrue( oneToManyInt.isEmpty() );
    }


    /** Do some simple associations, 3 with left of 2, 3 with left of 1 */
    @Test
    public void simpleAssociate()
    {
        TestCase.assertEquals( 0, oneToManyInt.getSize() );
        TestCase.assertEquals( 0, oneToManyInt.associate( 2, 5 ) );
        TestCase.assertFalse( oneToManyInt.isEmpty() );
        TestCase.assertEquals( 1, oneToManyInt.associate( 2, 8 ) );
        TestCase.assertEquals( 2, oneToManyInt.associate( 2, 9 ) );

        //repeat
        TestCase.assertEquals( 0, oneToManyInt.associate( 2, 5 ) );
        TestCase.assertEquals( 3, oneToManyInt.getSize() );

        TestCase.assertEquals( 3, oneToManyInt.associate( 1, 2 ) );
        TestCase.assertEquals( 4, oneToManyInt.associate( 1, 3 ) );
        TestCase.assertEquals( 5, oneToManyInt.associate( 1, 4 ) );

        TestCase.assertEquals(  3, oneToManyInt.getCountForLeft( 2 ) );
        TestCase.assertEquals(  3, oneToManyInt.getCountForLeft( 1 ) );
        TestCase.assertEquals(  0, oneToManyInt.getCountForLeft( 4 ) );



        //assert all associated
        TestCase.assertTrue( oneToManyInt.isAssociated( 2, 5 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 2, 8 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 2, 9 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 1, 2 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 1, 3 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 1, 4 ) );

        //a false for each
        TestCase.assertFalse( oneToManyInt.isAssociated( 2, 2 ) );
        TestCase.assertFalse( oneToManyInt.isAssociated( 1, 9 ) );
    }


    //same as above EXCEPT constructs new structure that doesnt track
    @Test
    public void simpleAssociateNoTracking()
    {

        oneToManyInt = new OneToManyInt( 8, 16, false );
        TestCase.assertEquals( 0, oneToManyInt.getSize() );
        TestCase.assertEquals( 0, oneToManyInt.associate( 2, 5 ) );
        TestCase.assertFalse( oneToManyInt.isEmpty() );
        TestCase.assertEquals( 1, oneToManyInt.associate( 2, 8 ) );
        TestCase.assertEquals( 2, oneToManyInt.associate( 2, 9 ) );

        //repeat
        TestCase.assertEquals( 0, oneToManyInt.associate( 2, 5 ) );
        TestCase.assertEquals( 3, oneToManyInt.getSize() );

        TestCase.assertEquals( 3, oneToManyInt.associate( 1, 2 ) );
        TestCase.assertEquals( 4, oneToManyInt.associate( 1, 3 ) );
        TestCase.assertEquals( 5, oneToManyInt.associate( 1, 4 ) );

        TestCase.assertEquals( 1, oneToManyInt.getLeft( oneToManyInt.associate( 1, 2 ) ) );
        TestCase.assertEquals( 1, oneToManyInt.getLeft( oneToManyInt.associate( 1, 3 ) ) );
        TestCase.assertEquals( 1, oneToManyInt.getLeft( oneToManyInt.associate( 1, 4 ) ) );

        TestCase.assertEquals(  3, oneToManyInt.getCountForLeft( 2 ) );
        TestCase.assertEquals(  3, oneToManyInt.getCountForLeft( 1 ) );
        TestCase.assertEquals(  0, oneToManyInt.getCountForLeft( 4 ) );



        //assert all associated
        TestCase.assertTrue( oneToManyInt.isAssociated( 2, 5 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 2, 8 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 2, 9 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 1, 2 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 1, 3 ) );
        TestCase.assertTrue( oneToManyInt.isAssociated( 1, 4 ) );

        //a false for each
        TestCase.assertFalse( oneToManyInt.isAssociated( 2, 2 ) );
        TestCase.assertFalse( oneToManyInt.isAssociated( 1, 9 ) );
    }

    /**
     * Do a manual iteration, and make sure it is insert order and assert size
     * Compare with getting all rights with a null target (should be exact size and same contents)
     */
    @Test
    public void iterateRightsForLeft()
    {
        simpleAssociateNoTracking();

        int entry = Const.NO_ENTRY;
        int[] rights = new int[ 3 ];
        int rtCt = 0;
        while( ( entry = oneToManyInt.getNextRightEntry( 2, entry ) ) != Const.NO_ENTRY )
        {
            int right = oneToManyInt.getRight( entry );
            rights[ rtCt++ ] = right;
        }
        TestCase.assertEquals( rtCt, 3 );
        //insert order
        TestCase.assertEquals(  5, rights[ 0 ] );
        TestCase.assertEquals(  8, rights[ 1 ] );
        TestCase.assertEquals(  9, rights[ 2 ] );

        TestUtilsInt.assertArrayContentsToLen( rights, oneToManyInt.getAllRightAssociations( 2, null,
                                                                                             Const.NO_ENTRY ), 3 );
    }

    /** Assert that we will grow an array that is one off holding the set of associations */
    @Test
    public void getAllRightsToSmallArray()
    {
        simpleAssociate();
        int[] ofByOne = new int[ 2 ];
        //assert that we will grow the ofByOne array, we already know null will get correct array from test above
        TestUtilsInt.assertArrayContentsToLen( oneToManyInt.getAllRightAssociations( 1, null, Const.NO_ENTRY ),
                                               oneToManyInt.getAllRightAssociations( 1, ofByOne, Const.NO_ENTRY ), 3 );

    }

    @Test
    public void disassociateTest()
    {
        TestCase.assertEquals(Const.NO_ENTRY,  oneToManyInt.disassociate( 0, 0 ) );
        simpleAssociate();
        //remove 5 (first association)
        TestCase.assertEquals( 0, oneToManyInt.disassociate( 2, 5 ) );
        TestCase.assertEquals( Const.NO_ENTRY,oneToManyInt.disassociate( 2, 5 ) );
        int[] rights = oneToManyInt.getAllRightAssociations( 2, new int[2], Const.NO_ENTRY );
        TestCase.assertEquals(  8 ,rights[ 0 ]);
        TestCase.assertEquals(  9, rights[ 1 ] );
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 2 ), 2 );
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 1 ), 3 );

        //remove 9 (last association)
        TestCase.assertEquals(2,  oneToManyInt.disassociate( 2, 9 ) );
        TestCase.assertEquals(Const.NO_ENTRY, oneToManyInt.disassociate( 2, 9 ) );
        rights = oneToManyInt.getAllRightAssociations( 2, new int[2], Const.NO_ENTRY );
        TestCase.assertEquals(  8, rights[0] );
        TestCase.assertEquals( Const.NO_ENTRY, rights[ 1 ] ); //make sure we null full
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 2 ), 1 );
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 1 ), 3 );

        //remove 8 (sole association left)
        TestCase.assertEquals( 1, oneToManyInt.disassociate( 2, 8 ) );
        TestCase.assertEquals( Const.NO_ENTRY, oneToManyInt.disassociate( 2, 8 ) );
        rights = oneToManyInt.getAllRightAssociations( 2, new int[2], Integer.MAX_VALUE );
        TestCase.assertEquals(  Integer.MAX_VALUE, rights[ 0 ] );  //end of array with max val
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 2 ), 0 );
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 1 ), 3 );

        //remove 3 (middle item) first
        TestCase.assertEquals( 4, oneToManyInt.disassociate( 1, 3 ) );
        TestCase.assertEquals(Const.NO_ENTRY,  oneToManyInt.disassociate( 1, 3 ) );
        rights = oneToManyInt.getAllRightAssociations( 1, null, Const.NO_ENTRY );
        TestCase.assertEquals(  2, rights[ 0 ] );
        TestCase.assertEquals(  4, rights[ 1 ] );
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 2 ), 0 );
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 1 ), 2 );

        oneToManyInt.clear();
        simpleAssociate();
        //remove 3 (last item) first
        TestCase.assertEquals(5,  oneToManyInt.disassociate( 1, 4 ) );
        TestCase.assertEquals( Const.NO_ENTRY, oneToManyInt.disassociate( 1, 4 ) );
        rights = oneToManyInt.getAllRightAssociations( 1, null, Const.NO_ENTRY );
        TestCase.assertEquals(  2, rights[ 0 ] );
        TestCase.assertEquals(  3, rights[ 1 ] );


    }

    /** Assert clear will result in a empty structure */
    @Test
    public void clearTest()
    {
        OneToManyInt newOneToManyInt = new OneToManyInt( 8, 16, false );
        TestCase.assertEquals( oneToManyInt.getCountForLeft( 2 ), 0 );    //count in normal way

        simpleAssociate();
        oneToManyInt.clear();
        TestCase.assertTrue( oneToManyInt.isEmpty() );
        TestCase.assertEquals( newOneToManyInt.getSize(), oneToManyInt.getSize() );
        TestUtilsInt.assertArrayContentsEqual( newOneToManyInt.getAllRightAssociations( 1, new int[ 4 ], Const.NO_ENTRY ),
                               oneToManyInt.getAllRightAssociations( 1, new int[ 4 ], Const.NO_ENTRY ) );
        TestUtilsInt.assertArrayContentsEqual( newOneToManyInt.getAllRightAssociations( 2, new int[ 4 ], Const.NO_ENTRY ),
                                               oneToManyInt.getAllRightAssociations( 2, new int[ 4 ], Const.NO_ENTRY ) );
        TestCase.assertTrue( oneToManyInt.getSize() == 0 );

    }

    /** Grow Lefts and Associations Test */
    public void growthTest()
    {
        OneToManyInt newOneToManyInt = new OneToManyInt( 8, 16, false );
        for( int i = 0; i < 600; i++ )
        {
            newOneToManyInt.associate( i, i );
            newOneToManyInt.associate( i, i + 4500 );
            newOneToManyInt.associate( i, i + 19000 );
        }
        TestCase.assertEquals( newOneToManyInt.getSize(), 1800 );
    }

    /** Assert Compact Associations that are immediatelyRe-used */
    @Test
    public void testCompactness()
    {
        OneToManyInt newOneToManyInt = new OneToManyInt( 8, 16, false );
        for( int i = 0; i < 1800; i++ )
        {
            //assert compact,
            TestCase.assertEquals( i, newOneToManyInt.associate( i, i ) );
            //remove
            newOneToManyInt.disassociate( i, i );
            //assert immediate re-use
            TestCase.assertEquals( i, newOneToManyInt.associate( i, i + 1900 ) );
        }
        TestCase.assertEquals( newOneToManyInt.getSize(), 1800 );
    }

    /** Copy a loaded one to many from a null target */
    @Test
    public void copyFromNull()
    {
        simpleAssociate();
        OneToManyInt copy = oneToManyInt.copy( null );
        assertEquals( copy, oneToManyInt );
    }

    /** Copy a loaded one to many from a larger one */
    @Test
    public void copyFromLarger()
    {
        simpleAssociate();
        OneToManyInt copy = new OneToManyInt( 400, 1000, false );
        copy = oneToManyInt.copy( copy );
        assertEquals( oneToManyInt, copy );
    }

    /** Copy a loaded one to many from a smaller one */
    @Test
    public void copyFromSmaller()
    {
        simpleAssociate();
        OneToManyInt copy = new OneToManyInt( 1, 1, false );
        copy = oneToManyInt.copy( copy );
        assertEquals( oneToManyInt, copy );

    }

    /** Copy an empty one */
    @Test
    public void copyEmpty()
    {
        OneToManyInt copy = new OneToManyInt( 1, 1, false );
        copy = oneToManyInt.copy( copy );
        assertEquals( oneToManyInt, copy );
    }


    /**
     * Cheating way of comparing equality (cheating because it accesses internals).
     * We dont care if one OneToManyInt counts lefts and the other doesnt
     *
     * @param expected
     * @param actual
     */
    protected void assertEquals( OneToManyInt expected, OneToManyInt actual )
    {
        TestCase.assertEquals( expected.getSize(), actual.getSize() );
        TestUtilsInt.assertArrayContentsToLen( expected.lefts, actual.lefts, expected.getSize() );
        TestUtilsInt.assertArrayContentsToLen( expected.leftNexts, actual.leftNexts, expected.getSize() );
        TestHashSetLong.assertEquals( expected.associations, actual.associations );
    }


}
