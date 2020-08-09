package com.klakier.proRobIntranet;

import java.util.ArrayList;
import java.util.Collection;

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

    public static Collection<String> generateStringIntRange(int min, int max) {
        ArrayList<String> ret = new ArrayList<>();
        for (Integer i = min; i <= max; i++) {
            ret.add(i.toString());
        }
        return ret;
    }

}
