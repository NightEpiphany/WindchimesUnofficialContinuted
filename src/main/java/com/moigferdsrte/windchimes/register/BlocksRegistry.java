package com.moigferdsrte.windchimes.register;

import com.moigferdsrte.windchimes.WindChimes;
import com.moigferdsrte.windchimes.chimes.block.BambooChimeBlock;
import com.moigferdsrte.windchimes.chimes.block.CopperChimeBlock;
import com.moigferdsrte.windchimes.chimes.block.IronChimeBlock;
import com.moigferdsrte.windchimes.chimes.blockentity.BambooChimeBlockEntity;
import com.moigferdsrte.windchimes.chimes.blockentity.CopperChimeBlockEntity;
import com.moigferdsrte.windchimes.chimes.blockentity.IronChimeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public interface BlocksRegistry {
    class Blocks {

        public static final Block IRON_CHIME = commonReg("iron_chime", IronChimeBlock::new, BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_GRAY)
                .sound(SoundType.METAL)
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
                .strength(3.5F, 6.0F));

        public static final Block COPPER_CHIME = commonReg("copper_chime", CopperChimeBlock::new, BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_ORANGE)
                .sound(SoundType.COPPER_BULB)
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
                .strength(3.2F, 5.0F));

        public static final Block BAMBOO_CHIME = commonReg("bamboo_chime", BambooChimeBlock.Normal::new, BlockBehaviour.Properties.of()
                .mapColor(net.minecraft.world.level.block.Blocks.BAMBOO.defaultMapColor())
                .sound(SoundType.BAMBOO)
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
                .strength(3.0F, 4.0F));

        public static final Block WALL_BAMBOO_CHIME = commonReg("bamboo_chime_rack", BambooChimeBlock.Wall::new, BlockBehaviour.Properties.of()
                .mapColor(net.minecraft.world.level.block.Blocks.BAMBOO.defaultMapColor())
                .sound(SoundType.BAMBOO)
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
                .strength(3.0F, 4.0F));

        public static Block register(ResourceKey<Block> resourceKey, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
            Block block = function.apply(properties.setId(resourceKey));
            return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
        }
        private static Block commonReg(String string, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
            return register(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, string)), function, properties);
        }

        public static void register() {}
    }
    class BlockEntityTypes {
        public static final BlockEntityType<IronChimeBlockEntity> IRON_CHIME_BE = FabricBlockEntityTypeBuilder.create(
                IronChimeBlockEntity::new, Blocks.IRON_CHIME
        ).build();

        public static final BlockEntityType<CopperChimeBlockEntity> COPPER_CHIME_BE = FabricBlockEntityTypeBuilder.create(
                CopperChimeBlockEntity::new, Blocks.COPPER_CHIME
        ).build();

        public static final BlockEntityType<BambooChimeBlockEntity.Normal> BAMBOO_CHIME_BE = FabricBlockEntityTypeBuilder.create(
                BambooChimeBlockEntity.Normal::new, Blocks.BAMBOO_CHIME
        ).build();

        public static final BlockEntityType<BambooChimeBlockEntity.Wall> WALL_BAMBOO_CHIME_BE = FabricBlockEntityTypeBuilder.create(
                BambooChimeBlockEntity.Wall::new, Blocks.WALL_BAMBOO_CHIME
        ).build();

        public static void register() {
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "iron_chime"), IRON_CHIME_BE);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "copper_chime"), COPPER_CHIME_BE);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "bamboo_chime"), BAMBOO_CHIME_BE);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "bamboo_chime_rack"), WALL_BAMBOO_CHIME_BE);
        }
    }
}

