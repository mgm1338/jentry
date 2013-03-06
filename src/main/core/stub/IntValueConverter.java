package core.stub;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 * <p/>
 * Converter normally for test purposes that can convert all primitive types into an int, or an int
 * into a representation of a different type of primitive.
 */
public class IntValueConverter
{
    public static final int toInt( boolean b )
    {
        return ( b ) ? 0 : 1;
    }

    public static final int toInt( char c )
    {
        return ( int ) c;
    }

    public static final int toInt( byte b )
    {
        return ( int ) b;
    }

    public static final int toInt( short s )
    {
        return ( int ) s;
    }

    public static final int toInt( int i )
    {
        return i;
    }

    public static final int toInt( float f )
    {
        return ( int ) f;
    }

    public static final int toInt( double d )
    {
        return ( int ) d;
    }

    public static final int toInt( long l )
    {
        return ( int ) l;
    }

    public static final int toInt( CharSequence cs )
    {
        return cs.hashCode();
    }

    public static final int toInt( Object o )
    {
        return o.hashCode();
    }

    //this pattern, auto-generated, will form the methods below it
    public static final _key_ _key_FromInt( int i )
    {
        return null;
    }

    public static final _val_ _val_FromInt( int i )
    {
        return null;
    }


    public static final boolean booleanFromInt( int i )
    {
        return i % 2 == 0;
    }

    public static final char charFromInt( int i )
    {
        return ( char ) i;
    }

    public static final byte byteFromInt( int i )
    {
        return ( byte ) i;
    }

    public static final short shortFromInt( int i )
    {
        return ( short ) i;
    }

    public static final int intFromInt( int i )
    {
        return i;
    }

    public static final float floatFromInt( int i )
    {
        return ( float ) i;
    }

    public static final double doubleFromInt( int i )
    {
        return ( double ) i;
    }

    public static final long longFromInt( int i )
    {
        return ( long ) i;
    }

    public static final CharSequence CharSequenceFromInt( int i )
    {
        return Integer.toString( i );
    }

    public static final Object ObjectFromInt( int i )
    {
        return new Integer( i );
    }

}
