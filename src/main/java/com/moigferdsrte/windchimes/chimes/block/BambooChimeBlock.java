package com.moigferdsrte.windchimes.chimes.block;

import com.moigferdsrte.windchimes.chimes.block.abstracts.CommonChimeBlock;
import com.moigferdsrte.windchimes.chimes.block.abstracts.FlankChimeBlock;
import com.moigferdsrte.windchimes.chimes.blockentity.BambooChimeBlockEntity;
import com.moigferdsrte.windchimes.register.BlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BambooChimeBlock {
    public static class Normal extends CommonChimeBlock<BambooChimeBlockEntity.Normal> {

        public Normal(Properties properties) {
            super(properties);
        }

        @Override
        public BlockEntityType<BambooChimeBlockEntity.Normal> getBlockEntityType() {
            return BlocksRegistry.BlockEntityTypes.BAMBOO_CHIME_BE;
        }

        @Override
        public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
            return new BambooChimeBlockEntity.Normal(worldPosition, blockState);
        }
    }

    public static class Wall extends FlankChimeBlock<BambooChimeBlockEntity.Wall> {

        public Wall(Properties properties) {
            super(properties);
        }

        @Override
        public BlockEntityType<BambooChimeBlockEntity.Wall> getBlockEntityType() {
            return BlocksRegistry.BlockEntityTypes.WALL_BAMBOO_CHIME_BE;
        }

        @Override
        public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
            return new BambooChimeBlockEntity.Wall(worldPosition, blockState);
        }
    }
}
