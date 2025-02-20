package xyz.lp.common;

import java.util.List;

public class CollUtil {
    public static <E> boolean isEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }
}
