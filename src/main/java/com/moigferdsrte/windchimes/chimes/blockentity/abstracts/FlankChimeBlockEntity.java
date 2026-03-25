package com.moigferdsrte.windchimes.chimes.blockentity.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FlankChimeBlockEntity extends CommonChimeBlockEntity {
    public FlankChimeBlockEntity(final BlockEntityType<?> type, final BlockPos worldPosition, final BlockState blockState) {
        super(type, worldPosition, blockState);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, FlankChimeBlockEntity flankChimeBlockEntity) {
        CommonChimeBlockEntity.tick(level, pos, state, flankChimeBlockEntity);
    }
}
