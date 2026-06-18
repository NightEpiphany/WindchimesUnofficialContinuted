package com.moigferdsrte.windchimes.chimes.block.abstracts;

import com.moigferdsrte.windchimes.chimes.blockentity.abstracts.FlankChimeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class FlankChimeBlock<E extends FlankChimeBlockEntity> extends BaseEntityBlock {
    protected FlankChimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(final BlockState state, final @NonNull LevelReader level, final @NonNull BlockPos pos) {
        return canAttach(level, pos, state.getValue(FACING).getOpposite());
    }

    public static boolean canAttach(final LevelReader level, final BlockPos pos, final Direction direction) {
        BlockPos relative = pos.relative(direction);
        return level.getBlockState(relative).isFaceSturdy(level, relative, direction.getOpposite());
    }

    public abstract BlockEntityType<E> getBlockEntityType();

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SOUTH = Shapes.or(
            Shapes.create(0.3125, 0, 0, 0.6875, 0.25, 0.125),
            Shapes.create(0.4375, 0.0625, 0.125, 0.5625, 0.1875, 0.75)
    );

    private static final VoxelShape NORTH = Shapes.or(
            Shapes.create(0.3125, 0, 0.875, 0.6875, 0.25, 1),
            Shapes.create(0.4375, 0.0625, 0.25, 0.5625, 0.1875, 0.875)
    );

    private static final VoxelShape WEST = Shapes.or(
            Shapes.create(0.875, 0, 0.3125, 1, 0.25, 0.6875),
            Shapes.create(0.25, 0.0625, 0.4375, 0.875, 0.1875, 0.5625)
    );

    private static final VoxelShape EAST = Shapes.or(
            Shapes.create(0, 0, 0.3125, 0.125, 0.25, 0.6875),
            Shapes.create(0.125, 0.0625, 0.4375, 0.75, 0.1875, 0.5625)
    );

    private static final VoxelShape FLOAT = Shapes.or(
            Shapes.create(0.3125, 0.125, 0.375, 0.6875, 0.25, 0.625)
    );

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    protected @NonNull BlockState rotate(@NonNull BlockState state, @NonNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NonNull BlockState mirror(@NonNull BlockState state, @NonNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
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
        if (directionToNeighbour == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            if (level.getBlockEntity(pos) instanceof FlankChimeBlockEntity flankChimeBlockEntity) {
                flankChimeBlockEntity.setRemoved();
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof FlankChimeBlockEntity flankChimeBlockEntity) {
            flankChimeBlockEntity.ring(!player.isShiftKeyDown());
            flankChimeBlockEntity.ticksToNextRing += 4;
            return InteractionResult.SUCCESS_SERVER;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void onProjectileHit(@NonNull Level level, @NonNull BlockState state, @NonNull BlockHitResult blockHit, @NonNull Projectile projectile) {
        if (level.getBlockEntity(blockHit.getBlockPos()) instanceof FlankChimeBlockEntity flankChimeBlockEntity) {
            flankChimeBlockEntity.ring(true);
            flankChimeBlockEntity.ticksToNextRing += 4;
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
    protected boolean triggerEvent(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, int type, int data) {
        if (level.getBlockEntity(pos) instanceof BlockEntity entity) {
            return entity.triggerEvent(type, data);
        } else {
            return false;
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext ctx) {
        Direction clickedFace = ctx.getClickedFace();
        if (!clickedFace.getAxis().isHorizontal()) {
            return null;
        }

        Level level = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();

        BlockState blockState = this.defaultBlockState().setValue(FACING, clickedFace);
        return blockState.canSurvive(level, blockPos) ? blockState : null;
    }

    @Override
    protected @NonNull VoxelShape getShape(BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH -> {
                return NORTH;
            }
            case SOUTH -> {
                return SOUTH;
            }
            case WEST -> {
                return WEST;
            }
            case EAST -> {
                return EAST;
            }
            default -> {
                return FLOAT;
            }
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        return createTickerHelper(type, getBlockEntityType(), FlankChimeBlockEntity::tick);
    }
}
