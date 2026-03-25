package com.moigferdsrte.windchimes.chimes.blockentity;

import com.moigferdsrte.windchimes.chimes.blockentity.abstracts.CommonChimeBlockEntity;
import com.moigferdsrte.windchimes.chimes.blockentity.abstracts.FlankChimeBlockEntity;
import com.moigferdsrte.windchimes.register.BlocksRegistry;
import com.moigferdsrte.windchimes.register.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class BambooChimeBlockEntity {
    public static class Normal extends CommonChimeBlockEntity {

        public Normal(BlockPos worldPosition, BlockState blockState) {
            super(BlocksRegistry.BlockEntityTypes.BAMBOO_CHIME_BE, worldPosition, blockState);
        }

        @Override
        public SoundEvent getQuietSound() {
            return SoundRegistry.BAMBOO_QUIET;
        }

        @Override
        public SoundEvent getLoudSound() {
            return SoundRegistry.BAMBOO_LOUD;
        }
    }

    public static class Wall extends FlankChimeBlockEntity {

        public Wall(BlockPos worldPosition, BlockState blockState) {
            super(BlocksRegistry.BlockEntityTypes.WALL_BAMBOO_CHIME_BE, worldPosition, blockState);
        }

        @Override
        public SoundEvent getQuietSound() {
            return SoundRegistry.BAMBOO_QUIET;
        }

        @Override
        public SoundEvent getLoudSound() {
            return SoundRegistry.BAMBOO_LOUD;
        }
    }
}
