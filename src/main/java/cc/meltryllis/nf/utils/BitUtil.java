package cc.meltryllis.nf.utils;

/**
 * 位运算工具。
 *
 * @author Zachary W
 * @date 2025/2/25
 */
public class BitUtil {

    public static boolean stateContainsAll(int state, int containState) {
        return andEqual(state, containState, containState);
    }

    public static boolean stateContainsAny(int state, int containState) {
        return andNotEqual(state, containState, 0);
    }

    public static boolean andEqual(int a, int b, int res) {
        return and(a, b) == res;
    }

    public static boolean andNotEqual(int a, int b, int res) {
        return !andEqual(a, b, res);
    }

    public static boolean orEqual(int a, int b, int res) {
        return or(a, b) == res;
    }

    public static boolean orNotEqual(int a, int b, int res) {
        return !orEqual(a, b, res);
    }

    public static int and(int a, int b) {
        return a & b;
    }

    public static int or(int a, int b) {
        return a | b;
    }
}
