package core.util.comparator;

import core.stub._key_;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class Comparators
{

    public static final class _KeyTypeName_Asc  implements Comparator_KeyTypeName_
    {

        @Override
        public int compare (_key_ a, _key_ b)
        {
            return 0;
        }
    }

    public static final class _KeyTypeName_Desc  implements Comparator_KeyTypeName_
    {

        @Override
        public int compare (_key_ a, _key_ b)
        {
            return 0;
        }
    }

    public static final class BoolAsc  implements ComparatorBool
    {
        @Override
        public int compare (boolean a, boolean b)
        {
            if (!a) return -1;
            if (!b) return 1;
            return 0;
        }
    }

    public static final class BoolDesc  implements ComparatorBool
    {
        @Override
        public int compare (boolean a, boolean b)
        {
            if (a) return -1;
            if (b) return 1;
            return 0;
        }
    }

    public static final class CharAsc  implements ComparatorChar
    {
        @Override
        public int compare (char a, char b)
        {
            return a-b;
        }
    }

    public static final class CharDesc  implements ComparatorChar
    {
        @Override
        public int compare (char a, char b)
        {
            return b-a;
        }
    }

    public static final class ByteAsc implements ComparatorByte
    {

        @Override
        public int compare (byte a, byte b)
        {
            return a-b;
        }
    }

    public static final class ByteDesc implements ComparatorByte
    {

        @Override
        public int compare (byte a, byte b)
        {
            return b-a;
        }
    }

    public static final class ShortAsc implements ComparatorShort
    {
        @Override
        public int compare (short a, short b)
        {
            return a-b;
        }
    }

    public static final class ShortDesc implements ComparatorShort
    {
        @Override
        public int compare (short a, short b)
        {
            return b-a;
        }
    }

    public final static class IntAsc implements ComparatorInt
    {

        @Override
        public int compare (int a, int b)
        {
            return a - b;
        }
    }

    public final static class IntDesc implements ComparatorInt
    {

        @Override
        public int compare (int a, int b)
        {
            return b - a;
        }
    }

    public final static class FloatAsc implements ComparatorFloat
    {
        @Override
        public int compare (float a, float b)
        {
            return (int) (a-b);
        }
    }

    public final static class FloatDesc implements ComparatorFloat
    {

        @Override
        public int compare (float a, float b)
        {
            return (int) (b-a);
        }
    }

    public final static class DoubleAsc implements ComparatorDouble
    {
        @Override
        public int compare (double a, double b)
        {
            return (int) (a-b);
        }
    }

    public final static class DoubleDesc implements ComparatorDouble
    {
        @Override
        public int compare (double a, double b)
        {
            return (int) (b-a);
        }
    }

    public final static class LongAsc implements ComparatorLong
    {
        @Override
        public int compare (long a, long b)
        {
            return (int) (a-b);
        }
    }

    public final static class LongDesc implements ComparatorLong
    {
        @Override
        public int compare (long a, long b)
        {
            return (int) (a-b);
        }
    }

    public final static class CharSequenceAsc implements ComparatorCharSequence
    {
        @Override
        public int compare (CharSequence a, CharSequence b)
        {
            //TODO
            return 0;
        }
    }

    public final static class CharSequenceDesc implements ComparatorCharSequence
    {
        @Override
        public int compare (CharSequence a, CharSequence b)
        {
            //TODO
            return 0;
        }
    }

}
