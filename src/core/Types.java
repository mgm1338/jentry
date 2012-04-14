package core;

import core.util.comparator.CharSequenceCmp;

/**
 * millemax
 * Date: 3/24/12
 * Time: 2:34 PM
 */
public class Types
{
    public static final byte Bool = 1;
    public static final byte Char = 2;
    public static final byte Byte = 3;
    public static final byte Short = 4;
    public static final byte Int = 5;
    public static final byte Float = 6;
    public static final byte Double = 7;
    public static final byte Long = 8;
    public static final byte CharSequence = 9;
    public static final byte Object = 10;
    public static final byte Unknown = 11;


    public static byte getType( CharSequence name )
    {
        if( CharSequenceCmp.equals( "Bool", name, false ) ) return Types.Bool;
        if( CharSequenceCmp.equals( "Char", name, false ) ) return Types.Char;
        if( CharSequenceCmp.equals( "Byte", name, false ) ) return Types.Byte;
        if( CharSequenceCmp.equals( "Short", name, false ) ) return Types.Short;
        if( CharSequenceCmp.equals( "Int", name, false ) ) return Types.Int;
        if( CharSequenceCmp.equals( "Float", name, false ) ) return Types.Float;
        if( CharSequenceCmp.equals( "Double", name, false ) )
            return Types.Double;
        if( CharSequenceCmp.equals( "Long", name, false ) ) return Types.Long;
        if( CharSequenceCmp.equals( "CharSequence", name, false ) )
            return Types.CharSequence;
        if( CharSequenceCmp.equals( "Object", name, false ) )
            return Types.Object;
        return Types.Unknown;
    }

    public static CharSequence getName( byte type )
    {
        switch( type )
        {
            case Types.Bool:
                return "Boolean";
            case Types.Char:
                return "Char";
            case Types.Byte:
                return "Byte";
            case Types.Short:
                return "Short";
            case Types.Int:
                return "Int";
            case Types.Float:
                return "Float";
            case Types.Double:
                return "Double";
            case Types.Long:
                return "Long";
            case Types.CharSequence:
                return "CharSequence";
            case Types.Object:
                return "Object";
            default:
                throw new IllegalArgumentException( "Not a valid type" );
        }
    }

}
