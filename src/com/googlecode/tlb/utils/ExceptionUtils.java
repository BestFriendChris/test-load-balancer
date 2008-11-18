package com.googlecode.tlb.utils;

/**
 *
 */
public final class ExceptionUtils {
    private ExceptionUtils() {

    }

    public static RuntimeException bomb(String msg) {
        throw new RuntimeException(msg);
    }

    public static RuntimeException bomb() {
        throw new RuntimeException();
    }

    public static RuntimeException bomb(Throwable t) {
        throw new RuntimeException(t.getMessage(), t);
    }

    public static RuntimeException bomb(String msg, Throwable t) {
        throw new RuntimeException(msg, t);
    }

    public static void bombIfNull(Object o, String msg) {
        bombIf(o == null, msg);
    }


    public static void bombIfFailedToRunCommandLine(int returnValue, String msg) throws Exception {
        if (returnValue != 0) {
            throw new Exception(msg);
        }
    }

    public static void bombIfNotNull(Object o, String msg) {
        bombIf(o != null, msg);
    }

    public static void bombIf(boolean check, String msg) {
        if (check) {
            throw bomb(msg);
        }
    }

    public static void bombUnless(boolean check, String msg) {
        bombIf(!check, msg);
    }

    public static RuntimeException methodNotImplemented() {
        throw bomb("Not yet implemented");
    }

}
