package core.stub;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * <p>A Provider of the default value for the primitive types. This class
 * serves the function of providing the default value for a type. This value
 * is defined by the Java standard of what an array is instantiated with.
 * </p>
 * Specifically, the default values for the types are as follows:
 * <ul>
 * <li><b>Bool</b>: <pre>false</pre>
 * </li>
 * <li><b>Char</b>: <pre>(char)0</pre>
 * </li>
 * <li><b>Byte</b>: <pre>(byte)0</pre>
 * </li>
 * <li><b>Short</b>:<pre>(short)0</pre>
 * </li>
 * <li><b>Int</b>: <pre>0</pre>
 * </li>
 * <li><b>Float</b>:<pre>(float)0</pre>
 * </li>
 * <li><b>Double</b>: <pre>0.0</pre>
 * </li>
 * <li><b>Long</b>: <pre>0</pre>
 * </li>
 * <li><b>CharSequence</b>: <pre>""</pre>
 * </li>
 * <li><b>Object</b>:<pre>null</pre>
 * </li>
 * </ul>
 */
public class DefaultValueProvider
{
    /** Stub of the class to allow for compilation of generating classes. */

    public static final Default_KeyTypeName_ defaultProvider_key_ = new
        Default_KeyTypeName_();

    public static class Default_KeyTypeName_
    {
        public static final _key_ getValue()
        {
            return null;
        }
    }

    public static class DefaultBool
    {
        public static final boolean getValue()
        {
            return false;
        }
    }

    public static class DefaultByte
    {
        public static final byte getValue()
        {
            return ( byte ) 0;
        }
    }

    public static class DefaultChar
    {
        public static final char getValue()
        {
            return ( char ) 0;
        }
    }

    public static class DefaultCharSequence
    {
        public static final CharSequence getValue()
        {
            return "";
        }
    }

    public static class DefaultDouble
    {
        public static final double getValue()
        {
            return ( double ) 0;
        }
    }

    public static class DefaultFloat
    {
        public static final float getValue()
        {
            return ( float ) 0;
        }
    }

    public static class DefaultInt
    {
        public static final int getValue()
        {
            return 0;
        }
    }

    public static class DefaultLong
    {
        public static final long getValue()
        {
            return 0;
        }
    }

    public static class DefaultObject
    {
        public static final Object getValue()
        {
            return null;
        }
    }

    public static class DefaultShort
    {
        public static final short getValue()
        {
            return ( short ) 0;
        }
    }
}
