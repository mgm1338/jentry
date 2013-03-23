package core.array.growth;

import core.NumberUtil;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Copyright 1/27/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/27/13
 */
public class TestNumberUtil
{

    @Test
    public void testPackingLongs()
    {
        long full = NumberUtil.packLong( Integer.MAX_VALUE, Integer.MAX_VALUE );
        TestCase.assertEquals( Integer.MAX_VALUE, NumberUtil.getLeft( full ) );
        TestCase.assertEquals( Integer.MAX_VALUE, NumberUtil.getRight( full ) );

        long zero = NumberUtil.packLong( 0, 0 );
        TestCase.assertEquals( 0, NumberUtil.getLeft( zero ) );
        TestCase.assertEquals( 0, NumberUtil.getRight( zero ) );

        long min = NumberUtil.packLong( Integer.MIN_VALUE, Integer.MIN_VALUE );
        TestCase.assertEquals( Integer.MIN_VALUE, NumberUtil.getLeft( min ) );
        TestCase.assertEquals( Integer.MIN_VALUE, NumberUtil.getRight( min ) );

        long normal = NumberUtil.packLong( 10358, 80000 );
        TestCase.assertEquals( 10358, NumberUtil.getLeft( normal ) );
        TestCase.assertEquals( 80000, NumberUtil.getRight( normal ) );

    }
}
