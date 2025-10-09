package com.moigferdsrte.windchimes.refine;

import com.moigferdsrte.windchimes.refine.bamboo.BambooChimeBlockEntity;
import com.moigferdsrte.windchimes.refine.bamboo.BambooWallChimeBlockEntity;
import com.moigferdsrte.windchimes.refine.copper.CopperChimeBlockEntity;
import com.moigferdsrte.windchimes.refine.dreamcatcher.DreamcatcherBlockEntity;
import com.moigferdsrte.windchimes.refine.iron.IronChimeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityReg {

    public static final BlockEntityType<BambooChimeBlockEntity> BAMBOO = register("bamboo_chime",
            FabricBlockEntityTypeBuilder.create(BambooChimeBlockEntity::new, BlockReg.BAMBOO).build()
            );

    public static final BlockEntityType<BambooWallChimeBlockEntity> WALL_BAMBOO = register("wall_bamboo_chime",
            FabricBlockEntityTypeBuilder.create(BambooWallChimeBlockEntity::new, BlockReg.WALL_BAMBOO).build()
    );

    public static final BlockEntityType<CopperChimeBlockEntity> COPPER = register("copper_chime",
            FabricBlockEntityTypeBuilder.create(CopperChimeBlockEntity::new, BlockReg.COPPER).build()
    );

    public static final BlockEntityType<IronChimeBlockEntity> IRON = register("iron_chime",
            FabricBlockEntityTypeBuilder.create(IronChimeBlockEntity::new, BlockReg.IRON).build()
    );

    public static final BlockEntityType<DreamcatcherBlockEntity> DREAM = register("dreamcatcher",
            FabricBlockEntityTypeBuilder.create(DreamcatcherBlockEntity::new, BlockReg.DREAM).build()
    );

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Refine.MOD_ID, path), blockEntityType);
    }

    public static void init(){}
}
