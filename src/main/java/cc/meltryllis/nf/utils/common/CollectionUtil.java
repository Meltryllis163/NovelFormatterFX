package cc.meltryllis.nf.utils.common;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 集合工具。
 *
 * @author Zachary W
 * @date 2025/2/25
 */
public class CollectionUtil {

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        if (ArrayUtil.isEmpty(elements)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(elements));
    }
}
