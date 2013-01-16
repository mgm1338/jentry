package collections.hash.set;

import collections.hash.set.HashSet_KeyTypeName_;
import core.stub.IntValueConverter;
import junit.extensions.TestSetup;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 1/14/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/14/13
 */
public class TestHashSet_KeyTypeName_
{
    HashSet_KeyTypeName_ hashSet;
    boolean template = (this.getClass().getCanonicalName().contains( "_" ));

    public static final int TEST_SIZE = 8;
    @Before
    public void setup()
    {
        if (template) return;
        hashSet = new HashSet_KeyTypeName_( TEST_SIZE );
        TestCase.assertTrue( hashSet.isEmpty() );
        TestCase.assertTrue(hashSet.getSize()==0);

        //initial fill up
        for( int i = 0; i < TEST_SIZE; i++ )
        {
            int j = hashSet.insert( IntValueConverter._key_FromInt( i ) );
            TestCase.assertEquals( i, j ); //compact
        }

    }

    @Test
    public void loadedTest()
    {
        if (template) return;
        TestCase.assertTrue(hashSet.getSize()==TEST_SIZE);
        TestCase.assertFalse( hashSet.isEmpty() );

        for (int i=0; i<TEST_SIZE; i++)
        {
            TestCase.assertEquals( hashSet.get( i ), IntValueConverter._key_FromInt( i ));
        }


    }




}
