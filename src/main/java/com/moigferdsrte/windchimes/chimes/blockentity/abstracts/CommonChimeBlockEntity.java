package com.moigferdsrte.windchimes.chimes.blockentity.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CommonChimeBlockEntity extends BlockEntity {
    public int ringingTicks;
    public float strengthDivisor = 35f;
    public int ticksToNextRing;
    protected int baselineRingTicks;
    protected boolean cachedTypeNeedsUpdate;
    protected final int tickDisplacement;

    public abstract SoundEvent getQuietSound();

    public abstract SoundEvent getLoudSound();

    public CommonChimeBlockEntity(final BlockEntityType<?> type, final BlockPos worldPosition, final BlockState blockState) {
        super(type, worldPosition, blockState);
        this.ringingTicks = 0;
        this.ticksToNextRing = 40;
        this.baselineRingTicks = 0;
        this.cachedTypeNeedsUpdate = true;
        this.tickDisplacement = Math.abs(this.worldPosition.getX() + this.worldPosition.getY() + this.worldPosition.getZ()) % 6;
    }

    public void ring(boolean isLoud) {
        if(this.getLevel() != null && this.getLevel().getBlockState(this.getBlockPos().below()).isAir())
            this.getLevel().blockEvent(this.getBlockPos(), this.getLevel().getBlockState(this.getBlockPos()).getBlock(), 1, isLoud? 1: 0);
    }


    @Override
    public boolean triggerEvent(int type, int data) {
        if (this.getLevel() == null) return super.triggerEvent(type, data);
        if(type == 1) {
            if (data == 0) {
                this.ringingTicks = 60;
                this.strengthDivisor = 35f;
                this.getLevel().playSound(null, this.getBlockPos(), getQuietSound(), SoundSource.RECORDS,
                        0.9f + this.getLevel().getRandom().nextFloat() * 0.2f,
                        0.8f + this.getLevel().getRandom().nextFloat() * 0.4f);
            }
            else {
                this.strengthDivisor = 55f;
                this.ringingTicks = 140;
                this.getLevel().playSound(null, this.getBlockPos(), getLoudSound(), SoundSource.RECORDS,
                        0.9f + this.getLevel().getRandom().nextFloat() * 0.2f,
                        0.8f + this.getLevel().getRandom().nextFloat() * 0.4f);
            }
            return true;
        }
        return super.triggerEvent(type, data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CommonChimeBlockEntity commonChimeBlockEntity) {
        if(level.isClientSide()) {
            if (commonChimeBlockEntity.ringingTicks > commonChimeBlockEntity.baselineRingTicks)
                commonChimeBlockEntity.ringingTicks--;
            if (commonChimeBlockEntity.ringingTicks < commonChimeBlockEntity.baselineRingTicks) {
                commonChimeBlockEntity.ringingTicks = commonChimeBlockEntity.baselineRingTicks;
            }
            if (level.isRaining()) {
                if (level.isThundering())
                    commonChimeBlockEntity.baselineRingTicks = 26;
                else
                    commonChimeBlockEntity.baselineRingTicks = 12;
            } else {
                if (level.isBrightOutside())
                    commonChimeBlockEntity.baselineRingTicks = 0;
                else
                    commonChimeBlockEntity.baselineRingTicks = 6;
            }
            return;
        }

        commonChimeBlockEntity.ticksToNextRing -= 1;
        if(commonChimeBlockEntity.ticksToNextRing <= 0 && level.getGameTime() % 6 == commonChimeBlockEntity.tickDisplacement) {
            if(level.isRaining()) {
                if(level.isThundering()) {
                    commonChimeBlockEntity.ticksToNextRing = level.getRandom().nextInt(200);       // 0 - 10s
                    commonChimeBlockEntity.ring(level.getRandom().nextInt(4) != 0);          // 75% chance of loudness
                }
                else {
                    commonChimeBlockEntity.ticksToNextRing = 100 + level.getRandom().nextInt(400); // 5 - 25s
                    commonChimeBlockEntity.ring(level.getRandom().nextInt(3) == 0);          // 33% chance of loudness
                }
            }
            else {
                if(level.isBrightOutside()) {
                    commonChimeBlockEntity.ticksToNextRing = 200 + level.getRandom().nextInt(900); // 10s - 55s
                    commonChimeBlockEntity.ring(level.getRandom().nextInt(5) == 0);          // 25% chance of loudness
                }
                else {
                    commonChimeBlockEntity.ticksToNextRing = 100 + level.getRandom().nextInt(700); // 5 - 40s
                    commonChimeBlockEntity.ring(level.getRandom().nextInt(5) == 0);          // 20% chance of loudness
                }
            }
        }
    }
}
