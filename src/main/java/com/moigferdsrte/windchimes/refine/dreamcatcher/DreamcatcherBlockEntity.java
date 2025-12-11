package com.moigferdsrte.windchimes.refine.dreamcatcher;

import com.moigferdsrte.windchimes.refine.BlockEntityReg;
import com.moigferdsrte.windchimes.refine.Refine;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class DreamcatcherBlockEntity extends BlockEntity {

    private final BlockState state;
    public int ringingTicks;
    public float strengthDivisor = 35f;
    protected int ticksToNextRing;
    protected int baselineRingTicks;
    protected boolean cachedTypeNeedsUpdate;
    protected final int tickDisplacement;

    public DreamcatcherBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityReg.DREAM, pos, state);
        this.ringingTicks = 0;
        this.ticksToNextRing = 40;
        this.baselineRingTicks = 0;
        this.cachedTypeNeedsUpdate = true;
        this.tickDisplacement = Math.abs(this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 6;
        this.state = state;
    }

    public void ring(boolean isLoud) {
        assert this.world != null;
        if(this.world.getBlockState(this.pos.down()).isAir())
            this.world.addSyncedBlockEvent(this.pos, this.world.getBlockState(this.pos).getBlock(), 1, isLoud? 1: 0);
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if(type == 1) {
            if (data == 0) {
                this.ringingTicks = 60;
                this.strengthDivisor = 35f;
                assert this.world != null;
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_AZALEA_LEAVES_STEP, SoundCategory.RECORDS,
                        0.12f + this.world.random.nextFloat() * 0.2f,
                        0.89f + this.world.random.nextFloat() * 0.4f);
            }
            else {
                this.strengthDivisor = 15f;
                this.ringingTicks = 140;
                assert this.world != null;
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_CHERRY_LEAVES_STEP, SoundCategory.RECORDS,
                        0.12f + this.world.random.nextFloat() * 0.2f,
                        0.89f + this.world.random.nextFloat() * 0.4f);
            }
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }


    private static void giveBuff(World world, BlockPos pos) {
        long timeOfDay = world.getTimeOfDay() % 24000;
        if (timeOfDay >= 12000){
            Box box = new Box(pos).expand(10).stretch(0.0, world.getHeight(), 0.0);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
            for (PlayerEntity playerEntity : list) {
                if (playerEntity.isSleeping()) {
                    if (Refine.config.enableDreamcatcherHeal)
                        playerEntity.setHealth(playerEntity.getMaxHealth());
                    if (playerEntity.getStackInHand(Hand.MAIN_HAND).isOf(Items.TOTEM_OF_UNDYING)
                            && Refine.config.enableEXPTotemBonus
                            && world instanceof ServerWorld serverWorld) {
                        Integer value = serverWorld.getGameRules().getValue(Refine.EXP_BONUS);
                        if (value != null) {
                            playerEntity.addExperience(value);
                        }
                        playerEntity.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 5, 1, true, false));
                    }
                }
                if (DreamcatcherBlock.hasBedNearby(world, pos)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 5, 1, true, false));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 5, 1, true, false));
                }
            }
        }
    }


    public static void clientTick(World world, BlockPos pos, BlockState state, DreamcatcherBlockEntity blockEntity) {
        giveBuff(world, pos);
        tick(world, pos, state, blockEntity);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, DreamcatcherBlockEntity blockEntity) {
        giveBuff(world, pos);
        tick(world, pos, state, blockEntity);
    }

    public static void tick(World world, BlockPos pos, BlockState blockState, DreamcatcherBlockEntity that) {
        if(world.isClient()) {
            if(that.ringingTicks > that.baselineRingTicks)
                that.ringingTicks--;
            if(that.ringingTicks < that.baselineRingTicks) {
                that.ringingTicks = that.baselineRingTicks;
            }
            if(world.isRaining()) {
                if(world.isThundering())
                    that.baselineRingTicks = 26;
                else
                    that.baselineRingTicks = 12;
            }
            else {
                if(world.isDay())
                    that.baselineRingTicks = 0;
                else
                    that.baselineRingTicks = 6;
            }
            return;
        }

        that.ticksToNextRing -= 1;
        if(that.ticksToNextRing <= 0 && world.getTime() % 6 == that.tickDisplacement) {
            if(world.isRaining()) {
                if(world.isThundering()) {
                    that.ticksToNextRing = world.random.nextInt(200);       // 0 - 10s
                    that.ring(world.random.nextInt(4) != 0);          // 75% chance of loudness
                }
                else {
                    that.ticksToNextRing = 100 + world.random.nextInt(400); // 5 - 25s
                    that.ring(world.random.nextInt(3) == 0);          // 33% chance of loudness
                }
            }
            else {
                if(world.isDay()) {
                    that.ticksToNextRing = 200 + world.random.nextInt(900); // 10s - 55s
                    that.ring(world.random.nextInt(5) == 0);          // 25% chance of loudness
                }
                else {
                    that.ticksToNextRing = 100 + world.random.nextInt(700); // 5 - 40s
                    that.ring(world.random.nextInt(5) == 0);          // 20% chance of loudness
                }
            }
        }
    }
}
