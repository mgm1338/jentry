package collections.relationship;

import core.Const;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

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
        oneToMany = new OneToMany();
    }


    /** Do a few associations */
    @Test
    public void simpleAssociate()
    {
        TestCase.assertEquals( 0, oneToMany.getSize() );
        TestCase.assertEquals( 0, oneToMany.associate( 2, 5 ) );
        TestCase.assertEquals( 1, oneToMany.associate( 2, 8 ) );
        TestCase.assertEquals( 2, oneToMany.associate( 2, 9 ) );

        //repeat
        TestCase.assertEquals( 0, oneToMany.associate( 2, 5 ) );
        TestCase.assertEquals( 3, oneToMany.getSize() );

        TestCase.assertEquals( 3, oneToMany.associate( 1, 2 ) );
        TestCase.assertEquals( 4, oneToMany.associate( 1, 3 ) );
        TestCase.assertEquals( 5, oneToMany.associate( 1, 4 ) );

        //assert all associated
        TestCase.assertTrue( oneToMany.isAssociated(2,5) );
        TestCase.assertTrue( oneToMany.isAssociated(2,8) );
        TestCase.assertTrue( oneToMany.isAssociated(2,9) );
        TestCase.assertTrue( oneToMany.isAssociated(1,2) );
        TestCase.assertTrue( oneToMany.isAssociated(1,3) );
        TestCase.assertTrue( oneToMany.isAssociated(1,4) );

        //a false for each
        TestCase.assertFalse( oneToMany.isAssociated( 2, 2 ) );
        TestCase.assertFalse( oneToMany.isAssociated(1,9) );
    }

    @Test
    public void iterateRightsForLeft()
    {
        simpleAssociate();

        int entry = Const.NO_ENTRY;
        while ((entry = oneToMany.getNextRightEntry( 2, entry))!=Const.NO_ENTRY)
        {
            int right = oneToMany.getRight(entry);
        }

    }



}
