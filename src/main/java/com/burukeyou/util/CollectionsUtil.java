package com.burukeyou.util;

import java.util.Collection;

/**
 * @author burukeyou
 */
public class CollectionsUtil {

    private CollectionsUtil(){}

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }

}
