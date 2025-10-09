package com.moigferdsrte.windchimes.refine.dreamcatcher;

import com.moigferdsrte.windchimes.refine.utils.WoodType;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.function.Consumer;

public class DreamcatcherItem extends BlockItem {
    public DreamcatcherItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        if (type.isCreative()) {
            textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.info").formatted(Formatting.WHITE));
        }
        BlockStateComponent blockStateComponent = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        WoodType woodType = Objects.requireNonNullElse(blockStateComponent.getValue(DreamcatcherBlock.WOOD_TYPE), WoodType.OAK);
        switch (woodType) {
            case OAK -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.oak").formatted(Formatting.GRAY));
            case ACACIA -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.acacia").formatted(Formatting.GRAY));
            case BIRCH -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.birch").formatted(Formatting.GRAY));
            case DARK_OAK -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.dark_oak").formatted(Formatting.GRAY));
            case JUNGLE -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.jungle").formatted(Formatting.GRAY));
            case SPRUCE -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.spruce").formatted(Formatting.GRAY));
            case CHERRY -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.cherry").formatted(Formatting.GRAY));
            case PALE_OAK -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.pale_oak").formatted(Formatting.GRAY));
            default -> textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type.empty").formatted(Formatting.RED));
        }
    }
}
