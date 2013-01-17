package collections.hash;

import collections.hash.Hasher;
import core.Const;
import core.stub.IntValueConverter;
import core.stub._key_;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class HashFunctions
{

    public static final HashFunctionBool hashFunctionBool =
            new HashFunctionBool ();
    public static final HashFunctionChar hashFunctionChar =
            new HashFunctionChar ();
    public static final HashFunctionByte hashFunctionByte =
            new HashFunctionByte ();
    public static final HashFunctionShort hashFunctionShort =
            new HashFunctionShort ();
    public static final HashFunctionInt hashFunctionInt =
            new HashFunctionInt ();
    public static final HashFunctionFloat hashFunctionFloat =
            new HashFunctionFloat ();
    public static final HashFunctionLong hashFunctionLong =
            new HashFunctionLong ();
    public static final HashFunctionDouble hashFunctionDouble =
            new HashFunctionDouble ();
    public static final HashFunctionCharSequence hashFunctionCharSequence =
            new HashFunctionCharSequence ();
    public static final HashFunctionObject hashFunctionObject =
            new HashFunctionObject ();

    public static final HashFunction_KeyTypeName_ hashFunction_KeyTypeName_ =
            new HashFunction_KeyTypeName_ ();


    public static class HashFunction_KeyTypeName_
    {
        public int getHashCode (_key_ k)
        {
            return Const.NO_ENTRY;
        }
    }

    public static class HashFunctionBool
    {
        public int getHashCode (boolean b)
        {
            return Hasher.getHashCode (IntValueConverter.toInt (b));
        }
    }

    public static class HashFunctionChar
    {
        public int getHashCode (char c)
        {
            return Hasher.getHashCode (IntValueConverter.toInt (c));
        }
    }

    public static class HashFunctionByte
    {
        public int getHashCode (byte b)
        {
            return Hasher.getHashCode (IntValueConverter.toInt (b));
        }
    }

    public static class HashFunctionShort
    {
        public int getHashCode (short s)
        {
            return Hasher.getHashCode (IntValueConverter.toInt (s));
        }
    }

    public static class HashFunctionInt
    {
        public int getHashCode (int i)
        {
            return Hasher.getHashCode (i);
        }
    }

    public static class HashFunctionFloat
    {
        public int getHashCode (float f)
        {
            return Hasher.getHashCode (IntValueConverter.toInt (f));
        }
    }

    public static class HashFunctionDouble
    {
        public int getHashCode (double d)
        {
            return Hasher.getHashCode (IntValueConverter.toInt (d));
        }
    }

    public static class HashFunctionLong
    {
        public int getHashCode (long l)
        {
            return Hasher.getHashCode (IntValueConverter.toInt (l));
        }
    }

    public static class HashFunctionCharSequence
    {
        public int getHashCode (CharSequence cs)
        {
            return cs.hashCode ();
        }
    }

    public static class HashFunctionObject
    {
        public int getHashCode (Object o)
        {
            return o.hashCode ();
        }
    }

}
