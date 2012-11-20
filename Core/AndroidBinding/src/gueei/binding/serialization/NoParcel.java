package gueei.binding.serialization;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/**
 * Denotes that this Observable doesn't require parceling
 * @author andy
 *
 */
public @interface NoParcel {
}
