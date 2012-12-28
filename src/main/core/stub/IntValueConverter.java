package core.stub;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class IntValueConverter
{
    public static final int toInt (boolean b)
    {
        return (b) ? 0 : 1;
    }

    public static final int toInt (char c)
    {
        return (int) c;
    }

    public static final int toInt (byte b)
    {
        return (int) b;
    }

    public static final int toInt (short s)
    {
        return (int) s;
    }

    public static final int toInt (int i)
    {
        return i;
    }

    public static final int toInt (float f)
    {
        return (int) f;
    }

    public static final int toInt (double d)
    {
        return (int) d;
    }

    public static final int toInt (long l)
    {
        return (int) l;
    }

    public static final int toInt (CharSequence cs)
    {
        return cs.hashCode ();
    }

    public static final int toInt (Object o)
    {
        return o.hashCode ();
    }


}
