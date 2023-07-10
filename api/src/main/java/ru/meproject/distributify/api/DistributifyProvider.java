package ru.meproject.distributify.api;

public class DistributifyProvider {
    private static DistributifyFactory<?> instance = null;

    public static DistributifyFactory<?> get() {
        // TODO: add error logging
        return instance != null ? instance : null;
    }

    public static void register(DistributifyFactory<?> factory) {
        instance = factory;
    }

    public static void unregister() {
        instance = null;
    }

}
