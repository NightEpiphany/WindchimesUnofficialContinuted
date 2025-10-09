package com.moigferdsrte.windchimes.refine.utils;

import net.minecraft.util.StringIdentifiable;

public enum WoodType implements StringIdentifiable {
    OAK("oak"),
    SPRUCE("spruce"),
    BIRCH("birch"),
    JUNGLE("jungle"),
    ACACIA("acacia"),
    DARK_OAK("dark_oak"),
    PALE_OAK("pale_oak"),
    CHERRY("cherry"),
    NONE("empty");

    private final String name;

    WoodType(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
