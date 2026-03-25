package com.moigferdsrte.windchimes.chimes.block;

import com.moigferdsrte.windchimes.chimes.block.abstracts.CommonChimeBlock;
import com.moigferdsrte.windchimes.chimes.blockentity.IronChimeBlockEntity;
import com.moigferdsrte.windchimes.register.BlocksRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class IronChimeBlock extends CommonChimeBlock<IronChimeBlockEntity> {
    public IronChimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<IronChimeBlockEntity> getBlockEntityType() {
        return BlocksRegistry.BlockEntityTypes.IRON_CHIME_BE;
    }


    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(IronChimeBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new IronChimeBlockEntity(worldPosition, blockState);
    }
}
