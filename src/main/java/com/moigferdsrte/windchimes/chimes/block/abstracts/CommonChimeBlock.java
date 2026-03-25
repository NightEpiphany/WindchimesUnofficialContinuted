package com.moigferdsrte.windchimes.chimes.block.abstracts;

import com.moigferdsrte.windchimes.chimes.blockentity.abstracts.CommonChimeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class CommonChimeBlock<E extends CommonChimeBlockEntity> extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Block.box(4.0, 8.0, 4.0, 12.0, 16.0, 12.0);
    public CommonChimeBlock(Properties properties) {
        super(properties);
    }

    public abstract BlockEntityType<E> getBlockEntityType();

    @Override
    protected boolean triggerEvent(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, int type, int data) {
        if (level.getBlockEntity(pos) instanceof BlockEntity entity) {
            return entity.triggerEvent(type, data);
        } else {
            return false;
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        return createTickerHelper(type, getBlockEntityType(), CommonChimeBlockEntity::tick);
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state, @NonNull LevelReader level, @NonNull BlockPos pos) {
        return Block.canSupportCenter(level, pos.relative(Direction.UP), Direction.DOWN);
    }

    @Override
    protected void onProjectileHit(@NonNull Level level, @NonNull BlockState state, @NonNull BlockHitResult blockHit, @NonNull Projectile projectile) {
        if (level.getBlockEntity(blockHit.getBlockPos()) instanceof CommonChimeBlockEntity commonChimeBlockEntity) {
            commonChimeBlockEntity.ring(true);
            commonChimeBlockEntity.ticksToNextRing += 4;
            if (level instanceof ServerLevel serverWorld) {
                if (projectile instanceof Arrow) {
                    projectile.spawnAtLocation(serverWorld, Items.ARROW.getDefaultInstance());
                    projectile.kill(serverWorld);
                }
            }
        }
        super.onProjectileHit(level, state, blockHit, projectile);
    }

    @Override
    protected @NonNull BlockState updateShape(
            @NonNull BlockState state,
            @NonNull LevelReader level,
            @NonNull ScheduledTickAccess ticks,
            @NonNull BlockPos pos,
            @NonNull Direction directionToNeighbour,
            @NonNull BlockPos neighbourPos,
            @NonNull BlockState neighbourState,
            @NonNull RandomSource random
    ) {
        if (level.isEmptyBlock(pos.above()) && level.getBlockEntity(pos) instanceof CommonChimeBlockEntity commonChimeBlockEntity) {
            commonChimeBlockEntity.setRemoved();
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CommonChimeBlockEntity commonChimeBlockEntity) {
            commonChimeBlockEntity.ring(!player.isShiftKeyDown());
            commonChimeBlockEntity.ticksToNextRing += 4;
            return InteractionResult.SUCCESS_SERVER;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SHAPE;
    }
}
