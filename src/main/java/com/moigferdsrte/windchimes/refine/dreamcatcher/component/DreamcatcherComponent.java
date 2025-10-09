package com.moigferdsrte.windchimes.refine.dreamcatcher.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Consumer;

public record DreamcatcherComponent(List<WoodTypes> woodTypes) implements TooltipAppender {

    public static final Codec<DreamcatcherComponent> CODEC = WoodTypes.LIST_CODEC.xmap(DreamcatcherComponent::new, DreamcatcherComponent::woodTypes);
    public static final PacketCodec<ByteBuf, DreamcatcherComponent> PACKET_CODEC = WoodTypes.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(DreamcatcherComponent::new, DreamcatcherComponent::woodTypes);
    public static final DreamcatcherComponent DEFAULT = new DreamcatcherComponent(List.of());

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        textConsumer.accept(Text.translatable("tooltip.windchimes.dreamcatcher_type." + this.woodTypes.getFirst().toString()).formatted(Formatting.GRAY));
    }
}
