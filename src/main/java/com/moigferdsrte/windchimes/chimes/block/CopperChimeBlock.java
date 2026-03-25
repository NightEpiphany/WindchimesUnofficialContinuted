package com.moigferdsrte.windchimes.chimes.block;

import com.moigferdsrte.windchimes.chimes.block.abstracts.CommonChimeBlock;
import com.moigferdsrte.windchimes.chimes.blockentity.CopperChimeBlockEntity;
import com.moigferdsrte.windchimes.register.BlocksRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CopperChimeBlock extends CommonChimeBlock<CopperChimeBlockEntity> {
    public CopperChimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<CopperChimeBlockEntity> getBlockEntityType() {
        return BlocksRegistry.BlockEntityTypes.COPPER_CHIME_BE;
    }

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CopperChimeBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new CopperChimeBlockEntity(worldPosition, blockState);
    }
}
