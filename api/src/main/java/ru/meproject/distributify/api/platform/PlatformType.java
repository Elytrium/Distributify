package ru.meproject.distributify.api.platform;

public enum PlatformType {
    PAPER("paper"),
    VELOCITY("velocity");

    private final String title;

    PlatformType(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
