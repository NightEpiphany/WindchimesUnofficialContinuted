package com.moigferdsrte.windchimes.refine.copper;

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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CopperChimeBlock extends BlockWithEntity {

    public static final MapCodec<CopperChimeBlock> CODEC = createCodec(CopperChimeBlock::new);

    private static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 8.0, 4.0, 12.0, 16.0, 12.0);

    public CopperChimeBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityReg.COPPER, world.isClient() ? CopperChimeBlockEntity::clientTick : CopperChimeBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        CopperChimeBlockEntity here = ((CopperChimeBlockEntity)world.getBlockEntity(hit.getBlockPos()));
        if (here == null) return;
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
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.isAir(pos.up()) && world.isAir(pos.down())
                && !world.getBlockState(pos.up()).isOf(BlockReg.WALL_BAMBOO)
                && !world.getBlockState(pos.down()).isOf(BlockReg.WALL_BAMBOO)
                && !world.getBlockState(pos.up()).isOf(BlockReg.BAMBOO)
                && !world.getBlockState(pos.down()).isOf(BlockReg.BAMBOO)
                && !world.getBlockState(pos.up()).isOf(BlockReg.COPPER)
                && !world.getBlockState(pos.down()).isOf(BlockReg.COPPER)
                && !world.getBlockState(pos.up()).isOf(BlockReg.IRON)
                && !world.getBlockState(pos.down()).isOf(BlockReg.IRON);
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
        CopperChimeBlockEntity here = ((CopperChimeBlockEntity)world.getBlockEntity(pos));
        assert here != null;
        here.ring(!player.isSneaking());
        here.ticksToNextRing += 4;
        return ActionResult.SUCCESS_SERVER;
    }

    @Override
    protected MapCodec<CopperChimeBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperChimeBlockEntity(pos, state);
    }
}
