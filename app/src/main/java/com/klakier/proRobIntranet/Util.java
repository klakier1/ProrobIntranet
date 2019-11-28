package com.klakier.proRobIntranet;

public class Util {
    /**
     * Null safe comparison of two objects.
     *
     * @return true if the objects are identical.
     */
    public static boolean objectEquals(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        if (o1 == null) return false;
        return o1.equals(o2);
    }
}
