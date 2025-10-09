package com.moigferdsrte.windchimes.refine.dreamcatcher;

import com.moigferdsrte.windchimes.refine.BlockEntityReg;
import com.moigferdsrte.windchimes.refine.BlockReg;
import com.moigferdsrte.windchimes.refine.utils.WoodType;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DreamcatcherBlock extends BlockWithEntity {

    public static final MapCodec<DreamcatcherBlock> CODEC = createCodec(DreamcatcherBlock::new);

    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public static final BooleanProperty OFFSET = BooleanProperty.of("offset");

    public static final EnumProperty<WoodType> WOOD_TYPE = EnumProperty.of("wood_type", WoodType.class);

    private static final VoxelShape SHAPE = Block.createCuboidShape(3.0, 4.0, 3.0, 13.0, 16.0, 13.0);

    public DreamcatcherBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WOOD_TYPE, WoodType.OAK).with(FACING, Direction.NORTH).with(OFFSET, false));
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world instanceof ServerWorld serverWorld
                && !player.isCreative()
                && serverWorld.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)
                && world.getBlockEntity(pos) instanceof DreamcatcherBlockEntity dreamcatcherBlockEntity) {
            WoodType woodType = state.get(WOOD_TYPE);
            ItemStack itemStack = new ItemStack(this);
            itemStack.applyComponentsFrom(dreamcatcherBlockEntity.createComponentMap());
            itemStack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(WOOD_TYPE, woodType));
            ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getPickStack(world, pos, state, includeData);
        if (includeData) {
            itemStack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(WOOD_TYPE, state.get(WOOD_TYPE)));
        }

        return itemStack;
    }

    private static boolean calculateOffset(ItemPlacementContext ctx) {
        int s = RotationPropertyHelper.fromYaw(ctx.getPlayerYaw());
        return s % 4 != 0;
    }

    private static Direction getFacing(ItemPlacementContext ctx) {
        Direction originalFacing = ctx.getHorizontalPlayerFacing().getOpposite();
        int s = RotationPropertyHelper.fromYaw(ctx.getPlayerYaw());
        if (originalFacing == Direction.EAST && s > 0 && s < 4) {
            return Direction.NORTH;
        }

        if (originalFacing == Direction.SOUTH && s > 4 && s < 8) {
            return Direction.EAST;
        }

        if (originalFacing == Direction.WEST && s > 8 && s < 12) {
            return Direction.SOUTH;
        }

        if (originalFacing == Direction.NORTH && s > 12) {
            return Direction.WEST;
        }
        return originalFacing;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, getFacing(ctx)).with(OFFSET, calculateOffset(ctx));
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(OFFSET);
        builder.add(WOOD_TYPE);
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityReg.DREAM, world.isClient() ? DreamcatcherBlockEntity::clientTick : DreamcatcherBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.isAir(pos.up()) && world.isAir(pos.down()) && this.noSameBlockNearby(world, pos);
    }

    public boolean noSameBlockNearby(BlockView world, BlockPos pos){
        BlockState north = world.getBlockState(pos.north());
        BlockState south = world.getBlockState(pos.south());
        BlockState west = world.getBlockState(pos.west());
        BlockState east = world.getBlockState(pos.east());
        return !north.isOf(BlockReg.DREAM) && !south.isOf(BlockReg.DREAM) && !west.isOf(BlockReg.DREAM) && !east.isOf(BlockReg.DREAM);
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

    public static boolean hasBedNearby(World world, BlockPos pos) {
        for (int i = pos.getY() - 2; i <= pos.getY() + 2; i++) {
            for (int j = pos.getX() - 3; j <= pos.getX() + 3; j++) {
                for (int k = pos.getZ() - 3; k <= pos.getZ() + 3; k++) {
                    BlockPos targetPos = pos.add(i, j, k);
                    if (world.getBlockState(targetPos).isIn(BlockTags.BEDS)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        DreamcatcherBlockEntity here = ((DreamcatcherBlockEntity)world.getBlockEntity(pos));
        assert here != null;
        here.ring(!player.isSneaking());
        here.ticksToNextRing += 4;
        return ActionResult.SUCCESS_SERVER;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Direction direction = state.get(DreamcatcherBlock.FACING);
        boolean flag = state.get(OFFSET);
        if (stack.isIn(ItemTags.LOGS) && Registries.ITEM.getId(stack.getItem()).getNamespace().equals("minecraft")) {
            world.playSound(null, pos, SoundEvents.UI_HUD_BUBBLE_POP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (stack.isOf(Items.OAK_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.OAK) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.OAK).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            if (stack.isOf(Items.ACACIA_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.ACACIA) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.ACACIA).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            if (stack.isOf(Items.BIRCH_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.BIRCH) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.BIRCH).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            if (stack.isOf(Items.DARK_OAK_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.DARK_OAK) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.DARK_OAK).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            if (stack.isOf(Items.JUNGLE_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.JUNGLE) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.JUNGLE).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            if (stack.isOf(Items.SPRUCE_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.SPRUCE) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.JUNGLE).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            if (stack.isOf(Items.CHERRY_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.CHERRY) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.CHERRY).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            if (stack.isOf(Items.PALE_OAK_LOG) && state.get(DreamcatcherBlock.WOOD_TYPE) != WoodType.PALE_OAK) {
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state.with(DreamcatcherBlock.WOOD_TYPE, WoodType.PALE_OAK).with(DreamcatcherBlock.FACING, direction).with(OFFSET, flag), Block.NOTIFY_ALL_AND_REDRAW);
            }
            return ActionResult.SUCCESS;
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected MapCodec<DreamcatcherBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DreamcatcherBlockEntity(pos, state);
    }
}
