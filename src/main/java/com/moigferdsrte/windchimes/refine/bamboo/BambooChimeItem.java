package com.moigferdsrte.windchimes.refine.bamboo;

import net.minecraft.block.Block;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;

import java.util.function.Consumer;

public class BambooChimeItem extends VerticallyAttachableBlockItem {


    /**
     * @param standingBlock hieratical
     * @param wallBlock hieratical
     * @param verticalAttachmentDirection the direction of the item's vertical attachment, {@link Direction#UP} for hanging blocks
     *                                    and {@link Direction#DOWN} for standing blocks
     * @param settings hieratical
     */
    public BambooChimeItem(Block standingBlock, Block wallBlock, Direction verticalAttachmentDirection, Settings settings) {
        super(standingBlock, wallBlock, verticalAttachmentDirection, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        if (type.isCreative()) {
            textConsumer.accept((Text.translatable("tooltip.windchimes.bamboo_chime.info").formatted(Formatting.WHITE)));
        }
    }
}
