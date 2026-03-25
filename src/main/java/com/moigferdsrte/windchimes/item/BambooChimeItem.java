package com.moigferdsrte.windchimes.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class BambooChimeItem extends StandingAndWallBlockItem {
    public BambooChimeItem(Block block, Block wallBlock, Direction attachmentDirection, Properties properties) {
        super(block, wallBlock, attachmentDirection, properties);
    }

    @Override
    protected @Nullable BlockState getPlacementState(@NonNull BlockPlaceContext context) {
        BlockState wallState = this.wallBlock.getStateForPlacement(context);
        if (context.getNearestLookingDirection().getAxis() == Direction.Axis.Y)
            return this.getBlock().defaultBlockState();
        return wallState;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack itemStack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        if (tooltipFlag.isCreative()) {
            builder.accept((Component.translatable("tooltip.windchimes.bamboo_chime.info").withStyle(ChatFormatting.WHITE)));
        }
    }
}
