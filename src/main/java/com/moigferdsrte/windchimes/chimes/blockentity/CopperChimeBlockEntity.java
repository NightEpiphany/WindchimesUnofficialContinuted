package com.moigferdsrte.windchimes.chimes.blockentity;

import com.moigferdsrte.windchimes.chimes.blockentity.abstracts.CommonChimeBlockEntity;
import com.moigferdsrte.windchimes.register.BlocksRegistry;
import com.moigferdsrte.windchimes.register.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class CopperChimeBlockEntity extends CommonChimeBlockEntity {
    public CopperChimeBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlocksRegistry.BlockEntityTypes.COPPER_CHIME_BE, worldPosition, blockState);
    }

    @Override
    public SoundEvent getQuietSound() {
        return SoundRegistry.COPPER_QUIET;
    }

    @Override
    public SoundEvent getLoudSound() {
        return SoundRegistry.COPPER_LOUD;
    }
}
