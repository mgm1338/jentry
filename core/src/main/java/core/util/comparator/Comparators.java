package core.util.comparator;

import core.stub.IntValueConverter;
import core.stub._key_;
import core.stub._val_;

import java.util.*;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class Comparators
{

    //stub
    public static final Comparator_KeyTypeName_ _key_Asc = new _KeyTypeName_Asc();
    public static final Comparator_KeyTypeName_ _key_Desc = new _KeyTypeName_Desc();
    public static final Comparator_ValueTypeName_ _val_Asc = new _ValueTypeName_Asc();
    public static final Comparator_ValueTypeName_ _val_Desc = new _ValueTypeName_Desc();



    public static final ComparatorByte byteAsc = new ByteAsc();
    public static final ComparatorChar charAsc = new CharAsc();
    public static final ComparatorShort shortAsc = new ShortAsc();
    public static final ComparatorInt intAsc = new IntAsc();
    public static final ComparatorFloat floatAsc = new FloatAsc();
    public static final ComparatorDouble doubleAsc = new DoubleAsc();
    public static final ComparatorLong longAsc = new LongAsc();
    public static final ComparatorCharSequence CharSequenceAsc = new CharSequenceAsc();
    public static final ComparatorObject ObjectAsc = new ObjectAsc();

    public static final ComparatorByte byteDesc = new ByteDesc();
    public static final ComparatorChar charDesc = new CharDesc();
    public static final ComparatorShort shortDesc = new ShortDesc();
    public static final ComparatorInt intDesc = new IntDesc();
    public static final ComparatorFloat floatDesc = new FloatDesc();
    public static final ComparatorDouble doubleDesc = new DoubleDesc();
    public static final ComparatorLong longDesc = new LongDesc();
    public static final ComparatorCharSequence CharSequenceDesc = new CharSequenceDesc();
    public static final ComparatorObject ObjectDesc = new ObjectDesc();




    /**
     * Descending Comparators
     */

    //stub, never used
    public static final class _ValueTypeName_Desc
            implements Comparator_ValueTypeName_
    {

        @Override
        public int compare( _val_ a, _val_ b )
        {
            return 0;
        }

    }

    //stub, never used
    public static final class _KeyTypeName_Desc
            implements Comparator_KeyTypeName_
    {

        @Override
        public int compare( _key_ a, _key_ b )
        {
            return 0;
        }
    }

    public static final class CharDesc implements ComparatorChar
    {
        @Override
        public int compare( char a, char b )
        {
            return b - a;
        }
    }

    public static final class ByteDesc implements ComparatorByte
    {

        @Override
        public int compare( byte a, byte b )
        {
            return b - a;
        }
    }

    public static final class ShortDesc implements ComparatorShort
    {
        @Override
        public int compare( short a, short b )
        {
            return b - a;
        }
    }


    public final static class IntDesc implements ComparatorInt
    {

        @Override
        public int compare( int a, int b )
        {
            return b - a;
        }
    }

    public final static class FloatDesc implements ComparatorFloat
    {

        @Override
        public int compare( float a, float b )
        {
            return ( int ) ( b - a );
        }
    }

    public final static class DoubleDesc implements ComparatorDouble
    {
        @Override
        public int compare( double a, double b )
        {
            return ( int ) ( b - a );
        }
    }


    public final static class LongDesc implements ComparatorLong
    {
        @Override
        public int compare( long a, long b )
        {
            return ( int ) ( b - a );
        }
    }

    public final static class CharSequenceDesc implements ComparatorCharSequence,
                                                         java.util.Comparator<CharSequence>
    {
        @Override
        public int compare( CharSequence a, CharSequence b )
        {
            if( a == null && b == null )
            {
                return 0;
            }
            if( a == null )
            {
                return 1;
            }
            if( b == null )
            {
                return -1;
            }
            CharDesc charCmp = new CharDesc();
            int aLen = a.length();
            int bLen = b.length();
            for( int i = 0; i < a.length() && i < b.length(); i++ )
            {
                int cmp;
                if( ( cmp = charCmp.compare( a.charAt( i ), b.charAt( i ) ) ) != 0 )
                {
                    return cmp;
                }
            }
            if( aLen < bLen )
            {
                return 1;
            }
            else if( bLen < aLen )
            {
                return -1;
            }
            return 0;
        }
    }

    /**
     * Ascending Comparators
     */
    //stub, never used
    public static final class _KeyTypeName_Asc
            implements Comparator_KeyTypeName_
    {

        @Override
        public int compare( _key_ a, _key_ b )
        {
            return 0;
        }

    }

    //stub, never used
    public static final class _ValueTypeName_Asc
            implements Comparator_ValueTypeName_
    {

        @Override
        public int compare( _val_ a, _val_ b )
        {
            return 0;
        }

    }



    public static final class CharAsc implements ComparatorChar
    {
        @Override
        public int compare( char a, char b )
        {
            return a - b;
        }
    }


    public static final class ByteAsc implements ComparatorByte
    {

        @Override
        public int compare( byte a, byte b )
        {
            return a - b;
        }
    }


    public static final class ShortAsc implements ComparatorShort
    {
        @Override
        public int compare( short a, short b )
        {
            return a - b;
        }
    }


    public final static class IntAsc implements ComparatorInt
    {

        @Override
        public int compare( int a, int b )
        {
            return a - b;
        }
    }


    public final static class FloatAsc implements ComparatorFloat
    {
        @Override
        public int compare( float a, float b )
        {
            return ( int ) ( a - b );
        }
    }


    public final static class DoubleAsc implements ComparatorDouble
    {
        @Override
        public int compare( double a, double b )
        {
            return ( int ) ( a - b );
        }
    }

    public final static class LongAsc implements ComparatorLong
    {
        @Override
        public int compare( long a, long b )
        {
            return ( int ) ( a - b );
        }
    }


    public final static class CharSequenceAsc implements ComparatorCharSequence
            , java.util.Comparator<CharSequence>
    {
        @Override
        public int compare( CharSequence a, CharSequence b )
        {
            if( a == null && b == null )
            {
                return 0;
            }
            if( a == null )
            {
                return -1;
            }
            if( b == null )
            {
                return 1;
            }
            CharAsc charCmp = new CharAsc();
            int aLen = a.length();
            int bLen = b.length();
            for( int i = 0; i < a.length() && i < b.length(); i++ )
            {
                int cmp;
                if( ( cmp = charCmp.compare( a.charAt( i ), b.charAt( i ) ) ) != 0 )
                {
                    return cmp;
                }
            }
            if( aLen < bLen )
            {
                return -1;
            }
            else if( bLen < aLen )
            {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Object comparators, probably better to stick with java.util.Comparator.
     */
    public final static class ObjectAsc implements ComparatorObject
    {

        @Override
        public int compare( Object a, Object b )
        {
            return IntValueConverter.toInt( a ) - IntValueConverter.toInt( b );
        }
    }

    public final static class ObjectDesc implements ComparatorObject
    {

        @Override
        public int compare( Object a, Object b )
        {
            return IntValueConverter.toInt( b ) - IntValueConverter.toInt( a );
        }
    }



}
