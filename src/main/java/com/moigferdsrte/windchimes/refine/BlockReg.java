package com.moigferdsrte.windchimes.refine;

import com.moigferdsrte.windchimes.refine.bamboo.BambooChimeBlock;
import com.moigferdsrte.windchimes.refine.bamboo.BambooWallChimeBlock;
import com.moigferdsrte.windchimes.refine.copper.CopperChimeBlock;
import com.moigferdsrte.windchimes.refine.dreamcatcher.DreamcatcherBlock;
import com.moigferdsrte.windchimes.refine.iron.IronChimeBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class BlockReg {

    public static final Block BAMBOO = register(
            "bamboo_chime",
            BambooChimeBlock::new,
            AbstractBlock.Settings.create().mapColor(Blocks.BAMBOO.getDefaultMapColor()).solid().strength(3.0F).sounds(BlockSoundGroup.BAMBOO).pistonBehavior(PistonBehavior.DESTROY).breakInstantly().nonOpaque()
    );

    public static final Block IRON = register(
            "iron_chime",
            IronChimeBlock::new,
            AbstractBlock.Settings.create().mapColor(Blocks.IRON_BLOCK.getDefaultMapColor()).solid().strength(3.5F).sounds(BlockSoundGroup.METAL).pistonBehavior(PistonBehavior.DESTROY).breakInstantly().nonOpaque()
    );

    public static final Block COPPER = register(
            "copper_chime",
            CopperChimeBlock::new,
            AbstractBlock.Settings.create().mapColor(Blocks.COPPER_BLOCK.getDefaultMapColor()).solid().strength(3.2F).sounds(BlockSoundGroup.COPPER).pistonBehavior(PistonBehavior.DESTROY).breakInstantly().nonOpaque()
    );

    public static final Block WALL_BAMBOO = register(
            "bamboo_chime_rack",
            BambooWallChimeBlock::new,
            AbstractBlock.Settings.create().mapColor(Blocks.BAMBOO.getDefaultMapColor()).solid().strength(3.0F).sounds(BlockSoundGroup.BAMBOO).pistonBehavior(PistonBehavior.DESTROY).breakInstantly().nonOpaque()
    );

    public static final Block DREAM = register(
            "dreamcatcher",
            DreamcatcherBlock::new,
            AbstractBlock.Settings.create().mapColor(Blocks.WHITE_CANDLE.getDefaultMapColor()).solid().strength(3.2F).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY).breakInstantly().nonOpaque()
    );


    //1st nest
    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return register(keyOf(id), factory, settings);
    }

    //2nd nest
    public static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = (Block)factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Refine.MOD_ID, id));
    }

    public static void init(){}
}
