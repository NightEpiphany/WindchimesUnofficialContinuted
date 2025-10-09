package com.moigferdsrte.windchimes.refine;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundReg {

    public static final SoundEvent IRON_LOUD = register("chime.iron.loud");
    public static final SoundEvent IRON_QUIET = register("chime.iron.quiet");
    public static final SoundEvent COPPER_LOUD = register("chime.copper.loud");
    public static final SoundEvent COPPER_QUIET = register("chime.copper.quiet");
    public static final SoundEvent BAMBOO_LOUD = register("chime.bamboo.loud");
    public static final SoundEvent BAMBOO_QUIET = register("chime.bamboo.quiet");



    //1st nest
    private static SoundEvent register(String id) {
        return register(Identifier.of(Refine.MOD_ID, id));
    }

    //2nd nest
    private static SoundEvent register(Identifier id) {
        return register(id, id);
    }

    //3rd nest
    private static SoundEvent register(Identifier id, Identifier soundId) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(soundId));
    }

    public static void init(){}
}
