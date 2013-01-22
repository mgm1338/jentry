package core.util.comparator;

import core.Const;
import core.stub._key_;

/**
 * Copyright 1/21/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 1/21/13
 *
 * A set of utility functions that will compare the equality of the primitive types, and delegate the
 * equality comparison of CharSequence and Object to their own equality functions. Necessary to standardize
 * the types when auto-generating classes.
 */
public class EqualityFunctions
{

    public static final class Equals_KeyTypeName_
    {
        public boolean equals(_key_ a, _key_ b)
        {
            return false;
        }
    }

    public static final class EqualsBool
    {
        public static final boolean equals(boolean a, boolean b)
        {
            return a==b;
        }
    }

    public static final class EqualsChar
    {
        public static final boolean equals(char a, char b)
        {
            return a==b;
        }
    }

    public static final class EqualsByte
    {
        public static final boolean equals(byte a, byte b)
        {
            return a==b;
        }
    }

    public static final class EqualsShort
    {
        public static final boolean equals(short a, short b)
        {
            return a==b;
        }
    }

    public static final class EqualsInt
    {
        public static final boolean equals(int a, int b)
        {
            return a==b;
        }
    }

    public static final class EqualsFloat
    {
        public static final boolean equals(float a, float b)
        {
            return a==b;
        }
    }

    public static final class EqualsLong
    {
        public static final boolean equals(long a, long b)
        {
            return a==b;
        }
    }

    public static final class EqualsDouble
    {
        public static final boolean equals(double a, double b)
        {
            return a==b;
        }
    }

    public static final class EqualsCharSequence
    {
        public static final boolean equals(CharSequence a, CharSequence b)
        {
            return a.equals( b );
        }
    }
    public static final class EqualsObject
    {
        public static final boolean equals(Object a, Object b)
        {
            return a.equals( b );
        }
    }



}
