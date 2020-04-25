package com.muye.rocket.cache;

public class IEOCache {
    private static double IEO_CACHE_RELEASED = 0;
    private static double IEO_CACHE_UNRELEASED = 0;

    public static void saveIEOReleased(double released) {
        IEO_CACHE_RELEASED = released;
    }

    public static double getIEOReleased() {
        return IEO_CACHE_RELEASED;
    }

    public static void saveIEOUnReleased(double unReleased) {
        IEO_CACHE_UNRELEASED = unReleased;
    }

    public static double getIEOUnReleased() {
        return IEO_CACHE_UNRELEASED;
    }
}
