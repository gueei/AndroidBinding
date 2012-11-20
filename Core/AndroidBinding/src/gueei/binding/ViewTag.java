package gueei.binding;

import gueei.binding.utility.TypeAsKeyHashMap;

/**
 * Strong typed collection of object tags that need to associated with a View
 * Strong type to elminiate necessary type casting in accessing classes
 * Added since View has a problem of memory leakage
 * @author andy
 *
 */
public class ViewTag extends TypeAsKeyHashMap<Object>{
}
