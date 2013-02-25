package core.util.comparator;

import core.stub.*;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 */
public interface Comparator_KeyTypeName_ extends Comparator
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
     * if equal
     */
    public int compare(_key_ a,_key_ b);
}