package util;

import core.util.comparator.ComparatorObject;

/**
 * Copyright 2/26/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 2/26/13
 */
public class TestComparators
{
    /**
     * Stubbed class for comparing Objects
     */
    public static final class ObjectAsc implements ComparatorObject
    {

        /**
         * Type specific comparator for the primitive types.
         * Conforms to the same convention as {@link Comparable }, returns a
         * negative number if a is less than b, a positive number if a is greater
         * than b, and 0 if the two are equal.
         *
         * @param a first item
         * @param b second item
         * @return negative if a less than b, positive if b less than a, zero
         *         if equal
         */
        @Override
        public int compare( Object a, Object b )
        {
           return a.hashCode()-b.hashCode();
        }
    }

    /**
     * Stubbed class, not very useful, if comparing Objects
     */
    public static final class ObjectDesc implements ComparatorObject
    {

        /**
         * Type specific comparator for the primitive types.
         * Conforms to the same convention as {@link Comparable }, returns a
         * negative number if a is less than b, a positive number if a is greater
         * than b, and 0 if the two are equal.
         *
         * @param a first item
         * @param b second item
         * @return negative if a less than b, positive if b less than a, zero
         *         if equal
         */
        @Override
        public int compare( Object a, Object b )
        {
            return b.hashCode()-a.hashCode();
        }
    }
}
