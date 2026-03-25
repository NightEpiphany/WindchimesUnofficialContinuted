package com.moigferdsrte.windchimes.register;

import com.moigferdsrte.windchimes.WindChimes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistry {
    public static final SoundEvent IRON_LOUD = register("chime.iron.loud");
    public static final SoundEvent IRON_QUIET = register("chime.iron.quiet");
    public static final SoundEvent COPPER_LOUD = register("chime.copper.loud");
    public static final SoundEvent COPPER_QUIET = register("chime.copper.quiet");
    public static final SoundEvent BAMBOO_LOUD = register("chime.bamboo.loud");
    public static final SoundEvent BAMBOO_QUIET = register("chime.bamboo.quiet");



    //1st nest
    private static SoundEvent register(String id) {
        return register(Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, id));
    }

    //2nd nest
    private static SoundEvent register(Identifier id) {
        return register(id, id);
    }

    //3rd nest
    private static SoundEvent register(Identifier id, Identifier soundId) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId));
    }

    public static void register(){}
}
