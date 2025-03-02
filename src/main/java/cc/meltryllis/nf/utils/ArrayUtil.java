package cc.meltryllis.nf.utils;

/**
 * 数组工具。
 *
 * @author Zachary W
 * @date 2025/2/25
 */
public class ArrayUtil {

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static <E> boolean isEmpty(E[] array) {
        return array == null || array.length == 0;
    }

    public static int indexOf(char[] array, char value) {
        if (ArrayUtil.isEmpty(array)) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
