package core.annotations;

import java.lang.annotation.*;
import java.lang.annotation.Target;

/**
 * Copyright 4/29/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/29/13
 *
 * This annotation designates that for purposes of speed, a method will not check the bounds of the array upon reading
 * or writing. Be wary of array bounds and array initialization when using these methods.
 *
 */
@Target( ElementType.METHOD)
public @interface UncheckedArray
{}
