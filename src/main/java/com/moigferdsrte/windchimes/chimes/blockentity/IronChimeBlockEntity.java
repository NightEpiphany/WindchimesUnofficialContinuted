package com.moigferdsrte.windchimes.chimes.blockentity;

import com.moigferdsrte.windchimes.chimes.blockentity.abstracts.CommonChimeBlockEntity;
import com.moigferdsrte.windchimes.register.BlocksRegistry;
import com.moigferdsrte.windchimes.register.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class IronChimeBlockEntity extends CommonChimeBlockEntity {
    public IronChimeBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlocksRegistry.BlockEntityTypes.IRON_CHIME_BE, worldPosition, blockState);
    }

    @Override
    public SoundEvent getQuietSound() {
        return SoundRegistry.IRON_QUIET;
    }

    @Override
    public SoundEvent getLoudSound() {
        return SoundRegistry.IRON_LOUD;
    }
}
