package com.moigferdsrte.windchimes.refine.dreamcatcher.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.encoding.StringEncoding;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum WoodTypes implements StringIdentifiable {
    OAK("oak"),
    DARK_OAK("dark_oak"),
    BIRCH("birch"),
    JUNGLE("jungle"),
    ACACIA("acacia"),
    SPRUCE("spruce");

    public static final Codec<WoodTypes> CODEC = RecordCodecBuilder.create(woodTypesInstance ->
            woodTypesInstance.group(Codec.STRING.fieldOf("wood_id").forGetter(WoodTypes::toString)).apply(woodTypesInstance, WoodTypes::valueOf)
    );

    public static final Codec<List<WoodTypes>> LIST_CODEC = CODEC.listOf();

    public static final PacketCodec<ByteBuf, WoodTypes> PACKET_CODEC = new PacketCodec<>() {
        public WoodTypes decode(ByteBuf byteBuf) {return WoodTypes.valueOf(StringEncoding.decode(byteBuf, 10));}
        public void encode(ByteBuf byteBuf, WoodTypes types) {
            StringEncoding.encode(byteBuf, types.toString(), 10);
        }
    };

    private final String wood;

    WoodTypes(String name) {
        this.wood = name;
    }

    public static WoodTypes getFromInt(int i) {
        switch (i) {
            case 0 -> {
                return WoodTypes.OAK;
            }
            case 1 -> {
                return WoodTypes.ACACIA;
            }
            case 2 -> {
                return WoodTypes.BIRCH;
            }
            case 3 -> {
                return WoodTypes.DARK_OAK;
            }
            case 4 -> {
                return WoodTypes.JUNGLE;
            }
            default -> {
                return WoodTypes.SPRUCE;
            }
        }
    }

    @Override
    public String asString() {
        return this.wood;
    }

    @Override
    public @NotNull String toString() {
        return this.asString();
    }
}
