package core.array;

import core.Types;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public class ArrayGrowthException extends RuntimeException
{
    private final static StringBuilder builder = new StringBuilder( 1024 );

    /**
     *
     * @param aClass class requesting growth
     * @param originalSize original array size
     * @param minAcceptableSize minimum size that array must grow to
     * @param type type of array (int, byte, etc...)
     */
    public ArrayGrowthException( Class aClass, int originalSize, int minAcceptableSize,
                                 byte type )
    {
        throw new RuntimeException( builder.append( "Class [" ).append(
                aClass.getSimpleName() ).
                append( "] cannot grow array of type [" ).
                append( Types.getName( type ) ).append( "] from size  [" ).
                append( originalSize ).append( "] to a minimum size of [" ).
                append( minAcceptableSize ).append( "]" ).toString() );
    }
}
