package com.moigferdsrte.windchimes.refine.bamboo;

import com.moigferdsrte.windchimes.refine.BlockEntityReg;
import com.moigferdsrte.windchimes.refine.BlockReg;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BambooWallChimeBlock extends BlockWithEntity {

    public static final MapCodec<BambooWallChimeBlock> CODEC = createCodec(BambooWallChimeBlock::new);

    public static final EnumProperty<Direction> FACING = Properties.FACING;

    private static final VoxelShape SOUTH = VoxelShapes.union(
            VoxelShapes.cuboid(0.3125, 0, 0, 0.6875, 0.25, 0.125),
            VoxelShapes.cuboid(0.4375, 0.0625, 0.125, 0.5625, 0.1875, 0.75)
    );

    private static final VoxelShape NORTH = VoxelShapes.union(
            VoxelShapes.cuboid(0.3125, 0, 0.875, 0.6875, 0.25, 1),
            VoxelShapes.cuboid(0.4375, 0.0625, 0.25, 0.5625, 0.1875, 0.875)
    );

    private static final VoxelShape WEST = VoxelShapes.union(
            VoxelShapes.cuboid(0.875, 0, 0.3125, 1, 0.25, 0.6875),
            VoxelShapes.cuboid(0.25, 0.0625, 0.4375, 0.875, 0.1875, 0.5625)
    );

    private static final VoxelShape EAST = VoxelShapes.union(
            VoxelShapes.cuboid(0, 0, 0.3125, 0.125, 0.25, 0.6875),
            VoxelShapes.cuboid(0.125, 0.0625, 0.4375, 0.75, 0.1875, 0.5625)
    );

    private static final VoxelShape FLOAT = VoxelShapes.union(
            VoxelShapes.cuboid(0.3125, 0.125, 0.375, 0.6875, 0.25, 0.625)
    );

    public BambooWallChimeBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityReg.WALL_BAMBOO, world.isClient() ? BambooWallChimeBlockEntity::clientTick : BambooWallChimeBlockEntity::serverTick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<BambooWallChimeBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        BambooWallChimeBlockEntity here = ((BambooWallChimeBlockEntity)world.getBlockEntity(hit.getBlockPos()));
        assert here != null;
        here.ring(true);
        here.ticksToNextRing += 4;
        if (world instanceof ServerWorld serverWorld) {
            if (projectile instanceof ArrowEntity) {
                projectile.dropStack(serverWorld, new ItemStack(Items.ARROW));
                projectile.kill(serverWorld);
            }
        }
        super.onProjectileHit(world, state, hit, projectile);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
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

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction[] directions = ctx.getPlacementDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.with(FACING, direction2);
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlaceAt(world, pos, state.get(FACING)) && world.isAir(pos.down());
    }

    public static boolean canPlaceAt(WorldView world, BlockPos pos, Direction facing) {
        BlockPos blockPos = pos.offset(facing.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, facing)
                && world.isAir(pos.up())
                && !world.isAir(blockPos)
                && !world.getBlockState(pos.down()).isOf(BlockReg.WALL_BAMBOO)
                && !world.getBlockState(pos.up()).isOf(BlockReg.WALL_BAMBOO)
                && !world.getBlockState(pos.up()).isOf(BlockReg.BAMBOO);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        if (world.isAir(pos.up())) {
            Objects.requireNonNull(world.getBlockEntity(pos)).markRemoved();
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        if (world.getBlockEntity(pos) instanceof BlockEntity entity) {
            return entity.onSyncedBlockEvent(type, data);
        } else {
            return false;
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BambooWallChimeBlockEntity here = ((BambooWallChimeBlockEntity)world.getBlockEntity(pos));
        assert here != null;
        here.ring(!player.isSneaking());
        here.ticksToNextRing += 4;
        return ActionResult.SUCCESS_SERVER;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BambooWallChimeBlockEntity(pos, state);
    }
}
